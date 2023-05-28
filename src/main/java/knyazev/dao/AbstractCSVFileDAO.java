package knyazev.dao;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractCSVFileDAO<I, E extends IdentityInterface<I>> implements DAO<I, E> {


    private final CSVPrinter csvPrinter;
    private String table;

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.builder().build();

    protected AbstractCSVFileDAO(String table) throws IOException {
        this.csvPrinter = new CSVPrinter(new FileWriter(table + ".csv", true), CSV_FORMAT);
        this.table = table;
    }

    protected void putInCSVFile(I identity, Object... params) throws IOException {
        var record = this.seekIdentity(identity);
        if (record.isPresent()) {
            throw new IllegalThreadStateException("Duplicate key = " + identity);
        }

        this.csvPrinter.printRecord(Stream.concat(Stream.of(identity), Stream.of(params)));
        this.csvPrinter.flush();
    }

    protected Optional<Object[]> getFromCSV(I identity) throws IOException {
        var record = this.seekIdentity(identity);
        return record.map(CSVRecord::values);

    }

    private Optional<CSVRecord> seekIdentity(I identity) throws IOException {
        try (var parser = new CSVParser(new BufferedReader(new FileReader(table + ".csv")), CSV_FORMAT)) {
            return parser.getRecords().stream().filter(r -> Long.valueOf(r.get(0)) == identity).findFirst();
        }
    }
}

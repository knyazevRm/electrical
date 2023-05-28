package knyazev.dao;

import knyazev.model.Element;
import knyazev.model.SubCircuit;
import knyazev.reflection.DependencyInjection;
import org.jetbrains.annotations.NotNull;
import knyazev.reflection.Component;
import knyazev.utils.Utils;

import java.io.IOException;
import java.util.*;


@Component
public class SubCircuitDaoCSV extends AbstractCSVFileDAO<Long, SubCircuit> {

    @DependencyInjection
    private ElementDaoCSV elementDaoCSV;

    public SubCircuitDaoCSV() throws IOException {
        super("subCircuitTable");
    }

    @Override
    public void put(@NotNull SubCircuit object) {
        var key = object.getIdentity();
        var fields = new Object[] {
                Utils.objectsToList(object.getElements())
        };

        try {
            this.putInCSVFile(key, fields);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<SubCircuit> get(Long key) {
        Optional<Object[]> fieldsInternal;
        try {
            fieldsInternal = this.getFromCSV(key);
        } catch (IOException e) {
            throw  new IllegalStateException(e);
        }
        
        if (fieldsInternal.isEmpty()) {
            return  Optional.empty();
        }


        var fields = fieldsInternal.get();
        List<Long> idElements = Utils.strToList((String) fields[1]);
        List<Element> elements = new ArrayList<>();
        for (Long id: idElements) {
            elements.add(elementDaoCSV.get(id).get());
        }
        SubCircuit subCircuit = new SubCircuit();
        subCircuit.setElements(elements);
        subCircuit.setIdentity(key);
        return Optional.of(subCircuit);
    }

}

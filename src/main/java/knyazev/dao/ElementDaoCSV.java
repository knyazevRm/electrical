package knyazev.dao;

import knyazev.model.Element;
import knyazev.model.ElementType;
import knyazev.reflection.Component;
import knyazev.reflection.DependencyInjection;

import java.io.IOException;
import java.util.Optional;

@Component
public class ElementDaoCSV extends AbstractCSVFileDAO<Long, Element> {

    public ElementDaoCSV() throws IOException {
        super("elementTable");
    }

    @Override
    public void put(Element object) {
        var key = object.getIdentity();
        var fields = new Object[] {
                object.getAmperage(),
                object.getResistance(),
                object.getType().ordinal()
        };

        try {
            this.putInCSVFile(key, fields);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<Element> get(Long key) {
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
        var element = new Element();
        element.setAmperage(Integer.parseInt((String) fields[1]));
        element.setResistance(Integer.parseInt((String) fields[2]));
        element.setType(ElementType.values()[Integer.parseInt((String) fields[3])]);

        element.setIdentity(key);
        return Optional.of(element);
    }
}

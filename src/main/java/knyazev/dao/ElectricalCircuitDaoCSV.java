package knyazev.dao;

import knyazev.model.SubCircuit;
import knyazev.model.ElectricalCircuit;
import knyazev.reflection.Component;
import knyazev.reflection.DependencyInjection;
import knyazev.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ElectricalCircuitDaoCSV extends AbstractCSVFileDAO<Long, ElectricalCircuit> {

    @DependencyInjection
    private SubCircuitDaoCSV subCircuitDaoCSV;
    public ElectricalCircuitDaoCSV() throws IOException {
        super("elementCircuitTable");
    }

    @Override
    public void put(ElectricalCircuit object) {
        var key = object.getIdentity();
        var fields = new Object[] {
                Utils.objectsToList(object.getSubCircuits())
        };

        try {
            this.putInCSVFile(key, fields);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<ElectricalCircuit> get(Long key) {
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
        List<Long> idSubCircuits = Utils.strToList((String) fields[1]);
        List<SubCircuit> subCircuits = new ArrayList<>();
        for (Long id: idSubCircuits) {
            subCircuits.add(subCircuitDaoCSV.get(id).get());
        }
        var electricalCircuit = new ElectricalCircuit();
        electricalCircuit.setSubCircuits(subCircuits);
        electricalCircuit.setIdentity(key);
        return Optional.of(electricalCircuit);
    }
}

package knyazev;

import knyazev.dao.ElementDaoCSV;
import knyazev.dao.SubCircuitDaoCSV;
import knyazev.dao.ElectricalCircuitDaoCSV;
import knyazev.model.Element;
import knyazev.model.ElementType;
import knyazev.model.SubCircuit;
import knyazev.model.ElectricalCircuit;
import knyazev.reflection.ApplicationContext;
import knyazev.reflection.DependencyInjection;

public class MainCSV {

    @DependencyInjection
    private static SubCircuitDaoCSV subCircuitDaoCSV;

    @DependencyInjection
    private static ElementDaoCSV elementDaoCSV;

    @DependencyInjection
    private static ElectricalCircuitDaoCSV electricalCircuitDaoCSV;

    static {
        try {
            ApplicationContext.injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Element element = new Element();
        element.setIdentity(1L);
        SubCircuit subCircuit1 = new SubCircuit();
        subCircuit1.getElements().add(element);
        subCircuit1.setIdentity(1L);
        SubCircuit subCircuit2 = new SubCircuit();
        subCircuit2.setIdentity(2L);
        ElectricalCircuit electricalCircuit = new ElectricalCircuit();
        electricalCircuit.setIdentity(1L);
        electricalCircuit.getSubCircuits().add(subCircuit1);
        electricalCircuit.getSubCircuits().add(subCircuit2);

        element.setResistance(11);
        element.setAmperage(21);
        element.setType(ElementType.LIGHT_BULB);

        subCircuitDaoCSV.put(subCircuit1);
        subCircuitDaoCSV.put(subCircuit2);

        electricalCircuitDaoCSV.put(electricalCircuit);

        elementDaoCSV.put(element);

        System.out.println(subCircuitDaoCSV.get(1L));
        System.out.println(subCircuitDaoCSV.get(2L));

        System.out.println(electricalCircuitDaoCSV.get(1L));

        System.out.println(elementDaoCSV.get(1L));
    }
}

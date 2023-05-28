package knyazev;

import knyazev.dao.*;
import knyazev.model.ElectricalCircuit;
import knyazev.model.Element;
import knyazev.model.ElementType;
import knyazev.model.SubCircuit;
import knyazev.reflection.ApplicationContext;
import knyazev.reflection.DependencyInjection;

public class Main {

    @DependencyInjection
    private static SubCircuitDao subCircuitDao;

    @DependencyInjection
    private static ElementDao elementDao;

    @DependencyInjection
    private static ElectricalCircuitDao electricalCircuitDao;

    static {
        try {
            ApplicationContext.injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Element element = new Element();
        element.setResistance(11);
        element.setAmperage(21);
        element.setType(ElementType.LIGHT_BULB);
        elementDao.put(element);


        SubCircuit subCircuit1 = new SubCircuit();
        element = elementDao.get(1L).get();
        subCircuit1.getElements().add(element);
        subCircuitDao.put(subCircuit1);

        SubCircuit subCircuit2 = new SubCircuit();
        subCircuitDao.put(subCircuit2);


        ElectricalCircuit electricalCircuit = new ElectricalCircuit();
        electricalCircuit.getSubCircuits().add(subCircuitDao.get(1L).get());
        electricalCircuit.getSubCircuits().add(subCircuitDao.get(2L).get());
        electricalCircuitDao.put(electricalCircuit);


        System.out.println(electricalCircuitDao.get(1L));
    }
}

package knyazev.dao;

import knyazev.model.SubCircuit;
import knyazev.model.ElectricalCircuit;
import knyazev.reflection.Component;
import knyazev.reflection.DependencyInjection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class ElectricalCircuitDao implements DAO<Long, ElectricalCircuit> {

    private Connection connection;

    @DependencyInjection
    private SubCircuitDao subCircuitDao;


    public void setConnection(Connection connection) {
        this.connection = connection;
        subCircuitDao.setConnection(connection);
    }

    @Override
    public void put(ElectricalCircuit object) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"electricalCircuit\" DEFAULT VALUES RETURNING id");

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");


            for (SubCircuit subCircuit : object.getSubCircuits()) {
                subCircuitDao.update(subCircuit.getIdentity(), id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ElectricalCircuit> get(Long key) {
        ElectricalCircuit electricalCircuit = new ElectricalCircuit();
        electricalCircuit.setIdentity(key);
        List<Optional<SubCircuit>> subCircuits = subCircuitDao.getByElectricalCircuit(key);

        for (Optional<SubCircuit> subCircuit : subCircuits) {
            electricalCircuit.getSubCircuits().add(subCircuit.get());
        }

        return Optional.of(electricalCircuit);
    }


}

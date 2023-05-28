package knyazev.dao;

import knyazev.model.Element;
import knyazev.model.SubCircuit;
import knyazev.reflection.Component;
import knyazev.reflection.DependencyInjection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class SubCircuitDao implements DAO<Long, SubCircuit> {

    private Connection connection;

    @DependencyInjection
    private ElementDao elementDao;


    public void setConnection(Connection connection) {
        this.connection = connection;
        elementDao.setConnection(connection);
    }

    @Override
    public void put(SubCircuit object) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"subCircuit\" (electricalCircuit_id) VALUES (null) RETURNING id");

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");


            for (Element element : object.getElements()) {
                elementDao.update(element.getIdentity(), id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Long id, Long electricalCircuit) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE \"subCircuit\" SET electricalCircuit_id = ?  WHERE id = ?");

            statement.setInt(1, electricalCircuit.intValue());
            statement.setInt(2, id.intValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SubCircuit> get(Long key) {
        SubCircuit subCircuit = new SubCircuit();
        subCircuit.setIdentity(key);
        List<Optional<Element>> elements = elementDao.getBySubCircuit(key);

        for (Optional<Element> element : elements) {
            subCircuit.getElements().add(element.get());
        }

        return Optional.of(subCircuit);

    }

    public List<Optional<SubCircuit>> getByElectricalCircuit(Long key) {
        List<Optional<SubCircuit>> subCircuit = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"subCircuit\" WHERE electricalCircuit_id = ?");
            statement.setInt(1, key.intValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                SubCircuit subCircuit1 = get(id).get();

                subCircuit.add(Optional.of(subCircuit1));
            }


            return subCircuit;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

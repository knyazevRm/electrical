package knyazev.dao;
import knyazev.model.Element;
import knyazev.model.ElementType;
import knyazev.reflection.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ElementDao implements DAO<Long, Element> {

    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void put(Element object) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"element\" (amperage, resistance, type) VALUES (?, ?, ?)");

            statement.setInt(1, object.getAmperage());
            statement.setInt(2, object.getResistance());
            statement.setInt(3, object.getType().ordinal());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Long id, Long subCircuit) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE \"element\" SET subCircuit_id = ?  WHERE id = ?");

            statement.setInt(1, subCircuit.intValue());
            statement.setInt(2, id.intValue());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Element> get(Long key) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"element\" WHERE id = ?");
            statement.setInt(1, key.intValue());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            int amperage = resultSet.getInt("amperage");
            int resistance = resultSet.getInt("resistance");
            ElementType type = ElementType.values()[resultSet.getInt("type")];
            Element element = new Element();
            element.setResistance(resistance);
            element.setAmperage(amperage);
            element.setType(type);
            element.setIdentity(id);

            return Optional.of(element);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Optional<Element>> getBySubCircuit(Long key) {
        List<Optional<Element>> elements = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"element\" WHERE subCircuit_id = ?");
            statement.setInt(1, key.intValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                int amperage = resultSet.getInt("amperage");
                int resistance = resultSet.getInt("resistance");
                ElementType type = ElementType.values()[resultSet.getInt("type")];
                Element element = new Element();
                element.setAmperage(amperage);
                element.setResistance(resistance);
                element.setType(type);
                element.setIdentity(id);

                elements.add(Optional.of(element));
            }


            return elements;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

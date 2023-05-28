package knyazev;
import knyazev.controller.ElementController;
import knyazev.controller.SubCircuitController;
import knyazev.controller.ElectricalCircuitController;
import knyazev.reflection.ApplicationContext;
import knyazev.reflection.DependencyInjection;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainServer {


    @DependencyInjection
    private static ElementController elementController;

    @DependencyInjection
    private static SubCircuitController subCircuitController;

    @DependencyInjection
    private static ElectricalCircuitController electricalCircuitController;

    private static final String URL = "jdbc:postgresql://localhost:9100/electricalCircuit";
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "root";

    static {
        try {
            ApplicationContext.injectDependencies();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        elementController.getDao().setConnection(connection);
        subCircuitController.getDao().setConnection(connection);
        electricalCircuitController.getDao().setConnection(connection);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/api");

        servletContextHandler.addServlet(new ServletHolder(elementController), "/element");
        servletContextHandler.addServlet(new ServletHolder(subCircuitController), "/sub");
        servletContextHandler.addServlet(new ServletHolder(electricalCircuitController), "/electrical");

        server.setHandler(servletContextHandler);

        server.start();
        server.join();
    }
}

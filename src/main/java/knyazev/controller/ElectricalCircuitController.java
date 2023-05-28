package knyazev.controller;

import knyazev.model.SubCircuit;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knyazev.dao.ElectricalCircuitDao;
import knyazev.model.ElectricalCircuit;
import knyazev.reflection.Component;
import knyazev.reflection.DependencyInjection;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@WebServlet("/electrical")
public class ElectricalCircuitController extends HttpServlet {
    @DependencyInjection
    private ElectricalCircuitDao dao;

    public ElectricalCircuitDao getDao() {
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Long id = Long.parseLong(request.getParameter("id"));
        ElectricalCircuit electricalCircuit = dao.get(id).get();

        PrintWriter out = response.getWriter();
        String json = new JSONObject(electricalCircuit).toString();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuffer buffer = new StringBuffer();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            buffer.append(inputLine);
        }
        JSONObject jsonObject = new JSONObject(buffer.toString());
        Map<String, Object> map = jsonObject.toMap();

        List<SubCircuit> subCircuits = new ArrayList<>();
        for (Object id: (List<Object>) map.get("subCircuits")) {
            SubCircuit subCircuit = new SubCircuit();
            subCircuit.setIdentity(Long.parseLong((String) id));
            subCircuits.add(subCircuit);
        }

        ElectricalCircuit electricalCircuit = new ElectricalCircuit();
        electricalCircuit.setSubCircuits(subCircuits);

        dao.put(electricalCircuit);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject(electricalCircuit).toString());

    }
}

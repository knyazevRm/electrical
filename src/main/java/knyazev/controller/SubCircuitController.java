package knyazev.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knyazev.dao.SubCircuitDao;
import knyazev.model.Element;
import knyazev.model.SubCircuit;
import knyazev.reflection.Component;
import knyazev.reflection.DependencyInjection;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
@WebServlet("/sub")
public class SubCircuitController extends HttpServlet {

    @DependencyInjection
    private SubCircuitDao dao;

    public SubCircuitDao getDao() {
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        SubCircuit subCircuit = dao.get(id).get();
        PrintWriter out = response.getWriter();
        String json = new JSONObject(subCircuit).toString();
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

        List<Element> elements = new ArrayList<>();
        for (Object id: (List<Object>) map.get("elements")) {
            Element element = new Element();
            element.setIdentity(Long.parseLong((String) id));
            elements.add(element);
        }

        SubCircuit subCircuit = new SubCircuit();
        subCircuit.setElements(elements);

        dao.put(subCircuit);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject(subCircuit).toString());

    }
}

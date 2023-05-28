package knyazev.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knyazev.dao.ElementDao;
import knyazev.model.Element;
import knyazev.model.ElementType;
import knyazev.reflection.Component;
import knyazev.reflection.DependencyInjection;
import org.json.JSONObject;


import javax.servlet.annotation.WebServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@WebServlet("/element")
public class ElementController extends HttpServlet {

    @DependencyInjection
    private ElementDao dao;

    public ElementDao getDao() {
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        Element element = dao.get(id).get();

        PrintWriter out = response.getWriter();
        String json = new JSONObject(element).toString();
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
        Element element = new Element();
        element.setAmperage(Integer.parseInt((String)map.get("amperage")));
        element.setResistance(Integer.parseInt((String)map.get("resistance")));
        element.setType(ElementType.values()[Integer.parseInt((String)map.get("type"))]);
        dao.put(element);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new JSONObject(element).toString());

    }
}

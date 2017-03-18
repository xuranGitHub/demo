package com.baturu.demo.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xuran on 2016/12/19.
 */
@WebServlet(value = "/submitBeer.do", name = "submitBeer")
public class SubmitFormServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Beer Selection Advice<br>");
        String c = request.getParameter("color");
        out.println("<br>Got beer color " + c);
        request.setAttribute("styles", c);
        RequestDispatcher view = request.getRequestDispatcher("/jsp/result.jsp");
        view.forward(request, response);
    }
}

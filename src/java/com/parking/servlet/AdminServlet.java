package com.parking.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles admin-related requests.
 * Author: Abishek
 */
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>Admin Servlet</title></head>");
            out.println("<body>");
            out.println("<h1>Admin Servlet - GET Method</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head><title>Admin Servlet</title></head>");
            out.println("<body>");
            out.println("<h1>Admin Servlet - POST Method</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles admin-related operations.";
    }
}

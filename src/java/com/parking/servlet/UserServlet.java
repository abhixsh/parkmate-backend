package com.parking.servlet;

import com.parking.dao.UserDao;
import com.parking.model.User;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/parkmate/user")
public class UserServlet extends HttpServlet {

    private UserDao userDao;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        userDao = new UserDao();
        gson = new Gson();
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Read JSON data from request
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            // Parse JSON to User object
            User user = gson.fromJson(jsonBuilder.toString(), User.class);

            // Validate user data
            if (!isValidUser(user)) {
                String errorJson = gson.toJson(new JsonResponse("Invalid user data provided.", false));
                sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, errorJson);
                return;
            }

            // Register user
            boolean success = userDao.registerUser(user);

            if (success) {
                String successJson = gson.toJson(new JsonResponse("User registered successfully.", true));
                sendResponse(response, HttpServletResponse.SC_OK, successJson);
            } else {
                String errorJson = gson.toJson(new JsonResponse("Error registering user.", false));
                sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorJson = gson.toJson(new JsonResponse("An unexpected error occurred: " + e.getMessage(), false));
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
        }
    }

    private void sendResponse(HttpServletResponse response, int status, String jsonResponse) throws IOException {
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

// Helper class for JSON responses
    private static class JsonResponse {

        private String message;
        private boolean success;

        public JsonResponse(String message, boolean success) {
            this.message = message;
            this.success = success;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.write("{\"message\": \"" + message + "\"}");
        out.flush();
    }

    private boolean isValidUser(User user) {
        return user != null
                && user.getFullName() != null && !user.getFullName().trim().isEmpty()
                && user.getEmail() != null && !user.getEmail().trim().isEmpty()
                && user.getPassword() != null && !user.getPassword().trim().isEmpty();
    }
}

package com.parking.servlet;

import com.parking.dao.AdminDao;
import com.parking.model.Admin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/parkmate/admin/*")
public class AdminServlet extends HttpServlet {

    private AdminDao adminDao;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        adminDao = new AdminDao();
        gson = new Gson();
    }

   private void setCorsHeaders(HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173"); // Allow frontend origin
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept"); // Add Content-Type here
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Max-Age", "3600");
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

    String pathInfo = request.getPathInfo();
   
    if (pathInfo != null && pathInfo.equals("/login")) {
        
        handleLogin(request, response);
    } else {
        handleRegistration(request, response);
    }
}


    private void handleRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            Admin admin = gson.fromJson(jsonBuilder.toString(), Admin.class);

            if (!isValidAdmin(admin)) {
                String errorJson = gson.toJson(new JsonResponse("Invalid admin data provided.", false, null));
                sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, errorJson);
                return;
            }

            boolean success = adminDao.registerAdmin(admin);

            if (success) {
                String successJson = gson.toJson(new JsonResponse("Admin registered successfully.", true, null));
                sendResponse(response, HttpServletResponse.SC_OK, successJson);
            } else {
                String errorJson = gson.toJson(new JsonResponse("Error registering admin.", false, null));
                sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorJson = gson.toJson(new JsonResponse("An unexpected error occurred: " + e.getMessage(), false, null));
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JsonObject credentials = gson.fromJson(jsonBuilder.toString(), JsonObject.class);
            String email = credentials.get("email").getAsString();
            String password = credentials.get("password").getAsString();

            Admin admin = adminDao.validateLogin(email, password);

            if (admin != null) {
                HttpSession session = request.getSession();
                session.setAttribute("adminId", admin.getAdminId());
                session.setAttribute("adminEmail", admin.getEmail());

                JsonObject adminJson = new JsonObject();
                adminJson.addProperty("adminId", admin.getAdminId());
                adminJson.addProperty("email", admin.getEmail());
                adminJson.addProperty("role", admin.getRole());

                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Login successful");
                responseJson.addProperty("success", true);
                responseJson.add("admin", adminJson);

                sendResponse(response, HttpServletResponse.SC_OK, gson.toJson(responseJson));
            } else {
                String errorJson = gson.toJson(new JsonResponse("Invalid email or password", false, null));
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, errorJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorJson = gson.toJson(new JsonResponse("An unexpected error occurred: " + e.getMessage(), false, null));
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
        }
    }

    private void sendResponse(HttpServletResponse response, int status, String jsonResponse) throws IOException {
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    private static class JsonResponse {

        private String message;
        private boolean success;
        private Integer adminId;

        public JsonResponse(String message, boolean success, Integer adminId) {
            this.message = message;
            this.success = success;
            this.adminId = adminId;
        }
    }

    private boolean isValidAdmin(Admin admin) {
        return admin != null
                && admin.getEmail() != null && !admin.getEmail().trim().isEmpty()
                && admin.getPassword() != null && !admin.getPassword().trim().isEmpty();
    }
}

package com.parking.servlet;

import com.parking.dao.UserDao;
import com.parking.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/parkmate/user/*")
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
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS"); 
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && pathInfo.equals("/")) {
                List<User> users = userDao.getAllUsers();
                String usersJson = gson.toJson(users);
                sendResponse(response, HttpServletResponse.SC_OK, usersJson);
            } else {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    int userId = Integer.parseInt(pathParts[1]);
                    User user = userDao.getUserById(userId);
                    if (user != null) {
                        String userJson = gson.toJson(user);
                        sendResponse(response, HttpServletResponse.SC_OK, userJson);
                    } else {
                        String errorJson = gson.toJson(new JsonResponse("User not found", false, null));
                        sendResponse(response, HttpServletResponse.SC_NOT_FOUND, errorJson);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorJson = gson.toJson(new JsonResponse("Failed to fetch users: " + e.getMessage(), false, null));
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
        }
    }

    private void sendResponse(HttpServletResponse response, int status, String jsonResponse) throws IOException {
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
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

            User user = gson.fromJson(jsonBuilder.toString(), User.class);

            if (!isValidUser(user)) {
                String errorJson = gson.toJson(new JsonResponse("Invalid user data provided.", false, null));
                sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, errorJson);
                return;
            }

            boolean success = userDao.registerUser(user);

            if (success) {
                String successJson = gson.toJson(new JsonResponse("User registered successfully.", true, null));
                sendResponse(response, HttpServletResponse.SC_OK, successJson);
            } else {
                String errorJson = gson.toJson(new JsonResponse("Error registering user.", false, null));
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

            User user = userDao.validateLogin(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("userEmail", user.getEmail());

                JsonObject userJson = new JsonObject();
                userJson.addProperty("id", user.getId());
                userJson.addProperty("fullName", user.getFullName());
                userJson.addProperty("email", user.getEmail());
                userJson.addProperty("phoneNumber", user.getPhoneNumber());

                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("message", "Login successful");
                responseJson.addProperty("success", true);
                responseJson.add("user", userJson);

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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null) {
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    int userId = Integer.parseInt(pathParts[1]);
                    boolean deleted = userDao.deleteUser(userId);

                    if (deleted) {
                        String successJson = gson.toJson(new JsonResponse("User deleted successfully.", true, userId));
                        sendResponse(response, HttpServletResponse.SC_OK, successJson);
                    } else {
                        String errorJson = gson.toJson(new JsonResponse("Error deleting user.", false, null));
                        sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorJson = gson.toJson(new JsonResponse("An error occurred while deleting the user: " + e.getMessage(), false, null));
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorJson);
        }
    }

    private boolean isValidUser(User user) {
        return user != null
                && user.getFullName() != null && !user.getFullName().trim().isEmpty()
                && user.getEmail() != null && !user.getEmail().trim().isEmpty()
                && user.getPassword() != null && !user.getPassword().trim().isEmpty();
    }

    private static class JsonResponse {

        private String message;
        private boolean success;
        private Integer userId;

        public JsonResponse(String message, boolean success, Integer userId) {
            this.message = message;
            this.success = success;
            this.userId = userId;
        }
    }
}

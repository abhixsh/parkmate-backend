package com.parking.servlet;

import com.parking.dao.ReservationDao;
import com.parking.model.Reservation;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/parkmate/reservation/*")
public class ReservationServlet extends HttpServlet {

    private ReservationDao reservationDao;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        reservationDao = new ReservationDao();
        gson = new Gson();
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // Handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo();

        // Check if the URL contains a reservation ID (e.g. /reservation/{id})
        if (pathInfo != null && pathInfo.startsWith("/")) {
            try {
                int reservationId = Integer.parseInt(pathInfo.substring(1));
                Reservation reservation = reservationDao.getReservationById(reservationId);
                if (reservation != null) {
                    sendResponse(response, HttpServletResponse.SC_OK, gson.toJson(reservation));
                } else {
                    sendErrorResponse(response, "Reservation not found.");
                }
            } catch (NumberFormatException e) {
                sendErrorResponse(response, "Invalid reservation ID format.");
            }
        } else {
            // If no specific ID is provided, fetch all reservations
            List<Reservation> reservations = reservationDao.getAllReservations();
            if (reservations != null && !reservations.isEmpty()) {
                sendResponse(response, HttpServletResponse.SC_OK, gson.toJson(reservations));
            } else {
                sendErrorResponse(response, "No reservations found.");
            }
        }
    }

    // Handle POST requests for creating a reservation
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");

        try {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            Reservation reservation = gson.fromJson(jsonBuilder.toString(), Reservation.class);

            if (reservation == null) {
                sendErrorResponse(response, "Invalid data for reservation.");
                return;
            }

            boolean success = reservationDao.addReservation(reservation);

            if (success) {
                sendResponse(response, HttpServletResponse.SC_CREATED, "Reservation created successfully.");
            } else {
                sendErrorResponse(response, "Failed to create reservation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "An unexpected error occurred while creating the reservation.");
        }
    }

    // Handle PUT requests for updating a reservation
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");

        try {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            Reservation reservation = gson.fromJson(jsonBuilder.toString(), Reservation.class);

            if (reservation == null) {
                sendErrorResponse(response, "Invalid data for reservation update.");
                return;
            }

            boolean success = reservationDao.updateReservation(reservation);

            if (success) {
                sendResponse(response, HttpServletResponse.SC_OK, "Reservation updated successfully.");
            } else {
                sendErrorResponse(response, "Failed to update reservation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "An unexpected error occurred while updating the reservation.");
        }
    }

    // Handle DELETE requests for deleting a reservation
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setContentType("application/json");

        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.startsWith("/")) {
                int reservationId = Integer.parseInt(pathInfo.substring(1));
                boolean success = reservationDao.deleteReservation(reservationId);
                if (success) {
                    sendResponse(response, HttpServletResponse.SC_OK, "Reservation deleted successfully.");
                } else {
                    sendErrorResponse(response, "Failed to delete reservation.");
                }
            } else {
                sendErrorResponse(response, "Invalid reservation ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "An unexpected error occurred while deleting the reservation.");
        }
    }

    // Send a successful response with proper JSON format
    private void sendResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        PrintWriter out = response.getWriter();
        out.print("{\"message\": " + message + "}");  // Ensure the message is properly wrapped in quotes for JSON
        out.flush();
    }

    // Send an error response with proper JSON format
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = response.getWriter();
        out.print("{\"error\": \"" + message + "\"}");
        out.flush();
    }
}

package com.parking.servlet;

import com.parking.dao.ParkingSpotDao;
import com.parking.model.ParkingSpot;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/parkingspot/*")
public class ParkingSpotServlet extends HttpServlet {
    private ParkingSpotDao parkingSpotDao;
    private Gson gson = new Gson();

    @Override
    public void init() {
        parkingSpotDao = new ParkingSpotDao();
    }

    // Method to set CORS Headers
    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");

        if (pathInfo == null || pathInfo.equals("/")) {
            // Fetch all parking spots
            try {
                response.getWriter().write(gson.toJson(parkingSpotDao.getAllParkingSpots()));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(gson.toJson("Failed to fetch parking spots"));
            }
        } else {
            // Fetch a specific parking spot by ID
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                ParkingSpot spot = parkingSpotDao.getParkingSpotById(id);
                if (spot != null) {
                    response.getWriter().write(gson.toJson(spot));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write(gson.toJson("Parking spot not found"));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Invalid parking spot ID"));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        try {
            // Parse request body to create a new parking spot
            ParkingSpot spot = gson.fromJson(request.getReader(), ParkingSpot.class);
            boolean success = parkingSpotDao.addParkingSpot(spot);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(success));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Failed to add parking spot"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        try {
            // Parse the parking spot id from the path
            int id = Integer.parseInt(request.getPathInfo().substring(1));
            ParkingSpot spot = gson.fromJson(request.getReader(), ParkingSpot.class);
            spot.setId(id);
            boolean success = parkingSpotDao.updateParkingSpot(spot);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(success));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Failed to update parking spot"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        try {
            int id = Integer.parseInt(request.getPathInfo().substring(1));
            boolean success = parkingSpotDao.deleteParkingSpot(id);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(success));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Failed to delete parking spot"));
        }
    }
}

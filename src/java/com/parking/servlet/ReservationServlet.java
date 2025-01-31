package com.parking.servlet;

import com.parking.dao.ReservationDao;
import com.parking.model.Reservation;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/parkmate/reservation/*")
public class ReservationServlet extends HttpServlet {
    private ReservationDao reservationDao;
    private Gson gson = new Gson();

    @Override
    public void init() {
        reservationDao = new ReservationDao();
    }

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
            try {
                response.getWriter().write(gson.toJson(reservationDao.getAllReservations()));
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(gson.toJson("Failed to fetch reservations"));
            }
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Reservation reservation = reservationDao.getReservationById(id);
                if (reservation != null) {
                    response.getWriter().write(gson.toJson(reservation));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write(gson.toJson("Reservation not found"));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson("Invalid reservation ID"));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        try {
            Reservation reservation = gson.fromJson(request.getReader(), Reservation.class);
            boolean success = reservationDao.addReservation(reservation);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(success));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Failed to add reservation"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        try {
            int id = Integer.parseInt(request.getPathInfo().substring(1));
            Reservation reservation = gson.fromJson(request.getReader(), Reservation.class);
            reservation.setReservationId(id);
            boolean success = reservationDao.updateReservation(reservation);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(success));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Failed to update reservation"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setCorsHeaders(response);
        try {
            int id = Integer.parseInt(request.getPathInfo().substring(1));
            boolean success = reservationDao.deleteReservation(id);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(success));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson("Failed to delete reservation"));
        }
    }
}

package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.persistence.UserDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        urlPatterns = ("/editor")
)

public class Editor extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();

        req.setAttribute("users", userDAO.getAllUsers());

        RequestDispatcher dispatcher = req.getRequestDispatcher("/editor.jsp");
        dispatcher.forward(req, res);
    }
}
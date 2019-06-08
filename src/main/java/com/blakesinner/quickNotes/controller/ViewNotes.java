package com.blakesinner.quickNotes.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to view all notes.
 *
 * @author bsinner
 */
@WebServlet(urlPatterns = "/viewNotes")
public class ViewNotes extends HttpServlet {

    /**
     * Get user notes and forward to viewNotes jsp page.
     *
     * @param req               The HttpRequest
     * @param res               The HttpResponse
     * @throws ServletException If a servlet exception occurs
     * @throws IOException      If an I/O exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//        ServletAuthenticator auth = new ServletAuthenticator(req.getCookies());
//        RequestDispatcher rd = null;
//
////        if (filter.isValid()) {
//            rd = req.getRequestDispatcher("/viewNotes.jsp");
////        } else {
////            rd = req.getRequestDispatcher("/login.jsp");
////        }
//
//        req.setAttribute("servlet", "/viewNotes");
//
//        rd.forward(req, res);
    }

}

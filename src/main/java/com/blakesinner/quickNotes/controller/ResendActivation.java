package com.blakesinner.quickNotes.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to forward to resend account activation email page.
 *
 * @author bsinner
 */
@WebServlet(urlPatterns = "/resend", name = "resend")
public class ResendActivation extends HttpServlet {

    /**
     * Forward to resend JSP.
     *
     * @param req               the HttpRequest
     * @param res               the HttpResponse
     * @throws IOException      if an I/O exception occurs
     * @throws ServletException if a Servlet exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        forwardToJsp(req, res);
    }

    /**
     * Forward to resend JSP when login page makes a post request.
     *
     * @param req               the HttpRequest
     * @param res               the HttpResponse
     * @throws IOException      if an I/O exception occurs
     * @throws ServletException if a Servlet exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        forwardToJsp(req, res);
    }

    /**
     * Set forward to resend JSP.
     *
     * @param req               the HttpRequest
     * @param res               the HttpResponse
     * @throws IOException      if an I/O exception occurs
     * @throws ServletException if a Servlet exception occurs
     */
    private void forwardToJsp(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        RequestDispatcher rd = req.getRequestDispatcher("/resend.jsp");
        rd.forward(req, res);
    }

}

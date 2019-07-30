package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.ServletAuthenticator;

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
@WebServlet(urlPatterns = "/resend")
public class ResendActivation extends HttpServlet {

    /**
     * Forward to resend jsp page, or forward to login page if user isn't logged in.
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
     * Forward to resend jsp page when login page makes a post request to this servlet, or
     * forward back to login page if user isn't logged in.
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
     * Set up request dispatcher with ServletAuthenticator and forward to destination JSP.
     *
     * @param req               the HttpRequest
     * @param res               the HttpResponse
     * @throws IOException      if an I/O exception occurs
     * @throws ServletException if a Servlet exception occurs
     */
    private void forwardToJsp(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        ServletAuthenticator auth = new ServletAuthenticator(req, res
                , (byte[]) getServletContext().getAttribute("authKey"));

        auth.setUpDispatcher("/resend.jsp", "/resend")
                .forward(req, res);
    }

}

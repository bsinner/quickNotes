package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.JspFilter;

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
        JspFilter filter = new JspFilter(req, res);
        RequestDispatcher dispatcher;

        filter.updateCookies();

        if (filter.isLoggedIn()) {
            dispatcher = req.getRequestDispatcher("/resend.jsp");
        } else {
            req.setAttribute("servlet", "/resend");
            dispatcher = req.getRequestDispatcher("/login.jsp");
        }

        dispatcher.forward(req, res);

    }

}

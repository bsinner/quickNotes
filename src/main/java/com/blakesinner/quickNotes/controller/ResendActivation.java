package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.JspFilter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/resend")
public class ResendActivation extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        JspFilter filter = new JspFilter(req.getCookies());
        RequestDispatcher rd = null;

        if (filter.isValid()) {
            rd = req.getRequestDispatcher("/WEB-INF/jsps/resend.jsp");
        } else {
            req.setAttribute("servlet", "/resend");
            rd = req.getRequestDispatcher("/WEB-INF/jsps/login.jsp");
        }

        rd.forward(req, res);
    }

}

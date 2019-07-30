package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.util.KeyLoader;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Initialize application.
 *
 * @author bsinner
 */
@WebServlet(loadOnStartup = 0
        , urlPatterns = "/appInit"
        , name = "applicationInit")
public class ApplicationInit extends HttpServlet {

    private static final String TRANSLATE = "/translateKey.txt";
    private static final String AUTH = "/accessTokenPw.txt";

    /**
     * Initialize the application by loading secret keys into environment variables.
     */
    @Override
    public void init() {
        ServletContext context = getServletContext();
        KeyLoader loader = new KeyLoader();

        context.setAttribute("translateKey", loader.getKey(TRANSLATE));
        context.setAttribute("authKey", loader.getKeyBytes(AUTH));
    }

}

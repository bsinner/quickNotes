package com.blakesinner.quickNotes.controller;

import com.blakesinner.quickNotes.api.Secured;
import com.blakesinner.quickNotes.util.KeyLoader;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
     * Add data to the servlet context when the application starts up.
     */
    @Override
    public void init() {
        ServletContext context = getServletContext();
        KeyLoader loader = new KeyLoader();

        context.setAttribute("translateKey", loader.getKey(TRANSLATE));
        context.setAttribute("authKey", loader.getKeyBytes(AUTH));
        context.setAttribute("servletRoles", getServletRoles());
    }

    /**
     * Get a map of roles authorized to access servlets. Map keys are url patterns, and values are
     * role arrays. If a servlet has more than one url pattern, multiple entries are added with the
     * same array of roles.
     *
     * @return the roles map
     */
    private Map<String, String[]> getServletRoles() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.blakesinner.quickNotes.controller"
                        , ClasspathHelper.contextClassLoader(), ClasspathHelper.staticClassLoader()))
        );

        return extractAnnotations(reflections.getSubTypesOf(HttpServlet.class));
    }

    /**
     * Loop a list of servlet classes and extract the roles from the secured annotation.
     *
     * @param controllers the controllers to scan
     * @return            a map with URL pattern keys and role array values
     */
    private Map<String, String[]> extractAnnotations(Set<Class<? extends HttpServlet>> controllers) {
        Map<String, String[]> roles = new HashMap<>();

        controllers.forEach(controller -> {
            addRoles(controller.getAnnotation(Secured.class)
                    , controller.getAnnotation(WebServlet.class)
                    , roles);
        });

        return roles;
    }

    /**
     * Add roles in to the map, if Secured or WebServlet annotation is null doesn't add an entry.
     *
     * @param secured    the Secured annotation
     * @param webServlet the WebServlet annotation
     * @param map        the map to add results to
     */
    private void addRoles(Secured secured, WebServlet webServlet, Map<String, String[]> map) {
        if (secured != null && webServlet != null) {
            if (webServlet.urlPatterns().length > 0) {
                Arrays.stream(webServlet.urlPatterns()).forEach(p -> map.put(p, secured.roles()));
            }
        }
    }

}

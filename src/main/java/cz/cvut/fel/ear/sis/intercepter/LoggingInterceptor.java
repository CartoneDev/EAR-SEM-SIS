package cz.cvut.fel.ear.sis.intercepter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor{
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    /**
     * Pre-handle method of the interceptor.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param handler  The handler object.
     * @return true if the request is allowed to proceed, false otherwise.
     * @throws Exception if an exception occurs.
     */

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Log the HTTP method and URI
        String method = request.getMethod();
        String uri = request.getRequestURI();
        logger.info("Received a {} request for {}", method, uri);

        // Measure the time it takes to handle the request
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return true;
    }

    /**
     * Post-handle method of the interceptor.
     *
     * @param request        The HttpServletRequest object.
     * @param response       The HttpServletResponse object.
     * @param handler        The handler object.
     * @param modelAndView The ModelAndView object.
     * @throws Exception if an exception occurs.
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Request URL::" + request.getRequestURL().toString() + " Sent to Handler :: Current Time=" + System.currentTimeMillis());
    }

    /**
     * After-completion method of the interceptor.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param handler  The handler object.
     * @param ex       The exception object.
     * @throws Exception if an exception occurs.
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        logger.info("Request URL::" + request.getRequestURL().toString() + ":: End Time=" + System.currentTimeMillis());
        logger.info("Request URL::" + request.getRequestURL().toString() + ":: Time Taken=" + (System.currentTimeMillis() - startTime) + "ms");
    }



}

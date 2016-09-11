package com.becomejavasenior.mvc;

import org.apache.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class ErrorHandler {
    public static final String DEFAULT_ERROR_VIEW = "errorPage";
    private static final Logger logger = Logger.getLogger("controller_logger");

    @ExceptionHandler(value = Exception.class)
    public ModelAndView
    defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        logger.error("MVC Controller: Error.", e);
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        // Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("exceptionClass", e.getClass());
        mav.addObject("exceptionMessage", e.getLocalizedMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}

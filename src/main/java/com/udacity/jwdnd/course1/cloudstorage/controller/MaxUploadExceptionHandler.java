package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class MaxUploadExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Object obj, Exception e) {
        ModelAndView homeView = new ModelAndView("home.html");
        ModelAndView errorView = new ModelAndView("error.html");

        if (e instanceof MaxUploadSizeExceededException) {
            errorView.addObject("errorOperation", "File size upload was exceeded. The maximum allowed is 10MB.");
            return errorView;
        }

        return homeView;
    }
}

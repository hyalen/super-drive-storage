package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PageErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest req, Model model) {
        Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer httpStatus = Integer.valueOf(status.toString());

            if (httpStatus == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorOperation", "There was an error accessing this page. Try again later.");
            } else if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorOperation", "There was an internal server error. Try again later.");
            }
        }

        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

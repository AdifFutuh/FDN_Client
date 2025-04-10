package com.juaracoding.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GlobalController {

    @ModelAttribute
    public void addSessionToModel(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("USR_NAME");
            String menuNavBar = (String) session.getAttribute("MENU_NAVBAR");

            model.addAttribute("USR_NAME", username);
            model.addAttribute("MENU_NAVBAR", menuNavBar);
        }
    }
}

package edu.alexfrear.loginapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "login"; // renders templates/login.html
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") UserModel user, Model model) {
        boolean ok = "admin".equals(user.getUsername()) && "password".equals(user.getPassword());
        model.addAttribute("ok", ok);
        model.addAttribute("username", user.getUsername());
        return "result"; // renders templates/result.html
    }
}

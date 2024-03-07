package vn.uef.g2.foodlocation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientController {
    @GetMapping(value = {"", "/"})
    public String home() {
        return "client/index";
    }
}

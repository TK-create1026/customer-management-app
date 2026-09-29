package com.tkcreate.customer_management_app.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    @GetMapping("/customer")
    public String home(){
        return "customer/index";
    }
}

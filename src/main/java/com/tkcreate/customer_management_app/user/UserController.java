package com.tkcreate.customer_management_app.user;

import com.tkcreate.customer_management_app.customer.Customer;
import com.tkcreate.customer_management_app.customer.CustomerRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController{
    private final CustomerRepository customerRepository;

    public UserController(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @GetMapping("/user")
    public String showLoginForm(){
        return "user/login";
    }

    @GetMapping("/user/register")
    public String showRegisterForm(Model model){

        model.addAttribute("customer",new Customer());
        return "user/register";
    }
    @PostMapping("/user/register")
    public String register(Customer customer,Model model){
        if(!customer.isValidCustomer() || customer.getPassword() == null || customer.getPassword().isBlank()){
            model.addAttribute("message","すべての項目を入力してください。");
            return"user/register";
        }
         if(!customer.isValidBirthday()){
            model.addAttribute("message","生年月日は0歳以上120歳以下になる日付を入力してください。");
            return "user/register";
        }

        customerRepository.save(customer);
        return "user/register-complete";
    }
}
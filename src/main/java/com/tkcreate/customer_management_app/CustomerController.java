package com.tkcreate.customer_management_app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    //顧客情報追加画面表示
    @GetMapping("/add")
    public String showaddForm(Model model){
        model.addAttribute("customer", new Customer());
        return "add";
    }

    //顧客情報登録処理
    @PostMapping("/add")
    public String addCustomer(Customer customer){
        customerRepository.save(customer);
        return "complete";
    }

    @GetMapping("/list")
    public String listCustomers(Model model){
        List<Customer> customers = customerRepository.findAll();

        model.addAttribute("customers",customers);

        return "customer-list";
    }

    @GetMapping("/delete/{id}")
    public String deletecustomer(@PathVariable Long id,Model model){
        if(!customerRepository.existsById(id)){
            model.addAttribute("message","指定した顧客が存在しません。");
            return "error";
        }
        customerRepository.deleteById(id);
        return "redirect:/list";
    }
    
    @GetMapping("/update/{id}")
    public String showupdateForm(@PathVariable Long id,Model model){
        Customer customer = customerRepository.findById(id).orElse(null);
        
        if(customer == null){
            model.addAttribute("message","指定した顧客が存在しません。");
            return "error";
        }

        model.addAttribute("customer",customer);
        return "update";
    }

    @PostMapping ("/update")
    public String updateCustomer(Customer customer){
        customerRepository.save(customer);
        return "redirect:/list";
    }
}

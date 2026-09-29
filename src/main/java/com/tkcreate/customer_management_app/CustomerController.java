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

    // 顧客情報追加画面表示
    @GetMapping("/add")
    public String showaddForm(Model model){
        model.addAttribute("customer", new Customer());
        return "add";
    }

    // 顧客情報登録処理
    @PostMapping("/add")
    public String addCustomer(Customer customer,Model model){

        // 入力判定
        if(!customer.isValidCustomer()){
            model.addAttribute("message","すべての項目を入力してください。");
            return "add";
        }

        // 生年月日有効判定
        if(!customer.isValidBirthday()){
            model.addAttribute("message","生年月日は0歳以上120歳以下になる日付を入力してください。");
            return "add";
        }
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
    public String updateCustomer(Customer customer,Model model){

        if(!customer.isValidCustomer()){
            model.addAttribute("message","すべての項目を入力してください。");
            return "update";
        }

        if(!customer.isValidBirthday()){
            model.addAttribute("message","生年月日は0歳以上120歳以下になる日付を入力してください。");
            return "update";
        }
        customerRepository.save(customer);
        return "redirect:/list";
    }

    // 検索画面表示
    @GetMapping("/search")
    public String showsearchForm(){
        return "search";
    }

    @GetMapping("search/result")
    public String searchCustomer(String name,String gender,Model model){

        boolean noName = (name == null || name.isBlank());
        boolean noGender = (gender == null || gender.isBlank());

        if(noName && noGender){
            model.addAttribute("message","名前または性別を入力してください。");
            return "search";
        }

        List<Customer> customers;

        if(!noName && !noGender){
            customers =customerRepository.findByNameContainingAndGender(name,gender);
        }
        else if(!noName){
            customers = customerRepository.findByNameContaining(name);
        }
        else{
            customers = customerRepository.findByGender(gender);
        }
        if(customers.isEmpty()){
            model.addAttribute("message","該当する顧客が見つかりませんでした");
        }
        model.addAttribute("customers",customers);

        return "customer-list";
    }
}

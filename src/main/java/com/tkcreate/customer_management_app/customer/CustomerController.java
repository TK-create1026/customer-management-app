package com.tkcreate.customer_management_app.customer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

// 顧客情報の登録・更新・削除・検索を担当するContoroller
@Controller
public class CustomerController {
    private final CustomerRepository customerRepository; //Repositoryを利用してDB操作を行う
    private final PasswordEncoder passwordEncoder; // PasswordEncoderを利用してパスワードをハッシュ化する

    public CustomerController(CustomerRepository customerRepository,PasswordEncoder passwordEncoder){
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 顧客登録画面を表示する
    @GetMapping("/customer/add")
    public String showaddForm(Model model){
        model.addAttribute("customer", new Customer());
        return "customer/add";
    }

    /* 顧客情報登録処理
       入力チェックと生年月日チェックを行った後にDBへ保存する */
    @PostMapping("/customer/add")
    public String addCustomer(Customer customer,Model model){

        // 入力チェック
        if(!customer.isValidCustomer() ||(customer.getPassword() == null) || (customer.getPassword().isBlank())){

            model.addAttribute("message","すべての項目を入力してください。");
            return "customer/add";
        }

        // 生年月日チェック
        if(!customer.isValidBirthday()){
            model.addAttribute("message","生年月日は0歳以上120歳以下になる日付を入力してください。");
            return "customer/add";
        }

        // パスワード漏洩時のリスクを軽減するためハッシュ化して保存
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return "customer/complete";
    }

    @GetMapping("/customer/list")
    public String listCustomers(Model model){
        List<Customer> customers = customerRepository.findAll();

        model.addAttribute("customers",customers);

        return "customer/customer-list";
    }

    @GetMapping("/customer/delete/{id}")
    public String deletecustomer(@PathVariable Long id,Model model){
        if(!customerRepository.existsById(id)){
            model.addAttribute("message","指定した顧客が存在しません。");
            return "customer/error";
        }
        customerRepository.deleteById(id);
        return "redirect:/customer/list";
    }
    
    @GetMapping("/customer/update/{id}")
    public String showupdateForm(@PathVariable Long id,Model model){
        Customer customer = customerRepository.findById(id).orElse(null);
        
        if(customer == null){
            model.addAttribute("message","指定した顧客が存在しません。");
            return "customer/error";
        }

        model.addAttribute("customer",customer);
        return "customer/update";
    }

    @PostMapping ("/customer/update")
    public String updateCustomer(Customer customer,Model model){

        if(!customer.isValidCustomer()){
            model.addAttribute("message","すべての項目を入力してください。");
            return "customer/update";
        }

        if(!customer.isValidBirthday()){
            model.addAttribute("message","生年月日は0歳以上120歳以下になる日付を入力してください。");
            return "customer/update";
        }
        customerRepository.save(customer);
        return "redirect:/customer/list";
    }

    @GetMapping("/customer/password/{id}")
    public String showPasswordupdateForm(@PathVariable Long id,Model model){
        Customer customer = customerRepository.findById(id).orElse(null);

        if(customer == null){
            model.addAttribute("message","指定した顧客が存在しません。");
            return "customer/error";
        }

        model.addAttribute("customer",customer);
        return "customer/password-update";
    }

    @PostMapping("/customer/password")
    public String updatePassword(Customer customer,Model model){
        Customer target = customerRepository.findById(customer.getId()).orElse(null);

        if(target == null){
            model.addAttribute("message","指定した顧客が存在しません。");
            return "customer/error";
        }

        if(customer.getPassword() == null || customer.getPassword().isBlank()){
            model.addAttribute("message","パスワードを入力してください。");
            return "customer/password-update";
        }

        target.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(target);
        return "customer/password-complete";
    }

    // 検索画面表示
    @GetMapping("/customer/search")
    public String showsearchForm(){
        return "customer/search";
    }

    @GetMapping("/customer/search/result")
    public String searchCustomer(String name,String gender,Model model){

        boolean noName = (name == null || name.isBlank());
        boolean noGender = (gender == null || gender.isBlank());

        if(noName && noGender){
            model.addAttribute("message","名前または性別を入力してください。");
            return "customer/search";
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

        return "customer/customer-list";
    }
}

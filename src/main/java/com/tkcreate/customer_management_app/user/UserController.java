package com.tkcreate.customer_management_app.user;

import com.tkcreate.customer_management_app.customer.Customer;
import com.tkcreate.customer_management_app.customer.CustomerRepository;

import jakarta.servlet.http.HttpSession;
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
    @PostMapping("/user")
    public String login(String email,String password,Model model,HttpSession session){
        Customer customer = customerRepository.findByEmailAndPassword(email,password);

        if(customer == null){
            model.addAttribute("message","メールアドレスまたはパスワードが違います。");
            return "redirect:/user";
        }
        session.setAttribute("loginCustomer",customer);
        return "redirect:/user/mypage";
    }

    @GetMapping("/user/mypage")
    public String showMypage(HttpSession session,Model model){
        Customer customer = (Customer)session.getAttribute("loginCustomer");
        model.addAttribute("customer",customer);
        return "user/mypage";
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

    @GetMapping("/user/mypage/lookup")
    public String showLookup(HttpSession session,Model model){
        Customer customer = (Customer)session.getAttribute("loginCustomer");
        model.addAttribute("customer",customer);
        return "user/lookup";
    }

    @GetMapping("/user/mypage/lookup/email-update")
    public String showEmailUpdate(HttpSession session, Model model){
        Customer customer = (Customer)session.getAttribute("loginCustomer");
        model.addAttribute("customer",customer);
        return "user/email-update";
    }
    @PostMapping("/user/mypage/lookup/email-update")
    public String updateEmai(Customer formCustomer, HttpSession session,Model model){

        if(formCustomer.getEmail() == null ||!formCustomer.getEmail().contains("@")){
            model.addAttribute("message","有効なメールアドレスを入力してください。");
            model.addAttribute("customer",formCustomer);
            return "user/email-update";
        }

        Customer customer = (Customer)session.getAttribute("loginCustomer");

        customer.setEmail(formCustomer.getEmail());

        customerRepository.save(customer);

        session.setAttribute("loginCustomer",customer);

        return "redirect:/user/mypage/lookup";
    }

    @GetMapping("/user/mypage/lookup/password-update")
    public String showPasswordUpdate(HttpSession session, Model model){

        Customer customer = (Customer)session.getAttribute("loginCustomer");

        model.addAttribute("customer",customer);

        return "user/password-update";
    }

    @PostMapping("/user/mypage/lookup/password-update")
    public String updatePassword(String currentPassword,String newPassword, HttpSession session,Model model){

        Customer customer = (Customer)session.getAttribute("loginCustomer");

        if(!customer.getPassword().equals(currentPassword)){
            model.addAttribute("message","現在のパスワードが間違っています。");
            model.addAttribute("customer",customer);
            return "user/password-update";
        }

        customer.setPassword(newPassword);

        customerRepository.save(customer);

        session.setAttribute("loginCustomer",customer);

        return "redirect:/user/mypage/lookup";
    }
}
package com.tkcreate.customer_management_app.user;

import com.tkcreate.customer_management_app.customer.Customer;
import com.tkcreate.customer_management_app.customer.CustomerRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController{
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(CustomerRepository customerRepository,PasswordEncoder passwordEncoder){
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user")
    public String showLoginForm(){
        return "user/login";
    }
    @PostMapping("/user")
    public String login(String email,String password,Model model,HttpSession session){
        Customer customer = customerRepository.findByEmail(email);

        if(customer == null){
            model.addAttribute("message","メールアドレスまたはパスワードが違います。");
            return "user/login";
        }

        if(!passwordEncoder.matches(password,customer.getPassword())){
            model.addAttribute("message","メールアドレスまたわパスワードが違います。");
            return "user/login";
        }

        session.setAttribute("loginCustomer",customer);
        return "redirect:/user/mypage";
    }

    @GetMapping("/user/mypage")
    public String showMypage(HttpSession session,Model model){
        Customer customer = (Customer)session.getAttribute("loginCustomer");
        if(customer == null){
            return "redirect:/user";
        }
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

        Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());

        if(!customer.isValidCustomer() || customer.getPassword() == null || customer.getPassword().isBlank()){
            model.addAttribute("message","すべての項目を入力してください。");
            return"user/register";
        }
        if(!customer.isValidBirthday()){
            model.addAttribute("message","生年月日は0歳以上120歳以下になる日付を入力してください。");
            return "user/register";
        }
        if(existingCustomer != null){
            model.addAttribute("message","既に登録されているメールアドレスです。");
            return "user/register";
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return "user/register-complete";
    }

    @GetMapping("/user/mypage/lookup")
    public String showLookup(HttpSession session,Model model){

        Customer customer = (Customer)session.getAttribute("loginCustomer");
        if(customer == null){
            return "redirect:/user";
        }

        model.addAttribute("customer",customer);
        return "user/lookup";
    }

    @GetMapping("/user/mypage/lookup/email-update")
    public String showEmailUpdate(HttpSession session, Model model){

        Customer customer = (Customer)session.getAttribute("loginCustomer");
        if(customer == null){
            return "redirect:/user";
        }
        model.addAttribute("customer",customer);
        return "user/email-update";
    }
    @PostMapping("/user/mypage/lookup/email-update")
    public String updateEmai(Customer formCustomer, HttpSession session,Model model){
        Customer duplicateCustomer = customerRepository.findByEmail(formCustomer.getEmail());

        if(formCustomer.getEmail() == null ||!formCustomer.getEmail().contains("@")){
            model.addAttribute("message","有効なメールアドレスを入力してください。");
            model.addAttribute("customer",formCustomer);
            return "user/email-update";
        }
        
        if(duplicateCustomer != null){
            model.addAttribute("message","既に登録されているメールアドレスです。");
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
        if(customer == null){
            return "redirect:/user";
        }

        model.addAttribute("customer",customer);

        return "user/password-update";
    }

    @PostMapping("/user/mypage/lookup/password-update")
    public String updatePassword(String currentPassword,String newPassword, HttpSession session,Model model){

        Customer customer = (Customer)session.getAttribute("loginCustomer");

        if(!passwordEncoder.matches(currentPassword,customer.getPassword())){
            model.addAttribute("message","現在のパスワードが間違っています。");
            model.addAttribute("customer",customer);
            return "user/password-update";
        }

        customer.setPassword(passwordEncoder.encode(newPassword));

        customerRepository.save(customer);

        session.setAttribute("loginCustomer",customer);

        return "redirect:/user/mypage/lookup";
    }
    @GetMapping("/user/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/user";
    }

    @GetMapping("/user/mypage/unsubsc")
    public String showUnsubsc(HttpSession session){
        Customer customer = (Customer)session.getAttribute("loginCustomer");
        if(customer == null){
            return "redirect:/user";
        }

        return "user/unsubsc";
    }

    @PostMapping("/user/mypage/unsubsc")
    public String unsubscribe(HttpSession session){
        Customer customer = (Customer)session.getAttribute("loginCustomer");
        if(customer == null){
            return "redirect:/user";
        }
        customerRepository.delete(customer);
        session.invalidate();
        return "redirect:/user";
    }
}
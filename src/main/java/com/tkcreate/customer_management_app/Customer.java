package com.tkcreate.customer_management_app;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String gender;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;
    private String email;
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getGender(){
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public LocalDate getBirthday(){
        return birthday;
    }

    public void setBirthday(LocalDate birthday){
        this.birthday = birthday;
    }

    public boolean isValidBirthday(){
        if(birthday == null){
            return false;
        }

        LocalDate today = LocalDate.now();

        if(birthday.isAfter(today)){
            return false;
        }

        int age = Period.between(
            birthday,
            today)
            .getYears();
        
        return 0 <= age && age <= 120;
    }
    public int getAge(){
    if(birthday == null){
        return 0;
    }
    return Period.between(
            birthday,
            LocalDate.now())
            .getYears();
    }

        public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public boolean isValEmail(){
        if(email == null || email.isBlank()){
            return false;
        }
        return email.contains("@");
    }

    public boolean isValidCustomer(){
        if(name == null || name.isBlank()){
            return false;
        }

        if(gender == null || gender.isBlank()){
            return false;
        }

        if(birthday == null){
            return false;
        }

        if(email == null || email.isBlank()){
            return false;
        }
        return true;
    }
}

package com.tkcreate.customer_management_app.customer;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // Customerテーブルに対応するEntity
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; // 顧客ID(主キー)新規登録時に自動採番される
    private String name;
    private String gender;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthday;
    private String email;
    private String password; // BCryptでハッシュ化されたパスワードを保持

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

    /*生年月日が有効か判断する
      ・未来日付は入力不可
      ・0歳以上120歳以下のみ許可 */
    public boolean isValidBirthday(){
        if(birthday == null){
            return false;
        }

        LocalDate today = LocalDate.now();
        if(birthday.isAfter(today)){
            return false;
        }

        int age = Period.between(birthday,today).getYears();
        
        return 0 <= age && age <= 120;
    }

    // 生年月日から現在の年齢を計算する
    public int getAge(){
        if(birthday == null){
            return 0;
        }
        return Period.between(birthday,LocalDate.now()).getYears();
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    /* メールアドレスの簡易チェック
       @が含まれているかを確認する*/
    public boolean isValidEmail(){
        if(email == null || email.isBlank()){
            return false;
        }
        return email.contains("@");
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    // 必須項目がすべて入力されているか確認する。
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

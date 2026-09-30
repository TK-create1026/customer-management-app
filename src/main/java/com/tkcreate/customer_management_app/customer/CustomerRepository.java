package com.tkcreate.customer_management_app.customer;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends  JpaRepository<Customer,Long>{
    List<Customer> findByNameContaining(String name);
    List<Customer> findByGender(String gender);
    List<Customer> findByNameContainingAndGender(String name,String gender);
    Customer findByEmailAndPassword(String email,String password);
    Customer findByEmail(String email);
}

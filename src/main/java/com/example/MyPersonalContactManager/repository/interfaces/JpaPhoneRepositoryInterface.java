package com.example.MyPersonalContactManager.repository.interfaces;

import com.example.MyPersonalContactManager.models.ContactModels.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPhoneRepositoryInterface extends JpaRepository<Phone, String> {

}

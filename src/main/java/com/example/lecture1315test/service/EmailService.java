package com.example.lecture1315test.service;

import com.example.lecture1315test.models.Email;
import com.example.lecture1315test.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    EmailRepository emailRepository;

    public List<Email> findAll(){
        return emailRepository.findAll();
    }

    public void saveEmail(Email email){
        String uniqueID = UUID.randomUUID().toString();
        email.setId(uniqueID);
        email.setStatus("Pending");
        email.setDate(java.time.LocalDate.now());
        email.setTime(java.time.LocalTime.now());
        emailRepository.save(email);
    }

    public void updateStatus(Email email, String status){
        email.setStatus(status);
        emailRepository.save(email);
    }

    public Email findEmail(String id){
       Email email = emailRepository.findById(id);
       return email;
    }

}

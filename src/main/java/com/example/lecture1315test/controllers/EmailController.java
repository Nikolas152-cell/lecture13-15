package com.example.lecture1315test.controllers;

import com.example.lecture1315test.models.Email;
import com.example.lecture1315test.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class EmailController {

    private final static String topicName = "lecture13-15";


    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    EmailService emailService;

    @Autowired
    JavaMailSender mailSender;

    @RequestMapping(value = "/emails", method = RequestMethod.GET)
    public ResponseEntity<List<Email>> getEmails(){
        if(emailService.findAll().size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(emailService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/emails", method = RequestMethod.POST)
    public ResponseEntity<String> addEmail(@RequestBody @Validated Email email){
        if(email == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        emailService.saveEmail(email);
        kafkaTemplate.send(topicName,email.getId());
        return new ResponseEntity<>(email.getId(),HttpStatus.OK);
    }

    @KafkaListener(topics = "lecture13-15", groupId = "console-consumer-9360")
    public void listenMessages(String id){
        Email email = emailService.findEmail(id);
        if(email == null){
            return;
        }
        SimpleMailMessage emailMessageToGmail = new SimpleMailMessage();
        try{
            emailMessageToGmail.setFrom("maznichin03@gmail.com");
            emailMessageToGmail.setTo(email.getAddress());
            emailMessageToGmail.setText(email.getMessage());
            emailService.updateStatus(email, "SENT");
        }catch (NullPointerException e){
            emailService.updateStatus(email, "ERROR");
            email.setErrorDescription("Wrong values for sending an email");
        }
        mailSender.send(emailMessageToGmail);
    }

}

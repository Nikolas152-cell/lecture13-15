package com.example.lecture1315test.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


@Document("emails")
@Data
public class Email {

    @Id
    private String id;

    private String errorDescription;
    private String address;
    private String message;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Email(String id, String address, String message, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.address = address;
        this.message = message;
        this.date = date;
        this.time = time;
        this.status = status;
    }
}

package com.example.musicCollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.musicCollection.view.ApplicationView;

import javafx.application.Application;

@SpringBootApplication
public class MusicCollectionApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(MusicCollectionApplication.class, args);
        
        SpringContext.setApplicationContext(context);
        
        Application.launch(ApplicationView.class, args);
    }
}


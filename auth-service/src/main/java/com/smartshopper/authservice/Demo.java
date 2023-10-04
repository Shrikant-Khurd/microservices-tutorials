package com.smartshopper.authservice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.*;
import java.net.URL;
import java.util.Locale;


public class Demo {

    public void msgDemo(){

        File file = new File("src/main/resources/messages/api_error_messages.properties");
        if (file.exists()) {
            System.out.println("File found at: " + file.getAbsolutePath());
        } else {
            System.out.println("File not found.");
        }
        String classpath = System.getProperty("java.class.path");
        System.out.println("Classpath: " + classpath);

        URL s = getClass().getClassLoader().getResource("/messages/api_error_messages.properties");
        System.out.println("sdsfd: " + s);

        URL resourceUrl = getClass().getClassLoader().getResource("/messages/api_error_messages.properties");
        System.out.println("resource path :" + resourceUrl);
        if (resourceUrl != null) {
            System.out.println("Resource found at: " + resourceUrl.getPath());
        } else {
            System.out.println("Resource not found.");
        }


    }

    public static void main(String[] args) {
        Demo demo=new Demo();
        demo.msgDemo();
//        demo.testGetMessage();


    }
}

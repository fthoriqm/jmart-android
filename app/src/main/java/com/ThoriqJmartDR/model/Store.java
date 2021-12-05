package com.ThoriqJmartDR.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Store extends Serializable {
    public static final String REGEX_NAME = "^[A-Z](?!.*(\\s)\1).{4,20}$";
    public static final String REGEX_PHONE = "^\\d{9,12}";
    public String address;
    public String name;
    public String phoneNumber;

    public Store(String name,String address, String phoneNumber) {
        super();
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    public String toString(){
        return
                "Name : "+ this.name + "\n" +
                        "email : "+ this.address + "\n" +
                        "phoneNumber : "+ this.phoneNumber;
    }
    public boolean validate(){
        Pattern pattern = Pattern.compile(REGEX_PHONE);
        Matcher matcher = pattern.matcher("this.phoneNumber");
        boolean matchFound1 = matcher.find();

        pattern = Pattern.compile(REGEX_NAME);
        matcher = pattern.matcher("this.name");
        boolean matchFound2 = matcher.find();

        return matchFound1 && matchFound2;
    }
}

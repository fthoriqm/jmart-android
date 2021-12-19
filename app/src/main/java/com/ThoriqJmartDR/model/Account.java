package com.ThoriqJmartDR.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account extends Serializable {
    public String name;
    public String email;
    public String password;
    public static final String REGEX_EMAIL = "^(?!\\.)(?!.*?\\.\\.)[a-zA-Z0-9&_*~.]+@(?!-)[a-zA-Z0-9-]+\\.(?!.*\\.$)[a-zA-Z0-9.]+$";
    public static final String REGEX_PASSWORD = "^([A-Z]{1,}[a-z]{1,}\\d{1,}){8,}$";
    public Store store;
    public double balance;

    public Account(String name, String email, String password, double balance) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public boolean validate(){
        Pattern pattern = Pattern.compile(REGEX_EMAIL);
        Matcher matcher = pattern.matcher("this.email");
        boolean matchFound = matcher.find();
        String res1 = matchFound ? "FOUND!" : "NOT FOUND!";
        pattern = Pattern.compile(REGEX_PASSWORD);
        matcher = pattern.matcher("this.password");
        matchFound = matcher.find();
        String res2 = matchFound ? "FOUND!" : "NOT FOUND!";

        return res1.equals("FOUND!") && res2.equals("FOUND!");
    }
}

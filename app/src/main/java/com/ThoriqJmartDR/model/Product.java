package com.ThoriqJmartDR.model;

public class Product extends Serializable{
    public ProductCategory category;
    public boolean conditionUsed;
    public double discount;
    public String name;
    public double price;
    public byte shipmentPlans;
    public int weigth;

    public Product (String name, int weigth, boolean conditionUsed, double price, double discount, ProductCategory category, byte shipmentPlans){
        super();
        this.name = name;
        this.weigth = weigth;
        this.conditionUsed = conditionUsed;
        this.price = price;
        this.discount = discount;
        this.category = category;
        this.shipmentPlans = shipmentPlans;
    }

    public String toString(){
        return null; // bagian sini belum dibuat outputnya apa
    }
}

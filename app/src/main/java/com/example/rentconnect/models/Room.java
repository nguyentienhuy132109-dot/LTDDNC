package com.example.rentconnect.models;

public class Room {
    private int id;
    private String title;
    private String address;
    private double price;
    private double deposit;
    private double electricRate;
    private double waterRate;
    private int capacity;
    private String status;

    public Room(int id, String title, String address, double price, double deposit, double electricRate, double waterRate, int capacity, String status) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.price = price;
        this.deposit = deposit;
        this.electricRate = electricRate;
        this.waterRate = waterRate;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAddress() { return address; }
    public double getPrice() { return price; }
    public double getDeposit() { return deposit; }
    public double getElectricRate() { return electricRate; }
    public double getWaterRate() { return waterRate; }
    public int getCapacity() { return capacity; }
    public String getStatus() { return status; }
}

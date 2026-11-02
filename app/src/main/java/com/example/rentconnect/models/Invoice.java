package com.example.rentconnect.models;

public class Invoice {
    private int id;
    private int roomId;
    private String monthYear;
    private double roomFee;
    private double electricFee;
    private double waterFee;
    private double serviceFee;
    private double totalAmount;
    private String qrContent;
    private String status;

    public Invoice(int id, int roomId, String monthYear, double roomFee, double electricFee, double waterFee, double serviceFee, double totalAmount, String qrContent, String status) {
        this.id = id;
        this.roomId = roomId;
        this.monthYear = monthYear;
        this.roomFee = roomFee;
        this.electricFee = electricFee;
        this.waterFee = waterFee;
        this.serviceFee = serviceFee;
        this.totalAmount = totalAmount;
        this.qrContent = qrContent;
        this.status = status;
    }

    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public String getMonthYear() { return monthYear; }
    public double getRoomFee() { return roomFee; }
    public double getElectricFee() { return electricFee; }
    public double getWaterFee() { return waterFee; }
    public double getServiceFee() { return serviceFee; }
    public double getTotalAmount() { return totalAmount; }
    public String getQrContent() { return qrContent; }
    public String getStatus() { return status; }
}

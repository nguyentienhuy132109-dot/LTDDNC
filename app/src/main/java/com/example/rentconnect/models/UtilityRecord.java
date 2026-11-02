package com.example.rentconnect.models;

public class UtilityRecord {
    private int id;
    private int roomId;
    private String monthYear;
    private double newElectric;
    private double newWater;
    private String electricPhotoPath;
    private String waterPhotoPath;
    private int isSynced;

    public UtilityRecord(int id, int roomId, String monthYear, double newElectric, double newWater, String electricPhotoPath, String waterPhotoPath, int isSynced) {
        this.id = id;
        this.roomId = roomId;
        this.monthYear = monthYear;
        this.newElectric = newElectric;
        this.newWater = newWater;
        this.electricPhotoPath = electricPhotoPath;
        this.waterPhotoPath = waterPhotoPath;
        this.isSynced = isSynced;
    }

    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public String getMonthYear() { return monthYear; }
    public double getNewElectric() { return newElectric; }
    public double getNewWater() { return newWater; }
    public String getElectricPhotoPath() { return electricPhotoPath; }
    public String getWaterPhotoPath() { return waterPhotoPath; }
    public int getIsSynced() { return isSynced; }
}

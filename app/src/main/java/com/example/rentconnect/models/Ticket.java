package com.example.rentconnect.models;

public class Ticket {
    private int id;
    private int tenantId;
    private int roomId;
    private String category;
    private String priority;
    private String description;
    private String imagePath;
    private String status;

    public Ticket(int id, int tenantId, int roomId, String category, String priority, String description, String imagePath, String status) {
        this.id = id;
        this.tenantId = tenantId;
        this.roomId = roomId;
        this.category = category;
        this.priority = priority;
        this.description = description;
        this.imagePath = imagePath;
        this.status = status;
    }

    public int getId() { return id; }
    public int getTenantId() { return tenantId; }
    public int getRoomId() { return roomId; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
    public String getStatus() { return status; }
}

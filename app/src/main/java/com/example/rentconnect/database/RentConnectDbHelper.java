package com.example.rentconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rentconnect.models.Invoice;
import com.example.rentconnect.models.Room;
import com.example.rentconnect.models.Ticket;
import com.example.rentconnect.models.User;
import com.example.rentconnect.models.UtilityRecord;

import java.util.ArrayList;
import java.util.List;

public class RentConnectDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rentconnect.db";
    private static final int DATABASE_VERSION = 1;

    public RentConnectDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_name TEXT, email TEXT UNIQUE, password TEXT, phone TEXT, role TEXT)";

        String createRooms = "CREATE TABLE rooms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, address TEXT, price REAL, deposit REAL, electric_rate REAL, water_rate REAL, capacity INTEGER, status TEXT)";

        String createUtilities = "CREATE TABLE utility_records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "room_id INTEGER, month_year TEXT, new_electric REAL, new_water REAL, electric_photo TEXT, water_photo TEXT, is_synced INTEGER)";

        String createInvoices = "CREATE TABLE invoices (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "room_id INTEGER, month_year TEXT, room_fee REAL, electric_fee REAL, water_fee REAL, service_fee REAL, total_amount REAL, qr_content TEXT, status TEXT)";

        String createTickets = "CREATE TABLE tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenant_id INTEGER, room_id INTEGER, category TEXT, priority TEXT, description TEXT, image_path TEXT, status TEXT)";

        db.execSQL(createUsers);
        db.execSQL(createRooms);
        db.execSQL(createUtilities);
        db.execSQL(createInvoices);
        db.execSQL(createTickets);

        // Seed initial room data
        db.execSQL("INSERT INTO rooms (title, address, price, deposit, electric_rate, water_rate, capacity, status) VALUES " +
                "('Phòng 101 - Khu Nam Từ Liêm', '12 Ngõ 45 Cầu Diễn, Hà Nội', 3500000, 3500000, 3800, 25000, 2, 'ACTIVE'), " +
                "('Phòng 202 - Khu Cầu Giấy', '88 Xuân Thủy, Cầu Giấy, Hà Nội', 4200000, 4200000, 4000, 30000, 3, 'ACTIVE')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS rooms");
        db.execSQL("DROP TABLE IF EXISTS utility_records");
        db.execSQL("DROP TABLE IF EXISTS invoices");
        db.execSQL("DROP TABLE IF EXISTS tickets");
        onCreate(db);
    }

    // Auth FR01
    public boolean registerUser(String fullName, String email, String password, String phone, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("full_name", fullName);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("phone", phone);
        cv.put("role", role);
        long result = db.insert("users", null, cv);
        return result != -1;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        if (cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("role"))
            );
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    // FR03 Rooms
    public List<Room> getAllRooms() {
        List<Room> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM rooms", null);
        while (cursor.moveToNext()) {
            list.add(new Room(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getString(cursor.getColumnIndexOrThrow("address")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("deposit")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("electric_rate")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("water_rate")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("capacity")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status"))
            ));
        }
        cursor.close();
        return list;
    }

    // FR07 Utility Record Offline
    public boolean insertUtilityRecord(int roomId, String monthYear, double newElectric, double newWater, String elecPath, String waterPath, int isSynced) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("room_id", roomId);
        cv.put("month_year", monthYear);
        cv.put("new_electric", newElectric);
        cv.put("new_water", newWater);
        cv.put("electric_photo", elecPath);
        cv.put("water_photo", waterPath);
        cv.put("is_synced", isSynced);
        return db.insert("utility_records", null, cv) != -1;
    }

    // FR08 Invoice Creation
    public boolean createInvoice(int roomId, String monthYear, double roomFee, double electricFee, double waterFee, double serviceFee, String qrContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        double total = roomFee + electricFee + waterFee + serviceFee;
        ContentValues cv = new ContentValues();
        cv.put("room_id", roomId);
        cv.put("month_year", monthYear);
        cv.put("room_fee", roomFee);
        cv.put("electric_fee", electricFee);
        cv.put("water_fee", waterFee);
        cv.put("service_fee", serviceFee);
        cv.put("total_amount", total);
        cv.put("qr_content", qrContent);
        cv.put("status", "UNPAID");
        return db.insert("invoices", null, cv) != -1;
    }

    public List<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM invoices ORDER BY id DESC", null);
        while (cursor.moveToNext()) {
            list.add(new Invoice(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("room_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("month_year")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("room_fee")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("electric_fee")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("water_fee")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("service_fee")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("total_amount")),
                    cursor.getString(cursor.getColumnIndexOrThrow("qr_content")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status"))
            ));
        }
        cursor.close();
        return list;
    }

    // FR10 Ticket Creation
    public boolean createTicket(int tenantId, int roomId, String category, String priority, String description, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tenant_id", tenantId);
        cv.put("room_id", roomId);
        cv.put("category", category);
        cv.put("priority", priority);
        cv.put("description", description);
        cv.put("image_path", imagePath);
        cv.put("status", "PENDING");
        return db.insert("tickets", null, cv) != -1;
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tickets ORDER BY id DESC", null);
        while (cursor.moveToNext()) {
            list.add(new Ticket(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("tenant_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("room_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("priority")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image_path")),
                    cursor.getString(cursor.getColumnIndexOrThrow("status"))
            ));
        }
        cursor.close();
        return list;
    }
}

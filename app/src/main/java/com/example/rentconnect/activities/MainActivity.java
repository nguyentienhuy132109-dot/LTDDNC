package com.example.rentconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rentconnect.R;
import com.example.rentconnect.adapters.RoomAdapter;
import com.example.rentconnect.database.RentConnectDbHelper;
import com.example.rentconnect.models.Room;
import com.example.rentconnect.utils.SensorHelper;
import com.example.rentconnect.utils.SharedPrefManager;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvUserHeader, tvSensorStatus;
    private RecyclerView rvRooms;
    private Button btnGoUtility, btnGoInvoice, btnGoTicket, btnLogout;
    private RentConnectDbHelper dbHelper;
    private SharedPrefManager prefManager;
    private SensorHelper sensorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new RentConnectDbHelper(this);
        prefManager = new SharedPrefManager(this);

        tvUserHeader = findViewById(R.id.tvUserHeader);
        tvSensorStatus = findViewById(R.id.tvSensorStatus);
        rvRooms = findViewById(R.id.rvRooms);
        btnGoUtility = findViewById(R.id.btnGoUtility);
        btnGoInvoice = findViewById(R.id.btnGoInvoice);
        btnGoTicket = findViewById(R.id.btnGoTicket);
        btnLogout = findViewById(R.id.btnLogout);

        tvUserHeader.setText("Xin chào: " + prefManager.getUserName() + " (" + prefManager.getUserRole() + ")");

        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        refreshRoomList();

        btnGoUtility.setOnClickListener(v -> startActivity(new Intent(this, UtilityRecordActivity.class)));
        btnGoInvoice.setOnClickListener(v -> startActivity(new Intent(this, InvoiceActivity.class)));
        btnGoTicket.setOnClickListener(v -> startActivity(new Intent(this, TicketActivity.class)));
        btnLogout.setOnClickListener(v -> {
            prefManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Khởi tạo các cảm biến thông minh (Light Sensor & Accelerometer)
        setupSensors();
    }

    private void refreshRoomList() {
        List<Room> roomList = dbHelper.getAllRooms();
        rvRooms.setAdapter(new RoomAdapter(this, roomList));
    }

    private void setupSensors() {
        sensorHelper = new SensorHelper(this);

        // 1. Cảm biến Ánh sáng (TYPE_LIGHT) -> Tự động gợi ý/chuyển Light/Dark theme khi đi xem phòng
        sensorHelper.setOnLightChangeListener((isDarkEnvironment, luxValue) -> {
            if (isDarkEnvironment) {
                tvSensorStatus.setText("🌙 Cảm biến ánh sáng: Môi trường tối (" + luxValue + " Lux) - Đã bật Dark Mode đọc hợp đồng");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                tvSensorStatus.setText("☀️ Cảm biến ánh sáng: Môi trường sáng (" + luxValue + " Lux) - Chế độ Sáng chuẩn");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // 2. Cảm biến Gia tốc (TYPE_ACCELEROMETER) -> Lắc thiết bị làm mới dữ liệu & kích hoạt nhanh Báo lỗi
        sensorHelper.setOnShakeListener(() -> {
            refreshRoomList();
            Toast.makeText(MainActivity.this, "📱 [Cảm biến gia tốc] Đã lắc thiết bị! Đã làm mới danh sách phòng trọ.", Toast.LENGTH_SHORT).show();
            
            // Tùy chọn mở nhanh giao diện Báo cáo sự cố
            Intent intent = new Intent(MainActivity.this, TicketActivity.class);
            intent.putExtra("QUICK_REPORT_SHAKE", true);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorHelper != null) {
            sensorHelper.register();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorHelper != null) {
            sensorHelper.unregister();
        }
    }
}

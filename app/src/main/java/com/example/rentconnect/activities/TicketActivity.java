package com.example.rentconnect.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rentconnect.R;
import com.example.rentconnect.adapters.TicketAdapter;
import com.example.rentconnect.database.RentConnectDbHelper;
import com.example.rentconnect.models.Ticket;
import com.example.rentconnect.utils.SharedPrefManager;
import java.util.List;

public class TicketActivity extends AppCompatActivity {

    private Spinner spCategory, spPriority;
    private EditText edtDescription;
    private Button btnSendTicket;
    private RecyclerView rvTickets;
    private RentConnectDbHelper dbHelper;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        dbHelper = new RentConnectDbHelper(this);
        prefManager = new SharedPrefManager(this);

        spCategory = findViewById(R.id.spCategory);
        spPriority = findViewById(R.id.spPriority);
        edtDescription = findViewById(R.id.edtTicketDesc);
        btnSendTicket = findViewById(R.id.btnSendTicket);
        rvTickets = findViewById(R.id.rvTickets);

        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        loadTickets();

        // Xử lý khi kích hoạt nhanh qua thao tác Lắc thiết bị (Accelerometer Sensor)
        boolean isShakeTriggered = getIntent().getBooleanExtra("QUICK_REPORT_SHAKE", false);
        if (isShakeTriggered) {
            edtDescription.setText("[Báo cáo khẩn cấp từ thao tác Lắc máy]: ");
            edtDescription.requestFocus();
            Toast.makeText(this, "📱 Kích hoạt cửa sổ báo cáo khẩn cấp từ thao tác Lắc thiết bị!", Toast.LENGTH_LONG).show();
        }

        btnSendTicket.setOnClickListener(v -> {
            String category = spCategory.getSelectedItem() != null ? spCategory.getSelectedItem().toString() : "Điện";
            String priority = spPriority.getSelectedItem() != null ? spPriority.getSelectedItem().toString() : "Gấp";
            String desc = edtDescription.getText().toString().trim();

            if (desc.isEmpty()) {
                Toast.makeText(this, "Mô tả sự cố không được để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = dbHelper.createTicket(prefManager.getUserId(), 1, category, priority, desc, "ticket_img.jpg");
            if (ok) {
                Toast.makeText(this, "Tạo báo cáo sự cố (Ticket) thành công!", Toast.LENGTH_SHORT).show();
                edtDescription.setText("");
                loadTickets();
            }
        });
    }

    private void loadTickets() {
        List<Ticket> ticketList = dbHelper.getAllTickets();
        rvTickets.setAdapter(new TicketAdapter(this, ticketList));
    }
}

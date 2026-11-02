package com.example.rentconnect.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rentconnect.R;
import com.example.rentconnect.adapters.InvoiceAdapter;
import com.example.rentconnect.database.RentConnectDbHelper;
import com.example.rentconnect.models.Invoice;
import com.example.rentconnect.utils.VietQRHelper;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    private EditText edtInvMonth, edtRoomFee, edtElecFee, edtWaterFee, edtServiceFee;
    private Button btnCreateInvoice;
    private RecyclerView rvInvoices;
    private RentConnectDbHelper dbHelper;
    private InvoiceAdapter adapter;
    private List<Invoice> invoiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        dbHelper = new RentConnectDbHelper(this);

        edtInvMonth = findViewById(R.id.edtInvMonth);
        edtRoomFee = findViewById(R.id.edtRoomFee);
        edtElecFee = findViewById(R.id.edtElecFee);
        edtWaterFee = findViewById(R.id.edtWaterFee);
        edtServiceFee = findViewById(R.id.edtServiceFee);
        btnCreateInvoice = findViewById(R.id.btnCreateInvoice);
        rvInvoices = findViewById(R.id.rvInvoices);

        rvInvoices.setLayoutManager(new LinearLayoutManager(this));
        loadInvoices();

        btnCreateInvoice.setOnClickListener(v -> {
            String month = edtInvMonth.getText().toString().trim();
            String roomStr = edtRoomFee.getText().toString().trim();
            String elecStr = edtElecFee.getText().toString().trim();
            String waterStr = edtWaterFee.getText().toString().trim();
            String servStr = edtServiceFee.getText().toString().trim();

            if (month.isEmpty() || roomStr.isEmpty()) {
                Toast.makeText(this, "Nhập thông tin hóa đơn!", Toast.LENGTH_SHORT).show();
                return;
            }

            double roomFee = Double.parseDouble(roomStr);
            double elecFee = elecStr.isEmpty() ? 0 : Double.parseDouble(elecStr);
            double waterFee = waterStr.isEmpty() ? 0 : Double.parseDouble(waterStr);
            double servFee = servStr.isEmpty() ? 0 : Double.parseDouble(servStr);
            double total = roomFee + elecFee + waterFee + servFee;

            String qrPayload = VietQRHelper.generateQrPayload(101, total, "999988887777");

            boolean success = dbHelper.createInvoice(1, month, roomFee, elecFee, waterFee, servFee, qrPayload);
            if (success) {
                Toast.makeText(this, "Xuất hóa đơn thành công & Sinh VietQR!", Toast.LENGTH_SHORT).show();
                loadInvoices();
            }
        });
    }

    private void loadInvoices() {
        invoiceList = dbHelper.getAllInvoices();
        adapter = new InvoiceAdapter(this, invoiceList);
        rvInvoices.setAdapter(adapter);
    }
}

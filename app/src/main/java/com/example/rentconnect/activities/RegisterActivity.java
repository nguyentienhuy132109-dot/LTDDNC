package com.example.rentconnect.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rentconnect.R;
import com.example.rentconnect.database.RentConnectDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtPhone;
    private RadioGroup rgRole;
    private Button btnSubmitRegister;
    private RentConnectDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new RentConnectDbHelper(this);

        edtName = findViewById(R.id.edtRegName);
        edtEmail = findViewById(R.id.edtRegEmail);
        edtPassword = findViewById(R.id.edtRegPassword);
        edtPhone = findViewById(R.id.edtRegPhone);
        rgRole = findViewById(R.id.rgRole);
        btnSubmitRegister = findViewById(R.id.btnSubmitRegister);

        btnSubmitRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            int selectedRoleId = rgRole.getCheckedRadioButtonId();
            String role = "TENANT";
            if (selectedRoleId == R.id.rbLandlord) {
                role = "LANDLORD";
            }

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.registerUser(name, email, pass, phone, role);
            if (success) {
                Toast.makeText(this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

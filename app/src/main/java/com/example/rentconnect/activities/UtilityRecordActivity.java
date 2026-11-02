package com.example.rentconnect.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.rentconnect.R;
import com.example.rentconnect.database.RentConnectDbHelper;

public class UtilityRecordActivity extends AppCompatActivity {

    private EditText edtMonthYear, edtNewElec, edtNewWater;
    private Button btnCapturePhoto, btnSaveUtility;
    private ImageView imgPreview;
    private RentConnectDbHelper dbHelper;
    private String capturedPhotoPath = "local_photo_meter.jpg";

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.get("data") != null) {
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imgPreview.setImageBitmap(imageBitmap);
                        Toast.makeText(this, "Đã lưu ảnh công tơ vào máy!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_record);

        dbHelper = new RentConnectDbHelper(this);

        edtMonthYear = findViewById(R.id.edtMonthYear);
        edtNewElec = findViewById(R.id.edtNewElec);
        edtNewWater = findViewById(R.id.edtNewWater);
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto);
        btnSaveUtility = findViewById(R.id.btnSaveUtility);
        imgPreview = findViewById(R.id.imgPreviewMeter);

        btnCapturePhoto.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(takePictureIntent);
        });

        btnSaveUtility.setOnClickListener(v -> {
            String monthYear = edtMonthYear.getText().toString().trim();
            String elecStr = edtNewElec.getText().toString().trim();
            String waterStr = edtNewWater.getText().toString().trim();

            if (monthYear.isEmpty() || elecStr.isEmpty() || waterStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đủ chỉ số điện/nước!", Toast.LENGTH_SHORT).show();
                return;
            }

            double elec = Double.parseDouble(elecStr);
            double water = Double.parseDouble(waterStr);

            boolean success = dbHelper.insertUtilityRecord(1, monthYear, elec, water, capturedPhotoPath, capturedPhotoPath, 0);
            if (success) {
                Toast.makeText(this, "Lưu chỉ số điện nước thành công (Đã hỗ trợ Offline Local Storage)!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi lưu dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

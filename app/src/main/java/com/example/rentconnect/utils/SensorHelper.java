package com.example.rentconnect.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatDelegate;

public class SensorHelper implements SensorEventListener {

    public interface OnShakeListener {
        void onShakeDetected();
    }

    public interface OnLightChangeListener {
        void onLightModeChanged(boolean isDarkEnvironment, float luxValue);
    }

    private final SensorManager sensorManager;
    private final Sensor lightSensor;
    private final Sensor accelerometer;

    private OnShakeListener shakeListener;
    private OnLightChangeListener lightChangeListener;

    // Shake detection variables
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private long mShakeTimestamp;

    // Light theme toggle state control
    private boolean lastIsDarkState = false;

    public SensorHelper(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            lightSensor = null;
            accelerometer = null;
        }
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.shakeListener = listener;
    }

    public void setOnLightChangeListener(OnLightChangeListener listener) {
        this.lightChangeListener = listener;
    }

    public void register() {
        if (sensorManager != null) {
            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    public void unregister() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        // 1. Dùng Cảm biến Ánh sáng (TYPE_LIGHT) tối ưu hiển thị giao diện khi xem phòng trọ
        if (sensorType == Sensor.TYPE_LIGHT) {
            float lux = event.values[0];
            boolean isDark = lux < 15.0f; // Môi trường phòng tối / ánh sáng yếu
            if (isDark != lastIsDarkState) {
                lastIsDarkState = isDark;
                if (lightChangeListener != null) {
                    lightChangeListener.onLightModeChanged(isDark, lux);
                }
            }
        }

        // 2. Dùng Cảm biến Gia tốc (TYPE_ACCELEROMETER) cho tính năng "Lắc điện thoại để Refresh / Báo lỗi"
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // Tính gForce tổng hợp
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // Bỏ qua các rung lắc quá sát nhau (dưới 500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }
                mShakeTimestamp = now;
                if (shakeListener != null) {
                    shakeListener.onShakeDetected();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Không yêu cầu xử lý thêm
    }
}

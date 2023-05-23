package com.example.appsensores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor, temperatureSensor, humiditySensor, proximitySensor, motionSensor;
    private TextView temperatureText, luzText, umidadeText, proximidadeText;
    private ConstraintLayout layout;
    private boolean movimentoDetectado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        temperatureText = findViewById(R.id.tvTemperatura);
        luzText = findViewById(R.id.tvLuzConfirma);
        umidadeText = findViewById(R.id.tvUmidade);
        proximidadeText = findViewById(R.id.tvProximidade);

        if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, motionSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightLevel = event.values[0];

            // Mudança de luz altera a cor do background e texto//
            if (lightLevel < 50) {
                layout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                luzText.setText("Escuro");
            } else {
                layout.setBackgroundColor(getResources().getColor(android.R.color.white));
                luzText.setText("Claro");
            }
        } else if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            // Temperatura //
            float temperature = event.values[0];
            temperatureText.setText(String.format("%.2f°C", temperature));
        } else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            // Umidade //
            float humidity = event.values[0];
            umidadeText.setText(String.format("%.2f%%", humidity));
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            // Proximidade //
            float proximity = event.values[0];
            proximidadeText.setText(String.format("%.2f", proximity));
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Movimento //
            float x = event.values[0];
            float y = event.values[0];
            float z = event.values[0];

            if (x > 1 || y > 1 || z > 1 || x < -1 || y < -1 || z < -1) {
                // apagar campos ao virar tela //
                movimentoDetectado = true;
                LimparSensores();
            } else {
                movimentoDetectado = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void LimparSensores() {
        // apagar os campos de sensores //
        temperatureText.setText("");
        luzText.setText("");
        umidadeText.setText("");
        proximidadeText.setText("");
    }
}
package com.example.drno.android_app_1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import java.lang.Math;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.util.Half.EPSILON;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import android.os.StrictMode;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    MyTask mt;
    TextView tvInfo;

    private TextView textView;
    private Button button;

    private TextView textView_HTTP;

    private Sensor mTemperature;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";

    private TextView accelerometerlabel;
//    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;


    private TextView gyroscopelabel;
    private Sensor senGyroscope;
    // Create a constant to convert nanoseconds to seconds.
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private float timestamp;

    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mAccelerometer;

    private String Send_http;

    private final String USER_AGENT = "Mozilla/5.0";

    class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText("Begin");
        }

        @Override
        protected String doInBackground(Void... params) {

//                TimeUnit.SECONDS.sleep(2);
            String url = "http://31.220.63.13:5005/accelerometer";
            URL obj = null;
            try {
                obj = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) obj.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // optional default is GET
            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = 0;
            try {
                responseCode = con.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            response.append("");
            try {
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView_HTTP.setText(result);
            tvInfo.setText("End");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.textView_async_task);
        textView_HTTP = (TextView) findViewById(R.id.textView_HTTP);

//        нажатие выход из приложения
        button = (Button) findViewById(R.id.button_exit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });

        textView = (TextView) findViewById(R.id.textView_HTTP);


//        нажатие старт измерений
        button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              new MainActivity();
                onResume();

            }
        });
//        нажатие стоп измерений
        button = (Button) findViewById(R.id.button_stop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
            }
        });

        accelerometerlabel = (TextView) findViewById(R.id.textView_Accelerometer);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopelabel = (TextView) findViewById(R.id.textView_Gyroscope);
        senGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    public void onclick_async_task(View v) {
        mt = new MyTask();
        mt.execute();
    }


    public void sayHello(View view) throws IOException {
        textView_HTTP.setText("Текст должен быть изменен");
        TextView textv = (TextView) findViewById(R.id.textView_HTTP);
//        textView_HTTP.setText(response.toString());

    }

    private void getRandomNumber() {
        ArrayList numbersGenerated = new ArrayList();

        for (int i = 0; i < 6; i++) {
            Random randNumber = new Random();
            int iNumber = randNumber.nextInt(48) + 1;

            if (!numbersGenerated.contains(iNumber)) {
                numbersGenerated.add(iNumber);
            } else {
                i--;
            }
        }
    }

    // HTTP GET request
    private String sendGet(String URL) throws Exception {

        URL obj = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + URL);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
//        System.out.println(response.toString());
        return (response.toString());
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

//        if (mySensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
//            float ambient_temperature = sensorEvent.values[0];
//            temperaturelabel.setText("Окружающая температура:\n " + String.valueOf(ambient_temperature) + getResources().getString(R.string.celsius));
//        }

        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            float ambient_temperature = sensorEvent.values[0];
            // This timestep's delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (timestamp != 0) {
                final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = sensorEvent.values[0];
                float axisY = sensorEvent.values[1];
                float axisZ = sensorEvent.values[2];

                // Calculate the angular speed of the sample
                float omegaMagnitude = (float) sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                gyroscopelabel.setText("Гироскоп:\n X=" + String.valueOf(axisX) + ": Y=" + String.valueOf(axisY) + ": Z=" + String.valueOf(axisZ) + ": магнитуда=" + String.valueOf(omegaMagnitude));

                // Normalize the rotation vector if it's big enough to get the axis
                // (that is, EPSILON should represent your maximum allowable margin of error)
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                float sinThetaOverTwo = (float) sin(thetaOverTwo);
                float cosThetaOverTwo = (float) cos(thetaOverTwo);
                deltaRotationVector[0] = sinThetaOverTwo * axisX;
                deltaRotationVector[1] = sinThetaOverTwo * axisY;
                deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                deltaRotationVector[3] = cosThetaOverTwo;
            }
            timestamp = sensorEvent.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            // User code should concatenate the delta rotation we computed with the current rotation
            // in order to get the updated rotation.
            // rotationCurrent = rotationCurrent * deltaRotationMatrix;
        }


        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            accelerometerlabel.setText("Значения акселерометра:\n X=" + String.valueOf(x) + ": Y=" + String.valueOf(y) + ": Z=" + String.valueOf(z) + ".");

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    getRandomNumber();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(this);
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}

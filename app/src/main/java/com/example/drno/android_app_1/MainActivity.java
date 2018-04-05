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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.util.Half.EPSILON;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

import android.os.StrictMode;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


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

//    // Sensor static
//    static private SensorManager mSensorManager;
//    static private List<Sensor> deviceSensors;
//    static private Sensor mAccelerometer;
//    static private Sensor mGravity;
//    static private Sensor mGyroscope;
//    static private Sensor mLinearAcceleration;
//    static private Sensor mRotationVector;
//    static private Sensor mOrientation;
//    static private Sensor mMagneticField;
//    static private Sensor mProximity;
//    static private Sensor mPressure;
//    static private Sensor mLight;

//    private final SensorManager mSensorManager;
//    private final Sensor mAccelerometer;

    private SensorManager mSensorManager;
    private Sensor mLight;
    private Sensor mAccelerometer;

    private String Send_http;

    private final String USER_AGENT = "Mozilla/5.0";

//    public MainActivity() {
//    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
//    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//    }

//    private SensorEventListener mLightSensorListener = new SensorEventListener() {
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//            Log.d("MY_APP", event.toString());
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//            Log.d("MY_APP", sensor.toString() + " - " + accuracy);
//        }
//    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

//        new MainActivity();

        textView_HTTP =(TextView) findViewById(R.id.textView_HTTP);
//
//    //  Нажатие кнопки номер 2
//button=(Button) findViewById(R.id.button2);
//button.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        textView.setText("Пока");
//    }
//});

//        нажатие выход из приложения
        button =(Button) findViewById(R.id.button_exit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });

        textView=(TextView) findViewById(R.id.textView_HTTP);
//        нажатие выход отправка по http
//        button =(Button) findViewById(R.id.button_http);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                //
//
////                URL url = null;
////                try {
////                    url = new URL("http://www.android.com/");
////                } catch (MalformedURLException e) {
////                    e.printStackTrace();
////                }
////                HttpURLConnection urlConnection = null;
////                try {
////                    urlConnection = (HttpURLConnection) url.openConnection();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//                String url = "http://www.google.com/search?q=mkyong";
//                url="http://31.220.63.13:5005/accelerometer";
//                textView.setText("начал");
//                try {
//                    String message = (String) sendGet(url);
//
//                    textView.setText(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
////                    textView.setText();
//
//                }
//
//
////                try {
////                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
////                    textView.setText("http");
//////                    readStream(in);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                } finally {
////                    urlConnection.disconnect();
////                }
//
//
//            }
//        });


//        нажатие старт измерений
        button =(Button) findViewById(R.id.button_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              new MainActivity();
              onResume();

            }
        });
//        нажатие стоп измерений
        button =(Button) findViewById(R.id.button_stop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
            }
        });

//        temperaturelabel = (TextView) findViewById(R.id.myTemp);
//        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
//            mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);	// requires API level 14.
//        }
//        if (mTemperature == null) {
//            temperaturelabel.setText(NOT_SUPPORTED_MESSAGE);
//        }

        accelerometerlabel = (TextView) findViewById(R.id.textView_Accelerometer);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
   //     mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

//
        gyroscopelabel = (TextView) findViewById(R.id.textView_Gyroscope);
////        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        mSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);

//        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        senGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    public void sayHello(View view) throws IOException {
        textView_HTTP.setText("Текст должен быть изменен");
        TextView textv = (TextView)findViewById(R.id.textView_HTTP);
        String url="http://31.220.63.13:5005/accelerometer";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
        textView_HTTP.setText(response.toString());

    }

    private void getRandomNumber() {
        ArrayList numbersGenerated = new ArrayList();

        for (int i = 0; i < 6; i++) {
            Random randNumber = new Random();
            int iNumber = randNumber.nextInt(48) + 1;

            if(!numbersGenerated.contains(iNumber)) {
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
           return(response.toString());
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
                float omegaMagnitude = (float) sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

                gyroscopelabel.setText("Гироскоп:\n X="+String.valueOf(axisX)+": Y="+String.valueOf(axisY)+": Z="+String.valueOf(axisZ)+": магнитуда="+String.valueOf(omegaMagnitude));

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

            accelerometerlabel.setText("Значения акселерометра:\n X="+String.valueOf(x)+": Y="+String.valueOf(y)+": Z="+String.valueOf(z)+".");

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

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
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(this);
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}

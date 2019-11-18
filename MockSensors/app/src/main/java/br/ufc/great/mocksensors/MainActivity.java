package br.ufc.great.mocksensors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import br.ufc.great.mocksensors.loccam.ContextKeys;
import br.ufc.great.mocksensors.loccam.ContextListener;
import br.ufc.great.mocksensors.model.Device;
import br.ufc.great.mocksensors.model.DeviceAction;
import br.ufc.great.mocksensors.model.DeviceActionMessage;

public class MainActivity extends AppCompatActivity implements ContextListener {

    private Gson gson;
    private ArrayList<Device> devices;
    private LinkedHashMap<String, String> context;
    private static final String CTOKEN = "CMU-2019";
    private static final String COAP_SERVER_URL = "coap://18.229.202.214:5683/devices";

    private static boolean runUDL_Listener = true;

    private String[] myPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE,
            Manifest.permission_group.SENSORS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ContextManager.getInstance().connect(this,"MockSensorsApp_LoCCAM");
        //ContextManager.getInstance().registerListener(this);

        gson = new Gson();

        new UpdateContextTask().execute(30000l); // Task to update context for each 30 seconds
        new UDP_Listener().execute();                     // Task to listen broadcast messages

        context = new LinkedHashMap<String, String>();

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(myPermissions,123);
            }
        }else {
            startSnapshots();
        }

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        devices = createDevices(availableSensors);

        ListView list = (ListView) findViewById(R.id.listSensors);
        ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(this, android.R.layout.simple_list_item_1, devices);
        list.setAdapter(adapter);

        findViewById(R.id.btnSendReq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btnSendReq){
                    checkAndSendCoapRequest();
                }
            }
        });
    }

    public void checkAndSendCoapRequest(){
        String envText = ((EditText) findViewById(R.id.editText)).getText().toString().trim();
        if(envText.length() > 0) context.put("env", envText);

        WifiManager manager = (WifiManager) MainActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String sSID = info.getSSID();
        if(sSID.length() > 0) context.put("network", sSID);

        NetworkConfig.createStandardWithoutFile();
        CoapClient client = new CoapClient(COAP_SERVER_URL);
        for(Device device : devices){
            device.setContext(context);
            client.post(gson.toJson(device), MediaTypeRegistry.APPLICATION_JSON);
        }
        Toast.makeText(MainActivity.this, "CoAP Request Sent!", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("MissingPermission")
    public void startSnapshots(){
        SnapshotClient snapshot = Awareness.getSnapshotClient(MainActivity.this);
        System.out.println("Starting snapshots...");
        snapshot.getWeather().addOnSuccessListener(new OnSuccessListener<WeatherResponse>() {
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                context.put("temperature", String.format("%.2f", weatherResponse.getWeather().getTemperature(Weather.CELSIUS)));
                context.put("humidity", weatherResponse.getWeather().getHumidity() + "");
                context.put("feels", String.format("%.2f", weatherResponse.getWeather().getFeelsLikeTemperature(Weather.CELSIUS)));
            }
        });

        snapshot.getLocation().addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
            @Override
            public void onSuccess(LocationResponse locationResponse) {
                context.put("latitude", locationResponse.getLocation().getLatitude() + "");
                context.put("longitude", locationResponse.getLocation().getLongitude() + "");
            }
        });

        snapshot.getDetectedActivity().addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>() {
            @Override
            public void onSuccess(DetectedActivityResponse detectedActivityResponse) {
                String detectedActivity = null;

                switch (detectedActivityResponse.getActivityRecognitionResult().getMostProbableActivity().getType()){
                    case DetectedActivity.IN_VEHICLE: detectedActivity = "inVehicle"; break;
                    case DetectedActivity.ON_BICYCLE: detectedActivity = "onBicycle"; break;
                    case DetectedActivity.ON_FOOT:    detectedActivity = "onFoot";    break;
                    case DetectedActivity.STILL:      detectedActivity = "still";     break;
                    case DetectedActivity.TILTING:    detectedActivity = "tilting";   break;
                    case DetectedActivity.WALKING:    detectedActivity = "walking";   break;
                    case DetectedActivity.RUNNING:    detectedActivity = "running";   break;
                    default: detectedActivity = "unknown";
                }

                context.put("detectedActivity", detectedActivity);
            }
        });

        snapshot.getTimeIntervals().addOnSuccessListener(new OnSuccessListener<TimeIntervalsResponse>() {
            @Override
            public void onSuccess(TimeIntervalsResponse timeIntervalsResponse) {
                String interval = null;
                int[] intervals = timeIntervalsResponse.getTimeIntervals().getTimeIntervals();

                switch (intervals[0]){
                    case TimeFence.TIME_INTERVAL_WEEKDAY: interval = "weekday_"; break;
                    case TimeFence.TIME_INTERVAL_WEEKEND: interval = "weekend_"; break;
                    case TimeFence.TIME_INTERVAL_HOLIDAY: interval = "holiday_"; break;
                    default: interval = "unknown";
                }
                switch (intervals[1]){
                    case TimeFence.TIME_INTERVAL_MORNING:   interval += "morning";   break;
                    case TimeFence.TIME_INTERVAL_AFTERNOON: interval += "afternoon"; break;
                    case TimeFence.TIME_INTERVAL_EVENING:   interval += "evening";   break;
                    case TimeFence.TIME_INTERVAL_NIGHT:     interval += "night";     break;
                    default: interval = "unknown";
                }

                context.put("timeInterval", interval);
                Toast.makeText(MainActivity.this, "Context updated!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<Device> createDevices(List<Sensor> sensors){
        ArrayList<String> uuids = new ArrayList<>();
        ArrayList<Device> devices = new ArrayList<>();

        for(Sensor sensor : sensors){
            if(sensor != null
                    && !(sensor.getStringType().toLowerCase().contains("não calibrado") || sensor.getStringType().toLowerCase().contains("uncalibrated"))
                    && sensorTypeToString(sensor.getType()) != "Unknown"){

                String ip = getLocalIpAddress();
                String rType = sensorTypeToString(sensor.getType());
                String type = (rType.equals("Light") || rType.equals("AmbientTemperature")) ? "actuator" : "sensor";
                String uid = ip.replaceAll("\\.", "") + "_" + rType.toLowerCase().replaceAll(" ", "").trim();

                if(!uuids.contains(uid)){
                    devices.add(new Device(uid, type, rType, MediaTypeRegistry.APPLICATION_JSON, ip));
                }
            }
        }

        return devices;
    }

    private String sensorTypeToString(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                return "Accelerometer";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "AmbientTemperature";
            case Sensor.TYPE_TEMPERATURE:
                return "AmbientTemperature";
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                return "GameRotationVector";
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                return "GeomagneticRotationVector";
            case Sensor.TYPE_GRAVITY:
                return "Gravity";
            case Sensor.TYPE_GYROSCOPE:
                return "Gyroscope";
            case Sensor.TYPE_HEART_RATE:
                return "HeartRateMonitor";
            case Sensor.TYPE_LIGHT:
                return "Light";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "LinearAcceleration";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "MagneticField";
            case Sensor.TYPE_ORIENTATION:
                return "Orientation";
            case Sensor.TYPE_PRESSURE:
                return "Pressure";
            case Sensor.TYPE_PROXIMITY:
                return "Proximity";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "Humidity";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "RotationVector";
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                return "SignificantMotion";
            case Sensor.TYPE_STEP_COUNTER:
                return "StepCounter";
            case Sensor.TYPE_STEP_DETECTOR:
                return "StepDetector";
            default:
                return "Unknown";
        }
    }

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onContextReady(String data) {
        System.out.println("\n###############\n" + data + "\n###############\n");
        Log.d("LOCCAM", data);
    }

    @Override
    public String getContextKey() {
        return ContextKeys.PROXIMITY;
    }

    private final class UpdateContextTask extends AsyncTask<Long, String, String>{
        @Override
        protected String doInBackground(Long... waitParams) {
            Looper.prepare();
            while(true){
                try {
                    Thread.sleep(waitParams[0]);
                    MainActivity.this.startSnapshots();
                    System.out.println("Context: " + MainActivity.this.context);
                    MainActivity.this.checkAndSendCoapRequest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private final class UDP_Listener extends AsyncTask<Void, DeviceActionMessage, String>{
        @Override
        protected String doInBackground(Void... voids) {
            String json;
            int server_port = 4445;
            Gson gsonService = new Gson();
            byte[] message = new byte[1024];
            while(MainActivity.runUDL_Listener){
                try{
                    DatagramPacket p = new DatagramPacket(message, message.length);
                    DatagramSocket s = new DatagramSocket(server_port);
                    s.receive(p);
                    json = new String(message, 0, p.getLength());

                    DeviceActionMessage deviceActionMessage = gsonService.fromJson(json, DeviceActionMessage.class);

                    s.close();
                    publishProgress(deviceActionMessage);
                }catch(Exception e){
                    Log.d("UDP_Listener","Error: " + e.toString());
                }
            }
            return "UDL Listener Closed";
        }

        @Override
        protected void onProgressUpdate(DeviceActionMessage... progress) {
            DeviceActionMessage message = progress[0];
            Toast.makeText(MainActivity.this, message.toString(), Toast.LENGTH_LONG).show();
            if(message.getIps().isEmpty() || message.getIps().contains(getLocalIpAddress())){
                /* Checking the cToken*/
                if(message.getcToken().equals(MainActivity.CTOKEN) && !message.getActions().isEmpty()){
                    for(DeviceAction action : message.getActions()){
                        if(action.getType().equals("vibrate")){
                            int duration = (action.getDuration() > 0) ? action.getDuration() : 500;

                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                v.vibrate(duration); //deprecated in API 26
                            }
                        }else if(action.getType().equals("light")){
                            if(MainActivity.this.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                                try {
                                    CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                                    String cameraId = cameraManager.getCameraIdList()[0];
                                    int lightMode = action.getDuration();

                                    if(lightMode == 0){ // turn light off
                                        cameraManager.setTorchMode(cameraId, false);
                                    }else if(lightMode < 0){ // turn light on
                                        cameraManager.setTorchMode(cameraId, true);
                                    }else if(lightMode > 0){
                                        cameraManager.setTorchMode(cameraId, true);  // turn on
                                        Thread.sleep(lightMode);                             // wait a moment
                                        cameraManager.setTorchMode(cameraId, false); // turn off
                                    }
                                } catch (CameraAccessException e) {
                                    Log.d("UDP_Listener","Error: " + e.toString());
                                } catch (InterruptedException e) {
                                    Log.d("UDP_Listener","Error: " + e.toString());
                                }
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Unknown action type: " + action.getType(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
}
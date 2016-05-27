package es.uclm.paco.accelerometerclient;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ReadingSensorService extends IntentService implements SensorEventListener {

    private com.github.nkzawa.socketio.client.Socket mSocket;

    private static String NAME = "";

    private static SensorManager sensorService;
    private Sensor sensor;
    public static boolean stop;

    public ReadingSensorService() {
        super("ReadingSensorService");
    }




    @Override
    protected void onHandleIntent(Intent intent) {
        sensorService=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor=sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        NAME =intent.getStringExtra("name");
        stop=false;
        new Thread(new ClientThread()).start();
        if(sensor==null){
            Log.e("Service", "ERROR getting the sensor.");
        }else{
            sensorService.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    public static void stop(){
        stop =true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float[] values=event.values;
            float x=values[0];
            float y=values[1];
            float z=values[2];
            JSONObject js=new JSONObject();
            try {
                js.put("time", DateFormat.getDateTimeInstance().format(new Date()));
                js.put("x",x);
                js.put("y",y);
                js.put("z",z);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("accelerometer",js);
        }
        if(stop){
            sensorService.unregisterListener(this);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //http://zendlabs.deazweb.fr/nodejs/how-to-make-works-socket-ionodejs-on-c9-io/
    class ClientThread implements Runnable{
        @Override
        public void run(){
            try {
                mSocket = IO.socket("https://androidjs3-ipakto.c9users.io/");
                mSocket.connect();
                mSocket.emit("hello",NAME);
            } catch (URISyntaxException e) {}

        }
    }

}

package minion.kim.rv;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import android.util.Log;

public class RV extends ActionBarActivity implements SensorEventListener{

    int accelX, accelY, accelZ;

    byte[] _accval;

    int gyroX, gyroY, gyroZ;
    float roll, pitch, yaw;

    TextView list_g;
    TextView list_orientation;
    TextView list;

    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private Sensor accSensor;
    private Sensor orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        orientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        list = (TextView) findViewById(R.id.list);
        list_g = (TextView) findViewById(R.id.list_g);
        list_orientation = (TextView) findViewById(R.id.list_orientation);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rv, menu);
        return true;
    }

    public void onSensorChanged(SensorEvent event){
        Sensor sensor = event.sensor;
        String acc;
        float[] orient = {};

        if(sensor.getType() == Sensor.TYPE_ORIENTATION){

            yaw = event.values[0];
            pitch = event.values[1];
            roll = event.values[2];

            new Thread(new client_UDP(yaw, pitch, roll)).start();

            //Log.d("debug","mAzimuth :"+Float.toString(yaw));
            //Log.d("debug","mPitch :"+Float.toString(pitch));
            //Log.d("debug", "mRoll :"+Float.toString(roll));

            //System.out.println("yaw : " + yaw + "pitch : " + pitch + "roll : " + roll);

            list_orientation.setText("yaw : " + yaw + "\n" + "pitch : " + pitch + "\n" + "roll : " + roll);
        }

        if(sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyroX = Math.round(event.values[0] * 1000);
            gyroY = Math.round(event.values[1] * 1000);
            gyroZ = Math.round(event.values[2] * 1000);
            //System.out.println("gyroX = "+gyroX);
            //System.out.println("gyroY = "+gyroY);
            //System.out.println("gyroZ = "+gyroZ);
        }

        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelX = Math.round(event.values[0] * 1000);
            accelY = Math.round(event.values[1] * 1000);
            accelZ = Math.round(event.values[2] * 1000);

            acc = ("Acc_X-Axis : " + accelX + "\n" +"Acc_Y-Axis : " + accelY + "\n" +"Acc_Z-Axis : " + accelZ);
            list.setText(acc);

            _accval = acc.getBytes();
            //new Thread(new Client(_accval)).start();
            //UDP.send_pkt(_accval);

            list_g.setText("Gyro_X-Axis : " + gyroX + "\n" +"Gyro_Y-Axis : " + gyroY + "\n" +"Gyro_Z-Axis : " + gyroZ);
            // System.out.println("accX = "+accelX);
            // System.out.println("accY = "+accelY);
            // System.out.println("accZ = "+accelZ);

        }
    }

    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, orientation,SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package es.uclm.paco.accelerometerclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class StopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
    }

    public void stopService(View view){
        //Stop service and unregister listener
        stopService(new Intent(this,ReadingSensorService.class));
        ReadingSensorService.stop();
        Toast.makeText(this, "Stop sending values.",Toast.LENGTH_SHORT).show();
        //Finish this activity
        finish();
    }
}

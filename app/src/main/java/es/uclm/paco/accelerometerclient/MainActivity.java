package es.uclm.paco.accelerometerclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Prueba
    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name =(EditText)findViewById(R.id.txtName);
    }

    public void launchService(View view){
        //Start ReadingSensorService and send name of the user
        Intent mServiceIntent= new Intent(this, ReadingSensorService.class);
        Bundle b=new Bundle();
        b.putString("name", name.getText().toString());
        mServiceIntent.putExtras(b);
        startService(mServiceIntent);
        //Start StopActivity
        Intent intent = new Intent(this, StopActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Sending values to https://androidjs3-ipakto.c9users.io/",Toast.LENGTH_SHORT).show();
    }

}

package com.fcs.fcspos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fcs.fcspos.ui.activities.PositionActivity;


public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPos1 = findViewById(R.id.btnPos1);
        Button btnPos2 = findViewById(R.id.btnPos2);
        Button btnPos3 = findViewById(R.id.btnPos3);
        Button btnPos4 = findViewById(R.id.btnPos4);

        btnPos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PositionActivity.class);
                i.putExtra("position",(byte) 1);
                startActivity(i);
            }
        });
        btnPos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PositionActivity.class);
                i.putExtra("position",(byte) 2);
                startActivity(i);
            }
        });
        btnPos3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Posicion 3 no habilitada", Toast.LENGTH_SHORT).show();
            }
        });
        btnPos4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Posicion 4 no habilitada", Toast.LENGTH_SHORT).show();
            }
        });


    }

}

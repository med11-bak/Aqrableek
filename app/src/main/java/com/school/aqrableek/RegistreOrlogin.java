package com.school.aqrableek;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aqrableek.R;

public class RegistreOrlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre_orlogin);
        Button conect = findViewById(R.id.seconnect);
        Button registre = findViewById(R.id.registre);
        String us = "";
        SharedPreferences sp = getSharedPreferences("currentid",Activity.MODE_PRIVATE);
        us = sp.getString("cru",null);
        try {
            if (us != null) {
                startActivity(new Intent(getApplicationContext(), Menu.class));
            }
        }catch(Exception ignored){
        }

        conect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        registre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreataAcc.class));
            }
        });


    }
}
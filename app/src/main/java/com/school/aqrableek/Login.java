package com.school.aqrableek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aqrableek.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText tel,Pas;
    Button login;
    ProgressBar prg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ini();



        //btn login

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prg.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(tel.getText().toString()+"@gmail.com",Pas.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String currentuserid = user.getUid();
                            SharedPreferences sp = getSharedPreferences("currentid", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("cru",currentuserid);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), Menu.class));
                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }


    public void ini(){
        tel = findViewById(R.id.tel);
        Pas=findViewById(R.id.pass);
        mAuth = FirebaseAuth.getInstance();
        prg=findViewById(R.id.prg);
        login = findViewById(R.id.login);
        prg.setVisibility(View.GONE);
    }
}
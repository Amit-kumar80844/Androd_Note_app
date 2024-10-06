package com.example.noteapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.concurrent.Delayed;

public class LoginActivity extends AppCompatActivity {
    Button btn_login_login;
    LinearLayout login_top,login_middle_login;
    TextView btn_signup_login,text_password_login,text_email_login;
    ProgressBar processbar_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_login_login=findViewById(R.id.btn_login_login);
        login_middle_login=findViewById(R.id.login_middle_login);
        login_top= findViewById(R.id.login_top);
        btn_signup_login=findViewById(R.id.btn_signup_login);
        processbar_login=findViewById(R.id.processbar_login);
        text_password_login=findViewById(R.id.text_password_login);
        text_email_login=findViewById(R.id.text_email_login);
        //code for login to signup
       btn_login_login.setOnClickListener((v ->login_user(text_email_login.getText().toString(),text_password_login.getText().toString())));
        btn_signup_login.setOnClickListener(v->logintosignup());

    }
    void visibility(boolean b) {
        if (b == true) {
            login_top.setVisibility(View.GONE);
            login_middle_login.setVisibility(View.GONE);
            processbar_login.setVisibility(View.VISIBLE);
        } else {
           processbar_login.setVisibility(View.GONE);
        }
    }
    void logintosignup(){
        Intent intent= new Intent(this, signup_Activity.class);
        startActivity(intent);
        finish();
    }

    void login_user(String email,String password){
        if (email.isEmpty() || password.isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_email_login.setError("Invalid email");
            return;
        }
      login_infirebase(email,password);
    }
    void login_infirebase(String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_LONG);
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"You have not any account ",Toast.LENGTH_LONG);
                    Intent intent = new Intent(LoginActivity.this,signup_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
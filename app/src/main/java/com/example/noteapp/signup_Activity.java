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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class signup_Activity extends AppCompatActivity {
    Button btn_signup;
    TextView btn_login, text_email, text_password, text_confirm;
    ProgressBar progressBar_signup;
    LinearLayout signup_middle, signup_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Ensure this method exists
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_signup), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login);  // Initialize the btn_login view
        text_email = findViewById(R.id.text_email);
        text_password = findViewById(R.id.text_password);
        text_confirm = findViewById(R.id.text_confirm);
        signup_top = findViewById(R.id.signup_top);  // Initialize other views
        signup_middle = findViewById(R.id.signup_middle);
        progressBar_signup = findViewById(R.id.processbar_signup);

        btn_signup.setOnClickListener(v -> createaccount(text_email.getText().toString(), text_password.getText().toString(), text_confirm.getText().toString()));
        //we will add a function to make it more better
        btn_login.setOnClickListener(v ->signuptologin());
    }
    void signuptologin(){
        Intent intent= new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    void createaccount(String email, String password, String confirm) {
        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_email.setError("Invalid email");
            return;
        }
        if (!password.equals(confirm) || password.length() < 4) {
            text_password.setError("Password is either short or not matches");
            return;
        }
        createaccountinfirebase(email, password);
    }
    void createaccountinfirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_LONG).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            visibility(true);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            visibility(false);
                        }
                    }
                });
    }
    void visibility(boolean b) {
        if (b) {
            signup_top.setVisibility(View.GONE);
            signup_middle.setVisibility(View.GONE);
            progressBar_signup.setVisibility(View.VISIBLE);
        } else {
            progressBar_signup.setVisibility(View.GONE);
        }
    }
}

package com.inzucorp.artistly;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

// declare layout ids
    EditText etLoginEmail, etLoginPassword;
    TextView tvLoginSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    // initialize layout ids
        etLoginEmail = findViewById(R.id.etLogin_Email);
        etLoginPassword = findViewById(R.id.etLogin_Password);
        tvLoginSignIn  = findViewById(R.id.tvLogin_SignIn);

    // set onClickListeners
        tvLoginSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etLoginEmail.getText().toString();
                final String password = etLoginPassword.getText().toString();

            // next 2 if statements check to see if any field is empty
                if (password.isEmpty()) {
                    etLoginPassword.setError("Please enter a password");
                    etLoginPassword.requestFocus();
                }
                if (email.isEmpty()) {
                    etLoginEmail.setError("Please enter a email");
                    etLoginEmail.requestFocus();
                }
            // if no field is empty, we attempt to sign in
                if (!email.isEmpty() && !password.isEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign in Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(LoginActivity.this, ExploreActivity.class));
                            }
                        }
                    });
                }
            }
        });
    }
}

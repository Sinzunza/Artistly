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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Classes.artistlyDB;
import Classes.userDB;

public class CreateAccountActivity extends AppCompatActivity {
    EditText etCreateAccountUsername, etCreateAccountEmail, etCreateAccountPassword;
    TextView tvCreateAccountCreateAccount, tvCreateAccountSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etCreateAccountUsername = findViewById(R.id.tvCreateAccount_Username);
        etCreateAccountEmail = findViewById(R.id.etCreateAccount_Email);
        etCreateAccountPassword = findViewById(R.id.etCreateAccount_Password);
        tvCreateAccountCreateAccount = findViewById(R.id.tvCreateAccount_CreateAccount);
        tvCreateAccountSignIn  = findViewById(R.id.tvCreateAcount_SignIn);

        tvCreateAccountCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etCreateAccountEmail.getText().toString();
                final String password =  etCreateAccountPassword.getText().toString();
                final String username = etCreateAccountUsername.getText().toString();
                final String usernameLowerCase = username.toLowerCase();

            // next 3 if statements check to see if any field is empty
                if (password.isEmpty()) {
                    etCreateAccountPassword.setError("Please enter a password");
                    etCreateAccountPassword.requestFocus();
                }
                if (email.isEmpty()) {
                    etCreateAccountEmail.setError("Please enter a email");
                    etCreateAccountEmail.requestFocus();
                }
                if (username.isEmpty()) {
                    etCreateAccountUsername.setError("Please enter a username");
                    etCreateAccountUsername.requestFocus();
                }
            // if no field is empty, we then check if username is available & attempt to create the account
                if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty()) {
                // query the database to search if username is already being used
                    final Query usernameQuery = artistlyDB.equalToQuery("Users", "usernameLowerCase", usernameLowerCase);
                    usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // if username is already being used snapshot children will equal 1, don't create account
                            if (snapshot.getChildrenCount() == 1) {
                                Toast.makeText(CreateAccountActivity.this, "Username already exists, choose a different one",Toast.LENGTH_SHORT).show();
                            }
                        // if username hasn't been used we can finally create the account
                            else {
                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(CreateAccountActivity.this, "Sign Up Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                                        } else {
                                            userDB theUserDB = new userDB();
                                            Map newPost = new HashMap();
                                            newPost.put("username", username);
                                            newPost.put("usernameLowerCase", usernameLowerCase);
                                            newPost.put("name", "");
                                            newPost.put("typeArtist", "None");
                                            newPost.put("bio", "");
                                            newPost.put("profilePhoto", "https://firebasestorage.googleapis.com/v0/b/artistly-a2bcc.appspot.com/o/profilePhotos%2FdefaultProfilePhoto.png?alt=media&token=b6ded1a3-e414-4e24-900b-becec1a79fc3");
                                            newPost.put("followers", "");
                                            newPost.put("following", "");
                                            theUserDB.newVals(newPost);
                                            startActivity(new Intent(CreateAccountActivity.this, TypeArtistActivity.class));
                                        }
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            // if none are empty yet it got here then there clearly was an error somewhere
                else {
                    Toast.makeText(CreateAccountActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    // Set OnClickListeners
        tvCreateAccountSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
            }
        });
    }
}

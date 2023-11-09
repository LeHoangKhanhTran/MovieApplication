package com.example.movieapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("users");
        TextView usernameTV = findViewById(R.id.username_input);
        TextView emailTV = findViewById(R.id.email_input);
        TextView passwordTV = findViewById(R.id.password_input);
        Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTV.getText().toString();
                String email = emailTV.getText().toString();
                String password = passwordTV.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (auth.getCurrentUser() != null)
                        {
                            String id = auth.getCurrentUser().getUid();
                            User user = new User(email, username);
                            Map<String, User> users = new HashMap<>();
                            users.put(id, user);
                            reference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                                        String oldId = child.getKey();
                                        GenericTypeIndicator<User> t = new GenericTypeIndicator<User>() {};
                                        User oldUser = child.getValue(t);
                                        users.put(oldId,oldUser);
                                    }
                                    reference.setValue(users);
                                    Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            });

                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        TextView loginTV = findViewById(R.id.login_text);
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.appointmentwithsecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    ImageButton btnLogOut;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnLogOut = findViewById(R.id.logoutBtn);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(Home.this, MainActivity.class));
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user == null){
            startActivity(new Intent(Home.this, MainActivity.class));
        }
    }

    public void gotoForm(View v) {
        Button b = (Button)v;
        String buttonText = b.getText().toString();
        Intent i = new Intent(Home.this, Form.class);
        i.putExtra("meet", buttonText);
        startActivity(i);
    }
}
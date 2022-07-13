package com.example.paperlizardspock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RulesDemonstrationActivity extends AppCompatActivity {
Button ok_demo_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules_demonstration);

        ok_demo_button = findViewById(R.id.ok_demo_button);

        ok_demo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMenuActivity = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(openMenuActivity);
            }
        });

    }
}
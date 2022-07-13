package com.example.paperlizardspock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------- Login button -----------
        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Intent openLoginActivity = new Intent(getApplicationContext(),LoginActivity.class);
             //   startActivity(openLoginActivity);

                Intent openLoginActivity = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(openLoginActivity);
            }
        });

    }
}
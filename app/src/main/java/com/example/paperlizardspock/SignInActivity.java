package com.example.paperlizardspock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String TAG = "Test";
    EditText TB_sign_in_email;
    EditText TB_sign_in_password;
    Button sign_in_validation_button;
  //  TextView debug_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        sign_in_validation_button = findViewById(R.id.sign_in_validation_button);
        TB_sign_in_email = findViewById(R.id.TB_sign_in_email);
        TB_sign_in_password = findViewById(R.id.TB_sign_in_password);
  //      debug_TV = findViewById(R.id.debug_TV);
        sign_in_validation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(TB_sign_in_email.getText().toString(), TB_sign_in_password.getText().toString());
            }
        });

    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                //            debug_TV.setText("task successfull");
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                 //           debug_TV.setText("task failed");

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user) {

        if (user != null) {
            AlertDialog.Builder SignedInAlert  = new AlertDialog.Builder(SignInActivity.this);
            SignedInAlert.setMessage("Vous pouvez commencer à jouer.");
            SignedInAlert.setTitle("Bienvenue!");
            SignedInAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent openMenu = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(openMenu);
                }
            });
            SignedInAlert.setCancelable(true);
            SignedInAlert.create().show();
        } else {
            AlertDialog.Builder errorOnSignInAlert  = new AlertDialog.Builder(SignInActivity.this);
            errorOnSignInAlert.setMessage("Un problème est survenu, veuillez rééssayer plus tard.");
            errorOnSignInAlert.setTitle("Erreur dans l'identifiant ou dans le mot de passe.");
            errorOnSignInAlert.setPositiveButton("OK", null);
            errorOnSignInAlert.setCancelable(true);
            errorOnSignInAlert.create().show();


        }
    }
}
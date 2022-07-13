package com.example.paperlizardspock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    String TAG = "Test";
    EditText TB_pseudo;
    EditText TB_email;
    EditText TB__password;
    EditText TB_password_confirmation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TB_email = findViewById(R.id.TB_email);
        TB_pseudo = findViewById(R.id.TB_pseudo);
        TB__password = findViewById(R.id.TB_password);
        TB_password_confirmation = findViewById(R.id.TB_password_confirmation);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // --------------- Validate account button -----------------------
        Button validate_account_button =(Button) findViewById(R.id.validate_account_button);
        validate_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TB__password.getText().toString().equals(TB_password_confirmation.getText().toString())){        // si le password et la confirmation du password sont égaux
                    createAccount(TB_email.getText().toString(), TB__password.getText().toString());
              }else{
                    AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(LoginActivity.this);
                    dlgAlert.setMessage("Le mot de passe est différent de sa confirmation.");
                    dlgAlert.setTitle("Erreur mot de passe");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }

            }
        });

        // --------------- Sign in button --------------------------------
        Button sign_in_button =(Button) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSignInActivity = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(openSignInActivity);
            }
        });


    }

    public void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            addData();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.validate_account_button).setVisibility(View.GONE);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            AlertDialog.Builder accountCreatedAlert  = new AlertDialog.Builder(LoginActivity.this);
            accountCreatedAlert.setMessage("Votre compte a bien été créé.");
            accountCreatedAlert.setTitle("Compte créé!");
            accountCreatedAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent openDemo = new Intent(getApplicationContext(), RulesDemonstrationActivity.class);
                    startActivity(openDemo);
                }
            });

            accountCreatedAlert.setCancelable(true);
            accountCreatedAlert.create().show();
        //    button_go_to_demo.setVisibility(View.VISIBLE);

        } else {
            AlertDialog.Builder errorOnCreationAlert  = new AlertDialog.Builder(LoginActivity.this);
            errorOnCreationAlert.setMessage("Un problème est survenu, veuillez rééssayer plus tard.");
            errorOnCreationAlert.setTitle("Erreur à la création du compte!");
            errorOnCreationAlert.setPositiveButton("OK", null);
            errorOnCreationAlert.setCancelable(true);
            errorOnCreationAlert.create().show();
        }
    }

    public void addData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Map<String, Object> player = new HashMap<>();
        player.put("name", TB_pseudo.getText().toString());
        player.put("score", "0");

        db.collection("players").document(currentUser.getUid())
                .set(player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }
}
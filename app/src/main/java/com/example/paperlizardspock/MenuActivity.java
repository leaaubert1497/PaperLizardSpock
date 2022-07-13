package com.example.paperlizardspock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.protobuf.StringValue;

import org.w3c.dom.Text;

import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView TV_bienvenue_menu;
    String TAG;
    String nomJoueur;
    String scoreJoueurCurrent;
    TextView TV_hallOfFame;
    String nomScore;
    String score;
    String TB_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        TAG = "test";
        TB_content="";
        TV_bienvenue_menu = findViewById(R.id.TV_bienvenue_menu);
        TV_hallOfFame = findViewById(R.id.TV_hall_of_fame);

        setName();
        setScores();




        // ----------------------- Spinner ---------------------------------------------------

        //  Spinner difficulties
        Spinner spinner = (Spinner) findViewById(R.id.spinner_difficulty);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulties_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        // ----------------------- Button Start ----------------------------------------------
        Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String game_difficulty = spinner.getSelectedItem().toString();
                Intent openGameBoardActivity3 = new Intent(getApplicationContext(),GameBoardActivity.class);
                openGameBoardActivity3.putExtra("GAME_DIFFICULTY", game_difficulty);
                startActivity(openGameBoardActivity3);
            }
        });




    }

    public void setScores(){
        db.collection("players").orderBy("score", Query.Direction.DESCENDING).limit(5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                nomScore = (String)document.getString("name");
                                score = (String)document.getString("score");
                                TB_content = TB_content + "\n" + nomScore+"   " + score;

                                Log.d(TAG, "joueur" + nomScore + score);

                                TV_hallOfFame.setText(TB_content);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void setName(){

        // ---------------- Récupération profil du joueur pour avoir son nom ---------------------------

        FirebaseUser currentUser = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("players").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        nomJoueur = (String)document.getString("name");
                        Log.d(TAG,"Nom joueur : "+ nomJoueur);
                        scoreJoueurCurrent = (String)document.getString("score");
                        TV_bienvenue_menu.setText("Bienvenue " + nomJoueur+", votre score actuel est de "+ scoreJoueurCurrent+" points. Choisissez une diffilculté de jeu! ");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });
    }
}
package com.example.paperlizardspock;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class GameBoardActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;


    TextView TV_bot_answer;
    TextView TV_manche_nb;
    TextView TV_difficulty_nb;
    TextView TV_player_score;
    TextView TV_bot_score;
    TextView TV_final;
    TextView TV_player_name;
    Button retry_button;
    ImageView imageBot;
    ImageView imageSheldon;
    ImageView gifSign;
    ImageView img_rock;
    ImageView img_paper;
    ImageView img_scissors;
    ImageView img_lizard;
    ImageView img_spock;
    ImageButton help_button;
    String nomJoueur;
    String TAG;
    static String scoreJoueur;
    String gameDifficulty;
    Boolean gameOver;

    int scoreJoueurInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        TAG = "Test";
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        getCurrentScore();
        getPlayerName();
        gameOver = false;

        // ---------------------- Mise en place interface ---------------------------

        TV_bot_answer = findViewById(R.id.TV_bot_answer);
        TV_manche_nb = findViewById(R.id.TV_manche_nb);
        TV_difficulty_nb = findViewById(R.id.TV_difficulty_nb);
        TV_player_score = findViewById(R.id.TV_player_score);
        TV_bot_score = findViewById(R.id.TV_bot_score);
        TV_final = findViewById(R.id.TV_final);
        TV_player_name = findViewById(R.id.TV_player_name);
        retry_button = findViewById(R.id.retry_button);
        help_button = findViewById(R.id.help_button);


        imageBot = findViewById(R.id.img_bot_result);
        imageSheldon = findViewById(R.id.image_sheldon);
        gifSign = findViewById(R.id.gifSigns);
        img_rock = findViewById(R.id.img_rock);
        img_paper = findViewById(R.id.img_paper);
        img_scissors = findViewById(R.id.img_scissors);
        img_lizard = findViewById(R.id.img_lizard);
        img_spock = findViewById(R.id.img_spock);

        gifSign.setVisibility(View.INVISIBLE);
        imageSheldon.setVisibility(View.INVISIBLE);

        // Récupération et afichage de la difficulté à partir de MenuActivity
        Bundle bundle = getIntent().getExtras();
        gameDifficulty = bundle.getString("GAME_DIFFICULTY");

        TV_difficulty_nb.setText(gameDifficulty);                                             // on l'affiche dans la TextView prévue à cet effet


        // ------------------------ Help Button -------------------------------
        help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent openRulesDemonstrationInActivity2 = new Intent(getApplicationContext(), RulesDemonstrationActivity.class);
                //openRulesDemonstrationInActivity2.putExtra("IS_GAME_ON", gameInProgress);
               // startActivity(openRulesDemonstrationInActivity2);

                    AlertDialog.Builder rules  = new AlertDialog.Builder(GameBoardActivity.this);
                    rules.setMessage("Les ciseaux coupent le papier. \n" +
                            "Le papier bat la pierre.\n" +
                            "La pierre écrase le lézard.\n" +
                            "Le lézard empoisonne Spock.\n" +
                            "Spock écrabouille les ciseaux.\n" +
                            "Les ciseaux décapitent le papier.\n" +
                            "Le papier repousse Spock.\n" +
                            "Spock détruit la pierre.\n" +
                            "La pierre bat les ciseaux");
                    rules.setTitle("Voici un rappel des règles.");
                    rules.setPositiveButton("OK", null);
                    rules.setCancelable(true);
                    rules.create().show();
                }

        });

        // ----------------------- Retry Button ------------------------------
        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver = false;
                TV_bot_answer.setVisibility(View.INVISIBLE);
                TV_bot_score.setText("0");
                TV_player_score.setText("0");
                TV_manche_nb.setText("1");
                imageBot.setImageResource(android.R.color.transparent);
                img_paper.setColorFilter(null);
                img_lizard.setColorFilter(null);
                img_scissors.setColorFilter(null);
                img_rock.setColorFilter(null);
                img_spock.setColorFilter(null);
                retry_button.setVisibility(View.INVISIBLE);
                imageSheldon.setVisibility(View.INVISIBLE);
                TV_final.setVisibility(View.INVISIBLE);

            }
        });

        // ----------------------- Rock --------------------------------------
        img_rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!gameOver) {
                    // mettre à jour les filtres de couleur, changer visibilité des images
                    formatForNewManche(img_rock, img_lizard, img_paper, img_scissors, img_spock);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {                // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                        public void run() {
                            TV_bot_answer.setVisibility(View.VISIBLE);
                            switch (gameDifficulty) {
                                case "1":
                                    actionsRock(0.75);
                                    break;
                                case "2":
                                    actionsRock(0.50);
                                    break;
                                case "3":
                                    actionsRock(0.25);
                                    break;
                            }
                        }
                    }, 2000);

                }
            }
        });

        // ---------------------- Paper ---------------------------------------
        img_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {
                    // mettre à jour les filtres de couleur, changer visibilité des images
                    formatForNewManche(img_paper, img_lizard, img_rock, img_scissors, img_spock);

                    Handler handler = new Handler();                // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            TV_bot_answer.setVisibility(View.VISIBLE);
                            switch (gameDifficulty) {
                                case "1":
                                    actionsPaper(0.75);
                                    break;
                                case "2":
                                    actionsPaper(0.50);
                                    break;
                                case "3":
                                    actionsPaper(0.25);
                                    break;
                            }
                        }
                    }, 2000);
                }
            }
        });

        // --------------------- Scissors -------------------------------------
        img_scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {

                    // mettre à jour les filtres de couleur, changer visibilité des images
                    formatForNewManche(img_scissors, img_lizard, img_rock, img_paper, img_spock);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {                    // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                        public void run() {
                            TV_bot_answer.setVisibility(View.VISIBLE);
                            switch (gameDifficulty) {
                                case "1":
                                    actionsScissors(0.75);
                                    break;
                                case "2":
                                    actionsScissors(0.50);
                                    break;
                                case "3":
                                    actionsScissors(0.25);
                                    break;
                            }
                        }
                    }, 2000);
                }
            }
        });

        // ------------------- Lizard ------------------------------------------
        img_lizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {

                    // mettre à jour les filtres de couleur, changer visibilité des images
                    formatForNewManche(img_lizard, img_paper, img_rock, img_scissors, img_spock);

                    Handler handler = new Handler();                    // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            TV_bot_answer.setVisibility(View.VISIBLE);
                            switch (gameDifficulty) {
                                case "1":
                                    actionsLizard(0.75);
                                    break;
                                case "2":
                                    actionsLizard(0.50);
                                    break;
                                case "3":
                                    actionsLizard(0.25);
                                    break;
                            }
                        }
                    }, 2000);
                }
            }
        });

        // --------------------- Spock -----------------------------------------
        img_spock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {

                    // mettre à jour les filtres de couleur, changer visibilité des images
                    formatForNewManche(img_spock, img_lizard, img_rock, img_scissors, img_paper);

                    Handler handler = new Handler();                        // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            TV_bot_answer.setVisibility(View.VISIBLE);
                            switch (gameDifficulty) {
                                case "1":
                                    actionsSpock(0.75);
                                    break;
                                case "2":
                                    actionsSpock(0.50);
                                    break;
                                case "3":
                                    actionsSpock(0.25);
                                    break;
                            }
                        }
                    }, 2000);
                }
            }
        });


    }

    // *****************************************************************************************************************************************
    //                                                             M E T H O D E S
    // *****************************************************************************************************************************************






    public void actionsRock(double difficulty_percentage) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE); // on fait disparaître le gif
        if (d < difficulty_percentage) {                  // si le joueur gagne
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_rock).length);  // On accède au tableau de perdants pour la pierre
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_rock);
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins();

        } else {                                          // si le joueur ne gagne pas
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_rock).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_rock);                       // On accède au tableau de gagnants pour la pierre
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            if (indexToGetImageFrom == 3) {                                                    // match nul (draw)
                draw();
            } else {                                                                          //  si victoire bot
                // update du score et de la manche
                playerLooses();
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString()) == 3 || Integer.parseInt(TV_player_score.getText().toString()) == 3) {         // si l'un des deux a un score de 3
            endOfGame();         // appel fonction fin de jeu
        }

    }

    public void actionsPaper(double difficulty_percentage) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_paper).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_paper);                         // On accède au tableau de gagnants pour le papier
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins();

        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_paper).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_paper);                         // On accède au tableau de perdants pour le papier
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if (indexToGetImageFrom == 2) {
                draw();
            } else {
                // update du score et de la manche
                playerLooses();
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString()) == 3 || Integer.parseInt(TV_player_score.getText().toString()) == 3) {         // si l'un des deux a un score de 3
            endOfGame();         // appel fonction fin de jeu
        }
    }

    public void actionsScissors(double difficulty_percentage) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_scissors).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_scissors);                         // On accède au tableau de gagnants pour les ciseaux
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins();


        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_scissors).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_scissors);                         // On accède au tableau de perdants pour les ciseaux
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if (indexToGetImageFrom == 2) {
                draw();
            } else {
                // update du score et de la manche
                playerLooses();
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString()) == 3 || Integer.parseInt(TV_player_score.getText().toString()) == 3) {         // si l'un des deux a un score de 3
            endOfGame();         // appel fonction fin de jeu
        }
    }

    public void actionsLizard(double difficulty_percentage) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_lizard).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_lizard);                         // On accède au tableau de gagnants pour le lézard
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins();

        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_lizard).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_lizard);                         // On accède au tableau de perdants pour le lézard
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if (indexToGetImageFrom == 2) {
                draw();
            } else {
                // update du score et de la manche
                playerLooses();
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString()) == 3 || Integer.parseInt(TV_player_score.getText().toString()) == 3) {         // si l'un des deux a un score de 3
            endOfGame();         // appel fonction fin de jeu
        }
    }

    public void actionsSpock(double difficulty_percentage) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_spock).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_spock);                         // On accède au tableau de gagnants pour Spock
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins();


        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_spock).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_spock);                         // On accède au tableau de perdants pour Spock
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if (indexToGetImageFrom == 2) {
                draw();
            } else {
                // update du score et de la manche
                playerLooses();
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString()) == 3 || Integer.parseInt(TV_player_score.getText().toString()) == 3) {         // si l'un des deux a un score de 3
            endOfGame();         // appel fonction fin de jeu
        }
    }

    public void endOfGame() {
     //   getCurrentScore();
        gameOver = true;


        retry_button.setVisibility(View.VISIBLE);
        TV_final.setVisibility((View.VISIBLE));
        imageSheldon.setVisibility(View.VISIBLE);

        int score_bot = Integer.parseInt(TV_bot_score.getText().toString());
        int score_player = Integer.parseInt(TV_player_score.getText().toString());
        if (score_bot < score_player) {                     // si victoire joueur
            TV_final.setText(getString(R.string.player_victory));
            imageSheldon.setImageResource(R.drawable.sheldon_win_game);
            try {
                Log.d(TAG, "Score joueur endof = " + scoreJoueur);
                scoreJoueurInt = Integer.parseInt(scoreJoueur);
            } catch(NumberFormatException nfe) {
                System.out.println("Could not parse " + nfe);
            }
            try {
                switch(gameDifficulty) {
                    case "1":
                        getCurrentScore();
                        scoreJoueurInt = scoreJoueurInt +1;
                        scoreJoueur = String.valueOf(scoreJoueurInt);
                        updatePlayersScore(scoreJoueur);
                        break;
                    case "2":
                        getCurrentScore();
                        scoreJoueurInt = scoreJoueurInt +3;
                        scoreJoueur = String.valueOf(scoreJoueurInt);
                        updatePlayersScore(scoreJoueur);
                        break;
                    case "3" :
                        getCurrentScore();
                        scoreJoueurInt = scoreJoueurInt +5;
                        scoreJoueur = String.valueOf(scoreJoueurInt);
                        updatePlayersScore(scoreJoueur);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {                                         // si victoire bot
            //  TV_bot_answer.setText(getString(R.string.bot_victory));
            TV_final.setText(getString(R.string.bot_victory));
            imageSheldon.setImageResource(R.drawable.sheldon_loose_game);
        }
    }

    public void playerWins() {
        imageSheldon.setVisibility(View.VISIBLE);
        TV_manche_nb.setText("" + (Integer.parseInt(TV_manche_nb.getText().toString()) + 1));
        TV_player_score.setText("" + (Integer.parseInt(TV_player_score.getText().toString()) + 1));
        imageSheldon.setImageResource(R.drawable.sheldon_win);

    }

    public void playerLooses() {
        imageSheldon.setVisibility(View.VISIBLE);
        TV_manche_nb.setText("" + (Integer.parseInt(TV_manche_nb.getText().toString()) + 1));
        TV_bot_score.setText("" + (Integer.parseInt(TV_bot_score.getText().toString()) + 1));
        imageSheldon.setImageResource(R.drawable.sheldon_loose);

    }

    public void draw() {            // match nul
        imageSheldon.setVisibility(View.VISIBLE);
        imageSheldon.setImageResource(R.drawable.shedon_draw);
    }

    public void formatForNewManche(ImageView img_current, ImageView img_1, ImageView img_2, ImageView img_3, ImageView img_4) {
        img_current.setColorFilter(R.color.purple_500);
        img_1.setColorFilter(null);
        img_2.setColorFilter(null);
        img_3.setColorFilter(null);
        img_4.setColorFilter(null);
        //   gameInProgress = true;
        imageSheldon.setVisibility(View.INVISIBLE);
        gifSign.setVisibility(View.VISIBLE);
        TV_bot_answer.setVisibility(View.INVISIBLE);
    }

    public void updatePlayersScore(String new_score) {
        // récupération de l'utilisateur courant
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("score", new_score);

        db.collection("players").document(currentUser.getUid())
                .set(data, SetOptions.merge());
    }

    public void getCurrentScore() {
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
                        scoreJoueur = (String) document.getString("score");
                        Log.d(TAG, "Score du joueur : " + scoreJoueur);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void getPlayerName() {
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
                        TV_player_name.setText(nomJoueur);

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





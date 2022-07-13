package com.example.paperlizardspock;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;


public class TestGameBoard extends AppCompatActivity {
    //Boolean gameInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        // ---------------------- Mise en place interface ---------------------------

        TextView TV_bot_answer = (TextView) findViewById(R.id.TV_bot_answer);
        TextView TV_manche_nb = (TextView) findViewById(R.id.TV_manche_nb);
        TextView TV_difficulty_nb = (TextView) findViewById(R.id.TV_difficulty_nb);
        TextView TV_player_score = (TextView) findViewById(R.id.TV_player_score);
        TextView TV_bot_score = (TextView) findViewById(R.id.TV_bot_score);
        TextView TV_final = (TextView) findViewById(R.id.TV_final);

        Button retry_button = (Button) findViewById(R.id.retry_button);

        ImageView imageBot = (ImageView) findViewById(R.id.img_bot_result);
        ImageView imageSheldon = (ImageView) findViewById(R.id.image_sheldon);
        ImageView gifSign = (ImageView) findViewById(R.id.gifSigns);
        ImageView img_rock = (ImageView) findViewById(R.id.img_rock);
        ImageView img_paper = (ImageView) findViewById(R.id.img_paper);
        ImageView img_scissors = (ImageView) findViewById(R.id.img_scissors);
        ImageView img_lizard = (ImageView) findViewById(R.id.img_lizard);
        ImageView img_spock = (ImageView) findViewById(R.id.img_spock);

//        gameInProgress = false;



        gifSign.setVisibility(View.INVISIBLE);
        imageSheldon.setVisibility(View.INVISIBLE);

        // Récupération et afichage de la difficulté à partir de MenuActivity
        Bundle bundle = getIntent().getExtras();
        String gameDifficulty = bundle.getString("GAME_DIFFICULTY");

        TV_difficulty_nb.setText(gameDifficulty);                                             // on l'affiche dans la TextView prévue à cet effet






        // ------------------------ Help Button -------------------------------
        ImageButton help_button = (ImageButton) findViewById(R.id.help_button);
        help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRulesDemonstrationInActivity2 = new Intent(getApplicationContext(),RulesDemonstrationActivity.class);
                //   openRulesDemonstrationInActivity2.putExtra("IS_GAME_ON", gameInProgress);
                startActivity(openRulesDemonstrationInActivity2);
            }
        });

        // ----------------------- Retry Button ------------------------------
        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                // mettre à jour les filtres de couleur, changer visibilité des images
                formatForNewManche(img_rock,img_lizard, img_paper, img_scissors, img_spock, imageSheldon, gifSign, TV_bot_answer);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {                // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                    public void run() {
                        TV_bot_answer.setVisibility(View.VISIBLE);
                        switch(gameDifficulty) {
                            case "1":
                                actionsRock( 0.75,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button,imageSheldon, TV_final);
                                break;
                            case "2":
                                actionsRock(0.50,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                            case "3" :
                                actionsRock(0.25,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                        }
                    }
                }, 2000);

            }
        });

        // ---------------------- Paper ---------------------------------------
        img_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mettre à jour les filtres de couleur, changer visibilité des images
                formatForNewManche(img_paper,img_lizard, img_rock, img_scissors, img_spock, imageSheldon, gifSign, TV_bot_answer);

                Handler handler = new Handler();                // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                handler.postDelayed(new Runnable() {
                    public void run() {
                        TV_bot_answer.setVisibility(View.VISIBLE);
                        switch(gameDifficulty) {
                            case "1":
                                actionsPaper( 0.75,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                            case "2":
                                actionsPaper(0.50,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final) ;
                                break;
                            case "3" :
                                actionsPaper(0.25,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                        }}
                }, 2000);
            }
        });

        // --------------------- Scissors -------------------------------------
        img_scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mettre à jour les filtres de couleur, changer visibilité des images
                formatForNewManche(img_scissors,img_lizard, img_rock, img_paper, img_spock, imageSheldon, gifSign, TV_bot_answer);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {                    // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                    public void run() {
                        TV_bot_answer.setVisibility(View.VISIBLE);
                        switch(gameDifficulty) {
                            case "1":
                                actionsScissors( 0.75,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                            case "2":
                                actionsScissors(0.50,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final) ;
                                break;
                            case "3" :
                                actionsScissors(0.25,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                        }
                    }
                }, 2000);
            }
        });

        // ------------------- Lizard ------------------------------------------
        img_lizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mettre à jour les filtres de couleur, changer visibilité des images
                formatForNewManche(img_lizard,img_paper, img_rock, img_scissors, img_spock, imageSheldon, gifSign, TV_bot_answer);

                Handler handler = new Handler();                    // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                handler.postDelayed(new Runnable() {
                    public void run() {
                        TV_bot_answer.setVisibility(View.VISIBLE);
                        switch(gameDifficulty) {
                            case "1":
                                actionsLizard( 0.75,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                            case "2":
                                actionsLizard(0.50,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final) ;
                                break;
                            case "3" :
                                actionsLizard(0.25,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                        }
                    }
                }, 2000);
            }
        });

        // --------------------- Spock -----------------------------------------
        img_spock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mettre à jour les filtres de couleur, changer visibilité des images
                formatForNewManche(img_spock,img_lizard, img_rock, img_scissors, img_paper, imageSheldon, gifSign, TV_bot_answer);

                Handler handler = new Handler();                        // sert à faire une pause de 2 sec pour avoir le temps de voir le gif
                handler.postDelayed(new Runnable() {
                    public void run() {
                        TV_bot_answer.setVisibility(View.VISIBLE);
                        switch(gameDifficulty) {
                            case "1":
                                actionsSpock( 0.75,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                            case "2":
                                actionsSpock(0.50,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final) ;
                                break;
                            case "3" :
                                actionsSpock(0.25,  imageBot,  TV_manche_nb,  TV_player_score,  TV_bot_score,  TV_bot_answer,  gifSign,  retry_button, imageSheldon, TV_final);
                                break;
                        }
                    }
                }, 2000);
            }
        });



    }

    // *****************************************************************************************************************************************
    //                                                             M E T H O D E S
    // *****************************************************************************************************************************************


    public void actionsRock(double difficulty_percentage, ImageView imageBot, TextView TV_manche_nb, TextView TV_player_score, TextView TV_bot_score, TextView TV_bot_answer, ImageView gifSign, Button retryButton, ImageView imageSheldon, TextView TV_final) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE); // on fait disparaître le gif
        if (d<difficulty_percentage) {                  // si le joueur gagne
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_rock).length);  // On accède au tableau de perdants pour la pierre
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_rock);
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins( TV_manche_nb,  TV_player_score,  imageSheldon);

        }else{                                          // si le joueur ne gagne pas
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_rock).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_rock);                       // On accède au tableau de gagnants pour la pierre
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            if (indexToGetImageFrom==3){                                                    // match nul (draw)
                draw(TV_manche_nb, imageSheldon);
            }else{                                                                          //  si victoire bot
                // update du score et de la manche
                playerLooses( TV_manche_nb,  TV_bot_score,  imageSheldon);
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString())==3||Integer.parseInt(TV_player_score.getText().toString())==3){         // si l'un des deux a un score de 3
            endOfGame(TV_bot_score,  TV_bot_answer,  gifSign,  retryButton,  TV_player_score, TV_final, imageSheldon);         // appel fonction fin de jeu
        }

    }

    public void actionsPaper(double difficulty_percentage, ImageView imageBot, TextView TV_manche_nb, TextView TV_player_score, TextView TV_bot_score, TextView TV_bot_answer, ImageView gifSign, Button retryButton, ImageView imageSheldon, TextView TV_final) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_paper).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_paper);                         // On accède au tableau de gagnants pour le papier
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins( TV_manche_nb,  TV_player_score,  imageSheldon);

        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_paper).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_paper);                         // On accède au tableau de perdants pour le papier
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if(indexToGetImageFrom==2){
                draw(TV_manche_nb, imageSheldon);
            }else{
                // update du score et de la manche
                playerLooses( TV_manche_nb,  TV_bot_score,  imageSheldon);
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString())==3||Integer.parseInt(TV_player_score.getText().toString())==3){         // si l'un des deux a un score de 3
            endOfGame(TV_bot_score,  TV_bot_answer,  gifSign,  retryButton,  TV_player_score, TV_final, imageSheldon);         // appel fonction fin de jeu
        }
    }

    public void actionsScissors(double difficulty_percentage, ImageView imageBot, TextView TV_manche_nb, TextView TV_player_score, TextView TV_bot_score, TextView TV_bot_answer, ImageView gifSign, Button retryButton, ImageView imageSheldon, TextView TV_final) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_scissors).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_scissors);                         // On accède au tableau de gagnants pour les ciseaux
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins( TV_manche_nb,  TV_player_score,  imageSheldon);



        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_scissors).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_scissors);                         // On accède au tableau de perdants pour les ciseaux
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if(indexToGetImageFrom==2){
                draw(TV_manche_nb, imageSheldon);
            }else{
                // update du score et de la manche
                playerLooses( TV_manche_nb,  TV_bot_score,  imageSheldon);
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString())==3||Integer.parseInt(TV_player_score.getText().toString())==3){         // si l'un des deux a un score de 3
            endOfGame(TV_bot_score,  TV_bot_answer,  gifSign,  retryButton,  TV_player_score, TV_final, imageSheldon);         // appel fonction fin de jeu
        }
    }

    public void actionsLizard(double difficulty_percentage, ImageView imageBot, TextView TV_manche_nb, TextView TV_player_score, TextView TV_bot_score, TextView TV_bot_answer, ImageView gifSign, Button retryButton, ImageView imageSheldon, TextView TV_final) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_lizard).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_lizard);                         // On accède au tableau de gagnants pour le lézard
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins(TV_manche_nb, TV_player_score, imageSheldon);

        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_lizard).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_lizard);                         // On accède au tableau de perdants pour le lézard
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if(indexToGetImageFrom==2){
                draw(TV_manche_nb, imageSheldon);
            }else{
                // update du score et de la manche
                playerLooses(TV_manche_nb, TV_bot_score, imageSheldon);
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString())==3||Integer.parseInt(TV_player_score.getText().toString())==3){         // si l'un des deux a un score de 3
            endOfGame(TV_bot_score,  TV_bot_answer,  gifSign,  retryButton,  TV_player_score, TV_final, imageSheldon);         // appel fonction fin de jeu
        }
    }

    public void actionsSpock(double difficulty_percentage, ImageView imageBot, TextView TV_manche_nb, TextView TV_player_score, TextView TV_bot_score, TextView TV_bot_answer, ImageView gifSign, Button retryButton, ImageView imageSheldon, TextView TV_final) {
        double d = Math.random();           // on tire un double random entre 0 et 1
        gifSign.setVisibility(View.INVISIBLE);
        if (d < difficulty_percentage) {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.loosers_on_spock).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.loosers_on_spock);                         // On accède au tableau de gagnants pour Spock
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi

            // update du score et de la manche
            playerWins(TV_manche_nb, TV_player_score, imageSheldon);


        } else {
            Random random = new Random();
            int indexToGetImageFrom = random.nextInt(getResources().getStringArray(R.array.winners_on_spock).length);
            TypedArray imgs = getResources().obtainTypedArray(R.array.winners_on_spock);                         // On accède au tableau de perdants pour Spock
            imageBot.setImageResource(imgs.getResourceId(indexToGetImageFrom, 0));                    // On affiche la réponse de l'ordi
            if(indexToGetImageFrom==2){
                draw(TV_manche_nb, imageSheldon);
            }else{
                // update du score et de la manche
                playerLooses(TV_manche_nb, TV_bot_score, imageSheldon);
            }
        }
        if (Integer.parseInt(TV_bot_score.getText().toString())==3||Integer.parseInt(TV_player_score.getText().toString())==3){         // si l'un des deux a un score de 3
            endOfGame(TV_bot_score,  TV_bot_answer,  gifSign,  retryButton,  TV_player_score, TV_final, imageSheldon);         // appel fonction fin de jeu
        }
    }

    public void getScore (TextView TV_manche_nb, TextView TV_player_score, TextView TV_bot_score){            // cette fonction sert à récupérer les infos de la partie afin de les sauvegarder

    }

    public void endOfGame(TextView TV_bot_score, TextView TV_bot_answer, ImageView gifSign, Button retryButton, TextView TV_player_score, TextView TV_final, ImageView imageSheldon){
        retryButton.setVisibility(View.VISIBLE);
        TV_final.setVisibility((View.VISIBLE));
        imageSheldon.setVisibility(View.VISIBLE);
        //     gameInProgress = false;
        int score_bot = Integer.parseInt(TV_bot_score.getText().toString());
        int score_player = Integer.parseInt(TV_player_score.getText().toString());
        if(score_bot<score_player){                     // si victoire joueur
            //  TV_bot_answer.setText(getString(R.string.player_victory));
            TV_final.setText(getString(R.string.player_victory));
            imageSheldon.setImageResource(R.drawable.sheldon_win_game);
        }else {                                         // si victoire bot
            //  TV_bot_answer.setText(getString(R.string.bot_victory));
            TV_final.setText(getString(R.string.bot_victory));
            imageSheldon.setImageResource(R.drawable.sheldon_loose_game);
        }
    }

    public void playerWins(TextView TV_manche_nb, TextView TV_player_score, ImageView imageSheldon){
        imageSheldon.setVisibility(View.VISIBLE);
        TV_manche_nb.setText("" + (Integer.parseInt(TV_manche_nb.getText().toString()) + 1));
        TV_player_score.setText("" + (Integer.parseInt(TV_player_score.getText().toString()) + 1));
        imageSheldon.setImageResource(R.drawable.sheldon_win);

    }

    public void playerLooses(TextView TV_manche_nb, TextView TV_bot_score, ImageView imageSheldon){
        imageSheldon.setVisibility(View.VISIBLE);
        TV_manche_nb.setText("" + (Integer.parseInt(TV_manche_nb.getText().toString()) + 1));
        TV_bot_score.setText("" + (Integer.parseInt(TV_bot_score.getText().toString()) + 1));
        imageSheldon.setImageResource(R.drawable.sheldon_loose);

    }

    public void draw (TextView TV_manche_nb, ImageView imageSheldon){            // match nul
        imageSheldon.setVisibility(View.VISIBLE);
        // TV_manche_nb.setText("" + (Integer.parseInt(TV_manche_nb.getText().toString()) + 1));
        imageSheldon.setImageResource(R.drawable.shedon_draw);
    }

    public void formatForNewManche(ImageView img_current, ImageView img_1, ImageView img_2, ImageView img_3, ImageView img_4, ImageView imageSheldon, ImageView gifSign, TextView TV_bot_answer){
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
}


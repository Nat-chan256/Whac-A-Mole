package ru.moskovka.whacamole;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import ru.moskovka.whacamole.util.ButtonsHandler;
import ru.moskovka.whacamole.util.TextCustomizer;

public class ResultActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        loadPreferences();
        handlePlayAgainButton();
        handleMenuButton();
        TextCustomizer.customizeTextViews(
                findViewById(R.id.tv_current_score),
                findViewById(R.id.tv_current_score_value),
                findViewById(R.id.tv_current_record_result_activity),
                findViewById(R.id.tv_record_value_result_activity)
        );
    }

    //=================================================================
    //======================Utility methods============================
    //=================================================================



    private void handleMenuButton(){
        ButtonsHandler.startNewActivityWhenClickButton(
                this,
                findViewById(R.id.btn_menu),
                MainActivity.class
        );
    }

    private void handlePlayAgainButton(){
        ButtonsHandler.startNewActivityWhenClickButton(
                this,
                findViewById(R.id.btn_play_again),
                GameActivity.class
        );
    }

    private void loadPreferences(){
        SharedPreferences gamePreferences = getSharedPreferences(
                MainActivity.PREFS_NAME, Context.MODE_PRIVATE
        );
        int currentScore = gamePreferences.getInt(MainActivity.CURRENT_SCORE, 0);
        int currentRecord = gamePreferences.getInt(MainActivity.RECORD_VALUE_STRING, 0);

        TextView tvCurrentScore = findViewById(R.id.tv_current_score_value);
        TextView tvCurrentRecord = findViewById(R.id.tv_record_value_result_activity);

        tvCurrentScore.setText(String.valueOf(currentScore));
        if (currentScore > currentRecord){
            tvCurrentRecord.setText(String.valueOf(currentScore));
        } else {
            tvCurrentRecord.setText(String.valueOf(currentRecord));
        }
    }
}
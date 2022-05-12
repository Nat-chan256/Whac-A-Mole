package ru.moskovka.whacamole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import ru.moskovka.whacamole.util.ButtonsHandler;
import ru.moskovka.whacamole.util.TextCustomizer;

public class MainActivity extends AppCompatActivity {

    public static final String CURRENT_SCORE = "current_score";
    public static final String PREFS_NAME = "game_preferences";
    public static final String RECORD_VALUE_STRING = "record_value";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlePlayButtonOnClickEvent();
        handleRulesButtonOnClickEvent();
        TextCustomizer.customizeTextViews(
                findViewById(R.id.tv_current_record),
                findViewById(R.id.tv_record_value)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadCurrentRecord();
    }

    //=================================================================
    //======================Utility methods============================
    //=================================================================

    private void handlePlayButtonOnClickEvent(){
        ButtonsHandler.startNewActivityWhenClickButton(
                this,
                findViewById(R.id.btn_play),
                GameActivity.class
        );
    }

    private void handleRulesButtonOnClickEvent(){
        ButtonsHandler.startNewActivityWhenClickButton(
                this,
                findViewById(R.id.btn_rules),
                RulesActivity.class
        );
    }

    private void loadCurrentRecord(){
        SharedPreferences gamePreferences = getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );
        TextView tvRecordValue = findViewById(R.id.tv_record_value);
        tvRecordValue.setText(String.valueOf(
                gamePreferences.getInt(RECORD_VALUE_STRING, 0)
        ));
    }

}
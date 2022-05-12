package ru.moskovka.whacamole;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import ru.moskovka.whacamole.util.ButtonsHandler;
import ru.moskovka.whacamole.util.TextCustomizer;

public class RulesActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        ButtonsHandler.startNewActivityWhenClickButton(
                this,
                findViewById(R.id.btn_back),
                MainActivity.class
        );

        TextCustomizer.customizeTextViews(findViewById(R.id.tv_title));
        TextView tvRules = findViewById(R.id.tv_rules);
        tvRules.setTextAppearance(R.style.font_style_light);
    }
}
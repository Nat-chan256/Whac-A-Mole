package ru.moskovka.whacamole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private static int moleAdvented = 0;

    private final int COLUMN_COUNT = 3;
    private final long GAME_DURATION = 30_000;
    private final int HOLE_MARGIN = 10;
    private final int HOLE_TOP_MARGIN = 60;
    private final int HOLES_COUNT = 9;
    private final long MOLE_APPEAR_PERIOD = 550;

    private int currentScore = 0;

    private RelativeLayout mainLayout;
    private Handler moleAdventHandler = new Handler();
    private ImageButton[] holes;
    private int moleLastHoleId = 0;
    private Random rand = new Random();

    private int timeLeft = (int)(GAME_DURATION / 1000);
    private Timer timer;
    private TimerTask timerTask;

    private TextView tvScoreValue;
    private TextView tvTimeLeftValue;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createHoles();
        displayScoreAndTimer();
        try {
            releaseMole();
            launchTimer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        SharedPreferences gamePreferences = getSharedPreferences(
                MainActivity.PREFS_NAME,
                Context.MODE_PRIVATE
        );
        int currentRecord = gamePreferences.getInt(MainActivity.RECORD_VALUE_STRING, 0);
        SharedPreferences.Editor prefsEditor = gamePreferences.edit();
        if (currentScore > currentRecord){
            prefsEditor.putInt(MainActivity.RECORD_VALUE_STRING, currentScore);
        }
        prefsEditor.putInt(MainActivity.CURRENT_SCORE, currentScore);
        prefsEditor.commit();

        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    //=================================================================
    //======================Utility methods============================
    //=================================================================

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addButton(GridLayout gridLayout, int buttonIndex){
        holes[buttonIndex] = new ImageButton(this);
        holes[buttonIndex].setImageResource(R.drawable.hole);
        holes[buttonIndex].setTag(R.drawable.hole);
        holes[buttonIndex].setBackgroundColor(Color.TRANSPARENT);
        holes[buttonIndex].setId(buttonIndex);
        holes[buttonIndex].setOnClickListener(v -> {
            if (v.getTag().equals(R.drawable.mole_in_hole)){
                currentScore++;
                tvScoreValue.setText(String.valueOf(currentScore));
                holes[buttonIndex].setImageResource(R.drawable.hole);
                holes[buttonIndex].setTag(R.drawable.hole);
                holes[buttonIndex].setLayoutParams(createEmptyHoleParams());
            }
        });

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(HOLE_MARGIN, HOLE_TOP_MARGIN, HOLE_MARGIN, HOLE_MARGIN);
        holes[buttonIndex].setLayoutParams(params);

        gridLayout.addView(holes[buttonIndex]);
    }

    private GridLayout.LayoutParams createEmptyHoleParams(){
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(HOLE_MARGIN, HOLE_TOP_MARGIN, HOLE_MARGIN, HOLE_MARGIN);
        return params;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createHoles(){
        holes = new ImageButton[HOLES_COUNT];

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        mainLayout = new RelativeLayout(this);

        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(COLUMN_COUNT);
        gridLayout.setLayoutParams(params);
        for (int i = 0; i < HOLES_COUNT; ++i){
            addButton(gridLayout, i);
        }

        mainLayout.addView(gridLayout);
        mainLayout.setBackground(getDrawable(R.drawable.background_glade));
        setContentView(mainLayout);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private LinearLayout createLayoutWithTextViews(String firstTextViewValue,
                                                   TextView secondTextView,
                                                   int secondTextViewValue){
        LinearLayout scoreLayout = new LinearLayout(this);
        scoreLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView tvScore = new TextView(this);
        tvScore.setText(firstTextViewValue);
        secondTextView.setText(String.valueOf(secondTextViewValue));

        tvScore.setTextAppearance(R.style.font_style);
        secondTextView.setTextAppearance(R.style.font_style);

        scoreLayout.addView(tvScore);
        scoreLayout.addView(secondTextView);

        return scoreLayout;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void displayScoreAndTimer(){
        tvScoreValue = new TextView(this);
        LinearLayout scoreLayout = createLayoutWithTextViews(
                "Score: ", tvScoreValue, currentScore
        );
        tvTimeLeftValue = new TextView(this);
        LinearLayout timerLayout = createLayoutWithTextViews(
                "Time left: ", tvTimeLeftValue, timeLeft
        );
        LinearLayout scoreAndTimerLayout = new LinearLayout(this);
        scoreAndTimerLayout.setOrientation(LinearLayout.VERTICAL);
        scoreAndTimerLayout.addView(scoreLayout);
        scoreAndTimerLayout.addView(timerLayout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, 8, 8, 0);
        scoreAndTimerLayout.setLayoutParams(params);

        mainLayout.addView(scoreAndTimerLayout);
    }

    private void launchTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    timeLeft--;
                    tvTimeLeftValue.setText(String.valueOf(timeLeft));
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void releaseMole() throws InterruptedException {
        final int MOLE_ADVENTS_COUNT = (int) (GAME_DURATION / MOLE_APPEAR_PERIOD);
        moleAdvented = 0;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                moleAdventHandler.post(()->{
                    holes[moleLastHoleId].setImageResource(R.drawable.hole);
                    holes[moleLastHoleId].setTag(R.drawable.hole);
                    holes[moleLastHoleId].setLayoutParams(createEmptyHoleParams());
                    int newHoleId = rand.nextInt(HOLES_COUNT);
                    while (newHoleId == moleLastHoleId)
                        newHoleId = rand.nextInt(HOLES_COUNT);
                    moleLastHoleId = newHoleId;
                    holes[moleLastHoleId].setImageResource(R.drawable.mole_in_hole);
                    holes[moleLastHoleId].setTag(R.drawable.mole_in_hole);

                    GridLayout.LayoutParams newParams = new GridLayout.LayoutParams();
                    newParams.setMargins(HOLE_MARGIN, 0, HOLE_MARGIN, HOLE_MARGIN);

                    holes[moleLastHoleId].setLayoutParams(newParams);
                    moleAdvented++;
                    if (moleAdvented >= MOLE_ADVENTS_COUNT){
                        Intent intent = new Intent(
                                GameActivity.this,
                                ResultActivity.class
                        );
                        startActivity(intent);
                        return;
                    }
                    moleAdventHandler.postDelayed(this, MOLE_APPEAR_PERIOD);
                });
            }
        }, MOLE_APPEAR_PERIOD);
    }
}
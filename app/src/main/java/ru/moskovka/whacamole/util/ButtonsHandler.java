package ru.moskovka.whacamole.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

public class ButtonsHandler {
    public static void startNewActivityWhenClickButton(
            Context context,
            Button button,
            Class newActivityClass){
        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, newActivityClass);
            context.startActivity(intent);
        });
    }
}

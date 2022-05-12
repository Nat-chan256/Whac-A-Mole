package ru.moskovka.whacamole.util;

import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import ru.moskovka.whacamole.R;

public class TextCustomizer {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void customizeTextViews(TextView...textViews){
        for (TextView textView : textViews)
            textView.setTextAppearance(R.style.font_style);
    }
}

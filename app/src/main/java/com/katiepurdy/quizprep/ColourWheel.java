package com.katiepurdy.quizprep;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by katiepurdy on 2014-08-19.
 */
public class ColourWheel {
    String[] mColours = {
            "#e40027", // crimson
            "#f20000", // red
            "#fd3800", // orangered
            "#ff9300", // darkorange
            "#ff386e", // deeppink
            "#fa0086", // deeppink (deeper pink)
            "#cf007a", // mediumvioletred
            "#a6007e", // darkmagenta
            "#ffc800", // gold
            "#c6d500", // gold (greener gold)
            "#00aaac", // lightseagreen
            "#00c2d6"  // darkturquoise
    };

    /**
     * Gets a random colour from the array of colours
     *
     * @return an integer representation of a colour
     */
    public int getColour() {
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(mColours.length);

        String colour = mColours[randomNumber];
        return Color.parseColor(colour);
    }
}
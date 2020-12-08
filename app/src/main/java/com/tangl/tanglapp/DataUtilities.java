package com.tangl.tanglapp;

import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class DataUtilities {
    public static String[] getFormattedArray(List<FirebaseVisionText.TextBlock> textBlocksList){
        for (FirebaseVisionText.TextBlock textBlock : textBlocksList){
            String text = textBlock.getText();
            if(containsIgnoreCase(text,"ingredients")){
                return text.split(Pattern.quote(","));
            }

        }
        return new String[]{""};
    }

    public static List<String> ingredientsCheck(String[] ingredients, String[] badList){
        List<String> badIngredients = new ArrayList<>();
        for(String ingredient : badList){
            System.out.println("checking "+ ingredient);
            if(Arrays.asList(ingredients).contains(ingredient)){
                badIngredients.add(ingredient);
            }
        }

        return badIngredients;
    }


    private static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}


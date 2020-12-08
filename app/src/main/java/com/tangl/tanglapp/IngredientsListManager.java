package com.tangl.tanglapp;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

class IngredientsListManager {

        final private FirebaseFirestore database = FirebaseFirestore.getInstance();
        private List<String> badIngredientsList = new ArrayList<>();

        public IngredientsListManager(String whichList){
            prepareCollection(getThisList(whichList));
         }


        public void prepareCollection(String whichList){
        final CollectionReference badIngredients = database.collection(whichList);
        badIngredients.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        badIngredientsList.add(document.getString("name"));
                    }

                }
            }
        });
         }


    private String getThisList(String list){
            if(list.equalsIgnoreCase("SKIN")){
                return "BadIngredients_Skin";
            }
            return "BadIngredients_Hair";
    }


    public List<String> checkCapturedText(String resultText)
            throws Exception {
        List<String> matchedBadIngredients = new ArrayList<>();
        String ingredients = getIngredientsList(resultText);
        if(ingredients.length()>1){
            matchedBadIngredients = individualIngredientsCheck(ingredients,badIngredientsList);
            //badIngredients = remoteIngredientsCheck(ingredients);
            System.out.println("ingredients block found ");
        }
        else{
            System.out.println("ingredients not found");
            throw new Exception(String.valueOf(R.string.no_ingredients_found));
        }
        return matchedBadIngredients;
    }

    private static String getIngredientsList(String textBlock){
        String[] ingredientDetectorArray = new String[]{"ingredients", "water", "aqua"};//generalize this
        System.out.println("the text from the image "+textBlock);
        for (String markerIngredient : ingredientDetectorArray){
            String text = textBlock.toLowerCase();
            if(text.contains(markerIngredient)){
                return text;
            }
        }
        return "";
    }


    private static List<String> individualIngredientsCheck(String ingredients, List<String> badList){
        List<String> badIngredients = new ArrayList<>();
        for(String ingredient : badList){
            if(containsIgnoreCase(ingredients,ingredient)){
                badIngredients.add(ingredient);
            }
        }
        return badIngredients;
    }


    private static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0) //maybe change this
            return true; // Empty string is contained <--mot mine

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


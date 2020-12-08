package com.tangl.tanglapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScanResults extends AppCompatActivity {
    private RecyclerView mIngredientsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_results_activity);
        ImageView mImageView = findViewById(R.id.scan_result_top_image);
        TextView title = findViewById(R.id.results_title);
        ArrayList<String> badList = getIntent().getStringArrayListExtra("badIngredients");
        if(badList.size()>0){
            mImageView.setImageResource(R.drawable.alert);
            title.setText(getResources().getString(R.string.bad_ingredients_found));
        }
        else{
            title.setText(getResources().getString(R.string.no_bad_ingredients_found));
            mImageView.setImageResource(R.drawable.good);
        }
        mIngredientsRecyclerView = findViewById(R.id.bad_ingredients_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mIngredientsRecyclerView.setLayoutManager(linearLayoutManager);
        mIngredientsRecyclerView.setAdapter(new IngredientsAdapter(badList));
    }


    private class IngredientsAdapter extends RecyclerView.Adapter<IngredientsHolder>{
        private ArrayList<String> mIngredients;

        public IngredientsAdapter(ArrayList<String> ingredients){
            mIngredients = ingredients;
        }

        @Override
        public IngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            return new IngredientsHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(IngredientsHolder holder, int position){
            holder.bind(mIngredients.get(position));
        }

        @Override
        public int getItemCount(){
            return mIngredients.size();
        }
    }


    private class IngredientsHolder extends RecyclerView.ViewHolder{
        private TextView mIngredientName;
        public IngredientsHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.ingredient_item,parent,false));
            mIngredientName = (TextView) itemView.findViewById(R.id.ingredient_name);
        }

        void bind(String ingredient){
            mIngredientName.setText(ingredient);
        }
    }



}
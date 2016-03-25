package com.example.hp.speechify;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.speechify.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 25-03-2016.
 */
public class SecondActivity extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_file);
        listView=(ListView)findViewById(R.id.list2);
        ArrayList<RecipeModel.Ingredient> ingredientList;
        ingredientList=(ArrayList<RecipeModel.Ingredient>)getIntent().getSerializableExtra("Mykey");
        IngredientAdapter adapter=new IngredientAdapter(SecondActivity.this,R.layout.ingredient_row,ingredientList);
        listView.setAdapter(adapter);

    }
    private class IngredientAdapter extends ArrayAdapter
    {   ArrayList<RecipeModel.Ingredient> ingredientList;
        int resource;
        private LayoutInflater inflater=null;
        public IngredientAdapter(Context context, int resource, ArrayList<RecipeModel.Ingredient> objects) {
            super(context, resource, objects);
            this.resource = resource;
            ingredientList = objects;
            inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
            {
                convertView=inflater.inflate(resource,null);
            }
            TextView ingredient_id=(TextView)convertView.findViewById(R.id.ingredient_id);
            ingredient_id.setText(ingredientList.get(position).getIngredient_id());
            TextView ingredient_name=(TextView)convertView.findViewById(R.id.ingredient_name);
            ingredient_name.setText(ingredientList.get(position).getIngredient_name());

            return convertView;
        }
    }

}

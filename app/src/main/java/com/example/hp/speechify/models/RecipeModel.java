package com.example.hp.speechify.models;

import java.util.List;

/**
 * Created by hp on 24-03-2016.
 */
public class RecipeModel {
    private String id;
    private String name;
    private List<Ingredient> ingredientList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public static class Ingredient
    {
        private String ingredient_id;
        private String ingredient_name;

        public String getIngredient_id() {
            return ingredient_id;
        }

        public void setIngredient_id(String ingredient_id) {
            this.ingredient_id = ingredient_id;
        }

        public String getIngredient_name() {
            return ingredient_name;
        }

        public void setIngredient_name(String ingredient_name) {
            this.ingredient_name = ingredient_name;
        }
    }
}

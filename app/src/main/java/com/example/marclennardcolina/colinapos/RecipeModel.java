package com.example.marclennardcolina.colinapos;

public class RecipeModel {
    String mealType;
    int mealServing;
    String recipeName;
    double cookingTime;
    String ingredients;
    String instructions;
    String recipeID;


    public RecipeModel(){}


    public RecipeModel(String mealType, int mealServing, String recipeName, double cookingTime, String ingredients, String instructions,String recipeID) {
        this.mealType = mealType;
        this.mealServing = mealServing;
        this.recipeName = recipeName;
        this.cookingTime = cookingTime;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeID=recipeID;
    }



    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public int getMealServing() {
        return mealServing;
    }

    public void setMealServing(int mealServing) {
        this.mealServing = mealServing;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public double getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(double cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

}

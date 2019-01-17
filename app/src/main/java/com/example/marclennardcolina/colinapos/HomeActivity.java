package com.example.marclennardcolina.colinapos;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    RecyclerView homeActivity_recyclerView;
    private ArrayList<RecipeModel> recipes=new ArrayList<>();

    private ArrayList<RecipeModel> myRecipes;
    RecipeRecyclerAdapter adapter;
    FirebaseFirestore db;
    private StorageReference mStorageRef;
    Button homeActivity_addRecipeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        refs();
        adapter=null;

        homeActivity_addRecipeButton.setOnClickListener(AddRecipe);


        db = FirebaseFirestore.getInstance();
        db.collection("Recipe").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e("THIS IS THE DATA", document.getId() + " => " + document.getData());

                        RecipeModel recipe = document.toObject(RecipeModel.class);
                        Log.e("RECIPE THIS",  "" + recipe.getRecipeName()  + " lALAA");
                        Log.e("RECIPE THIS",  "" + recipe.getMealType() + " lALAA");
                        Log.e("RECIPE THIS",  "" + recipe.getIngredients() + " lALAA");
                        recipes.add(recipe);
          //   recipes =task.getDocuments().get(0).toObject(questionObject.class);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error in Retrieving Records!!",
                            Toast.LENGTH_SHORT).show();
                }
                Log.e("RECIPES!", recipes.toString());
                if(recipes.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "NO RECORDS TO SHOW",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    adapter = new RecipeRecyclerAdapter(recipes, HomeActivity.this);
                    homeActivity_recyclerView.addItemDecoration(new DividerItemDecoration(HomeActivity.this, LinearLayoutManager.HORIZONTAL));
                    homeActivity_recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    homeActivity_recyclerView.setAdapter(adapter);
                }
            }
        });


     //  recipes.add(new RecipeModel("GINALING",10,"SPECIAL GINALING", 60, "Baboy","SAUTE GARLIC AND ONION","ID"));



    }


    public View.OnClickListener AddRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(HomeActivity.this,AddRecipeActivity.class);
            startActivity(intent);
        }
    };

    public void refs()
    {
        homeActivity_recyclerView=findViewById(R.id.homeActivity_recyclerView);
        homeActivity_addRecipeButton=findViewById(R.id.homeActivity_addRecipeButton);

    }

}

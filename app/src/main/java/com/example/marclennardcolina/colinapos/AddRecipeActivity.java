package com.example.marclennardcolina.colinapos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class AddRecipeActivity extends AppCompatActivity {


    ImageView addRecipe_ImageView;
    EditText addRecipeEditTxt_MealType,addRecipeEditTxt_MealServing,addRecipeEditTxt_RecipeName,addRecipeEditTxt_CookingTime,addRecipeEditTxt_Ingredients,addRecipeEditTxt_Instruction;
    Button addRecipeEditTxt_AddRecipeBtn;
    Uri filePath;

    FirebaseFirestore db;
    FirebaseUser user;

    private final static int GALLERY_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        refs();
        addRecipe_ImageView.setOnClickListener(pickImage);
        addRecipeEditTxt_AddRecipeBtn.setOnClickListener(addRecipe);
    }

    public View.OnClickListener addRecipe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String mealType,recipeName,Ingredients,Instruction,mealServing,cookingTime;

            mealType=addRecipeEditTxt_MealType.getText().toString();
            cookingTime  =addRecipeEditTxt_CookingTime.getText().toString();
            mealServing =addRecipeEditTxt_MealServing.getText().toString();
            recipeName =addRecipeEditTxt_RecipeName.getText().toString();
            Ingredients =addRecipeEditTxt_Ingredients.getText().toString();
            Instruction =addRecipeEditTxt_Instruction.getText().toString();


            if(mealType.isEmpty())
            {
                addRecipeEditTxt_MealType.setError("Enter Meal Type");
            }
            else if(mealServing.isEmpty())
            {
                addRecipeEditTxt_MealServing.setError("Enter Meal Serving");
            }
           else if(recipeName.isEmpty())
            {
                addRecipeEditTxt_RecipeName.setError("Enter Recipe Name");
            }
            else if(cookingTime.isEmpty())
            {
                addRecipeEditTxt_CookingTime.setError("Enter Cooking Time");
            }

            else if(Ingredients.isEmpty())
            {
                addRecipeEditTxt_Ingredients.setError("Enter Ingredients");
            }

            else if(Instruction.isEmpty())
            {
                addRecipeEditTxt_Instruction.setError("Enter Meal Instructions");
            }
            else
            {
                RecipeModel recipe = new RecipeModel(mealType,Integer.parseInt(mealServing),recipeName,Double.parseDouble(cookingTime),Ingredients,Instruction,"ID");
                // Add a new document with a generated ID
                db = FirebaseFirestore.getInstance();
                db.collection("Recipe")
                        .add(recipe)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                uploadImage(documentReference.getId());

                                db = FirebaseFirestore.getInstance();
                                DocumentReference recipeToUpdate = db.collection("Recipe").document("" + documentReference.getId());
                                recipeToUpdate.update("recipeID", documentReference.getId())
                                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //SUCCESSFULLY UPDATED!!!
                                            }
                                        });

                                showAlert("SUCCESSFULLY ADDED RECIPE","SUCCESS");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                              //  Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        }
    };

    public void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
            } catch (Exception e) {

            }
        } else {
            pickImage();
        }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            checkAndroidVersion();
        }
    }

    public void pickImage() {
        CropImage.startPickImageActivity(this);
    }

    private void croprequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    public void uploadImage(String txtid){

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(AddRecipeActivity.this);
            progressDialog.setTitle("Uploading...");
           // progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images").child(txtid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            showAlert("Successfully Updated!","Success");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showAlert("An Error Occured","ERROR");

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

        else{
            showAlert("Please Select an Image","WARNING!");
        }

    }



    public void showAlert(String Message,String label)
    {
        //set alert for executing the task
        AlertDialog.Builder alert = new AlertDialog.Builder(AddRecipeActivity.this);
        alert.setTitle(""+label);
        alert.setMessage(""+Message);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog, int id){
                dialog.cancel();
            }
        });

        Dialog dialog = alert.create();
       // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private View.OnClickListener pickImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           /* Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
*/
           checkAndroidVersion();

        }
    };
/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                addRecipe_ImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            croprequest(imageUri);
        }

        //RESULT FROM CROPING ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());

                   // ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
                    addRecipe_ImageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public void refs()
    {
        addRecipe_ImageView=findViewById(R.id.addRecipe_ImageView);
        addRecipeEditTxt_MealType=findViewById(R.id.addRecipeEditTxt_MealType);
        addRecipeEditTxt_MealServing=findViewById(R.id.addRecipeEditTxt_MealServing);
        addRecipeEditTxt_RecipeName=findViewById(R.id.addRecipeEditTxt_RecipeName);
        addRecipeEditTxt_CookingTime=findViewById(R.id.addRecipeEditTxt_CookingTime);
        addRecipeEditTxt_Ingredients=findViewById(R.id.addRecipeEditTxt_Ingredients);
        addRecipeEditTxt_Instruction=findViewById(R.id.addRecipeEditTxt_Instruction);
        addRecipeEditTxt_AddRecipeBtn=findViewById(R.id.addRecipeEditTxt_AddRecipeBtn);

    }
}

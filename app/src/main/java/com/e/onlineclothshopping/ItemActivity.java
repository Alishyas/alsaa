package com.e.onlineclothshopping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemActivity extends AppCompatActivity {
    private EditText etCode, etName, etPrice, etDescription;
    private Button btnSave;
    private ImageView imgProfile;
    String imagePath;
    String imageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        etCode=findViewById(R.id.etCode);
        etName = findViewById(R.id.etName);
        etPrice= findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDesc);
        imgProfile = findViewById(R.id.imgProfile);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
                Intent intent = new Intent(ItemActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request permission here, THIRD PARAM IS SOME INTEGER CONSTANT (REQUEST CODE) VIA WHICH WE HANDLE OUR PERMISSION REQUEST RESPONSE
                    ActivityCompat.requestPermissions(ItemActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                BrowseImage();
            }
        });



    }

    private void BrowseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Please Select an image", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri = data.getData();
        imagePath = getRealPathFromUri(uri);
        previewImage(imagePath);
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private void previewImage(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgProfile.setImageBitmap(myBitmap);
        }
    }

    private void StrictMode() {
        android.os.StrictMode.ThreadPolicy policy =
                new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        android.os.StrictMode.setThreadPolicy(policy);


    }

    private void SaveImageOnly() {
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/from-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);

        ItemAPI itemAPI = Url.getInstance().create(ItemAPI.class);
        Call<ImageResponse> responseBodyCall = itemAPI.uploadImage( Url.Cookie,body);
        StrictMode();
        try {
            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();

            imageName = imageResponseResponse.body().getFilename();
        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void Save() {
        SaveImageOnly();
        String code= etCode.getText().toString();
        String name= etName.getText().toString();
        String desc= etDescription.getText().toString();
        String price = etPrice.getText().toString();


        Call<ResponseBody> call=MainActivity.itemAPI.addItem(Url.Cookie,new Item(code, name,price,desc,imageName));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(ItemActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("tag","res"+response.toString());
                    return;
                }
                Toast.makeText(ItemActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ItemActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("tag","err"+ t.getLocalizedMessage());


            }
        });



    }



}

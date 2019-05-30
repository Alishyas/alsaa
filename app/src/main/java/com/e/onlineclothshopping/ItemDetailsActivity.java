package com.e.onlineclothshopping;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ItemDetailsActivity extends AppCompatActivity {
    ImageView imgProfile;
    TextView tvCode, tvName, tvPrice, tvDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        imgProfile=findViewById(R.id.imgProfile);
        tvCode= findViewById(R.id.tvCode);
        tvName=findViewById(R.id.tvName);
        tvPrice=findViewById(R.id.tvPrice);
        tvDescription=findViewById(R.id.tvDescription);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            String imgPath= Url.BASE_URL + "uploads/" + bundle.getString("image");
            StrictMode();

            try {
              URL  url = new URL(imgPath);
                imgProfile.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            tvCode.setText("Item Code: " + bundle.getString("code"));
            tvName.setText("Item Name: "+ bundle.get("name"));
            tvPrice.setText("Item Price: $"+ bundle.getString("price"));
            tvDescription.setText("Description: "+ bundle.getString("description"));

        }
    }

    private void StrictMode() {
        android.os.StrictMode.ThreadPolicy policy =
                new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
        android.os.StrictMode.setThreadPolicy(policy);


    }
}

package com.e.onlineclothshopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {
   private RecyclerView recyclerView;
   private Button add, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        add = findViewById(R.id.addItem);
        logout=findViewById(R.id.logout);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this,ItemActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserClient userClient= Url.getInstance().create(UserClient.class);
                Call<ResponseBody> log = userClient.logout(Url.Cookie);
                log.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Url.Cookie="";
                            Intent intent = new Intent(DashboardActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        recyclerView=findViewById(R.id.recyclerView);
        getAllItems();


    }

    private void getAllItems() {
       ItemAPI itemAPI = Url.getInstance().create(ItemAPI.class);
        Call<List<Item>> listCall = itemAPI.getAllItems(Url.Cookie);
        listCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(DashboardActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Item> itemsList = response.body();
                ContactsAdapter contactsAdapter = new ContactsAdapter(DashboardActivity.this,itemsList);
                recyclerView.setAdapter(contactsAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(DashboardActivity.this,2));
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Erorr"+ t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

package com.e.onlineclothshopping;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button signIn, signUp;
    private EditText uname, pwd;
    private TextInputLayout uLayout, pLayout;
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create());
    public static Retrofit retrofit = builder.build();


    public static UserClient userClient = retrofit.create(UserClient.class);
    public static ItemAPI itemAPI=retrofit.create(ItemAPI.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname = findViewById(R.id.uname);
        pwd = findViewById(R.id.pwd);
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signup);
        uLayout = findViewById(R.id.input_layout_username);
        pLayout = findViewById(R.id.input_layout_password);
        uname.addTextChangedListener(new MyText(uname));
        pwd.addTextChangedListener(new MyText(pwd));
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUp);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uname.getText().toString().isEmpty()) {
                    uLayout.setError("Enter Your User Name");
                }

                if (pwd.getText().toString().isEmpty()) {
                    pLayout.setError("Enter Your Password");
                }

                else {
                    String u = uname.getText().toString();
                    String p = pwd.getText().toString();

                    Call<ResponseBody> call = userClient.validateUser(new User(u, p));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Url.Cookie= response.headers().get("Set-Cookie");
                                try {

                                    JSONObject res= new JSONObject((response.body().string()));
                                    Toast.makeText(MainActivity.this, res.getString("status"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                Intent dashboard= new Intent(MainActivity.this,DashboardActivity.class);
                                startActivity(dashboard);
                                finish();

                            } else {
                                Toast.makeText(MainActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });

    }

    private class MyText implements TextWatcher {
        private View view;

        private MyText(View view) {
            this.view=view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.uname:
                    uLayout.setError(null);
                    break;
                case R.id.pwd:
                    pLayout.setError(null);
                    break;
            }
        }


    }
}


package com.e.onlineclothshopping;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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

public class SignUpActivity extends AppCompatActivity {
    private Button signIn,signUp;
    private EditText fname, lname, uname,pwd;
    private TextInputLayout fLayout, lLayout, uLayout, pLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signIn=findViewById(R.id.signin);
        signUp=findViewById(R.id.signup);
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        uname=findViewById(R.id.uname);
        pwd=findViewById(R.id.pwd);
        fLayout=findViewById(R.id.input_layout_firstname);
        lLayout=findViewById(R.id.input_layout_lastname);
        uLayout=findViewById(R.id.input_layout_username);
        pLayout=findViewById(R.id.input_layout_password);
        fname.addTextChangedListener(new MyTextWatcher(fname));
        lname.addTextChangedListener(new MyTextWatcher(lname));
        uname.addTextChangedListener(new MyTextWatcher(uname));
        pwd.addTextChangedListener(new MyTextWatcher(pwd));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().toString().isEmpty()){
                    fLayout.setError("Enter your First Name");
                }
                if(lname.getText().toString().isEmpty()){
                    lLayout.setError("Enter Your Last Name");
                }

                if(uname.getText().toString().isEmpty()){
                    uLayout.setError("Enter Your User Name");
                }

                if(pwd.getText().toString().isEmpty()){
                    pLayout.setError("Enter Your Password");
                }

                else{
                    String f=fname.getText().toString();
                    String l= lname.getText().toString();
                    String u= uname.getText().toString();
                    String p= pwd.getText().toString();
                    Call<ResponseBody> call=MainActivity.userClient.createUser(new User(f,l,u,p));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                try {
                                    JSONObject res= new JSONObject((response.body().string()));
                                    Toast.makeText(SignUpActivity.this, res.getString("status"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            else{
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    JSONObject user= jObjError.getJSONObject("err");


                                    Toast.makeText(SignUpActivity.this, user.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });






        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(signIn);
                finish();
            }
        });
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View view){
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
            switch (view.getId()){
                case R.id.fname:
                    fLayout.setError(null);
                    break;
                case R.id.lname:
                    lLayout.setError(null);
                    break;

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

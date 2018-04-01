package com.example.shivang.codex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PatternMatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;


public class SignINActivity extends AppCompatActivity {
    private static final String TAG = "SignINActivity";
    EditText editText,editText2;String password,Email;
    boolean flag;String message;
    public static final String PREFS_NAME = "MyPrefsFile";
    ProgressDialog progressDialog;
    private int uid=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

         editText=(EditText)findViewById(R.id.editText);
         editText2=(EditText)findViewById(R.id.editText2);
    }

    public void logInMethod(View view) {
        Email = editText.getText().toString();
        password = editText2.getText().toString();
        check();
        if (flag) {
            if (Utility.isConnection(this)) {
                login();
            } else {
                Toast.makeText(getApplicationContext(), "no Internet Connection Bro! ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        setProgressBar();


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", Email);
        jsonObject.addProperty("password", password);



        Log.d(TAG,jsonObject.toString());
        Ion.with(this).load(Constant
                .URL +Constant.SIGNIN).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
                dismissProgressBar();
                if (e != null) {
                    Toast.makeText(SignINActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                    Log.e("Signin", e.toString());
                } else {
                    try {
                        Log.d(TAG,result);
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.length() > 0) {
                            int msgStatus = jsonObject.getInt("status");
                            if(msgStatus==1){
                                uid=Integer.parseInt(jsonObject.getString("UID"));
                                putStatus();
                                String returnMessage=jsonObject.getString("message");
                                Intent detailsActivity=new Intent(SignINActivity.this,AppSetupActiviy.class);
                                detailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(SignINActivity.this,returnMessage, Toast.LENGTH_LONG).show();
                                startActivity(detailsActivity);
                            }
                            else{
                                Toast.makeText(SignINActivity.this,"Login Unsuccessful", Toast.LENGTH_LONG).show();
                            }

                        }
                    } catch (Exception e1) {
                        Toast.makeText(SignINActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                        Log.e("Signin", e1.toString());
                    }

                }
            }
        });
    }
    private void setProgressBar(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private void dismissProgressBar(){
        progressDialog.dismiss();
    }

    private void putStatus(){
        SharedPreferences preferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.putInt("UID", uid);
        editor.putString("EMAIL", Email);
        editor.putString("PASSWORD", password);
        editor.commit();
    }

    public void check(){
        flag=true;message="";
        if(!(Patterns.EMAIL_ADDRESS.matcher(Email).matches())){
            flag=false;
            message="you entered wrong e-mail id";
        }
        if(Email==null){
            flag=false;
            message="you did  not enter your e-mail id";
        }
        if(password==null){
            flag=false;
            message="you did  not enter your password";
        }
    }



    public void signUpMethod(View view){
        Intent myIntent=new Intent(this,SignUpActivity.class);
        startActivity(myIntent);

    }

}

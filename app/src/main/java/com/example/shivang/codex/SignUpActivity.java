package com.example.shivang.codex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import static android.R.attr.id;
import static com.example.shivang.codex.SignINActivity.PREFS_NAME;

public class SignUpActivity extends AppCompatActivity {
     EditText SignUpName,SignUpNum,SignEID,SignUpPass,SignConUpPass;
     String name,number,EID,pass,conPass;
     boolean flag;String message;
    ProgressDialog progressDialog; int countContacts;
    int PlacePickerRequest=1;
    int uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SignUpName=(EditText)findViewById(R.id.name_editText4);
        SignUpNum=(EditText)findViewById(R.id.phNumb_editText4);
        SignEID=(EditText)findViewById(R.id.editText4);
        SignUpPass=(EditText)findViewById(R.id.editText6);
        SignConUpPass=(EditText)findViewById(R.id.editText8);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        do {
            countContacts++;

        } while (phones.moveToNext());
        SharedPreferences preferences=getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("countContacts",countContacts);
    }
    public  void signUpMethod(View view) {
         name = SignUpName.getText().toString();
         number = SignUpNum.getText().toString();
         EID = SignEID.getText().toString();
         pass = SignUpPass.getText().toString();
         conPass = SignConUpPass.getText().toString();
         check();
        if(flag){
            if(Utility.isConnection(this)){
                signup();
            }
            else{
                Toast.makeText(getApplicationContext(),"no Internet Connection Bro! ",Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
    }
    public void check(){
        flag=true;message="";
        if(name==null){
            flag=false;
            message="Did you forget your name!";
        }
        if((number==null) || !(number.length()==10)){
            flag=false;
            message="10 digits please";
        }

        if((EID==null) || !(Patterns.EMAIL_ADDRESS.matcher(EID).matches())){
            flag=false;
            message="your e-mail id bro";
        }
        if(pass==null) {
            flag = false;
            message = "enter password";
        }
        if(conPass==null) {
            flag = false;
            message = "confirm your password";
        }
        if(!(conPass.equals(pass))){
            flag = false;
            message = "passwords don't match";
        }
    }
    public void signup(){
        setProgressBar();


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("mobile", number);
        jsonObject.addProperty("email", EID);
        jsonObject.addProperty("password", pass);


        Ion.with(this).load(Constant
                .URL +Constant.SIGNUP).setTimeout(20000).setJsonObjectBody(jsonObject).asString().setCallback(new FutureCallback<String>() {

            @Override
            public void onCompleted(Exception e, String result) {
                dismissProgressBar();
                if (e != null) {
                    Toast.makeText(SignUpActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                    Log.e("Signin", e.toString());
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.length() > 0) {
                            int msgStatus = jsonObject.getInt("status");
                            if(msgStatus==1){

                                uid=Integer.parseInt(jsonObject.getString("UID"));
                                putStatus();
                                Intent detailsActivity=new Intent(SignUpActivity.this,AppSetupActiviy.class);
                                detailsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(detailsActivity);
                            }
                            else{
                                Toast.makeText(SignUpActivity.this,jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        }
                    } catch (Exception e1) {
                        Toast.makeText(SignUpActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
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
        editor.putString("NAME",name);
        editor.putString("EMAIL", EID);
        editor.putString("PASSWORD", pass);
        editor.commit();
    }
    }



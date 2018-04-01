package com.example.shivang.codex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WriteMessageActvity extends AppCompatActivity {
    TextView textView;
    EditText editTextMesg;
    TextView MemoTextView;
    EditText MemoEditTextMesg;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message_actvity);
        editTextMesg=(EditText)findViewById(R.id.editText3);
        MemoEditTextMesg=(EditText)findViewById(R.id.MemoEditText3);

        SharedPreferences settings = getSharedPreferences(SignINActivity.PREFS_NAME, 0);
        String randomColor = settings.getString("backgroundColor", null);
        String textColor = settings.getString("buttonColor", null);

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.activity_write_message_actvity);
        Button b=(Button)findViewById(R.id.button6);

        linearLayout.setBackgroundColor(Color.parseColor(randomColor));
        b.setBackgroundColor(Color.parseColor(textColor));
    }
    public void onSendMsg(View view){
        String message=editTextMesg.getText().toString();
        String memo=MemoEditTextMesg.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("message",message);
        returnIntent.putExtra("memo",memo);
        setResult(2,returnIntent);
        finish();

    }
}

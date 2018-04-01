package com.example.shivang.codex;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;
import static com.example.shivang.codex.SignINActivity.PREFS_NAME;

/**
 * Created by shivang on 13-04-2017.
 */

public class RenameGroupDBox extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    public Button ok;
    String s;
    EditText editText;

    public RenameGroupDBox(Activity a,String s) {
        super(a);
        this.c = a;
        this.s=s;
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        editText=(EditText)findViewById(R.id.dialogBoxEditText) ;
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
    }
    public void onClick(View v) {
        String newGroupName=editText.getText().toString();
        SharedPreferences preferences=getContext().getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("newGroupName",newGroupName);
        GroupsHelper obj=new GroupsHelper(getContext());
        Log.d("Dialog",s);
        obj.changeTableName(s,newGroupName);
        c.recreate();
        dismiss();
    }
}

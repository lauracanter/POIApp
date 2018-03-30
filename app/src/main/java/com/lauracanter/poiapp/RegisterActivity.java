package com.lauracanter.poiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.app.AlertDialog;

/**
 * Created by Laura on 07/03/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    //Constants
    static private String CHAT_PREFS = "ChatPrefes";
    static private String DISPLAY_KEY_NAME = "username";

    //UI Reference Variables
    private AutoCompleteTextView mUsernameView, mEmailView;
    private TextView mPasswordView, mConfirmPasswordView;

    //FireBase Authentication DB Reference
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (TextView) findViewById(R.id.register_password);
        mConfirmPasswordView = (TextView) findViewById(R.id.register_confirm_password);

        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                 if(id == R.integer.register_form_finished || id == EditorInfo.IME_NULL)
                 {
                     attemptRegistration();
                     return  true;
                 }
                 else return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    //when signUp button pressed
    public void signUp(View v){
        attemptRegistration();
    }

    public void attemptRegistration()
    {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Check validity of password
        if(!TextUtils.isEmpty(password) && isPasswordValid(password))
        {
            mPasswordView.setError("Password field either empty or too short");
            focusView = mPasswordView;
            cancel = true;
        }

        //Check validity of email
        if(TextUtils.isEmpty(email))
        {
            mEmailView.setError("Email invalid");
            focusView = mEmailView;
            cancel = true;
        }
        else if(!isEmailValid(email))
        {
            mEmailView.setError("Email invalid");
            focusView = mEmailView;
            cancel = true;
        }

        if(cancel)
        {
            focusView.requestFocus();
        }
        else createFireBaseUser();

    }

    public void createFireBaseUser()
    {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("POIApp", "CreateUser Completed: "+task.isSuccessful());

                if(!task.isSuccessful())
                {
                    Log.d("POIApp", "CreateUser Failed"+task.isSuccessful());
                    showErrorDialog("Registration attempt failed");
                }
                else{
                    saveDisplayName();
                    Intent intent = new Intent(RegisterActivity.this, TagwordsActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

    }

    public boolean isPasswordValid(String password)
    {
        mConfirmPasswordView = (TextView) findViewById(R.id.register_confirm_password);
        String confirmPassword = mConfirmPasswordView.getText().toString();
        return ((confirmPassword == password) && (password.length() > 5));
    }

    public boolean isEmailValid(String email)
    {
        return email.contains("@");
    }

    public void saveDisplayName()
    {
        String displayName = mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(DISPLAY_KEY_NAME, displayName).apply();
    }

    public void showErrorDialog(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert);
    }
}

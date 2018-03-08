package com.lauracanter.poiapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    public void signInExistingUser(View v){
        attemptLogin();
    }

    public void registerNewUser(View v)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(intent);

    }

    public void attemptLogin()
    {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if(email.equals("") || password.equals(""))
        {
            return;
        }
        else
        {
            Toast.makeText(this, "Login in process", Toast.LENGTH_SHORT);
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("POIApp", "signInWithEmailAndPassoword complete: " + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.d("POIApp", "Problem Signing In: " + task.isSuccessful());
                    showMessageDialog("There was a problem signing in.");
                } else {
                    Intent intent = new Intent(LoginActivity.this, TagwordsActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    public void showMessageDialog(String message)
    {

    }
}

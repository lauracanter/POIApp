package com.lauracanter.poiapp;

import com.microsoft.windowsazure.mobileservices.*;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

import android.app.AlertDialog;

import java.net.MalformedURLException;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private MobileServiceClient mClient;
    private TodoItem item = new TodoItem();
    private boolean b = false;
    private AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        try {
            mClient = new MobileServiceClient(
                    "https://poimobileapp.azurewebsites.net",
                    this);
        } catch (MalformedURLException e) {
            Log.d("Msgs", "Unsuccessful: "+e.toString());
            e.printStackTrace();
        }


        item.Text = "Awesome item";

        mClient.getTable(TodoItem.class).insert(item, new TableOperationCallback<TodoItem>() {
            @Override
            public void onCompleted(TodoItem entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    // Insert succeeded
                    b=true;
                } else {
                    // Insert failed
                }
            }
        });

        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);

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


    public void registerNewUser(View v)
    {
        Intent intent = new Intent(this, com.lauracanter.poiapp.RegisterActivity.class);
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
                Log.d("Msgs", "signInWithEmailAndPassoword complete: " + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.d("Msgs", "Problem Signing In: " + task.isSuccessful());
                    showMessageDialog("There was a problem signing in.");
                } else {
                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    //executes when user taps signIn button
    public void signInExistingUser(View v){
        attemptLogin();
    }

    public void showMessageDialog(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Ooops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public class TodoItem {
        public String Id;
        public String Text;
    }
}

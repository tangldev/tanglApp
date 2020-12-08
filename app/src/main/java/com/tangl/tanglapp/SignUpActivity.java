package com.tangl.tanglapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final String TAG = "SIGNUP.ACTIVITY";
    private Boolean emailInputsStatus = false;
    private Boolean passwordInputStatus = false;
    private ProgressBar signup_progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        final EditText passwordInput = (EditText) findViewById(R.id.password_input);
        final EditText emailInput = (EditText) findViewById(R.id.email_input);
        final TextView emailInputWarning = findViewById(R.id.email_warning);
        final TextView passwordInputWarning= findViewById(R.id.password_warning);
        final Button signUpButton = (Button) findViewById(R.id.sign_up_button2);
        signup_progressbar = (ProgressBar) findViewById(R.id.signup_progressbar);
        signUpButton.setEnabled(false);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validEmail(charSequence.toString())){
                    emailInputWarning.setText(R.string.not_valid_email);
                    emailInputWarning.setVisibility(View.VISIBLE);
                    signUpButton.setEnabled(false);
                }
                else{
                   emailInputWarning.setVisibility(View.INVISIBLE);//do one for both instead of having them share ....maybe
                    emailInputsStatus = true;
                    if(checkBothEmailAndPassword()){
                        signUpButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() < 8){
                    passwordInputWarning.setText(R.string.password_too_short);
                    passwordInputWarning.setVisibility(View.VISIBLE);
                    signUpButton.setEnabled(false);
                }
                else{
                    passwordInputWarning.setVisibility(View.INVISIBLE);
                    passwordInputStatus = true;
                    if(checkBothEmailAndPassword()){
                        signUpButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
              createEmailUser(emailInput.getText().toString(),passwordInput.getText().toString());
              signup_progressbar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void createEmailUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            assert user != null;
                            Map<String, Object> newUser = new HashMap<>();
                            newUser.put("email", Objects.requireNonNull(user.getEmail()));
                            Date joinDate = new Date();
                            newUser.put("join_date", joinDate.toString());
                            database.collection("Users").document(user.getUid()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent homeIntent = new Intent(getApplicationContext(),BaseActivity.class);
                                    signup_progressbar.setVisibility(View.INVISIBLE);
                                    startActivity(homeIntent);
                                }
                            });
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"login failed",Toast.LENGTH_LONG).show();
                            signup_progressbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private Boolean checkBothEmailAndPassword(){
        return passwordInputStatus && emailInputsStatus;
    }

}
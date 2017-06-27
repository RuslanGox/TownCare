package com.example.ruslan.towncare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.User.UserFireBase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_signIn = (Button) findViewById(R.id.loginButtonSignIn);
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = ((EditText) findViewById(R.id.loginUserName)).getText().toString();
                final String password = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
                UserFireBase.loginAccount(LoginActivity.this, email, password, new MasterInterface.LoginAccountCallBack() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success -> " + email);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
            }
        });

        Button btn_signUp = (Button) findViewById(R.id.loginButtonSignUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}

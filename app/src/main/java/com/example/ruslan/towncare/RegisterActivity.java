package com.example.ruslan.towncare;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ruslan.towncare.Models.User.User;
import com.example.ruslan.towncare.Models.User.UserFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);

        Button btn_register = (Button) findViewById(R.id.createAccountRegisterButton);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = ((EditText) findViewById(R.id.createAccountUserEmail)).getText().toString();
                final String password = ((EditText) findViewById(R.id.createAccountPassword)).getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "createUserWithEmail:success -> " + email);
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(((EditText) findViewById(R.id.createAccountId)).getText().toString())
                                            .build();
                                    if (user != null) {
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("TAG", "User profile updated -> " + user.getDisplayName());
                                                            UserFireBase.addUser(newUser());
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                }
                            }
                        });

            }
        });

        Button btn_cancel = (Button) findViewById(R.id.createAccountCancelButton);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private User newUser() {
        String userEmail = ((EditText) findViewById(R.id.createAccountUserEmail)).getText().toString();
        String userId = ((EditText) findViewById(R.id.createAccountId)).getText().toString();
        String userTown = ((EditText) findViewById(R.id.createAccountTown)).getText().toString();
        String userPhone = ((EditText) findViewById(R.id.createAccountPhone)).getText().toString();
        return new User(userEmail, userId, userTown, userPhone);
    }
}

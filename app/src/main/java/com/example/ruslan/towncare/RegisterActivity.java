package com.example.ruslan.towncare;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.Models.User.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btn_register = (Button) findViewById(R.id.createAccountRegisterButton);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // register new user
                final String email = ((EditText) findViewById(R.id.createAccountUserEmail)).getText().toString();
                final String password = ((EditText) findViewById(R.id.createAccountPassword)).getText().toString();
                final String id = ((EditText) findViewById(R.id.createAccountId)).getText().toString();
                Model.instance.registerAccount(RegisterActivity.this, email, password, id, new MasterInterface.RegisterAccountCallBack() {
                    @Override
                    public void onComplete(FirebaseUser user, Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated -> " + user.getDisplayName());
                            Model.instance.addUser(newUser()); // creates also new user in "users" table
                            finish();
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

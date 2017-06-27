package com.example.ruslan.towncare.Models.User;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.ruslan.towncare.LoginActivity;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Namregog on 6/21/2017.
 */

public class UserFireBase {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String USERS_TABLE = "Users";
    private static DatabaseReference myRef = database.getReference(USERS_TABLE);

    public static void addUser(User user) {
        myRef.child("" + user.getUserId()).setValue(user);
    }


    public static String getCurrentLoggedUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getDisplayName();
        } else {
            return null;
        }
    }

    public static void getUser(String accountId, final MasterInterface.GetUserCallback callback) {
        Log.d("TAG", "accountId is " + accountId);
        myRef.child(accountId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onComplete(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    public static void registerAccount(RegisterActivity registerActivity, final String email, final String password, final String id, final MasterInterface.RegisterAccountCallBack callBack) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registerActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success -> " + email);
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(id)
                                    .build();
                            if (user != null) {
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                callBack.onComplete(user, task);

                                            }
                                        });
                            }
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public static void loginAccount(final LoginActivity loginActivity, final String email, final String password, final MasterInterface.LoginAccountCallBack callBack) {
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            callBack.onComplete(task);
                        }
                    });
        } else {
            Toast.makeText(loginActivity, "Please Enter Credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
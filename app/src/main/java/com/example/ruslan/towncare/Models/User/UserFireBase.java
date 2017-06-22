package com.example.ruslan.towncare.Models.User;

import android.util.Log;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Namregog on 6/21/2017.
 */

public class UserFireBase {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final String USERS_TABLE = "Users";
    private static DatabaseReference myRef = database.getReference(USERS_TABLE);

//    public static void getData(final MasterInterface.GetAllCasesCallback callback) {
////        ValueEventListener listener =
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Case> list = new LinkedList<Case>();
//                for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                    Case aCase = snap.getValue(Case.class);
//                    list.add(aCase);
//                }
//                callback.onComplete(list);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                callback.onCancel();
//            }
//        });
//    }

    public static void addUser(User user) {
        myRef.child("" + user.getUserId()).setValue(user);
    }

    public static String getCurrentUserId(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getDisplayName();
        } else {
            return null;
        }
    }

    public static void getUser(String accountId, final MasterInterface.GetUserCallback callback) {
        Log.d("TAG","accountId is " + accountId);
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

}

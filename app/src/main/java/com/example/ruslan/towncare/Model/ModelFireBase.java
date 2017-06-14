package com.example.ruslan.towncare.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 10-Jun-17.
 */

public class ModelFireBase {

    private List<Case> caseList = new LinkedList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Cases");


    public List<Case> getData() {
        return caseList;
    }

    public void addCase(Case c) {
        myRef.child("" + c.getCaseId()).setValue(c);
    }

    public void removeCase(int id) {
        caseList.remove(id);
    }

    interface GetCaseCallback {
        void onComplete (Case aCase);
        void onCancel ();
    }

    public void getCase(String id , final GetCaseCallback callback) {
        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Case aCase = dataSnapshot.getValue(Case.class);
                callback.onComplete(aCase);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }
}

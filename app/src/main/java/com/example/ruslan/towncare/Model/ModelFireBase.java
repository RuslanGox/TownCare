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


    interface GetAllCasesCallback{
        void onComplete(List<Case> list);
        void onCancel();
    }

    public void getData(final GetAllCasesCallback callback) {
        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Case> list = new LinkedList<Case>();
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Case aCase = snap.getValue(Case.class);
                    list.add(aCase);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    public void addCase(Case c) {
        myRef.child("" + c.getCaseId()).setValue(c);
    }

    public void removeCase(String id, final GetCaseCallback callback) {
        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                callback.onComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    interface GetCaseCallback {
        void onComplete (Case aCase);
        void onComplete ();
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

    public void updateCase(Case aCase){
        addCase(aCase);
    }
}

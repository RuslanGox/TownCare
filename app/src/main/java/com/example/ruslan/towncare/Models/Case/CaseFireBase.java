package com.example.ruslan.towncare.Models.Case;

import com.example.ruslan.towncare.Models.MasterInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ruslan on 16/06/2017.
 */

public class CaseFireBase {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference(CaseSql.CASE_TABLE);

    public static void getData(final MasterInterface.GetAllCasesCallback callback) {
//        ValueEventListener listener =
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Case> list = new LinkedList<Case>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
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

    public static void addCase(Case c) {
        myRef.child("" + c.getCaseId()).setValue(c);
    }

    public static void removeCase(String id, final MasterInterface.GetCaseCallback callback) {
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

    public static void getCase(String id, final MasterInterface.GetCaseCallback callback) {
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

    public static void updateCase(Case aCase) {
        addCase(aCase);
    }
}

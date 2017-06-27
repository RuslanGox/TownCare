package com.example.ruslan.towncare.Models.Case;

import android.util.Log;

import com.example.ruslan.towncare.Models.Enums.DataStateChange;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ruslan on 16/06/2017.
 */

public class CaseFireBase {
    public static final String SORT_CASE_LAST_UPDATE = "CaseLastUpdate";

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference(CaseSql.CASE_TABLE);

    /* --- Public Methods --- */

    public static void getData(long CaseLastUpdate, final MasterInterface.GetAllCasesCallback callback) {
        myRef.orderByChild(SORT_CASE_LAST_UPDATE).startAt(CaseLastUpdate);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Case> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Case aCase = snap.getValue(Case.class);
                    if (Model.CurrentUser.getUserTown().equalsIgnoreCase(aCase.getCaseTown())) { // show only case for user town
                        list.add(aCase);
                    }
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

//        String key = myRef.push().getKey(); // this will create a new unique key
        Map<String, Object> value = new HashMap<>();
        value.put("CaseLastUpdate", ServerValue.TIMESTAMP);
        value.put("caseId", c.getCaseId());
        value.put("caseTitle", c.getCaseTitle());
        value.put("caseDate", c.getCaseDate());
        value.put("caseLikeCount", c.getCaseLikeCount());
        value.put("caseType", c.getCaseType());
        value.put("caseStatus", c.getCaseStatus());
        value.put("caseOpenerPhone", c.getCaseOpenerPhone());
        value.put("caseOpenerId", c.getCaseOpenerId());
        value.put("caseTown", c.getCaseTown());
        value.put("caseAddress", c.getCaseAddress());
        value.put("caseDesc", c.getCaseDesc());
        value.put("caseImageUrl", c.getCaseImageUrl());

        myRef.child("" + c.getCaseId()).setValue(value);
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

    public static void updateCase(Case aCase) {
        addCase(aCase);
    }

    public static void syncAndRegisterCaseData(long lastUpdate, final MasterInterface.RegisterCasesEvents callback) {
        Log.d("TAG", "syncAndRegisterCaseData - CaseFireBase - pulling data from firebase");
        myRef.orderByChild("CaseLastUpdate").startAt(lastUpdate).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Case aCase = dataSnapshot.getValue(Case.class);
                Log.d("TAG", "syncAndRegisterCaseData - CaseFireBase - onChildAdded " + aCase.getCaseTitle());
                callback.onCaseUpdate(aCase, DataStateChange.ADDED);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Case aCase = dataSnapshot.getValue(Case.class);
                Log.d("TAG", "syncAndRegisterCaseData - CaseFireBase - onChildChanged " + aCase.getCaseTitle());
                callback.onCaseUpdate(aCase, DataStateChange.CHANGED);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Case aCase = dataSnapshot.getValue(Case.class);
                Log.d("TAG", "syncAndRegisterCaseData - CaseFireBase - onChildRemoved " + aCase.getCaseTitle());
                callback.onCaseUpdate(aCase, DataStateChange.REMOVED);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Case aCase = dataSnapshot.getValue(Case.class);
                Log.d("TAG", "syncAndRegisterCaseData - CaseFireBase - onChildMoved " + aCase.getCaseTitle());
                callback.onCaseUpdate(aCase, DataStateChange.CHANGED);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

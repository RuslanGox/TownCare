package com.example.ruslan.towncare.Models.Case;

import android.util.Log;

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
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference(CaseSql.CASE_TABLE);

    public static void getData(long CaseLastUpdate, final MasterInterface.GetAllCasesCallback callback) {
        myRef.orderByChild("CaseLastUpdate").startAt(CaseLastUpdate);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Case> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Case aCase = snap.getValue(Case.class);
                    if (Model.CurrentUser.getUserTown().equalsIgnoreCase(aCase.getCaseTown())) {
                        list.add(aCase);
                    }
                }
                Log.d("TAG", "list size is " + list.size());
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
//        queryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Case> list = new LinkedList<>();
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
    }

    public static void addCase(Case c) {
//        Map<String, Object> value = new HashMap<>();
//        value.put("timestamp", ServerValue.TIMESTAMP);
//
//        Log.d("TAG"," time stamp " + value.get("timestamp"));
//        c.setCaseLastUpdateDate(x);
        String key = myRef.push().getKey(); // this will create a new unique key
        Map<String, Object> value = new HashMap<>();
        value.put("timeStamp", ServerValue.TIMESTAMP);
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

//        c.setCaseLastUpdateDate(ServerValue.TIMESTAMP);
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

//    public static void getCase(String id, final MasterInterface.GetCaseCallback callback) {
//        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Case aCase = dataSnapshot.getValue(Case.class);
//                callback.onComplete(aCase);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                callback.onCancel();
//            }
//        });
//    }

    public static void updateCase(Case aCase) {
        addCase(aCase);
    }

    public static void syncAndRegisterCaseData(long lastUpdate , final MasterInterface.RegisterCasesEvents callback) {
        myRef.orderByChild("CaseLastUpdate").startAt(lastUpdate).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Case aCase = dataSnapshot.getValue(Case.class);
                callback.onCaseUpdate(aCase);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Case aCase = dataSnapshot.getValue(Case.class);
                callback.onCaseUpdate(aCase);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Case aCase = dataSnapshot.getValue(Case.class);
                callback.onCaseUpdate(aCase);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Case aCase = dataSnapshot.getValue(Case.class);
                callback.onCaseUpdate(aCase);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

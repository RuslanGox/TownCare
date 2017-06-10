package com.example.ruslan.towncare.Model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 10-Jun-17.
 */

public class ModelFireBase {

    private List<Case> caseList = new LinkedList<>();

    public List<Case> getData() {
        return caseList;
    }

    public void addCase(Case c) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Cases");
        myRef.child(""+c.getCaseId()).setValue(c);
    }

    public void removeCase(int id) {
        caseList.remove(id);
    }

    public Case getCase(int id) {
        return caseList.get(id);
    }
}

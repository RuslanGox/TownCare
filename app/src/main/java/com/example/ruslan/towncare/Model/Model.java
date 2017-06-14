package com.example.ruslan.towncare.Model;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    public final static Model instance = new Model();
    List<Case> caseList = new LinkedList<>();
    private ModelFireBase modelFireBase = new ModelFireBase();

    private Model() {
        for (int i = 0; i < 5; i++) {
            caseList.add(new Case("" + 1234 + System.currentTimeMillis() + i, "case " + i, "11/12/17", 500 + i * 3, 200 - i * 5, "1", "Open", "052476", 1234, "GIDON ISRAEL", "THIS IS THE DEST", "URL"));

        }
        Case c = caseList.get(0);
        modelFireBase.addCase(c);
    }

    public List<Case> getData() {
        return caseList;
    }

    public void addCase(Case c) {
        caseList.add(c);
    }

    public void removeCase(String id) {
        int location = -1;
        for (Case case2 : caseList) {
            location++;
            if (case2.getCaseId().equals(id)) {
                break;
            }
        }
        caseList.remove(location);
    }

    public Case getCase(String id) {
        Log.d("TAG", "looking for id " + id);
        Log.d("TAG", "case size " + caseList.size());
        for (Case case2 : caseList) {
            Log.d("TAG", "id:" + case2.getCaseId());
            Log.d("TAG", "" + case2.getCaseId().length());
            Log.d("TAG", "" + id.length());
            if (Objects.equals(case2.getCaseId(), id)) {
                Log.d("TAG", "found it");
                return case2;
            }
        }
        return null;
    }

    public void updateCase(Case c) {
        Log.d("TAG", "" + (c.getCaseId()));
        removeCase(c.getCaseId());
        caseList.add(c);
    }
}

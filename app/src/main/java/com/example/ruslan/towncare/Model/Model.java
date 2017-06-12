package com.example.ruslan.towncare.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    public final static Model instance = new Model();
    List<Case> caseList = new LinkedList<>();
    private ModelFireBase modelFireBase = new ModelFireBase();

    private Model() {
        for (int i = 0; i < 5; i++) {
            caseList.add(new Case(i, "case " + i, "11/12/17", 500 + i * 3, 200 - i * 5, "1", "Open", "052476", 1234, "GIDON ISRAEL", "THIS IS THE DEST", "URL"));

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

    public void removeCase(int id) {
        caseList.remove(id);
    }

    public Case getCase(int id) {
        return caseList.get(id);
    }

    public void updateCase (Case c){
        caseList.remove(c.getCaseId()-1);
        caseList.add(c);
    }
}

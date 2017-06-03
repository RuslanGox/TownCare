package com.example.ruslan.towncare.Model;

import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    public final static Model instance = new Model();
    List<Case> caseList = new LinkedList<>();


    private Model() {
        for (int i = 0 ; i<5 ; i ++){
            caseList.add(new Case ("case " + i,"11/12/17",500+i*3,200-i*5,"Suggestion","Open","052476",1234,"GIDON ISRAEL","THIS IS THE DEST","URL"));
        }
    }

    public List<Case> getData(){
        return caseList;
    }

    public void addCase(Case c){
        caseList.add(c);
    }

    public void removeCase (int id){
        caseList.remove(id);
    }

    public Case getCase(int id){
        return caseList.get(id);
    }
}

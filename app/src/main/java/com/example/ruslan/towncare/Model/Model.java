package com.example.ruslan.towncare.Model;

import com.example.ruslan.towncare.MyApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    public final static Model instance = new Model();
    List<Case> caseList = new LinkedList<>();
    private ModelFireBase modelFireBase = new ModelFireBase();
    private ModelSql modelSql;

    private Model() {
        modelSql = new ModelSql(MyApplication.getMyContext());
        for (int i = 0; i < 5; i++) {
            caseList.add(new Case("" + 1234 + System.currentTimeMillis() + i, "case " + i, "11/12/17", 500 + i * 3, 200 - i * 5, "1", "Open", "052476", "1234", "GIDON ISRAEL", "THIS IS THE DEST", "URL"));
//            CaseSql.addCase(modelSql.getWritableDatabase(),caseList.get(i));
            modelFireBase.addCase(caseList.get(i));
        }
    }

    public interface GetAllCasesCallback {
        void onComplete(List<Case> list);

        void onCancel();
    }

    public List<Case> getDataSql() {
        return CaseSql.getData(modelSql.getReadableDatabase());
    }

    public void getData(final GetAllCasesCallback callback) {
        modelFireBase.getData(new ModelFireBase.GetAllCasesCallback() {
            @Override
            public void onComplete(List<Case> list) {
                for (Case aCase : list) {
                    CaseSql.addCase(modelSql.getWritableDatabase(), aCase);
                }
                callback.onComplete(CaseSql.getData(modelSql.getReadableDatabase()));
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
        CaseSql.getData(modelSql.getReadableDatabase());
    }

    public void addCase(Case c) {
        CaseSql.addCase(modelSql.getWritableDatabase(), c);
        modelFireBase.addCase(c);
    }

    //    public void removeCaseSql(String id){
//        CaseSql.removeCase(modelSql.getWritableDatabase(),id);
//    }
    public void removeCase(String id, final GetCaseCallback callback) {
        modelFireBase.removeCase(id, new ModelFireBase.GetCaseCallback() {
            @Override
            public void onComplete(Case aCase) {

            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
        CaseSql.removeCase(modelSql.getWritableDatabase(), id);
    }


    public interface GetCaseCallback {
        void onComplete(Case aCase);

        void onComplete();

        void onCancel();
    }

    public Case getCase(String id) {
        return CaseSql.getCase(modelSql.getReadableDatabase(), id);
    }
//    public void getCase(String id , final GetCaseCallback callback) {
//        CaseSql.getCase(modelSql.getReadableDatabase(),id);
////        modelFireBase.getCase(id, new ModelFireBase.GetCaseCallback() {
////            @Override
////            public void onComplete(Case aCase) {
////                callback.onComplete(aCase);
////            }
////
////            @Override
////            public void onComplete() {
////
////            }
////
////            @Override
////            public void onCancel() {
////                callback.onCancel();
////            }
////        });
//    }

    public void updateCase(Case c) {
        modelFireBase.updateCase(c);
    }
}

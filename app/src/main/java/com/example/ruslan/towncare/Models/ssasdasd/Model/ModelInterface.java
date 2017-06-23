package com.example.ruslan.towncare.Model;

import java.util.List;

/**
 * Created by Ruslan on 16/06/2017.
 */

public interface ModelInterface {

    interface GetAllCasesCallback {
        void onComplete(List<Case> list);
        void onCancel();
    }

    interface GetCaseCallback {
        void onComplete(Case aCase);
        void onComplete();
        void onCancel();
    }

    void getData(final GetAllCasesCallback callback);

    void addCase(Case c);

    void removeCase(String id, final GetCaseCallback callback);

    void getCase(String id, final GetCaseCallback callback);

    void updateCase(Case c);
}


package com.example.ruslan.towncare.Models;

import android.graphics.Bitmap;
import android.view.View;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Enums.AlertDialogButtons;
import com.example.ruslan.towncare.Models.Enums.DataStateChange;
import com.example.ruslan.towncare.Models.User.User;

import java.util.List;

/**
 * Created by Ruslan on 16/06/2017.
 */

public interface MasterInterface {

    interface GetAllCasesCallback {
        void onComplete(List<Case> list);
        void onCancel();
    }

    interface RegisterCasesEvents{
        void onCaseUpdate(Case aCase , DataStateChange dsc);
    }

    interface GetCaseCallback {
        void onComplete();
        void onCancel();
    }

    interface GetUserCallback {
        void onComplete(User User);
        void onCancel();
    }

    interface SaveImageListener{
        void complete(String url);
        void fail();
    }

    interface LoadImageListener {
        void onSuccess(Bitmap image);
        void onFail();
    }

    interface loadImageFromFileAsyncListener{
        void onComplete(Bitmap bitmap);

    }

//    interface AlertCaseDialogListener {
//        void onAlertButtonClick(AlertDialogButtons which, boolean dataChanged);
//    }

    interface CaseListInteractionListener {
        void onItemListClick(String id);
    }

//    interface UpsertInteractionListener {
//        void onUpsertButtonClick(View view, boolean dataChanged);
//    }

    interface GotCurrentUserLogged{
        void Create();
    }

}


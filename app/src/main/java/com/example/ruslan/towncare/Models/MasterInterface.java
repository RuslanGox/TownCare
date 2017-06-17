package com.example.ruslan.towncare.Models;

import android.graphics.Bitmap;

import com.example.ruslan.towncare.Models.Case.Case;
import java.util.List;

/**
 * Created by Ruslan on 16/06/2017.
 */

public interface MasterInterface {

    interface GetAllCasesCallback {
        void onComplete(List<Case> list);
        void onCancel();
    }

    interface GetCaseCallback {
        void onComplete(Case aCase);
        void onComplete();
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

}


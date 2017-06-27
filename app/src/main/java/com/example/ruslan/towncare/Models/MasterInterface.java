package com.example.ruslan.towncare.Models;

import android.graphics.Bitmap;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Enums.DataStateChange;
import com.example.ruslan.towncare.Models.User.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by Ruslan on 16/06/2017.
 */

public interface MasterInterface {

    // listener for when getting data (GetData)
    interface GetAllCasesCallback {
        void onComplete(List<Case> list);
    }

    // listener for when register case with firebase (Socke.IO style)
    interface RegisterCasesEvents {
        void onCaseUpdate(Case aCase, DataStateChange dsc);
    }

    // listener for single getCase
    interface GetCaseCallback {
        void onComplete();

        void onCancel();
    }

    // listener when for getting user from FireBase Users table
    interface GetUserCallback {
        void onComplete(User User);

        void onCancel();
    }

    // listener for when saving Image
    interface SaveImageListener {
        void complete(String url);

        void fail();
    }

    // listener for when loading Image
    interface LoadImageListener {
        void onSuccess(Bitmap image);

        void onFail();
    }

    // listener for when loading Image Async for CaseList
    interface loadImageFromFileAsyncListener {
        void onComplete(Bitmap bitmap);

    }

    // listener for CaseList when clicking on each list row
    interface CaseListInteractionListener {
        void onItemListClick(String id);
    }

    // listener to get the current user logged
    interface GotCurrentUserLogged {
        void Create();
    }

    // listener when register account in the register activity
    interface RegisterAccountCallBack {
        void onComplete(FirebaseUser user, Task<Void> task);
    }

    // listener when register account in the login activity
    interface LoginAccountCallBack {
        void onComplete(Task<AuthResult> task);
    }

}


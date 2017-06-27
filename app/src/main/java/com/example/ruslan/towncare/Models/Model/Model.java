package com.example.ruslan.towncare.Models.Model;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Case.CaseFireBase;
import com.example.ruslan.towncare.Models.Case.CaseSql;
import com.example.ruslan.towncare.Models.Enums.DataStateChange;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.User.User;
import com.example.ruslan.towncare.Models.User.UserFireBase;
import com.example.ruslan.towncare.MyApplication;

import org.greenrobot.eventbus.EventBus;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    private static final String CASE_LAST_UPDATE_PARAMETER = "caseLastUpdateDate";

    public static Model instance;
    private ModelFireBase modelFireBase;
    private ModelSql modelSql;
    public static User CurrentUser;

    private Model(final MasterInterface.GotCurrentUserLogged callback) {
        modelSql = new ModelSql(MyApplication.getMyContext());
        modelFireBase = new ModelFireBase();
        UserFireBase.getUser(UserFireBase.getCurrentLoggedUserId(), new MasterInterface.GetUserCallback() {
            @Override
            public void onComplete(User user) {
                CurrentUser = user;
                Log.d("TAG", "USER READY");
                syncAndRegisterCaseData();
                callback.Create();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public static void getInstance(MasterInterface.GotCurrentUserLogged callback) {
        if (instance == null) {
            Log.d("TAG", "instance was null");
            instance = new Model(callback);
        }
    }

    // make the connection to the FireBase DB like (Socket.IO)
    private void syncAndRegisterCaseData() {
        Log.d("TAG", "syncAndRegisterCaseData entered");
        SharedPreferences ref = MyApplication.getMyContext().getSharedPreferences("TAG", MODE_PRIVATE);
        final long lastUpdate = ref.getLong(CASE_LAST_UPDATE_PARAMETER, 0);
        Log.d("TAG", "lastUpdate time is " + lastUpdate);
        CaseFireBase.syncAndRegisterCaseData(lastUpdate, new MasterInterface.RegisterCasesEvents() {
            @Override
            public void onCaseUpdate(Case aCase, DataStateChange dsc) {
                Log.d("TAG", "syncAndRegisterCaseData - MODEL - onCaseUpdate " + aCase.getCaseTitle() + "dsc " + dsc);
                switch (dsc) {
                    case ADDED:
                        if (Model.CurrentUser.getUserTown().equalsIgnoreCase(aCase.getCaseTown())) { // show only case for user town
                            CaseSql.addCase(modelSql.getWritableDatabase(), aCase);
                        }
                        break;
                    case CHANGED:
                        CaseSql.updateCase(modelSql.getWritableDatabase(), aCase);
                        break;
                    case REMOVED:
                        CaseSql.removeCase(modelSql.getWritableDatabase(), aCase.getCaseId());
                        break;
                }
                SharedPreferences.Editor prefEditor = MyApplication.getMyContext().getSharedPreferences("TAG", MODE_PRIVATE).edit();
                prefEditor.putLong(CASE_LAST_UPDATE_PARAMETER, aCase.getCaseLastUpdateDate()).apply();
                EventBus.getDefault().post(new CaseUpdateEvent(aCase));
            }
        });
    }

    public void getData(final MasterInterface.GetAllCasesCallback callback) {
        callback.onComplete(CaseSql.getData(modelSql.getReadableDatabase()));
    }

    public void addCase(Case c) {
        CaseSql.addCase(modelSql.getWritableDatabase(), c);
        modelFireBase.addCase(c);
    }

    public void removeCase(String id, final MasterInterface.GetCaseCallback callback) {
        modelFireBase.removeCase(id, new MasterInterface.GetCaseCallback() {
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

    public Case getCase(String id) {
        return CaseSql.getCase(modelSql.getReadableDatabase(), id);
    }

    public void updateCase(Case c) {
        CaseSql.updateCase(modelSql.getWritableDatabase(), c);
        modelFireBase.updateCase(c);
    }

    public void saveImage(final Bitmap imageBmp, final String name, final MasterInterface.SaveImageListener listener) {
        modelFireBase.saveImage(imageBmp, name, new MasterInterface.SaveImageListener() {
            @Override
            public void complete(String url) {
                Log.d("TAG", "Saving to LOCALY");
                final String fileName = URLUtil.guessFileName(url, null, null);
                ModelFiles.saveImageToFile(imageBmp, fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });

    }

    public void getImage(final String url, final MasterInterface.LoadImageListener listener) {
        final String fileName = URLUtil.guessFileName(url, null, null);
        Log.d("Tag", "the urls is " + url);
        ModelFiles.loadImageFromFileAsync(fileName, new MasterInterface.loadImageFromFileAsyncListener() {
            @Override
            public void onComplete(final Bitmap bitmap) {
                if (bitmap != null) {
                    Log.d("TAG", "IMAGE LOCALY");
                    listener.onSuccess(bitmap);
                } else {
                    Log.d("TAG", "bitmap is null");
                    modelFireBase.getImage(url, new MasterInterface.LoadImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            Log.d("TAG", "found bitmap from server " + image);
                            Log.d("TAG", "found Filename from server " + fileName);
                            ModelFiles.saveImageToFile(image, fileName);
                            listener.onSuccess(image);
                        }

                        @Override
                        public void onFail() {
                            listener.onFail();
                        }
                    });
                }
            }
        });
    }

    public long getIdRandomizer() {
        return System.currentTimeMillis() % 100000;
    }

    public void changeLikeCount(Case aCase, boolean increase) {
        if (increase) {
            aCase.increaseLikeCount();
        } else {
            aCase.decreaseLikeCount();
        }
        updateCase(aCase);
    }

    public class CaseUpdateEvent {
        public final Case aCase;

        public CaseUpdateEvent(Case aCase) {
            this.aCase = aCase;
        }
    }

    public void addUser(User user) {
        modelFireBase.addUser(user);
    }

//    public String getCurrentLoggedUserId() {
//        return modelFireBase.getCurrentLoggedUserId();
//    }

//    public void getUser(String accountId, final MasterInterface.GetUserCallback callback) {
//        modelFireBase.getUser(accountId, callback);
//    }

//    public void registerAccount(RegisterActivity registerActivity, final String email, final String password, final String id, final MasterInterface.RegisterAccountCallBack callBack) {
//        modelFireBase.registerAccount(registerActivity, email, password, id, callBack);
//    }
//
//    public void loginAccount(final LoginActivity loginActivity, final String email, final String password, final MasterInterface.LoginAccountCallBack callBack) {
//        modelFireBase.loginAccount(loginActivity, email, password, callBack);
//    }
}
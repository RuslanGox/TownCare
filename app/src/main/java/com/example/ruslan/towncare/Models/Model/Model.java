package com.example.ruslan.towncare.Models.Model;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Case.CaseFireBase;
import com.example.ruslan.towncare.Models.Case.CaseSql;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.User.User;
import com.example.ruslan.towncare.Models.User.UserFireBase;
import com.example.ruslan.towncare.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    public static Model instance;

    //    private List<Case> caseList;
    private ModelFireBase modelFireBase;
    private ModelSql modelSql;

    public static User CurrentUser;

    private Model(final MasterInterface.GotCurrentUserLogged callback) {
        modelSql = new ModelSql(MyApplication.getMyContext());
        modelFireBase = new ModelFireBase();
        syncAndRegisterCaseData();
        UserFireBase.getUser(UserFireBase.getCurrentLoggedUserId(), new MasterInterface.GetUserCallback() {
            @Override
            public void onComplete(User user) {
                CurrentUser = user;
                callback.Create();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void syncAndRegisterCaseData() {
        SharedPreferences ref = MyApplication.getMyContext().getSharedPreferences("TAG", MODE_PRIVATE);
        final long lastUpdate = ref.getLong("CaseLastUpdate", 0);
        CaseFireBase.syncAndRegisterCaseData(lastUpdate, new MasterInterface.RegisterCasesEvents() {
            @Override
            public void onCaseUpdate(Case aCase) {
                CaseSql.addCase(modelSql.getWritableDatabase(), aCase);
                SharedPreferences.Editor prefEditor = MyApplication.getMyContext().getSharedPreferences("TAG", MODE_PRIVATE).edit();
                prefEditor.putLong("CaseLastUpdate", aCase.getCaseLastUpdateDate()).apply();
                EventBus.getDefault().post(new CaseUpdateEvent(aCase));
            }
        });
    }


    public static boolean getInstance(MasterInterface.GotCurrentUserLogged callback) {
        if (instance == null) {
            instance = new Model(callback);
            return true;
        }
        else{
            return false;
        }
    }

    public class CaseUpdateEvent {
        public final Case aCase;
        public CaseUpdateEvent(Case aCase) {
            this.aCase = aCase;
        }
    }

//    public List<Case> getDataSql() {
//        return CaseSql.getData(modelSql.getReadableDatabase());
//    }

    public void getData(final MasterInterface.GetAllCasesCallback callback) {
        callback.onComplete(CaseSql.getData(modelSql.getReadableDatabase()));
//        modelFireBase.getData(new MasterInterface.GetAllCasesCallback() {
//            @Override
//            public void onComplete(List<Case> list) {
//                for (Case aCase : list) {
//                    CaseSql.addCase(modelSql.getWritableDatabase(), aCase);
//                }
//                callback.onComplete(CaseSql.getData(modelSql.getReadableDatabase()));
//            }
//
//            @Override
//            public void onCancel() {
//                callback.onCancel();
//            }
//        });
//        CaseSql.getData(modelSql.getReadableDatabase());

//        SharedPreferences ref = MyApplication.getMyContext().getSharedPreferences("TAG",MODE_PRIVATE);
//        final long lastUpdate = ref.getLong("CaseLastUpdate",0);
//
//        modelFireBase.getData(lastUpdate, new MasterInterface.GetAllCasesCallback() {
//            @Override
//            public void onComplete(List<Case> list) {

//                long newCaseLastUpdate = lastUpdate;
//                for (Case aCase: list) {
//                    CaseSql.addCase(modelSql.getWritableDatabase(),aCase);
//                    if (aCase.getCaseLastUpdateDate() > newCaseLastUpdate){
//                        newCaseLastUpdate = aCase.getCaseLastUpdateDate();
//                    }
//                }
//                SharedPreferences.Editor prefEditor = MyApplication.getMyContext().getSharedPreferences("TAG",MODE_PRIVATE).edit();
//                prefEditor.putLong("CaseLastUpdate",newCaseLastUpdate).apply();
//                callback.onComplete(CaseSql.getData(modelSql.getReadableDatabase()));
//            }

//            @Override
//            public void onCancel() {
//
//            }
//        });
    }

    public void addCase(Case c) {
        CaseSql.addCase(modelSql.getWritableDatabase(), c);
        modelFireBase.addCase(c);
    }

    public void removeCase(String id, final MasterInterface.GetCaseCallback callback) {
        modelFireBase.removeCase(id, new MasterInterface.GetCaseCallback() {
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
}

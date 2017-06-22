package com.example.ruslan.towncare.Models.Model;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Case.CaseSql;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.User.User;
import com.example.ruslan.towncare.Models.User.UserFireBase;
import com.example.ruslan.towncare.MyApplication;

import java.util.List;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    public final static Model instance = new Model();

//    private List<Case> caseList;
    private ModelFireBase modelFireBase;
    private ModelSql modelSql;

    public User CurrentUser;

    private Model() {
        modelSql = new ModelSql(MyApplication.getMyContext());
        modelFireBase = new ModelFireBase();
        UserFireBase.getUser(UserFireBase.getCurrentUserId(), new MasterInterface.GetUserCallback() {
            @Override
            public void onComplete(User user) {
                CurrentUser = user;
            }

            @Override
            public void onCancel() {

            }
        });

//        for (int i = 0; i < 5; i++) {
//            caseList.add(new Case("" + 1234 + System.currentTimeMillis() + i, "case " + i, "11/12/17", 500 + i * 3, "1", "Open", "052476", "1234","TOWN" ,"GIDON ISRAEL", "THIS IS THE DEST", "url"));
////            CaseSql.addCase(modelSql.getWritableDatabase(),caseList.get(i));
//            modelFireBase.addCase(caseList.get(i));
//        }
    }

    public List<Case> getDataSql() {
        return CaseSql.getData(modelSql.getReadableDatabase());
    }

    public void getData(final MasterInterface.GetAllCasesCallback callback) {
        modelFireBase.getData(new MasterInterface.GetAllCasesCallback() {
            @Override
            public void onComplete(List<Case> list) {
                for (Case aCase : list) {
                    if(aCase.getCaseTown().equals(CurrentUser.getUserTown())){
                        CaseSql.addCase(modelSql.getWritableDatabase(), aCase);
                    }
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
//        final Bitmap[] realBitmap = new Bitmap[1];
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

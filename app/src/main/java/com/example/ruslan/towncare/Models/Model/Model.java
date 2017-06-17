package com.example.ruslan.towncare.Models.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Case.CaseSql;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by omrih on 27-May-17.
 */

public class Model {
    public final static Model instance = new Model();

    private List<Case> caseList;
    private ModelFireBase modelFireBase;
    private ModelSql modelSql;

    private Model() {
        modelSql = new ModelSql(MyApplication.getMyContext());
        modelFireBase = new ModelFireBase();
        caseList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            caseList.add(new Case("" + 1234 + System.currentTimeMillis() + i, "case " + i, "11/12/17", 500 + i * 3, 200 - i * 5, "1", "Open", "052476", "1234", "GIDON ISRAEL", "THIS IS THE DEST", "url"));
//            CaseSql.addCase(modelSql.getWritableDatabase(),caseList.get(i));
            modelFireBase.addCase(caseList.get(i));
        }
    }

    public List<Case> getDataSql() {
        return CaseSql.getData(modelSql.getReadableDatabase());
    }

    public void getData(final MasterInterface.GetAllCasesCallback callback) {
        modelFireBase.getData(new MasterInterface.GetAllCasesCallback() {
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
                saveImageToFile(imageBmp, fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });

    }

    public void getImage(String url, final MasterInterface.LoadImageListener listener) {

        final String fileName = URLUtil.guessFileName(url, null, null);
        final Bitmap bitmap = loadImageFromFile(fileName);
        if (bitmap != null) {
            Log.d("TAG", "IMAGE LOCALY");
            listener.onSuccess(bitmap);
        } else {
            modelFireBase.getImage(url, new MasterInterface.LoadImageListener() {

                @Override
                public void onSuccess(Bitmap image) {
                    saveImageToFile(bitmap, fileName);
                    listener.onSuccess(bitmap);
                }

                @Override
                public void onFail() {
                    listener.onFail();
                }
            });
        }
    }


    private void saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir, imageFileName);
            imageFile.createNewFile();
            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            addPicureToGallery(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPicureToGallery(File imageFile) {
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApplication.getMyContext().sendBroadcast(mediaScanIntent);
    }


    private Bitmap loadImageFromFile(String imageFileName) {
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir, imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag", "got image from cache: " + imageFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

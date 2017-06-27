package com.example.ruslan.towncare.Models.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ruslan on 17/06/2017.
 */

public class ModelFiles {

    public static void saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        Log.d("TAG", "saveIamgeFromFile bitmap " + imageBitmap);
        Log.d("TAG", "saveIamgeFromFile fileName " + imageFileName);
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            boolean mkdirSuccess = true;
            boolean newFileSuccess = true;
            if (!dir.exists()) {
                mkdirSuccess = dir.mkdir();
            }
            if (mkdirSuccess) {
                File imageFile = new File(dir, imageFileName);
                newFileSuccess = imageFile.createNewFile();
                if (newFileSuccess) {
                    OutputStream out = new FileOutputStream(imageFile);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    addPicureToGallery(imageFile);
                    return;
                }
            }
            throw new IOException("mkdirSuccess:" + mkdirSuccess + "newFileSuccess:" + newFileSuccess);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addPicureToGallery(File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApplication.getMyContext().sendBroadcast(mediaScanIntent);
    }


    public static void loadImageFromFileAsync(final String imageFileName, final MasterInterface.loadImageFromFileAsyncListener listener) {
        AsyncTask<String, String, Bitmap> task = new AsyncTask<String, String, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return loadImageFromFile(imageFileName);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                Log.d("TAG", "Got bitmap from onPost + " + bitmap);
                listener.onComplete(bitmap);
            }
        };
        task.execute(imageFileName);
    }

    public static Bitmap loadImageFromFile(String imageFileName) {
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
package com.example.ruslan.towncare.Models.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Case.CaseFireBase;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by omrih on 10-Jun-17.
 */

class ModelFireBase {

//    void getData(long CaseLastUpdate , final MasterInterface.GetAllCasesCallback callback) {
//        CaseFireBase.getData(CaseLastUpdate , callback);
//    }

    boolean addCase(Case aCase) {
        CaseFireBase.addCase(aCase);
        return false;
    }

    boolean updateCase(Case student) {
        CaseFireBase.updateCase(student);
        return false;
    }

    void removeCase(String studentIndex, final MasterInterface.GetCaseCallback callback) {
        CaseFireBase.removeCase(studentIndex, callback);
    }

    void saveImage(Bitmap imageBmp, String name, final MasterInterface.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null) {
                    listener.complete(downloadUrl.toString());
                }else {
                    listener.fail();
                }
            }
        });
    }

    void getImage(String url, final MasterInterface.LoadImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("TAG", "getImage exception \n" + exception.getMessage());
                listener.onFail();
            }
        });
    }
}
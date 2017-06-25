package com.example.ruslan.towncare.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Enums.MessageResult;
import com.example.ruslan.towncare.Models.Enums.UpsertMode;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.Models.Model.ModelFiles;
import com.example.ruslan.towncare.R;

import org.greenrobot.eventbus.EventBus;


public class CaseUpsertFragment extends Fragment {

    private static final String ADMIN_PARAMETER = "Admin";
    private static final String ARG_PARAM1 = "caseID";
    private static final String URL_DEFAULT_PARAMETER = "url";
    public static final String JPEG = ".jpeg";

    private View contentView;
    private String caseId = null;
    private ImageView imageView;
    private Bitmap bitmap;

    public static class MessageEvent {
        public final MessageResult messageResultn;

        public MessageEvent(MessageResult messageResultn) {
            this.messageResultn = messageResultn;
        }
    }

    public CaseUpsertFragment() {
    }

    public static CaseUpsertFragment newInstance(String caseId) {
        CaseUpsertFragment fragment = new CaseUpsertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, caseId);
        fragment.setArguments(args);
        Log.d("TAG", ">>" + caseId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.caseId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        checkCameraPermission();
        contentView = inflater.inflate(R.layout.fragment_case_upsert, container, false);
        if (!caseId.isEmpty()) {
            Log.d("TAG", caseId);
            showCaseData(contentView, Model.instance.getCase(caseId));
        }
        imageView = (ImageView) contentView.findViewById(R.id.upsertCasePic);
        ((TextView) contentView.findViewById(R.id.upsertCaseTown)).setText(Model.CurrentUser.getUserTown());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        Button saveButton = (Button) contentView.findViewById(R.id.upsertCaseSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                (contentView.findViewById(R.id.upsertProgressBar)).setVisibility(View.VISIBLE);
                if (!caseId.isEmpty()) { // Edit old Case
                    upsertImageAndCase(UpsertMode.INSERT_MODE);
                } else { // Save new Case
                    upsertImageAndCase(UpsertMode.EDIT_MODE);
                }
            }
        });
        Button cancelButton = (Button) contentView.findViewById(R.id.upsertCaseCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(MessageResult.CANCEL_BUTTON_PRESSED));
            }
        });
        return contentView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.actionBarPlusButton).setVisible(false);
        if (caseId.isEmpty()) {
            if (getActivity().getActionBar() != null) {
                getActivity().getActionBar().setTitle(R.string.create_case);
                menu.findItem(R.id.actionBarRemoveButton).setVisible(false);
            }
        } else {
            if (getActivity().getActionBar() != null) {
                getActivity().getActionBar().setTitle(R.string.edit_case);
                menu.findItem(R.id.actionBarRemoveButton).setVisible(true);
            }
        }
    }

    private Case createCase() {
        String caseId;
        int caseLikeCount;
        String caseStatus;
        String caseImageUrl;
        if (this.caseId.isEmpty()) { // Insert Mode
            Log.d("TAG", "INSERT MODE");
            caseId = Model.CurrentUser.getUserId() + Model.instance.getIdRandomizer();
            Log.d("TAG", "new caseId is " + caseId);
            caseLikeCount = 1;
            caseStatus = "Open";
            caseImageUrl = URL_DEFAULT_PARAMETER;
        } else {
            caseId = this.caseId;
            caseLikeCount = 1; //Integer.parseInt(((TextView) contentView.findViewById(R.id.case_like_count)).getText().toString());
            caseStatus = ((TextView) contentView.findViewById(R.id.upsertCaseStatus)).getText().toString();
            caseImageUrl = Model.instance.getCase(caseId).getCaseImageUrl();
        }
        String caseTitle = ((EditText) contentView.findViewById(R.id.upsertCaseTitle)).getText().toString();
        String caseDate = ((EditText) contentView.findViewById(R.id.upsertCaseDate)).getText().toString();
        String caseType = Long.toString(((Spinner) contentView.findViewById(R.id.upsertCaseType)).getSelectedItemId());
        String caseAddress = ((EditText) contentView.findViewById(R.id.upsertCaseAddress)).getText().toString();
        String caseDesc = ((EditText) contentView.findViewById(R.id.upsertCaseDesc)).getText().toString();
        return new Case(caseId, caseTitle, caseDate, caseLikeCount, caseType, caseStatus, caseAddress, caseDesc, caseImageUrl);
    }

    private void upsertImageAndCase(final UpsertMode mode) {
        final Case c = createCase();
        if (bitmap != null) {
            Model.instance.saveImage(bitmap, (c.getCaseId() + Model.instance.getIdRandomizer() + JPEG), new MasterInterface.SaveImageListener() {
                @Override
                public void complete(String url) {
                    c.setCaseImageUrl(url);
                    UpsertCase(c, mode);
                    (contentView.findViewById(R.id.upsertProgressBar)).setVisibility(View.GONE);
                }

                @Override
                public void fail() {
                    c.setCaseImageUrl(URL_DEFAULT_PARAMETER);
                    UpsertCase(c, mode);
                }
            });
        } else {
            UpsertCase(c, mode);
        }
    }



    private void UpsertCase(Case c, final UpsertMode mode) {
        switch (mode) {
            case INSERT_MODE:
                Model.instance.addCase(c);
                EventBus.getDefault().post(new MessageEvent(MessageResult.SAVE_BUTTON_PRESSED));
                break;
            case EDIT_MODE:
                Model.instance.updateCase(c);
                EventBus.getDefault().post(new MessageEvent(MessageResult.UPDATE_BUTTON_PRESSED));
                break;
        }
    }


    private void showCaseData(View contentView, Case aCase) {
        ((EditText) contentView.findViewById(R.id.upsertCaseTitle)).setText(aCase.getCaseTitle());
        if (aCase.getCaseImageUrl() != null && !aCase.getCaseImageUrl().equalsIgnoreCase(URL_DEFAULT_PARAMETER)) {
            ((ImageView) contentView.findViewById(R.id.upsertCasePic)).setImageBitmap(ModelFiles.loadImageFromFile(URLUtil.guessFileName(aCase.getCaseImageUrl(), null, null)));
        } else {
            ((ImageView) contentView.findViewById(R.id.upsertCasePic)).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sym_def_app_icon));
        }
        ((EditText) contentView.findViewById(R.id.upsertCaseDate)).setText(aCase.getCaseDate());
        ((TextView) contentView.findViewById(R.id.upsertCaseTown)).setText(aCase.getCaseTown());
        ((EditText) contentView.findViewById(R.id.upsertCaseAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.upsertCaseStatus)).setText(aCase.getCaseStatus());
        ((Spinner) contentView.findViewById(R.id.upsertCaseType)).setSelection(Integer.parseInt(aCase.getCaseType()));
        ((EditText) contentView.findViewById(R.id.upsertCaseDesc)).setText(aCase.getCaseDesc());
        if (Model.CurrentUser.getUserRole().equals(ADMIN_PARAMETER) || Model.CurrentUser.getUserId().equals(aCase.getCaseOpenerId())) {
            ((TextView) contentView.findViewById(R.id.upsertCaseOpenerId)).setText(aCase.getCaseOpenerId());
            ((TextView) contentView.findViewById(R.id.upsertCaseOpenerPhone)).setText(aCase.getCaseOpenerPhone());
        } else {
            contentView.findViewById(R.id.upsertCaseOpenerId).setVisibility(View.INVISIBLE);
            contentView.findViewById(R.id.upsertCaseOpenerPhone).setVisibility(View.INVISIBLE);
        }
    }

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == android.app.Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void checkCameraPermission() {
        boolean hasPermission2 = (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission2) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA}, 1);
        }
    }
}

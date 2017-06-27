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
    private static final String URL_DEFAULT_PARAMETER = "url";
    private static final String ARG_PARAM1 = "caseID";
    private static final String ARG_PARAM2 = "upsertMode";
    private static final String JPEG = ".jpeg";
    private static final String ERROR = "ERROR";

    private View contentView;
    private String caseId = null;
    private UpsertMode upsertMode;
    private ImageView imageView;
    private Bitmap bitmap;

    public static class MessageEvent {
        public final MessageResult messageResult;

        public MessageEvent(MessageResult messageResult) {
            this.messageResult = messageResult;
        }
    }

    public CaseUpsertFragment() {
    }

    public static CaseUpsertFragment newInstance(String caseId, UpsertMode upsertMode) {
        CaseUpsertFragment fragment = new CaseUpsertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, caseId);
        args.putSerializable(ARG_PARAM2, upsertMode);
        fragment.setArguments(args);
        Log.d("TAG", "CaseUpsertFragment -> caseId -> " + caseId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.caseId = getArguments().getString(ARG_PARAM1);
            this.upsertMode = (UpsertMode) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        checkCameraPermission();
        contentView = inflater.inflate(R.layout.fragment_case_upsert, container, false);
        imageView = (ImageView) contentView.findViewById(R.id.caseUpsertImage);
        ((TextView) contentView.findViewById(R.id.caseUpsertTown)).setText(Model.CurrentUser.getUserTown());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePicture();
            }
        });
        switch (upsertMode) {
            case EDIT_MODE:
                showCaseData(contentView, Model.instance.getCase(caseId));
            case INSERT_MODE:
                // for future use
        }
        Button saveButton = (Button) contentView.findViewById(R.id.caseUpsertSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                (contentView.findViewById(R.id.caseListProgressBar)).setVisibility(View.VISIBLE);
                upsertImageAndCase();
            }
        });
        Button cancelButton = (Button) contentView.findViewById(R.id.caseUpsertCancelButton);
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
        switch (upsertMode) {
            case INSERT_MODE:
                if (getActivity().getActionBar() != null) {
                    getActivity().getActionBar().setTitle(R.string.create_case);
                    menu.findItem(R.id.actionBarRemoveButton).setVisible(false);
                }
                break;
            case EDIT_MODE:
                if (getActivity().getActionBar() != null) {
                    getActivity().getActionBar().setTitle(R.string.edit_case);
                    menu.findItem(R.id.actionBarRemoveButton).setVisible(true);
                }
                break;
        }
    }

    private Case createCase() {
        String caseId;
        int caseLikeCount;
        String caseStatus;
        String caseImageUrl;
        switch (upsertMode) {
            case INSERT_MODE:
                caseId = Model.CurrentUser.getUserId() + Model.instance.getIdRandomizer();
                caseLikeCount = 1;
                caseImageUrl = URL_DEFAULT_PARAMETER;
                caseStatus = Long.toString(((Spinner) contentView.findViewById(R.id.caseUpsertType)).getSelectedItemId());
                break;
            case EDIT_MODE:
                caseId = this.caseId;
                caseLikeCount = Integer.parseInt(((TextView) contentView.findViewById(R.id.caseListLikeCount)).getText().toString());
                caseStatus = Long.toString(((Spinner) contentView.findViewById(R.id.caseUpsertType)).getSelectedItemId());
                caseImageUrl = Model.instance.getCase(caseId).getCaseImageUrl();
                break;
            default:
                caseId = ERROR;
                caseLikeCount = -999;
                caseStatus = ERROR;
                caseImageUrl = ERROR;
        }
        String caseTitle = ((EditText) contentView.findViewById(R.id.caseUpsertTitle)).getText().toString();
        String caseDate = ((EditText) contentView.findViewById(R.id.caseUpsertDate)).getText().toString();
        String caseType = Long.toString(((Spinner) contentView.findViewById(R.id.caseUpsertType)).getSelectedItemId());
        String caseAddress = ((EditText) contentView.findViewById(R.id.caseUpsertAddress)).getText().toString();
        String caseDesc = ((EditText) contentView.findViewById(R.id.caseUpsertDesc)).getText().toString();
        return new Case(caseId, caseTitle, caseDate, caseLikeCount, caseType, caseStatus, caseAddress, caseDesc, caseImageUrl);
    }

    private void upsertImageAndCase() {
        final Case c = createCase();
        if (bitmap != null) {
            Model.instance.saveImage(bitmap, (c.getCaseId() + Model.instance.getIdRandomizer() + JPEG), new MasterInterface.SaveImageListener() {
                @Override
                public void complete(String url) {
                    c.setCaseImageUrl(url);
                    UpsertCase(c, upsertMode);
                    (contentView.findViewById(R.id.caseListProgressBar)).setVisibility(View.GONE);
                }

                @Override
                public void fail() {
                    c.setCaseImageUrl(URL_DEFAULT_PARAMETER);
                    UpsertCase(c, upsertMode);
                }
            });
        } else {
            UpsertCase(c, upsertMode);
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
        ((EditText) contentView.findViewById(R.id.caseUpsertTitle)).setText(aCase.getCaseTitle());
        if (aCase.getCaseImageUrl() != null && !aCase.getCaseImageUrl().equalsIgnoreCase(URL_DEFAULT_PARAMETER)) {
            ((ImageView) contentView.findViewById(R.id.caseUpsertImage)).setImageBitmap(ModelFiles.loadImageFromFile(URLUtil.guessFileName(aCase.getCaseImageUrl(), null, null)));
        } else {
            ((ImageView) contentView.findViewById(R.id.caseUpsertImage)).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sym_def_app_icon));
        }
        ((EditText) contentView.findViewById(R.id.caseUpsertDate)).setText(aCase.getCaseDate());
        ((TextView) contentView.findViewById(R.id.caseUpsertTown)).setText(aCase.getCaseTown());
        ((EditText) contentView.findViewById(R.id.caseUpsertAddress)).setText(aCase.getCaseAddress());
        ((Spinner) contentView.findViewById(R.id.caseUpsertType)).setSelection(Integer.parseInt(aCase.getCaseType()));
        ((EditText) contentView.findViewById(R.id.caseUpsertDesc)).setText(aCase.getCaseDesc());

        // only admin allowt to change status of the case
        if (Model.CurrentUser.getUserRole().equals(ADMIN_PARAMETER)) {
            ((Spinner) contentView.findViewById(R.id.caseUpsertStatus)).setClickable(true);
            ((Spinner) contentView.findViewById(R.id.caseUpsertStatus)).setSelection(Integer.parseInt(aCase.getCaseStatus()));
            ((TextView) contentView.findViewById(R.id.caseUpsertOpenerId)).setText(aCase.getCaseOpenerId());
            ((TextView) contentView.findViewById(R.id.caseUpsertOpenerPhone)).setText(aCase.getCaseOpenerPhone());
            contentView.findViewById(R.id.caseUpsertOpenerId).setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.caseUpsertOpenerPhone).setVisibility(View.VISIBLE);
        }
    }

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePicture() {
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
package com.example.ruslan.towncare.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
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
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.Models.Model.ModelFiles;
import com.example.ruslan.towncare.R;

import static android.app.Activity.RESULT_OK;


public class CaseUpsertFragment extends Fragment {


    private static final String ARG_PARAM1 = "caseID";
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private String caseId = null;
    private ImageView imageView;
    private Bitmap bitmap;

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
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        checkSDPermission();
        contentView = inflater.inflate(R.layout.fragment_case_upsert, container, false);
        if (!caseId.isEmpty()) {
            Log.d("TAG", caseId);
            showCaseData(contentView, Model.instance.getCase(caseId));
        }
        imageView = (ImageView) contentView.findViewById(R.id.upsertCasePic);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        Button saveButton = (Button) contentView.findViewById(R.id.upsertCaseSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                (contentView.findViewById(R.id.upsertProgressBar)).setVisibility(View.VISIBLE);
                if (!caseId.isEmpty()) { // Edit old Case
//                    Model.instance.updateCase(updateCase());
//                    mListener.onClick(v, true);
                    final Case c = updateCase();
                    if (bitmap != null) {
                        Model.instance.saveImage(bitmap, (c.getCaseId() + System.currentTimeMillis() + ".jpeg"), new MasterInterface.SaveImageListener() {
                            @Override
                            public void complete(String url) {
                                c.setCaseImageUrl(url);
                                Model.instance.updateCase(c);
                                mListener.onClick(v, true);
                                (contentView.findViewById(R.id.upsertProgressBar)).setVisibility(View.GONE);
                            }

                            @Override
                            public void fail() {
                                c.setCaseImageUrl("Error Upload Foto");
                                Model.instance.updateCase(c);
                                mListener.onClick(v, true);
                            }
                        });
                    } else {
                        Model.instance.updateCase(c);
                        mListener.onClick(v, true);
                    }
                } else { // Save new Case
                    final Case c = newCase();
                    if (bitmap != null) {
                        Model.instance.saveImage(bitmap, (c.getCaseId() + System.currentTimeMillis() + ".jpeg"), new MasterInterface.SaveImageListener() {
                            @Override
                            public void complete(String url) {
                                c.setCaseImageUrl(url);
                                Model.instance.addCase(c);
                                mListener.onClick(v, false);
                                (contentView.findViewById(R.id.upsertProgressBar)).setVisibility(View.GONE);
                            }

                            @Override
                            public void fail() {
                                c.setCaseImageUrl("Error Upload Foto");
                                Model.instance.addCase(c);
                                mListener.onClick(v, false);
                            }
                        });
                    } else {
                        Model.instance.addCase(c);
                        mListener.onClick(v, false);
                    }
                }

            }
        });
        Button cancelButton = (Button) contentView.findViewById(R.id.upsertCaseCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v, false);
            }
        });
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onClick(View view, boolean dataChanged);
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

    private Case updateCase() {
        String id = this.caseId;
        String caseTitle = ((EditText) contentView.findViewById(R.id.upsertCaseTitle)).getText().toString();
        String caseDate = ((EditText) contentView.findViewById(R.id.upsertCaseDate)).getText().toString();
        int caseLikeCount = 1;
        int caseUnLikeCount = 0;
        String caseType = Long.toString(((Spinner) contentView.findViewById(R.id.upsertCaseType)).getSelectedItemId());
        String caseStatus = "Open"; //alwasys
        String caseOpenerPhone = "phone from data base";
        String caseOpener = ((EditText) contentView.findViewById(R.id.upsertCaseOpenerId)).getText().toString();
        String caseAddress = ((EditText) contentView.findViewById(R.id.upsertCaseAddress)).getText().toString();
        String caseDesc = ((EditText) contentView.findViewById(R.id.upsertCaseDesc)).getText().toString();

        return new Case(id, caseTitle, caseDate, caseLikeCount, caseUnLikeCount, caseType, caseStatus, caseOpenerPhone, caseOpener, caseAddress, caseDesc, "url");
    }


    private Case newCase() {
        String id = ((EditText) contentView.findViewById(R.id.upsertCaseOpenerId)).getText().toString() + System.currentTimeMillis();
        String caseTitle = ((EditText) contentView.findViewById(R.id.upsertCaseTitle)).getText().toString();
        String caseDate = ((EditText) contentView.findViewById(R.id.upsertCaseDate)).getText().toString();
        int caseLikeCount = 1;
        int caseUnLikeCount = 0;
        String caseType = Long.toString(((Spinner) contentView.findViewById(R.id.upsertCaseType)).getSelectedItemId());
        String caseStatus = "Open"; //alwasys
        String caseOpenerPhone = "phone from data base";
        String caseOpener = ((EditText) contentView.findViewById(R.id.upsertCaseOpenerId)).getText().toString();
        String caseAddress = ((EditText) contentView.findViewById(R.id.upsertCaseAddress)).getText().toString();
        String caseDesc = ((EditText) contentView.findViewById(R.id.upsertCaseDesc)).getText().toString();

        return new Case(id, caseTitle, caseDate, caseLikeCount, caseUnLikeCount, caseType, caseStatus, caseOpenerPhone, caseOpener, caseAddress, caseDesc, "url");
    }

    private void showCaseData(View contentView, Case aCase) {
        ((EditText) contentView.findViewById(R.id.upsertCaseTitle)).setText(aCase.getCaseTitle());
//        ((ImageButton)contentView.findViewById(R.id.createCasePic)).set(aCase.getCaseImageUrl());
        if (aCase.getCaseImageUrl() != null && !aCase.getCaseImageUrl().equalsIgnoreCase("url")) {

            ((ImageView) contentView.findViewById(R.id.upsertCasePic)).setImageBitmap(ModelFiles.loadImageFromFile(URLUtil.guessFileName(aCase.getCaseImageUrl(), null, null)));
        } else {
            ((ImageView) contentView.findViewById(R.id.upsertCasePic)).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sym_def_app_icon));
        }
        ((EditText) contentView.findViewById(R.id.upsertCaseDate)).setText(aCase.getCaseDate());
        ((EditText) contentView.findViewById(R.id.upsertCaseAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.upsertCaseStatus)).setText(aCase.getCaseStatus());
        ((Spinner) contentView.findViewById(R.id.upsertCaseType)).setSelection(Integer.parseInt(aCase.getCaseType()));
        ((EditText) contentView.findViewById(R.id.upsertCaseDesc)).setText(aCase.getCaseDesc());
        ((EditText) contentView.findViewById(R.id.upsertCaseOpenerId)).setText("" + aCase.getCaseOpener());
        ((EditText) contentView.findViewById(R.id.upsertCaseOpenerPhone)).setText(aCase.getCaseOpenerPhone());
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        }
    }


    private void checkSDPermission() {
        boolean hasPermission2 = (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission2) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA}, 1);
        }
    }
}

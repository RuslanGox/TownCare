package com.example.ruslan.towncare.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ruslan.towncare.Model.Case;
import com.example.ruslan.towncare.Model.Model;
import com.example.ruslan.towncare.R;


public class CaseUpsertFragment extends Fragment {


    private static final String ARG_PARAM1 = "caseID";
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private String caseId = null;

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
        contentView = inflater.inflate(R.layout.fragment_case_upsert, container, false);
        if (!caseId.isEmpty()) {
            Log.d("TAG", caseId);
            showCaseData(contentView,Model.instance.getCase(caseId));
        }
        Button saveButton = (Button) contentView.findViewById(R.id.upsertCaseSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!caseId.isEmpty()) {
                    Model.instance.updateCase(updateCase());
                    mListener.onClick(v,true);
                } else {
                    Model.instance.addCase(newCase());
                    mListener.onClick(v,false);
                }

            }
        });
        Button cancelButton = (Button) contentView.findViewById(R.id.upsertCaseCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v,false);
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


    private Case updateCase(){
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

        return new Case(id, caseTitle, caseDate, caseLikeCount, caseUnLikeCount, caseType, caseStatus, caseOpenerPhone, caseOpener, caseAddress, caseDesc, "img url");
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

        return new Case(id, caseTitle, caseDate, caseLikeCount, caseUnLikeCount, caseType, caseStatus, caseOpenerPhone, caseOpener, caseAddress, caseDesc, "img url");
    }

    private void showCaseData(View contentView, Case aCase) {
        ((EditText) contentView.findViewById(R.id.upsertCaseTitle)).setText(aCase.getCaseTitle());
//        ((ImageButton)contentView.findViewById(R.id.createCasePic)).set(aCase.getCaseImageUrl());
        ((EditText) contentView.findViewById(R.id.upsertCaseDate)).setText(aCase.getCaseDate().toString());
        ((EditText) contentView.findViewById(R.id.upsertCaseAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.upsertCaseStatus)).setText(aCase.getCaseStatus());
        ((Spinner) contentView.findViewById(R.id.upsertCaseType)).setSelection(Integer.parseInt(aCase.getCaseType()));
        ((EditText) contentView.findViewById(R.id.upsertCaseDesc)).setText(aCase.getCaseDesc());
        ((EditText) contentView.findViewById(R.id.upsertCaseOpenerId)).setText("" + aCase.getCaseOpener());
        ((EditText) contentView.findViewById(R.id.upsertCaseOpenerPhone)).setText(aCase.getCaseOpenerPhone());
    }
}

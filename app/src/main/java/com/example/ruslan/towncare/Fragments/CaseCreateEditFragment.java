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


public class CaseCreateEditFragment extends Fragment {


    private static final String ARG_PARAM1 = "caseID";
    private OnFragmentInteractionListener mListener;
    private View contentView;
    private String caseId = null;
    public CaseCreateEditFragment() {
    }


    public static CaseCreateEditFragment newInstance(String caseId) {
        CaseCreateEditFragment fragment = new CaseCreateEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, caseId);
        fragment.setArguments(args);
        Log.d("TAG",">>" + caseId);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        contentView = inflater.inflate(R.layout.fragment_case_create, container, false);
        if (caseId != ""){
            Log.d("TAG", caseId);
            showCaseData(contentView);
        }
        Button saveButton = (Button) contentView.findViewById(R.id.createCaseSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caseId != ""){
                    Model.instance.updateCase(newCase());
                }
                else{
                    Model.instance.addCase(newCase());
                }
                mListener.onClick(v);
            }
        });
        Button cancelButton = (Button) contentView.findViewById(R.id.createCaseCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v);
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
        void onClick(View view);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.actionBarCreatePlusButton).setVisible(false);
        if (caseId == ""){
            if (getActivity().getActionBar() != null) {
                getActivity().getActionBar().setTitle(R.string.create_case);
            }
        }
        else{
            if (getActivity().getActionBar() != null) {
                getActivity().getActionBar().setTitle(R.string.edit_case);
            }
        }


    }


    private Case newCase() {
        // what should be the ID ?
//        String id = ?
        String caseTitle = ((EditText) contentView.findViewById(R.id.createCaseTitle)).getText().toString();
        String caseDate = ((EditText) contentView.findViewById(R.id.createCaseDate)).getText().toString();
        int caseLikeCount = 1;
        int caseUnLikeCount = 0;
        String caseType = Long.toString(((Spinner)contentView.findViewById(R.id.createCaseType)).getSelectedItemId());
        String caseStatus = "Open"; //alwasys
        String caseOpenerPhone = "phone from data base";
        String caseOpener = "id from data base";
        String caseAddress = ((EditText) contentView.findViewById(R.id.createCaseAddress)).getText().toString();
        String caseDesc = ((EditText) contentView.findViewById(R.id.createCaseDesc)).getText().toString();

        return new Case(6, caseTitle, caseDate, caseLikeCount, caseUnLikeCount, caseType, caseStatus, caseOpenerPhone, 312721970, caseAddress, caseDesc, "img url");
    }

    private void showCaseData(View contentView) {
        Case aCase = Model.instance.getCase(Integer.parseInt(caseId));
        ((EditText) contentView.findViewById(R.id.createCaseTitle)).setText(aCase.getCaseTitle());
//        ((ImageButton)contentView.findViewById(R.id.createCasePic)).set(aCase.getCaseImageUrl());
        ((EditText) contentView.findViewById(R.id.createCaseDate)).setText(aCase.getCaseDate());
        ((EditText) contentView.findViewById(R.id.createCaseAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.createCaseStatus)).setText(aCase.getCaseStatus());
        ((Spinner) contentView.findViewById(R.id.createCaseType)).setSelection(Integer.parseInt(aCase.getCaseType()));
        ((EditText) contentView.findViewById(R.id.createCaseDesc)).setText(aCase.getCaseDesc());
        ((EditText) contentView.findViewById(R.id.createCaseOpener)).setText("" + aCase.getCaseOpener());
        ((EditText) contentView.findViewById(R.id.createCaseOpenerPhone)).setText(aCase.getCaseOpenerPhone());
    }
}

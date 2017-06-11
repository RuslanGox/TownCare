package com.example.ruslan.towncare.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ruslan.towncare.Model.Case;
import com.example.ruslan.towncare.Model.Model;
import com.example.ruslan.towncare.R;

import org.w3c.dom.Text;


public class CaseUpdateFragment extends Fragment {

    private static final String ARG_PARAM1 = "CASE_ID_OF_LINKED_LIST";
    private String id;


    private OnFragmentInteractionListener mListener;

    public CaseUpdateFragment() {
        // Required empty public constructor
    }


    public static CaseUpdateFragment newInstance(String id) {
        CaseUpdateFragment fragment = new CaseUpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View contentView = inflater.inflate(R.layout.fragment_case_create, container, false);
        showCaseData(contentView);

        Button saveButton = (Button) contentView.findViewById(R.id.createCaseSaveButton);
        Button cancelButton = (Button) contentView.findViewById(R.id.createCaseCancelButton);
        Log.d("TAG","UPDATER!");
        return contentView;

        //return inflater.inflate(R.layout.fragment_case_update, container, false);
    }

    private void showCaseData(View contentView) {
        Case aCase = Model.instance.getCase(Integer.parseInt(id));
        ((EditText) contentView.findViewById(R.id.createCaseTitle)).setText(aCase.getCaseTitle());
//        ((ImageButton)contentView.findViewById(R.id.createCasePic)).set(aCase.getCaseImageUrl());
        ((EditText) contentView.findViewById(R.id.createCaseDate)).setText(aCase.getCaseDate());
        ((EditText) contentView.findViewById(R.id.createCaseAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.createCaseStatus)).setText(aCase.getCaseStatus());
        ((Spinner) contentView.findViewById(R.id.createCaseType)).setSelection(1);
        ((EditText) contentView.findViewById(R.id.createCaseDesc)).setText(aCase.getCaseDesc());
        ((EditText) contentView.findViewById(R.id.createCaseOpener)).setText("" + aCase.getCaseOpener());
        ((EditText) contentView.findViewById(R.id.createCaseOpenerPhone)).setText(aCase.getCaseOpenerPhone());
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
}

package com.example.ruslan.towncare.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruslan.towncare.Model.Case;
import com.example.ruslan.towncare.Model.Model;
import com.example.ruslan.towncare.R;


public class CaseDetails extends Fragment {
    public static final String ARG_PARAM1 = "CASE_ID_OF_LINKED_LIST";
    private String id;

    private OnFragmentInteractionListener mListener;

    public CaseDetails() {
    }

    public static CaseDetails newInstance(String id) {
        CaseDetails fragment = new CaseDetails();
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

        View contentView = inflater.inflate(R.layout.fragment_case_details, container, false);
        showCaseData(contentView);
        return contentView;
    }

    private void showCaseData(View contentView) {
        Case aCase = Model.instance.getCase(Integer.parseInt(id));
        ((TextView) contentView.findViewById(R.id.detailsCaseTitle)).setText(aCase.getCaseTitle());
//        ((ImageButton)contentView.findViewById(R.id.detailsCasePic)).set(aCase.getCaseImageUrl());
        ((TextView) contentView.findViewById(R.id.detailsCaseDate)).setText(aCase.getCaseDate());
        ((TextView) contentView.findViewById(R.id.detailsCaseAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.detailsCaseStatus)).setText(aCase.getCaseStatus());
        ((TextView) contentView.findViewById(R.id.detailsCaseType)).setText(aCase.getCaseType());
        ((TextView) contentView.findViewById(R.id.detailsCaseDesc)).setText(aCase.getCaseDesc());
        ((TextView) contentView.findViewById(R.id.detailsCaseOpener)).setText("" + aCase.getCaseOpener());
        ((TextView) contentView.findViewById(R.id.detailsCaseOpenerPhone)).setText(aCase.getCaseOpenerPhone());
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

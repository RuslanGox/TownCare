package com.example.ruslan.towncare.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ruslan.towncare.Model.Case;
import com.example.ruslan.towncare.Model.Model;
import com.example.ruslan.towncare.R;


public class CaseCreateFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private View contentView;

    public CaseCreateFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        contentView = inflater.inflate(R.layout.fragment_case_create, container, false);

        Button saveButton = (Button) contentView.findViewById(R.id.createCaseSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.addCase(newCase());
                mListener.onClick(v);
            }
        });
        Button cancelButton = (Button) contentView.findViewById(R.id.createCaseCancelButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
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
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(R.string.create_case);
        }
    }

    private Case newCase() {
        // what should be the ID ?
//        String id = ?
        String caseTitle = ((EditText) contentView.findViewById(R.id.createCaseTitle)).getText().toString();
        String caseDate = ((EditText) contentView.findViewById(R.id.createCaseDate)).getText().toString();
        int caseLikeCount = 1;
        int caseUnLikeCount = 0;
        String caseType = "its a spinner";
        String caseStatus = "Open"; //alwasys
        String caseOpenerPhone = "phone from data base";
        String caseOpener = "id from data base";
        String caseAddress = ((EditText) contentView.findViewById(R.id.createCaseAddress)).getText().toString();
        String caseDesc = ((EditText) contentView.findViewById(R.id.createCaseDesc)).getText().toString();

        return new Case(6, caseTitle, caseDate, caseLikeCount, caseUnLikeCount, caseType, caseStatus, caseOpenerPhone, 312721970, caseAddress, caseDesc, "img url");
    }
}

package com.example.ruslan.towncare.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.Models.Model.ModelFiles;
import com.example.ruslan.towncare.R;


public class CaseDetailsFragment extends Fragment {
    public static final String ARG_PARAM1 = "CASE_ID_OF_LINKED_LIST";
    private String id;

    public CaseDetailsFragment() {
    }

    public static CaseDetailsFragment newInstance(String id) {
        CaseDetailsFragment fragment = new CaseDetailsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View contentView = inflater.inflate(R.layout.fragment_case_details, container, false);
        showCaseData(contentView, Model.instance.getCase(id));

        return contentView;
    }

    private void showCaseData(View contentView, Case aCase) {
        ((TextView) contentView.findViewById(R.id.detailsCaseTitle)).setText(aCase.getCaseTitle());
        if (aCase.getCaseImageUrl() != null && !aCase.getCaseImageUrl().equalsIgnoreCase("url")) {
            ((ImageView) contentView.findViewById(R.id.detailsCasePic)).setImageBitmap(ModelFiles.loadImageFromFile(URLUtil.guessFileName(aCase.getCaseImageUrl(), null, null)));
        } else {
            ((ImageView) contentView.findViewById(R.id.detailsCasePic)).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sym_def_app_icon));
        }
        ((TextView) contentView.findViewById(R.id.detailsCaseDate)).setText(aCase.getCaseDate());
        ((TextView) contentView.findViewById(R.id.detailsCaseTown)).setText(aCase.getCaseTown());
        ((TextView) contentView.findViewById(R.id.detailsCaseAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.detailsCaseStatus)).setText(aCase.getCaseStatus());
        ((TextView) contentView.findViewById(R.id.detailsCaseType)).setText((getResources().getStringArray(R.array.caseTypes))[Integer.parseInt(aCase.getCaseType())]);
        ((TextView) contentView.findViewById(R.id.detailsCaseDesc)).setText(aCase.getCaseDesc());
        if(Model.instance.CurrentUser.getUserRole().equals("Admin") || Model.instance.CurrentUser.getUserId().equals(aCase.getCaseOpenerId())){
            ((TextView) contentView.findViewById(R.id.detailsCaseOpenerId)).setText(aCase.getCaseOpenerId());
            ((TextView) contentView.findViewById(R.id.detailsCaseOpenerPhone)).setText(aCase.getCaseOpenerPhone());
        }
        else{
            contentView.findViewById(R.id.detailsCaseOpenerId).setVisibility(View.INVISIBLE);
            contentView.findViewById(R.id.detailsCaseOpenerPhone).setVisibility(View.INVISIBLE);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.actionBarPlusButton).setVisible(false);
        menu.findItem(R.id.actionBarEditButton).setVisible(true);
        menu.findItem(R.id.actionBarRemoveButton).setVisible(false);
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("Case Details");
        }
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

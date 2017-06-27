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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.Enums.MessageResult;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.Models.Model.ModelFiles;
import com.example.ruslan.towncare.R;

import org.greenrobot.eventbus.EventBus;

public class CaseDetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "CASE_ID_OF_LINKED_LIST";
    private static final String ADMIN_PARAMETER = "Admin";
    private static final String URL_DEFAULT_PARAMETER = "url";
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

        if (!Model.CurrentUser.getUserRole().equals(ADMIN_PARAMETER)){
            ImageButton likeButton = ((ImageButton) contentView.findViewById(R.id.caseDetailsLikeButton));
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.instance.changeLikeCount(Model.instance.getCase(id), true);
                    EventBus.getDefault().post(new CaseUpsertFragment.MessageEvent(MessageResult.LIKE_BUTTON_PRESSED));
                }
            });
            ImageButton unLikeButton = ((ImageButton) contentView.findViewById(R.id.caseDetailsUnLikeButton));
            unLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.instance.changeLikeCount(Model.instance.getCase(id), false);
                    EventBus.getDefault().post(new CaseUpsertFragment.MessageEvent(MessageResult.UNLIKE_BUTTON_PRESSED));
                }
            });
        }

        return contentView;
    }

    private void showCaseData(View contentView, Case aCase) {
        ((TextView) contentView.findViewById(R.id.caseDetailsTitle)).setText(aCase.getCaseTitle());

        // show image if available else show default image
        if (aCase.getCaseImageUrl() != null && !aCase.getCaseImageUrl().equalsIgnoreCase(URL_DEFAULT_PARAMETER)) {
            ((ImageView) contentView.findViewById(R.id.caseDetailsPic)).setImageBitmap(ModelFiles.loadImageFromFile(URLUtil.guessFileName(aCase.getCaseImageUrl(), null, null)));
        } else {
            ((ImageView) contentView.findViewById(R.id.caseDetailsPic)).setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sym_def_app_icon));
        }

        ((TextView) contentView.findViewById(R.id.caseDetailsDate)).setText(aCase.getCaseDate());
        ((TextView) contentView.findViewById(R.id.caseDetailsTown)).setText(aCase.getCaseTown());
        ((TextView) contentView.findViewById(R.id.caseDetailsAddress)).setText(aCase.getCaseAddress());
        ((TextView) contentView.findViewById(R.id.caseDetailsStatus)).setText(aCase.getCaseStatus());
        ((TextView) contentView.findViewById(R.id.caseDetailsType)).setText((getResources().getStringArray(R.array.caseTypes))[Integer.parseInt(aCase.getCaseType())]);
        ((TextView) contentView.findViewById(R.id.caseDetailsDesc)).setText(aCase.getCaseDesc());

        // private info available only to admin or opener user
        if (Model.CurrentUser.getUserRole().equals(ADMIN_PARAMETER) || Model.CurrentUser.getUserId().equals(aCase.getCaseOpenerId())) {
            ((TextView) contentView.findViewById(R.id.caseDetailsOpenerId)).setText(aCase.getCaseOpenerId());
            ((TextView) contentView.findViewById(R.id.caseDetailsOpenerPhone)).setText(aCase.getCaseOpenerPhone());
        } else {
            contentView.findViewById(R.id.caseDetailsOpenerId).setVisibility(View.INVISIBLE);
            contentView.findViewById(R.id.caseDetailsOpenerPhone).setVisibility(View.INVISIBLE);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.actionBarPlusButton).setVisible(false);
        if (Model.CurrentUser.getUserRole().equals(ADMIN_PARAMETER) || Model.CurrentUser.getUserId().equals(Model.instance.getCase(this.id).getCaseOpenerId())) {
            menu.findItem(R.id.actionBarEditButton).setVisible(true);
        }
        menu.findItem(R.id.actionBarRemoveButton).setVisible(false);
        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle(R.string.CaseDetails);
        }
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

package com.example.ruslan.towncare.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.MyApplication;
import com.example.ruslan.towncare.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;


public class CaseListFragment extends Fragment {

    private static final String URL_DEFAULT_PARAMETER = "url";
    private static final String ADMIN_PARAMETER = "Admin";

    private MasterInterface.CaseListInteractionListener mListener;
    private List<Case> caseListData = new LinkedList<>();
    CaseListAdapter adapter;
    Toast toast;

    public CaseListFragment() {
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checkSDPermission();
        EventBus.getDefault().register(this);
        final View contentView = inflater.inflate(R.layout.fragment_case_list, container, false);

        ListView list = (ListView) contentView.findViewById(R.id.caseListFragment);
        adapter = new CaseListAdapter();
        list.setAdapter(adapter);

        if (Model.instance == null) {
            (contentView.findViewById(R.id.caseListProgressBar)).setVisibility(View.VISIBLE);
            Model.getInstance(new MasterInterface.GotCurrentUserLogged() {
                @Override
                public void Create() {
                    (contentView.findViewById(R.id.caseListProgressBar)).setVisibility(View.GONE);
                    GetData();
                    setHasOptionsMenu(true);
                }
            });
        } else {
            GetData();
            setHasOptionsMenu(true);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "Case was clicked -> " + id);
                mListener.onItemListClick("" + id);
            }
        });
        return contentView;
    }

    private void GetData() {
        Model.instance.getData(new MasterInterface.GetAllCasesCallback() {
            @Override
            public void onComplete(List<Case> list) {
                Log.d("TAG", "data loaded from Sql DB -> " + list.size());
                caseListData = list;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MasterInterface.CaseListInteractionListener) {
            mListener = (MasterInterface.CaseListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CaseListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        if (toast != null) {
            toast.cancel(); // will cancel any ongoing Toast messages
        }
        super.onPause();
    }

    private class CaseListAdapter extends BaseAdapter {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return caseListData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(caseListData.get(position).getCaseId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.case_list_row, null);
            }

            final Case c = caseListData.get(position);
            ((TextView) convertView.findViewById(R.id.caseListTitle)).setText(c.getCaseTitle());
            ((TextView) convertView.findViewById(R.id.caseListDate)).setText(c.getCaseDate());
            ((TextView) convertView.findViewById(R.id.caseListStatus)).setText((getResources().getStringArray(R.array.caseStatus))[Integer.parseInt(c.getCaseStatus())]);
            ((TextView) convertView.findViewById(R.id.caseListLikeCount)).setText(String.valueOf(c.getCaseLikeCount()));
            ((TextView) convertView.findViewById(R.id.caseListType)).setText((getResources().getStringArray(R.array.caseTypes))[Integer.parseInt(c.getCaseType())]);
            final ImageView imageView = ((ImageView) convertView.findViewById(R.id.caseListImage));
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sym_def_app_icon));
            imageView.setTag(c.getCaseImageUrl());
            final ProgressBar progressBar = ((ProgressBar) convertView.findViewById(R.id.caseProgressBar));
            progressBar.setVisibility(View.GONE);
            if (c.getCaseImageUrl() != null && !c.getCaseImageUrl().equalsIgnoreCase(URL_DEFAULT_PARAMETER)) {
                progressBar.setVisibility(View.VISIBLE);
                Model.instance.getImage(c.getCaseImageUrl(), new MasterInterface.LoadImageListener() {

                    @Override
                    public void onSuccess(Bitmap image) {
                        String imgUrl = imageView.getTag().toString();
                        if (imgUrl.equalsIgnoreCase(c.getCaseImageUrl())) {
                            imageView.setImageBitmap(image);
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFail() {
                    }
                });
            }
            return convertView;
        }
    }

    // create action bar menu
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        while (Model.CurrentUser == null) {
            menu.findItem(R.id.actionBarPlusButton).setVisible(false);
        }
        Log.d("TAG", "CurrentUser = " + Model.CurrentUser.getUserId());
        Log.d("TAG", "CurrentUser = " + Model.CurrentUser.getUserRole());
        if (Model.CurrentUser.getUserRole().equals(ADMIN_PARAMETER)) { // admin not allowed to create cases
            menu.findItem(R.id.actionBarPlusButton).setVisible(false);
        } else {
            menu.findItem(R.id.actionBarPlusButton).setVisible(true);
        }
    }

    // ask for permission to access to sd card
    private void checkSDPermission() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Model.CaseUpdateEvent event) {
        if (Model.instance != null) {
            if (toast != null) {
                toast.cancel(); // will cancel any ongoing Toast messages
            }
            if(caseListData.size()==0){
                toast = Toast.makeText(MyApplication.getMyContext(), "Case list is empty", Toast.LENGTH_SHORT);
                toast.show();
            }
            else{
                toast = Toast.makeText(MyApplication.getMyContext(), "Case List was updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        GetData();
    }
}
package com.example.ruslan.towncare.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.R;

import java.util.LinkedList;
import java.util.List;


public class CaseListFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private List<Case> caseData = new LinkedList<>();

    public CaseListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        checkCameraPermission();
        View contentView = inflater.inflate(R.layout.fragment_case_list, container, false);
        ListView list = (ListView) contentView.findViewById(R.id.caseListFreg);
        final CaseListAdapter adapter = new CaseListAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onItemClickListener("" + id);
            }
        });
        Model.instance.getData(new MasterInterface.GetAllCasesCallback() {
            @Override
            public void onComplete(List<Case> list) {
                caseData = list;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {

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


        void onItemClickListener(String id);
    }

    private class CaseListAdapter extends BaseAdapter {

        LayoutInflater infalter = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return caseData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(caseData.get(position).getCaseId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = infalter.inflate(R.layout.case_list_row, null);
            }

            final Case c = caseData.get(position);
            ((TextView) convertView.findViewById(R.id.case_title)).setText(c.getCaseTitle());
            ((TextView) convertView.findViewById(R.id.case_date)).setText(c.getCaseDate());
            ((TextView) convertView.findViewById(R.id.case_status)).setText(c.getCaseStatus());
            ((TextView) convertView.findViewById(R.id.case_like_count)).setText("" + c.getCaseLikeCount());
            ((TextView) convertView.findViewById(R.id.case_unlike_count)).setText("" + c.getCaseUnLikeCount());
            ((TextView) convertView.findViewById(R.id.case_type)).setText(c.getCaseType());
            final ImageView imageView = ((ImageView) convertView.findViewById(R.id.case_image));
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sym_def_app_icon));
            imageView.setTag(c.getCaseImageUrl());
            final ProgressBar progressBar = ((ProgressBar) convertView.findViewById(R.id.case_progress_bar));
            progressBar.setVisibility(View.GONE);
            if (c.getCaseImageUrl() != null && !c.getCaseImageUrl().equalsIgnoreCase("url")) {
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

    private void checkCameraPermission() {
        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
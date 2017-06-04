package com.example.ruslan.towncare.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ruslan.towncare.Model.Case;
import com.example.ruslan.towncare.Model.Model;
import com.example.ruslan.towncare.R;

import java.util.LinkedList;
import java.util.List;


public class CaseList extends Fragment {


    private OnFragmentInteractionListener mListener;
    private List<Case> caseData = new LinkedList<>();

    public CaseList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_case_list, container, false);
        caseData = Model.instance.getData();
        ListView list = (ListView) contentView.findViewById(R.id.caseListFreg);
        list.setAdapter(new CaseList.CaseListAdapter());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onItemClickListener("" + position);
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = infalter.inflate(R.layout.case_list_row, null);
            }

            Case c = caseData.get(position);
            ((TextView) convertView.findViewById(R.id.case_title)).setText(c.getCaseTitle());
            ((TextView) convertView.findViewById(R.id.case_date)).setText(c.getCaseDate());
            ((TextView) convertView.findViewById(R.id.case_status)).setText(c.getCaseStatus());
            ((TextView) convertView.findViewById(R.id.case_like_count)).setText("" + c.getCaseLikeCount());
            ((TextView) convertView.findViewById(R.id.case_unlike_count)).setText("" + c.getCaseUnLikeCount());
            ((TextView) convertView.findViewById(R.id.case_type)).setText(c.getCaseType());

            return convertView;
        }
    }
}

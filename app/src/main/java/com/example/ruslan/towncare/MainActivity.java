package com.example.ruslan.towncare;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.ruslan.towncare.Fragments.CaseDetails;
import com.example.ruslan.towncare.Fragments.CaseList;

public class MainActivity extends Activity implements CaseList.OnFragmentInteractionListener, CaseDetails.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, new CaseList());
        ft.commit();
    }

    @Override
    public void onItemClickListener(String id) {
        Log.d("TAG", id);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, CaseDetails.newInstance(id));
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

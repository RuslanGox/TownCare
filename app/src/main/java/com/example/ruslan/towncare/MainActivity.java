package com.example.ruslan.towncare;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;

import com.example.ruslan.towncare.Fragments.caseList;

public class MainActivity extends Activity implements caseList.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment,new caseList());
        ft.commit();
    }

    @Override
    public void onItemClickListener(String id) {

    }
}

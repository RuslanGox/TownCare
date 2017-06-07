package com.example.ruslan.towncare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ruslan.towncare.Fragments.CaseDetails;
import com.example.ruslan.towncare.Fragments.CaseList;

public class MainActivity extends Activity implements CaseList.OnFragmentInteractionListener, CaseDetails.OnFragmentInteractionListener {

    CaseList caseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caseList = new CaseList();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, caseList);
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

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.case_action_bar_create, menu);
        if (getActionBar() != null) {
            getActionBar().setTitle(R.string.app_name);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        ActionBar actionBar = getActionBar();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.fragmentCreate:
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
//                transaction.replace(R.id.mainFregment, CaseDetails.newInstance(""+0));
                break;
            case R.id.fragmentEdit:
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
//                transaction.replace(R.id.caseListFreg, StudentEditFragment.newInstance(currentStudentIndex));
                break;
            default:
                backToMainActivity();
                return true;

        }
        transaction.commit();
        return true;
    }

    private void backToMainActivity() {
        ActionBar actionBar = getActionBar();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        transaction.replace(R.id.mainFregment, caseList);
        transaction.commit();
    }
}

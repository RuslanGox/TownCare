package com.example.ruslan.towncare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.ruslan.towncare.Fragments.CaseCreateEditFragment;
import com.example.ruslan.towncare.Fragments.CaseDetailsFragment;
import com.example.ruslan.towncare.Fragments.CaseListFragment;

public class MainActivity extends Activity implements CaseListFragment.OnFragmentInteractionListener, CaseDetailsFragment.OnFragmentInteractionListener, CaseCreateEditFragment.OnFragmentInteractionListener {

    CaseListFragment caseListFragment;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caseListFragment = new CaseListFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, caseListFragment);
        ft.commit();
    }

    @Override
    public void onItemClickListener(String id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, CaseDetailsFragment.newInstance(id));
        this.id = id;
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    private void backToMainActivity() {
        ActionBar actionBar = getActionBar();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        transaction.replace(R.id.mainFregment, caseListFragment);
        transaction.commit();
    }

    @Override // onClick MASTER METHOD
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createCaseSaveButton:
                // do something here when save button is pressed (like reload list)
            case R.id.createCaseCancelButton:
                // do something here when cancel button (like error message)
            default:
                backToMainActivity();
        }

    }

    // ACTION BAR METHOD
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.case_action_bar_create, menu);
        if (getActionBar() != null) {
            getActionBar().setTitle(R.string.app_name);
        }
        return true;
    }

    // ACTION BAR LISTENER
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionBar actionBar = getActionBar();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.actionBarCreatePlusButton:
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                transaction.replace(R.id.mainFregment, CaseCreateEditFragment.newInstance(""));
                break;
            case R.id.actionBarDetailsEditButton:
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                Log.d("TAG",id);
                transaction.replace(R.id.mainFregment, CaseCreateEditFragment.newInstance(""+id));
                break;
            default:
                backToMainActivity();
                return true;

        }
        transaction.commit();
        return true;
    }
}
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

import com.example.ruslan.towncare.Fragments.CaseDetailsFragment;
import com.example.ruslan.towncare.Fragments.CaseListFragment;
import com.example.ruslan.towncare.Fragments.CaseUpsertFragment;
import com.example.ruslan.towncare.Models.AlertDialogButtons;
import com.example.ruslan.towncare.Models.Case.Case;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.PickersAndDialogs.AlertCaseDialog;

import static com.example.ruslan.towncare.Models.AlertDialogButtons.OK_BUTTON;
import static com.example.ruslan.towncare.Models.AlertDialogButtons.OK_CANCEL_BUTTONS;

public class MainActivity extends Activity implements CaseListFragment.OnFragmentInteractionListener, CaseUpsertFragment.OnFragmentInteractionListener, AlertCaseDialog.AlertCaseDialogListener {

    CaseListFragment caseListFragment;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caseListFragment = new CaseListFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, caseListFragment, "ListFragment");
        ft.commit();
    }

    @Override
    public void onItemClickListener(String id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, CaseDetailsFragment.newInstance(id), "DetailsFragment");
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
        transaction.replace(R.id.mainFregment, caseListFragment, "ListFragment");
        transaction.commit();
    }

    @Override // onClick MASTER METHOD
    public void onClick(View view, boolean dataChanged) {
        switch (view.getId()) {
            case R.id.upsertCaseSaveButton:
                if (dataChanged) {
                    AlertCaseDialog.newInstance("Edit GOOD", OK_BUTTON).show(getFragmentManager(), "SAVE");
                } else {
                    AlertCaseDialog.newInstance("Create GOOD", OK_BUTTON).show(getFragmentManager(), "SAVE");
                }
                // do something here when save button is pressed (like reload list)

            case R.id.upsertCaseCancelButton:
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
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        switch (item.getItemId()) {
            case R.id.actionBarPlusButton:
                transaction.replace(R.id.mainFregment, CaseUpsertFragment.newInstance(""), "CreateFragemnt");
                break;
            case R.id.actionBarEditButton:
                Log.d("TAG", id);
                transaction.replace(R.id.mainFregment, CaseUpsertFragment.newInstance("" + id), "EditFragment");
                break;
            case R.id.actionBarRemoveButton:
                AlertCaseDialog.newInstance("Are you sure you want to delete " + id, OK_CANCEL_BUTTONS).show(getFragmentManager(), "AlertDialog");
                break;
            default:
                backToMainActivity();
                return true;

        }
        transaction.commit();
        return true;
    }

    @Override
    public void onClick(AlertDialogButtons which, boolean dataChanged) {
        switch (which) {
            case OK_BUTTON:
                if (dataChanged) {
//                    Model.instance.removeCaseSql(id);
                    Model.instance.removeCase(id, new MasterInterface.GetCaseCallback() {
                        @Override
                        public void onComplete(Case aCase) {
                        }

                        @Override
                        public void onComplete() {
                            AlertCaseDialog.newInstance("DELETE OK", OK_BUTTON);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                backToMainActivity();
                break;
        }

        Log.d("TAG", "ALERT WORKS " + which);
    }

}
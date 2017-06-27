package com.example.ruslan.towncare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ruslan.towncare.Fragments.CaseDetailsFragment;
import com.example.ruslan.towncare.Fragments.CaseListFragment;
import com.example.ruslan.towncare.Fragments.CaseUpsertFragment;
import com.example.ruslan.towncare.Models.Enums.AlertDialogButtons;
import com.example.ruslan.towncare.Models.Enums.UpsertMode;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.Models.User.UserFireBase;
import com.example.ruslan.towncare.PickersAndDialogs.AlertCaseDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends Activity implements MasterInterface.CaseListInteractionListener {

    public static final String CASE_WAS_EDITED_SUCCESSFULLY = "Case was edited successfully";
    public static final String CASE_WAS_CREATED_SUCCESSFULLY = "Case was created successfully";
    public static final String CASE_WAS_DELETED_SUCCESSFULLY = "Case was deleted successfully";
    public static final String CASE_WAS_VOTED_SUCCESSFULLY = "Thank you for caring";

    CaseListFragment caseListFragment;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        caseListFragment = new CaseListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, caseListFragment, "ListFragment");
        ft.commit();
    }

    @Override
    public void onItemListClick(String id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFregment, CaseDetailsFragment.newInstance(id), "DetailsFragment");
        this.id = id;
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    // Default back to the main activity (list fragment)
    private void backToMainActivity() {
        ActionBar actionBar = getActionBar();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        transaction.replace(R.id.mainFregment, caseListFragment, "ListFragment");
        transaction.commit();
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
            case R.id.actionBarPlusButton: // insert new case
                transaction.replace(R.id.mainFregment, CaseUpsertFragment.newInstance("", UpsertMode.INSERT_MODE), "CreateFragemnt");
                break;
            case R.id.actionBarEditButton: // edit case
                transaction.replace(R.id.mainFregment, CaseUpsertFragment.newInstance("" + id, UpsertMode.EDIT_MODE), "EditFragment");
                break;
            case R.id.actionBarRemoveButton: // remove case
                AlertCaseDialog.newInstance("Are you sure you want to delete " + id, AlertDialogButtons.OK_CANCEL_BUTTONS).show(getFragmentManager(), "AlertDialog");
                break;
            case R.id.actionBarLogOutButton: // logout & back to Login Screen
                UserFireBase.signOut();
                finish();
            default:
                backToMainActivity();
                return true;
        }
        transaction.commit();
        return true;
    }

    // Main listener for any button
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CaseUpsertFragment.MessageEvent event) {
        switch (event.messageResult) {
            case CANCEL_BUTTON_PRESSED:
                backToMainActivity();
                break;
            case UPDATE_BUTTON_PRESSED:
                backToMainActivity();
                AlertCaseDialog.newInstance(CASE_WAS_EDITED_SUCCESSFULLY, AlertDialogButtons.OK_BUTTON).show(getFragmentManager(), "SAVE");
                break;
            case SAVE_BUTTON_PRESSED:
                backToMainActivity();
                AlertCaseDialog.newInstance(CASE_WAS_CREATED_SUCCESSFULLY, AlertDialogButtons.OK_BUTTON).show(getFragmentManager(), "SAVE");
                break;
            case DELETE_BUTTON_PRESSED:
                Model.instance.removeCase(id, new MasterInterface.GetCaseCallback() {
                    @Override
                    public void onComplete() {
                        backToMainActivity();
                        AlertCaseDialog.newInstance(CASE_WAS_DELETED_SUCCESSFULLY, AlertDialogButtons.OK_BUTTON).show(getFragmentManager(), "AlertDialog");
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                break;
            case LIKE_BUTTON_PRESSED:
            case UNLIKE_BUTTON_PRESSED:
                AlertCaseDialog.newInstance(CASE_WAS_VOTED_SUCCESSFULLY, AlertDialogButtons.OK_BUTTON).show(getFragmentManager(), "AlertDialog");
                backToMainActivity();
                break;
        }
    }
}
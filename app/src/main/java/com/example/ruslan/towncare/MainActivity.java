package com.example.ruslan.towncare;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ruslan.towncare.Fragments.CaseDetailsFragment;
import com.example.ruslan.towncare.Fragments.CaseListFragment;
import com.example.ruslan.towncare.Fragments.CaseUpsertFragment;
import com.example.ruslan.towncare.Models.MasterInterface;
import com.example.ruslan.towncare.Models.Model.Model;
import com.example.ruslan.towncare.PickersAndDialogs.AlertCaseDialog;
import com.google.firebase.auth.FirebaseAuth;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.example.ruslan.towncare.Models.Enums.AlertDialogButtons.OK_BUTTON;
import static com.example.ruslan.towncare.Models.Enums.AlertDialogButtons.OK_CANCEL_BUTTONS;

//public class MainActivity extends Activity implements MasterInterface.CaseListInteractionListener, MasterInterface.UpsertInteractionListener, MasterInterface.AlertCaseDialogListener {
public class MainActivity extends Activity implements MasterInterface.CaseListInteractionListener{

    CaseListFragment caseListFragment;
    String id;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CaseUpsertFragment.MessageEvent event) {
        Toast.makeText(MyApplication.getMyContext(), "got message", Toast.LENGTH_SHORT).show();
        switch (event.messageResultn){
            case CANCEL_BUTTON_PRESSED:
                backToMainActivity();
                Toast.makeText(MyApplication.getMyContext(), "Cancel button was clicked", Toast.LENGTH_SHORT).show();
                break;
            case UPDATE_BUTTON_PRESSED:
                backToMainActivity();
                AlertCaseDialog.newInstance("Edit GOOD", OK_BUTTON).show(getFragmentManager(), "SAVE");
                break;
            case SAVE_BUTTON_PRESSED:
                backToMainActivity();
                AlertCaseDialog.newInstance("Create GOOD", OK_BUTTON).show(getFragmentManager(), "SAVE");
                break;
            case DELETE_BUTTON_PRESSED:
                Model.instance.removeCase(id, new MasterInterface.GetCaseCallback() {
                    @Override
                    public void onComplete() {
                        backToMainActivity();
                        AlertCaseDialog.newInstance("DELETE OK", OK_BUTTON).show(getFragmentManager(), "AlertDialog");
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;

        }
    }

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

    private void backToMainActivity() {
        ActionBar actionBar = getActionBar();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        transaction.replace(R.id.mainFregment, caseListFragment, "ListFragment");
        transaction.commit();
    }

//    @Override // onClick MASTER METHOD
//    public void onUpsertButtonClick(View view, boolean dataChanged) {
//        switch (view.getId()) {
//            case R.id.upsertCaseSaveButton:
//                if (dataChanged) {
//                    AlertCaseDialog.newInstance("Edit GOOD", OK_BUTTON).show(getFragmentManager(), "SAVE");
//                } else {
//                    AlertCaseDialog.newInstance("Create GOOD", OK_BUTTON).show(getFragmentManager(), "SAVE");
//                }
//                // do something here when save button is pressed (like reload list)
//
//            case R.id.upsertCaseCancelButton:
//                // do something here when cancel button (like error message)
//            default:
//                backToMainActivity();
//        }
//
//    }

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
                transaction.replace(R.id.mainFregment, CaseUpsertFragment.newInstance("" + id), "EditFragment");
                break;
            case R.id.actionBarRemoveButton:
                AlertCaseDialog.newInstance("Are you sure you want to delete " + id, OK_CANCEL_BUTTONS).show(getFragmentManager(), "AlertDialog");
                break;
            case R.id.actionBarLogOutButton:
                FirebaseAuth.getInstance().signOut();
                finish();
            default:
                backToMainActivity();
                return true;
        }
        transaction.commit();
        return true;
    }

//    @Override
//    public void onAlertButtonClick(AlertDialogButtons which, boolean dataChanged) {
//        switch (which) {
//            case OK_BUTTON:
//                if (dataChanged) {
//                    Model.instance.removeCase(id, new MasterInterface.GetCaseCallback() {
//                        @Override
//                        public void onComplete(Case aCase) {
//                            Log.d("TAG", "WITH CASE");
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Log.d("TAG", "WITHOUT CASE");
//                            AlertCaseDialog.newInstance("DELETE OK", OK_BUTTON).show(getFragmentManager(), "AlertDialog");
//                            Log.d("TAG", "WITHOUT CASE AFTER");
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//                    });
//                }
//                backToMainActivity();
//                break;
//        }
//        Log.d("TAG", "ALERT WORKS " + which);
//    }
}
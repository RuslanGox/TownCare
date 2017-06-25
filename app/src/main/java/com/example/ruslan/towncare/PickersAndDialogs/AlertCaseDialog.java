package com.example.ruslan.towncare.PickersAndDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.ruslan.towncare.Fragments.CaseUpsertFragment;
import com.example.ruslan.towncare.Models.Enums.AlertDialogButtons;
import com.example.ruslan.towncare.Models.Enums.MessageResult;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by omrih on 14-Jun-17.
 */


public class AlertCaseDialog extends DialogFragment {

    //    private MasterInterface.AlertCaseDialogListener mListener;
    public static final String ARG_PARAM1 = "CASE_ID";
    public static final String ARG_PARAM2 = "ALERT_BUTTONS";

    private String msg;
    private AlertDialogButtons buttons;

    public AlertCaseDialog() {
    }

    public static AlertCaseDialog newInstance(String msg, AlertDialogButtons buttons) {
        AlertCaseDialog fragment = new AlertCaseDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, msg);
        args.putSerializable(ARG_PARAM2, buttons);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            msg = getArguments().getString(ARG_PARAM1);
            buttons = (AlertDialogButtons) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        switch (buttons) {
            case OK_BUTTON:
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        mListener.onAlertButtonClick(OK_BUTTON, false);
                    }
                });
                break;
//            case CANCEL_BUTTON:
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        mListener.onAlertButtonClick(CANCEL_BUTTON, false);
//                    }
//                });
//                break;
            case OK_CANCEL_BUTTONS:
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EventBus.getDefault().post(new CaseUpsertFragment.MessageEvent(MessageResult.DELETE_BUTTON_PRESSED));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        mListener.onAlertButtonClick(CANCEL_BUTTON, false);
                    }
                });
                break;

        }
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof MasterInterface.AlertCaseDialogListener) {
//            mListener = (MasterInterface.AlertCaseDialogListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement AlertCaseDialogListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
}

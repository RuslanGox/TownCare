package com.example.ruslan.towncare.PickersAndDialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Created by omrih on 14-Jun-17.
 */

interface OnDateSetListener {
    void onDateSet(int year, int month, int day);
}

public class CaseDatePicker extends EditText implements OnDateSetListener {

    public CaseDatePicker(Context context) {
        super(context);
    }

    public CaseDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CaseDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static class CaseDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private static final String ARG_CONTAINER_EDIT_TEXT_VIEW = "edit_text_container";
        private OnDateSetListener listener;

        public static CaseDatePickerDialog newInstance(int viewId) {
            CaseDatePickerDialog cdpd = new CaseDatePickerDialog();
            Bundle args = new Bundle();
            args.putInt(ARG_CONTAINER_EDIT_TEXT_VIEW, viewId);
            cdpd.setArguments(args);
            return cdpd;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            Dialog datePicker = new DatePickerDialog(getActivity(), 0, this, 2017, 1, 1);
            if (getArguments() != null) {
                int viewId = getArguments().getInt(ARG_CONTAINER_EDIT_TEXT_VIEW);
                listener = (OnDateSetListener) getActivity().findViewById(viewId);
            }
            return datePicker;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            listener.onDateSet(year, month + 1, dayOfMonth);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            CaseDatePickerDialog cdpd = CaseDatePickerDialog.newInstance(getId());
            cdpd.show(((Activity) getContext()).getFragmentManager(), "Touch");
            return true;
        }
        return true;
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        setText("" + day + "\\" + month + "\\" + year);
    }

}

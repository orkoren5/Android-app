package finalproject.homie.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Date;

import finalproject.homie.DO.Assignment;

/**
 * Created by I311044 on 31/05/2017.
 */

public class DatePickerDialogFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

    private EditText container;
    private Date date;

    public void setContainerView(EditText view) {
        this.container = view;
    }

    public void setCurrentDate(Date d) {
        this.date = d;
    }
    @RequiresApi(24)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int realMonth = month + 1;
        if (this.container != null) {
            this.container.setText(dayOfMonth + "/" + realMonth + "/" + year);
        }
    }
}

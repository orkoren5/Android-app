package finalproject.homie.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Date;

import finalproject.homie.DO.Assignment;

/**
 * Created by I311044 on 31/05/2017.
 */

public class TimePickerDialogFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

    private EditText container;
    private Date date;

    public void setContainerView(EditText view) {
        this.container = view;
    }

    public void setCurrentDate(Date date) {
        this.date = date;
    }

    @RequiresApi(24)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hours, minutes,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (this.container != null) {
            this.container.setText(hourOfDay + ":" + minute);
        }
    }
}

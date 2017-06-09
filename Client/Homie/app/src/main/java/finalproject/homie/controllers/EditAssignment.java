package finalproject.homie.controllers;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import finalproject.homie.DAL.DataAppender;
import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.Bindables.BindableInteger;
import finalproject.homie.R;
import finalproject.homie.adapters.EntityTextWatcher;
import finalproject.homie.databinding.ActivityEditAssignmentBinding;
import finalproject.homie.model.Model;

public class EditAssignment extends BaseEditAssignmentActivity {

    private TextView mTextMessage;
    private Assignment ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityEditAssignmentBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_assignment);
        super.onCreate(savedInstanceState);

        Model m = ((BaseApplication)getApplication()).getModel();
        final Assignment ctx = m.getSelectedAssignment();
        final boolean isNew = getIntent().getBooleanExtra("IS_NEW", true);
        this.ctx = ctx;
        binding.setAssignment(ctx);

        EditText txt = (EditText)findViewById(R.id.txtNumber);
        txt.addTextChangedListener(new EntityTextWatcher(ctx, "setNumber", int.class));

        EditText txtDays = (EditText)findViewById(R.id.txtDateAssessment);
        txtDays.addTextChangedListener(new EntityTextWatcher(ctx, "setDaysAssessment", int.class));

        EditText txtDeadlineDate = (EditText)findViewById(R.id.txtDeadLineDate);
        txtDeadlineDate.addTextChangedListener(new EntityTextWatcher(ctx, "setDeadline", Date.class));
        txtDeadlineDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePickerDialog(v);
                }
            }
        });

        EditText txtDeadlineTime = (EditText)findViewById(R.id.txtDeadLineTime);
        //txtDeadlineTime.addTextChangedListener(new EntityTextWatcher(ctx, "setDeadline", Date.class));
        txtDeadlineTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showTimePickerDialog(v);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = ((BaseApplication)getApplicationContext()).getToken();
                DataAppender da = new DataAppender<Assignment>(token, new IDataResponseHandler() {
                    @Override
                    public void OnError(int errorCode) {
                        if (errorCode == 403) {
                            showMessageToast(getString(R.string.msg_error_auth));
                        } else if (errorCode == 505) {
                            showMessageToast(getString(R.string.msg_server_down));
                        }
                    }

                    @Override
                    public void OnSuccess(String message) {
                        showMessageToast(getString(R.string.msg_assignment_added));
                        if (isNew) {
                            ((BaseApplication)getApplication()).getModel().addAssignment(ctx);
                        }
                        onBackPressed();
                    }
                });
                da.addAssignment(ctx, isNew);
            }
        });
    }

    @Override
    protected void setSelectedItem(Menu menu) {
        menu.getItem(0).setChecked(true);
    }

    @Override
    protected void additem() {

    }

    void showMessageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showDatePickerDialog(View container) {
        DatePickerDialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.setCurrentDate(ctx.getDeadline());
        newFragment.setContainerView((EditText)container);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View container) {
        TimePickerDialogFragment newFragment = new TimePickerDialogFragment();
        newFragment.setCurrentDate(ctx.getDeadline());
        newFragment.setContainerView((EditText)container);
        newFragment.show(getFragmentManager(), "timePicker");
    }
}

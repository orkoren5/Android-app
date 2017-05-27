package finalproject.homie.controllers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class EditAssignment extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditAssignmentBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_assignment);

        Model m = ((BaseApplication)getApplication()).getModel();
        final Assignment ctx = m.getSelectedAssignment();
        binding.setAssignment(ctx);

        EditText txt = (EditText)findViewById(R.id.txtNumber);
        txt.addTextChangedListener(new EntityTextWatcher(ctx, "setNumber", int.class));

        EditText txtDays = (EditText)findViewById(R.id.txtDateAssessment);
        txtDays.addTextChangedListener(new EntityTextWatcher(ctx, "setDaysAssessment", int.class));

        EditText txtDeadline = (EditText)findViewById(R.id.txtDeadLine);
        txtDeadline.addTextChangedListener(new EntityTextWatcher(ctx, "setDeadline", Date.class));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = ((BaseApplication)getApplicationContext()).getToken();
                DataAppender da = new DataAppender(token);
                da.addAssignment(ctx, new IDataResponseHandler() {
                    @Override
                    public void OnError(int errorCode) {
                        if (errorCode == 403) {
                            showMessageToast("Error authenticating user. Please login and retry");
                        } else if (errorCode == 505) {
                            showMessageToast("The server is not responding. Please retry later");
                        }
                    }

                    @Override
                    public void OnSuccess() {
                        showMessageToast("Assignment was added");
                        if (getIntent().getBooleanExtra("IS_NEW", true)) {
                            ((BaseApplication)getApplication()).getModel().addAssignment(ctx);
                        }
                        onBackPressed();
                    }
                });
            }
        });
    }

    void showMessageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

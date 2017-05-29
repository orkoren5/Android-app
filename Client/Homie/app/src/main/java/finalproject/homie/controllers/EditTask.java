package finalproject.homie.controllers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import finalproject.homie.DAL.DataAppender;
import finalproject.homie.DO.Assignment;
import finalproject.homie.DO.Task;
import finalproject.homie.R;
import finalproject.homie.adapters.EntityTextWatcher;
import finalproject.homie.databinding.ActivityEditTaskBinding;
import finalproject.homie.model.Model;

import static finalproject.homie.R.layout.activity_edit_task;

public class EditTask extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditTaskBinding binding = DataBindingUtil.setContentView(this, activity_edit_task);

        Model m = ((BaseApplication)getApplication()).getModel();
        final Task ctx = m.getSelectedTask();
        binding.setTask(ctx);

        EditText txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtTitle.addTextChangedListener(new EntityTextWatcher(ctx, "setTitle", String.class));

        EditText txtDays = (EditText)findViewById(R.id.txtDaysAssessment);
        txtDays.addTextChangedListener(new EntityTextWatcher(ctx, "setDaysAssessment", int.class));

        EditText txtDescription = (EditText)findViewById(R.id.txtDeadLine);
        txtDescription.addTextChangedListener(new EntityTextWatcher(ctx, "setDescription", String.class));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = ((BaseApplication)getApplicationContext()).getToken();
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OTI5ZjdlZGRjZGQwMTc4ZGUyZjNhNzgiLCJuYW1lIjoib3JrbyIsInBhc3N3b3JkIjoiMTIzIiwiaWF0IjoxNDk2MDQ3OTk5LCJleHAiOjE0OTYxMzQzOTl9.K7wVWDfEe3hTH-h8bjBWJULjISUyhZmJZTyUIdiKyo4";
                DataAppender da = new DataAppender(token);
                da.addTask(ctx, new IDataResponseHandler() {
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
                            ((BaseApplication)getApplication()).getModel().addTask(ctx);
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

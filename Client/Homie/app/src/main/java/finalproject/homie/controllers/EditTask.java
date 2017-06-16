package finalproject.homie.controllers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import finalproject.homie.DAL.DataAppender;
import finalproject.homie.DO.Task;
import finalproject.homie.DO.User;
import finalproject.homie.R;
import finalproject.homie.adapters.EntityTextWatcher;
import finalproject.homie.databinding.ActivityEditTaskBinding;
import finalproject.homie.model.Model;

import static finalproject.homie.DO.Task.*;
import static finalproject.homie.R.layout.activity_edit_task;

public class EditTask extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEditTaskBinding binding = DataBindingUtil.setContentView(this, activity_edit_task);

        Model m = ((BaseApplication)getApplication()).getModel();
        final Task ctx = m.getSelectedTask();
        final boolean isNew = getIntent().getBooleanExtra("IS_NEW", true);
        binding.setTask(ctx);

        EditText txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtTitle.addTextChangedListener(new EntityTextWatcher(ctx, "setTitle", String.class));

        EditText txtDays = (EditText)findViewById(R.id.txtDaysAssessment);
        txtDays.addTextChangedListener(new EntityTextWatcher(ctx, "setDaysAssessment", int.class));

        EditText txtDescription = (EditText)findViewById(R.id.txtDescription);
        txtDescription.addTextChangedListener(new EntityTextWatcher(ctx, "setDescription", String.class));

        final List<User> users = m.getSelectedAssignment().getUsers();
        Spinner spnrUsers = (Spinner) findViewById(R.id.spnrProcessingUser);
        spnrUsers.setAdapter(new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, users));
        spnrUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ctx.setAssignedUserId(users.get(position).getID());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spnrUsers.setSelection(ctx.getStatus().toInt() - 1);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_status);
        spinner.setAdapter(new ArrayAdapter<Status>(this, android.R.layout.simple_list_item_1, Status.values()));
        spinner.setOnItemSelectedListener(new EntityTextWatcher(ctx, "setStatus", Status.class));
        spinner.setSelection(ctx.getStatus().toInt() - 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = ((BaseApplication)getApplicationContext()).getToken();
                DataAppender da = new DataAppender<Task>(token, new IDataResponseHandler() {
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
                        showMessageToast(getString(R.string.msg_task_added));
                        Model m = ((BaseApplication)getApplication()).getModel();
                        if (isNew) {
                            m.addTask(ctx);
                        } else {
                            m.taskUpdated(ctx);
                        }
                        onBackPressed();
                    }
                });
                da.addTask(ctx, isNew);
            }
        });
    }

    void showMessageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

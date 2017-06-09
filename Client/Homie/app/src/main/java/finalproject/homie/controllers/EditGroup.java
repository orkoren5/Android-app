package finalproject.homie.controllers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import finalproject.homie.DAL.CallServerFunction;
import finalproject.homie.DAL.DataAppender;
import finalproject.homie.DO.Assignment;
import finalproject.homie.R;
import finalproject.homie.adapters.EntityTextWatcher;
import finalproject.homie.databinding.ActivityEditGroupBinding;
import finalproject.homie.model.Model;

import static finalproject.homie.DO.Task.Status;
import static finalproject.homie.R.layout.activity_edit_group;
import static finalproject.homie.R.layout.activity_edit_task;

public class EditGroup extends BaseEditAssignmentActivity {

    private TextView mTextMessage;
    private boolean isAddAction = true;
    private Assignment ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityEditGroupBinding binding = DataBindingUtil.setContentView(this, activity_edit_group);
        super.onCreate(savedInstanceState);


        Model m = ((BaseApplication)getApplication()).getModel();
        final Assignment ctx = m.getSelectedAssignment();
        this.ctx = ctx;
        binding.setAssignment(ctx);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.group_members, ctx.getUsers().toString()));

        Spinner spinner = (Spinner) findViewById(R.id.spinner_group);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_group, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Status.values()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isAddAction = position == 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void setSelectedItem(Menu menu) {
        menu.getItem(2).setChecked(true);
    }

    @Override
    protected void additem() {

    }

    void showMessageToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    void sendAction(View v) {
        String token = ((BaseApplication)getApplication()).getToken();
        CallServerFunction caller = new CallServerFunction(token, new IDataResponseHandler() {

            @Override
            public void OnError(int errorCode) {

            }

            @Override
            public void OnSuccess(String message) {
                showMessageToast(getString(R.string.msg_assignment_added));
            }
        });
        String username = ((EditText)findViewById(R.id.txtUsername)).getText().toString();
        caller.addOrDeleteUserToAssignment(ctx, username, isAddAction);
    }

}

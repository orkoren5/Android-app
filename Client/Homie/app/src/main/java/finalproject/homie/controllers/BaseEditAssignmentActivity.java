package finalproject.homie.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.view.Menu;
import android.view.MenuItem;

import finalproject.homie.DO.Task;
import finalproject.homie.R;

/**
 * Created by I311044 on 01/06/2017.
 */

public abstract class BaseEditAssignmentActivity extends BaseNavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        nav.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.toString()) {
                    case "Group":
                        openGroupActivity();
                        break;
                    case "Tasks":
                        openTaskListActivity();
                        break;
                    case "Edit":
                        openEditAssignmentActivity();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        setSelectedItem(nav.getMenu());
    }

    protected abstract void setSelectedItem(Menu menu);

    protected void openTaskListActivity() {
        Intent intent = new Intent(this, TaskList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    protected void openGroupActivity() {
        Intent intent = new Intent(this, EditGroup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    protected void openEditAssignmentActivity() {
        Intent intent = new Intent(this, EditAssignment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("IS_NEW", false);
        startActivity(intent);
    }
}

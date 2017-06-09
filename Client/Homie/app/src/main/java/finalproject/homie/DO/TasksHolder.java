package finalproject.homie.DO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I311044 on 30/05/2017.
 */

public class TasksHolder {
    List<Task>[] tasks;

    public TasksHolder() {
        tasks = new ArrayList[3];
        tasks[0] = new ArrayList<>();
        tasks[1] = new ArrayList<>();
        tasks[2] = new ArrayList<>();
    }

    public List<Task> getList(Task.Status status) {
        int index = status.toInt() - 1;
        return tasks[index];
    }

    public void add(Task t) {
        int index = t.getStatus().toInt() - 1;
        tasks[index].add(t);
    }

    public void taskUpdated(Task t) {
        int index = t.getStatus().toInt() - 1;
        if (!tasks[index].contains(t)) {
            tasks[0].remove(t);
            tasks[1].remove(t);
            tasks[2].remove(t);
            tasks[index].add(t);
        }
    }
}

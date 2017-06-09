package finalproject.homie.model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import finalproject.homie.DAL.*;
import finalproject.homie.DO.*;

/**
 * Created by Or Koren on 02/03/2017.
 */

public class Model {

    // Dirty flags
    public static final int ASSIGNMENT_FLAG = 0x01;
    public static final int COURSES_FLAG = 0x02;
    public static final int TASKS_FLAG = 0x04;

    private List<Course> myCourses = new ArrayList<>();
    private List<Assignment> myAssignments = new ArrayList<>();
    private HashMap<String, TasksHolder> tasks = new HashMap<>();
    private HashMap<String, List<Assignment>> assignments = new HashMap<>();
    private Assignment selectedAssignment = null;
    private Task selectedTask = null;
    private Course selectedCourse = null;
    private String rawData;
    private int dirtyFalgs = 0;

    /**
     * Creates a new empty model
     */
    public Model() {
        dirtyFalgs = ASSIGNMENT_FLAG | COURSES_FLAG | TASKS_FLAG;
        //myCourses.add(new Course("SPL", 20214041));
        //myCourses.add(new Course("DCS", 38110981));
    }

    public List<Course> getMyCourses() {
        return myCourses;
    }

    public List<Assignment> getMyAssignments() {
        return myAssignments;
    }

    public List<Assignment> getAssignmentsForCourse(String courseId) {
        List<Assignment> list = assignments.get(courseId);
        if (list == null) {
            list = new ArrayList<>();
            assignments.put(courseId, list);
        }
        return list;
    }

    public TasksHolder getTasksForAssignment(String assignmentId) {
        TasksHolder lists = tasks.get(assignmentId);
        if (lists == null) {
            lists = new TasksHolder();
        }
        tasks.put(assignmentId, lists);
        return lists;
    }

    public void updateTasksForAssignment(Assignment a) {
        tasks.put(a.getID(), a.getTasks());
    }

    public void setSelectedAssignment(Assignment selectedAssignment) {
        this.selectedAssignment = selectedAssignment;
    }

    public void setSelectedTask(Task task) {
        this.selectedTask = task;
    }

    public void setSelectedCourse(Course course) {
        this.selectedCourse = course;
    }
    public void addAssignment(Assignment a) {
        List<Assignment> list = assignments.get(a.getRelatedCourse().getID());
        if (list == null) {
            list = new ArrayList<>();
            assignments.put(a.getRelatedCourse().getID(), list);
        }
        list.add(a);
        dirtyFalgs |= ASSIGNMENT_FLAG;
    }

    public void addTask(Task t) {
        TasksHolder lists = tasks.get(t.getAssignmentId());
        if (lists == null && t.getRelatedAssignment().getTasks() == null) {
            lists = new TasksHolder();
        } else if (lists == null) {
            lists = t.getRelatedAssignment().getTasks();
        }
        tasks.put(t.getAssignmentId(), lists);
        lists.add(t);
        dirtyFalgs |= TASKS_FLAG;
    }

    public void taskUpdated(Task t) {
        TasksHolder lists = tasks.get(t.getAssignmentId());
        if (lists == null && t.getRelatedAssignment().getTasks() == null) {
            lists = new TasksHolder();
        } else if (lists == null) {
            lists = t.getRelatedAssignment().getTasks();
        }
        tasks.put(t.getAssignmentId(), lists);
        lists.taskUpdated(t);
        dirtyFalgs |= TASKS_FLAG;
    }

    public void setDirty(int flag) {
        dirtyFalgs |= flag;
    }

    /**
     * Checks if there was a change in any of the lists of the model. Upen checking, clears the
     * indicator.
     * @param flag selects which list to check. Accepted values are static ints on Model
     * @return weather the list has been changed
     */
    public boolean checkAndRemoveIsDirty(int flag) {
//        boolean ret = (dirtyFalgs & flag) > 0 ? true : false;
//        dirtyFalgs &= ~flag;
//        return ret;
        return true;
    }

    public Assignment getSelectedAssignment() {
        return this.selectedAssignment;
    }

    public Course getSelectedCourse() {
        return this.selectedCourse;
    }

    public Task getSelectedTask() {
        return this.selectedTask;
    }
}



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

    List<Course> myCourses = new ArrayList<>();
    List<Assignment> myAssignments = new ArrayList<>();
    HashMap<String, List<Task>> tasks = new HashMap<>();
    Assignment selectedAssignment = null;
    String rawData;
    int dirtyFalgs = 0;

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

    public List<Assignment> getAssignmentsForCourse(int courseIndex) {
        return myCourses.get(courseIndex).getAssignmentList();
    }

    public List<Task> getTasksForAssignment(String assignmentId) {
        List<Task> list = tasks.get(assignmentId);
        if (list == null) {
            list = new ArrayList<>();
            tasks.put(assignmentId, list);
        }
        return list;
    }

    public void updateTasksForAssignment(Assignment a) {
        tasks.put(a.getID(), a.getTasks());
    }
    public void setSelectedAssignment(Assignment selectedAssignment) {
        this.selectedAssignment = selectedAssignment;
    }

    public void addAssignment(Assignment a) {
        this.myAssignments.add(a);
        dirtyFalgs |= ASSIGNMENT_FLAG;
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
}



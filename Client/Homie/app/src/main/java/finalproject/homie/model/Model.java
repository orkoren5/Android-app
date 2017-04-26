package finalproject.homie.model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import finalproject.homie.DAL.*;
import finalproject.homie.DO.*;

/**
 * Created by Or Koren on 02/03/2017.
 */

public class Model {
    List<Course> myCourses = new ArrayList<Course>();
    List<Assignment> myAssignments = new ArrayList<Assignment>();

    String rawData;

    /**
     * Creates a new empty model
     */
    public Model() {
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
}



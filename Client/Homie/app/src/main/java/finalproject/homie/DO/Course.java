package finalproject.homie.DO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Or Koren on 02/03/2017.
 */

public class Course extends BusinessEntity{
    String name;
    long number;
    List<Assignment> assignmentList = new ArrayList<Assignment>();

    private Course() {};

    public Course(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public JSONObject toJSON() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Assignment> getAssignmentList() {
        return  assignmentList;
    }

    public static Course parseJSON(JSONObject json) throws JSONException{
        Course c = new Course();
        c.setNumber(json.getLong("number"));
        c.setName(json.getString("name"));
        c.setID(json.getString("_id"));
        return c;
    }
}

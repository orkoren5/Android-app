package finalproject.homie.DO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I311044 on 01/06/2017.
 */

public class User extends BusinessEntity {

    private String name;
    private List<String> courseIds = new ArrayList<>();
    private List<Course> courses = new EntityArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public String getCourseIdsStr() {
        StringBuilder sb = new StringBuilder();
        for (String s : courseIds) {
            sb.append(s);
            sb.append(",");
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 1, sb.length() - 1);
        }
        return sb.toString();
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public BusinessEntity parseJSON(JSONObject json) throws JSONException {
        this.setID(json.getString("_id"));
        this.setName(json.getString("name"));

        if (json.has("courseIds")) {
            JSONArray jCourses = json.getJSONArray("courseIds");
            for (int i = 0; i < jCourses.length(); i++) {
                courseIds.add(jCourses.getString(i));
            }
        }

        // not used
        if (json.has("courses")) {
            JSONArray jCourses = json.getJSONArray("courses");
            for (int i = 0; i < jCourses.length(); i++) {
                Course c = new Course();
                c.parseJSON(jCourses.getJSONObject(i));
                courses.add(c);
            }
        }
        return this;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", name);

        JSONArray jCourses = new JSONArray();
        for (int i = 0; i < courseIds.size(); i++) {
            jCourses.put(courseIds.get(i));
        }
        json.put("courseIds", jCourses);

        return json;
    }

    @Override
    public String getForeignIdFields() {
        return "courseIds";
    }

    @Override
    public String toString() {
        return this.name;
    }

}

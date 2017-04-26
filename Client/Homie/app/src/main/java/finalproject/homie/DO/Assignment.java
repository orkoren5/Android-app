package finalproject.homie.DO;

import android.nfc.FormatException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by I311044 on 02/03/2017.
 */

public class Assignment extends BusinessEntity{
    long courseNumber;
    int number;
    Date deadline = new Date(0);
    int daysAssessment;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public long getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(long courseNumber) {
        this.courseNumber = courseNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getDaysAssessment() {
        return daysAssessment;
    }

    public void setDaysAssessment(int daysAssessment) {
        this.daysAssessment = daysAssessment;
    }

    @Override
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("courseNumber", courseNumber);
        json.put("number", number);
        json.put("deadline", Assignment.format.format(deadline));
        json.put("daysAssessment", daysAssessment);
        json.put("approved", true);
        return json;
    }

    @Override
    public Assignment parseJSON(JSONObject json) throws JSONException{
        Assignment c = new Assignment();
        c.setID(json.getString("_id"));
        c.setCourseNumber(json.getLong("courseNumber"));
        c.setDaysAssessment(json.getInt("daysAssessment"));
        c.setNumber(json.getInt("number"));
        try {
            c.setDeadline(format.parse(json.getString("deadline")));
        }
        catch (ParseException ex) {
            ex.printStackTrace();
            c.setDeadline(new Date(0));
        }
        return c;
    }
}

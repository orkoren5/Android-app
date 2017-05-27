package finalproject.homie.DO;

import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by I311044 on 05/03/2017.
 */

public abstract class BusinessEntity extends BaseObservable{
    private String ID;

    public String getID() {
        return ID;
    }

    public static class Factory<T extends BusinessEntity> {

        Class<T> c;
        public Factory(Class<T> c) {
            this.c = c;
        }
        public T create() {
            try {
                return c.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Nullable
    public static BusinessEntity parseJSON(JSONObject json, String subClassName)
            throws JSONException {
        switch (subClassName) {
            case "Assignment":
                //return Assignment.parseJSON(json);
            case "Course":
                //return Course.parseJSON(json);
            default:
                return null;
        }
    }

    public abstract BusinessEntity parseJSON(JSONObject json) throws  JSONException;

    public abstract JSONObject toJSON() throws JSONException;

}

package finalproject.homie.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import finalproject.homie.DO.BusinessEntity;

/**
 * Created by I311044 on 26/05/2017.
 */

public class EntityTextWatcher implements TextWatcher, OnItemSelectedListener {

    BusinessEntity obj;
    Method method;
    Class<?> type;
    boolean ignoreFirstChar = false;
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public EntityTextWatcher(final BusinessEntity obj, String setteMethodName, Class<?> type) {
        try {
            this.method = obj.getClass().getMethod(setteMethodName, type);
            this.type = type;
            this.obj = obj;
        } catch (NoSuchMethodException ex) {

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        switch (type.getSimpleName()) {
            case "int":
            case "integer":
                if (s.toString().equals("0")) {
                    ignoreFirstChar = true;
                }
            default:
                return;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            Object param = null;
            switch (type.getSimpleName()) {
                case "int":
                case "integer":
                    if (s.length() == 0) {
                        param = 0;
                    } else if (ignoreFirstChar) {
                        int c =  s.charAt(0) == '0' ? 1 : 0;
                        param = Character.digit(s.charAt(c), 10);
                    } else {
                        param = Integer.valueOf(s.toString());
                    }
                    break;
                case "Date":
                    param = format.parse(s.toString());
                    break;
                case "String":
                    param = s.toString();
                    break;
                default:
                    return;
            }
            method.invoke(this.obj, param);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Bad input field: " + s.toString());
        } catch (ParseException e) {
            System.out.println("ERROR: Bad input field: " + s.toString());
        } finally {
            ignoreFirstChar = false;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (type.isEnum()) {
                Object param = type.getMethod("valueOf", int.class).invoke(type, position + 1);
                method.invoke(this.obj, param);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

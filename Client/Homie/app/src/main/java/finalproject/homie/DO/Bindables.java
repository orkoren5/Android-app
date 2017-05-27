package finalproject.homie.DO;

import android.annotation.TargetApi;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.widget.EditText;

import java.util.Date;
import java.util.Objects;

import finalproject.homie.R;
import finalproject.homie.controllers.EditAssignment;

/**
 * Created by I311044 on 26/05/2017.
 */

public class Bindables {

    public static class BindableString extends BaseObservable {
        private String value;
        public String get() {
            return value != null ? value : "";
        }

        @TargetApi(19)
        public void set(String value) {
            if (!Objects.equals(this.value, value)) {
                this.value = value;
                notifyChange();
            }
        }
        public boolean isEmpty() {
            return value == null || value.isEmpty();
        }

        @BindingConversion
        public static String convertBindableToString(
                BindableString bindableString) {
            return bindableString.get();
        }
    }

    public static class BindableDate extends BaseObservable {
        private Date value;
        public Date get() {
            return value;
        }

        public BindableDate() {
            this.value = new Date(0);
        }

        @TargetApi(19)
        public void set(Date value) {
            if (!Objects.equals(this.value, value)) {
                this.value = value;
                notifyChange();
            }
        }

        @NonNull
        @BindingConversion
        public static String convertBindableToString(BindableDate bindableDate) {
            return String.valueOf(bindableDate.get());
        }

        @NonNull
        @BindingConversion
        public static String convertBindableToString(Integer i) {
            return String.valueOf(i);
        }
    }


    public static class BindableInteger extends BaseObservable {
        private int value;
        public int get() {
            return value;
        }

        @TargetApi(19)
        public void set(int value) {
            if (!Objects.equals(this.value, value)) {
                this.value = value;
                notifyChange();
            }
        }

        @NonNull
        @BindingConversion
        public static String convertBindableToString(BindableInteger bindableInt) {
            return String.valueOf(bindableInt.get());
        }
    }

    @BindingAdapter("app:binding")
    public static void bindEditText(final EditText view, final Integer value) {
        Pair<Integer, TextWatcher> pair = (Pair) view.getTag(R.id.bound_observable);
        if (pair == null || pair.first != value) {
            if (pair != null) {
                view.removeTextChangedListener(pair.second);
            }
            TextWatcher watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int i = s.length() > 0 ? Integer.valueOf(s.toString()) : 0;
                    //((EditAssignment)view.getContext()).getAssignment().setNumber(i);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };
            view.setTag(R.id.bound_observable, new Pair<>(value, watcher));
            view.addTextChangedListener(watcher);
        }
        int newValue = value;
        if (!view.getText().toString().equals(newValue)) {
            view.setText(String.valueOf(newValue));
        }
    }
}

package com.arkal.powerbill;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/** Small helpers shared by the calculator and the edit screen. */
public final class UiUtils {

    /** The rebate SeekBar runs 0..10, each step = 0.5%. */
    public static final int REBATE_SEEKBAR_MAX = 10;

    private UiUtils() {
    }

    public static double rebateFromProgress(int progress) {
        return progress / 2.0;
    }

    public static int progressFromRebate(double rebate) {
        return (int) Math.round(rebate * 2);
    }

    /**
     * Reads and validates the units field. Shows an error message on the field
     * and returns null when the value is missing or outside 1 - 1000.
     */
    public static Integer readUnits(Context context, EditText field) {
        String text = field.getText().toString().trim();
        if (text.isEmpty()) {
            field.setError(context.getString(R.string.error_units_empty));
            field.requestFocus();
            return null;
        }
        int units;
        try {
            units = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            field.setError(context.getString(R.string.error_units_range));
            field.requestFocus();
            return null;
        }
        if (!BillCalculator.isValidUnits(units)) {
            field.setError(context.getString(R.string.error_units_range));
            field.requestFocus();
            return null;
        }
        field.setError(null);
        return units;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        view.clearFocus();
    }
}

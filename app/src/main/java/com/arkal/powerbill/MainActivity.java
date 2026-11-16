package com.arkal.powerbill;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/** Main page: inputs, calculation, result on the same page, auto-save to database. */
public class MainActivity extends Activity {

    private Spinner spinnerMonth;
    private EditText etUnits;
    private SeekBar seekRebate;
    private TextView tvRebateValue;

    private LinearLayout resultCard;
    private TextView tvResMonth, tvResUnits, tvResTotal, tvResRebate, tvResFinal, tvBreakdown;
    private ScrollView scrollView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        scrollView = findViewById(R.id.scrollMain);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        etUnits = findViewById(R.id.etUnits);
        seekRebate = findViewById(R.id.seekRebate);
        tvRebateValue = findViewById(R.id.tvRebateValue);

        resultCard = findViewById(R.id.resultCard);
        tvResMonth = findViewById(R.id.tvResMonth);
        tvResUnits = findViewById(R.id.tvResUnits);
        tvResTotal = findViewById(R.id.tvResTotal);
        tvResRebate = findViewById(R.id.tvResRebate);
        tvResFinal = findViewById(R.id.tvResFinal);
        tvBreakdown = findViewById(R.id.tvBreakdown);

        Button btnCalculate = findViewById(R.id.btnCalculate);
        Button btnReset = findViewById(R.id.btnReset);
        Button btnHistory = findViewById(R.id.btnHistory);

        // Month dropdown (Jan - Dec), defaults to the current month
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
        spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));

        // Rebate slider 0% - 5% in 0.5% steps
        seekRebate.setMax(UiUtils.REBATE_SEEKBAR_MAX);
        seekRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateRebateLabel();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        updateRebateLabel();

        // Live validation while typing
        etUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (text.isEmpty()) {
                    etUnits.setError(null);
                    return;
                }
                try {
                    int value = Integer.parseInt(text);
                    etUnits.setError(BillCalculator.isValidUnits(value)
                            ? null : getString(R.string.error_units_range));
                } catch (NumberFormatException e) {
                    etUnits.setError(getString(R.string.error_units_range));
                }
            }
        });

        // "Done" on the keyboard also calculates
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculateAndSave();
                return true;
            }
            return false;
        });

        btnCalculate.setOnClickListener(v -> calculateAndSave());
        btnReset.setOnClickListener(v -> resetForm());
        btnHistory.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
    }

    private void updateRebateLabel() {
        double rebate = UiUtils.rebateFromProgress(seekRebate.getProgress());
        tvRebateValue.setText(getString(R.string.rebate_value, rebate));
    }

    private void calculateAndSave() {
        Integer units = UiUtils.readUnits(this, etUnits);
        if (units == null) {
            Toast.makeText(this, R.string.error_units_range, Toast.LENGTH_SHORT).show();
            return;
        }

        double rebate = UiUtils.rebateFromProgress(seekRebate.getProgress());
        double total = BillCalculator.calculateTotalCharges(units);
        double finalCost = BillCalculator.calculateFinalCost(total, rebate);
        int monthIndex = spinnerMonth.getSelectedItemPosition();
        String month = spinnerMonth.getSelectedItem().toString();

        // Show result on the same page
        tvResMonth.setText(month);
        tvResUnits.setText(getString(R.string.units_format, units));
        tvResTotal.setText(BillCalculator.formatRM(total));
        tvResRebate.setText(getString(R.string.rebate_detail, rebate,
                BillCalculator.formatRM(total - finalCost)));
        tvResFinal.setText(BillCalculator.formatRM(finalCost));
        tvBreakdown.setText(BillCalculator.buildBreakdown(units));
        resultCard.setVisibility(View.VISIBLE);

        UiUtils.hideKeyboard(this);
        scrollView.post(() -> scrollView.smoothScrollTo(0, resultCard.getTop()));

        // Store in local database
        long id = db.insertBill(new Bill(monthIndex, month, units, rebate, total, finalCost));
        Toast.makeText(this, id > 0 ? R.string.toast_saved : R.string.toast_save_failed,
                Toast.LENGTH_SHORT).show();
    }

    private void resetForm() {
        etUnits.setText("");
        etUnits.setError(null);
        seekRebate.setProgress(0);
        spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        resultCard.setVisibility(View.GONE);
        UiUtils.hideKeyboard(this);
        scrollView.smoothScrollTo(0, 0);
        Toast.makeText(this, R.string.toast_reset, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

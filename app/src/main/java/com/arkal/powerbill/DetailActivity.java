package com.arkal.powerbill;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/** Shows full details of one bill and lets the user edit or delete it. */
public class DetailActivity extends Activity {

    public static final String EXTRA_BILL_ID = "extra_bill_id";

    private DatabaseHelper db;
    private Bill bill;

    private TextView tvMonth, tvUnits, tvTotal, tvRebate, tvFinal;
    private LinearLayout actionRow, editCard;
    private Spinner spEditMonth;
    private EditText etEditUnits;
    private SeekBar sbEditRebate;
    private TextView tvEditRebateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);
        long billId = getIntent().getLongExtra(EXTRA_BILL_ID, -1);
        bill = db.getBillById(billId);
        if (bill == null) {
            Toast.makeText(this, R.string.toast_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvMonth = findViewById(R.id.tvDetailMonth);
        tvUnits = findViewById(R.id.tvDetailUnits);
        tvTotal = findViewById(R.id.tvDetailTotal);
        tvRebate = findViewById(R.id.tvDetailRebate);
        tvFinal = findViewById(R.id.tvDetailFinal);
        actionRow = findViewById(R.id.actionRow);
        editCard = findViewById(R.id.editCard);
        spEditMonth = findViewById(R.id.spEditMonth);
        etEditUnits = findViewById(R.id.etEditUnits);
        sbEditRebate = findViewById(R.id.sbEditRebate);
        tvEditRebateValue = findViewById(R.id.tvEditRebateValue);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEditMonth.setAdapter(monthAdapter);

        sbEditRebate.setMax(UiUtils.REBATE_SEEKBAR_MAX);
        sbEditRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvEditRebateValue.setText(getString(R.string.rebate_value,
                        UiUtils.rebateFromProgress(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnSave = findViewById(R.id.btnSaveChanges);
        Button btnCancel = findViewById(R.id.btnCancelEdit);

        btnEdit.setOnClickListener(v -> openEditor());
        btnCancel.setOnClickListener(v -> closeEditor());
        btnSave.setOnClickListener(v -> saveChanges());
        btnDelete.setOnClickListener(v -> confirmDelete());

        showDetails();
    }

    private void showDetails() {
        tvMonth.setText(bill.getMonth());
        tvUnits.setText(getString(R.string.units_format, bill.getUnits()));
        tvTotal.setText(BillCalculator.formatRM(bill.getTotalCharges()));
        tvRebate.setText(getString(R.string.rebate_detail, bill.getRebatePercent(),
                BillCalculator.formatRM(bill.getRebateAmount())));
        tvFinal.setText(BillCalculator.formatRM(bill.getFinalCost()));
    }

    private void openEditor() {
        spEditMonth.setSelection(bill.getMonthIndex());
        etEditUnits.setText(String.valueOf(bill.getUnits()));
        etEditUnits.setError(null);
        int progress = UiUtils.progressFromRebate(bill.getRebatePercent());
        sbEditRebate.setProgress(progress);
        tvEditRebateValue.setText(getString(R.string.rebate_value,
                UiUtils.rebateFromProgress(progress)));
        editCard.setVisibility(View.VISIBLE);
        actionRow.setVisibility(View.GONE);
    }

    private void closeEditor() {
        UiUtils.hideKeyboard(this);
        editCard.setVisibility(View.GONE);
        actionRow.setVisibility(View.VISIBLE);
    }

    private void saveChanges() {
        Integer units = UiUtils.readUnits(this, etEditUnits);
        if (units == null) {
            Toast.makeText(this, R.string.error_units_range, Toast.LENGTH_SHORT).show();
            return;
        }
        double rebate = UiUtils.rebateFromProgress(sbEditRebate.getProgress());
        double total = BillCalculator.calculateTotalCharges(units);

        bill.setMonthIndex(spEditMonth.getSelectedItemPosition());
        bill.setMonth(spEditMonth.getSelectedItem().toString());
        bill.setUnits(units);
        bill.setRebatePercent(rebate);
        bill.setTotalCharges(total);
        bill.setFinalCost(BillCalculator.calculateFinalCost(total, rebate));

        if (db.updateBill(bill) > 0) {
            Toast.makeText(this, R.string.toast_updated, Toast.LENGTH_SHORT).show();
            showDetails();
            closeEditor();
        } else {
            Toast.makeText(this, R.string.toast_save_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, bill.getMonth()))
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    db.deleteBill(bill.getId());
                    Toast.makeText(this, R.string.toast_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

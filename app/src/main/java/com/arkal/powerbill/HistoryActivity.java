package com.arkal.powerbill;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/** Lists saved bills (Month + Final cost). Tapping a row opens the detail page. */
public class HistoryActivity extends Activity {

    private DatabaseHelper db;
    private ListView listBills;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);
        listBills = findViewById(R.id.listBills);
        tvCount = findViewById(R.id.tvCount);
        listBills.setEmptyView(findViewById(R.id.tvEmpty));

        listBills.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_BILL_ID, id);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload every time so edits and deletes are reflected immediately
        List<Bill> bills = db.getAllBills();
        listBills.setAdapter(new BillAdapter(this, bills));
        tvCount.setText(getString(R.string.history_count, bills.size()));
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

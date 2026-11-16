package com.arkal.powerbill;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** Local (offline) SQLite database that stores every calculated bill. */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "power_bill.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_BILLS = "bills";
    public static final String COL_ID = "id";
    public static final String COL_MONTH_INDEX = "month_index";
    public static final String COL_MONTH = "month";
    public static final String COL_UNITS = "units";
    public static final String COL_REBATE = "rebate_percent";
    public static final String COL_TOTAL = "total_charges";
    public static final String COL_FINAL = "final_cost";
    public static final String COL_CREATED = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BILLS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MONTH_INDEX + " INTEGER NOT NULL, "
                + COL_MONTH + " TEXT NOT NULL, "
                + COL_UNITS + " INTEGER NOT NULL, "
                + COL_REBATE + " REAL NOT NULL, "
                + COL_TOTAL + " REAL NOT NULL, "
                + COL_FINAL + " REAL NOT NULL, "
                + COL_CREATED + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }

    private ContentValues toValues(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(COL_MONTH_INDEX, bill.getMonthIndex());
        values.put(COL_MONTH, bill.getMonth());
        values.put(COL_UNITS, bill.getUnits());
        values.put(COL_REBATE, bill.getRebatePercent());
        values.put(COL_TOTAL, bill.getTotalCharges());
        values.put(COL_FINAL, bill.getFinalCost());
        return values;
    }

    private Bill fromCursor(Cursor c) {
        return new Bill(
                c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                c.getInt(c.getColumnIndexOrThrow(COL_MONTH_INDEX)),
                c.getString(c.getColumnIndexOrThrow(COL_MONTH)),
                c.getInt(c.getColumnIndexOrThrow(COL_UNITS)),
                c.getDouble(c.getColumnIndexOrThrow(COL_REBATE)),
                c.getDouble(c.getColumnIndexOrThrow(COL_TOTAL)),
                c.getDouble(c.getColumnIndexOrThrow(COL_FINAL)));
    }

    /** CREATE: returns the new row id, or -1 on failure. */
    public long insertBill(Bill bill) {
        ContentValues values = toValues(bill);
        values.put(COL_CREATED, System.currentTimeMillis());
        long id = getWritableDatabase().insert(TABLE_BILLS, null, values);
        bill.setId(id);
        return id;
    }

    /** READ all, ordered January to December (newest first within a month). */
    public List<Bill> getAllBills() {
        List<Bill> bills = new ArrayList<>();
        try (Cursor c = getReadableDatabase().query(TABLE_BILLS, null, null, null,
                null, null, COL_MONTH_INDEX + " ASC, " + COL_CREATED + " DESC")) {
            while (c.moveToNext()) {
                bills.add(fromCursor(c));
            }
        }
        return bills;
    }

    /** READ one. Returns null if not found. */
    public Bill getBillById(long id) {
        try (Cursor c = getReadableDatabase().query(TABLE_BILLS, null,
                COL_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null)) {
            if (c.moveToFirst()) {
                return fromCursor(c);
            }
        }
        return null;
    }

    /** UPDATE: returns number of rows changed. */
    public int updateBill(Bill bill) {
        return getWritableDatabase().update(TABLE_BILLS, toValues(bill),
                COL_ID + " = ?", new String[]{String.valueOf(bill.getId())});
    }

    /** DELETE: returns number of rows removed. */
    public int deleteBill(long id) {
        return getWritableDatabase().delete(TABLE_BILLS,
                COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}

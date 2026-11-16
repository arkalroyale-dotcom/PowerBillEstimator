package com.arkal.powerbill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/** ListView adapter: shows Month and Final cost only. */
public class BillAdapter extends ArrayAdapter<Bill> {

    private final LayoutInflater inflater;

    public BillAdapter(Context context, List<Bill> bills) {
        super(context, 0, bills);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        Bill bill = getItem(position);
        return bill != null ? bill.getId() : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_bill, parent, false);
            holder = new ViewHolder();
            holder.tvMonth = convertView.findViewById(R.id.tvItemMonth);
            holder.tvFinal = convertView.findViewById(R.id.tvItemFinal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Bill bill = getItem(position);
        if (bill != null) {
            holder.tvMonth.setText(bill.getMonth());
            holder.tvFinal.setText(BillCalculator.formatRM(bill.getFinalCost()));
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tvMonth;
        TextView tvFinal;
    }
}

package com.feup.ei12078.shopandpay.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feup.ei12078.shopandpay.R;
import com.feup.ei12078.shopandpay.data_classes.Invoice;

import java.util.List;

/**
 * Created by andremachado on 09/11/2017.
 */

public class InvoiceListItemAdapter extends BaseAdapter {

    private final List<Invoice> invoices;
    private final Activity act;


    public InvoiceListItemAdapter(List<Invoice> invoices, Activity act) {
        this.invoices = invoices;
        this.act = act;
    }


    @Override
    public int getCount() {
        return invoices.size();
    }

    @Override
    public Object getItem(int position) {
        return invoices.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.invoice_list_item, parent, false);
        final Invoice invoice = invoices.get(position);

        TextView id = (TextView) view.findViewById(R.id.invoice_item_id);

        TextView year = (TextView) view.findViewById(R.id.invoice_item_year);
        TextView month = (TextView) view.findViewById(R.id.invoice_item_month);
        TextView day = (TextView) view.findViewById(R.id.invoice_item_day);

        TextView hour = (TextView) view.findViewById(R.id.invoice_item_hour);
        TextView minutes = (TextView) view.findViewById(R.id.invoice_item_minute);

        TextView price = (TextView) view.findViewById(R.id.invoice_item_price);

        id.setText(invoice.getId());
        year.setText(invoice.getYear());
        month.setText(invoice.getMonth());
        day.setText(invoice.getDay());
        hour.setText(invoice.getHour());
        minutes.setText(invoice.getMinute());
        price.setText(invoice.getPrice() + " €");


        return view;

    }
}
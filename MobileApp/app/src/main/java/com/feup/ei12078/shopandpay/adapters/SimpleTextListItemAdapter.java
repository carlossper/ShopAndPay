package com.feup.ei12078.shopandpay.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feup.ei12078.shopandpay.R;
import com.feup.ei12078.shopandpay.data_classes.Category;

import java.util.List;

/**
 * Created by andremachado on 11/11/2017.
 */

public class SimpleTextListItemAdapter extends BaseAdapter{
    private final List<Category> cats;
    private final Activity act;


    public SimpleTextListItemAdapter(List<Category> cats, Activity act) {
        this.cats = cats;
        this.act = act;
    }


    @Override
    public int getCount() {
        return cats.size();
    }

    @Override
    public String getItem(int position) {
        return cats.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.simple_text_list_item, parent, false);
        final String cat = cats.get(position).getName();

        TextView catView = (TextView) view.findViewById(R.id.simple_text_list_item_text);

        catView.setText(cat);

        return view;

    }
}

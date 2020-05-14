package com.example.groupup;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Extend_SpinnerAdapter extends ArrayAdapter {
    private SparseBooleanArray checked = new SparseBooleanArray();
    private SparseArray<String> array;
    private TextView titleView;
    Extend_SpinnerAdapter(@NonNull Context context, SparseArray<String> array, String title) {
        super(context, 0);
        this.array = array;
        titleView = new TextView(context);
        titleView.setText(title);
        titleView.setPadding(10, 3, 10, 3);
        titleView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    void setCheckedArray(SparseBooleanArray checked){
        this.checked = checked;
    }

    Integer getCheckedKeys(){
        Integer integer = 0;
        if(checked.size() > 0){
            integer = array.keyAt(checked.keyAt(0));
            for(int index = 1; index < checked.size(); index++){
                integer = integer | array.keyAt(checked.keyAt(index));
            }
        }
        return integer;
    }

    String getCheckedValues(){
        StringBuilder string = new StringBuilder();
        if(checked.size() > 0){
            for(int index = 0; index < checked.size(); index++){
                if (index != checked.size()-1){
                    string.append(array.valueAt(checked.keyAt(index)).concat(","));
                }else {
                    string.append(array.valueAt(checked.keyAt(index)).concat(""));
                }

            }
            titleView.setText(string.toString());
        }
        return string.toString();
    }

    class ViewHolder {
        CheckBox checkBox;
        TextView title;
    }

    private void handleDropDownHolder(final int position, ViewHolder holder){
        holder.title.setText(getItem(position));
        holder.checkBox.setChecked(checked.get(position, false));
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View checkBox) {
                setChecked(checkBox, position);
                getCheckedValues();
            }
        });
    }

    void setChecked(View view, int position){
        if(checked.get(position, false)){
            ((CheckBox) view).setChecked(false);
            checked.delete(position);
        } else {
            ((CheckBox) view).setChecked(true);
            checked.put(position, true);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return array.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return array.valueAt(position);
    }

    class Item extends LinearLayout {
        ViewHolder holder;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Item(Context context) {
            super(context);
            setLayoutParams(params);
            setOrientation(LinearLayout.HORIZONTAL);
            setBackgroundColor(Color.WHITE);
            CheckBox checkBox = new CheckBox(context);
            TextView title = new TextView(context);
            title.setLayoutParams(params);
            holder = new ViewHolder();
            holder.title = title;
            holder.checkBox = checkBox;
            addView(checkBox);
            addView(title);
        }

        ViewHolder getHolder(){
            return holder;
        }
    }

    @Override
    public @NonNull
    View getDropDownView(int position, View view, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        if(view == null){
            view = new Item(getContext());
        }
        handleDropDownHolder(position, ((Item) view).getHolder());
        return view;
    }

    @Override
    public @NonNull
    View getView(int position, View view, @NonNull ViewGroup parent) {
        // TODO Auto-generated method stub
        return titleView;
    }
}
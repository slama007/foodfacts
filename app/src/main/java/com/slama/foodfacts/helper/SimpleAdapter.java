package com.slama.foodfacts.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.slama.foodfacts.R;
import com.slama.foodfacts.model.BarCodeList;
import com.slama.foodfacts.scanLib.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleAdapter extends BaseAdapter {

  private ArrayList<BarCodeList> currentBarCode ;

  private final LayoutInflater inflater;

  public SimpleAdapter(Context context) {
    inflater = LayoutInflater.from(context);


    currentBarCode= util.getBareCode(context,currentBarCode);
  }



  @Override public int getCount() {
    return currentBarCode.size();
  }

  @Override public BarCodeList getItem(int position) {
    return currentBarCode.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = inflater.inflate(R.layout.simple_list_item, parent, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }

    String barcode = getItem(position).getBarCode();
    String time = getItem(position).getTime();
    holder.barcode.setText(String.format(Locale.getDefault(),  barcode));
    holder.time.setText(String.format(Locale.getDefault(),  time));


    return view;
  }

  static final class ViewHolder {
    @BindView(R.id.barcode) TextView barcode;
    @BindView(R.id.time) TextView time;


    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}

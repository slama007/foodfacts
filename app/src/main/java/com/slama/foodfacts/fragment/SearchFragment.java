package com.slama.foodfacts.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.slama.foodfacts.R;
import com.slama.foodfacts.SimpleScannerActivity;
import com.slama.foodfacts.app.AppController;
import com.slama.foodfacts.helper.util;
import com.slama.foodfacts.model.BarCodeList;
import com.slama.foodfacts.model.Product;
import com.slama.foodfacts.scanLib.ObjectSerializer;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Dell on 14/11/2017.
 */

public class SearchFragment extends Fragment {



    private String url="https://world.openfoodfacts.org/api/v0/product/";
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private ArrayList<BarCodeList> currentBarCode;
    private ArrayList<Product> currentProduct;

    private Class<?> mClss;
    public static Bus bus = new Bus(ThreadEnforcer.MAIN);


    @BindView(R.id.scan)
    Button scan;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.barcode)
    EditText barCode;

    @OnClick(R.id.scan) void scan() {

        launchActivity(SimpleScannerActivity.class);


    }

    @SuppressLint("WrongConstant")
    @OnClick(R.id.search) void search() {
        DateFormat df = new DateFormat();
        String date = String.valueOf(DateFormat.format("yyyy-MM-dd HH:mm:ss ", new Date()));
        String bareText=barCode.getText().toString();
        String url1=url+bareText+".json";

        if(util.isOnline(getActivity())) {

            //if barCode EditText is not empty add barcode to SharedPreferences
            if (!bareText.replaceAll(" ", "").equals("")) {
                util.getProductWebService(url1,getActivity(),getLayoutInflater(),currentProduct);
                util.addBarCode(getActivity(),new BarCodeList(bareText, date),currentBarCode);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nobarcode), 2000).show();
            }
        }else{

            //if barCode EditText is not empty add barcode to SharedPreferences
            if (!bareText.replaceAll(" ", "").equals("")) {
                if(currentProduct.size()==0){
                    util.showProductNotExist(getActivity());
                }else{
                    for(int j=0;j<currentProduct.size();j++) {
                        if (currentProduct.get(j).getCode().equals(bareText)) {
                            util.showProductInfo(currentProduct.get(j),getLayoutInflater(),getActivity());
                            break;
                        } else if (!currentProduct.get(j).getCode().equals(bareText) && j == (currentProduct.size() - 1)) {
                            util.showProductNotExist(getActivity());
                            break;
                        }
                    }
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.nobarcode), 2000).show();
            }

        }

    }




    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_fragment, container, false);


        ButterKnife.bind(SearchFragment.this,view);

        //register bus
        bus.register(this);


        currentBarCode= util.getBareCode(getActivity(),currentBarCode);

        currentProduct= util.getProduct(getActivity(),currentProduct);

        return view;
    }


    //accept permission before opening the activity
    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getActivity(), clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(getActivity(), mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.camerapermission), Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    //subscribe on the bus and display all that is sent
    @Subscribe
    public void getMessage(String s) {
        barCode.setText(s);
    }




}

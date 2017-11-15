package com.slama.foodfacts.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slama.foodfacts.R;
import com.slama.foodfacts.helper.SimpleAdapter;
import com.slama.foodfacts.helper.util;
import com.slama.foodfacts.model.BarCodeList;
import com.slama.foodfacts.model.Product;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Dell on 14/11/2017.
 */

public class HistoryFragment extends Fragment {

    private SimpleAdapter adapter;
    @BindView(R.id.list_of_bar_code)
    ListView listOfBarCode;
    private ArrayList<BarCodeList> currentBarCode ;
    private ArrayList<Product> currentProduct ;



    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.history_fragment, container, false);
        ButterKnife.bind(HistoryFragment.this,view);


        // show all list of history bar code
        adapter = new SimpleAdapter(getActivity());

        currentBarCode= util.getBareCode(getActivity(),currentBarCode);

        currentProduct= util.getProduct(getActivity(),currentProduct);

        listOfBarCode.setAdapter(adapter);

        listOfBarCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentProduct.size()==0){

                    util.showProductNotExist(getActivity());

                }else{
                       for(int j=0;j<currentProduct.size();j++) {
                           if (currentProduct.get(j).getCode().equals(currentBarCode.get(i).getBarCode())) {
                               util.showProductInfo(currentProduct.get(j),getLayoutInflater(),getActivity());
                               break;
                           } else if (!currentProduct.get(j).getCode().equals(currentBarCode.get(i).getBarCode()) && j == (currentProduct.size() - 1)) {
                               util.showProductNotExist(getActivity());
                               break;
                           }
                       }
               }
            }
        });


        return view;
    }



}

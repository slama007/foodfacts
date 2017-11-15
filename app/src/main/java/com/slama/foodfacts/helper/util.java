package com.slama.foodfacts.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.slama.foodfacts.R;
import com.slama.foodfacts.app.AppController;
import com.slama.foodfacts.model.BarCodeList;
import com.slama.foodfacts.model.Product;
import com.slama.foodfacts.scanLib.ObjectSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dell on 14/11/2017.
 */

public class util {

    private static final String SHARED_PREFS_FILE="SAVED";
    private static final String BAR="OBJECT";
    private static final String PRODUCT="PRODUCT";
    private static String tag_json_obj = "json_obj_req";
    private static Product product;


    // load barcode from preference
    public static ArrayList<BarCodeList> getBareCode(Context context, ArrayList<BarCodeList> currentBarCode) {
        if (null == currentBarCode) {
            currentBarCode = new ArrayList<BarCodeList>();
        }

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);


        try {
            currentBarCode = (ArrayList<BarCodeList>) ObjectSerializer.deserialize(prefs.getString(BAR, ObjectSerializer.serialize(new ArrayList<BarCodeList>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentBarCode;
    }

    // load Product from preference
    public static ArrayList<Product> getProduct(Context context, ArrayList<Product> currentProduct) {

        if (null == currentProduct) {
            currentProduct = new ArrayList<Product>();
        }

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);

        try {
            currentProduct = (ArrayList<Product>) ObjectSerializer.deserialize(prefs.getString(PRODUCT, ObjectSerializer.serialize(new ArrayList<Product>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentProduct;
    }




    // save the barcode list to preference
    public static void addBarCode(Context context, BarCodeList barCode, ArrayList<BarCodeList> currentBarCode) {
        if (currentBarCode == null) {
            currentBarCode = new ArrayList<BarCodeList>();
        }
        currentBarCode.add(barCode);


        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(BAR,  ObjectSerializer.serialize(currentBarCode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }


    // save the product list to preference
    public static void addProduct(Context context, Product product, ArrayList<Product> currentProduct) {
        if (currentProduct == null) {
            currentProduct = new ArrayList<Product>();
        }
        currentProduct.add(product);


        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString(PRODUCT,  ObjectSerializer.serialize(currentProduct));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }


    //test if I have an internet connection
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }




    //Get Product information from Web Service
    public static void getProductWebService(String url1, final Context context, final LayoutInflater inflater, final ArrayList<Product> currentProduct) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            product = new Product(response.getString("code"),
                                    response.getJSONObject("product").getString("product_name"),
                                    response.getJSONObject("product").getJSONObject("selected_images").getJSONObject("front").getJSONObject("display").getString("fr"),
                                    response.getJSONObject("product").getString("ingredients_text_with_allergens"),
                                    String.valueOf(Float.parseFloat(response.getJSONObject("product").getJSONObject("nutriments").getString("energy")) / 4.1818));

                            showProductInfo(product,inflater,context);
                            addProduct(context,product,currentProduct);
                        } catch (JSONException e) {
                            try {
                                product = new Product(response.getString("code"),
                                        response.getJSONObject("product").getString("product_name"),
                                        response.getJSONObject("product").getJSONObject("selected_images").getJSONObject("front").getJSONObject("display").getString("en"),
                                        response.getJSONObject("product").getString("ingredients_text_with_allergens"),
                                        String.valueOf(Float.parseFloat(response.getJSONObject("product").getJSONObject("nutriments").getString("energy")) / 4.1818));

                                showProductInfo(product,inflater,context);
                                addProduct(context,product,currentProduct);
                            } catch (JSONException e2) {
                                showProductNotExist(context);
                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    //show Alert Message if product is not exist
    public static void showProductNotExist(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.productinfo));
        alertDialog.setMessage(context.getResources().getString(R.string.noproduct));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    //show Alert Message with product information if product exist
    public static void showProductInfo(Product product, LayoutInflater inflater, Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView energy = dialogView.findViewById(R.id.energy);
        final TextView ingredian = dialogView.findViewById(R.id.ingedian);
        final TextView name = dialogView.findViewById(R.id.name);
        final ImageView image = dialogView.findViewById(R.id.image);

        dialogBuilder.setTitle(context.getResources().getString(R.string.productinfo));
        energy.setText(context.getResources().getString(R.string.energy)+" "+(int)Float.parseFloat(product.getEnergy())+"kcal");
        name.setText(product.getName());
        ingredian.setText(context.getResources().getString(R.string.ingredian)+" "+ Html.fromHtml(product.getIngredients()));

        Glide.with(context).load(product.getPicture()).fitCenter().into(image);

        dialogBuilder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


}

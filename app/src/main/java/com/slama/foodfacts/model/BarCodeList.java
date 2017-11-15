package com.slama.foodfacts.model;

import java.io.Serializable;

/**
 * Created by Dell on 14/11/2017.
 */

public class BarCodeList  implements Serializable {


    private String barCode;
    private String time;

    public BarCodeList(String barCode, String time) {
        this.barCode = barCode;
        this.time = time;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

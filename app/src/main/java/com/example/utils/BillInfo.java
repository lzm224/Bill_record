package com.example.utils;

public class BillInfo {
    public static double income;
    public static double expend;
    public int xuhao;
    public String date;
    public int month;
    public int type;
    public String desc; //描述型文字，描述账单用途
    public double amount;
    public String remark;

    public long rowid;
    public String create_time;
    public String update_time;
    public BillInfo() {
        rowid = 0L;
        xuhao = 0;
        date = "";
        month = 0;
        type = 0;
        amount = 0.0;
        desc = "";
        create_time = "";
        update_time = "";
        remark = "";
    }
}

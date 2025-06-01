package com.example.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.bill_record.R;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment{
    private static  final String TAG = "BillFragment";
    protected View mView;//一个视图对象
    protected Context mContext;//上下文对象
    private int mMonth;//当前选择的月份用在数据库查询
    private ListView lv_bill;
    private ArrayList<BillInfo> mBillList = new ArrayList<>();//泛型擦除


    public static Fragment newInstance(int month) {
        //获取一个碎片的示例让pagerAdapter来装配
        BillFragment fragment = new BillFragment();
        Bundle bundle = new Bundle();
        //通过包裹塞给碎片让碎片适配器适用数据
        bundle.putInt("momth",month);
        fragment.setArguments(bundle);
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();//获取活动页面上下文
        if (getArguments() != null)
            mMonth =getArguments().getInt("month");
        mView = inflater.inflate(R.layout.fragment_bill,container,false);
        return mView;
    }
    public void onStart(){
        super.onStart();
        BillDBHelper helper = BillDBHelper.getInstance(mContext);
        mBillList = helper.queryByMonth(mMonth);
        if(mBillList != null && mBillList.size() >0){
            double income = 0, expend = 0;
            for(BillInfo bill:mBillList){
                income += BillInfo.income;
                expend += BillInfo.expend;
            }

            BillInfo sum = new BillInfo();
            sum.date = "合计",sum.desc =String.format("收入%f\n指出%f元",income,expend);
            sum.remark = String.format("净支出%f",income-expend);
            mBillList.add(sum);
        }
        BillListAdapter listAdapter = new BillListAdapter(mContext, mBillList);
        lv_bill.setAdapter((listAdapter);
        lv_bill.setOnItemClickListener(listAdapter);
        lv_bill.setOnItemLongClickListener(listAdapter);

    }
}

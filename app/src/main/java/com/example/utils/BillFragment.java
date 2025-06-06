package com.example.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.bill_record.R;

import java.util.ArrayList;
import java.util.List;

public class BillFragment extends Fragment{
    private static  final String TAG = "BillFragment";
    protected View mView;//一个视图对象
    protected Context mContext;//上下文对象
    private int mMonth;//当前选择的月份用在数据库查询，传入的实际是标题上面的月份，就是页面的position
    private ListView lv_bill;
    private List<BillInfo> mBillList = new ArrayList<BillInfo>();//泛型擦除


    public static Fragment newInstance(int month) {
        //获取一个碎片的示例让pagerAdapter来装配
        BillFragment fragment = new BillFragment();
        Bundle bundle = new Bundle();
        //通过包裹塞给碎片让碎片适配器适用数据
        bundle.putInt("month",month);
        fragment.setArguments(bundle);//碎片构造时候用
        return fragment;
    }


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();//获取活动页面上下文
        if (getArguments() != null)
            mMonth =getArguments().getInt("month",1);
        mView = inflater.inflate(R.layout.bill_fragment,container,false);
        //创建出碎片视图
        lv_bill = mView.findViewById(R.id.lv_bill);
        lv_bill.setVisibility(View.VISIBLE);//设置列表视图可见
        return mView;
    }
    public void onStart(){
        super.onStart();
        BillDBHelper helper = BillDBHelper.getInstance(mContext);
        mBillList = helper.queryByMonth(mMonth);
        //得到传递给列表适配器的数据序列
        if(mBillList != null && mBillList.size() >0){
            double income = 0, expend = 0;
            for(BillInfo bill:mBillList){
                if(bill.type == 0)
                    income += bill.amount;
                else
                    expend += bill.amount;
            }

            BillInfo sum = new BillInfo();
            sum.date = "合计";
            sum.descb =String.format("收入%f\n支出%f元",income,expend);
            sum.remark = String.format("净支出%f",expend-income);
            mBillList.add(sum);
        }

        BillListAdapter listAdapter = new BillListAdapter(mContext, mBillList);
        lv_bill.setAdapter(listAdapter);//fragment中的列表视图设置一个装填器
        lv_bill.setOnItemClickListener(listAdapter);//列表视图设置事件
        lv_bill.setOnItemLongClickListener(listAdapter);
    }

}

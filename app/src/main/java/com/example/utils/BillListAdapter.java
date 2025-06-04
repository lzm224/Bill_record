package com.example.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bill_record.BillAddActivity;
import com.example.bill_record.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    private static final String TAG = "BillListAdapter";
    private Context mContext;
    private List<BillInfo> mBillList = new ArrayList<BillInfo>();

    public BillListAdapter(Context mContext, List<BillInfo> mBillList) {
        super();
        this.mContext = mContext;
        this.mBillList = mBillList;

    }

    @Override
    public int getCount() {
        return mBillList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBillList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bill_item, null);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.tv_amount = convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        BillInfo bill = mBillList.get(position);
        holder.tv_date.setText(bill.date);
        holder.tv_desc.setText((bill.descb));
        if (bill.date.equals("合计"))
            holder.tv_amount.setText(bill.remark);
        else
            holder.tv_amount.setText(String.format("%s%f元", bill.type == 0 ? "收入:" : "支出:", bill.amount));
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position >= mBillList.size() - 1){
            //小计行不响应点击事件
            return;
        }
        Log.d(TAG,"onItemCLick position = " +position);
        BillInfo bill = mBillList.get(position);
        Intent intent = new Intent(mContext, BillAddActivity.class);
        intent.putExtra("xuhao", bill.xuhao);//点击就会编辑账单，跳转到账单添加界面
        mContext.startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(position >= mBillList.size()-1) return true; //合计行不响应事件
        Log.d(TAG,"onItemLongClick position = "+position);
        BillInfo bill = mBillList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String desc = String.format("删除账单？\n%s %s%d %s",bill.date,
                bill.type==0?"收入":"支出",(int)bill.amount,bill.descb);
        builder.setMessage(desc);
        builder.setPositiveButton("Yes", (DialogInterface dialog, int which)->{
            deleteBill(position);
        });//函数式接口。实现监听器。
        builder.setNegativeButton("No", null);
        builder.create().show();
        return true;
    }



    private void deleteBill(int position){
        BillInfo bill = mBillList.get(position);
        mBillList.remove((position));//从数据序列移除指定位置的元素
        BillInfo sum = mBillList.get(mBillList.size()-1);
        mBillList.remove(mBillList.size() - 1);

        //分离出bill.descb 中的数值，更新支出和收入
        ArrayList<Double> amounts = getSumAmount(sum.descb);
        double sum_expand = amounts.get(1);
        double sum_income = amounts.get(0);
        if(bill.type == 0) {sum_income -= bill.amount;}
        else sum_expand -= bill.amount;
        amounts = getSumAmount(sum.remark);
        sum.descb = String.format("收入%f\n支出%f元", sum_income, sum_expand);
        sum.remark = String.format("净支出%f", sum_expand-sum_income);
        mBillList.add(sum);//同时更新合计上面的收入，支出和净支出
        notifyDataSetChanged();//告诉适配器数据放生了变化

        BillDBHelper helper = BillDBHelper.getInstance(mContext);
        helper.delete(bill.xuhao);
//        if(helper.query("").size()==0)
//        {
//            mBillList.remove(mBillList.size() - 1);
//            notifyDataSetChanged();
//        }
//

    }
    //获取字符串中的小数
    private ArrayList<Double> getSumAmount(String input) {
        String regex = "[-+]?\\d*\\.?\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        ArrayList<Double> amounts = new ArrayList<>();
        while(matcher.find()) {
            String match = matcher.group();
            double amount = Double.parseDouble(match);

            amounts.add(amount);

        }
        return amounts;
    }
    public  class ViewHolder {
        public TextView tv_date;
        public TextView tv_desc;
        public TextView tv_amount;
    }
}



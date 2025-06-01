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

public class BillListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    private static final String TAG = "BillListAdapter";
    private Context mContext;
    private List<BillInfo> mBillList = new ArrayList<BillInfo>();

    public BillListAdapter(Context mContext, List<BillInfo> mBillList) {
        mContext = mContext;
        mBillList = mBillList;

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

        } else holder = (ViewHolder) convertView.getTag();

        BillInfo bill = mBillList.get(position);
        holder.tv_date.setText(bill.date);
        holder.tv_desc.setText((bill.descb));
        if (bill.date.equals("合计"))
            holder.tv_amount.setText(bill.remark);
        else
            holder.tv_amount.setText(String.format("%s%f元", bill.type == 0 ? "income:" : "expand:", bill.amount));

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
        String desc = String.format("删除账单？\n%s% s%d %s",bill.date,
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
        notifyDataSetChanged();//告诉适配器数据放生了变化
        BillDBHelper helper = BillDBHelper.getInstance(mContext);
        helper.delete(bill.xuhao);

    }

    public final class ViewHolder {
        public TextView tv_date;
        public TextView tv_desc;
        public TextView tv_amount;
    }
}



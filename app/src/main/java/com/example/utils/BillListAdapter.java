package com.example.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bill,null);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.tv_amount = convertView.findViewById(R.id.tv_amount);

        }
        else holder = (ViewHolder)convertView.getTag();

        BillInfo bill = mBillList.get(position);
        holder.tv_date.setText(bill.date);
        holder.tv_desc.setText((bill.desc));
        if(bill.date.equals("合计"))
            holder.tv_amount.setText(bill.remark);
        else
            holder.tv_amount.setText(String.format("%s%f元",bill.type==0?"income:":"expand:", bill.amount));

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    public final class ViewHolder{
        public TextView tv_date;
        public TextView tv_desc;
        public TextView tv_amount;
    }
}



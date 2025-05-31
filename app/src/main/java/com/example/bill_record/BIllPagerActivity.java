package com.example.bill_record;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import java.util.Calendar;

public class BIllPagerActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private TextView tv_month = findViewById(R.id.tv_month);
    private ViewPager vp_bill = findViewById(R.id.vp_bill);
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_pager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView tv_title = findViewById(R.id.tv_title),
                tv_option = findViewById(R.id.tv_option);
        tv_option.setText("添加账单");
        tv_title.setText("账单列表");
        tv_option.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tv_month.setText("DateUtil.getMonth(calendar");
        tv_month.setOnClickListener(this);
        initViewPager();
    }

    private void initViewPager() {
        PagerAdapter adapter = new BillPagerAdapter(getSupportFragmentManager(),
                calendar.get(Calendar.YEAR));
        PagerTabStrip pts_bill = findViewById(R.id.pts_bill);
        pts_bill.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        vp_bill.setAdapter(adapter);
        vp_bill.setCurrentItem(calendar.get(Calendar.MONTH));
        vp_bill.addOnPageChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) finish();
        else if (v.getId() == R.id.tv_option) {
            var intent = new Intent(this, BillAddActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_month) {
            DatePickerDialog dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }

    public void onDateSet(DatePicker View, int year, int mont, int date) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, date);
        tv_month.setText(DateUtil.getMonth(calendar));
        vp_bill.setCurrentitem(month);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        calendar.set(Calendar.MONTH, position);
        tv_month.setText(DateUtil.getMonth(calendar));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

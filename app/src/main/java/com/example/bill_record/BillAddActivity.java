
package com.example.bill_record;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.utils.BillDBHelper;
import com.example.utils.BillInfo;
import com.example.utils.DateUtil;
import com.example.utils.ViewUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BillAddActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, DatePickerDialog.OnDateSetListener {
    //params
    private final static String TAG = "BillAddActivity";
    private TextView tv_date;
    private RadioButton rb_income, rb_expand;
    private EditText et_desc, et_amount;
    private int mBillType = 1; //账单类型
    private int xuhao;//是一个临时变量，用来存储记录的id在从列表中返回时候
    private Calendar calendar = Calendar.getInstance();//获取日历实例
    private BillDBHelper mBillHelper;//数据库操作对象，不使用线程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bill_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //得到组件对象
        tv_date = findViewById(R.id.tv_date);
        rb_income = findViewById(R.id.rb_income);
        rb_expand = findViewById(R.id.rb_expand);
        et_desc = findViewById(R.id.et_desc);
        et_amount = findViewById(R.id.et_amount);


        //对noactionbar（顶头自定义的导航栏）进行文本设置和返回动作的设置
        TextView tv_title = findViewById(R.id.tv_title);
        TextView tv_option = findViewById(R.id.tv_option);
        tv_title.setText("请填写账单");
        tv_option.setText("查看账单列表");
        tv_option.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        RadioGroup rg_type = findViewById(R.id.rg_type);
        rg_type.setOnCheckedChangeListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //通过触发事件的组件id分发事件
        if (v.getId() == R.id.iv_back) finish();//关闭当前页面，模拟退出动作
        else if (v.getId() == R.id.tv_option) {
            Intent intent = new Intent(this, BillPagerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //防止重复跳转页面
            startActivity(intent);
        }
        else if (v.getId() == R.id.tv_date) {
            //选中了日期文本框，唤起一个日期选择器
            var dialog = new DatePickerDialog(this, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }
        else if (v.getId() == R.id.btn_save) {
            saveBill();
        } else ;

    }

    public void onDateSet(DatePicker d,
                          int year, int month, int date) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, date);
        tv_date.setText(DateUtil.getDate(calendar));
    }

    private void saveBill() {
        ViewUtil.hideOneInputMethod(this, et_amount);
        BillInfo bill = new BillInfo();
        bill.xuhao = xuhao;
        bill.date = tv_date.getText().toString();
        bill.month = 100 * calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH));
        //month映射成为一个包含所在年份的唯一值
        bill.type = mBillType;
        bill.descb = et_desc.getText().toString();
        bill.amount = Double.parseDouble(et_amount.getText().toString());
        mBillHelper.save(bill);
        Toast.makeText(this, "账单添加成功", Toast.LENGTH_LONG);
        //页面重置
        reset();
    }

    private void reset() {
        calendar = Calendar.getInstance();
        et_amount.setText("");
        et_desc.setText("");
        tv_date.setText(DateUtil.getDate(calendar));
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mBillType = (checkedId == R.id.rb_expand) ? 1 : 0;
    }

    protected void onResume() {
        super.onResume();
        xuhao = getIntent().getIntExtra("xuhao", -1);
        mBillHelper = BillDBHelper.getInstance(this);
        if (xuhao != -1) {
            List<BillInfo> bill_list = (List<BillInfo>) mBillHelper.queryById(xuhao);
            if (bill_list.size() > 0) {
                BillInfo bill = bill_list.get(0);
                Date date = DateUtil.formatString("bill.date");
                calendar.set(Calendar.YEAR, date.getYear());
                calendar.set(Calendar.MONTH, date.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
                if (bill.type == 0) rb_income.setChecked(true);
                else rb_expand.setChecked(true);
                et_desc.setText(bill.descb);
                et_amount.setText("" + bill.amount);

            }

        }
        tv_date.setText(DateUtil.getDate(calendar));
    }
}
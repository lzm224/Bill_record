package com.example.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BillPagerAdapter extends FragmentPagerAdapter{
    private int mYear;//现在账单所在的年份，好通过数据库查询后生成页面适配器要处理的数据序列

    public BillPagerAdapter(FragmentManager fm, int year) {
       super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
       mYear = year;
    }

    @Override
    public int getCount() {
        return 12;//一年12个月，则要生成12个滑动页面,每个页面就有一个碎片
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    public Fragment getItem(int position) {
        return BillFragment.newInstance(mYear * 100 + (position + 1));

        //获取到指定月份的碎片， 碎片通过BillFragment生成，传入参数类似碎片的id
    }
    public CharSequence getPageTitle(int position){
        return (position +1) + "月份";
    }
}

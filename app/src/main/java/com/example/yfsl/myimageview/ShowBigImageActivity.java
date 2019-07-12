package com.example.yfsl.myimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowBigImageActivity extends FragmentActivity {
    private ViewPager viewPager;
    private TextView textView;
    private ViewInfo viewInfo;
    private List<String> urls;
    private List<Fragment> fragments;

    public static void startShowBigImgAct(Context context,ViewInfo viewInfo,List<String> urls){
        Intent intent = new Intent(context,ShowBigImageActivity.class);
        intent.putExtra("view",viewInfo);
        intent.putStringArrayListExtra("urls", (ArrayList<String>) urls);
        context.startActivity(intent);
    }

    public void getArgus(){
        Intent intent = getIntent();
        if (intent == null) return;
        viewInfo = intent.getParcelableExtra("view");
        urls = intent.getStringArrayListExtra("urls");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);
        getArgus();
        viewPager = findViewById(R.id.viewpager);
        textView = findViewById(R.id.textview);

        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        if (urls != null && !urls.isEmpty()) {
            for (String url : urls){
                fragments.add(ShowBigImageFragment.getInstance(url,viewInfo));
            }
            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),fragments));
            viewPager.setOffscreenPageLimit(urls.size());

            int viewId = viewInfo.getViewId();
            for (int i = 0;i<fragments.size();i++){
                if (viewId == i){
                    viewPager.setCurrentItem(i);
                    textView.setText((i+1)+"/"+urls.size());
                }
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onPageSelected(int i) {
                    Log.e("TAG","当前页面："+i);
                    textView.setText((i+1)+"/"+urls.size());
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }



    }
}

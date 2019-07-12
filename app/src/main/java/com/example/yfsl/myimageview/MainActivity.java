package com.example.yfsl.myimageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        urls = new ArrayList<>();
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_0.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_1.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_2.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_3.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_4.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_5.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_6.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_7.jpg");
        urls.add("http://192.168.1.203:8080/file/PatImagePath_Service/201906/IMG1800204_8.jpg");
        float space = MeasureUtil.dip2px(this,5);
        float margin = MeasureUtil.dip2px(this,5);
        final float itemWidth = (MeasureUtil.getScreenWidth(this) - margin - space * 3)/4;

        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(urls,this,itemWidth);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void itemClick(int position, View view) {
                Log.e("TAG","viewId:"+position);
                /*
                    记录小图的数据（包括尺寸数据和位置数据）
                 */
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                Log.e("TAG","view"+position+"的左上角坐标："+"("+location[0]+","+location[1]+")");
                ViewInfo viewInfo = new ViewInfo();
                viewInfo.setLeft(location[0]);
                viewInfo.setTop(location[1]);
                viewInfo.setWidth(itemWidth);
                viewInfo.setHeight(itemWidth);
                viewInfo.setViewId(position);
                ShowBigImageActivity.startShowBigImgAct(MainActivity.this,viewInfo,urls);
                //加上页面跳转的进场出场alpha动画  解决该死的页面跳转短暂的黑屏问题 出场动画也要加 不然看起挺秃然的
                overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out);
            }
        });
    }

}

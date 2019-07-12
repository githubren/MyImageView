package com.example.yfsl.myimageview;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ShowBigImageFragment extends Fragment implements GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener{
    private ImageView newImageView;
    private RelativeLayout container;
    private String url;
    private ViewInfo viewInfo;
    private GestureDetector gestureDetector;
    private boolean isScale = false;//是否放大
    private boolean isExit = false;

    public static ShowBigImageFragment getInstance(String url,ViewInfo viewInfo){
        ShowBigImageFragment showBigImageFragment = new ShowBigImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        bundle.putParcelable("viewinfo",viewInfo);
        showBigImageFragment.setArguments(bundle);
        return showBigImageFragment;
    }

    private void getArgus(){
        Bundle bundle = getArguments();
        if (bundle == null) return;
        url = bundle.getString("url",null);
        viewInfo = bundle.getParcelable("viewinfo");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_big_img,container,false);
        getArgus();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newImageView = view.findViewById(R.id.newimageview);
        if (url != null && !TextUtils.isEmpty(url)){
            Glide.with(getActivity())
                    .load(url)
                    .error(R.drawable.icon_iv_default)
                    .skipMemoryCache(true)//内存不缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//磁盘不缓存
                    .into(newImageView);
        }

        container = view.findViewById(R.id.container);
        container.setBackgroundColor(Color.BLACK);

        final float statusHeight = MeasureUtil.getStatusBarHeight(getContext()); //Nexus 5X模拟器中 StatusBarHeight（状态栏高度）为63px
        float navigationBarHeight = MeasureUtil.getNavigationBarHeight(getActivity());
        Log.e("TAG","statusHeight:"+statusHeight);
        Log.e("TAG","navigationBarHeight:"+navigationBarHeight);

        /*
            属性动画
            总的来说步骤为以下几点：
            1、在点击小图的时候记录下小图的数据传到展示大图的页面中（数据包括尺寸和位置）
            2、在大图展示页面将信息拿出来 同时获取大图的数据
            3、通过对大小图的数据做运算 得到缩放比、位移值等数据
            4、大图页面的动画为  将大图按缩放比缩到跟小图一样大 再按位移值将缩小后的大图移动到之前小图的位置
            5、设置属性动画 将缩小的图变回原来正常的大小
         */
        newImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                newImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                int[] bigLocation = new int[2];
                newImageView.getLocationOnScreen(bigLocation);//获取大图在屏幕中左上角的位置数据 单位为px
                float bigLeft = bigLocation[0];
                Log.e("TAG","bigLeft："+bigLeft);
                float bigTop = bigLocation[1];
                float bigWidth = newImageView.getWidth();//获取大图的宽度
                Log.e("TAG","bigWidth："+bigWidth);
                float bigHeight = newImageView.getHeight();//获取大图的高度
                float translateX = Math.abs(bigLeft - viewInfo.getLeft());//需要X方向移动的距离  取绝对值
                Log.e("TAG","translateX："+translateX);
                float translateY = Math.abs(bigTop - viewInfo.getTop());//需要Y方向移动的距离  取绝对值
                float scaleWidth = viewInfo.getWidth()/bigWidth;//宽度缩放比
                float scaleHeight = viewInfo.getHeight()/bigHeight;//高度缩放比
                Log.e("TAG","小照片宽度："+viewInfo.getWidth());

                newImageView.setPivotX(0);//设置大图做出变化的基准点的X坐标  就是大图之后的变换都是以此点为基准
                newImageView.setPivotY(0);//设置大图做出变化的基准点的Y坐标
                newImageView.setTranslationX(translateX);//括号中的值表“移动了”而不是“移动到”
                newImageView.setTranslationY(translateY);
                newImageView.setScaleX(scaleWidth);//大图缩放
                newImageView.setScaleY(scaleHeight);
                newImageView.animate()
                        .setDuration(400)
                        .translationX(0)//括号里的值表“移动到”而不是“移动了”
                        .translationY(0)
                        .scaleX(1)//括号中的值 0 表缩放到没有 1 表没有缩放 2 表缩放到两倍
                        .scaleY(1)
                        .setInterpolator(new DecelerateInterpolator());
                return false;
            }
        });

        gestureDetector = new GestureDetector(getContext(),this);
        gestureDetector.setOnDoubleTapListener(this);
        newImageView.setClickable(true);
        newImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }



    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.e("TAG","onSingleTapConfirmed");
        //执行单击事件
        isExit = true;
        if (isExit) {
            exitWithAnim();
        }
        return false;
    }

    private void exitWithAnim() {
        final float left = viewInfo.getLeft();
        final float top = viewInfo.getTop();
        float smallWidth = viewInfo.getWidth();
        float smallHeight = viewInfo.getHeight();
        float bigWidth = newImageView.getWidth();
        float bigHeight = newImageView.getHeight();
        final float scaleWidth = smallWidth/bigWidth;
        final float scaleHeight = smallHeight/bigHeight;
        newImageView.animate()
                .translationX(left)
                .translationY(top)
                .scaleX(scaleWidth)
                .scaleY(scaleHeight)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(400)
                .withEndAction(new Runnable() {//动画结束紧接着跳转页面
                    @Override
                    public void run() {
                        getActivity().finish();
                        getActivity().overridePendingTransition(0,0);
                    }
                });
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.e("TAG","onDoubleTap");
        //执行双击事件
        /*
            得到点击的点的坐标
         */
        float clickX = e.getX();
        float clickY = e.getY();
        if (!isScale) {
            //将点击的点设置为控件缩放的基准点 这样就能实现点击哪儿就放大哪儿
            newImageView.setPivotX(clickX);
            newImageView.setPivotY(clickY);
            newImageView.animate()
                    .scaleX(2)
                    .scaleY(2)
                    .setInterpolator(new DecelerateInterpolator());
            isScale = true;
        }else {
            newImageView.animate().scaleX(1).scaleY(1).setInterpolator(new DecelerateInterpolator());
            isScale = false;
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.e("TAG","onDoubleTapEvent");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.e("TAG","onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.e("TAG","onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.e("TAG","onSingleTapUp");
        return false;
    }

    private boolean isCanTranslate = false;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        isExit = false;
        Log.e("TAG","onScroll");
        //执行滑动事件
//        float oldY = e1.getRawY();
//        float newY = e2.getRawY();
//        float moveY = Math.abs(newY - oldY);
//        float screenHeight = MeasureUtil.getScreenHeight(getContext());
//        float screenWidth = MeasureUtil.getScreenWidth(getContext());
//        if (moveY < screenHeight/4){
//            float scale = 1 - moveY/screenHeight;
//            newImageView.setPivotX(screenWidth/2);
//            newImageView.setPivotY(screenHeight/2);
//            newImageView.setScaleX(scale);
//            newImageView.setScaleY(scale);
//            newImageView.setTranslationX(distanceX);
//            newImageView.setTranslationY(distanceY);
//        }else {
//            isCanTranslate = true;
//        }
//        if (isCanTranslate) {
//            newImageView.animate().translationX(distanceX).translationY(distanceY);
//        }
//
////        newImageView.setTranslationX(distanceX);
////        newImageView.setTranslationY(distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.e("TAG","onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("TAG","onFling");
        float e1Y = e1.getY();
        float e2Y = e2.getY();
        float distanceY = Math.abs(e2Y - e1Y);
        if (distanceY > 100 || velocityY > 200){
            exitWithAnim();
        }
        return false;
    }
}

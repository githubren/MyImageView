package com.example.yfsl.myimageview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> urlList;
    private Context context;
    private float width;

    public RecyclerViewAdapter(List<String> urlList, Context context,float width) {
        this.urlList = urlList;
        this.context = context;
        this.width = width;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_imageview,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((ViewHolder)viewHolder).imageView.getLayoutParams();
        params.width = (int) width;
        params.height = (int) width;
        ((ViewHolder)viewHolder).imageView.requestLayout();
        if (urlList != null && !urlList.isEmpty()) {
            String url = urlList.get(i);
            //照片圆角
            int radius = (int) (Resources.getSystem().getDisplayMetrics().density*3);
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.icon_iv_default)
                    .error(R.drawable.icon_iv_default)
                    .skipMemoryCache(true)//内存不缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//磁盘不缓存
                    .transform(new CenterCrop(),new RoundedCorners(radius))
                    .into(((ViewHolder)viewHolder).imageView);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClick(i,((ViewHolder)viewHolder).imageView);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return urlList == null ? 0 : urlList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void itemClick(int position,View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

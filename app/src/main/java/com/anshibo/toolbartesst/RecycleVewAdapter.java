package com.anshibo.toolbartesst;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author zhaopanlong
 * @createtime：2019/6/26 11:19
 */
public class RecycleVewAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_viewpager, parent, false);
            return new ViewHolderViewPager(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_recycle, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderViewPager){
            ((ViewHolderViewPager) holder).viewPager.setAdapter(new PagerAdapter());
        }else {
        }
    }

    class PagerAdapter extends androidx.viewpager.widget.PagerAdapter{

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TextView textView = new TextView(container.getContext());
            textView.setText("这是第"+position+"页面");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.alignWithParent = true;
            container.addView(textView,layoutParams);
            return textView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class ViewHolderViewPager extends ViewHolder {

        ViewPager viewPager;

        public ViewHolderViewPager(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPager);
        }
    }
}

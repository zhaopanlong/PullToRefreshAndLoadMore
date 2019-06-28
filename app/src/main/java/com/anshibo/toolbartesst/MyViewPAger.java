package com.anshibo.toolbartesst;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * @author zhaopanlong
 * @createtime：2019/6/27 10:50
 */
public class MyViewPAger extends ViewPager {
    float downX = 0;
    float moveX = 0;

    public MyViewPAger(@NonNull Context context) {
        super(context);
    }

    public MyViewPAger(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("MyViewPAger", "dispatchTouchEvent" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            moveX = ev.getX();
        }
        Log.d("MyViewPAger", "onTouchEvent" + ev.getAction());

        if (ev.getAction() == MotionEvent.ACTION_MOVE && isRequestDisallowInterceptTouchEvent()) {
            Log.d("MyViewPAger", "父组件不处理");
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }

    private boolean isRequestDisallowInterceptTouchEvent() {
        if (getCurrentItem() == 0 && moveX - downX > 0) {
            return false;
        }
        if (getCurrentItem() == getAdapter().getCount() - 1 && moveX - downX < 0) {
            return false;
        }
        return true;
    }
}

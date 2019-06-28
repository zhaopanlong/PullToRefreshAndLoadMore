# **PullToRefreshAndLoadMore**

这个项目是在别人基础上修改来的原来的项目地址

https://github.com/captainbupt/android-Ultra-Pull-To-Refresh-With-Load-More

主要解决了 recycleView或者ListView的条目中如果用viewpager等滑动组件失灵的问题

通过修改之后viewpaer是可以滑动了 但是感觉老是被刷新组件拦截滑动事件所以自己又

自定义了一个viewPager虽然有所改善 但是感觉还是和淘宝的滑动有些差别 自己以后还

会继续完善

# 修改原来项目的PtrFrameLayout

## 1、添加了requestDisallowInterceptTouchEvent(boolean disallowIntercept) 方法

```jade
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

```

## 2、在dispatchTouchEvent(MotionEvent e)的开头添加

``` java
   if (disallowInterceptTouchEvent) {
            if (e.getAction() == MotionEvent.ACTION_UP) {
                requestDisallowInterceptTouchEvent(false);
            }
            return dispatchTouchEventSupper(e);
        }
```

# 自定义的viepager

```java
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
```


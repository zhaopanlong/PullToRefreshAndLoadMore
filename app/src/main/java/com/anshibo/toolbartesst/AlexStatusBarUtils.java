package com.anshibo.toolbartesst;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by Alex on 2017/3/2.
 * Alex
 */

public class AlexStatusBarUtils {

    public static final int IS_SET_PADDING_KEY = 54648632;
    private static final int STATUS_VIEW_ID = R.id.status_view;
    private static final int TRANSLUCENT_VIEW_ID = R.id.translucent_view;

    //------------单色明暗度状态栏------------

    /**
     * 设置普通toolbar中状态栏颜色以及明暗度
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */
    public static void setStatusColor(Activity activity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(statusColorIntensity(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setStatusViewToAct(activity, color, statusBarAlpha);
            setRootView(activity);
        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }


    /**
     * 设置普通toolbar中状态栏颜色
     *
     * @param activity
     * @param color
     */
    public static void setStatusColor(Activity activity, @ColorInt int color) {
        setStatusColor(activity, color, 0);
    }

    /**
     * 设置toolbar带drawerLayout状态栏透明度,5.0以上使用默认系统的第二颜色colorPrimaryDark
     * 但是drawerLayout打开的时候会有一条statusbar高度的半透明条
     * 如果想修改，请到style中设置<item name="colorPrimaryDark">@color/colorPrimary</item>
     * 注：必须将drawerLayout设置android:fitsSystemWindows="true"
     * 还有下方的toolbar，
     * 一般不要设置滑动toolbar,在4.4系统会有问题，下部如果有tablayout会遮挡
     *
     * @param activity
     * @param statusBarAlpha
     */
    public static void setDyeDrawerStatusAlpha(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            setTranslucentStatusViewToAct(activity, statusBarAlpha);
        }
    }

    /**
     * toolbar可伸缩版本
     * 设置toolbar带drawerLayout状态栏透明,5.0以上使用默认系统的第二颜色colorPrimaryDark
     * 如果想修改，请到style中设置<item name="colorPrimaryDark">@color/colorPrimary</item>
     * 4.4版本跟随toolbar颜色
     * 但是drawerLayout打开的时候会有一条statusbar高度的半透明条
     * 注：必须将drawerLayout设置android:fitsSystemWindows="true"
     * CoordinatorLayout设置背景颜色，因为4.4状态栏的颜色会跟着它走
     * 下边内容布局设置背景颜色
     *
     * @param activity
     */
    public static void setDyeDrawerStatusTransparent(Activity activity, CoordinatorLayout coordinatorLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            coordinatorLayout.setFitsSystemWindows(true);
            View mStatusBarView = contentLayout.getChildAt(0);
            //改变颜色时避免重复添加statusBarView
            if (mStatusBarView != null && mStatusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
                return;
            }
            mStatusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            contentLayout.addView(mStatusBarView, lp);

        }
    }

    /**
     * 颜色不要用0x000000模式的，要用Color.rgb/argb(),或者activity.getResource().getColor等等
     * 设置普通toolbar带drawerLayout状态栏
     * 1.设置toolbar的颜色和颜色光暗度
     * 2.drawerLayout的顶部透明度
     * ,不要把toolbar设置为可滑动
     * 注：必须将drawerLayout设置android:fitsSystemWindows="true"
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */
    public static void setDyeDrawerStatusColor(Activity activity, DrawerLayout drawerLayout, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        View statusBarView = contentLayout.findViewById(STATUS_VIEW_ID);
        if (statusBarView != null) {
            if (statusBarView.getVisibility() == View.GONE) {
                statusBarView.setVisibility(View.VISIBLE);
            }
            statusBarView.setBackgroundColor(color);
        } else {
            contentLayout.addView(createStatusBarView(activity, color, 0), 0);
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                    .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                            contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout);
        setTranslucentStatusViewToAct(activity, statusBarAlpha);
    }

    /**
     * 隐藏statusView
     *
     * @param activity
     */
    public static void hideStatusView(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(STATUS_VIEW_ID);
        if (fakeStatusBarView != null) {
            fakeStatusBarView.setVisibility(View.GONE);
        }
        View fakeTranslucentView = decorView.findViewById(TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            fakeTranslucentView.setVisibility(View.GONE);
        }
    }

    //----------透明状态栏，可调整透明度-------------

    /**
     * 设置真正的状态栏透明度
     *
     * @param activity
     * @param statusBarAlpha
     */
    public static void setStatusAlpha(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setTranslucentStatusViewToAct(activity, statusBarAlpha);
            setRootView(activity);
        }
    }

    /**
     * 设置全透明状态栏
     *
     * @param activity
     */
    public static void setTransparentStatusBar(Activity activity) {
        setTranslucentStatusBar(activity, null, 0);
    }

    /**
     * 设置ImageView为第一控件的全透明状态栏
     *
     * @param activity
     */
    public static void setTransparentStatusBar(Activity activity, View topView) {
        setTranslucentStatusBar(activity, topView, 0);
    }

    /**
     * 设置ImageView为第一控件的可以调整透明度的状态栏
     *
     * @param activity
     */
    public static void setTranslucentStatusBar(Activity activity, View topView, int alpha) {
        setARGBStatusBar(activity, topView, 0, 0, 0, alpha);
    }

    /**
     * 设置透明状态栏版本的状态栏的ARGB
     *
     * @param activity
     * @param topView
     * @param r
     * @param g
     * @param b
     * @param alpha
     */
    public static void setARGBStatusBar(Activity activity, View topView, int r, int g, int b, int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.argb(alpha, r, g, b));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setARGBStatusViewToAct(activity, r, g, b, alpha);
        }
        if (topView != null) {
            boolean isSetPadding = topView.getTag(IS_SET_PADDING_KEY) != null;
            if (!isSetPadding) {
                topView.setPadding(topView.getPaddingLeft(), topView.getPaddingTop() + getStatusBarHeight(activity), topView.getPaddingRight(), topView.getPaddingBottom());
                topView.setTag(IS_SET_PADDING_KEY, true);
            }
        }
    }

    /**
     * drawerlayout中设置全透明状态栏
     *
     * @param activity
     * @param drawerLayout
     * @param topView
     */
    public static void setTransparentStatusForDrawer(Activity activity, DrawerLayout drawerLayout, View topView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            drawerLayout.setFitsSystemWindows(true);
            drawerLayout.setClipToPadding(false);
        }
        setTransparentStatusBar(activity, topView);
    }

    /**
     * drawerlayout中设置透明状态栏的透明度
     * drawer布局和主布局都会看到
     *
     * @param activity
     * @param drawerLayout
     * @param topView
     * @param statusBarAlpha
     */
    public static void setStatusAlphaForDrawer(Activity activity, DrawerLayout drawerLayout, View topView, int statusBarAlpha) {
        setStatusColorAndCAlphaForDrawer(activity, drawerLayout, topView, 0x000000, statusBarAlpha);
    }

    /**
     * drawerlayout中设置透明状态栏的颜色和透明度
     * drawer布局和主布局都会看到
     *
     * @param activity
     * @param drawerLayout
     * @param topView
     * @param color
     * @param statusBarAlpha
     */
    public static void setStatusColorAndCAlphaForDrawer(Activity activity, DrawerLayout drawerLayout, View topView, @ColorInt int color, int statusBarAlpha) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        setStatusARGBForDrawer(activity, drawerLayout, topView, r, g, b, statusBarAlpha);
    }

    public static void setStatusARGBForDrawer(Activity activity, DrawerLayout drawerLayout, View topView, int r, int g, int b, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            drawerLayout.setFitsSystemWindows(true);
            drawerLayout.setClipToPadding(false);
        }
        setARGBStatusBar(activity, topView, r, g, b, statusBarAlpha);
    }

    /**
     * 在有fragment的activity中使用
     * 注：需要在有状态栏的fragment的最顶端加一个状态栏大小的view
     *
     * @param activity
     * @param alpha
     */
    public static void setTranslucentForImageViewInFragment(Activity activity, int alpha) {
        setTranslucentStatusBar(activity, null, alpha);
    }


    //----------------私有方法----------------------

    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContentLayout DrawerLayout 的内容布局
     */
    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * 设置状态栏view的颜色并添加到界面中，如果找到状态栏view则直接设置，否则创建一个再设置
     *
     * @param activity
     * @param color
     * @param statusBarAlpha
     */
    private static void setStatusViewToAct(Activity activity, @ColorInt int color, int statusBarAlpha) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewById(STATUS_VIEW_ID);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(statusColorIntensity(color, statusBarAlpha));
        } else {
            decorView.addView(createStatusBarView(activity, color, statusBarAlpha));
        }
    }

    /**
     * 设置状态栏view的透明度，如果找到状态栏view则直接设置，否则创建一个再设置
     *
     * @param activity
     * @param statusBarAlpha
     */
    private static void setTranslucentStatusViewToAct(Activity activity, int statusBarAlpha) {
        setARGBStatusViewToAct(activity, 0, 0, 0, statusBarAlpha);
    }

    /**
     * 设置状态栏view的ARGB，如果找到状态栏view则直接设置，否则创建一个再设置
     *
     * @param activity
     * @param statusBarAlpha
     */
    private static void setARGBStatusViewToAct(Activity activity, int r, int g, int b, int statusBarAlpha) {

        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeStatusBarView = contentView.findViewById(TRANSLUCENT_VIEW_ID);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(Color.argb(statusBarAlpha, r, g, b));
        } else {
            contentView.addView(createARGBStatusBarView(activity, r, g, b, statusBarAlpha));
        }
    }

    /**
     * 创建和状态栏一样高的矩形，用于改变状态栏颜色和明暗度
     *
     * @param activity
     * @param color
     * @param alpha
     * @return
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(statusColorIntensity(color, alpha));
        statusBarView.setId(STATUS_VIEW_ID);
        return statusBarView;
    }

    /**
     * 创建和状态栏一样高的矩形，用于改变状态栏透明度
     *
     * @param activity
     * @param alpha
     * @return
     */
    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        return createARGBStatusBarView(activity, 0, 0, 0, alpha);
    }

    /**
     * 创建和状态栏一样高的矩形，用于改变状态栏ARGB
     *
     * @param activity
     * @param r
     * @param g
     * @param b
     * @param alpha
     * @return
     */
    private static View createARGBStatusBarView(Activity activity, int r, int g, int b, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, r, g, b));
        statusBarView.setId(TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }

    /**
     * 得到statusbar高度
     *
     * @param activity
     * @return
     */
    private static int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 计算状态栏颜色明暗度
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int statusColorIntensity(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 配置状态栏之下的View
     *
     * @param activity
     */
    public static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(0);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }
}


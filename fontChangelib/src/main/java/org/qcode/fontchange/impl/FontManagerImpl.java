package org.qcode.fontchange.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.qcode.fontchange.FontSizeAttr;
import org.qcode.fontchange.IActivityFontEventHandler;
import org.qcode.fontchange.IFontChangeListener;
import org.qcode.fontchange.IFontManager;
import org.qcode.fontchange.base.observable.INotifyUpdate;
import org.qcode.fontchange.base.observable.Observable;
import org.qcode.fontchange.base.utils.Logging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 字体大小调节加载管理类对外实现接口
 * qqliu
 * 2016/9/24.
 */
public class FontManagerImpl implements IFontManager {

    private static final String TAG = "FontManagerImpl";

    //单例相关
    private static volatile FontManagerImpl mInstance;

    private FontManagerImpl() {
    }

    public static FontManagerImpl getInstance() {
        if (null == mInstance) {
            synchronized (FontManagerImpl.class) {
                if (null == mInstance) {
                    mInstance = new FontManagerImpl();
                }
            }
        }
        return mInstance;
    }

    private Context mContext;

    private float mScale = 1.0f;

    private Observable<IActivityFontEventHandler> mObservable;

    @Override
    public void init(Context context) {
        mContext = context.getApplicationContext();
        mObservable = new Observable<IActivityFontEventHandler>();
    }

    @Override
    public void changeFontSize(float scale, IFontChangeListener fontChangeListener) {
        if (null != fontChangeListener) {
            fontChangeListener.onLoadStart(scale);
        }

        float tmpScale = mScale;
        mScale = scale;

        try {
            refreshFont();
            if (fontChangeListener != null) {
                fontChangeListener.onLoadSuccess(scale);
            }
        } catch (Exception ex) {
            Logging.d(TAG, "changeFontSize()| error happened", ex);
            mScale = tmpScale;

            if (fontChangeListener != null) {
                fontChangeListener.onLoadFail(scale);
            }
        }
    }

    @Override
    public void applyFont(TextView textView) {
        if (null == textView) {
            Logging.d(TAG, "applyFont()| view is null");
            return;
        }

        FontSizeAttr sizeAttr = ViewFontTagHelper.getFontAttr(textView);
        if (null != sizeAttr) {
            sizeAttr.apply(textView, mScale);
        }
    }

    @Override
    public void applyFont(View view, boolean applyChild) {
        if (view instanceof TextView) {
            applyFont((TextView) view);
        } else {
            if(view instanceof RecyclerView) {
                clearRecyclerView((RecyclerView)view);
            }
            
            if (applyChild) {
                if (view instanceof ViewGroup) {
                    //遍历子元素应用字体大小调节
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int i = 0; i < viewGroup.getChildCount(); i++) {
                        applyFont(viewGroup.getChildAt(i), true);
                    }
                }
            }
        }
    }

    @Override
    public void addObserver(IActivityFontEventHandler observer) {
        mObservable.addObserver(observer);
    }

    @Override
    public void removeObserver(IActivityFontEventHandler observer) {
        mObservable.removeObserver(observer);
    }

    @Override
    public void notifyUpdate(INotifyUpdate<IActivityFontEventHandler> callback, String identifier, Object... params) {
        mObservable.notifyUpdate(callback, identifier, params);
    }

    /***
     * 告知外部观察者当前字体大小发生了变化
     */
    private void refreshFont() {
        notifyUpdate(new INotifyUpdate<IActivityFontEventHandler>() {
            @Override
            public boolean notifyEvent(
                    IActivityFontEventHandler handler,
                    String identifier,
                    Object... params) {
                handler.handleFontUpdate();
                return false;
            }
        }, null);
    }

    private void clearRecyclerView(RecyclerView recyclerView) {
        Logging.d(TAG, "refreshRecyclerView()| clear recycler view");
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler" );
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler. class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(recyclerView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = recyclerView.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (Exception ex) {
            Logging.d(TAG, "refreshRecyclerView()| error happened", ex);
        }
    }
}

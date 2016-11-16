package org.qcode.fontchange.impl;

import android.app.Activity;
import android.view.View;

import org.qcode.fontchange.IActivityFontEventHandler;
import org.qcode.fontchange.IFontAttributeParser;
import org.qcode.fontchange.IFontChangeActivity;
import org.qcode.fontchange.IViewCreateListener;
import org.qcode.fontchange.base.utils.Logging;

import java.lang.ref.WeakReference;

/***
 * 支持字体大小调节界面的帮助类
 */
public class ActivityFontEventHandlerImpl implements IActivityFontEventHandler {

    private static final String TAG = "ActivityFontEventHandlerImpl";

    private final FontManagerImpl mFontManager;

    private volatile boolean mNeedRefreshFont = false;

    //当前界面是否有Focus
    private boolean mHasFocus;

    private WeakReference<Activity> mActivity = null;
    private FontInflaterFactoryImpl mFontInflaterFactory;
    private IViewCreateListener mViewCreateListener;

    private boolean mIsSupportFontChange = false;
    //字体设置发生变化时，当前界面是否需要立刻刷新字体大小
    private boolean mSwitchFontImmediately;

    private boolean mNeedDelegateViewCreate = true;

    private IFontAttributeParser mFontAttributeParser;

    public ActivityFontEventHandlerImpl() {
        mFontManager = FontManagerImpl.getInstance();
    }

    @Override
    public void onCreate(Activity activity) {
        if (!mIsSupportFontChange) {
            return;
        }

        mActivity = new WeakReference<Activity>(activity);

        if(mNeedDelegateViewCreate) {
            mFontInflaterFactory = new FontInflaterFactoryImpl(getFontAttributeParser());
            activity.getLayoutInflater().setFactory(mFontInflaterFactory);
            mFontInflaterFactory.setViewCreateListener(mViewCreateListener);
        }

        mFontManager.addObserver(this);
    }

    @Override
    public void setViewCreateListener(IViewCreateListener viewCreateListener) {
        if (!mIsSupportFontChange) {
            return;
        }

        mViewCreateListener = viewCreateListener;

        if(null != mFontInflaterFactory) {
            mFontInflaterFactory.setViewCreateListener(viewCreateListener);
        }
    }

    @Override
    public void onViewCreated() {
        if (!mIsSupportFontChange) {
            return;
        }

        Logging.d(TAG, "onViewCreated()");

//        if (!mFontManager.getResourceManager().isDefault()) {
            mFontManager.applyFont(getContentView(), true);
//        }
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!mIsSupportFontChange) {
            return;
        }

        mHasFocus = hasFocus;

        if(mHasFocus) {
            if (mNeedRefreshFont) {
                mNeedRefreshFont = false;
                //后台界面展示出来时再刷新
                refreshFont();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (!mIsSupportFontChange) {
            return;
        }

        mFontManager.removeObserver(this);
//        FontManager
//                .with(getContentView())
//                .cleanAttrs(true);

        mActivity.clear();
    }

    @Override
    public IActivityFontEventHandler setNeedDelegateViewCreate(boolean needDelegateViewCreate) {
        mNeedDelegateViewCreate = needDelegateViewCreate;
        return this;
    }

    @Override
    public IActivityFontEventHandler setSupportFontChange(boolean isSupportFontChange) {
        mIsSupportFontChange = isSupportFontChange;
        return this;
    }

    @Override
    public IActivityFontEventHandler setSwitchFontImmediately(boolean isImmediate) {
        mSwitchFontImmediately = isImmediate;
        return this;
    }

    @Override
    public void handleFontUpdate() {
        if (mHasFocus || mSwitchFontImmediately) {
            mNeedRefreshFont = false;
            refreshFont();
        } else {
            //仅置位，不立刻刷新
            mNeedRefreshFont = true;
        }
    }

    @Override
    public IFontAttributeParser getFontAttributeParser() {
        if(null == mFontAttributeParser) {
            mFontAttributeParser = new FontAttributeParser();
        }

        return mFontAttributeParser;
    }

    private void refreshFont() {
        if(null == mActivity) {
            return;
        }

        final Activity activity = mActivity.get();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View contentView = getContentView();
                mFontManager.applyFont(contentView, true);

                //通知Activity做其他刷新操作
                if(activity instanceof IFontChangeActivity) {
                    ((IFontChangeActivity) activity).handleFontChange();
                }
            }
        });
    }

    public View getContentView() {
        if (null == mActivity) {
            return null;
        }

        Activity activity = mActivity.get();
        if (null == activity) {
            return null;
        }

        return activity.findViewById(android.R.id.content);
    }
}

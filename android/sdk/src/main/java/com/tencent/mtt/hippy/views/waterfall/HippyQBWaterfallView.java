package com.tencent.mtt.hippy.views.waterfall;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.tencent.mtt.hippy.HippyEngineContext;
import com.tencent.mtt.hippy.HippyInstanceContext;
import com.tencent.mtt.hippy.common.HippyArray;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.DiffUtils;
import com.tencent.mtt.hippy.uimanager.HippyViewBase;
import com.tencent.mtt.hippy.uimanager.HippyViewEvent;
import com.tencent.mtt.hippy.uimanager.NativeGestureDispatcher;
import com.tencent.mtt.hippy.uimanager.RenderNode;
import com.tencent.mtt.hippy.utils.PixelUtil;
import com.tencent.mtt.supportui.views.recyclerview.ContentHolder;
import com.tencent.mtt.supportui.views.recyclerview.IRecyclerViewFooter;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerAdapter;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerView;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerViewBase;
import com.tencent.mtt.supportui.views.recyclerview.Scroller;

import java.util.ArrayList;
import java.util.Arrays;

public class HippyQBWaterfallView extends RecyclerView implements HippyViewBase
//        , HippyQBSkinHandler.HippyQBCommonSkin, HippyQBRefreshHeader.RefreshableCallback
{

  static final String TAG = "HippyQBWaterfallView";
  static final boolean DEBUG = DebugUtil.DEBUG;

  HippyWaterfallAdapter mAdapter;
  private HippyEngineContext mHippyContext;
  private NativeGestureDispatcher mGestureDispatcher;
  private Runnable mDispatchLayout = null;

  public static final int DEFAULT_REFRESH_TYPE = 1;
  public static final int HIPPY_SKIN_CHANGE = 1001;

  boolean mEnableFooter;
  boolean mEnableRefresh;
  HippyArray mRefreshColors;
  //    protected HippyQBRefreshHeader mQBRefreshHeader;
  private OnInitialListReadyEvent mOnInitialListReadyEvent;

  private boolean mHasRemovePreDraw = false;
  private ViewTreeObserver.OnPreDrawListener mPreDrawListener = null;
  private ViewTreeObserver mViewTreeObserver = null;

  // 这里的代码仅用于自动化测试 >>>
  private boolean mHasLoadMore = false;
  private boolean mHasScrollToIndex = false;
  private boolean mHasScrollToContentOffset = false;
  private boolean mHasStartRefresh = false;
  private boolean mHasCompeleteRefresh = false;
  // 这里的代码仅用于自动化测试 <<<

  //    private IQBRefreshDropdown mCallback;
  public HippyQBWaterfallView(Context context) {
    super(context);
    mHippyContext = ((HippyInstanceContext) context).getEngineContext();
    this.setLayoutManager(new HippyQBWaterfallLayoutManager(context));
    //mContext = context;
    mAdapter = new HippyWaterfallAdapter(this);
    //        postDelayed(new Runnable()
    //        {
    //            @Override
    //            public void run()
    //            {
    //                HippyWaterfallView.this.setAdapter(HippyWaterfallView.this.mAdapter);
    //                mAdapter.notifyDataSetChanged();
    //                HippyWaterfallView.this.dispatchLayout();
    //            }
    //        }, 500);
    setRecycledViewPool(new RNWFRecyclerPool());

    mEnableFooter = true;
    mEnableRefresh = false;
    mRefreshColors = null;

    addOnListScrollListener(mAdapter.getOnListScrollListener());

  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    boolean result = super.onTouchEvent(event);
    if (mGestureDispatcher != null) {
      result |= mGestureDispatcher.handleTouchEvent(event);
    }
    return result;
  }

//    /**
//     * 设置自定义footer
//     */
//    public void setCustomFooter(HippyFooterView footer) {
//        if (null != mAdapter) {
//            mAdapter.setCustomFooter(footer);
//        }
//    }

  @Override
  public NativeGestureDispatcher getGestureDispatcher() {
    return mGestureDispatcher;
  }

  @Override
  public void setGestureDispatcher(NativeGestureDispatcher dispatcher) {
    this.mGestureDispatcher = dispatcher;
  }

  //    void sendEvent(String eventName)
  //    {
  //        sendEvent(eventName, null);
  //    }
  //
  //    void sendEvent(String eventName, HippyMap hippyMap)
  //    {
  //        if (hippyMap == null)
  //            hippyMap = new HippyMap();
  //        mHippyContext.getModuleManager().getJavaScriptModule(EventDispatcher.class).receiveUIComponentEvent(getId(), "onChangeText", hippyMap);
  //    }

  protected void setListData() {
    if (DEBUG) {
      Log.d(TAG, "setListData: " + mHippyContext.getRenderManager().getRenderNode(getId()));
    }

    if (getAdapter() == null) {
      setAdapter(mAdapter);
    }

    mAdapter.notifyDataSetChanged();

    if (mDispatchLayout == null) {
      mDispatchLayout = new Runnable() {
        @Override
        public void run() {
          dispatchLayout();
        }
      };
    }
    removeCallbacks(mDispatchLayout);
    post(mDispatchLayout);
  }

  public void startLoadMore() {
    // 这里的代码仅用于自动化测试
    mHasLoadMore = true;

    //sendEvent("OnEndReachedEvent");
    mAdapter.getOnEndReachedEvent().send(this, null);
    mAdapter.setLoadingStatus(IRecyclerViewFooter.LOADING_STATUS_LOADING);
  }
//
//    @Override
//    public void hippySwitchSkin() {
//        // 切换皮肤时，需要把缓存池中的itemview也刷新一次
//        traversal(HIPPY_SKIN_CHANGE);
//        // 刷新refreshHeader
////        if (mQBRefreshHeader != null) {
////            mQBRefreshHeader.onSwitchSkin();
////        }
//        // 刷新loadingFooter
////        if (mRecyclerViewAdapter != null) {
////            AppManifest.getInstance().queryExtension(IHippySkinExtension.class, null)
////                    .switchSkin(mRecyclerViewAdapter.mDefaultLoadingView);
////        }
//    }

  @Override
  public void handleInTraversal(int traversalPurpose, int position, View contentView) {
    if (traversalPurpose == HIPPY_SKIN_CHANGE) {
      traversalChildViewForSkinChange(contentView);
    }
  }

  private void traversalChildViewForSkinChange(View view) {
//        if (view instanceof HippyQBSkinHandler.HippyQBCommonSkin) {
//            ((HippyQBSkinHandler.HippyQBCommonSkin) view).hippySwitchSkin();
//        }
    if (view instanceof ViewGroup) {
      int childCount = ((ViewGroup) view).getChildCount();
      for (int i = 0; i < childCount; i++) {
        traversalChildViewForSkinChange(((ViewGroup) view).getChildAt(i));
      }
    }
  }

  public void checkExposureForReport(int oldState, int newState) {
    if (getAdapter() != null) {
      mAdapter.checkExposureForReport(oldState, newState);
    }
  }

  public void setCustomRefreshColor(int ballColor, int bgColor, int tipsBgColor) {
//        if (mEnableRefresh) {
//            if (this.mQBRefreshHeader == null) {
//                this.mQBRefreshHeader = new HippyQBRefreshHeader(this);
//            }
//            this.mQBRefreshHeader.setCustomRefreshBallColor(ballColor, bgColor, tipsBgColor);
//        }
//
//        if (mEnableFooter) {
//            if (mAdapter.mDefaultLoadingView instanceof HippyFooterView) {
//                HippyFooterView footerView = (HippyFooterView) mAdapter.mDefaultLoadingView;
//                footerView.setCustomColor(ballColor);
//            }
//        }
  }

  public void scrollToIndex(int xIndex, int yIndex, boolean animated) {
    // 这里的代码仅用于自动化测试
    mHasScrollToIndex = true;

    scrollToPosition(yIndex, 0);
    post(new Runnable() {
      @Override
      public void run() {
        dispatchLayout();
      }
    });
  }

  public void scrollToContentOffset(double xOffset, double yOffset, boolean animated) {
    // 这里的代码仅用于自动化测试
    mHasScrollToContentOffset = true;

    scrollToPosition(0, (int) -PixelUtil.dp2px(yOffset));
    post(new Runnable() {
      @Override
      public void run() {
        dispatchLayout();
      }
    });
  }

  public void setScrollbarEnabled(boolean scrollbarEnabled) {
  }

  public void setFastScrollerEnabled(boolean fastScrollerEnabled) {
  }

  public void setLiftEnabled(boolean liftEnabled) {
  }

  public void setPlaceHolderDrawableEnabled(boolean placeHolderDrawableEnabled) {
  }

  public void setRefreshEnabled(boolean refreshEnabled) {
//        this.mEnableRefresh = refreshEnabled;
//        if (refreshEnabled) {
//            this.mQBRefreshHeader = new HippyQBRefreshHeader(this);
//        } else {
//            this.mQBRefreshHeader = null;
//        }
  }

  public void setEnableScrollForReport(boolean enableScrollForReport) {
    mAdapter.setEnableScrollForReport(enableScrollForReport);
  }

  public void setEnableExposureReport(boolean enableExposureReport) {
    mAdapter.setEnableExposureReport(enableExposureReport);
  }

  public void setRefreshColors(HippyArray refreshColors) {
    mRefreshColors = refreshColors;
  }

  protected void setLoadingStatus(int loadingStatus, String text) {
    mAdapter.setLoadingStatus(loadingStatus, text);
  }

  @Override
  public void checkNotifyFooterAppearWithFewChild(int endOffset) {
    // TODO: 需要重构HippyRecyclervIew逻辑
  }

  @Override
  public void draw(Canvas c) {
    super.draw(c);
//        if (mQBRefreshHeader != null) {
//            mQBRefreshHeader.onDraw(c);
//        }
  }

  @Override
  public void onDraw(Canvas c) {
    super.onDraw(c);

    // bug fixed 这里被绘制了两次，去掉
//        if (mQBRefreshHeader != null)
//        {
//            mQBRefreshHeader.onDraw(c);
//        }
  }

  @Override
  public void onScrollStateChanged(int oldState, int newState) {
    super.onScrollStateChanged(oldState, newState);
    if (getAdapter() != null) {
      mAdapter.checkScrollForReport();
      mAdapter.checkExposureForReport(oldState, newState);
    }
  }

  public void startRefresh(int type) {
    // 防止双击tab三个点收回
//        if (mEnableRefresh && mQBRefreshHeader != null
//                && mQBRefreshHeader.getRefreshState() == HippyQBRefreshHeader.REFRESH_STATE_WAIT) {
//            // 这里把footer的状态强制改为loading
//            mAdapter.setLoadingStatus(IRecyclerViewFooter.LOADING_STATUS_FINISH, "");
//            scrollToPosition(0, 0);
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    dispatchLayout();
//                }
//            });
//            // 这里的代码仅用于自动化测试
//            mHasStartRefresh = true;
//
//            if (type == DEFAULT_REFRESH_TYPE) {
//                startRefresh(true);
//            } else {
//                startRefreshWithType(true);
//            }
//        }
  }

  public void setRefreshPromptInfo(String descriptionText, int descriptionTextColor,
    int descriptionTextFontSize, String imgUrl, int imgWidth, int imgHeight) {
//        if (mQBRefreshHeader != null) {
//            mQBRefreshHeader.setRefreshPromptInfo(descriptionText, descriptionTextColor,
//                    descriptionTextFontSize, imgUrl, imgWidth, imgHeight, 0);
//        }
  }

  public void startRefresh(boolean inInit) {
//        if (this.mEnableRefresh) {
//            this.mQBRefreshHeader.startRefresh(inInit);
//        }

  }

  public void startRefreshWithType(boolean inInit) {
    if (this.mEnableRefresh) {
//            this.mQBRefreshHeader.startRefreshWithType(inInit);
    }

  }

  public void startRefreshWithOnlyAnimation(boolean inInit) {
    if (this.mEnableRefresh) {
//            this.mQBRefreshHeader.startRefreshOnlyWithAimation(inInit);
    }
  }

  protected boolean checkNeedToReport(float velocity, int scrollState) {
    return true;
  }

  protected boolean enableOnSrollReport() {
    return true;
  }

  protected ExposureForReport getExposureForReport(int oldState, int newState) {
    return mAdapter.getExposureForReportInner(oldState, newState);
  }

//    @Override
//    public void completeRefresh(int result) {
////        if (mQBRefreshHeader != null) {
////            mQBRefreshHeader.completeRefresh(result);
////        }
//    }
//
//    @Override
//    public void completeRefresh(int result, String text, int bgBeginColor, int bgEndColor,
//            int textColor, int textFontSize, String imageUrl, boolean showIcon, long duration,
//            int isMaintab, Promise promise, int hideIndex) {
////        if (mQBRefreshHeader != null) {
////            // 这里的代码仅用于自动化测试
////            mHasCompeleteRefresh = true;
////            mQBRefreshHeader.completeRefresh(result, text, bgBeginColor, bgEndColor, textColor,
////                    textFontSize, imageUrl, showIcon, duration, promise, 0);
////        }
//    }
//
//    @Override
//    public void scrollToShowHeader(int headerHeight,
//            OnScrollFinishListener onScrollFinishListener) {
//        smoothScrollBy(0, -headerHeight - mOffsetY, false, true);
//        mViewFlinger.mScrollFinishListener = onScrollFinishListener;
//        mViewFlinger.mTargetPosition = -headerHeight - mOffsetY;
//    }
//
//    @Override
//    public void scrollToShowHeaderAtOnce(int headerHeight) {
//        mLayout.scrollToPositionWithOffset(mAdapter != null ? -mAdapter.getHeaderViewCount() : 0,
//                headerHeight);
//        mLayout.mPreventFixGap = true;
//    }
//
//    @Override
//    public void scrollToShowHeaderSmooth(int headerHeight) {
//        smoothScrollBy(0, -headerHeight - mOffsetY, false, true);
//    }
//
//    @Override
//    public void onAboutToRefresh() {
//
//    }
//
//    @Override
//    public void onRefresh(boolean fromPull) {
//        if (mAdapter != null) {
//            mAdapter.startRefreshData(fromPull);
//        }
//    }
//
//    @Override
//    public void onShowToast() {
//
//    }
//
//    @Override
//    public void scrollToTop(OnScrollFinishListener onScrollFinishListener, int hideIndex) {
//        if (hasNoItem() && mQBRefreshHeader != null && mQBRefreshHeader.getRefreshState()
//                == HippyQBRefreshHeader.REFRESH_STATE_SUCCESSS) { // 如果当前listview没有item\header\footer，并且状态是REFRESH_STATE_SUCCESSS，将mOffsetY = 0
//            //            Log.e("leo", "scrollToTop mOffsetY " + mOffsetY + ", 0");
//            mOffsetY = 0;
//            // 这里需要补充执行postOnAnimation，否则refreshState不对
//            mViewFlinger.postOnAnimation();
//        } else {
//            smoothScrollBy(0, -mOffsetY, false, true);
//        }
//        mViewFlinger.mScrollFinishListener = onScrollFinishListener;
//        mViewFlinger.mTargetPosition = -mOffsetY;
//    }
//
//    @Override
//    public void scrollBack(int headerHeight, OnScrollFinishListener listener, int hideIndex) {
//        scrollToTop(listener, 0);
//    }

  @Override
  public void scrollToTopAtOnce() {
    super.scrollToTopAtOnce();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
//        if (mQBRefreshHeader != null) {
//            mQBRefreshHeader.restoreRefresh();
//        }

    if (!mHasRemovePreDraw) {
      mViewTreeObserver = getViewTreeObserver();
      if (mPreDrawListener == null) {
        mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
          @Override
          public boolean onPreDraw() {
            if (mAdapter.getItemCount() > 0
              && HippyQBWaterfallView.this.getChildCount() > 0) {
              mViewTreeObserver.removeOnPreDrawListener(this);
              mHasRemovePreDraw = true;
              post(new Runnable() {
                @Override
                public void run() {
                  getOnInitialListReadyEvent()
                    .send(HippyQBWaterfallView.this, null);
                }
              });

            }
            return true;
          }
        };
      }
      mViewTreeObserver.removeOnPreDrawListener(mPreDrawListener);
      mViewTreeObserver.addOnPreDrawListener(mPreDrawListener);

    }
  }

  @Override
  protected void onDetachedFromWindow() {
//        if (mQBRefreshHeader == null || !mQBRefreshHeader.isRefreshHeaderShowing()) {
    stopScroll();
//        }
//        if (mQBRefreshHeader != null) {
//            mQBRefreshHeader.stopRefresh();
//        }
    if (mPreDrawListener != null && mViewTreeObserver != null) {
      mViewTreeObserver.removeOnPreDrawListener(mPreDrawListener);

    }
    super.onDetachedFromWindow();
  }

  @Override
  public void cancelTouch() {
//        if (mQBRefreshHeader != null) {
//            mQBRefreshHeader.onCancelTouch();
//        }
    super.cancelTouch();
  }

//    @Override
//    public void removeOnScrollFinishListener() {
//        mViewFlinger.mScrollFinishListener = null;
//        mViewFlinger.mTargetPosition = Integer.MAX_VALUE;
//    }
//
//    @Override
//    public void removeCallbacksDelegate(Runnable runnable) {
//        removeCallbacks(runnable);
//    }
//
//    @Override
//    public void postDelayedDelegate(Runnable runnable, long delayTime) {
//        postDelayed(runnable, delayTime);
//    }
//
//    @Override
//    public RecyclerViewBase getAttachView() {
//        return this;
//    }

  @Override
  protected void checkRefreshHeadOnFlingRun() {
//        if (mQBRefreshHeader != null) {
//            if (mOffsetY <= 0 || !optimizeHeaderRefresh) {
//                invalidate();
//            }
//        }
  }

  @Override
  public boolean isRefreshing() {
    return false; // mQBRefreshHeader != null && mQBRefreshHeader.isRefreshing();
  }

  @Override
  protected boolean changeUpOverScrollEnableOnComputeDxDy(int dx, int dy,
    boolean careSpringBackMaxDistance, Scroller scroller, boolean isTouch,
    boolean currentUpOverScrollEnabled) {
//        if (!currentUpOverScrollEnabled && mQBRefreshHeader != null) {
//            if (!isTouch) {
//                if (!careSpringBackMaxDistance) {
//                    return true;
//                }
//            }
//        }
    return currentUpOverScrollEnabled;
  }

  @Override
  protected boolean checkShouldStopScroll() {
//        if (mQBRefreshHeader != null && mEnableRefresh) {
//            if (!mQBRefreshHeader.onScrolled()) {
//                return true;
//            }
//        }
    return false;
  }

  @Override
  protected void invalidateRefreshHeader() {
//        if (mQBRefreshHeader != null) {
//            if (mOffsetY <= 0 || !optimizeHeaderRefresh) {
//                invalidate();
//            }
//        }
  }

  @Override
  protected boolean shouldStopReleaseGlows(boolean canGoRefresh, boolean fromTouch) {
//        if (mQBRefreshHeader != null && mEnableRefresh) {
//            if (mQBRefreshHeader.onUpAction(canGoRefresh)) {
//                return true;
//            }
//        }
    return false;
  }

  @Override
  protected boolean shouldStopOnInterceptTouchEvent(MotionEvent e, int totalHeight,
    boolean upOverScrollEnabled) {
//        if (mOffsetY < 0 || getHeight() > totalHeight) {
//            if (mQBRefreshHeader != null && mEnableRefresh && mQBRefreshHeader
//                    .isRefreshHeaderShowing()) {
//                if (!upOverScrollEnabled) {
//                    return true;
//                }
//            }
//
//        }
    return false;
  }

  @Override
  protected boolean shouldStopOnTouchEvent(MotionEvent e, int totalHeight,
    boolean upOverScrollEnabled) {
    if (mOffsetY < 0 || getHeight() > totalHeight) {
//            if (mQBRefreshHeader != null && mEnableRefresh && mQBRefreshHeader
//                    .isRefreshHeaderShowing()) {
//                if (!upOverScrollEnabled) {
//                    return true;
//                }
//            }
    }
    return false;
  }

  protected void setPreloadItemNumber(int count) {
    mAdapter.setPreloadItemNum(count);
  }

//    @Override
//    public boolean supportDropdown() {
//        return mCallback != null && mCallback.supportDropdown();
//    }
//
//    @Override
//    public int getDropdownHeight() {
//        return mCallback != null ? mCallback.getDropdownHeight() : 0;
//    }
//
//    @Override
//    public void onEnterDropdown(RecyclerViewBase attachView, QBRefreshDropdownState dropdownState, int offsetY) {
//        if (mCallback != null) {
//            mCallback.onEnterDropdown(attachView, dropdownState, offsetY);
//        }
//    }
//
//    @Override
//    public TipsText getDropdownTipsText() {
//        return mCallback == null ? null : mCallback.getDropdownTipsText();
//    }
//
//    public void setQBRefreshDropdownCallback(IQBRefreshDropdown dropdown) {
//        mCallback = dropdown;
//    }

  /**
   * -------------------待实现空方法----------------
   */

  public class HippyWaterfallAdapter extends RecyclerAdapter implements
    HippyQBWaterfallItemRenderNode.IRecycleItemTypeChange {

    private HippyQBWaterfallEvent mOnEndReachedEvent;
    private HippyQBWaterfallEvent mOnFooterAppearedEvent;
    private HippyQBWaterfallEvent mOnRefreshEvent;
    private HippyQBWaterfallEvent mOnScrollForReportEvent;
    private int mPreloadItemNum;
    private boolean mShouldUpdatePreloadDistance;
    private int mPreloadDistanceWithItemNumber;
    private boolean mOnPreloadCalled;
    private boolean mEnableScrollForReport;
    private boolean mEnableExposureReport;
    private HippyMap mScrollReportResultMap;
    private HippyMap mExposureReportResultMap;
    private OnListScrollListener mOnListScrollListener;

    // 这里的代码仅用于自动化测试 >>>
    private boolean mHasOnScrollForReport = false;
    private boolean mHasExposureReport = false;
    private boolean mHasOnRefresh = false;
    private boolean mHasOnFooterAppeared = false;
    private boolean mHasPreload = false;
    private boolean mHasOnEndReached = false;
    private boolean mHasSetLoadingStatus = false;
    // 这里的代码仅用于自动化测试 <<<

    public HippyWaterfallAdapter(RecyclerView recyclerView) {
      super(recyclerView);
//            mDefaultLoadingView = new HippyDefaultFooter(recyclerView.getContext());
      setLoadingStatus(IRecyclerViewFooter.LOADING_STATUS_LOADING);
    }

    ArrayList<ViewHolder> mListViewHolder;

    public int getRecyclerItemCount() {
      mListViewHolder = new ArrayList<>();

      Recycler recycler = mParentRecyclerView.getRecycler();

      mListViewHolder.addAll(recycler.mAttachedScrap);

      mListViewHolder.addAll(recycler.mCachedViews);

      for (int i = 0; i < recycler.getRecycledViewPool().mScrap.size(); i++) {
        mListViewHolder.addAll(recycler.getRecycledViewPool().mScrap.valueAt(i));
      }
      return mListViewHolder.size() + mParentRecyclerView.getChildCount();
    }

    View getRecyclerItemView(int index) {
      if (index < mListViewHolder.size()) {
        return mListViewHolder.get(index).mContent;
      } else {
        return mParentRecyclerView.getChildAt(index - mListViewHolder.size());
      }

    }

//        /**
//         * 设置自定义footer
//         */
//        public void setCustomFooter(HippyFooterView footer) {
//            if (null != mDefaultLoadingView) {
//                mDefaultLoadingView = footer;
//            }
//        }

    @Override
    public ContentHolder onCreateContentViewWithPos(ViewGroup parent, int position,
      int viewType) {
      NodeHolder contentHolder = new NodeHolder();
      RenderNode contentViewRenderNode = mHippyContext.getRenderManager()
        .getRenderNode(getId()).getChildAt(position);
      contentViewRenderNode.setLazy(false);
      contentHolder.mContentView = contentViewRenderNode.createViewRecursive();
      contentHolder.mBindNode = contentViewRenderNode;
      contentHolder.isCreated = true;
      if (DEBUG) {
        Log.d(TAG,
          "onCreateContentViewWithPos #" + position + " type=" + viewType + " node="
            + DebugUtil.stringifyR(contentViewRenderNode) + " view="
            + contentHolder.mContentView);
        DebugUtil.forEach(contentHolder.mContentView, new DebugUtil.IForOne<View>() {
          @Override
          public void forOne(View one) {
            if (one instanceof ViewGroup) {
              ((ViewGroup) one)
                .setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
                  Object[] trunc(Object[] array, int limit) {
                    if (array.length <= limit) {
                      return array;
                    }
                    return Arrays.copyOf(array, limit);
                  }

                  @Override
                  public void onChildViewAdded(View parent, View child) {
                    Log.d(TAG, "onChildViewAdded:" + " parent=" + DebugUtil
                      .stringifyS(parent) + " child=" + DebugUtil
                      .stringifyS(child) + " @" + DebugUtil
                      .stackTrace(0));
                  }

                  @Override
                  public void onChildViewRemoved(View parent, View child) {
                    Log.d(TAG,
                      "onChildViewRemoved:" + " parent=" + DebugUtil
                        .stringifyS(parent) + " child="
                        + DebugUtil.stringifyS(child) + " @"
                        + DebugUtil.stackTrace(0));
                  }
                });
            }
          }
        });
      }
      return contentHolder;
    }

    public void onViewAbandonHelper(ViewHolderWrapper viewHolder) {
      onViewAbandon(viewHolder);
    }

    @Override
    protected void onViewAbandon(ViewHolderWrapper viewHolder) {
      // set is lazy true the holder is delete so delete view
      NodeHolder nodeHolder = (NodeHolder) viewHolder.mContentHolder;
      if (DEBUG) {
        Log.d(TAG, "onViewAbandon node=" + DebugUtil.stringifyR(nodeHolder.mBindNode)
          + " contentView=" + DebugUtil.stringifyR(nodeHolder.mContentView));
      }
      if (nodeHolder.mBindNode != null) {
        nodeHolder.mBindNode.setLazy(true);
        mHippyContext.getRenderManager().getControllerManager()
          .deleteChild(mParentRecyclerView.getId(), nodeHolder.mBindNode.getId());
      }

      if (nodeHolder.mBindNode != null
        && nodeHolder.mBindNode instanceof HippyQBWaterfallItemRenderNode) {
        ((HippyQBWaterfallItemRenderNode) nodeHolder.mBindNode)
          .setRecycleItemTypeChangeListener(null);
      }

      super.onViewAbandon(viewHolder);
    }

    @Override
    public void onBindContentView(ContentHolder holder, int position, int layoutType) {
      NodeHolder contentHolder = (NodeHolder) holder;

      if (contentHolder.isCreated) {
        try {
          Log.d(TAG, "onBindContentView #" + position + " bindNode: " + DebugUtil
            .stringifyR(contentHolder.mBindNode));
          Log.d(TAG, "onBindContentView #" + position + " contentView: " + DebugUtil
            .stringifyR(holder.mContentView));
          contentHolder.mBindNode.updateViewRecursive();
          contentHolder.isCreated = false;
        } catch (Throwable t) {
          Log.e(TAG, "onBindContentView #" + position, t);
          throw t;
        }
      } else {
        //step 1: diff
        RenderNode fromNode = contentHolder.mBindNode;
        if (contentHolder.mBindNode != null) {
          contentHolder.mBindNode.setLazy(true);
        }
        try {
          RenderNode toNode = mHippyContext.getRenderManager().getRenderNode(getId())
            .getChildAt(position);
          toNode.setLazy(false);

          if (DEBUG) {
            Log.d(TAG, "onBindContentView #" + position + " fromNode: " + DebugUtil
              .stringifyR(fromNode));
            Log.d(TAG, "onBindContentView #" + position + " fromView: " + DebugUtil
              .stringifyR(getHippyView(fromNode.getId())));
            Log.d(TAG, "onBindContentView #" + position + " contentView: " + DebugUtil
              .stringifyR(holder.mContentView));
            Log.d(TAG, "onBindContentView #" + position + " toNode: " + DebugUtil
              .stringifyR(toNode));
          }

          ArrayList<DiffUtils.PatchType> patchTypes = DiffUtils
            .diff(contentHolder.mBindNode, toNode);
          if (DEBUG) {
            StringBuilder sb = new StringBuilder("onBindContentView #").append(position)
              .append(" diffPaths = {");
            for (DiffUtils.PatchType patchType : patchTypes) {
              sb.append(patchType.mPatch.toString()).append(",");
            }
            Log.d(TAG, sb.append("}").toString());
          }

          try {
            //step:2 delete unUseful views
            DiffUtils.deleteViews(mHippyContext.getRenderManager().getControllerManager(),
              patchTypes);
            //step:3 replace id
            DiffUtils.replaceIds(mHippyContext.getRenderManager().getControllerManager(),
              patchTypes);
            //step:4 create view is do not  reUse
            DiffUtils.createView(mHippyContext.getRenderManager().getControllerManager(),
              patchTypes);
            //step:5 patch the dif result
            DiffUtils.doPatch(mHippyContext.getRenderManager().getControllerManager(),
              patchTypes);
          } catch (Throwable t) {
            Log.e(TAG, "onBindContentView #" + position, t);
            throw t;
          }

          //                if (toNode.getHeight() - toNode.getY() != holder.mContentView.getMeasuredHeight())
          //                {//偶尔会出现个别没有被recycle（只被detache/scrap）的item没有成功的被updateLayout，这里补一发
          //                    Log.d(TAG,
          //                            "onBindContentView(" + position + ") need update layout," + " node=" + (toNode.getHeight() - toNode.getY()) + "x"
          //                                    + (toNode.getWidth() - toNode.getX()) + " view=" + holder.mContentView.getMeasuredWidth() + "x"
          //                                    + holder.mContentView.getMeasuredHeight());
          //
          //                    LinkedList<RenderNode> queue = new LinkedList<RenderNode>();
          //                    for (queue.add(toNode); !queue.isEmpty();)
          //                    {
          //                        RenderNode node = queue.pollFirst();
          //                        node.updateLayout(node.getX(), node.getY(), node.getWidth(), node.getHeight());
          //                        for (int k = 0; k < node.getChildCount(); ++k)
          //                            queue.add(node.getChildAt(k));
          //                    }
          //                    toNode.updateViewRecursive();
          //                }

          contentHolder.mBindNode = toNode;

          if (DEBUG) {
            Log.d(TAG, "onBindContentView #" + position + " toView: " + DebugUtil
              .stringifyR(getHippyView(toNode.getId())));
            Log.d(TAG, "onBindContentView #" + position + " resultView : " + DebugUtil
              .stringifyR(holder.mContentView));
          }
        } catch (Throwable t) {
        }

      }

      if (contentHolder.mBindNode instanceof HippyQBWaterfallItemRenderNode) {
        ((HippyQBWaterfallItemRenderNode) contentHolder.mBindNode)
          .setRecycleItemTypeChangeListener(this);
      }
    }

    @Override
    public int getItemCount() {
      try {
        return getRenderNode().getChildCount();
      } catch (NullPointerException e) {
        e.printStackTrace();
        return 0;
      }
    }

    @Override
    public boolean isAutoCalculateItemHeight() {
      return true;
    }

    @Override
    public int getItemViewType(int index) {
      RenderNode itemNode = getItemNode(index);
      if (itemNode != null) {
        HippyMap props = itemNode.getProps();
        if (props != null && props.containsKey("type")) {
          return props.getInt("type");
        }
      }

      return 0;
    }

    RenderNode getRenderNode() {
      return mHippyContext.getRenderManager().getRenderNode(getId());
    }

    View getHippyView(int id) {
      return mHippyContext.getRenderManager().getControllerManager().findView(id);
    }

    RenderNode getItemNode(int index) {
      return getRenderNode().getChildAt(index);
    }

    @Override
    public int getItemHeight(int index) {
      int itemHeight = 0;
      RenderNode listNode = mHippyContext.getRenderManager()
        .getRenderNode(mParentRecyclerView.getId());
      if (listNode != null && listNode.getChildCount() > index && index >= 0) {
        RenderNode listItemNode = listNode.getChildAt(index);
        if (listItemNode != null) {
          itemHeight = listItemNode.getHeight();
        }
      }
      return itemHeight + ((HippyQBWaterfallLayoutManager) mParentRecyclerView
        .getLayoutManager()).getItemGap();
    }

    @Override
    public int getHeightBefore(int pos) {
      return ((HippyQBWaterfallLayoutManager) getLayoutManager()).getHeightBefore(pos);
    }

    @Override
    public int getTotalHeight() {
      return ((HippyQBWaterfallLayoutManager) getLayoutManager()).getTotalHeight();
    }

//        void preCalculateItemHeights()
//        {//前端指定的高度发生变化（如转屏）时，需要快速重新计算item的高度，以免排布出现错位
//            View firstChild = getLayoutManager().getChildClosestToStartByOrder();
//            if (firstChild == null)
//            {
//                //Log.d(TAG, "preCalculateItemHeights null first child");
//                return;
//            }
//
//            int firstItemPosition = getLayoutManager().getPosition(firstChild);
//            int itemCount = getItemCount();
//            if (firstItemPosition == 0)
//            {
//                return;
//            }
//            if (firstItemPosition < 0 || firstItemPosition >= itemCount)
//            {
//                Log.d(TAG, "preCalculateItemHeights invalid firstItemPosition=" + firstItemPosition);
//                return;
//            }
//
//
//            int heightLen = mItemHeightList != null ? mItemHeightList.size() : 0;
//            if (heightLen == 0)
//            {
//                Log.d(TAG, "preCalculateItemHeights empty mItemHeightList");
//                return;
//            }
//
//            for (int i = 0; /* i < firstItemPosition && */ i < itemCount && i < heightLen; ++i)
//            {
//                int itemHeight = 0;
//                LinkedList<RenderNode> queue = new LinkedList<RenderNode>();
//                for (queue.add(getItemNode(i)); !queue.isEmpty(); )
//                {
//                    RenderNode node = queue.pollFirst();
//                    int height = node.getHeight() - node.getY();
//                    if (height <= 0)
//                    {
//                        HippyMap props = node.getProps();
//                        if (props.containsKey("height"))
//                        {
//                            height = props.getInt("height");
//                            height = TKDResources.dip2px(height);
//                        }
//                    }
//
//                    if (height > 0)
//                    {
//                        if (height > itemHeight)
//                            itemHeight = height;
//                    }
//                    else
//                    {
//                        for (int j = node.getChildCount(); j < node.getChildCount(); ++j)
//                            queue.add(node.getChildAt(j));
//                    }
//                }
//
//                if (itemHeight > 0)
//                {
//                    if (mItemHeightList.get(i) != itemHeight)
//                    {
//                        if (DEBUG)
//                            Log.d(TAG, "preCalculateItemHeights #" + i + " " + mItemHeightList.get(i) + " -> " + itemHeight);
//                        mItemHeightList.set(i, itemHeight);
//                    }
//                }
//            }
//        }

    @Override
    public void notifyDataSetChanged() {
//            preCalculateItemHeights();
      setPreloadItemNum(getPreloadThresholdInItemNumber());
      super.notifyDataSetChanged();
    }

    @Override
    public void startRefreshData() {
      // 这里的代码仅用于自动化测试
      mHasOnRefresh = true;

      //sendEvent("OnRefreshEvent");
      getOnRefreshEvent().send(mParentRecyclerView, null);
    }

    public void startRefreshData(boolean fromPull) {
      // 这里的代码仅用于自动化测试
      mHasOnRefresh = true;
      HippyMap params = new HippyMap();
      params.pushString("refreshFrom", fromPull ? "pull" : "command");
      getOnRefreshEvent().send(mParentRecyclerView, params);
    }

    @Override
    public void notifyLastFooterAppeared() {
      super.notifyLastFooterAppeared();
      if (mLoadingStatus != IRecyclerViewFooter.LOADING_STATUS_LOADING
        && mLoadingStatus != IRecyclerViewFooter.LOADING_STATUS_CUSTOM
        && mLoadingStatus != IRecyclerViewFooter.LOADING_STATUS_NOMORE_CLICKBACKWARDS) {
        setLoadingStatus(IRecyclerViewFooter.LOADING_STATUS_LOADING);
      }

      if (!mOnPreloadCalled) { //预加载之后，数据回来之前滑到底，不触发OnEndReached

        int preloadThresholdInPixel = getPreloadThresholdInPixels();
        int preloadThresholdInItemNumber = getPreloadThresholdInItemNumber();
        if (preloadThresholdInPixel > 0 || preloadThresholdInItemNumber > 0) {
          // 这里的代码仅用于自动化测试
          mHasOnEndReached = true;
          getOnEndReachedEvent().send(mParentRecyclerView, null);
        }
      }

      if (mLoadingStatus == IRecyclerViewFooter.LOADING_STATUS_LOADING) {
        // 这里的代码仅用于自动化测试
        mHasOnFooterAppeared = true;

        // 通知前端footer出现（fake loading）
        getOnFooterAppearedEvent().send(mParentRecyclerView, null);
      }
    }

    protected void setLoadingStatus(int loadingStatus, String text) {
      if (loadingStatus != IRecyclerViewFooter.LOADING_STATUS_LOADING) {
        if (loadingStatus != IRecyclerViewFooter.LOADING_STATUS_CUSTOM) {
          // 这里的代码仅用于自动化测试
          mHasSetLoadingStatus = true;
          this.setLoadingStatus(loadingStatus);
//                    if (this.mDefaultLoadingView != null
//                            && this.mDefaultLoadingView instanceof HippyFooterView) {
//                        ((HippyFooterView) this.mDefaultLoadingView).setText(text);
//                    }
        } else {
//                    if (this.mDefaultLoadingView != null
//                            && this.mDefaultLoadingView instanceof HippyFooterView) {
//                        ((HippyFooterView) this.mDefaultLoadingView)
//                                .setLoadingStatus(IRecyclerViewFooter.LOADING_STATUS_CUSTOM, text);
//                    }
          // 这里的代码仅用于自动化测试
          mHasSetLoadingStatus = true;
          this.setLoadingStatus(loadingStatus);
        }

        if (this.mDefaultLoadingView != null) {
          this.mDefaultLoadingView.measure(MeasureSpec
            .makeMeasureSpec(this.mDefaultLoadingView.getWidth(),
              MeasureSpec.EXACTLY), MeasureSpec
            .makeMeasureSpec(this.mDefaultLoadingView.getHeight(),
              MeasureSpec.EXACTLY));
          this.mDefaultLoadingView.layout(this.mDefaultLoadingView.getLeft(),
            this.mDefaultLoadingView.getTop(), this.mDefaultLoadingView.getRight(),
            this.mDefaultLoadingView.getBottom());
          this.mDefaultLoadingView.invalidate();
        }

        mOnPreloadCalled = false;
      } else {
        // 这里的代码仅用于自动化测试
        mHasSetLoadingStatus = true;
        this.setLoadingStatus(loadingStatus);
      }
    }

    protected void setPreloadItemNum(int preloadItemNum) {
      mPreloadItemNum = preloadItemNum;
      mShouldUpdatePreloadDistance = true;
    }

    protected void setEnableScrollForReport(boolean enableScrollForReport) {
      mEnableScrollForReport = enableScrollForReport;
    }

    protected void setEnableExposureReport(boolean enableExposureReport) {
      mEnableExposureReport = enableExposureReport;
    }

    protected void checkScrollForReport() {
      if (!mEnableScrollForReport) {
        return;
      }

      int startEdgePos = (int) PixelUtil.px2dp(mParentRecyclerView.mOffsetY);
      int endEdgePos = (int) PixelUtil
        .px2dp(mParentRecyclerView.getHeight() + mParentRecyclerView.mOffsetY);
      int firstVisiblePos = ((HippyQBWaterfallLayoutManager) mParentRecyclerView
        .getLayoutManager()).findFirstVisibleItemPosition();
      int lastVisiblePos = ((HippyQBWaterfallLayoutManager) mParentRecyclerView
        .getLayoutManager()).findLastVisibleItemPosition();
      // harryguo: 如果最后一个可见View是footer，那就要减掉这个footer
//            if (lastVisiblePos >= 1 && mParentRecyclerView.getLayoutManager()
//                    .findViewByPosition(lastVisiblePos) instanceof HippyFooterView) {
//                lastVisiblePos = lastVisiblePos - 1;
//            }

      if (mParentRecyclerView.mViewFlinger.getScroller() == null) {
        return;
      }

      float currentVelocity = Math
        .abs(mParentRecyclerView.mViewFlinger.getScroller().getCurrVelocity());
      int currentScrollState = mParentRecyclerView.getScrollState();

      // 传入包含item frames的数组
      HippyArray visibleItemArray = new HippyArray();
      for (int i = firstVisiblePos; i <= lastVisiblePos; i++) {
        View v = mParentRecyclerView.getLayoutManager()
          .findViewByPosition(i);
        if (v != null) {
          HippyMap itemData = new HippyMap();
          itemData.pushInt("x", v.getLeft());
          itemData.pushInt("y", v.getTop() + mOffsetY);
          itemData.pushInt("width", (int) PixelUtil.px2dp(getItemWidth(i)));
          itemData.pushInt("height", (int) PixelUtil.px2dp(getItemHeight(i)));

          visibleItemArray.pushMap(itemData);
        }
      }

      handleCurrentScrollStateInner(startEdgePos, endEdgePos, firstVisiblePos, lastVisiblePos,
        currentVelocity, currentScrollState, visibleItemArray);
    }

    void handleCurrentScrollStateInner(int startEdgePos, int endEdgePos,
      int firstVisiblePos, int lastVisiblePos, float currentVelocity,
      int currentScrollState,
      HippyArray visibleItemArray) {
      if ((currentScrollState == RecyclerViewBase.SCROLL_STATE_IDLE
        || currentScrollState == RecyclerViewBase.SCROLL_STATE_DRAGGING) && checkNeedToReport(0,
        currentScrollState)) {
        sendOnScrollForReport(startEdgePos, endEdgePos, firstVisiblePos, lastVisiblePos,
          currentScrollState,
          visibleItemArray);
      } else if (currentVelocity < mParentRecyclerView.getHeight() * 2 && checkNeedToReport(
        currentVelocity, currentScrollState)) { // 速度过快，不通知前端

        // 这里的代码仅用于自动化测试
        sendOnScrollForReport(startEdgePos, endEdgePos, firstVisiblePos, lastVisiblePos,
          currentScrollState,
          visibleItemArray);
      }
    }

    private void sendOnScrollForReport(int startEdgePos, int endEdgePos, int firstVisiblePos,
      int lastVisiblePos,
      int currentScrollState, HippyArray visibleItemArray) {
      // 这里的代码仅用于自动化测试
      mHasOnScrollForReport = true;

      if (mScrollReportResultMap == null) {
        mScrollReportResultMap = new HippyMap();
      }
      mScrollReportResultMap.clear();
      mScrollReportResultMap.pushInt("startEdgePos", startEdgePos);
      mScrollReportResultMap.pushInt("endEdgePos", endEdgePos);
      mScrollReportResultMap.pushInt("firstVisibleRowIndex", firstVisiblePos);
      mScrollReportResultMap.pushInt("lastVisibleRowIndex", lastVisiblePos);
      mScrollReportResultMap.pushInt("scrollState", currentScrollState);
      mScrollReportResultMap.pushArray("visibleRowFrames", visibleItemArray);
      getOnScrollForReportEvent().send(mParentRecyclerView, mScrollReportResultMap);
    }

    // 检查是否通知前端标准曝光数据
    protected void checkExposureForReport(int oldState, int newState) {
      if (!mEnableExposureReport) {
        return;
      }

      ExposureForReport exposureForReport = getExposureForReport(oldState, newState);
      if (exposureForReport == null) {
        return;
      }
      if (checkNeedToReport(exposureForReport.mVelocity, newState)) {
        if (mExposureReportResultMap == null) {
          mExposureReportResultMap = new HippyMap();
        }
        mExposureReportResultMap.clear();
        mExposureReportResultMap.pushInt("startEdgePos", exposureForReport.mStartEdgePos);
        mExposureReportResultMap.pushInt("endEdgePos", exposureForReport.mEndEdgePos);
        mExposureReportResultMap
          .pushInt("firstVisibleRowIndex", exposureForReport.mFirstVisibleRowIndex);
        mExposureReportResultMap
          .pushInt("lastVisibleRowIndex", exposureForReport.mLastVisibleRowIndex);
        //            reportData.putInt("velocity", mVelocity);
        mExposureReportResultMap.pushInt("scrollState", exposureForReport.mScrollState);
        mExposureReportResultMap
          .pushArray("visibleRowFrames", exposureForReport.mVisibleRowFrames);

        exposureForReport.send(mParentRecyclerView, mExposureReportResultMap);
      }
    }

    protected ExposureForReport getExposureForReportInner(int oldState, int newState) {
      if (!mEnableExposureReport) {
        return null;
      }

      if (mParentRecyclerView.mViewFlinger.getScroller() == null) {
        return null;
      }
      // 这里的代码仅用于自动化测试
      mHasExposureReport = true;

      int startEdgePos = (int) PixelUtil.px2dp(mParentRecyclerView.mOffsetY);
      int endEdgePos = (int) PixelUtil
        .px2dp(mParentRecyclerView.getHeight() + mParentRecyclerView.mOffsetY);
      int firstVisiblePos = ((HippyQBWaterfallLayoutManager) mParentRecyclerView
        .getLayoutManager()).findFirstVisibleItemPosition();
      int lastVisiblePos = ((HippyQBWaterfallLayoutManager) mParentRecyclerView
        .getLayoutManager()).findLastVisibleItemPosition();
      // harryguo: 如果最后一个可见View是footer，那就要减掉这个footer
//            if (lastVisiblePos >= 1 && mParentRecyclerView.getLayoutManager()
//                    .findViewByPosition(lastVisiblePos) instanceof HippyFooterView) {
//                lastVisiblePos = lastVisiblePos - 1;
//            }
      // 传入包含item frames的数组
      HippyArray visibleItemArray = new HippyArray();
//            int baseHeight = 0;
//            for (int i = 0; i < firstVisiblePos; i++) {
//                baseHeight += getItemHeight(i);
//                baseHeight += getItemMaigin(RecyclerViewBase.Adapter.LOCATION_TOP, i);
//                baseHeight += getItemMaigin(RecyclerViewBase.Adapter.LOCATION_BOTTOM, i);
//            }
//            for (int i = firstVisiblePos; i <= lastVisiblePos; i++) {
//                HippyMap itemData = new HippyMap();
//                itemData.pushInt("x", 0);
//                itemData.pushInt("y", (int) PixelUtil.px2dp(baseHeight));
//                baseHeight += getItemHeight(i);
//                itemData.pushInt("width", (int) PixelUtil.px2dp(getItemWidth(i)));
//                itemData.pushInt("height", (int) PixelUtil.px2dp(getItemHeight(i)));
//
//                visibleItemArray.pushMap(itemData);
//            }

      for (int i = firstVisiblePos; i <= lastVisiblePos; i++) {
        View v = mParentRecyclerView.getLayoutManager()
          .findViewByPosition(i);
        if (v != null) {
          HippyMap itemData = new HippyMap();
          itemData.pushInt("x", v.getLeft());
          itemData.pushInt("y", v.getTop() + mOffsetY);
          itemData.pushInt("width", (int) PixelUtil.px2dp(getItemWidth(i)));
          itemData.pushInt("height", (int) PixelUtil.px2dp(getItemHeight(i)));

          visibleItemArray.pushMap(itemData);
        }
      }

      float currentVelocity = Math
        .abs(mParentRecyclerView.mViewFlinger.getScroller().getCurrVelocity());
      return new ExposureForReport(mParentRecyclerView.getId(), startEdgePos, endEdgePos,
        firstVisiblePos, lastVisiblePos, (int) currentVelocity, newState,
        visibleItemArray);
    }


    protected boolean checkNeedToReport(float velocity, int scrollState) {
      return true;
    }

    @Override
    public void onPreload() {
      // 这里的代码仅用于自动化测试
      mHasPreload = true;

      mOnPreloadCalled = true;
      getOnEndReachedEvent().send(mParentRecyclerView, null);
    }

    public boolean hasCustomRecycler() {
      return true;
    }

    ViewHolder findBestHolderRecursive(int position, int targetType,
      Recycler recycler) {
      ViewHolder matchHolder = getScrapViewForPositionInner(position,
        targetType, recycler);
      if (matchHolder == null) {
        matchHolder = recycler.getViewHolderForPosition(position);
      }

      if (matchHolder != null && ((NodeHolder) matchHolder.mContentHolder).mBindNode
        .isDelete()) {
        matchHolder = findBestHolderRecursive(position, targetType, recycler);
      }

      return matchHolder;
    }

    @Override
    public ViewHolder findBestHolderForPosition(int position,
      Recycler recycler) {
      int targetType = getItemViewType(position);
      ViewHolder matchHolder = findBestHolderRecursive(position, targetType,
        recycler);
      return matchHolder;
    }

    private ViewHolder getScrapViewForPositionInner(int position, int type,
      Recycler recycler) {
      final int scrapCount = recycler.mAttachedScrap.size();
      // Try first for an exact, non-invalid match from scrap.
      for (int i = 0; i < scrapCount; i++) {
        final ViewHolder holder = recycler.mAttachedScrap.get(i);
        if (holder.getPosition() == position && !holder.isInvalid() && (!holder
          .isRemoved())) {
          if (holder.getItemViewType() == type
            && holder.mContentHolder instanceof NodeHolder) {
            RenderNode holderNode = ((NodeHolder) holder.mContentHolder).mBindNode;
            RenderNode toNode = mHippyContext.getRenderManager()
              .getRenderNode(mParentRecyclerView.getId()).getChildAt(position);
            if (holderNode == toNode) {
              recycler.mAttachedScrap.remove(i);
              holder.setScrapContainer(null);
              return holder;
            }
          }
        }
      }

      // Search in our first-level recycled view cache.
      final int cacheSize = recycler.mCachedViews.size();
      for (int i = 0; i < cacheSize; i++) {
        final ViewHolder holder = recycler.mCachedViews.get(i);
        if (holder.getPosition() == position && holder.getItemId() == type && !holder
          .isInvalid() && holder.mContentHolder instanceof NodeHolder) {
          RenderNode holderNode = ((NodeHolder) holder.mContentHolder).mBindNode;
          RenderNode toNode = mHippyContext.getRenderManager()
            .getRenderNode(mParentRecyclerView.getId()).getChildAt(position);
          if (holderNode == toNode) {
            recycler.mCachedViews.remove(i);
            return holder;
          }
        }
      }
      // Give up. Head to the shared pool.
      return this
        .getRecycledViewFromPoolInner(recycler.getRecycledViewPool(), type, position);
    }

    private ViewHolder getRecycledViewFromPoolInner(
      RecycledViewPool pool, int viewType, int position) {
      if (pool != null) {
        final ArrayList<ViewHolder> scrapHeap = pool.mScrap.get(viewType);
        if (scrapHeap != null && !scrapHeap.isEmpty()) {
          // traverse all scrap
          for (ViewHolder holder : scrapHeap) {
            if (holder.getItemViewType() == viewType
              && holder.mContentHolder instanceof NodeHolder) {
              RenderNode holderNode = ((NodeHolder) holder.mContentHolder).mBindNode;
              RenderNode toNode = mHippyContext.getRenderManager()
                .getRenderNode(mParentRecyclerView.getId())
                .getChildAt(position);
              if (holderNode == toNode) {
                scrapHeap.remove(holder);
                return holder;
              }
            }
          }
        }
      }
      return null;
    }

    private void checkHolderType(int oldType, int newType,
      HippyQBWaterfallItemRenderNode listItemRenderNode) {
      //do checkHolderType onScreen
      if (doCheckHolderTypeOnScreen(oldType, newType, listItemRenderNode)) {
        return;
      }

      //do checkHolderType inCache
      final int scrapCount = mRecycler.mAttachedScrap.size();
      // Try first for an exact, non-invalid match from scrap.
      for (int i = 0; i < scrapCount; i++) {
        final ViewHolder holder = mRecycler.mAttachedScrap.get(i);

        if (holder.getItemViewType() == oldType
          && holder.mContentHolder instanceof NodeHolder) {
          RenderNode holderNode = ((NodeHolder) holder.mContentHolder).mBindNode;
          if (holderNode == listItemRenderNode) {
            holder.setItemViewType(newType);
            return;
          }
        }
      }

      // Search in our first-level recycled view cache.
      final int cacheSize = mRecycler.mCachedViews.size();
      for (int i = 0; i < cacheSize; i++) {
        final ViewHolder holder = mRecycler.mCachedViews.get(i);
        if (holder.getItemViewType() == oldType
          && holder.mContentHolder instanceof NodeHolder) {
          RenderNode holderNode = ((NodeHolder) holder.mContentHolder).mBindNode;
          if (holderNode == listItemRenderNode) {
            holder.setItemViewType(newType);
            return;
          }
        }
      }

      // Give up. Head to the shared pool.
      doHeadToTheSharedPool(oldType, newType, listItemRenderNode);
    }

    private boolean doCheckHolderTypeOnScreen(int oldType, int newType,
      HippyQBWaterfallItemRenderNode listItemRenderNode) {
      int count = mParentRecyclerView.getChildCount();
      for (int i = 0; i < count; i++) {
        final ViewHolder holder = mParentRecyclerView
          .getChildViewHolder(mParentRecyclerView.getChildAt(i));
        if (holder.getItemViewType() == oldType
          && holder.mContentHolder instanceof NodeHolder) {
          RenderNode holderNode = ((NodeHolder) holder.mContentHolder).mBindNode;
          if (holderNode == listItemRenderNode) {
            holder.setItemViewType(newType);
            return true;
          }
        }
      }
      return false;
    }

    private void doHeadToTheSharedPool(int oldType, int newType,
      HippyQBWaterfallItemRenderNode listItemRenderNode) {
      if (mRecycler.getRecycledViewPool() != null) {
        final ArrayList<ViewHolder> scrapHeap = mRecycler
          .getRecycledViewPool().mScrap.get(oldType);
        if (scrapHeap != null && !scrapHeap.isEmpty()) {
          // traverse all scrap
          for (ViewHolder holder : scrapHeap) {
            if (holder.getItemViewType() == oldType
              && holder.mContentHolder instanceof NodeHolder) {
              RenderNode holderNode = ((NodeHolder) holder.mContentHolder).mBindNode;
              if (holderNode == listItemRenderNode) {
                holder.setItemViewType(newType);
                scrapHeap.remove(holder);
                mRecycler.getRecycledViewPool().getScrapHeapForType(newType)
                  .add(holder);
                return;
              }
            }
          }
        }
      }
    }

    @Override
    public int getPreloadThresholdInItemNumber() {
      return mPreloadItemNum;
    }

    @Override
    public int calcPreloadThresholdWithItemNumber() {
      if (mShouldUpdatePreloadDistance) {
        int startIndex = getItemCount() - 1;
        int endIndex = getItemCount() - mPreloadItemNum;
        if (endIndex < 0) {
          endIndex = 0;
        }
        mPreloadDistanceWithItemNumber = 0;
        for (int i = startIndex; i >= endIndex; i--) {
          mPreloadDistanceWithItemNumber += getItemHeight(i);
        }
        mShouldUpdatePreloadDistance = false;
      }
      return mPreloadDistanceWithItemNumber;
    }

    @Override
    public void onSuddenStop() {
      checkScrollForReport();
    }

    //        @Override
    //        public boolean isSuspentedItem(int pos)
    //        {
    //            RenderNode listNode = mHippyContext.getRenderManager().getRenderNode(mParentRecyclerView.getId());
    //            if (listNode != null && listNode.getChildCount() > pos)
    //            {
    //                RenderNode listItemNode = listNode.getChildAt(pos);
    //                if (listItemNode instanceof HippyQBWaterfallItemRenderNode)
    //                {
    //                    return ((HippyQBWaterfallItemRenderNode) listItemNode).shouldSticky();
    //                }
    //            }
    //            return super.isSuspentedItem(pos);
    //        }

    private HippyQBWaterfallEvent getOnFooterAppearedEvent() {
      if (mOnFooterAppearedEvent == null) {
        mOnFooterAppearedEvent = new HippyQBWaterfallEvent("onFooterAppeared");
      }
      return mOnFooterAppearedEvent;
    }

    @Override
    public void onRecycleItemTypeChanged(int oldType, int newType,
      HippyQBWaterfallItemRenderNode listItemNode) {
      checkHolderType(oldType, newType, listItemNode);
    }

    //        private class OnFooterAppearedEvent extends HippyViewEvent
    //        {
    //            public OnFooterAppearedEvent(String eventName)
    //            {
    //                super(eventName);
    //            }
    //        }

    private HippyQBWaterfallEvent getOnRefreshEvent() {
      if (mOnRefreshEvent == null) {
        mOnRefreshEvent = new HippyQBWaterfallEvent("onRefresh");
      }
      return mOnRefreshEvent;
    }

    //        private class OnRefreshEvent extends HippyViewEvent
    //        {
    //            public OnRefreshEvent(String eventName)
    //            {
    //                super(eventName);
    //            }
    //        }

    private HippyQBWaterfallEvent getOnScrollForReportEvent() {
      if (mOnScrollForReportEvent == null) {
        mOnScrollForReportEvent = new HippyQBWaterfallEvent("onScrollForReport");
      }
      return mOnScrollForReportEvent;
    }

    //        private class OnScrollForReportEvent extends HippyViewEvent
    //        {
    //            public OnScrollForReportEvent(String eventName)
    //            {
    //                super(eventName);
    //            }
    //        }

    public OnListScrollListener getOnListScrollListener() {
      if (mOnListScrollListener == null) {
        mOnListScrollListener = new OnListScrollListener() {
          @Override
          public void onStartDrag() {

          }

          @Override
          public void onScroll(int i, int i1) {
            if (mParentRecyclerView instanceof HippyQBWaterfallView
              && ((HippyQBWaterfallView) mParentRecyclerView)
              .enableOnSrollReport()) {
              checkScrollForReport();
            }
          }

          @Override
          public void onScrollEnd() {
            checkScrollForReport();
          }

          @Override
          public void onDragEnd() {

          }

          @Override
          public void onStartFling() {

          }
        };
      }
      return mOnListScrollListener;
    }

    private HippyQBWaterfallEvent getOnEndReachedEvent() {
      if (mOnEndReachedEvent == null) {
        mOnEndReachedEvent = new HippyQBWaterfallEvent("onEndReached");
      }
      return mOnEndReachedEvent;
    }

    //        private class OnEndReachedEvent extends HippyViewEvent
    //        {
    //            public OnEndReachedEvent(String eventName)
    //            {
    //                super(eventName);
    //            }
    //        }
  }

  private class NodeHolder extends ContentHolder {

    public RenderNode mBindNode;
    public boolean isCreated = true;

    @Override
    public String toString() {
      if (DEBUG) {
        return "NodeHolder@" + Integer.toHexString(hashCode()) + " created=" + isCreated
          + " node=" + DebugUtil.stringifyR(mBindNode);
      } else {
        return "NodeHolder@" + Integer.toHexString(hashCode()) + " created=" + isCreated
          + " node=" + mBindNode.toString();
      }
    }
  }

  public static class RNWFRecyclerPool extends RecycledViewPool {

    @Override
    public void putRecycledView(ViewHolder scrap, Adapter adapter) {
      final int viewType = scrap.getItemViewType();
      final ArrayList scrapHeap = getScrapHeapForType(viewType);
      if (mMaxScrap.get(viewType) <= scrapHeap.size()) {
        // 如果scrapHeap已经满了，就不再向里面添加，此时这个viewHolder就被废弃，需要通知出去给adapter
        ViewHolder head = (ViewHolder) scrapHeap.get(0);
        scrapHeap.remove(0);
        if (adapter != null && adapter instanceof Adapter
          && head instanceof ViewHolderWrapper) {
          ((HippyWaterfallAdapter) adapter)
            .onViewAbandonHelper((ViewHolderWrapper) head);//scrap);
        }
        //return;
      }
      if (DEBUG) {
        Log.d(TAG, "putRecycledView...." + scrap + " contentView=" + DebugUtil
          .stringifyR(scrap.mContentHolder.mContentView));
      }
      scrap.mPosition = NO_POSITION;
      //scrap.mDraggedPosition = NO_POSITION;
      scrap.mOldPosition = NO_POSITION;
      scrap.mItemId = NO_ID;
      scrap.clearFlagsForSharedPool();
      scrapHeap.add(scrap);
    }
  }

  public static class ExposureForReport extends HippyViewEvent {

    public int mStartEdgePos = 0;
    public int mEndEdgePos = 0;
    public int mFirstVisibleRowIndex = 0;
    public int mLastVisibleRowIndex = 0;
    public int mVelocity = 0;
    public int mScrollState = 0;
    public HippyArray mVisibleRowFrames = null;

    public ExposureForReport(int tag, int startEdgePos, int endEdgePos, int firstVisiblePos,
      int lastVisiblePos, int velocity, int scrollState, HippyArray visibleItemArray) {
      super("onExposureReport");
      mStartEdgePos = startEdgePos;
      mEndEdgePos = endEdgePos;
      mFirstVisibleRowIndex = firstVisiblePos;
      mLastVisibleRowIndex = lastVisiblePos;
      mVelocity = velocity;
      mScrollState = scrollState;
      mVisibleRowFrames = visibleItemArray;
    }
  }

  private OnInitialListReadyEvent getOnInitialListReadyEvent() {
    if (mOnInitialListReadyEvent == null) {
      mOnInitialListReadyEvent = new OnInitialListReadyEvent("initialListReady");
    }
    return mOnInitialListReadyEvent;
  }

  private class OnInitialListReadyEvent extends HippyViewEvent {

    public OnInitialListReadyEvent(String eventName) {
      super(eventName);
    }
  }

  static class HippyQBWaterfallEvent extends HippyViewEvent {

    String eventName;

    public HippyQBWaterfallEvent(String name) {
      super(name);
      eventName = name;
    }

    @Override
    public void send(View view, Object param) {
      if (DEBUG) {
        Log.d(TAG, "sendEvent: name=" + eventName + " view=" + DebugUtil.stringifyS(view)
          + " param=" + param);
      }
      super.send(view, param);
    }
  }
}

package com.tencent.mtt.hippy.views.waterfall;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.mtt.hippy.HippyRootView;
import com.tencent.mtt.hippy.annotation.HippyController;
import com.tencent.mtt.hippy.annotation.HippyControllerProps;
import com.tencent.mtt.hippy.common.HippyArray;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.ControllerManager;
import com.tencent.mtt.hippy.uimanager.HippyViewController;
import com.tencent.mtt.hippy.uimanager.RenderNode;
import com.tencent.mtt.hippy.utils.PixelUtil;
import com.tencent.mtt.supportui.views.recyclerview.IRecyclerViewFooter;

/**
 * Created by leonardgong on 2017/12/7 0007.
 */
@HippyController(name = WaterFallComponentName.WATERFALL_CONTAINER)
public class HippyQBWaterfallViewController extends HippyViewController<HippyQBWaterfallView> {

  static final String TAG = WaterFallComponentName.WATERFALL_CONTAINER;

  @Override
  protected void addView(ViewGroup parentView, View view, int index) {
    //        super.addView(parentView, view, index);
  }

  @Override
  public int getChildCount(HippyQBWaterfallView viewGroup) {
    return ((HippyQBWaterfallView.HippyWaterfallAdapter) viewGroup.getAdapter())
      .getRecyclerItemCount();
  }

  @Override
  public View getChildAt(HippyQBWaterfallView viewGroup, int i) {
    return ((HippyQBWaterfallView.HippyWaterfallAdapter) viewGroup.getAdapter())
      .getRecyclerItemView(i);
  }

  @Override
  protected View createViewImpl(Context context) {
    return new HippyQBWaterfallView(context);
  }

  @Override
  public RenderNode createRenderNode(int id, HippyMap props, String className,
    HippyRootView hippyRootView, ControllerManager controllerManager,
    boolean lazy) {
    return new HippyQBWaterfallViewNode(id, props, className, hippyRootView, controllerManager,
      lazy);
  }

  @Override
  public void onBatchComplete(HippyQBWaterfallView view) {
    Log.d(TAG, "onBatchComplete #" + view.getId());
    super.onBatchComplete(view);
    view.setListData();
  }

  //    @HippyControllerProps(name = "setData", defaultType = HippyControllerProps.NUMBER, defaultNumber = 0)
  //    public void setListData(HippyQBWaterfallView listview, double timeStamp)
  //    {
  //        listview.setListData();
  //    }

  @HippyControllerProps(name = "containBannerView", defaultType = HippyControllerProps.BOOLEAN, defaultBoolean = false)
  public void setContainBannerView(HippyQBWaterfallView listview, boolean containBannerView) {
    ((HippyQBWaterfallLayoutManager) listview.getLayoutManager())
      .setContainBannerView(containBannerView);
  }

  @HippyControllerProps(name = "itemGap", defaultType = HippyControllerProps.NUMBER, defaultNumber = 0)
  public void setItemGap(HippyQBWaterfallView listview, int itemGap) {
    ((HippyQBWaterfallLayoutManager) listview.getLayoutManager())
      .setItemGap((int) PixelUtil.dp2px(itemGap));
  }

  @HippyControllerProps(name = "paddingStartZero", defaultType = HippyControllerProps.BOOLEAN, defaultBoolean = true)
  public void setPaddingStartZero(HippyQBWaterfallView listview, boolean paddingStartZero) {
    ((HippyQBWaterfallLayoutManager) listview.getLayoutManager())
      .setPaddingStartZero(paddingStartZero);
  }

  @HippyControllerProps(name = "bannerViewMatch", defaultType = HippyControllerProps.BOOLEAN, defaultBoolean = false)
  public void setBannerViewMatch(HippyQBWaterfallView listview, boolean bannerViewMatch) {
    ((HippyQBWaterfallLayoutManager) listview.getLayoutManager())
      .setBannerViewMatch(bannerViewMatch);
  }

  @HippyControllerProps(name = "numberOfColumns", defaultType = HippyControllerProps.NUMBER, defaultNumber = 2)
  public void setNumberOfColumns(HippyQBWaterfallView listview, int number) {
    ((HippyQBWaterfallLayoutManager) listview.getLayoutManager()).setColumns(number);
  }

  @HippyControllerProps(name = "enableLoadingFooter")
  public void setEnableLoadingFooter(HippyQBWaterfallView listView, boolean enableFooter) {
    if (enableFooter) {
      listView.mEnableFooter = true;
      listView.setLoadingStatus(IRecyclerViewFooter.LOADING_STATUS_FINISH, "");
      if (listView.mRefreshColors != null) {
//                int color = HippyQBSkinHandler.getColor(listView.mRefreshColors);
//                listView.setCustomRefreshColor(color, 0, 0);
      }
    } else {
      listView.setLoadingStatus(IRecyclerViewFooter.LOADING_STATUS_NONE, "");
      listView.mEnableFooter = false;
    }
  }

  @HippyControllerProps(name = "enableRefresh")
  public void setEnableRefresh(HippyQBWaterfallView listView, boolean enableRefresh) {
    if (enableRefresh && listView.mEnableRefresh) { // 已有refreshHeader时，不再重复构建refreshHeader

      return;
    }
    listView.setRefreshEnabled(enableRefresh);
    if (listView.mRefreshColors != null && enableRefresh) {
//            int color = HippyQBSkinHandler.getColor(listView.mRefreshColors);
//            listView.setCustomRefreshColor(color, 0, 0);
    }
  }

  @HippyControllerProps(name = "refreshColors")
  public void setRefreshColors(HippyQBWaterfallView listView, HippyArray refreshColors) {
//        int color = HippyQBSkinHandler.getColor(refreshColors);
    listView.setRefreshColors(refreshColors);
//        listView.setCustomRefreshColor(color, 0, 0);
  }

  @HippyControllerProps(name = "refreshColor")
  public void setRefreshColor(HippyQBWaterfallView listView, int color) {
    listView.setCustomRefreshColor(color, 0, 0);
  }

  @HippyControllerProps(name = "preloadItemNumber")
  public void setPreloadItemNumber(HippyQBWaterfallView listView, int preloadItemNumber) {
    listView.setPreloadItemNumber(preloadItemNumber);
  }

  @HippyControllerProps(name = "enableOnScrollForReport")
  public void setEnableOnScrollForReport(HippyQBWaterfallView listView, boolean enable) {
    listView.setEnableScrollForReport(enable);
  }

  @HippyControllerProps(name = "enableExposureReport")
  public void setOnExposureReport(HippyQBWaterfallView listView, boolean enable) {
    listView.setEnableExposureReport(enable);
  }

  //    @HippyControllerProps(name = "rowShouldSticky")
  //    public void setRowShouldSticky(HippyQBWaterfallView listView, boolean enable)
  //    {
  //        listView.setHasSuspentedItem(enable);
  //    }

  // 无认知复杂度
  // #lizard forgives
  @Override
  public void dispatchFunction(HippyQBWaterfallView listView, String functionName,
    HippyArray dataArray) {
    Log.e(TAG, "dispatchFunction " + functionName + dataArray.toString());
    super.dispatchFunction(listView, functionName, dataArray);

    int status;
    String text;
    int refreshResult;
    switch (functionName) {
      case "endReachedCompleted": { // 加载更多完成
        status = dataArray.getInt(0);
        text = dataArray.getString(1);
        refreshResult = 1;
        switch (status) {
          case 0:
            refreshResult = 2;
            break;
          case 1:
            refreshResult = 4;
            break;
          case 2:
            refreshResult = 6;
            break;
          case 3:
            refreshResult = 100;
            break;
          case 4:
            refreshResult = 0;
        }

        listView.setLoadingStatus(refreshResult, text);
        break;
      }
      case "refreshCompleted": {
        // 下拉刷新完成
        handleRefreshCompleted(listView, dataArray);
        break;
      }
      case "startRefresh": {
        //立即滑到顶部，开始从头刷新
        Log.e("leo", "startRefresh");
//                listView.startRefresh(DEFAULT_REFRESH_TYPE);
        break;
      }
      case "startRefreshWithType": {
        //立即滑到顶部，开始从头刷新，使用自定义loading动画
        int type = dataArray.getInt(0);
        listView.startRefresh(type);
        break;
      }
      case "startLoadMore": {
        // 加载更多数据
        listView.startLoadMore();
        break;
      }
      case "scrollToIndex": {
        // list滑动到某个item
        int xIndex = dataArray.getInt(0);
        int yIndex = dataArray.getInt(1);
        boolean animated = dataArray.getBoolean(2);
        listView.scrollToIndex(xIndex, yIndex, animated);
        break;
      }
      case "scrollToContentOffset": {
        // list滑动到某个距离
        double xOffset = dataArray.getDouble(0);
        double yOffset = dataArray.getDouble(1);
        boolean animated = dataArray.getBoolean(2);
        listView.scrollToContentOffset(xOffset, yOffset, animated);
        break;
      }
      case "callExposureReport": {
        // 主动触发一次曝光，发送给前端
        listView.onScrollStateChanged(listView.getScrollState(), listView.getScrollState());
        break;
      }
      case "setRefreshPromptInfo": {
        String descriptionText = dataArray.getString(0);
        int descriptionTextColor = dataArray.getInt(1);
        int descriptionTextFontSize = dataArray.getInt(2);
        String imgUrl = dataArray.getString(3);
        int imgWidth = dataArray.getInt(4);
        int imgHeight = dataArray.getInt(5);
        listView.setRefreshPromptInfo(descriptionText, descriptionTextColor,
          descriptionTextFontSize, imgUrl, imgWidth, imgHeight);
        break;
      }
      default:
        break;
    }
  }


  private void handleRefreshCompleted(HippyQBWaterfallView listView, HippyArray dataArray) {
    int status;
    String text;
    int refreshResult;
    status = dataArray.getInt(0);
    text = dataArray.getString(1);
    int refreshDuration = dataArray.getInt(2);
    int textColor = dataArray.getInt(3);
    String imageUrl = dataArray.getString(4);
    int bgBeginColor = dataArray.getInt(5);
    int bgEndColor = dataArray.getInt(6);
    int textFontSize = dataArray.getInt(7);
    int isMainTab = dataArray.getInt(8);
    int hideIndex = dataArray.getInt(9);
    refreshResult = 1;
//        switch (status) {
//            case 0:
//                refreshResult = HippyQBRefreshHeader.REFRESH_RESULT_SUCCESSS;
//                break;
//            case 1:
//                refreshResult = HippyQBRefreshHeader.REFRESH_RESULT_FAILED;
//        }
//
//        listView.completeRefresh(refreshResult, text, bgBeginColor, bgEndColor, textColor,
//                textFontSize, imageUrl, true, refreshDuration, isMainTab, null, hideIndex);
  }
}

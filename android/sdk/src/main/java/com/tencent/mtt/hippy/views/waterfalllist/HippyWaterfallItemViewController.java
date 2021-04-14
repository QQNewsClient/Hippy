package com.tencent.mtt.hippy.views.waterfalllist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.tencent.mtt.hippy.HippyRootView;
import com.tencent.mtt.hippy.annotation.HippyController;
import com.tencent.mtt.hippy.annotation.HippyControllerProps;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.ControllerManager;
import com.tencent.mtt.hippy.uimanager.HippyViewController;
import com.tencent.mtt.hippy.uimanager.RenderNode;

@HippyController(name = WaterFallComponentName.ITEM, isLazyLoad = true)
public class HippyWaterfallItemViewController extends
  HippyViewController<HippyWaterfallItemView> {

  static final String TAG = WaterFallComponentName.ITEM;

  @Override
  protected View createViewImpl(Context context) {
    return new HippyWaterfallItemView(context);
  }

  @HippyControllerProps(name = "type", defaultType = HippyControllerProps.NUMBER, defaultNumber = 0)
  public void setListItemType(HippyWaterfallItemView listItemView, int type) {
    listItemView.setType(type);
  }

  @Override
  public RenderNode createRenderNode(int id, HippyMap props, String className,
    HippyRootView hippyRootView, ControllerManager controllerManager,
    boolean lazy) {
    return new HippyWaterfallItemRenderNode(id, props, className, hippyRootView,
      controllerManager, lazy);
  }

  @Override
  protected boolean shouldInterceptLayout(View view, int x, int y, int width, int height) {
    ViewParent vp = view.getParent();
    if (vp != null && vp instanceof ViewGroup) {
      ViewGroup vg = (ViewGroup) vp;
      int leftPadding = vg.getPaddingLeft();
      if (leftPadding > 0) {
        if (DebugUtil.DEBUG) {
          Log.d(TAG, "shouldInterceptLayout: #" + view.getId() + " leftPadding="
            + leftPadding);
        }
        x += leftPadding;
        view.layout(x, y, x + width, y + height);
        return true;
      }
    }
    return false;
  }
}

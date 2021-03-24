package com.tencent.mtt.hippy.views.waterfall;

import android.util.Log;

import com.tencent.mtt.hippy.HippyRootView;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.ControllerManager;
import com.tencent.mtt.hippy.uimanager.RenderNode;

/**
 * Created by phantomqi on 2018/1/8.
 */

public class HippyQBWaterfallViewNode extends RenderNode {

  static final String TAG = "QBWaterfallViewNode";

  public HippyQBWaterfallViewNode(int mId, HippyMap mPropsToUpdate, String className,
    HippyRootView mRootView, ControllerManager componentManager,
    boolean isLazyLoad) {
    super(mId, mPropsToUpdate, className, mRootView, componentManager, isLazyLoad);
  }

  @Override
  protected void addChildToPendingList(RenderNode renderNode) {
    //        super.addPendChild(renderNode);
  }

  @Override
  public boolean removeChild(RenderNode uiNode) {
    if (DebugUtil.DEBUG) {
      Log.d(TAG, "removeChild child=" + DebugUtil.stringifyS(uiNode) + " @" + DebugUtil
        .stackTrace(0));
    }
    if (uiNode instanceof HippyQBWaterfallItemRenderNode) {
      HippyQBWaterfallItemRenderNode listItemRenderNode = (HippyQBWaterfallItemRenderNode) uiNode;
      listItemRenderNode.setRecycleItemTypeChangeListener(null);
    }
    return super.removeChild(uiNode);
  }

  @Override
  public void remove(int index) {
    if (DebugUtil.DEBUG) {
      Log.d(TAG, "remove #" + index + " @" + DebugUtil.stackTrace(0));
    }
    super.remove(index);
  }
}

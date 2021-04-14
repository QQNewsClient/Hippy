package com.tencent.mtt.hippy.views.waterfalllist;

import android.util.Log;

import com.tencent.mtt.hippy.HippyRootView;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.ControllerManager;
import com.tencent.mtt.hippy.uimanager.RenderNode;

/**
 * Created by phantomqi on 2018/1/8.
 */

public class HippyWaterfallViewNode extends RenderNode {

  static final String TAG = "QBWaterfallViewNode";

  public HippyWaterfallViewNode(int mId, HippyMap mPropsToUpdate, String className,
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
    if (uiNode instanceof HippyWaterfallItemRenderNode) {
      HippyWaterfallItemRenderNode listItemRenderNode = (HippyWaterfallItemRenderNode) uiNode;
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

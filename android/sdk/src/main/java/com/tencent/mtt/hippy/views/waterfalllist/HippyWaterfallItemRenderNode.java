package com.tencent.mtt.hippy.views.waterfalllist;

import android.util.Log;

import com.tencent.mtt.hippy.HippyRootView;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.ControllerManager;
import com.tencent.mtt.hippy.uimanager.RenderNode;

public class HippyWaterfallItemRenderNode extends RenderNode {

  static final String TAG = "HippyWaterfallItemNode";
  IRecycleItemTypeChange mRecycleItemTypeChangeListener;

  public HippyWaterfallItemRenderNode(int mId, HippyMap mPropsToUpdate, String className,
    HippyRootView mRootView,
    ControllerManager componentManager, boolean isLazyLoad) {
    super(mId, mPropsToUpdate, className, mRootView, componentManager, isLazyLoad);
  }

  @Override
  public void updateLayout(int x, int y, int w, int h) {
    if (DebugUtil.DEBUG) {
      Log.d(TAG, "updateLayout(" + x + "," + y + "," + w + "," + h + ") #" + getId());
    }
    super.updateLayout(x, 0, w, h);
    //this.mY = 0;
  }

  @Override
  public String toString() {
    return "[type:" + getType() + "]" + super.toString();
  }

  public int getType() {
    int type = -1;
    HippyMap props = getProps();
    if (props != null && props.containsKey("type")) {
      type = props.getInt("type");
    }
    return type;
  }

  @Override
  public void updateNode(HippyMap map) {
    int oldType = getProps().getInt("type");
    int newType = map.getInt("type");
    if (mRecycleItemTypeChangeListener != null && oldType != newType) {
      mRecycleItemTypeChangeListener.onRecycleItemTypeChanged(oldType, newType, this);
    }
    super.updateNode(map);
  }

  public void setRecycleItemTypeChangeListener(
    IRecycleItemTypeChange recycleItemTypeChangeListener) {
    mRecycleItemTypeChangeListener = recycleItemTypeChangeListener;
  }

  public interface IRecycleItemTypeChange {

    void onRecycleItemTypeChanged(int oldType, int newType,
      HippyWaterfallItemRenderNode listItemNode);
  }

}

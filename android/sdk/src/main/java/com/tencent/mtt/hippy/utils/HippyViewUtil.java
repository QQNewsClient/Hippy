package com.tencent.mtt.hippy.utils;

import android.view.View;

import com.tencent.mtt.hippy.HippyEngineContext;
import com.tencent.mtt.hippy.HippyInstanceContext;
import com.tencent.mtt.hippy.uimanager.RenderNode;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerViewItem;

public class HippyViewUtil {

  public static HippyEngineContext getEngineContext(View view) {
    if (view.getContext() instanceof HippyInstanceContext) {
      return ((HippyInstanceContext) view.getContext()).getEngineContext();
    }
    return null;
  }

  public static RenderNode getRenderNode(View view) {
    HippyEngineContext engineContext = getEngineContext(view);
    if (engineContext != null) {
      return engineContext.getRenderManager().getRenderNode(getNodeId(view));
    }
    return null;
  }

  public static int getNodeId(View view) {
    if (view instanceof RecyclerViewItem) {
      View child = ((RecyclerViewItem) view).getChildAt(0);
      if (child != null) {
        return child.getId();
      }
    }
    return view.getId();
  }

}

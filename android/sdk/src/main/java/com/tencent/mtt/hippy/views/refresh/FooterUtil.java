package com.tencent.mtt.hippy.views.refresh;

import android.view.View;

import com.tencent.mtt.hippy.uimanager.HippyViewEvent;
import com.tencent.mtt.hippy.uimanager.PullFooterRenderNode;
import com.tencent.mtt.hippy.uimanager.RenderNode;
import com.tencent.mtt.hippy.utils.HippyViewUtil;
import com.tencent.mtt.hippy.views.list.HippyListView;
import com.tencent.mtt.hippy.views.waterfalllist.HippyWaterfallView;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerViewBase;

public class FooterUtil {

  public static boolean isFooterView(View view) {
    RenderNode renderNode = HippyViewUtil.getRenderNode(view);
    return renderNode instanceof PullFooterRenderNode;
  }

  public static void sendFooterReleasedEvent(HippyPullFooterView footerView) {
    IFooterContainer footerContainer = null;
    if (footerView.getParentView() instanceof IFooterContainer) {
      footerContainer = (IFooterContainer) footerView.getParentView();
    }
    int curState = footerContainer != null
      ? footerContainer.getFooterState() : HippyListView.REFRESH_STATE_IDLE;

    if (curState == HippyListView.REFRESH_STATE_IDLE) {
      if (footerContainer != null) {
        footerContainer.setFooterState(HippyListView.REFRESH_STATE_LOADING);
      }
      new HippyViewEvent(HippyListView.EVENT_TYPE_FOOTER_RELEASED).send(footerView, null);
    }

    if (footerContainer instanceof HippyWaterfallView) {
      ((HippyWaterfallView) footerContainer).startLoadMore();
    }
  }

  public static void checkFooterBinding(RecyclerViewBase list, View view) {
    if (view instanceof HippyPullFooterView) {
      ((HippyPullFooterView) view).setParentView(list);
    }
  }

}

package com.tencent.mtt.hippy.views.wormhole;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.views.view.HippyViewGroup;

public class HippyWormholeView extends HippyViewGroup {
  private String mBusinessId;

  public HippyWormholeView(Context context) {
    super(context);
  }

  public String getBusinessId() {
    return mBusinessId;
  }

  public void setBusinessId(String id) {
    mBusinessId = id;
  }

}

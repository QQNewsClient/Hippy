package com.tencent.mtt.hippy.views.waterfall;

import android.content.Context;
import android.util.Log;

import com.tencent.mtt.hippy.views.view.HippyViewGroup;

public class HippyQBWaterfallItemView extends HippyViewGroup {

  static final String TAG = "HippyWaterfallItemView";
  private int mType;

  public HippyQBWaterfallItemView(Context context) {
    super(context);
    //setBackgroundColor(Color.BLACK);
  }

  public void setType(int type) {
    mType = type;
  }

  public int getType() {
    return mType;
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    if (DebugUtil.DEBUG) {
      Log.d(TAG, "onLayout(" + changed + "," + left + "," + top + "," + right + "," + bottom
        + ") #" + getId());
      //Log.e(TAG, "onLayout(" + changed + "," + left + "," + top + "," + right + "," + bottom + ") #" + getId(), new Throwable());
    }
    super.onLayout(changed, left, top, right, bottom);
  }
}

package com.tencent.mtt.hippy.views.refresh;

public interface IFooterContainer {

  int getFooterState();

  void setFooterState(int state);

  void onFooterRefreshFinish();

}

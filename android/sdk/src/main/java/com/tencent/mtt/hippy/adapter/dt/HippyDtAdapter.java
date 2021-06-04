package com.tencent.mtt.hippy.adapter.dt;

import android.view.View;

import com.tencent.mtt.hippy.common.HippyMap;

/**
 * Created by aprilgong on 2020/7/14.
 */
public interface HippyDtAdapter {
  /**
   * 设置页面及相关参数
   *
   * @param page   Page对象，支持View/Fragment/Activity
   * @param params Page参数
   */
  void setDtPage(Object page, HippyMap params);

  /**
   * 设置元素及其相关参数
   *
   * @param element 元素对象，支持View
   * @param params  元素参数
   */
  void setDtElement(Object element, HippyMap params);

  /**
   * 元素点击上报
   *
   * @param element 元素对象，支持View
   * @param params  元素参数
   */
  void onDtElementClick(Object element, HippyMap params);

  /**
   * 从该View的根视图开始遍历一次页面，找到新页面后会触发新页面进入以及新页面的元素曝光
   */
  void traversePage(View view);

}

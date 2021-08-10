package com.tencent.mtt.hippy.views.textinput;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author hengyangji
 *   on 2021/8/10
 */
public class KeyboardUtil {

  public static void showSoftInput(@NonNull View view) {
    showSoftInput(view, 0);
  }

  public static void showSoftInput(@NonNull final View view, int flags) {
    InputMethodManager imm = (InputMethodManager) view.getContext()
      .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      view.setFocusable(true);
      view.setFocusableInTouchMode(true);
      view.requestFocus();
      imm.showSoftInput(view, flags, new ResultReceiver(new Handler()) {
        protected void onReceiveResult(int resultCode, Bundle resultData) {
          if (resultCode == 1 || resultCode == 3) {
            KeyboardUtil.toggleSoftInput(view.getContext());
          }

        }
      });
      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
  }

  public static void toggleSoftInput(Context context) {
    InputMethodManager imm = (InputMethodManager) context
      .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.toggleSoftInput(0, 0);
    }
  }
}

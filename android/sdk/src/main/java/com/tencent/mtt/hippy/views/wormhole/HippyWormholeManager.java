package com.tencent.mtt.hippy.views.wormhole;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.mtt.hippy.HippyEngine;
import com.tencent.mtt.hippy.HippyEngineContext;
import com.tencent.mtt.hippy.HippyRootView;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.uimanager.HippyViewEvent;
import com.tencent.mtt.hippy.uimanager.RenderNode;
import com.tencent.mtt.hippy.utils.PixelUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HippyWormholeManager {
  public static final String WORMHOLE_TAG                       = "hippy_wormhole";

  public static final String WORMHOLE_PARAMS                    = "params";
  public static final String WORMHOLE_WORMHOLE_ID               = "wormholeId";
  public static final String WORMHOLE_CLIENT_DATA_RECEIVED      = "Wormhole.dataReceived";
  public static final String WORMHOLE_SERVER_BATCH_COMPLETE     = "onServerBatchComplete";
  public static final String EVENT_DATARECEIVED                 = "onClientMessageReceived";
  public static final String FUNCTION_SENDEVENT_TO_WORMHOLEVIEW = "sendEventToWormholeView";
  public static final String FUNCTION_ONCUSTOMEVENT             = "onCustomEvent";

  private static final AtomicInteger mWormholeIdCounter = new AtomicInteger(1000);
  private static volatile HippyWormholeManager INSTANCE;
  private HippyEngine mWormholeEngine;
  private HippyRootView mHippyRootView;
  private HippyWormholeContainer mWormholeContainer;

  private ConcurrentHashMap<String, Integer> mWormholeNodeMap = new ConcurrentHashMap<String, Integer>();

  //for native场景
  private ConcurrentHashMap<String, View> mNativeWormholeParentViewMap = new ConcurrentHashMap<String, View>();
  public ConcurrentHashMap<String, View> mNativeWormholeChildViewMap = new ConcurrentHashMap<String, View>();


  //存储业务方引擎
  private ArrayList<HippyEngine> mClientEngineList = new ArrayList<>();

  private HippyWormholeManager() {

  }

  public static HippyWormholeManager getInstance() {
    if (INSTANCE == null) {
      synchronized (HippyWormholeManager.class) {
        if (INSTANCE == null) {
          INSTANCE = new HippyWormholeManager();
        }
      }
    }
    return INSTANCE;
  }

  public void setServerEngine(HippyEngine engine,HippyRootView hippyRootView) {
    mWormholeEngine = engine;
    mHippyRootView = hippyRootView;
  }

  public HippyEngineContext getEngineContext() {
    return mWormholeEngine != null ? mWormholeEngine.getEngineContext() : null;
  }

  public HippyRootView getHippyRootView() {
    return mHippyRootView;
  }

  public void setWormholeContainer(HippyWormholeContainer container) {
    mWormholeContainer = container;
  }

  private void sendDataReceivedMessageToServer(HippyMap bundle) {
    JSONArray jsonArray = new JSONArray();
    jsonArray.put(bundle);
    mWormholeEngine.sendEvent(WORMHOLE_CLIENT_DATA_RECEIVED, jsonArray);
  }

  private void sendBatchCompleteMessageToClient(String wormholeId, View view) {
    int id = mWormholeNodeMap.get(wormholeId);
    HippyEngineContext engineContext = mWormholeEngine.getEngineContext();
    if (engineContext == null) {
      return;
    }

    RenderNode node = engineContext.getRenderManager().getRenderNode(id);
    if (node != null) {
      float width = node.getWidth();
      float height = node.getHeight();

      HippyMap layoutMeasurement = new HippyMap();
      layoutMeasurement.pushDouble("width", PixelUtil.px2dp(width));
      layoutMeasurement.pushDouble("height", PixelUtil.px2dp(height));
      HippyViewEvent event = new HippyViewEvent(WORMHOLE_SERVER_BATCH_COMPLETE);
      event.send(view, layoutMeasurement);
    }
  }

  private View findWormholeView(String wormholeId) {
    if (mWormholeEngine == null || !mWormholeNodeMap.containsKey(wormholeId)) {
      return null;
    }

    HippyEngineContext engineContext = mWormholeEngine.getEngineContext();
    if (engineContext == null) {
      return null;
    }

    int id = mWormholeNodeMap.get(wormholeId);
    View view = engineContext.getRenderManager().getControllerManager().findView(id);
    if (view == null) {
      RenderNode node = engineContext.getRenderManager().getRenderNode(id);
      if (node != null) {
        view = node.createViewRecursive();
        node.updateViewRecursive();
      }
    }
    return view;
  }

  private void addWormholeToParent(View wormholeView, View newParent) {
    if (newParent == null || !(newParent instanceof ViewGroup)) {
      return;
    }

    ViewGroup oldParent = (ViewGroup)(wormholeView.getParent());
    if (oldParent != newParent) {
      if (oldParent != null) {
        oldParent.removeView(wormholeView);
      }
      //todo 如果wormhole不是作为parent的唯一子节点的话，这里可能会有隐患
      ((ViewGroup)newParent).removeAllViews();
      ((ViewGroup)newParent).addView(wormholeView);
    }
    //将虫洞的宽高同步给oarent
    ViewGroup.LayoutParams layoutParams = newParent.getLayoutParams();
    if (layoutParams!=null && layoutParams.height != wormholeView.getHeight()) {
      layoutParams.height = wormholeView.getHeight();
      newParent.setLayoutParams(layoutParams);
    }
  }

  public void onServerBatchComplete(HippyWormholeView wormholeView) {
    String wormholeId = wormholeView.getWormholeId();
      if (mNativeWormholeParentViewMap.containsKey(wormholeId)) {
      //从native场景里找
      View parent = mNativeWormholeParentViewMap.get(wormholeId);
      if (parent != null) {
        addWormholeToParent(wormholeView, parent);
        sendBatchCompleteMessageToClient(wormholeId, parent);
      }
    }
  }

  public String generateWormholeId() {
    int id = mWormholeIdCounter.getAndIncrement();
    return "" + id;
  }

  public String getWormholeIdFromProps(HippyMap props) {
    HippyMap paramsMap = props.getMap(WORMHOLE_PARAMS);
    if (paramsMap == null) {
      return null;
    }

    String wormholeId = paramsMap.getString(WORMHOLE_WORMHOLE_ID);
    return wormholeId;
  }

  public String onWormholeNodeSetProps(HippyMap initProps, Integer id) {
    String wormholeId = getWormholeIdFromProps(initProps);
    if (!TextUtils.isEmpty(wormholeId)) {
      mWormholeNodeMap.put(wormholeId, id);
    }
    return wormholeId;
  }

  //如果是业务方收到了通知之后，应该要告知虫洞
  public void sendMessageToWormhole(HippyMap data) {
    if (mWormholeEngine != null && data != null) {
      mWormholeEngine.sendEvent(EVENT_DATARECEIVED, data);
    }
  }

  //如果是虫洞引擎收到了通知之后，应该要广播给所有的业务方
  public void sendMessageToAllClient(HippyMap data) {
    for (int i = 0; i < mClientEngineList.size(); i++) {
      if (mClientEngineList.get(i) != null) {
        mClientEngineList.get(i).sendEvent(EVENT_DATARECEIVED, data);
      }
    }
  }

  public void addNativeWormholeParent(String wormholeId, View parent) {
    mNativeWormholeParentViewMap.put(wormholeId, parent);
  }

  public void addNativeWormholeChild(String wormholeId, View child) {
    if (!TextUtils.isEmpty(wormholeId) && !mNativeWormholeChildViewMap.containsKey(wormholeId)) {
      mNativeWormholeChildViewMap.put(wormholeId, child);
    }
  }

  public View getNativeWormholeChild(String wormholeId) {
    if (!TextUtils.isEmpty(wormholeId) && mNativeWormholeChildViewMap.containsKey(wormholeId)) {
      return mNativeWormholeChildViewMap.get(wormholeId);
    }
    return null;
  }

  public View onNativeCreateWormholeParent(Context context) {
    FrameLayout frameLayout = new FrameLayout(context);
    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
    return frameLayout;
  }

  public void onNativeBindItemView(String wormholeId, View itemView, HippyMap hippyMap){
    addNativeWormholeParent(wormholeId, itemView);
    View childView = getNativeWormholeChild(wormholeId);
    if(childView!=null){
      addWormholeToParent(childView,itemView);
    } else {
      HippyWormholeManager.getInstance().sendDataReceivedMessageToServer(hippyMap);
    }
  }

  public void onNativeWormholeDestroy(String wormholeId) {
    if (!TextUtils.isEmpty(wormholeId) && mNativeWormholeParentViewMap.get(wormholeId) instanceof ViewGroup) {
      ViewGroup targetView = (ViewGroup) mNativeWormholeParentViewMap.get(wormholeId);
      if (targetView.getChildCount() > 0) {
        View child = targetView.getChildAt(0);
        if (child instanceof HippyWormholeView) {
          targetView.removeView(child);
          mWormholeContainer.addView(child);
          HippyEngineContext engineContext = mWormholeEngine.getEngineContext();
          if (engineContext != null) {
            engineContext.getRenderManager().getControllerManager().deleteChild(mWormholeContainer.getId(), child.getId());
          }
        }
      }
      if (!TextUtils.isEmpty(wormholeId)) {
        mNativeWormholeParentViewMap.remove(wormholeId);
      }
    }
  }

}

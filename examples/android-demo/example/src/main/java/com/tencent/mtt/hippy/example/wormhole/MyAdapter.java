package com.tencent.mtt.hippy.example.wormhole;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.example.R;
import com.tencent.mtt.hippy.views.wormhole.HippyWormholeManager;
import com.tencent.mtt.hippy.views.wormhole.HippyWormholeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.tencent.mtt.hippy.views.wormhole.HippyWormholeManager.WORMHOLE_WORMHOLE_ID;

/**
 * Created by aprilgong on 2020/9/23.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {

  private JSONArray mDatas = new JSONArray();
  private HippyWormholeEngine mEngine;

  public MyAdapter(HippyWormholeEngine engine) {
    initData();
    mEngine = engine;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case VIEW_TYPE_NATIVE:
        return create(parent.getContext(), R.layout.item_layout, parent);
      case VIEW_TYPE_WORMHOLE:
        Log.d("MyAdapter", " enter onCreateViewHolder");
        return create(parent.getContext());
    }
    return new VH(new View(parent.getContext()));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    int viewType = getItemViewType(position);
    if (viewType == VIEW_TYPE_WORMHOLE) {
      Log.d("MyAdapter", " enter onBindViewHolder " + position + " isCreated : " + holder.isCreated + " lastPos " + holder.lastPos);
    }
    boolean reuse = false;
    if (holder.isCreated) {
      holder.isCreated = false;
      holder.lastPos = holder.getAdapterPosition();
    } else {
      reuse = true;
    }

    switch (viewType) {
      case VIEW_TYPE_NATIVE:
        TextView tv = holder.itemView.findViewById(R.id.item_tv);
        if (position % 2 == 0) {
          holder.itemView.setBackgroundColor(Color.DKGRAY);
        } else {
          holder.itemView.setBackgroundColor(Color.GRAY);
        }
        tv.setText("position " + position);
        break;
      case VIEW_TYPE_WORMHOLE:
        if (reuse) {
          Log.d("MyAdapter", "hole reuse");
          if (((FrameLayout) holder.itemView).getChildAt(0) instanceof HippyWormholeView) {
            String businessId = ((HippyWormholeView) ((FrameLayout) holder.itemView).getChildAt(0)).getWormholeId();
            Log.d("MyAdapter", "hole reuse " + businessId);
            // todo aprilgong
            // todo update
          }
        } else {
          HippyWormholeManager.getInstance().onNativeBindItemView(getWormholeId(position),
            holder.itemView, getWormholeHippyMap(position));
        }
        break;
    }
  }

  protected void initData() {
    for (int i = 0; i < 40; i++) {
      try {
        JSONObject jsonObject = new JSONObject();
        if (i == 3 || i == 11 || i == 19 || i == 23 || i == 31) {
          jsonObject.put("type", 2);
          jsonObject.put("title", "[广告]--Nike,永不止步！");
          jsonObject.put("templateType", 2);
          jsonObject.put("coverUrl", "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=878011181,1009590567&fm=26&gp=0.jpg");
          jsonObject.put("wormholeId", HippyWormholeManager.getInstance().generateWormholeId());
        } else if (i == 7 || i == 15 || i == 27 || i == 35) {
          jsonObject.put("type", 2);
          jsonObject.put("title", "[广告]--腾讯公司！");
          jsonObject.put("templateType", 1);
          jsonObject.put("wormholeId", HippyWormholeManager.getInstance().generateWormholeId());
        } else {
          jsonObject.put("type", 1);
          jsonObject.put("title", "海关总署：驻休斯敦总领馆全体馆员核酸阴性");
        }
        jsonObject.put("index", i);
        mDatas.put(jsonObject);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  public HippyMap getWormholeHippyMap(int position) {
    JSONObject tempData = null;
    try {
      tempData = (JSONObject) mDatas.get(position);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    HippyMap hippyMap = new HippyMap();
    HippyMap data = new HippyMap();
    if (!TextUtils.isEmpty(tempData.optString("coverUrl"))) {
      data.pushString("coverUrl", tempData.optString("coverUrl"));
    }
    data.pushInt("templateType", tempData.optInt("templateType"));
    data.pushString("title", tempData.optString("title"));
    data.pushInt("type", 6);
    hippyMap.pushObject("data", data);
    String wormholeId = tempData.optString("wormholeId");
    hippyMap.pushString(WORMHOLE_WORMHOLE_ID, wormholeId);
    return hippyMap;
  }

  public String getWormholeId(int position) {
    JSONObject tempData = null;
    try {
      tempData = (JSONObject) mDatas.get(position);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    String wormholeId = tempData.optString("wormholeId");
    return wormholeId;
  }

  public static VH create(Context context, int layoutId, ViewGroup parent) {
    View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
    return new VH(itemView);
  }

  public static VH create(Context context) {
    View itemView = HippyWormholeManager.getInstance().onNativeCreateWormholeParent(context);
    return new VH(itemView);
  }

  public static class VH extends RecyclerView.ViewHolder {

    public VH(View itemView) {
      super(itemView);
    }

    public boolean isCreated = true;
    public int lastPos = -1;
  }

  @Override
  public int getItemCount() {
    if (mDatas != null) {
      return mDatas.length();
    }
    return 0;
  }

  public static final  int VIEW_TYPE_NATIVE = 1;
  public static final  int VIEW_TYPE_WORMHOLE = 2;

  @Override
  public int getItemViewType(int position) {
    try {
      if (((JSONObject) mDatas.get(position)).optInt("type") == 2) {
        return VIEW_TYPE_WORMHOLE;
      } else {
        return VIEW_TYPE_NATIVE;
      }
    } catch (Exception e) {

    }
    return VIEW_TYPE_NATIVE;
  }
}

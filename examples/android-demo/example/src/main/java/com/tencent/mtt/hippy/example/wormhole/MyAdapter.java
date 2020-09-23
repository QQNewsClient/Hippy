package com.tencent.mtt.hippy.example.wormhole;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.example.R;
import com.tencent.mtt.hippy.utils.ArgumentUtils;
import com.tencent.mtt.hippy.views.wormhole.HippyWormholeManager;
import com.tencent.mtt.hippy.views.wormhole.HippyWormholeProxy;
import com.tencent.mtt.hippy.views.wormhole.HippyWormholeView;

import java.util.List;

/**
 * Created by aprilgong on 2020/9/23.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {

  public static final int TYPE_COMMON = 0;
  public static final int TYPE_WORMHOLE = 1;

  private List<String> mData;
  private HippyWormholeEngine mEngine;

  private HippyWormholeProxy mWormholeProxy = HippyWormholeManager.getInstance();

  public MyAdapter(List<String> data, HippyWormholeEngine engine) {
    mData = data;
    mEngine = engine;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case TYPE_COMMON:
        return create(parent.getContext(), R.layout.item_layout, parent);
      case TYPE_WORMHOLE:
        Log.d("MyAdapter", " enter onCreateViewHolder");
        return create(parent.getContext(), R.layout.wormhole_layout, parent);
    }
    return new VH(new View(parent.getContext()));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    int viewType = getItemViewType(position);
    if (viewType == TYPE_WORMHOLE) {
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
      case TYPE_COMMON:
        TextView tv = holder.itemView.findViewById(R.id.item_tv);
        if (position % 2 == 0) {
          holder.itemView.setBackgroundColor(Color.DKGRAY);
        } else {
          holder.itemView.setBackgroundColor(Color.GRAY);
        }
        tv.setText("position " + position);
        break;
      case TYPE_WORMHOLE:
        if (reuse) {
          Log.d("MyAdapter", "hole reuse");
          if (((FrameLayout) holder.itemView).getChildAt(0) instanceof HippyWormholeView) {
            String businessId = ((HippyWormholeView) ((FrameLayout) holder.itemView).getChildAt(0)).getBusinessId();
            Log.d("MyAdapter", "hole reuse " + businessId);
            // todo aprilgong
            // todo update
          }
        } else {
          String initProps = "{\"style\":{\"height\":118.5952377319336,\"width\":387.547607421875},\"params\":{\"index\":2,\"data\":{\"templateType\":1,\"coverUrl\":\"https:\\/\\/timgsa.baidu.com\\/timg?image&quality=80&size=b9999_10000&sec=1596476976953&di=296ecd6b4a91719a814bfc0ff8d44904&imgtype=0&src=http%3A%2F%2Fimg1.imgtn.bdimg.com%2Fit%2Fu%3D756258046%2C2809017249%26fm%3D214%26gp%3D0.jpg\",\"title\":\"[广告]--Nike,永不止步！\"}},\"onServerBatchComplete\":true}";
          HippyMap map = ArgumentUtils.parseToMap(initProps);
          mWormholeProxy.createWormhole(map, (ViewGroup) holder.itemView);
        }
        break;
    }
  }

  public static VH create(Context context, int layoutId, ViewGroup parent) {
    View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
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
    if (mData != null) {
      return mData.size();
    }
    return 0;
  }

  @Override
  public int getItemViewType(int position) {
    if (position % 3 == 0) {
      return TYPE_WORMHOLE;
    }

    return TYPE_COMMON;
  }
}

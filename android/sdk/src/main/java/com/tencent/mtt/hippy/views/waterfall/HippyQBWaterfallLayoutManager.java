package com.tencent.mtt.hippy.views.waterfall;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.mtt.hippy.uimanager.PullFooterRenderNode;
import com.tencent.mtt.hippy.uimanager.RenderNode;
import com.tencent.mtt.hippy.utils.HippyViewUtil;
import com.tencent.mtt.hippy.views.refresh.FooterUtil;
import com.tencent.mtt.hippy.views.refresh.HippyPullFooterView;
import com.tencent.mtt.hippy.views.waterfall.HippyQBWaterfallView.HippyWaterfallAdapter;
import com.tencent.mtt.supportui.views.recyclerview.BaseLayoutManager;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerAdapter;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerView;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerViewBase;
import com.tencent.mtt.supportui.views.recyclerview.RecyclerViewItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tencent.mtt.supportui.views.recyclerview.RecyclerViewBase.LAYOUT_TYPE_WATERFALL;

/**
 * A {@link HippyQBWaterfallLayoutManager} implementation which provides similar functionality to
 * {@link android.widget.ListView}.
 */
public class HippyQBWaterfallLayoutManager extends BaseLayoutManager {

  private static final String TAG = "HippyQBWaterfallLayout";
  static final boolean DEBUG = DebugUtil.DEBUG;

  static final int MIN_COLUMN = 2;
  int mColumns = MIN_COLUMN;
  int mItemGap = 0;
  boolean mPaddingStartZero = true;
  boolean mBannerViewMatch = false;
  boolean mHasContainBannerView = false;
  ArrayList<Integer> mHeaderHeight = new ArrayList<Integer>();

  public HippyQBWaterfallLayoutManager(Context context) {
    this(context, VERTICAL, false);
  }

  /**
   * @param context Current context, will be used to access resources.
   * @param orientation Layout orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
   * @param reverseLayout When set to true, renders the layout from end to start.
   */
  public HippyQBWaterfallLayoutManager(Context context, int orientation, boolean reverseLayout) {
    super(context, orientation, false);
  }

  public boolean getContainBannerView() {
    return mHasContainBannerView;
  }

  public void setContainBannerView(boolean containBannerView) {
    mHasContainBannerView = containBannerView;
  }

  public int getColumns() {
    return mColumns;
  }

  public void setColumns(int columns) {
    this.mColumns = Math.max(2, columns);
  }

  public int getItemGap() {
    return mItemGap;
  }

  public void setItemGap(int gap) {
    mItemGap = Math.max(0, gap);
  }

  public void setPaddingStartZero(boolean paddingStartZero) {
    mPaddingStartZero = paddingStartZero;
  }

  public void setBannerViewMatch(boolean bannerViewMatch) {
    mBannerViewMatch = bannerViewMatch;
  }


  public int getHeaderHeight(int index) {
    if (index <= 0 || index > mHeaderHeight.size()) {
      return 0;
    }
    return mHeaderHeight.get(index - 1);
  }

  int[] calculateColumnHeightsBefore(int position,
    boolean caculateOffsetmap) {//TODO 优化重复计算，至少要确保在一次loop里面不需要重复计算
    // #lizard forgives
    int columnHeights[] = new int[mColumns];
    SparseArray<List<Integer>> items = new SparseArray<>();
    int n = 0;

    HippyWaterfallAdapter adapter = (HippyWaterfallAdapter) mRecyclerView.getAdapter();

    if (mHasContainBannerView) {
      position += 1;
    }

    for (int i = 0; i < position; i++) {
      int targetColumnIndex = 0;
      for (int j = 0; j < columnHeights.length; j++) {
        if (columnHeights[targetColumnIndex] > columnHeights[j]) {
          targetColumnIndex = j;
        }
      }
      //            if (caculateOffsetmap)
      //            {
      //                mRecyclerViewAdapter.mOffsetMap.append(i, columnHeights[targetColumnIndex]);
      //            }

      if (mHasContainBannerView) {
        if (i == 0 || i == 1) {
          n = 0;
        } else if (i > 1) {
          n = i - 1;
        }
      } else {
        n = i;
      }

      int myHeight = adapter.getItemHeight(n) + adapter
        .getItemMaigin(RecyclerAdapter.LOCATION_TOP, n)
        + adapter.getItemMaigin(RecyclerAdapter.LOCATION_BOTTOM, n);

      // todo learn：【重要逻辑】计算每一列view总高度（此处特殊处理了含footer的情况）
      RenderNode node = adapter.getItemNode(i);
      if (node instanceof PullFooterRenderNode) {
        int height = getHightestColumnHeight(columnHeights) + myHeight;
        Arrays.fill(columnHeights, height);
      } else {
        columnHeights[targetColumnIndex] += myHeight;
      }

      if (DEBUG) {
        List<Integer> itemLine = items.get(targetColumnIndex);
        if (itemLine == null) {
          itemLine = new ArrayList<>();
          items.put(targetColumnIndex, itemLine);
        }
        itemLine.add(myHeight);
      }
    }

    if (DEBUG) {
      Log.d(TAG, "calculateColumnHeightsBefore(" + position + ") starts");
      for (int c = 0; c < columnHeights.length; ++c) {
        Log.d(TAG, "calculateColumnHeightsBefore(" + position + ") column#" + c + " height="
          + columnHeights[c]
          + " items=" + items.get(c));
      }
      Log.d(TAG, "calculateColumnHeightsBefore(" + position + ") ends");
    }

    return columnHeights;
  }

  // calculate the height of every column after the item with index position.
  public int[] calculateColumnHeightsAfter(int position) {
    // #lizard forgives
    int columnHeights[] = new int[mColumns];
    SparseArray<List<Integer>> items = new SparseArray<>();
    int n = 0;
    HippyWaterfallAdapter adapter = (HippyWaterfallAdapter) mRecyclerView.getAdapter();

    if (mHasContainBannerView) {
      position += 1;
    }

    for (int i = 0; i <= position; i++) {
      int targetColumnIndex = 0;
      for (int j = 0; j < columnHeights.length; j++) {
        if (columnHeights[targetColumnIndex] > columnHeights[j]) {
          targetColumnIndex = j;
        }
      }

      if (mHasContainBannerView) {
        if (i == 0 || i == 1) {
          n = 0;
        } else if (i > 1) {
          n = i - 1;
        }
      } else {
        n = i;
      }

      int myHeight = adapter.getItemHeight(n) + adapter
        .getItemMaigin(RecyclerAdapter.LOCATION_TOP, n)
        + adapter.getItemMaigin(RecyclerAdapter.LOCATION_BOTTOM, n);

      RenderNode node = adapter.getItemNode(i);
      if (node instanceof PullFooterRenderNode) {
        int height = getHightestColumnHeight(columnHeights) + myHeight;
        Arrays.fill(columnHeights, height);
      } else {
        columnHeights[targetColumnIndex] += myHeight;
      }

      if (DEBUG) {
        List<Integer> itemLine = items.get(targetColumnIndex);
        if (itemLine == null) {
          itemLine = new ArrayList<>();
          items.put(targetColumnIndex, itemLine);
        }
        itemLine.add(myHeight);
      }
    }

    if (DEBUG) {
      Log.d(TAG, "calculateColumnHeightsAfter(" + position + ") starts");
      for (int c = 0; c < columnHeights.length; ++c) {
        Log.d(TAG, "calculateColumnHeightsAfter(" + position + ") column#" + c + " height="
          + columnHeights[c]
          + " items=" + items.get(c));
      }
      Log.d(TAG, "calculateColumnHeightsAfter(" + position + ") ends");
    }

    return columnHeights;
  }

  public static int getShortestColumnIndex(int[] columnHeights) {
    int shortestColumnIndex = 0;
    for (int j = 0; j < columnHeights.length; ++j) {
      if (columnHeights[shortestColumnIndex] > columnHeights[j]) {
        shortestColumnIndex = j;
      }
    }
    return shortestColumnIndex;
  }

  public static int getShortestColumnHeight(int columnHeights[]) {
    return columnHeights[getShortestColumnIndex(columnHeights)];
  }

  public static int getHightestColumnHeight(int columnHeights[]) {
    int heightestColumnIndex = 0;
    for (int j = 0; j < columnHeights.length; j++) {
      if (columnHeights[heightestColumnIndex] < columnHeights[j]) {
        heightestColumnIndex = j;
      }
    }
    return columnHeights[heightestColumnIndex];
  }

  @Override
  protected void updateRenderState(int layoutDirection, int requiredSpace,
    boolean canUseExistingSpace, RecyclerViewBase.State state) {
    super.updateRenderState(layoutDirection, requiredSpace, canUseExistingSpace, state);
    resetTargetColumn();
  }

  @Override
  protected void updateRenderStateToFillStart(int itemPosition, int offset) {
    super.updateRenderStateToFillStart(itemPosition, offset);
    if (mHasContainBannerView && itemPosition == 0) {
      ((WaterFallRenderState) mRenderState).targetColumn = 0;
    } else {
      resetTargetColumn();
    }
  }

  @Override
  protected void updateRenderStateToFillEnd(int itemPosition, int offset) {
    super.updateRenderStateToFillEnd(itemPosition, offset);
    if (mHasContainBannerView && itemPosition == 0) {
      ((WaterFallRenderState) mRenderState).targetColumn = 0;
    } else {
      resetTargetColumn();
    }
  }

  private void resetTargetColumn() {
    if (mHasContainBannerView && mRenderState.mCurrentPosition == 0) {
      ((WaterFallRenderState) mRenderState).targetColumn = 0;
    } else {
      int columnHeights[] = calculateColumnHeightsBefore(mRenderState.mCurrentPosition,
        false);
      ((WaterFallRenderState) mRenderState).targetColumn = getShortestColumnIndex(
        columnHeights);
    }
    //        if (DEBUG)
    //        {
    //            Integer[] a = new Integer[columnHeights.length];
    //            for (int i = 0; i < a.length; ++i)
    //                a[i] = columnHeights[i];
    //            Log.d(TAG, "resetTargetColumn columnHeights=" + Arrays.deepToString(a));
    //        }
  }

  // 无认知复杂度
  // #lizard forgives
  void compensateLayoutStart(WaterFallRenderState renderState) {//转屏的时候需要往前回溯几个，以免顶部出现空白
    if (renderState.mCurrentPosition <= 0
      || renderState.mLayoutDirection != RenderState.LAYOUT_END
      || renderState.mOffset >= 0) {
      return;
    }

    int columnHeights[] = calculateColumnHeightsBefore(renderState.mCurrentPosition, false);
    int maxHeight = columnHeights[0];
    int minHeight = columnHeights[0];
    for (int i = 1; i < columnHeights.length; ++i) {
      int one = columnHeights[i];
      if (one > maxHeight) {
        maxHeight = one;
      } else if (one < minHeight) {
        minHeight = one;
      }
    }

    int screenTop = minHeight - renderState.mOffset;
    if (maxHeight <= screenTop) {
      return;
    }

    final int rollbackLimit = mColumns;
    int resultPosition = 0, resultHeight = 0, resultColumn = renderState.targetColumn;
    for (int position = renderState.mCurrentPosition - 1;
      position > 0 && renderState.mCurrentPosition - position < rollbackLimit;
      --position) {
      columnHeights = calculateColumnHeightsBefore(position, false);
      maxHeight = columnHeights[0];
      minHeight = columnHeights[0];
      int minColumns = 0;
      for (int i = 1; i < columnHeights.length; ++i) {
        int one = columnHeights[i];
        if (one > maxHeight) {
          maxHeight = one;
        } else if (one < minHeight) {
          minHeight = one;
          minColumns = i;
        }
      }

      if (maxHeight <= screenTop) {
        resultPosition = position;
        resultHeight = minHeight;
        resultColumn = minColumns;
        break;
      }
    }

    if (renderState.mCurrentPosition - resultPosition >= rollbackLimit) {
      Log.e(TAG, "compensateLayoutStart: discard inappropriate sugguestion "
        + renderState.mCurrentPosition + " -> " + resultPosition);
      return;
    }

    int resultOffset = resultHeight - screenTop;
    Log.d(TAG, "compensateLayoutStart: position=" + renderState.mCurrentPosition + "->"
      + resultPosition + " mOffset=" + renderState.mOffset + "->"
      + resultOffset + " column=" + renderState.targetColumn + "->" + resultColumn);
    renderState.mCurrentPosition = resultPosition;
    renderState.mOffset = resultOffset;
    renderState.targetColumn = resultColumn;
  }

  /**
   * The magic functions :). Fills the given layout, defined by the renderState. This is fairly
   * independent from the rest of the {@link HippyQBWaterfallLayoutManager} and with little
   * change, can be made publicly available as a helper class.
   *
   * @param recycler Current recycler that is attached to RecyclerView
   * @param renderState Configuration on how we should fill out the available space.
   * @param state Context passed by the RecyclerView to control scroll steps.
   * @param stopOnFocusable If true, filling stops in the first focusable new child
   * @return Number of pixels that it added. Useful for scoll functions.
   */
  // #lizard forgives
  protected int fill(RecyclerViewBase.Recycler recycler, RenderState renderState,
    RecyclerViewBase.State state, boolean stopOnFocusable) {
    if (DEBUG) {
      renderState.log("fill starts: ");
    }
    Log.d(TAG, "fill starts: renderState.mOffset=" + renderState.mOffset);

    //mContainBannerView = false;

    compensateLayoutStart((WaterFallRenderState) renderState);

    final int itemWidth = (getWidth() - getPaddingLeft() - getPaddingRight()) / mColumns;
    final int itemGapH = mItemGap * (mColumns - 1) / mColumns; // todo

    // max offset we should set is mFastScroll + available
    final int start = renderState.mAvailable;
    if (renderState.mScrollingOffset != RenderState.SCOLLING_OFFSET_NaN) {
      // TODO ugly bug fix. should not happen
      if (renderState.mAvailable < 0) {
        renderState.mScrollingOffset += renderState.mAvailable;
      }
      recycleByRenderState(recycler, renderState);
    }
    int remainingSpace = renderState.mAvailable + renderState.mExtra;
    while (remainingSpace > 0) {
      if (renderState.hasMore(state) == RenderState.FILL_TYPE_NOMORE) {
        return remainingSpace;
      }

      int index = renderState.mCurrentPosition;
      int firstItemWidth = itemWidth;
      if (mHasContainBannerView && index == 0) {
        firstItemWidth = (getWidth() - getPaddingLeft() - getPaddingRight());
      }
      //            int currentRenderState = renderState.hasMore(state);
      View view = getNextView(recycler, renderState, state);
      if (view == null) {
        if (false && renderState.mScrapList == null) {
          throw new RuntimeException("received null view when unexpected");
        }
        // if we are laying out views in scrap, this may return null
        // which means there is
        // no more items to layout.
        break;
      }

      // todo learn：【重要逻辑】计算子view应占据的宽度，并给view的layoutParam赋值
      if (isFooterView(view)) {
        firstItemWidth = (getWidth() - getPaddingLeft() - getPaddingRight());
      }

      RecyclerViewBase.LayoutParams params = (RecyclerViewBase.LayoutParams) view
        .getLayoutParams();
      if (params instanceof LayoutParams) {
        ((LayoutParams) params).mLocateAtColumn = -1;
      }
      if (!params.isItemRemoved() && mRenderState.mScrapList == null) {
        if (mShouldReverseLayout == (renderState.mLayoutDirection
          == RenderState.LAYOUT_START)) {
          addView(view);
        } else {
          addView(view, 0);
        }
      }

      int viewType = params.mViewHolder.mViewType;
      int widthUsed = 0;

      if (viewType == RecyclerViewBase.ViewHolder.TYPE_NORMAL) {
        if (params instanceof LayoutParams) {
          int targetColumn = ((WaterFallRenderState) mRenderState).targetColumn;
          ((LayoutParams) params).mLocateAtColumn = targetColumn;
          setChildPadding(itemGapH, index, view, targetColumn);
        }
        if (getOrientation() == VERTICAL) {
          params.width = firstItemWidth - params.leftMargin - params.rightMargin;
        } else {
          params.height = firstItemWidth - params.topMargin - params.bottomMargin;
        }

        if (mRecyclerView.getAdapter() instanceof RecyclerAdapter
          && ((RecyclerAdapter) mRecyclerView.getAdapter())
          .isAutoCalculateItemHeight() && view instanceof RecyclerViewItem) {
          if (((RecyclerViewItem) view).getChildCount() > 0) {
            View contentView = ((RecyclerViewItem) view).getChildAt(0);
            ViewGroup.LayoutParams contentLayout = contentView.getLayoutParams();
            if (contentLayout != null) {
              contentLayout.width = params.width;
            } else {
              contentLayout = new ViewGroup.LayoutParams(params);
            }
            if (!mHasContainBannerView || index != 0 || !mBannerViewMatch) {
              if (contentLayout.width > 0) {
                contentLayout.width -= itemGapH;
              }
            }
            contentView.setLayoutParams(contentLayout);

            if (DEBUG) {
              Log.d(TAG, "contentView #" + index + " before measure: " + contentView
                .getMeasuredWidth() + "x"
                + contentView.getMeasuredHeight() + " (" + contentView
                .getWidth() + "x" + contentView.getHeight() + ")");
            }
            int widthSpec = View.MeasureSpec
              .makeMeasureSpec(contentLayout.width, View.MeasureSpec.AT_MOST);
            int heightSpec = View.MeasureSpec
              .makeMeasureSpec(contentView.getMeasuredHeight(),
                View.MeasureSpec.AT_MOST);
            contentView.measure(widthSpec, heightSpec);
            if (DEBUG) {
              Log.d(TAG, "contentView #" + index + " after measure: " + contentView
                .getMeasuredWidth() + "x"
                + contentView.getMeasuredHeight());
            }
          }
        }

        // todo learn：【重要逻辑】计算瀑布流已用宽度，用于后续布局子view
        if (isFooterView(view)) {
          widthUsed = 0;
        } else if (mHasContainBannerView && index == 0) {
          widthUsed = mRecyclerView.getMeasuredWidth();
        } else {
          widthUsed =
            mRecyclerView.getMeasuredWidth() * (getColumns() - 1) / getColumns();
        }
      }

      // TODO
      //            if (viewType == RecyclerView.ViewHolder.TYPE_FOOTER || viewType == RecyclerView.ViewHolder.TYPE_HEADERE)
      //            {
      //                if (view instanceof QBViewInterface)
      //                {
      //                    ((QBViewInterface) view).switchSkin();
      //                }
      //            }

      // todo learn：【重要逻辑】measure子view，确定宽高（widthUsed为瀑布流中，一行内已经使用了的宽度）
      measureChildWithMargins(view, widthUsed, 0);
      if (mRecyclerView.getAdapter() instanceof RecyclerAdapter
        && ((RecyclerAdapter) mRecyclerView.getAdapter()).isAutoCalculateItemHeight()) {
        if (view instanceof RecyclerViewItem) {
          if (((RecyclerViewItem) view).getChildCount() > 0) {
            recordItemSize(index, ((RecyclerViewItem) view).getChildAt(0));
            //((QBRecyclerAdapter) mRecyclerView.getAdapter()).forceUpdateOffsetMap();
          }
        } else {
          if (viewType == RecyclerViewBase.ViewHolder.TYPE_HEADERE) {
            int height = view.getMeasuredHeight();
            int headerIndex = Math.abs(index) - 1;
            while (headerIndex >= mHeaderHeight.size()) {
              mHeaderHeight.add(0);
            }
            mHeaderHeight.set(headerIndex, height);
          }
        }
        if (renderState.hasMore(state) == RenderState.FILL_TYPE_NOMORE) {
          //                    Log.e("leo", "FILL_TYPE_NOMORE " + state.mTotalHeight);
          mRecyclerView.mState.mTotalHeight = mRecyclerView.getAdapter()
            .getListTotalHeight();
//                    ((RecyclerAdapter) mRecyclerView.getAdapter()).mAutoCalcItemHeightFinish = true;
        } else {
          //                    Log.e("leo", "other FILL_TYPE " + state.mTotalHeight + " still autoCalcItemHeight");
//                    ((RecyclerAdapter) mRecyclerView.getAdapter()).mAutoCalcItemHeightFinish = false;
        }
      }

      int addViewLength = mOrientationHelper.getDecoratedMeasurement(view);
      int left, top, right, bottom;
      if (getOrientation() == VERTICAL) {

        // todo learn：【重要逻辑】计算子view的上下左右位置，用于后续layout
        if (isFooterView(view) || viewType == RecyclerViewBase.ViewHolder.TYPE_FOOTER) {
          left = getPaddingLeft();
          right = left + mOrientationHelper.getDecoratedMeasurementInOther(view);
          top = mOrientationHelper.getDecoratedEnd(getChildClosestToDefaultFooter());
          bottom = mOrientationHelper.getDecoratedEnd(getChildClosestToDefaultFooter())
            + addViewLength;
        } else if (viewType == RecyclerViewBase.ViewHolder.TYPE_HEADERE) {
          left = getPaddingLeft();
          right = left + mOrientationHelper.getDecoratedMeasurementInOther(view);
          if (renderState.mLayoutDirection == RenderState.LAYOUT_START) {
            bottom = renderState.mOffset;
            top = renderState.mOffset - addViewLength;
          } else {
            top = renderState.mOffset;
            bottom = renderState.mOffset + addViewLength;
          }
        } else {
          // the layout derection of waterfall will not care about
          // left-hander.
          left = ((WaterFallRenderState) mRenderState).targetColumn * itemWidth
            + getPaddingLeft();
          right = left + mOrientationHelper.getDecoratedMeasurementInOther(view);
          if (renderState.mLayoutDirection == RenderState.LAYOUT_START) {
            // renderState.mOffset = a;
            bottom = renderState.mOffset;
            top = renderState.mOffset - addViewLength;
          } else {
            top = renderState.mOffset;
            bottom = renderState.mOffset + addViewLength;
          }
        }
      } else {
        if (renderState.mLayoutDirection == RenderState.LAYOUT_START) {
          // renderState.mOffset = a;
          bottom = getHeight() - getPaddingBottom()
            - itemWidth * ((WaterFallRenderState) mRenderState).targetColumn;
          top = bottom - mOrientationHelper.getDecoratedMeasurementInOther(view);
          right = renderState.mOffset;
          left = renderState.mOffset - addViewLength;
        } else {
          top = ((WaterFallRenderState) mRenderState).targetColumn * itemWidth
            + getPaddingTop();
          bottom = top + mOrientationHelper.getDecoratedMeasurementInOther(view);
          left = renderState.mOffset;
          right = renderState.mOffset + addViewLength;
        }
      }
      // We calculate everything with View's bounding box (which includes
      // decor and margins)
      // To calculate correct layout position, we subtract margins.
      // todo learn：【重要逻辑】布局子view位置
      layoutDecorated(view, left + params.leftMargin, top + params.topMargin,
        right - params.rightMargin, bottom - params.bottomMargin);
      if (DEBUG) {
        Log.d(TAG, "laid out child at position " + getPosition(view) + ", with l:" + (left
          + params.leftMargin) + ", t:" + (view.getTop())
          + ", r:" + (right - params.rightMargin) + ", b:" + (bottom
          - params.bottomMargin));
      }

      if (!params.isItemRemoved()) {
        int cosume = 0;
        if (viewType == RecyclerViewBase.ViewHolder.TYPE_FOOTER) {
          int oldOffsetY = renderState.mOffset;
          renderState.mOffset = mOrientationHelper
            .getDecoratedEnd(getChildClosestToDefaultFooter());
          renderState.mOffset += mOrientationHelper.getDecoratedMeasurement(view);
//                    renderState.mOffsetHigh = renderState.mOffset;
          Log.d(TAG, "fill: mOffset=" + renderState.mOffset + " viewType=" + viewType + " @1");
          cosume = renderState.mOffset - oldOffsetY;
        } else if (viewType == RecyclerViewBase.ViewHolder.TYPE_HEADERE) {
          if (renderState.mLayoutDirection == RenderState.LAYOUT_START) {
            cosume = -mOrientationHelper.getDecoratedMeasurement(view);
          } else {
            cosume = mOrientationHelper.getDecoratedMeasurement(view);
          }
          renderState.mOffset += cosume;
//                    renderState.mOffsetHigh = renderState.mOffset;
          Log.d(TAG, "fill: mOffset=" + renderState.mOffset + " viewType=" + viewType + " @2");
        } else {
          if (renderState.mLayoutDirection == RenderState.LAYOUT_START) {
            int columnHeightBefore[] = calculateColumnHeightsAfter(
              renderState.mCurrentPosition - renderState.mItemDirection);
            int columnHeightAfter[] = calculateColumnHeightsBefore(
              renderState.mCurrentPosition - renderState.mItemDirection, false);
            int heightestHeightBefore = getHightestColumnHeight(columnHeightBefore);
            int heightestHeightAfter = getHightestColumnHeight(columnHeightAfter);
            cosume = heightestHeightAfter - heightestHeightBefore;
            renderState.mOffset = mOrientationHelper
              .getDecoratedStart(getChildClosestToStartInScreen());
//                        renderState.mOffsetHigh = renderState.mOffset;
            Log.d(TAG, "fill: mOffset=" + renderState.mOffset + " viewType=" + viewType + " @3");
          } else {
            if (mHasContainBannerView && index == 0) {
              cosume = addViewLength;
              renderState.mOffset += cosume;
//                            renderState.mOffsetHigh = renderState.mOffset;
              Log.d(TAG, "fill: mOffset=" + renderState.mOffset + " cosume=" + cosume + " viewType="
                + viewType + " @4");
            } else {
              int columnHeightBefore[] = calculateColumnHeightsBefore(
                renderState.mCurrentPosition - renderState.mItemDirection,
                false);
              int columnHeightAfter[] = calculateColumnHeightsAfter(
                renderState.mCurrentPosition - renderState.mItemDirection);
              int shortestHeightBefore = getShortestColumnHeight(columnHeightBefore);
              int shortestHeightAfter = getShortestColumnHeight(columnHeightAfter);
              cosume = shortestHeightAfter - shortestHeightBefore;
              renderState.mOffset += cosume;
//                            renderState.mOffsetHigh = getHightestColumnHeight(columnHeightAfter);
              Log.d(TAG, "fill: mOffset=" + renderState.mOffset + " cosume=" + cosume + " viewType="
                + viewType + " @5");
            }
          }
        }

        if (mHasContainBannerView && index == 0) {
          ((WaterFallRenderState) mRenderState).targetColumn = 0;
        } else {
          resetTargetColumn();
        }

        renderState.mAvailable -= Math.abs(cosume);
        // we keep a separate remaining space because mAvailable is
        // important for recycling
        remainingSpace -= Math.abs(cosume);

        if (renderState.mScrollingOffset != RenderState.SCOLLING_OFFSET_NaN) {
          renderState.mScrollingOffset += Math.abs(cosume);
          if (renderState.mAvailable < 0) {
            renderState.mScrollingOffset += renderState.mAvailable;
          }
          recycleByRenderState(recycler, renderState);
        }
      }

      if (stopOnFocusable && view.isFocusable()) {
        break;
      }

      if (state != null && state.getTargetScrollPosition() == getPosition(view)) {
        break;
      }
    }
    if (DEBUG) {
      // validateChildOrder();
    }
    return start - renderState.mAvailable;
  }

  void setChildPadding(int itemGapH, int index, View view, int targetColumn) {
    if (mHasContainBannerView && index == 0) {
      if (mBannerViewMatch) {
        view.setPadding(0, 0, 0, mItemGap);
      } else {
        view.setPadding(itemGapH / 2, 0, itemGapH / 2, mItemGap);
      }
    } else {
      if (mPaddingStartZero) {
        if (targetColumn == 0) {
          view.setPadding(0, 0, itemGapH, mItemGap);
        } else if (targetColumn == mColumns - 1) {
          view.setPadding(itemGapH, 0, 0, mItemGap);
        } else {
          view.setPadding(itemGapH / 2, 0, itemGapH / 2, mItemGap);
        }
      } else {
        int edgePadding = itemGapH * mColumns / (mColumns + 1);
        if (targetColumn == 0) {
          view.setPadding(edgePadding, 0, itemGapH - edgePadding, mItemGap);
        } else if (targetColumn == mColumns - 1) {
          view.setPadding(itemGapH - edgePadding, 0, edgePadding, mItemGap);
        } else {
          view.setPadding(itemGapH / 2, 0, itemGapH / 2, mItemGap);
        }
      }
    }
  }

  @Override
  public void measureChildWithMargins(View child, int widthUsed, int heightUsed) {
    RecyclerViewBase.LayoutParams lp = null;
    if (child == null) {
      return;
    }
    if (child.getLayoutParams() != null) {
      lp = (RecyclerViewBase.LayoutParams) child.getLayoutParams();
    } else {
      lp = generateDefaultLayoutParams();
    }
    final Rect insets = mRecyclerView.getItemDecorInsetsForChild(child);
    widthUsed += insets.left + insets.right;
    heightUsed += insets.top + insets.bottom;

    MeasureWH measureWH = new MeasureWH();
    measureWH.width = lp.width;
    measureWH.height = lp.height;
    if (mRecyclerView.getAdapter() instanceof RecyclerAdapter) {
      boolean enableAutoItemHeight = ((RecyclerAdapter) mRecyclerView.getAdapter())
        .isAutoCalculateItemHeight();
      if (enableAutoItemHeight) {
        if (child instanceof RecyclerViewItem) {
          if (((RecyclerViewItem) child).getChildCount() > 0) {
            View contentView = ((RecyclerViewItem) child).getChildAt(0);
            // todo learn：【重要逻辑】计算子view宽高，后续生成measureSpec用
            if (isFooterView(contentView)) {
              setFooterMeasureWH(contentView, measureWH);
            } else {
              measureWH.width = contentView.getMeasuredWidth()
                + child.getPaddingRight() + child
                .getPaddingLeft();//mItemGap * (mColumns - 1) / mColumns;
              measureWH.height = contentView.getMeasuredHeight()
                + mItemGap;// + ((RecyclerAdapter) mRecyclerView.getAdapter()).getDividerHeight(0)
            }
          }
        } else if (child instanceof HippyPullFooterView) {
          setFooterMeasureWH(child, measureWH);
        } else if (child instanceof ViewGroup) {
          ViewGroup viewGroup = (ViewGroup) child;
          if (viewGroup.getChildCount() > 0) {
            View contentView = viewGroup.getChildAt(0);
            measureWH.width = contentView.getMeasuredWidth();
            measureWH.height = contentView.getMeasuredHeight();
          }
        }
      }
    }

    final int widthSpec = getChildMeasureSpec(getWidth(),
      getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed,
      measureWH.width, canScrollHorizontally());
    final int heightSpec = getChildMeasureSpec(getHeight(),
      getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin + heightUsed,
      measureWH.height, canScrollVertically());

    child.measure(widthSpec, heightSpec);
  }

  public static class MeasureWH {

    int width;
    int height;
  }

  void setFooterMeasureWH(View footerView, MeasureWH measureWH) {
    RenderNode footerNode = HippyViewUtil.getRenderNode(footerView);
    if (footerNode != null) {
      measureWH.width = footerNode.getWidth();
      measureWH.height = footerNode.getHeight() + mItemGap;
    } else {
      measureWH.width = footerView.getWidth();
      measureWH.height = footerView.getHeight() + mItemGap;
    }
  }

  //    protected void handleRecordItemHeightChange(int index, int oldItemHeight, int newItemHeight)
  //    {
  //        if (mRecyclerView != null && mRecyclerView.getFirstVisibleItemPos() >= index && mRecyclerView.mOffsetY > 0)
  //        {
  //            mRecyclerView.mOffsetY -= oldItemHeight;
  //            mRecyclerView.mOffsetY += newItemHeight;
  //        }
  //    }

  protected static class WaterFallRenderState extends RenderState {

    public int targetColumn = 0;
  }

  @Override
  protected void ensureRenderState() {
    if (this.mRenderState == null) {
      this.mRenderState = new WaterFallRenderState();
    }

    super.ensureRenderState();
  }

  @Override
  public void calculateOffsetMap(SparseIntArray offsetMap, int startOffset) {
    RecyclerAdapter adapter = (RecyclerAdapter) mRecyclerView.getAdapter();
    int itemCount = adapter.getItemCount();
    int columnHeights[] = new int[mColumns];
    for (int i = 0; i < itemCount; i++) {
      int targetColumnIndex = getShortestColumnIndex(columnHeights);
      offsetMap.append(i, columnHeights[targetColumnIndex]);
      columnHeights[targetColumnIndex] += adapter.getItemHeight(i) + adapter
        .getItemMaigin(RecyclerAdapter.LOCATION_TOP, i)
        + adapter.getItemMaigin(RecyclerAdapter.LOCATION_BOTTOM, i);
    }
  }

  @Override
  public int getLayoutType() {
    return LAYOUT_TYPE_WATERFALL;
  }

  public RecyclerViewBase.LayoutParams onCreateItemLayoutParams(
    RecyclerView.ViewHolderWrapper holder, int position, int layoutType,
    int cardType) {
    int itemHeight = ((RecyclerAdapter) mRecyclerView.getAdapter()).getItemHeight(position);
    ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
    if (lp == null) {
      return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemHeight);
    }
    if (lp instanceof LayoutParams) {
      return (RecyclerViewBase.LayoutParams) lp;
    } else {
      return new LayoutParams(lp.width, itemHeight);
    }
  }

  public static class LayoutParams extends RecyclerViewBase.LayoutParams {

    public int mLocateAtColumn = -1;

    public LayoutParams(Context c, AttributeSet attrs) {
      super(c, attrs);
    }

    public LayoutParams(int width, int height) {
      super(width, height);
    }

    public LayoutParams(LayoutParams source) {
      super(source);
      mLocateAtColumn = source.mLocateAtColumn;
    }

    public LayoutParams(ViewGroup.MarginLayoutParams source) {
      super(source);
    }

    public LayoutParams(ViewGroup.LayoutParams source) {
      super(source);
    }
  }

  @Override
  public int getHeightBefore(int pos) {
    int columnHeights[] = calculateColumnHeightsBefore(pos, false);
    int sum = columnHeights[getShortestColumnIndex(columnHeights)];
    return sum;
  }

  public int getTotalHeight() {
    int columnHeights[] = calculateColumnHeightsBefore(getItemCount(), false);
    return getHightestColumnHeight(columnHeights);
  }

  /**
   * Convenience method to find the child closes to start. Caller should check it has enough
   * children.
   *
   * @return The shortest column's top child.
   */
  public View getChildClosestToStartInScreen() {
    return mShouldReverseLayout ? getChildClosestToEndInternal()
      : getChildClosestToStartInternal();
  }

  /**
   * Convenience method to find the child closes to end. Caller should check it has enough
   * children.
   *
   * @return The shortest column's bottom child.
   */
  public View getChildClosestToEndInScreen() {
    return mShouldReverseLayout ? getChildClosestToStartInternal()
      : getChildClosestToEndInternal();
  }

  /**
   * Convenience method to find the child closes to end. Caller should check it has enough
   * children.
   *
   * @return The shortest column's bottom child.
   */
  private View getChildClosestToDefaultFooter() {
    View childsClosestToEnd[] = new View[mColumns];
    for (int i = 0; i < getChildCount() - 1; i++) {
      View view = getChildAt(i);
      LayoutParams params = (LayoutParams) view.getLayoutParams();
      for (int j = 0; j < mColumns; j++) {
        if (params.mLocateAtColumn == j) {
          childsClosestToEnd[j] = view;
          break;
        }
      }
    }
    int targetIndex = 0;
    for (int i = 0; i < mColumns; i++) {
      if (childsClosestToEnd[targetIndex] == null) {
        break;
      }
      if (childsClosestToEnd[i] == null) {
        break;
      }
      if (childsClosestToEnd[targetIndex].getBottom() < childsClosestToEnd[i].getBottom()) {
        targetIndex = i;
      }
    }
    return childsClosestToEnd[targetIndex];
  }

  /**
   * Convenience method to find the child closes to end. Caller should check it has enough
   * children.
   *
   * @return The shortest column's bottom child.
   */
  private View getChildClosestToEndInternal() {
    //        if (getChildAt(getChildCount() - 1) instanceof IRecyclerViewFooter)
    //        {
    //            return getChildAt(getChildCount() - 1);
    //        }

    if (mHasContainBannerView && getChildCount() == 1) {
      return getChildAt(0);
    }

    View childsClosestToEnd[] = new View[mColumns];
    for (int i = 0; i < getChildCount(); i++) {
      View view = getChildAt(i);
      if (view.getLayoutParams() instanceof LayoutParams) {
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        for (int j = 0; j < mColumns; j++) {
          if (params.mLocateAtColumn == j) {
            childsClosestToEnd[j] = view;
            break;
          }
        }
      }
    }
    int targetIndex = 0;
    for (int i = 0; i < mColumns; i++) {
      if (childsClosestToEnd[targetIndex] == null) {
        break;
      }
      if (childsClosestToEnd[i] == null) {
        targetIndex = i;
        break;
      }

      // todo learn：【重要逻辑】查找距离底部最近的一个view（高度最短的一列中，最后的那个view）
      // 1. 此处处理滚动到列表底部时，需要以最后一个view为准，否则最后一个view仅能显示一半
      boolean isLastView = (getPosition(childsClosestToEnd[i]) == getItemCount() - 1);
      if (isLastView) {
        targetIndex = i;
        break;
      }

      //  2. 此处特殊处理了有footer时的场景，footer应该是最底部的view；
      //  *本方法选取的view会影响瀑布流 scrollBy 时候的计算
      boolean isFooter = isFooterView(childsClosestToEnd[targetIndex]);
      if (isFooter) {
        targetIndex = i;
        break;
      }
      if (childsClosestToEnd[targetIndex].getBottom() > childsClosestToEnd[i].getBottom()) {
        targetIndex = i;
      }
    }
    return childsClosestToEnd[targetIndex];
  }

  /**
   * 判断一个节点是否是footer（前端需在列表最后一位添加 PullFooterView）
   * 应用本方法的位置，都是适配footer所新增的代码（涉及measure、layout等过程）
   */
  private boolean isFooterView(View target) {
    return FooterUtil.isFooterView(target);
  }

  /**
   * Convenience method to find the child closes to end. Caller should check it has enough
   * children.
   *
   * @return The shortest column's bottom child.
   */
  private View getChildClosestToStartInternal() {
    int targetPosition =
      getPosition(getChildClosestToStartByOrder()) + mRenderState.mItemDirection;
    int columnHeights[] = calculateColumnHeightsBefore(targetPosition, false);
    int targetColumn = getShortestColumnIndex(columnHeights);
    View view = null;
    for (int i = 0; i < getChildCount(); i++) {
      view = getChildAt(i);
      if (view.getLayoutParams() instanceof LayoutParams) {
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        if (params.mLocateAtColumn == targetColumn) {
          break;
        }
      }
    }
    return view;
  }
}

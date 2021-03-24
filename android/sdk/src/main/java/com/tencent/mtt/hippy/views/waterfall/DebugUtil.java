package com.tencent.mtt.hippy.views.waterfall;

import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.tencent.mtt.hippy.HippyEngineContext;
import com.tencent.mtt.hippy.common.HippyArray;
import com.tencent.mtt.hippy.common.HippyMap;
import com.tencent.mtt.hippy.dom.node.TextExtra;
import com.tencent.mtt.hippy.uimanager.RenderNode;
import com.tencent.mtt.hippy.views.image.HippyImageView;
import com.tencent.mtt.hippy.views.image.HippyImageViewController;
import com.tencent.mtt.hippy.views.text.HippyTextView;
import com.tencent.mtt.hippy.views.text.HippyTextViewController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by phantomqi on 2017/12/29.
 */

public class DebugUtil {

  public static boolean DEBUG = false;
  static Field fieldTextViewLayout = null;
  static Field fieldNodeTextExtra = null;
  static Field fieldImageUrl = null;

  public static String stringifyS(View view) {
    return stringify_(view, false, null).toString();
  }

  public static String stringifyR(View view) {
    return stringify_(view, true, null).toString();
  }

  public static String stringifyS(RenderNode node) {
    return stringify_(node, false, null).toString();
  }

  public static String stringifyR(RenderNode node) {
    return stringify_(node, true, null).toString();
  }

  private static StringBuilder stringify_(RenderNode node, boolean recursive, StringBuilder sb) {
    if (sb == null) {
      sb = new StringBuilder();
    }

    if (node == null) {
      return sb.append("[null]");
    }

    sb.append("[").append(node.getClassName()).append("#").append(node.getId());

    if (HippyTextViewController.CLASS_NAME.equals(node.getClassName())) {
      handleStringifyWhenClassNameIsHippyTextView(node, sb);
    } else if (WaterFallComponentName.ITEM.equals(node.getClassName())) {
      int type = ((HippyQBWaterfallItemRenderNode) node).getType();
      sb.append("<type=").append(type).append(">");
    } else if (HippyImageViewController.CLASS_NAME.equals(node.getClassName())) {
      HippyMap props = node.getProps();
      if (props.containsKey("src")) {
        sb.append("<").append(props.getString("src")).append(">");
      }
    }

    if (recursive) {
      for (int i = 0; i < node.getChildCount(); ++i) {
        stringify_(node.getChildAt(i), recursive, sb);
      }
    }

    return sb.append("]");
  }

  private static void handleStringifyWhenClassNameIsHippyTextView(RenderNode node,
    StringBuilder sb) {
    if (DEBUG) {
      if (fieldNodeTextExtra == null) {
        try {
          fieldNodeTextExtra = RenderNode.class.getDeclaredField("mTextExtra");
          fieldNodeTextExtra.setAccessible(true);
        } catch (NoSuchFieldException e) {
          e.printStackTrace();
        }
      }
      if (fieldNodeTextExtra != null) {
        try {
          Object extra = fieldNodeTextExtra.get(node);
          if (extra != null && extra instanceof TextExtra) {
            TextExtra textExtra = (TextExtra) extra;
            if (textExtra.mExtra != null && textExtra.mExtra instanceof Layout) {
              sb.append("<").append(((Layout) textExtra.mExtra).getText())
                .append(">");
            }
          }
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }
  }

  // #lizard forgives
  private static StringBuilder stringify_(View view, boolean recursive, StringBuilder sb) {
    if (sb == null) {
      sb = new StringBuilder();
    }

    if (view == null) {
      return sb.append("[null]");
    }

    sb.append("[").append(view.getClass().getSimpleName()).append("#").append(view.getId())
      .append("@")
      .append(Integer.toHexString(view.hashCode()));

    if (view instanceof HippyQBWaterfallItemView) {
      sb.append("<").append(((HippyQBWaterfallItemView) view).getType()).append(">");
    } else if (view instanceof HippyTextView) {
      if (DEBUG) {
        if (fieldTextViewLayout == null) {
          try {
            fieldTextViewLayout = HippyTextView.class.getDeclaredField("mLayout");
            fieldTextViewLayout.setAccessible(true);
          } catch (NoSuchFieldException e) {
            e.printStackTrace();
          }
        }
        if (fieldTextViewLayout != null) {
          try {
            Layout layout = (Layout) fieldTextViewLayout.get(view);
            sb.append("<").append(layout == null ? "null" : layout.getText())
              .append(">");
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
//        } else if (view instanceof HippyQBImageView) {
//            sb.append("<").append(((HippyQBImageView) view).getUrl()).append(">");
    } else if (view instanceof HippyImageView) {
      if (DEBUG) {
        String url = ((HippyImageView) view).getUrl();
        if (url != null) {
          sb.append("<").append(url).append(">");
        }
      }
    }

    if (recursive && view instanceof ViewGroup) {
      ViewGroup g = (ViewGroup) view;
      for (int i = 0; i < g.getChildCount(); ++i) {
        stringify_(g.getChildAt(i), recursive, sb);
      }
    }

    return sb.append("]");
  }

  public static String stringify(HippyMap map) {
    if (map == null) {
      return "null";
    }

    StringBuilder sb = new StringBuilder("{");
    boolean firstItem = true;
    for (String key : map.keySet()) {
      if (firstItem) {
        firstItem = false;
      } else {
        sb.append(",");
      }
      sb.append(key).append("=");
      Object value = map.get(key);
      if (value instanceof HippyArray) {
        sb.append(stringify((HippyArray) value));
      } else if (value instanceof HippyMap) {
        sb.append(stringify((HippyMap) value));
      } else {
        sb.append(value);
      }
    }
    return sb.append("}").toString();
  }

  public static String stringify(HippyArray array) {
    if (array == null) {
      return "null";
    }

    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < array.size(); ++i) {
      if (i > 0) {
        sb.append(",");
      }
      Object value = array.get(i);
      if (value instanceof HippyArray) {
        sb.append(stringify((HippyArray) value));
      } else if (value instanceof HippyMap) {
        sb.append(stringify((HippyMap) value));
      } else {
        sb.append(value);
      }
    }
    return sb.append("]").toString();
  }

  public static String trace(View view, Class<?> until, boolean include) {
    StringBuilder sb = new StringBuilder(stringifyS(view));
    for (ViewParent vp = view.getParent(); vp != null; vp = view.getParent()) {
      if (until != null && !include) {
        if (until.isInstance(vp)) {
          sb.append("@@");
          break;
        }
      }

      view = (View) vp;
      sb.append("@").append(stringifyS(view));

      if (until != null && include) {
        if (until.isInstance(vp)) {
          sb.append("@@");
          break;
        }
      }
    }

    return sb.toString();
  }

  public interface IForOne<T> {

    void forOne(T one);
  }

  public static void forEach(RenderNode node, IForOne<RenderNode> forOne) {
    for (LinkedList<RenderNode> queue = new LinkedList<RenderNode>(Arrays.asList(node));
      !queue.isEmpty(); ) {
      node = queue.removeFirst();
      for (int i = 0; i < node.getChildCount(); ++i) {
        queue.add(node.getChildAt(i));
      }
      forOne.forOne(node);
    }
  }

  public static String[] traceR(final HippyEngineContext context, RenderNode node,
    final Class<?> until, final boolean include) {
    final ArrayList<String> list = new ArrayList<String>();
    DebugUtil.forEach(node, new IForOne<RenderNode>() {
      @Override
      public void forOne(RenderNode one) {
        View view = context.getRenderManager().getControllerManager().findView(one.getId());
        if (view == null) {
          list.add("null#" + one.getId());
        } else {
          list.add(DebugUtil.trace(view, until, include));
        }
      }
    });
    return list.toArray(new String[list.size()]);
  }

  public static void forEach(View view, IForOne<View> forOne) {
    for (LinkedList<View> queue = new LinkedList<View>(Arrays.asList(view));
      !queue.isEmpty(); ) {
      view = queue.removeFirst();
      if (view instanceof ViewGroup) {
        ViewGroup g = (ViewGroup) view;
        for (int i = 0; i < g.getChildCount(); ++i) {
          queue.add(g.getChildAt(i));
        }
      }
      forOne.forOne(view);
    }
  }

  public static String stackTrace(int limit) {
    if (limit < 4) {
      limit = 4;
    }
    final int skip = 2;
    StackTraceElement[] stack = new Throwable().getStackTrace();
    if (stack.length <= 1) {
      ;
    } else if (stack.length < skip) {
      stack = Arrays.copyOfRange(stack, 1, Math.min(limit + 1, stack.length));
    } else {
      stack = Arrays.copyOfRange(stack, skip, Math.min(limit + skip, stack.length));
    }
    return Arrays.deepToString(stack);
  }
}

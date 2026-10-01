package com.leo.control;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class LeoAccessibilityService extends AccessibilityService {
    public static LeoAccessibilityService instance;

    @Override public void onServiceConnected() { instance = this; }
    @Override public void onAccessibilityEvent(AccessibilityEvent event) { }
    @Override public void onInterrupt() { }
    @Override public void onDestroy() { if (instance == this) instance = null; super.onDestroy(); }

    public boolean tap(float x, float y) {
        Path p = new Path(); p.moveTo(x, y);
        GestureDescription.StrokeDescription s = new GestureDescription.StrokeDescription(p,0,60);
        return dispatchGesture(new GestureDescription.Builder().addStroke(s).build(), null, null);
    }

    public boolean swipe(float x1,float y1,float x2,float y2,long ms) {
        Path p = new Path(); p.moveTo(x1,y1); p.lineTo(x2,y2);
        GestureDescription.StrokeDescription s = new GestureDescription.StrokeDescription(p,0,Math.max(100,ms));
        return dispatchGesture(new GestureDescription.Builder().addStroke(s).build(), null, null);
    }

    public boolean global(int action) { return performGlobalAction(action); }

    public boolean typeText(String text) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return false;
        AccessibilityNodeInfo focused = root.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (focused == null) { root.recycle(); return false; }
        Bundle b = new Bundle(); b.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        boolean ok = focused.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, b);
        focused.recycle(); root.recycle(); return ok;
    }

    public String dumpUi() {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        appendNode(root,sb,0); sb.append("]"); root.recycle(); return sb.toString();
    }

    private void appendNode(AccessibilityNodeInfo n,StringBuilder sb,int depth) {
        if (depth > 8 || n == null) return;
        if (sb.length() > 1 && sb.charAt(sb.length()-1) != '[') sb.append(',');
        sb.append("{\"text\":\"").append(esc(String.valueOf(n.getText()))).append("\",\"desc\":\"")
          .append(esc(String.valueOf(n.getContentDescription()))).append("\",\"class\":\"")
          .append(esc(String.valueOf(n.getClassName()))).append("\"}");
        for(int i=0;i<n.getChildCount();i++){ AccessibilityNodeInfo c=n.getChild(i); if(c!=null){ appendNode(c,sb,depth+1); c.recycle(); } }
    }
    private String esc(String s){ return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n"," "); }
}

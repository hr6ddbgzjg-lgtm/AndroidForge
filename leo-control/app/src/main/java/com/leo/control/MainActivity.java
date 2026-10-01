package com.leo.control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;

    @Override public void onCreate(Bundle b){
        super.onCreate(b);
        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(36,60,36,36);
        root.setBackgroundColor(Color.rgb(7,16,24));

        TextView title=new TextView(this);
        title.setText("LEO CONTROL");
        title.setTextSize(30);
        title.setTextColor(Color.rgb(105,240,255));
        title.setGravity(Gravity.CENTER);
        root.addView(title,new LinearLayout.LayoutParams(-1,-2));

        TextView sub=new TextView(this);
        sub.setText("Agent-ready Android control layer\nVisible. Permission-based. Reversible.");
        sub.setTextSize(16); sub.setTextColor(Color.LTGRAY); sub.setGravity(Gravity.CENTER); sub.setPadding(0,20,0,30);
        root.addView(sub,new LinearLayout.LayoutParams(-1,-2));

        status=new TextView(this);
        status.setTextSize(18); status.setGravity(Gravity.CENTER); status.setPadding(20,20,20,20);
        root.addView(status,new LinearLayout.LayoutParams(-1,-2));

        Button enable=new Button(this);
        enable.setText("ENABLE LEO CONTROL");
        enable.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        root.addView(enable,new LinearLayout.LayoutParams(-1,-2));

        Button testHome=new Button(this);
        testHome.setText("TEST: HOME BUTTON");
        testHome.setOnClickListener(v -> { if(LeoAccessibilityService.instance!=null) LeoAccessibilityService.instance.global(2); });
        root.addView(testHome,new LinearLayout.LayoutParams(-1,-2));

        Button testBack=new Button(this);
        testBack.setText("TEST: BACK BUTTON");
        testBack.setOnClickListener(v -> { if(LeoAccessibilityService.instance!=null) LeoAccessibilityService.instance.global(1); });
        root.addView(testBack,new LinearLayout.LayoutParams(-1,-2));

        Button openChat=new Button(this);
        openChat.setText("OPEN CHATGPT");
        openChat.setOnClickListener(v -> {
            try { startActivity(getPackageManager().getLaunchIntentForPackage("com.openai.chatgpt")); }
            catch(Exception e){ startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://chatgpt.com"))); }
        });
        root.addView(openChat,new LinearLayout.LayoutParams(-1,-2));

        TextView note=new TextView(this);
        note.setText("When enabled, Leo Control can perform taps, swipes, Back/Home/Recents and enter text through Android Accessibility. You remain in control and can disable it in Accessibility settings at any time.");
        note.setTextColor(Color.GRAY); note.setTextSize(14); note.setPadding(4,30,4,4);
        root.addView(note,new LinearLayout.LayoutParams(-1,-2));

        setContentView(root);
    }

    @Override protected void onResume(){ super.onResume(); updateStatus(); }
    private void updateStatus(){
        boolean on=LeoAccessibilityService.instance!=null;
        status.setText(on ? "● CONTROL ENABLED" : "○ CONTROL OFF");
        status.setTextColor(on ? Color.rgb(100,255,160) : Color.rgb(255,170,90));
    }
}

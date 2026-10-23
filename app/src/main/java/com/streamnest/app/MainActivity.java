package com.streamnest.app;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final int BG=0xFF090B12, PANEL=0xFF171B29, WHITE=0xFFF4F5FB, MUTED=0xFF969DB2, PURPLE=0xFFB19BFF;
    private LinearLayout page, list;
    private final List<Video> videos=new ArrayList<>();
    private String filter="For you", query="";
    private final ArrayList<String> saved=new ArrayList<>(), history=new ArrayList<>();
    static class Video { String id,title,channel,category,meta; Video(String i,String t,String c,String g,String m){id=i;title=t;channel=c;category=g;meta=m;} }
    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(BG);
        saved.addAll(getPreferences(0).getStringSet("saved",new java.util.HashSet<>()));
        history.addAll(getPreferences(0).getStringSet("history",new java.util.HashSet<>()));
        videos.add(new Video("jfKfPfyJRdk","lofi hip hop radio ☕ beats to relax / study to","Lofi Girl","Music","LIVE · Music"));
        videos.add(new Video("aqz-KE-bpKQ","Big Buck Bunny — animated short film","Blender Studio","Film","10 min · Animation"));
        videos.add(new Video("ScMzIvxBSi4","A calm moment in nature","Nature Clips","Nature","4 min · Relaxation"));
        videos.add(new Video("ysz5S6PUM-U","Creative thinking and new ideas","Open Learning","Learning","6 min · Learning"));
        videos.add(new Video("LXb3EKWsInQ","Beautiful places from above","Travel Stories","Travel","5 min · Travel"));
        videos.add(new Video("kJQP7kiw5Fk","Latin music for your playlist","Music Mix","Music","4 min · Music"));
        home();
    }
    private void home(){
        LinearLayout shell=new LinearLayout(this);shell.setOrientation(1);shell.setBackgroundColor(BG);setContentView(shell);
        ScrollView scroll=new ScrollView(this);shell.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        page=new LinearLayout(this);page.setOrientation(1);page.setPadding(dp(20),dp(18),dp(20),dp(24));scroll.addView(page);
        LinearLayout brand=new LinearLayout(this);brand.setGravity(Gravity.CENTER_VERTICAL);page.addView(brand);
        LinearLayout names=new LinearLayout(this);names.setOrientation(1);brand.addView(names,new LinearLayout.LayoutParams(0,-2,1));
        TextView eyebrow=txt("YOUR PERSONAL STREAM",10,PURPLE,true);names.addView(eyebrow);
        names.addView(txt("StreamNest",30,WHITE,true));
        TextView logo=txt("◈",28,PURPLE,true);logo.setGravity(Gravity.CENTER);logo.setBackground(bg(0x223F3670,0x66B19BFF,18));brand.addView(logo,new LinearLayout.LayoutParams(dp(48),dp(48)));
        EditText search=new EditText(this);search.setSingleLine(true);search.setTextColor(WHITE);search.setHintTextColor(MUTED);search.setTextSize(14);search.setHint("Search videos, creators…");search.setPadding(dp(15),0,dp(15),0);search.setBackground(bg(PANEL,0x30FFFFFF,16));LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,dp(48));sp.topMargin=dp(18);page.addView(search,sp);
        TextView feature=txt("Find your next\nfavourite thing.",24,WHITE,true);feature.setPadding(dp(18),dp(20),dp(18),dp(4));feature.setBackground(bg(0xFF282542,0x55B19BFF,22));LinearLayout.LayoutParams fp=new LinearLayout.LayoutParams(-1,dp(126));fp.topMargin=dp(20);page.addView(feature,fp);
        TextView desc=txt("Music, learning, films and more — all in one clean space.",13,0xFFD0CEE5,false);desc.setPadding(dp(18),dp(12),dp(18),dp(12));desc.setBackground(bg(0xFF282542,0x55B19BFF,22));page.addView(desc);
        TextView heading=txt("Made for your mood",19,WHITE,true);LinearLayout.LayoutParams hp=new LinearLayout.LayoutParams(-1,-2);hp.topMargin=dp(24);hp.bottomMargin=dp(12);page.addView(heading,hp);
        ScrollView catScroll=new ScrollView(this);catScroll.setHorizontalScrollBarEnabled(false);LinearLayout cats=new LinearLayout(this);cats.setOrientation(0);catScroll.addView(cats);page.addView(catScroll);
        for(String c:new String[]{"For you","Music","Learning","Nature","Travel","Film"}){TextView chip=txt(c,13,filter.equals(c)?BG:WHITE,true);chip.setGravity(Gravity.CENTER);chip.setPadding(dp(16),dp(10),dp(16),dp(10));chip.setBackground(bg(filter.equals(c)?PURPLE:0xFF1B2030,0x30FFFFFF,30));LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-2,-2);cp.setMargins(0,0,dp(8),0);cats.addView(chip,cp);chip.setOnClickListener(v->{filter=c;home();});}
        heading=txt("Picked for you",19,WHITE,true);hp=new LinearLayout.LayoutParams(-1,-2);hp.topMargin=dp(22);hp.bottomMargin=dp(12);page.addView(heading,hp);
        list=new LinearLayout(this);list.setOrientation(1);page.addView(list);renderVideos();
        search.addTextChangedListener(new android.text.TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){}public void onTextChanged(CharSequence s,int st,int before,int count){query=s.toString();renderVideos();}public void afterTextChanged(android.text.Editable e){}});
        LinearLayout nav=new LinearLayout(this);nav.setGravity(Gravity.CENTER);nav.setPadding(dp(8),dp(8),dp(8),dp(8));nav.setBackground(bg(0xEE171B29,0x30FFFFFF,24));LinearLayout.LayoutParams np=new LinearLayout.LayoutParams(-1,dp(64));np.setMargins(dp(14),dp(4),dp(14),dp(10));shell.addView(nav,np);
        for(String item:new String[]{"⌂  Home","⌕  Explore","♡  Saved"}){TextView t=txt(item,13,item.startsWith("⌂")?PURPLE:MUTED,true);t.setGravity(Gravity.CENTER);nav.addView(t,new LinearLayout.LayoutParams(0,-1,1));t.setOnClickListener(v->{if(item.contains("Saved"))showSaved();else if(item.contains("Explore"))showExplore();else home();});}
    }
    private void renderVideos(){if(list==null)return;list.removeAllViews();for(Video v:videos){if(!filter.equals("For you")&&!v.category.equals(filter))continue;if(!(v.title+" "+v.channel+" "+v.category).toLowerCase().contains(query.toLowerCase()))continue;
        LinearLayout card=new LinearLayout(this);card.setOrientation(1);card.setPadding(dp(14),dp(14),dp(14),dp(14));card.setBackground(bg(0xFF151A27,0x20FFFFFF,20));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(12);list.addView(card,p);
        TextView thumb=txt(icon(v.category)+"     "+v.category.toUpperCase()+"   ·   STREAMNEST",11,PURPLE,true);thumb.setGravity(Gravity.CENTER_VERTICAL);thumb.setPadding(dp(14),0,dp(14),0);thumb.setBackground(bg(0xFF24243B,0x30B19BFF,14));card.addView(thumb,new LinearLayout.LayoutParams(-1,dp(76)));
        TextView title=txt(v.title,16,WHITE,true);LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(-1,-2);tp.topMargin=dp(12);card.addView(title,tp);TextView meta=txt(v.channel+"  ·  "+v.meta,12,MUTED,false);tp=new LinearLayout.LayoutParams(-1,-2);tp.topMargin=dp(5);card.addView(meta,tp);
        LinearLayout actions=new LinearLayout(this);actions.setGravity(Gravity.CENTER_VERTICAL);tp=new LinearLayout.LayoutParams(-1,dp(40));tp.topMargin=dp(8);card.addView(actions,tp);
        TextView play=txt("▶  Play",13,WHITE,true);play.setGravity(Gravity.CENTER);play.setBackground(bg(0xFF373054,0x55B19BFF,12));actions.addView(play,new LinearLayout.LayoutParams(dp(100),dp(36)));play.setOnClickListener(view->play(v));
        TextView save=txt(saved.contains(v.id)?"♥ Saved":"♡ Save",13,PURPLE,true);save.setGravity(Gravity.CENTER);LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(dp(100),dp(36));ap.leftMargin=dp(8);actions.addView(save,ap);save.setOnClickListener(view->{if(saved.contains(v.id))saved.remove(v.id);else saved.add(v.id);getPreferences(0).edit().putStringSet("saved",new java.util.HashSet<>(saved)).apply();renderVideos();});
    }}
    private void showSaved(){filter="For you";query="";home();query="";list.removeAllViews();for(Video v:videos)if(saved.contains(v.id))addSimpleCard(v);if(list.getChildCount()==0)list.addView(txt("No saved videos yet. Tap ♡ Save on any video.",14,MUTED,false));}
    private void showExplore(){home();filter="For you";query="";list.removeAllViews();for(String c:new String[]{"Music","Learning","Nature","Travel","Film"}){TextView t=txt(icon(c)+"   "+c+"     Explore "+c.toLowerCase()+" videos",16,WHITE,true);t.setPadding(dp(16),dp(20),dp(16),dp(20));t.setBackground(bg(PANEL,0x25FFFFFF,16));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(10);list.addView(t,p);t.setOnClickListener(v->{filter=c;home();});}}
    private void addSimpleCard(Video v){TextView t=txt("▶  "+v.title+"\n"+v.channel,15,WHITE,true);t.setPadding(dp(16),dp(18),dp(16),dp(18));t.setBackground(bg(PANEL,0x25FFFFFF,16));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.bottomMargin=dp(10);list.addView(t,p);t.setOnClickListener(w->play(v));}
    private void play(Video v){history.remove(v.id);history.add(0,v.id);getPreferences(0).edit().putStringSet("history",new java.util.HashSet<>(history)).apply();Dialog d=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);LinearLayout frame=new LinearLayout(this);frame.setOrientation(1);frame.setBackgroundColor(BG);TextView close=txt("✕     "+v.title,16,WHITE,true);close.setPadding(dp(18),dp(18),dp(18),dp(18));frame.addView(close);WebView web=new WebView(this);WebSettings ws=web.getSettings();ws.setJavaScriptEnabled(true);ws.setDomStorageEnabled(true);web.setWebChromeClient(new WebChromeClient());String html="<!doctype html><html><head><meta name='viewport' content='width=device-width,initial-scale=1'><style>html,body{margin:0;background:#000;height:100%}iframe{border:0;width:100%;height:100%}</style></head><body><iframe src='https://www.youtube-nocookie.com/embed/"+v.id+"?autoplay=1&playsinline=1&rel=0' allow='autoplay; encrypted-media; picture-in-picture; web-share' allowfullscreen></iframe></body></html>";web.loadDataWithBaseURL("https://www.youtube-nocookie.com",html,"text/html","UTF-8",null);frame.addView(web,new LinearLayout.LayoutParams(-1,0,1));TextView note=txt("YouTube controls playback, ads and account restrictions.",12,MUTED,false);note.setPadding(dp(16),dp(14),dp(16),dp(18));frame.addView(note);close.setOnClickListener(w->{web.destroy();d.dismiss();});d.setContentView(frame);d.show();Window w=d.getWindow();if(w!=null)w.setLayout(-1,-1);d.setOnDismissListener(x->web.destroy());}
    private TextView txt(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    private GradientDrawable bg(int color,int stroke,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));d.setStroke(dp(1),stroke);return d;}
    private String icon(String c){switch(c){case "Music":return "♫";case "Learning":return "✧";case "Nature":return "❋";case "Travel":return "⌁";default:return "▷";}}
    private int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
}
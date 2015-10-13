package se.martenolsson.lah15;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import se.martenolsson.lah15.classes.ObservableWebView;
import se.martenolsson.lah15.classes.TinyDB;

/**
 * Created by maroln on 15-08-19.
 */
public class Tab1 extends Fragment {
    public static ObservableWebView webview;
    TinyDB tinydb;
    ArrayList<String> followList = new ArrayList<>();
    StringBuilder titleList = new StringBuilder();
    LinearLayout loader;
    Boolean removeFollow;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tinydb = new TinyDB(container.getContext());
        removeFollow = tinydb.getBoolean("removefollow");

        View v = inflater.inflate(R.layout.tab_1,container,false);
        loader = (LinearLayout) v.findViewById(R.id.loaderCont);

        webview = (ObservableWebView) v.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        myWebChromeClient mWebChromeClient = new myWebChromeClient();
        webview.setWebChromeClient(mWebChromeClient);
        webview.getSettings().setBuiltInZoomControls(false); //Enable Multitouch if supported by ROM
        webview.getSettings().setSupportMultipleWindows(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //webview.loadUrl("file:///android_asset/tab1.html?3");
        File cacheDir = container.getContext().getCacheDir();
        webview.loadUrl("file://" + new String(String.valueOf(new File(cacheDir, "tab1.html"))));

        followList = tinydb.getList("followList");
        for(int index = 0; index < followList.size(); index++) {
            String[] listitem = followList.get(index).split(";;");
            String followtitle = listitem[0];
            if(titleList.length() > 0){
                titleList.append(',');
            }
            titleList.append(followtitle);
        }
        return v;
    }
    class myWebChromeClient extends WebChromeClient {
        //final Activity MyActivity = MainActivity.this;
        @Override
        public void onProgressChanged(WebView view, int progress){
            if(progress == 100){
                Log.e("test", "ready");
                webview.loadUrl("javascript:ifollow('"+titleList+"');");
                if(removeFollow == true){
                    webview.loadUrl("javascript:removeNoFollow();");
                }else if(removeFollow == true){
                    webview.loadUrl("javascript:showNoFollow();");
                }
            }
            if(progress > 80) {
                loader.setVisibility(View.GONE);
            }
        }
    }
}

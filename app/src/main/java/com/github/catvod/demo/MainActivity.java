package com.github.catvod.demo;

import android.app.Activity;
import android.os.Bundle;

import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.spider.Mac10Api;
import com.github.catvod.spider.Miss;

import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
//               testMissAV();
                testMac10Api();
            }
        }).start();
    }

    private void testMissAV(){
        Miss ms = new Miss();
        ms.init(null, "https://missav.com/cn/");
//        String res = ms.homeContent(true);
        String res = ms.detailContent(Arrays.asList("MEYD-217"));
        SpiderDebug.log("missav: " + res);
    }

    private void testMac10Api(){
        Mac10Api mac10Api = new Mac10Api();
        mac10Api.init(null, "http://api.ffzyapi.com/api.php/provide/vod/from/ffm3u8");
//        mac10Api.init(null, "https://api.1080zyku.com/inc/api_mac10.php");
//        String res = mac10Api.searchContent("摩西", false, "1");
        String res = mac10Api.homeContent(true);
        SpiderDebug.log(res);
    }
}

package com.github.catvod.demo;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpWithProxy {
    //    public static void main(String[] args) {
    public static void get() {
        String url = "https://missav.com/dm10/cn";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "missav.com");
//        headers.put("referer", url);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        headers.put("Connection", "close");
        String res = OkHttpUtil.string(url, headers);
//        SpiderDebug.log("missav: " + res);
        System.out.println(res);
    }

    public static void get2() {
        // 创建代理对象
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1082));

        // 创建 OkHttpClient，并设置代理
        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .proxy(Proxy.NO_PROXY)  // 设置代理
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)        // 失败自动重试
                .build();

        String url = "https://missav.com/dm10/cn";

        // 创建请求
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Host", "missav.com")
                .addHeader("referer", url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")
                .addHeader("Connection", "close")
                .build();

        // 执行请求
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response Code: " + response.code());
            if (response.isSuccessful()) {
                System.out.println("Response Body: " + response.body().string());
            } else {
                System.out.println("GET请求失败.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

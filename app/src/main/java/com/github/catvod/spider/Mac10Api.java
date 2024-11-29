package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.App;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.internal.StringUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 嗯哩嗯哩
 * <p>
 * Author: 群友 不负此生
 */
public class Mac10Api extends Spider {

    private String siteUrl = "";

    @Override
    public void init(Context context) {
        App.init(context);
    }

    @Override
    public void init(Context context, String extend) {
        SpiderDebug.log("进入init.");
        App.init(context);
        try {
            String[] extInfos = extend.split("###");
            siteUrl = extInfos[0];
        } catch (Throwable ignored) {
        }
    }

    private HashMap<String, String> getHeaders(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Connection", "Keep-Alive");
        headers.put("User-Agent", "okhttp/4.0.1");
        return headers;
    }

    @Override
    public String homeContent(boolean filter) {
        SpiderDebug.log("进入homeContent.");
        try {
            String res = OkHttpUtil.string(siteUrl + "?ac=list", getHeaders(siteUrl));
            JSONObject resObj = new JSONObject(res);
            JSONArray jsonArray = resObj.getJSONArray("class");
            JSONArray classes = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                if (!jObj.isNull("type_pid") && jObj.getInt("type_id") == 0) continue;
                classes.put(jObj);
            }

            JSONObject result = new JSONObject();
            result.put("class", classes);

            res = OkHttpUtil.string(siteUrl + "?ac=detail", getHeaders(siteUrl));
            resObj = new JSONObject(res);
            result.put("list", resObj.getJSONArray("list"));
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    @Override
    public String homeVideoContent() {
        SpiderDebug.log("进入homeVideoContent.");
        return homeContent(true);
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        SpiderDebug.log("进入categoryContent");
        try {
            String url = siteUrl + "?ac=detail&pg=" + pg + "&t=" + tid;

            String res = OkHttpUtil.string(url, getHeaders(url));
            SpiderDebug.log("res:" + res);
            return res;

        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    @Override
    public String detailContent(List<String> ids) {
        SpiderDebug.log("进入detailContent.");
        try {
            String url = siteUrl + "?ac=detail&ids=" + String.join(",", ids);
            return OkHttpUtil.string(url, getHeaders(url));
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }


    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        SpiderDebug.log("进入playerContent.");
        try {
            String url = id != null && !id.isEmpty() && id.endsWith(".m3u8") ?
                    "http://127.0.0.1:9978/proxy?do=m3u8&flag=" + flag + "&url=".concat(URLEncoder.encode(id)) : id;

            JSONObject result = new JSONObject();
            result.put("parse", 0);
            result.put("playUrl", "");
            result.put("url", url);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) {
        SpiderDebug.log("带page的搜索key=[" + key + "].");
        try {
            String url = siteUrl + "?ac=" + "list" + "&wd=" + URLEncoder.encode(key);
            String res = OkHttpUtil.string(url, getHeaders(url));
            if (quick) return res;

            JSONObject resObj = new JSONObject(res);
            JSONArray vods = resObj.getJSONArray("list");
            List<String> idList = new ArrayList<>();
            for (int i = 0; i < vods.length(); i++) {
                String id = vods.getJSONObject(i).getString("vod_id");
                idList.add(id);
            }
            String ids = String.join(",", idList);
            url = siteUrl + "?ac=" + "detail" + "&wd=" + URLEncoder.encode(key) + "&ids=" + ids;
            if (!StringUtil.isBlank(pg)) url += "&pg=" + pg;
            return OkHttpUtil.string(url, getHeaders(url));
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    @Override
    public String searchContent(String key, boolean quick) {
        SpiderDebug.log("不带带page的搜索key=[" + key + "].");
        return searchContent(key, quick, null);
    }
}

package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.App;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

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
        try {
            String res = OkHttpUtil.string(siteUrl + "?ac=list", getHeaders(siteUrl));
            JSONObject resObj = new JSONObject(res);
            JSONArray jsonArray = resObj.getJSONArray("class");
            JSONArray classes = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                if (jObj.getInt("type_pid") == 0) continue;
                classes.put(jObj);
            }


            JSONObject result = new JSONObject();
            result.put("class", classes);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    @Override
    public String homeVideoContent() {
        try {
            String url = siteUrl + "/api.php/provide/home_data?page=1&id=0";
            JSONObject jsonObject = new JSONObject(OkHttpUtil.string(url, getHeaders(url)));
            JSONArray jsonArray = new JSONArray();
            if (jsonObject.has("tv")) {
                JSONArray data = jsonObject.getJSONObject("tv").getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    jsonArray.put(data.getJSONObject(i));
                }
            }

            if (jsonObject.has("video")) {
                JSONArray vs = jsonObject.getJSONArray("video");
                for (int i = 0; i < vs.length(); i++) {
                    JSONArray data = vs.getJSONObject(i).getJSONArray("data");
                    for (int j = 0; j < data.length(); j++) {
                        jsonArray.put(data.getJSONObject(j));
                    }
                }
            }

            JSONArray videos = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject vObj = jsonArray.getJSONObject(i);
                JSONObject v = new JSONObject();
                v.put("vod_id", vObj.getString("id"));
                v.put("vod_name", vObj.getString("name"));
                v.put("vod_pic", vObj.getString("img"));
                v.put("vod_remarks", vObj.getString("qingxidu"));
                videos.put(v);
            }
            JSONObject result = new JSONObject();
            result.put("list", videos);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
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
        try {
            String url = flag != null && !flag.isEmpty() && id.endsWith(".m3u8") ?
                    "http://127.0.0.1:9978/proxy?do=m3u8&url=".concat(URLEncoder.encode(id)) : id;
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
        try {
            String act = quick ? "list" : "detail";
            String url = siteUrl + "?ac=" + act + "&pg=" + pg + "&wd=" + URLEncoder.encode(key);
            return OkHttpUtil.string(url, getHeaders(url));
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    @Override
    public String searchContent(String key, boolean quick) {
        try {
            String act = quick ? "list" : "detail";
            String url = siteUrl + "?ac=" + act + "&wd=" + URLEncoder.encode(key);
            return OkHttpUtil.string(url, getHeaders(url));
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}

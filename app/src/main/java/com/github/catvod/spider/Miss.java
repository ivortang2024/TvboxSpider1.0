package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.App;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 嗯哩嗯哩
 * <p>
 * Author: 群友 不负此生
 */
public class Miss extends Spider {

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

    private Map<String, String> getHeaders(String url) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "missav.com");
        headers.put("referer", url);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
        headers.put("Connection", "close");
        return headers;
    }

    @Override
    public String homeContent(boolean filter) {
        SpiderDebug.log("missav: 进入homeContent函数");
        JSONObject result = new JSONObject();
        try {
            JSONArray vods = getList("monthly-hot");
            result.put("list", vods);

            JSONArray types = new JSONArray();
            types.put(new JSONObject().put("type_id", "new").put("type_name", "最近更新"));
            types.put(new JSONObject().put("type_id", "release").put("type_name", "新作上市"));
            types.put(new JSONObject().put("type_id", "uncensored-leak").put("type_name", "无码流出"));
            types.put(new JSONObject().put("type_id", "chinese-subtitle").put("type_name", "中文字幕"));
            types.put(new JSONObject().put("type_id", "madou").put("type_name", "麻豆"));

            result.put("class", types);

        } catch (Exception e) {
            e.getMessage();
            SpiderDebug.log(e.getMessage());
        }
        return result.toString();
    }

    @Override
    public String homeVideoContent() {
        SpiderDebug.log("missav: 进入homeVideoContent函数");
        JSONObject result = new JSONObject();
        try {
            JSONArray vods = getList("monthly-hot");
            result.put("list", vods);
        } catch (Exception e) {
            e.getMessage();
            SpiderDebug.log(e.getMessage());
        }
        return result.toString();
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        JSONObject result = new JSONObject();
        try {
            JSONArray vods = getList(tid + "?page=" + pg);
            result.put("list", vods);
        } catch (Exception e) {
            e.getMessage();
            SpiderDebug.log(e.getMessage());
        }
        return result.toString();
    }

    @Override
    public String detailContent(List<String> ids) {
        JSONObject result = new JSONObject();
        try {
            JSONArray list = getDetail(ids.get(0));
            result.put("list", list);
        } catch (Exception e) {
            SpiderDebug.log(e.getMessage());
        }
        return result.toString();
    }


    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        JSONObject result = new JSONObject();
        try {
            result.put("parse", 0);
            result.put("playUrl", "");
            result.put("url", id);
        } catch (Exception e) {
            SpiderDebug.log(e.getMessage());
        }
        return result.toString();
    }

    @Override
    public String searchContent(String key, boolean quick, String pg) {
        JSONObject result = new JSONObject();
        try {
            JSONArray vods = getList("search/" + key + "?page=" + pg);
            result.put("list", vods);
        } catch (Exception e) {
            e.getMessage();
            SpiderDebug.log(e.getMessage());
        }
        return result.toString();
    }

    @Override
    public String searchContent(String key, boolean quick) {
        JSONObject result = new JSONObject();
        try {
            JSONArray vods = getList("search/" + key + "?page=");
            result.put("list", vods);
        } catch (Exception e) {
            e.getMessage();
            SpiderDebug.log(e.getMessage());
        }
        return result.toString();
    }

    public JSONArray getList(String path) {
        JSONArray vods = new JSONArray();
        try {
            String html = OkHttpUtil.string(siteUrl + path, null);
            // 获取 HTML
            Document doc = Jsoup.parse(html);
            Elements elements = null;
            Element parents = doc.selectFirst("html > body > div");
            if (parents != null) {
                Elements divList1 = parents.select("> div");
                if (divList1.size() > 2) {
                    Element div2 = divList1.get(2);
                    Elements divList2 = div2.select("> div");
                    if (divList2.size() > 1) {
                        Element div3 = divList2.get(1);
                        elements = div3.select("> div");
//                        SpiderDebug.log("missav: " + elements.toString());
                    }
                    if (divList2.size() > 2 && elements.isEmpty()) {
                        Element div3 = divList2.get(2);
                        elements = div3.select("> div");
//                        SpiderDebug.log("missav: " + elements.toString());
                    }
                }
            }
            if (elements == null) SpiderDebug.log("一个有用的东西都没有扒到。");
            // 遍历每个元素提取数据
            assert elements != null;
            for (Element ele : elements) {
                Element img = ele.selectFirst("img");
                Element span = ele.selectFirst("span");
                Element a = ele.selectFirst("a");

                if (a == null) continue;

                JSONObject vod = new JSONObject();
                vod.put("vod_id", a.attr("alt"));
                vod.put("vod_name", a.attr("alt"));
                vod.put("vod_pic", img != null ? img.attr("data-src") : null);
                vod.put("vod_remarks", span != null ? span.text().trim() : "");
                vod.put("vod_play_from", "MissAV");
                vods.put(vod);
            }
        } catch (Exception e) {
            e.printStackTrace();
            SpiderDebug.log("missav: " + e.getMessage());
        }
        return vods;

    }

    public JSONArray getDetail(String path) {
        JSONArray result = new JSONArray();
        try {
            String html = OkHttpUtil.string(siteUrl + path, null);
            // 获取 HTML
            Document doc = Jsoup.parse(html);

            // 提取 <script> 标签内容
            Elements scripts = doc.select("script");
            String matchingLink = scripts.size() > 9 ? scripts.get(9).data() : null;

            if (matchingLink == null) {
                throw new IllegalArgumentException("No script content found");
            }

            // 使用正则表达式提取 URL
            String regex = "\"(https:\\\\/\\\\/sixyik\\.com\\\\/[^\"\\\\]+)\"";
            String[] segments = matchingLink.split("\\\\/");

            // 提取描述、详细信息
            String description = doc.selectFirst("meta[property=og:description]").attr("content");
            Elements detailDivs = doc.select("div.space-y-2 > div");

            String vodId = null, vodName = null, vodYear = null, issuer = null;

            List<String> vodActors = new ArrayList<>();
            List<String> vodTypes = new ArrayList<>();
            for (Element div : detailDivs) {
                String text = div.text();
                if (text.contains("发行日期")) {
                    vodYear = Objects.requireNonNull(div.selectFirst("time")).text();
                }
                if (text.contains("番号")) {
                    vodId = div.select("span").get(1).text();
                    vodName = vodId;
                }
                if (text.contains("女优") || text.contains("男优")) {
                    Elements aElements = div.select("a");
                    if (!aElements.isEmpty()) {
                        for (Element a : aElements) {
                            vodActors.add(a.text().replaceAll("\\([^)]*\\)", "")   // 去掉圆括号及其中内容
                                    .replaceAll("\\[[^\\]]*\\]", ""));
                        }
                    }
                }
                if (text.contains("类型")) {
                    Elements aElements = div.select("a");
                    if (!aElements.isEmpty()) {
                        for (Element a : aElements) {
                            vodTypes.add(a.text());
                        }
                    }
                }
                if (text.contains("发行商")) {
                    issuer = div.select("a").text();
                    issuer = String.format("[a=cr:{\"id\":\"search/%s\",\"name\":\"%s\"}/]%s[/a] ", issuer, issuer, issuer);
                }
            }

            String vodPlayUrl = String.format("直连$%s%s/playlist.m3u8#" +
                            "代理1$%s%s/playlist.m3u8#" +
                            "代理2$%s%s/playlist.m3u8",
                    "https://surrit.com/", segments[3],
                    "https://miss.118318.xyz/", segments[3],
                    "https://miss-cf.118318.xyz/", segments[3]);

            // 格式化成内tvbox特殊内链格式
            StringBuilder actors = new StringBuilder();
            for (String actor : vodActors) {
                String s = String.format("[a=cr:{\"id\":\"search/%s\",\"name\":\"%s\"}/]%s[/a] ", actor, actor, actor);
                actors.append(s);
            }

            StringBuilder types = new StringBuilder();
            for (String t : vodTypes) {
                String s = String.format("[a=cr:{\"id\":\"search/%s\",\"name\":\"%s\"}/]%s[/a] ", t, t, t);
                types.append(s);
            }

            JSONObject detail = new JSONObject();
            detail.put("vod_id", vodId);
            detail.put("vod_name", vodName);
            detail.put("description", description);
            detail.put("vod_year", vodYear);
            detail.put("vod_actor", actors.toString());
            detail.put("vod_remarks", types.toString());
            detail.put("vod_play_from", "MissAV");
            detail.put("vod_play_url", vodPlayUrl);
            detail.put("vod_director", issuer);

            result.put(detail);
        } catch (Exception e) {
            SpiderDebug.log("missav:" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}

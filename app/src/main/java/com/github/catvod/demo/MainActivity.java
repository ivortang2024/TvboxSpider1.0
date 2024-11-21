package com.github.catvod.demo;

import android.app.Activity;
import android.os.Bundle;

import com.github.catvod.spider.AppYsV2;
import com.github.catvod.spider.Mac10Api;
import com.github.catvod.spider.Proxy;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

public class MainActivity extends Activity {

    private static OkHttpClient defaultClient;
    private final static String url = "https://ff.118318.xyz/api.php/provide/vod";
    private final static String url2 = "https://svipsvip.ffzy-online5.com/20240410/25821_e52cb5f1/index.m3u8";
    private final static String m3u8Url = "https://vip.lz-cdn1.com/20230502/20964_0bb8183f/index.m3u8";
    private final static String m3u8Url3 = "https://v.cdnlz12.com/20240602/14887_40bbd784/index.m3u8";
    private final static String m3u8Url2 = "https://m3u.118318.xyz/m3u8/4962f15dd8ed6c10a4857a8ed5cf210073ee19a59cbf3c742193f4d3ea4d0f559921f11e97d0da21.m3u8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                Mac10Api appYsV2 = new Mac10Api();
//                appYsV2.init(null, "https://lz.118318.xyz/api.php/provide/vod/from/snm3u8");
//                String res = appYsV2.homeContent(true);
//                System.out.println(res);
                Map<String,String> params = new HashMap<>();
                params.put("url", "https://vip.ffzy-play10.com/20230219/28135_cd852348/index.m3u8");
                params.put("flag", "ffm3u8");
                params.put("do", "m3u8");
                Proxy.proxy(params);
            }
        }).start();
    }
}
//    public void run() {
//        AppYsV2 aidi1 = new AppYsV2();
//        aidi1.init(MainActivity.this, "https://vipmv.co/xgapp.php/v1/");
//        String json = aidi1.homeContent(true);
//        System.out.println(json);
//        JSONObject homeContent = null;
//        try {
//            homeContent = new JSONObject(aidi1.homeVideoContent());
//            System.out.println(homeContent.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        System.out.println(aidi1.categoryContent("1", "1", false, null));
//        if (homeContent != null) {
//            try {
//                List<String> ids = new ArrayList<String>();
//                JSONArray array = homeContent.getJSONArray("list");
//                for (int i = 0; i < array.length() && i < 3; i++) {
//                    try {
//                        ids.clear();
//                        ids.add(array.getJSONObject(i).getString("vod_id"));
//                        System.out.println(aidi1.detailContent(ids));
//                        JSONObject detailContent = new JSONObject(aidi1.detailContent(ids)).getJSONArray("list").getJSONObject(0);
//                        String[] playFlags = detailContent.getString("vod_play_from").split("\\$\\$\\$");
//                        String[] playUrls = detailContent.getString("vod_play_url").split("\\$\\$\\$");
//                        for (int j = 0; j < playFlags.length; j++) {
//                            String pu = playUrls[j].split("#")[0].split("\\$")[1];
//                            System.out.println(aidi1.playerContent(playFlags[j], pu, new ArrayList<>()));
//                        }
//                    } catch (Throwable th) {
//
//                    }
//                }
//            } catch (Throwable th) {
//
//            }
//        }
//        System.out.println(aidi1.searchContent("陪你一起", false));
//        System.out.println(aidi1.searchContent("顶楼", false));
//
//        XPath aidi = new XPath();
//        aidi.init(MainActivity.this, "{\n" +
//                "  \"ua\": \"\",\n" +
//                "  \"homeUrl\": \"http://www.9rmb.com\",\n" +
//                "  \"cateNode\": \"//ul[contains(@class,'navbar-nav')]/li/a[contains(@href, '.html') and not(contains(@href, '6'))]\",\n" +
//                "  \"cateName\": \"/text()\",\n" +
//                "  \"cateId\": \"/@href\",\n" +
//                "  \"cateIdR\": \"/type/(\\\\d+).html\",\n" +
//                "  \"cateManual\": {},\n" +
//                "  \"homeVodNode\": \"//div[@class='col-md-12 movie-item-out']//a[not(contains(@href, '6'))]/parent::*/parent::*/parent::*/div[contains(@class, 'movie-item-out') and position()<10]/div[@class='movie-item']/a\",\n" +
//                "  \"homeVodName\": \"/@title\",\n" +
//                "  \"homeVodId\": \"/@href\",\n" +
//                "  \"homeVodIdR\": \"/show/(\\\\w+).html\",\n" +
//                "  \"homeVodImg\": \"/img/@src\",\n" +
//                "  \"homeVodMark\": \"/button/text()\",\n" +
//                "  \"cateUrl\": \"http://www.9rmb.com/type/{cateId}/{catePg}.html\",\n" +
//                "  \"cateVodNode\": \"//div[@class='movie-item']/a\",\n" +
//                "  \"cateVodName\": \"/@title\",\n" +
//                "  \"cateVodId\": \"/@href\",\n" +
//                "  \"cateVodIdR\": \"/show/(\\\\w+).html\",\n" +
//                "  \"cateVodImg\": \"/img/@src\",\n" +
//                "  \"cateVodMark\": \"/button/text()\",\n" +
//                "  \"dtUrl\": \"http://www.9rmb.com/show/{vid}.html\",\n" +
//                "  \"dtNode\": \"//div[@class='container-fluid']\",\n" +
//                "  \"dtName\": \"//div[@class='col-md-9']//div[@class='col-md-4']//img/@alt\",\n" +
//                "  \"dtNameR\": \"\",\n" +
//                "  \"dtImg\": \"//div[@class='col-md-9']//div[@class='col-md-4']//img/@src\",\n" +
//                "  \"dtImgR\": \"\",\n" +
//                "  \"dtCate\": \"//div[@class='col-md-8']//span[@class='info-label' and contains(text(), '类型')]/parent::*/following-sibling::*/text()\",\n" +
//                "  \"dtCateR\": \"\",\n" +
//                "  \"dtYear\": \"//div[@class='col-md-8']//span[@class='info-label' and contains(text(), '日期')]/parent::*/following-sibling::*/text()\",\n" +
//                "  \"dtYearR\": \"\",\n" +
//                "  \"dtArea\": \"//div[@class='col-md-8']//span[@class='info-label' and contains(text(), '国家')]/parent::*/following-sibling::*/text()\",\n" +
//                "  \"dtAreaR\": \"\",\n" +
//                "  \"dtMark\": \"//div[@class='col-md-8']//span[@class='info-label' and contains(text(), '状态')]/parent::*/following-sibling::*/text()\",\n" +
//                "  \"dtMarkR\": \"\",\n" +
//                "  \"dtActor\": \"//div[@class='col-md-8']//span[@class='info-label' and contains(text(), '主演')]/parent::*/following-sibling::*/text()\",\n" +
//                "  \"dtActorR\": \"\",\n" +
//                "  \"dtDirector\": \"//div[@class='col-md-8']//span[@class='info-label' and contains(text(), '导演')]/parent::*/following-sibling::*/text()\",\n" +
//                "  \"dtDirectorR\": \"\",\n" +
//                "  \"dtDesc\": \"//p[@class='summary']/text()\",\n" +
//                "  \"dtDescR\": \"\",\n" +
//                "  \"dtFromNode\": \"//div[contains(@class,'resource-list')]/div[@class='panel-heading']/strong\",\n" +
//                "  \"dtFromName\": \"/text()\",\n" +
//                "  \"dtFromNameR\": \"\\\\S+\\\\.(\\\\S+) \\\\(\",\n" +
//                "  \"dtUrlNode\": \"//div[contains(@class,'resource-list')]/ul[@class='dslist-group']\",\n" +
//                "  \"dtUrlSubNode\": \"/li/a\",\n" +
//                "  \"dtUrlId\": \"@href\",\n" +
//                "  \"dtUrlIdR\": \"/play/(\\\\S+).html\",\n" +
//                "  \"dtUrlName\": \"/text()\",\n" +
//                "  \"dtUrlNameR\": \"\",\n" +
//                "  \"playUrl\": \"http://www.9rmb.com/play/{playUrl}.html\",\n" +
//                "  \"playUa\": \"\",\n" +
//                "  \"searchUrl\": \"http://www.9rmb.com/search?wd={wd}\",\n" +
//                "  \"scVodNode\": \"//div[@class='movie-item']/a\",\n" +
//                "  \"scVodName\": \"/@title\",\n" +
//                "  \"scVodId\": \"/@href\",\n" +
//                "  \"scVodIdR\": \"/show/(\\\\w+).html\",\n" +
//                "  \"scVodImg\": \"/img/@src\",\n" +
//                "  \"scVodMark\": \"/button/text()\"\n" +
//                "}\n");
//        System.out.println(aidi.homeContent(true));
//        System.out.println(aidi.homeVideoContent());
//        System.out.println(aidi.categoryContent("2", "1", false, null));
//        List<String> ids = new ArrayList<String>();
//        ids.add("25603");
//        System.out.println(aidi.detailContent(ids));
//        System.out.println(aidi.playerContent("", "11111", new ArrayList<>()));
//        System.out.println(aidi.searchContent("陪你一起", false));
//    }


//    String url = m3u8Url3;
//    Map<String,String> params = new HashMap<>();
//                params.put("do", "m3u8");
//                        params.put("url", url);
////                String res = OkHttpUtil.string(params.get("url"), null);
//                        OkHttpClient client = OkHttpUtil.defaultClient();
//                        okhttp3.Request request = new Request.Builder().get()
//                        .url(Objects.requireNonNull(url)).build();
//                        try {
////                    Response response = client.newCall(request).execute();
////                    String res = response.body().string();
////                    String res = Proxy.fixM3u8(url, params);
//                        Object[] res = Proxy.proxy(params);
//                        System.out.println(res.toString());
//                        } catch (Exception e) {
//                        e.printStackTrace();
//                        }
//                Proxy proxy = new Proxy();
//                Object[] objects = Proxy.proxy(params);
//                Salad salad = new Salad();
//                String s = salad.homeContent(true);
//                System.out.println(s);
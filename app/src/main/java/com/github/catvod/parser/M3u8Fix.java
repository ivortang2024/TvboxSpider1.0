package com.github.catvod.parser;

import android.annotation.TargetApi;
import android.os.Build;

import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.App;
import com.github.catvod.utils.okhttp.OkHttpUtil;
import com.m3u8.parser.model.ContextType;
import com.m3u8.parser.model.PlayList;
import com.m3u8.parser.model.TrackData;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3u8Fix {

    @TargetApi(Build.VERSION_CODES.N)
    public static String fixM3u8(String url, Map<String, String> params) throws URISyntaxException {
        SpiderDebug.log("params:" + params.toString());
        String res = OkHttpUtil.string(url, null);
        if (res.length() == 0) {
            SpiderDebug.log("okhttp3获取m3u8失败 url:" + url);
            return "";
        }
        SpiderDebug.log("okhttp3获取m3u8成功 url:" + url);

        PlayList playList = new PlayList(res, url);
        SpiderDebug.log("解析成功，url:" + url);
        if (playList.getContentType() == ContextType.MASTER) {
            url = new URI(url).resolve(playList.getSubUri().get(0)).toString();
            SpiderDebug.log("okhttp3获取子列表url:" + url);
            return fixM3u8(url, params);
        }

        String flag = params.get("flag");
        SpiderDebug.log("播放源：" + flag);

        if ("snm3u8".equals(Objects.requireNonNull(flag))) {
            cutHeaderAds(playList.getTrackDataList(), url);
        } else if ("ffm3u8".equals(Objects.requireNonNull(flag))) {
            ffCutAds(playList.getTrackDataList(), url);
        } else {
            cutAds(playList.getTrackDataList(), url);
        }
        res = playList.toString();
        SpiderDebug.log("打包成功,长度为" + playList.getTrackDataList().size());
        SpiderDebug.log(res);
        return res;
    }

    private static void cutHeaderAds(List<TrackData> mediaSegmentList, String url) {
//        if (mediaSegmentList.size() > 10) mediaSegmentList.subList(0, 10).clear();
        int len = mediaSegmentList.size();
        if (len < 5) return;
        int duration = 0;
        while (true) {
            duration += mediaSegmentList.get(0).getTrackInfo().duration;
            if (duration > 22) break;
            mediaSegmentList.remove(0);
        }
    }

    public static void cutAds(List<TrackData> mediaSegmentList, String url) throws URISyntaxException {
        boolean start = false;
        int cnt = 0, len = mediaSegmentList.size();
        for (int i = 0; i < len; i++) {
            TrackData segment = mediaSegmentList.get(i);
            if (segment.hasDiscontinuity() && !start && i > 0) {
                String prevUrl = incrementTsFilename(mediaSegmentList.get(i - 1).getUri());
                if (!segment.getUri().equals(prevUrl) && cnt == 0) {
                    start = true;
                    cnt += 0;
                }
            } else if (segment.hasDiscontinuity() && start) {
                start = false;
            }
            segment.setDiscontinuity(start);
        }

        float duration = 0;
        for (int i = 0; i < len; ) {
            TrackData seg = mediaSegmentList.get(i);
            if (seg.hasDiscontinuity()) {
                mediaSegmentList.remove(seg);
                duration += seg.getTrackInfo().duration;
                len--;
            } else {
                String uri = new URI(url).resolve(seg.getUri()).toString();
                seg.setUri(uri);
                i++;
            }
        }
        if (duration > 0.1) App.showToast(String.format("已删去广告%.1f秒.", duration));

    }

    /**
     * 非凡去广告
     *
     * @param mediaSegmentList
     * @param url
     * @throws URISyntaxException
     */
    private static void ffCutAds(List<TrackData> mediaSegmentList, String url) throws URISyntaxException {
        List<TrackData> ads = new ArrayList<>();
        List<TrackData> tmpTracks = new ArrayList<>();
        double durations = 0.0;
        for (TrackData seg : mediaSegmentList) {
            String uri = new URI(url).resolve(seg.getUri()).toString();
            seg.setUri(uri);

            if (seg.hasDiscontinuity()) {
//                if (durations - 18.0 < 0.001 && durations - 18.0 > 0.0) {
                if (durations > 17.0 && durations < 21.0) {
                    ads.addAll(tmpTracks);
                }
                durations = 0.0;
                tmpTracks.clear();
            }

            durations += seg.getTrackInfo().duration;
            tmpTracks.add(seg);

        }

        durations = 0.0;
        for (TrackData ad : ads) {
            mediaSegmentList.remove(ad);
            durations += ad.getTrackInfo().duration;
        }

        if (durations > 0.1) App.showToast(String.format("已删去广告%.1f秒.", durations));

    }

    private static String incrementTsFilename(String filename) {
        Pattern pattern = Pattern.compile("(\\d+)(?=\\.ts$)");
        Matcher matcher = pattern.matcher(filename);

        if (matcher.find()) {
            String number = matcher.group(1);
            long newNumber = Long.parseLong(number) + 1;
            String newNumberStr = String.format("%0" + number.length() + "d", newNumber);
            return filename.substring(0, matcher.start(1)) + newNumberStr + filename.substring(matcher.end(1));
        } else {
            return filename;
        }
    }

    private static HashMap<String, String> getHeaders(Map<String, String> params) {
        params.put("Connection", "Keep-Alive");
        params.put("User-Agent", "okhttp/4.0.1");
        params.remove("do");
        params.remove("url");
        return (HashMap<String, String>) params;
    }

    public static Object[] loadM3u8(String context) {
        try {
            SpiderDebug.log("开始转http响应");
            Object[] result = new Object[3];
            result[0] = 200;
            result[1] = "text/plain; charset=utf-8";
            result[2] = new ByteArrayInputStream(context.getBytes("UTF-8"));
            SpiderDebug.log("转换响应完成：" + result.toString());
            return result;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }
}

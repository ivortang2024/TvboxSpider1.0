package com.m3u8.parser;

import com.github.catvod.crawler.SpiderDebug;
import com.m3u8.parser.model.ContextType;
import com.m3u8.parser.model.PlayList;
import com.m3u8.parser.model.TrackData;
import com.m3u8.parser.model.TrackInfo;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author tangah
 * @Title: Parser
 * @ProjectName Tools
 * @Description:
 * @date 2024/6/12     13:50
 */
public class Parser {
    private final static String text = "#EXTM3U\n" +
            "#EXT-X-STREAM-INF:PROGRAM-ID=1,BANDWIDTH=3652000,RESOLUTION=1920x1080\n" +
            "/20240125/fRYDxpXV/3652kb/hls/index.m3u8";
    private final static String media = "#EXTM3U\n" +
            "#EXT-X-VERSION:4\n" +
            "#EXT-X-START:TIME-OFFSET=-4.5,PRECISE=YES\n" +
            "#EXT-X-TARGETDURATION:10\n" +
            "#EXTINF:9.009,\n" +
            "http://media.example.com/first.ts\n" +
            "#EXTINF:9.009,\n" +
            "http://media.example.com/second.ts\n" +
            "#EXTINF:3.003,\n" +
            "http://media.example.com/third.ts\n" +
            "#EXT-X-ENDLIST";

    public static PlayList parse(String in, String baseUrl) throws URISyntaxException {
        if (in == null) {
            assert false;
            if (in.length() == 0) return null;
        }
        if (in.contains("EXT-X-STREAM-INF")) {
            return masterParse(in, baseUrl);
        } else if (in.contains("#EXT-X-ENDLIST")) {
            return mediaParse(in, baseUrl);
        }
        return null;
    }

    // 只考虑最简单的情况
    public static PlayList masterParse(String masterIn, String baseUrl) {
        PlayList playList = new PlayList(masterIn, baseUrl);
        playList.setContentType(ContextType.MASTER);
        String[] items = masterIn.split("#");
        for (String item : items) {
            if (item.startsWith("EXT-X-STREAM-INF")) {
                String[] uris = item.split("\n");
                playList.getSubUri().add(uris[1]);
            }
            if (!"".equals(item)) playList.getHeaders().add("#" + item);
        }
        return playList;
    }

    public static PlayList mediaParse(String mediaIn, String baseUrl) throws URISyntaxException {
        PlayList playList = new PlayList(mediaIn, baseUrl);
        playList.setContentType(ContextType.MEDIA);

        String[] items = mediaIn.split("#");
        boolean ads = false;
        for (int i = 1; i < items.length; i++) {
            String item = items[i];
            if (item.contains("ENDLIST")) break;
            if (!item.contains("EXTINF") && !item.contains("EXT-X-DISCONTINUITY")) {
                playList.getHeaders().add("#" + item);
            } else if (item.contains("EXT-X-DISCONTINUITY")) {
                ads = true;
            } else {
                String[] line = item.split(",");
                String uri = line[1].replace("\n", "").trim();
                if (baseUrl != null && baseUrl.length() > 0) {
                    uri = new URI(baseUrl).resolve(uri).toString();
                }
                String time = line[0].split(":")[1];
                TrackInfo trackInfo = new TrackInfo(Float.parseFloat(time), "");
                TrackData trackData = new TrackData.Builder()
                        .withDiscontinuity(ads)
                        .withTrackInfo(trackInfo)
                        .withUri(uri.replace("\n", "").trim())
                        .build();
                playList.getTrackDataList().add(trackData);
                ads = false;
            }
        }
        return playList;
    }

    public static String printPlaylist(PlayList playList) {
        StringBuilder builder = new StringBuilder();
        for (String header : playList.getHeaders()) {
            builder.append(header);
        }
        if (playList.getContentType() == ContextType.MEDIA) {
            List<TrackData> trackDatas = playList.getTrackDataList();
            for (TrackData item : trackDatas) {
                if (item.hasDiscontinuity()) {
                    builder.append("#EXT-X-DISCONTINUITY\n");
                }
                builder.append("#EXTINF:")
                        .append(item.getTrackInfo().duration).append(",\n")
                        .append(item.getUri()).append("\n");
            }
            builder.append("#EXT-X-ENDLIST");
        }
        try {
             byte[] bytes = builder.toString().getBytes("UTF-8");
             return new String(bytes, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            SpiderDebug.log(e.getLocalizedMessage());
            return "";
        }
    }
}

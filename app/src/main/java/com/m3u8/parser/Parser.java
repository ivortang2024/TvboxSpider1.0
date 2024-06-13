package com.m3u8.parser;

import com.github.catvod.crawler.SpiderDebug;
import com.m3u8.parser.model.ContextType;
import com.m3u8.parser.model.PlayList;
import com.m3u8.parser.model.TrackData;
import com.m3u8.parser.model.TrackInfo;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author tangah
 * @Title: Parser
 * @ProjectName Tools
 * @Description:
 * @date 2024/6/12     13:50
 */
public class Parser {
    public static void parse(PlayList playList) throws URISyntaxException {
        if (playList.getM3u8() == null) {
            assert false;
            if (playList.getM3u8().length() == 0) return;
        }
        if (playList.getM3u8().contains("EXT-X-STREAM-INF")) {
            masterParse(playList);
        } else if (playList.getM3u8().contains("#EXT-X-ENDLIST")) {
            mediaParse(playList);
        }
    }

    // 只考虑最简单的情况
    public static void masterParse(PlayList playList) {
        String masterIn = playList.getM3u8();
        playList.setContentType(ContextType.MASTER);
        String[] items = masterIn.split("#");
        for (String item : items) {
            if (item.startsWith("EXT-X-STREAM-INF")) {
                String[] uris = item.split("\n");
                playList.getSubUri().add(uris[1]);
            }
            if (!"".equals(item)) playList.getHeaders().add("#" + item);
        }
    }

    public static void mediaParse(PlayList playList) throws URISyntaxException {
        String mediaIn = playList.getM3u8(), baseUrl = playList.getUrl();
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            SpiderDebug.log(e.getLocalizedMessage());
            return "";
        }
    }
}

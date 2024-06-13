package com.m3u8.parser.model;

import com.github.catvod.crawler.SpiderDebug;
import com.m3u8.parser.Parser;
import com.m3u8.parser.model.TrackData;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tangah
 * @Title: MediaPlayList
 * @ProjectName Tools
 * @Description:
 * @date 2024/6/12     14:00
 */
public class PlayList {
    // 简单一点
    private List<String> headers = new ArrayList<>();

    private final List<TrackData> trackDataList = new ArrayList<>();

    private int ContentType;

    private List<String> subUri = new ArrayList<>();

    private String m3u8;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PlayList(String m3u8, String url) {
        this.m3u8 = m3u8;
        this.url = url;
        parse();
    }

    public String getM3u8() {
        return m3u8;
    }


    public List<TrackData> getTrackDataList() {
        return trackDataList;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }


    private void parse() {
        try {
            Parser.parse(this);
        } catch (URISyntaxException e) {
            SpiderDebug.log(e.getLocalizedMessage());
        }
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public int getContentType() {
        return ContentType;
    }

    public void setContentType(int contentType) {
        ContentType = contentType;
    }

    public List<String> getSubUri() {
        return subUri;
    }

    public void setSubUri(List<String> subUri) {
        this.subUri = subUri;
    }

    @Override
    public String toString() {
        return Parser.printPlaylist(this);
    }
}

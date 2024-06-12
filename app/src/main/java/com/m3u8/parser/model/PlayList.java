package com.m3u8.parser.model;

import com.m3u8.parser.Parser;

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

    private List<TrackData> trackDataList = new ArrayList<>();

    private int ContentType;

    private List<String> subUri = new ArrayList<>();

    private String m3u8;

    private final String url;

    public PlayList(String m3u8, String url) {
        this.m3u8 = m3u8;
        this.url = url;
    }

    public String getM3u8() {
        return m3u8;
    }

    public void setM3u8(String m3u8) {
        this.m3u8 = m3u8;
    }


    public PlayList parse() throws URISyntaxException {
        return Parser.parse(this.m3u8, this.url);
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<TrackData> getTrackDataList() {
        return trackDataList;
    }

    public void setTrackData(List<TrackData> trackDataList) {
        this.trackDataList = trackDataList;
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

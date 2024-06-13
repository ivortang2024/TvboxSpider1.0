package com.github.catvod.spider;

import android.util.Base64;

import com.github.catvod.crawler.Spider;
import com.github.catvod.live.TxtSubscribe;
import com.github.catvod.parser.M3u8Fix;

import java.util.Map;

public class Proxy extends Spider {

    public static Object[] proxy(Map<String, String> params) {
        try {
            String what = params.get("do");
            if (what.equals("nekk")) {
                String pic = params.get("pic");
                return Nekk.loadPic(pic);
            } else if (what.equals("live")) {
                String type = params.get("type");
                if (type.equals("txt")) {
                    String ext = params.get("ext");
                    ext = new String(Base64.decode(ext, Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP), "UTF-8");
                    return TxtSubscribe.load(ext);
                }
            } else if (what.equals("m3u8")) {
                return M3u8Fix.loadM3u8(M3u8Fix.fixM3u8(params.get("url"), params));
            }
        } catch (Throwable th) {

        }
        return null;
    }

}

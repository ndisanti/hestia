package com.despegar.p13n.hestia.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class URLUtils {

    private URLUtils() {
    }

    public static String addParameter(String url, String name, String value) {
        int qpos = url.indexOf('?');
        int hpos = url.indexOf('#');
        char sep = qpos == -1 ? '?' : '&';
        String seg = sep + encodeUrl(name) + '=' + encodeUrl(value);
        return hpos == -1 ? url + seg : url.substring(0, hpos) + seg + url.substring(hpos);
    }


    /**
     * Read response map as query string. This is de format used by facebook api
     *
     * @param query the query
     * @return the map
     */
    public static Map<String, String> parseQueryString(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String[] split = param.split("=");
            if (split.length == 2) {
                String name = split[0];
                String value = split[1];
                map.put(name, value);
            }
        }
        return map;
    }

    /**
     * The same behaviour as Web.escapeUrl, only without the "funky encoding" of
     * the characters ? and ; (uses JDK URLEncoder directly).
     * 
     * @param toencode
     *        The string to encode.
     * @return <code>toencode</code> fully escaped using URL rules.
     */
    public static String encodeUrl(String url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null");
        }
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw new IllegalArgumentException(uee);
        }
    }

}

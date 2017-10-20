package com.gpaer.service.v3.util;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by neo on 20/10/2017.
 *
 * 读取 http 请求 body 结果
 */
public class ReadResponseBodyUtil {
    /* 读取 response body 内容为字符串 */
    public static String readResponse(HttpResponse response) throws IOException {
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String result = new String();
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }
}

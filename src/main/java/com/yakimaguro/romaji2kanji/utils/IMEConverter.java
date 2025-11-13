/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.yakimaguro.romaji2kanji.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ひらがなのみの文章を、IMEを使用して変換します。
 * 使用される変換候補は全て第1候補のため、正しくない結果が含まれることもよくあります。
 * @author ucchy
 */
public class IMEConverter {

    private static final String GOOGLE_IME_URL =
            "https://www.google.com/transliterate?langpair=ja-Hira|ja&text=";

    private static final OkHttpClient HTTP = new OkHttpClient();

    /**
     * GoogleIMEを使って変換する
     * @param org 変換元
     * @return 変換後
     */
    public static String convByGoogleIME(String org) {
        return conv(org);
    }

    private static String conv(String org) {
        if (org.isEmpty()) {
            return "";
        }

        try {
            String url = GOOGLE_IME_URL + URLEncoder.encode(org, StandardCharsets.UTF_8);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = HTTP.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("GoogleIME request failed: " + response);
                    return "";
                }

                String json = response.body().string();
                return GoogleIME.parseJson(json);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}

package com.yht.exerciseassist.seoulAPI;

import com.yht.exerciseassist.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Component
@Slf4j
public class ApiExplorer {

    @Value("${SeoulAPI.key}")
    private String key;

    public ResponseResult<JSONObject> getSportsFacility(District district, int offset, int limit) throws IOException, ParseException {

        StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/").append(URLEncoder.encode(key, "UTF-8")); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
        urlBuilder.append("/").append(URLEncoder.encode("json", "UTF-8")); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/").append(URLEncoder.encode(String.valueOf(district), "UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/").append(URLEncoder.encode(Integer.toString(offset), "UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
        urlBuilder.append("/").append(URLEncoder.encode(Integer.toString(offset + limit - 1), "UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
        // 상위 5개는 필수적으로 순서바꾸지 않고 호출해야 합니다.

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        log.info("Response code: " + conn.getResponseCode());/* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        JSONParser jsonParser = new JSONParser();
        JSONObject res = (JSONObject) jsonParser.parse(sb.toString());
        JSONObject districtDetail = (JSONObject) res.get(String.valueOf(district));
        long listTotalCount = (Long) districtDetail.get("list_total_count");

        if (offset == 1) {
            districtDetail.put("isFirst", true);
        } else {
            districtDetail.put("isFirst", false);
        }

        if ((offset + limit - 1) > listTotalCount) {
            districtDetail.put("isLast", true);
        } else {
            districtDetail.put("isLast", false);
        }

        if (listTotalCount / limit == 0) {
            districtDetail.put("totalPages", listTotalCount / limit);
        } else {
            districtDetail.put("totalPages", listTotalCount / limit + 1);
        }

        return new ResponseResult<>(HttpStatus.OK.value(), res);
    }
}

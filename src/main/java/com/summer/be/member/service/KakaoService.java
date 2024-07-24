package com.summer.be.member.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.summer.be.member.domain.KakaoMember;
import com.summer.be.member.domain.SocialProperties;
import com.summer.be.member.domain.dto.KakaoDto;
import com.summer.be.member.repository.KakaoMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final SocialProperties socialProperties;
    private final KakaoMemberRepository kakaoMemberRepository;

    private static String clientKey;
    private static String kakaoRedirectUri;

    public void kakaoMethod() {
        clientKey = socialProperties.getKey();
        kakaoRedirectUri = socialProperties.getRedirectUri();
    }

    public String getAccessToken(String code) throws IOException {
        // 인가코드로 토큰받기
        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String token = "";
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(socialProperties.getKey());
            sb.append("&redirect_uri=").append(socialProperties.getRedirectUri());
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            }

            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }

            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);

            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();

            token = access_token;

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return token;
    }

    public KakaoDto getUserInfo(String accessToken) {
        String host = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while ((line = br.readLine()) != null) {
                res += line;
            }

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(res);
            JSONObject properties = (JSONObject) obj.get("properties");
            JSONObject kakaoAccount = (JSONObject) obj.get("kakao_account");
            String id = obj.get("id").toString();
            String nickname = properties.get("nickname").toString();
            //Object profileImgObject = properties.get("profile_image");
            //String profileImg = (profileImgObject != null) ? profileImgObject.toString() :
            //        null;  // 프로필 이미지 동의 안할 시 null 값 대신 기본 logo.png로 대체

            String email = kakaoAccount.get("email").toString();

            KakaoDto kakaoDto =  KakaoDto.builder()
                    .id(Long.valueOf(id))
                    .email(email)
                    .nickname(nickname)
                    //.profileImg(profileImg)
                    .build();

            KakaoMember kakaoMember = KakaoMember.builder()
                    .id(Long.valueOf(id))
                    .email(email)
                    .nickname(nickname)
                    //.profileImg(profileImg)
                    .build();

            kakaoMemberRepository.save(kakaoMember);

            return kakaoDto;


        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("카카오 정보를 가져오는데 실패했습니다!");
        }
    }

    public Object getKakaoApiKey() {
        return clientKey;
    }

    public Object getKakaoRedirectUri() {
        return kakaoRedirectUri;
    }
}

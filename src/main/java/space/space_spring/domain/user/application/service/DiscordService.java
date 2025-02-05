package space.space_spring.domain.user.application.service;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import space.space_spring.domain.user.application.port.in.OauthUseCase;
import space.space_spring.domain.user.domain.User;
import space.space_spring.global.exception.CustomException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscordService implements OauthUseCase {

    @Value("${oauth.discord.client.id}")
    private String DISCORD_CLIENT_ID;

    @Value("${oauth.discord.client.secret}")
    private String DISCORD_CLIENT_SECRET;

    @Value("${oauth.discord.uri.redirect}")
    private String DISCORD_REDIRECT_URI;

    @Value("${oauth.discord.uri.token}")
    private String DISCORD_TOKEN_URI;

    private static final String GRANT_TYPE = "authorization_code";
    private static final String SCOPE = "identify, email";

    @Override
    public String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", DISCORD_CLIENT_ID);
        body.add("client_secret", DISCORD_CLIENT_SECRET);
        body.add("grant_type", GRANT_TYPE);
        body.add("code", code);
        body.add("redirect_uri", DISCORD_REDIRECT_URI);
        body.add("scope", SCOPE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                DISCORD_TOKEN_URI,
               HttpMethod.POST,
               httpEntity,
               String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = objectMapper.readTree(responseBody);
            log.info("access token : " + jsonNode.get("access_token").asText());
            return jsonNode.get("access_token").asText();
        } else {
            throw new CustomException(DISCORD_TOKEN_ERROR);
        }
    }

    @Override
    public User getUserInfo(String accessToken) {
        return null;
    }

    @Override
    public String signIn(User user) {
        return "";
    }
}

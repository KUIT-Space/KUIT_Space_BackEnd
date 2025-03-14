package space.space_spring.domain.user.adapter.out.persistence;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.CANNOT_FIND_DISCORD_USER;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_TOKEN_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import space.space_spring.domain.user.application.port.out.DiscordOauthPort;
import space.space_spring.domain.user.domain.User;
import space.space_spring.global.exception.CustomException;

@Slf4j
@Component
public class DiscordOauthAdapter implements DiscordOauthPort {

    @Value("${oauth.discord.client.id}")
    private String DISCORD_CLIENT_ID;

    @Value("${oauth.discord.client.secret}")
    private String DISCORD_CLIENT_SECRET;

    @Value("${oauth.discord.uri.redirect}")
    private String DISCORD_REDIRECT_URI;

    @Value("${oauth.discord.uri.token}")
    private String DISCORD_TOKEN_URI;

    @Value("${oauth.discord.uri.request}")
    private String DISCORD_REQUEST_URI;

    private static final String GRANT_TYPE = "authorization_code";
    private static final String SCOPE = "identify, email";

    @Override
    public String getAccessToken(String code) throws JsonProcessingException {
        log.info("---env value for debugging---");
        log.info("client_id: " + DISCORD_CLIENT_ID);
        log.info("client_secret: " + DISCORD_CLIENT_SECRET);
        log.info("grant_type: " + GRANT_TYPE);
        log.info("code: " + code);
        log.info("redirect_uri: " + DISCORD_REDIRECT_URI);
        log.info("scope: " + SCOPE);
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
    public User getUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                DISCORD_REQUEST_URI,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            Long discordId = jsonNode.path("id").asLong();
            String name = jsonNode.path("username").asText();
            log.info("user id : " + discordId + ", user name : " + name);
            return User.withoutId(discordId);
        } else {
            throw new CustomException(CANNOT_FIND_DISCORD_USER);
        }
    }
}

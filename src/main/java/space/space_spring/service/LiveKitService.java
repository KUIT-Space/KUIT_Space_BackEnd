package space.space_spring.service;

import io.livekit.server.*;
import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Response;
import space.space_spring.dto.VoiceRoom.LiveKitSession;
import space.space_spring.dto.VoiceRoom.LiveKitSessionResponse;
import space.space_spring.dto.VoiceRoom.TokenRequest;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LiveKitService {
    @Value("${livekit.projet.host")
    private String hostUrl;
    @Value("${livekit.project.id}")
    private String projectId;
    @Value("${livekit.api.key}")
    private String apiKey;
    @Value("${livekit.api.secretKey}")
    private String apiSecretKey;

    private final RestTemplate restTemplate;



    //LiveKit Cloud의 Analytics API를 통해 현재 존재하는 Room 정보를 가져오는 코드입니다.
    //https://docs.livekit.io/home/cloud/analytics-api/
    public List<LiveKitSession> listLiveKitSessions() {
        String endpoint = "https://cloud-api.livekit.io/api/project/" + projectId + "/sessions/";
        AccessToken token = new AccessToken(apiKey, apiSecretKey);
        token.addGrants(new RoomList(true));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token.toJwt());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<LiveKitSessionResponse> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    entity,
                    LiveKitSessionResponse.class
            );
            //log.info("Response body: {}", response.getBody());
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.print("get response");
                System.out.print(response.getBody());
                LiveKitSessionResponse sessions = response.getBody();
                //sessions.forEach(System.out::println);  // 또는 원하는 처리를 수행
                return sessions!= null? sessions.getSessions() : null;
            } else {
                throw new RuntimeException("API call failed with status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("There was a problem: " + e.getMessage());
            return null;
        }
    }

    //LiveKit의 ServerClient SDK를 사용해서 Room 목록과 정보를 가져오는 코드입니다.
    //라이브러리에서 host URL로 request를 보내고 response를 받아옵니다.
    //https://docs.livekit.io/realtime/server/managing-rooms/
    //
    public List<LivekitModels.Room> getRoomList(){
        String liveKitHost = "https://space-biwhq7u2.livekit.cloud";
        RoomServiceClient roomServiceClient = RoomServiceClient.createClient(liveKitHost,apiKey,apiSecretKey);

        try {
            Response<List<LivekitModels.Room>> response = roomServiceClient.listRooms().execute();
            System.out.print(response.body());
            //response.body().get(0).
            //response.body().get(0).toString();
            return response.body();
        }catch (Exception e){
            System.out.println("There was a problem: " + e.getMessage());
            return null;
        }

    }
    //LiveKit room에 참여한 사용자 정보를 가져오는 함수입니다.
    //내부적으로 LiveKit Server에 request를 보내고 response를 받아 정보를 확인합니다.
    //https://docs.livekit.io/realtime/server/managing-participants/
    public List<LivekitModels.ParticipantInfo> getParticipantInfo(String roomName){
        String liveKitHost = "https://space-biwhq7u2.livekit.cloud";
        RoomServiceClient roomServiceClient = RoomServiceClient.createClient(liveKitHost,apiKey,apiSecretKey);
        String identity = roomName;
        try {
            Response<List<LivekitModels.ParticipantInfo>> response = roomServiceClient.listParticipants(roomName).execute();
            System.out.print(response.toString());
            //response.body().get(0).
            //response.body().get(0).toString();
            return response.body();
        }catch (Exception e){
            System.out.println("There was a problem: " + e.getMessage());
            return null;
        }
    }
    //LiveKit Room에 참여하기 위함 Token을 발행하는 함수 입니다.
    //여기서 생성된 JWT를 프론트에서 URL(LiveKit Cloud)로 보내면 room에 참여 가능합니다.
    public AccessToken getRoomToken(TokenRequest tokenRequest){
        if(tokenRequest==null){
            tokenRequest = new TokenRequest();
        }
        if(tokenRequest.getName()==null){
            tokenRequest.setName("defaultName"+getRangeRandomNum(0,9999));
        }
        if(tokenRequest.getIdentity()==null){
            tokenRequest.setIdentity(getRandomString(10,false));
        }
        if(tokenRequest.getRoomName()==null){
            tokenRequest.setRoomName("defaultRoom");
        }
        if(tokenRequest.getMetadata()==null){
            tokenRequest.setMetadata("default metadata");
        }
        return getRoomToken(tokenRequest.getName(),tokenRequest.getIdentity(),
                tokenRequest.getRoomName(),tokenRequest.getMetadata());
    }
    public AccessToken getRoomToken(String name, String identity, String roomName, String metadata){

        AccessToken token = new AccessToken(apiKey, apiSecretKey);

        // Fill in token information.
        token.setName(name);
        token.setIdentity(identity);
        token.setMetadata(metadata);
        token.addGrants(new RoomJoin(true), new RoomName(roomName));
        return token;
    }

    private String getRandomString(int length, boolean isUpperCase){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return isUpperCase ? sb.toString() : sb.toString().toLowerCase();
    }
    public static int getRangeRandomNum(int start, int end) {
        SecureRandom secureRandom = new SecureRandom();
        return start + secureRandom.nextInt(end + 1);
    }

}

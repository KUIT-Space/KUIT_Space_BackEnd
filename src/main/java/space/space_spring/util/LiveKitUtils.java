package space.space_spring.util;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.RoomServiceClient;
import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import retrofit2.Response;
import space.space_spring.domain.voiceRoom.model.dto.ParticipantDto;

import java.util.List;
@Component
@RequiredArgsConstructor
public class LiveKitUtils {
    @Value("${livekit.project.host}")
    private String hostUrl;
    @Value("${livekit.project.id}")
    private String projectId;
    @Value("${livekit.api.key}")
    private String apiKey;
    @Value("${livekit.api.secretKey}")
    private String apiSecretKey;

    private final RestTemplate restTemplate;

    public List<LivekitModels.Room> getRoomList(List<String> roomNameList) {
        String liveKitHost = hostUrl;
        System.out.print("[liveKit Url]:"+hostUrl);
        RoomServiceClient roomServiceClient = RoomServiceClient.createClient(liveKitHost, apiKey, apiSecretKey);

        try {
            Response<List<LivekitModels.Room>> response = roomServiceClient.listRooms(roomNameList).execute();
            System.out.print(response.body());

            //response.body().get(0).
            //response.body().get(0).toString();
            System.out.print("[DEBUG]LiveKit RoomList API:"+response.body());
            return response.body();
        } catch (Exception e) {
            System.out.println("There was a problem: " + e.getMessage());
            return null;
        }

    }

    public List<LivekitModels.Room> getRoomList(){
        return getRoomList(null);
    }

    public List<ParticipantDto> getParticipantInfo(String roomName) {
        String liveKitHost = hostUrl;
        RoomServiceClient roomServiceClient = RoomServiceClient.createClient(liveKitHost, apiKey, apiSecretKey);
        String identity = roomName;
        try {
            Response<List<LivekitModels.ParticipantInfo>> response = roomServiceClient.listParticipants(roomName).execute();
            System.out.print("[DEBUG]LiveKit participant List response:"+response.body().toString());
            //response.body().get(0).getTracksList().get(0).getMuted();
            //response.body().get(0).getTracksList().get(0).;
            //response.body().get(0).toString();
            return ParticipantDto.convertParticipantList(response.body());
        } catch (Exception e) {
            System.out.println("There was a problem: " + e.getMessage());
            return null;
        }
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
}


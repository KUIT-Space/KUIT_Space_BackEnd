package space.space_spring.controller;

import io.livekit.server.AccessToken;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.space_spring.dto.VoiceRoom.LiveKitSession;
import space.space_spring.dto.VoiceRoom.ParticipantDto;
import space.space_spring.dto.VoiceRoom.RoomDto;
import space.space_spring.dto.VoiceRoom.GetTokenRequest;
import space.space_spring.service.LiveKitService;

import java.util.List;

@Controller
@RequestMapping("/live")
@RequiredArgsConstructor
public class LiveKitController {

    private final LiveKitService liveKitService;

    @GetMapping("/token")
    public ResponseEntity<String> showUserForm(@RequestBody @Nullable GetTokenRequest getTokenRequest){
        AccessToken token = new AccessToken("APIS272JhrVicJF", "b8q8i1TDsPi5jTd6gq0kDMJF58Bc0wtgg4DJfEwfCf9B");

        // Fill in token information.
//        token.setName("name");
//        token.setIdentity("identity3");
//        token.setMetadata("metadata3");
//        token.addGrants(new RoomJoin(true), new RoomName("myroom"));
        //liveKitService.getRoomToken(TokenRequest);
        // Sign and create token string.
        System.out.println("New access token: " + token.toJwt());
        return ResponseEntity.ok(liveKitService.getRoomToken(getTokenRequest).toJwt());
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<LiveKitSession>> getLiveKitSessions() {
        List<LiveKitSession> sessions = liveKitService.listLiveKitSessions();
        if (sessions != null) {
            return ResponseEntity.ok(sessions);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/roomList")
    public ResponseEntity<String> getLiveKitRoomList(){
        List<RoomDto> roomList = liveKitService.getRoomDtoList();
        if (roomList != null) {
            return ResponseEntity.ok(roomList.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/participant")
    public ResponseEntity<String> participantList(@RequestParam("roomName") String roomName){
        System.out.print("participant Input : "+roomName);
        List<ParticipantDto> participantList= liveKitService.getParticipantInfo(roomName);
        if (participantList != null) {
            //participantList.get(0).getTracksList().get(0).getMuted();
            return ResponseEntity.ok(participantList.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

package space.space_spring.dto.VoiceRoom;

import lombok.Getter;

public class GetToken {
    @Getter
    public static class Request{
        private long roomId;
    }
    @Getter
    public static class Response{
        private String token;
        public Response(String token){
            this.token=token;
        }
        @Override
        public String toString(){
            return token;
        }
    }
}

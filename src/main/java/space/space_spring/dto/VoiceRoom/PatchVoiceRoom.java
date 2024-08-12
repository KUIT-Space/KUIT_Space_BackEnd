package space.space_spring.dto.VoiceRoom;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PatchVoiceRoom {
    @NotEmpty(message = "updateRoomList is empty")
    private List<UpdateRoom> updateRoomList;
    @Getter
    @Setter
    public static class UpdateRoom{
        @NotNull
        private Long roomId;
        @Nullable
        private String name;
        @Nullable
        private Integer order;
    }
}

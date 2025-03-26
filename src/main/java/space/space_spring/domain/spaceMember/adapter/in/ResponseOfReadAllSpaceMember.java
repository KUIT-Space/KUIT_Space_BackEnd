package space.space_spring.domain.spaceMember.adapter.in;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.spaceMember.application.port.out.query.SpaceMemberDetail;

import java.util.List;

@Getter
public class ResponseOfReadAllSpaceMember {

    List<SpaceMemberDetail> spaceMemberDetails;

    private ResponseOfReadAllSpaceMember(List<SpaceMemberDetail> spaceMemberDetails) {
        this.spaceMemberDetails = spaceMemberDetails;
    }

    public static ResponseOfReadAllSpaceMember of(List<SpaceMemberDetail> spaceMemberDetails) {
        return new ResponseOfReadAllSpaceMember(spaceMemberDetails);
    }
}

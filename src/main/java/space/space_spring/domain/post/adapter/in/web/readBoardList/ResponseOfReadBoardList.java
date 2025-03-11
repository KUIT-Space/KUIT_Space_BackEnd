package space.space_spring.domain.post.adapter.in.web.readBoardList;

import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardInfoCommand;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardListCommand;

import java.util.List;

@Getter
public class ResponseOfReadBoardList {

    private List<ResponseOfBoardInfo> readBoardList;

    private ResponseOfReadBoardList(List<ReadBoardInfoCommand> readBoardList) {
        this.readBoardList = readBoardList.stream()
                .map(ResponseOfBoardInfo::of)
                .toList();
    }

    public static ResponseOfReadBoardList of(ReadBoardListCommand list) {
        return new ResponseOfReadBoardList(list.getReadBoardList());
    }
}

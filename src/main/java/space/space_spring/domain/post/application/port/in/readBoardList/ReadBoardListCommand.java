package space.space_spring.domain.post.application.port.in.readBoardList;

import lombok.Getter;

import java.util.List;

@Getter
public class ReadBoardListCommand {

    private List<ReadBoardInfoCommand> readBoardList;

    private ReadBoardListCommand(List<ReadBoardInfoCommand> readBoardList) {
        this.readBoardList = readBoardList;
    }

    public static ReadBoardListCommand of(List<ReadBoardInfoCommand> readBoardList) {
        return new ReadBoardListCommand(readBoardList);
    }
}

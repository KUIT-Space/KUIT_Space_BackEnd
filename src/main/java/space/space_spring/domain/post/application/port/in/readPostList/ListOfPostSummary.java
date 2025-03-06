package space.space_spring.domain.post.application.port.in.readPostList;

import lombok.Getter;

import java.util.List;

@Getter
public class ListOfPostSummary {

    private List<PostSummary> readPostList;

    private ListOfPostSummary(List<PostSummary> readPostList) {
        this.readPostList = readPostList;
    }

    public static ListOfPostSummary of(List<PostSummary> readPostList) {
        return new ListOfPostSummary(readPostList);
    }
}

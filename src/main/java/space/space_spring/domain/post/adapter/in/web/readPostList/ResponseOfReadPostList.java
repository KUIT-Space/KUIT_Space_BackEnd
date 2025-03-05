package space.space_spring.domain.post.adapter.in.web.readPostList;

import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;

import java.util.List;

@Getter
public class ResponseOfReadPostList {

    private List<ResponseOfPostSummary> readPostList;

    private ResponseOfReadPostList(List<PostSummary> readPostList) {
        this.readPostList = readPostList.stream()
                .map(ResponseOfPostSummary::of)
                .toList();
    }

    public static ResponseOfReadPostList of(ListOfPostSummary list) {
        return new ResponseOfReadPostList(list.getReadPostList());
    }
}

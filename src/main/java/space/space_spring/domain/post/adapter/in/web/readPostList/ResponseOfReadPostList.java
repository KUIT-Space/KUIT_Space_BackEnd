package space.space_spring.domain.post.adapter.in.web.readPostList;

import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;

import java.util.List;

@Getter
public class ResponseOfReadPostList {

    private List<ResponseOfPostSummary> readPostList;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

    private Boolean isLast;

    private ResponseOfReadPostList(List<PostSummary> readPostList, int page, int size, long totalElements, int totalPages, Boolean isLast) {
        this.readPostList = readPostList.stream()
                .map(ResponseOfPostSummary::of)
                .toList();
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
    }

    public static ResponseOfReadPostList of(ListOfPostSummary list) {
        return new ResponseOfReadPostList(
                list.getReadPostList(),
                list.getPage(),
                list.getSize(),
                list.getTotalElements(),
                list.getTotalPages(),
                list.getIsLast());
    }
}

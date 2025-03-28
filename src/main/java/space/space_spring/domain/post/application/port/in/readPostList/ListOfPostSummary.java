package space.space_spring.domain.post.application.port.in.readPostList;

import lombok.Getter;
import org.springframework.data.domain.Page;
import space.space_spring.domain.post.domain.Post;

import java.util.List;

@Getter
public class ListOfPostSummary {

    private List<PostSummary> readPostList;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

    private Boolean isLast;

    private ListOfPostSummary(List<PostSummary> readPostList, int page, int size, long totalElements, int totalPages, Boolean isLast) {
        this.readPostList = readPostList;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.isLast = isLast;
    }

    public static ListOfPostSummary of(Page<Post> postPage, List<PostSummary> summaries) {
        return new ListOfPostSummary(
                summaries,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast());
    }
}

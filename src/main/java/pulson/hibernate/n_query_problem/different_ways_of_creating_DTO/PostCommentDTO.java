package pulson.hibernate.n_query_problem.different_ways_of_creating_DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PostCommentDTO {

    private  Long id;
    private  String review;
    private  String postTitle;

    public PostCommentDTO(Long id, String review, String postTitle) {
        this.id = id;
        this.review = review;
        this.postTitle = postTitle;
    }
}

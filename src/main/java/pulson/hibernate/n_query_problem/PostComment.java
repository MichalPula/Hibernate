package pulson.hibernate.n_query_problem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pulson.hibernate.n_query_problem.different_ways_of_creating_DTO.PostCommentDTO;

import javax.persistence.*;
import java.util.Objects;


@NamedNativeQueries({
        @NamedNativeQuery(name = "postComment.getDTO",
                query = "select pc.id, pc.review, p.title from n_plus_1_query_problem.posts_comments pc join n_plus_1_query_problem.posts p on pc.post_id = p.id",
                resultSetMapping = "PostCommentDTO"
        )
})
@SqlResultSetMapping(name = "PostCommentDTO",
        classes = {@ConstructorResult(targetClass = PostCommentDTO.class,
                columns = {@ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "review", type = String.class),
                        @ColumnResult(name = "title", type = String.class),
                })
        })


@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "posts_comments", schema = "n_plus_1_query_problem")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String review;

    //defaultowo relacje ..ToOne są EAGERly loaded
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    //@ToString.Exclude
    private Post post;

    public PostComment(Post post, String review) {
        this.post = post;
        this.review = review;
    }
}

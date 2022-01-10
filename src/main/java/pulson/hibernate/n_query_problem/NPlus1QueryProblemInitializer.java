package pulson.hibernate.n_query_problem;

import org.springframework.stereotype.Component;
import pulson.hibernate.n_query_problem.different_ways_of_creating_DTO.DifferentWaysForDTO;

import java.util.Arrays;

@Component
public class NPlus1QueryProblemInitializer {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final DifferentWaysForDTO waysForDTO;

    public NPlus1QueryProblemInitializer(PostRepository postRepository, PostCommentRepository postCommentRepository,
                                         DifferentWaysForDTO waysForDTO) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.waysForDTO = waysForDTO;

        //initialize();
    }

    private void initialize() {
        Post post1 = new Post("Learning - Part 1");
        Post post2 = new Post("Learning - Part 2");
        Post post3 = new Post("Learning - Part 3");
        Post post4 = new Post("Learning - Part 4");
        Post post5 = new Post("Learning - Part 5");
        postRepository.saveAll(Arrays.asList(post1, post2, post3, post4, post5));

        PostComment postComment1 = new PostComment(post1, "Comment to post 1");
        PostComment postComment2 = new PostComment(post2, "Comment to post 2");
        PostComment postComment3 = new PostComment(post3, "Comment to post 3");
        PostComment postComment4 = new PostComment(post4, "Comment to post 4.1");
        PostComment postComment5 = new PostComment(post4, "Comment to post 4.2");
        PostComment postComment6 = new PostComment(post4, "Comment to post 4.3");
        PostComment postComment7 = new PostComment(post4, "Comment to post 4.4");
        PostComment postComment8 = new PostComment(post5, "Comment to post 5");
        postCommentRepository.saveAll(Arrays.asList(postComment1, postComment2, postComment3, postComment4,
                postComment5, postComment6, postComment7, postComment8));


        System.out.println(waysForDTO.DTOWithCreatingNewObjectInJPQL() + "\n");
        System.out.println(waysForDTO.DTOWithModelMapper() + "\n");
        System.out.println(waysForDTO.DTOWithNamedNativeQuery() + "\n");
        System.out.println(waysForDTO.DTOWithTupleJPQL() + "\n");
        System.out.println(waysForDTO.DTOWithTupleNativeSQL() + "\n");
    }
}

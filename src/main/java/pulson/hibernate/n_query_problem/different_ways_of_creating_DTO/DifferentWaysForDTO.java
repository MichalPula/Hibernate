package pulson.hibernate.n_query_problem.different_ways_of_creating_DTO;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pulson.hibernate.n_query_problem.PostComment;
import pulson.hibernate.n_query_problem.PostCommentRepository;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DifferentWaysForDTO {

    @PersistenceContext
    private EntityManager entityManager;

    private final PostCommentRepository postCommentRepository;

    public DifferentWaysForDTO(PostCommentRepository postCommentRepository){
        this.postCommentRepository = postCommentRepository;
    }

    //public class PostComment {
    //    private Long id;
    //    private String review;
    //    @ManyToOne()
    //    private Post post;

    //public class PostCommentDTO {
    //    private  Long id;
    //    private  String review;
    //    private  String postTitle;

    public List<PostCommentDTO> DTOWithCreatingNewObjectInJPQL(){
        List<PostCommentDTO> dtos = entityManager.createQuery(
        "select new pulson.hibernate.n_query_problem.different_ways_of_creating_DTO.PostCommentDTO(pc.id, pc.review, pc.post.title) from PostComment pc", PostCommentDTO.class).getResultList();
        return dtos;
        //Hibernate:
        //    select
        //        postcommen0_.id as col_0_0_,
        //        postcommen0_.review as col_1_0_,
        //        post1_.title as col_2_0_
        //    from
        //        n_plus_1_query_problem.posts_comments postcommen0_ cross
        //    join
        //        n_plus_1_query_problem.posts post1_
        //    where
        //        postcommen0_.post_id=post1_.id
        //Niezależnie od Post.Eager/LAZY
    }

    public List<PostCommentDTO> DTOWithModelMapper(){
        //List<PostComment> postCommentsWrongWay1 = postCommentRepository.findAll();
        //List<PostComment> postCommentsWrongWay2 = entityManager.createQuery("select pc from PostComment pc", PostComment.class).getResultList();
        //z PostComment.Post.EAGER mamy n+1 problem - każdy obiekt Post wykorzystywany przez jakąkolwiek instancję PostComment musi zostać ściągnięty razem z PostComment
        //Przy 8xPostComment do 5x różnych Postów - oprócz zapytania wyszukującego wszystkie PostComments zostanie wywołane dodatkowe 5 zapytań wyszukujących każdy Post osobno
        //PostComment.Post.LAZY rzuci LazyInitializationException, bo w PostCommentDTO potrzebujemy przecież pola Post.title
        //Rozwiązaniem jest użycie join fetch
        List<PostComment> postCommentsRightWay = entityManager.createQuery("select pc from PostComment pc join fetch pc.post", PostComment.class).getResultList();
        ModelMapper modelMapper = new ModelMapper();
        //ModelMapper wymaga konkretnych nazw pól klasy stanowiącej DTO. Zadziała tylko z 'id, review i postTitle'. Inaczej nie będzie wiedział jak zmapować pola
        List<PostCommentDTO> dtos = postCommentsRightWay.stream().map(postComment -> modelMapper.map(postComment, PostCommentDTO.class)).collect(Collectors.toList());
        return dtos;
        //Hibernate:
        //    select
        //        postcommen0_.id as id1_12_0_,
        //        post1_.id as id1_11_1_,
        //        postcommen0_.post_id as post_id3_12_0_,
        //        postcommen0_.review as review2_12_0_,
        //        post1_.title as title2_11_1_
        //    from
        //        n_plus_1_query_problem.posts_comments postcommen0_
        //    inner join
        //        n_plus_1_query_problem.posts post1_
        //            on postcommen0_.post_id=post1_.id
        //Dzięki join fetch rezultat jest niezależny od PostComment.Post.EAGER/LAZY
        //Widać jednak, że tworzenie nowego obiektu przy pomocy JPQL jest najlepszą opcją
        //Z join fetch wyciągamy niepotrzebnie wszystkie pola obu klas (postComment.post_id i post.id, których nie używamy)
    }

    public List<PostCommentDTO> DTOWithNamedNativeQuery(){
        List<PostCommentDTO> dtos = entityManager.createNamedQuery("postComment.getDTO", PostCommentDTO.class).getResultList();
        return dtos;
        //Hibernate:
        //    select
        //        pc.id,
        //        pc.review,
        //        p.title
        //    from
        //        n_plus_1_query_problem.posts_comments pc
        //    join
        //        n_plus_1_query_problem.posts p
        //            on pc.post_id = p.id
        //Krótko i przejrzyście...
        //NamedQuery musi znajdować się w klasie PostComment!
    }

    public List<PostCommentDTO> DTOWithTupleJPQL(){
        List<Tuple> dtosTuples = entityManager.createQuery("select pc.id, pc.review, pc.post.title from PostComment pc", Tuple.class).getResultList();
        List<PostCommentDTO> dtos = dtosTuples.stream().map(tuple -> {
            return new PostCommentDTO(tuple.get(0, Long.class), tuple.get(1, String.class), tuple.get(2, String.class));
        }).collect(Collectors.toList());
        return dtos;
        //Hibernate:
        //    select
        //        postcommen0_.id as col_0_0_,
        //        postcommen0_.review as col_1_0_,
        //        post1_.title as col_2_0_
        //    from
        //        n_plus_1_query_problem.posts_comments postcommen0_ cross
        //    join
        //        n_plus_1_query_problem.posts post1_
        //    where
        //        postcommen0_.post_id=post1_.id
    }

    public List<PostCommentDTO> DTOWithTupleNativeSQL(){
        List<Tuple> dtosTuples = entityManager.createNativeQuery("select pc.id, pc.review, p.title as postTitle from n_plus_1_query_problem.posts_comments pc join n_plus_1_query_problem.posts p on pc.post_id = p.id", Tuple.class).getResultList();
        List<PostCommentDTO> dtos = dtosTuples.stream().map(tuple -> {
            return new PostCommentDTO(tuple.get(0, BigInteger.class).longValue(), tuple.get(1, String.class), tuple.get(2, String.class));
        }).collect(Collectors.toList());
        return dtos;
        //Hibernate:
        //    select
        //        pc.id,
        //        pc.review,
        //        p.title as postTitle
        //    from
        //        n_plus_1_query_problem.posts_comments pc
        //    join
        //        n_plus_1_query_problem.posts p
        //            on pc.post_id = p.id
    }

    //ResultTransformer is deprecated!
}

package pulson.hibernate.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAllByArtistName(String artistName);
    List<Song> findAllByArtistName(String artistName, Sort sort);
    List<Song> findAllByArtistNameOrderByTitle(String artistName);
    List<Song> findFirst5ByAlbumContaining(String album);



    @Query("select s from Song s where s.title = ?1")
    Song findByTitleJPQL(String title);
    //Hibernate: select song0_.id as id1_5_, song0_.album as album2_5_, song0_.artist_id as artist_i4_5_, song0_.title as title3_5_ from pagination.songs song0_ where song0_.title=?

    @Query("select s from Song s where s.title = :songTitle")
    Song findByTitleJPQLWithNamedParameters(@Param("songTitle") String title);//String songTitle

    @Query(value = "select * from pagination.songs s where s.title like %:songTitle%", nativeQuery = true)
    Song findByTitleNative(String songTitle);
    //Hibernate: select * from pagination.songs s where s.title = ?



    @Transactional//wymagane
    @Modifying(flushAutomatically = true)
    @Query("update Song s set s.title = ?1 where s.title = ?2")
    void setSongName(String newSongTitle, String oldSongTitle);



    @Query("select s from Song s")
    Page<Song> findAllWithPaginationJPQL(Pageable pageable);

    @Query(value = "select * from pagination.songs s where s.album like :albumName%",
    countQuery = "select count(*) from pagination.songs s where s.album like :albumName%", nativeQuery = true)
    Page<Song> findAllWithPaginationNative(@Param("albumName") String album, Pageable pageable);
}

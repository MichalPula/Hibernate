package pulson.hibernate.pagination;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "songs", schema = "pagination")
@NamedQueries({
        @NamedQuery(name = "songs.getSongsWith5LetterTitle",
                query = "select s from Song s where length(s.title) = :length")
})
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String album;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    @ToString.Exclude
    private Artist artist;


    public Song(Artist artist, String title, String album) {
        this.artist = artist;
        this.title = title;
        this.album = album;
    }
}

package pulson.hibernate.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PaginationInitializer {

    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PaginationInitializer.class);

    @Autowired
    public PaginationInitializer(ArtistRepository artistRepository, SongRepository songRepository) {
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;

        //initialize();
    }

    private void initialize() {
        Artist ye = new Artist("Kanye West");
        artistRepository.save(ye);
        songRepository.saveAll(getDondaSongs(ye));
        songRepository.saveAll(getLifeOfPabloSongs(ye));
        songRepository.saveAll(getMyBeautifulDarkTwistedFantasySongs(ye));
        songRepository.saveAll(getGraduationSongs(ye));

        Artist bladee = new Artist("Bladee");
        artistRepository.save(bladee);
        Song song1 = new Song(bladee, "Bleach", "GTBSG");
        Song song2 = new Song(bladee, "Drama", "Good Luck");
        songRepository.saveAll(Arrays.asList(song1, song2));

        LOGGER.info(songRepository.findAllByArtistName("Bladee").toString());
        LOGGER.info(songRepository.findAllByArtistName("Bladee", Sort.by(Sort.Direction.DESC, "title")).toString());
        LOGGER.info(songRepository.findAllByArtistNameOrderByTitle("Bladee").toString());
        LOGGER.info(songRepository.findFirst5ByAlbumContaining("Graduation").toString());

        LOGGER.info(songRepository.findByTitleJPQL("Runaway").toString());
        LOGGER.info(songRepository.findByTitleNative("Runaway").toString());
        LOGGER.info(songRepository.findByTitleJPQLWithNamedParameters("Runaway").toString());

        songRepository.setSongName("How many of them?", "Real Friends");
        LOGGER.info(songRepository.findByTitleNative("many of").toString());

        Page<Song> page1 = songRepository.findAllWithPaginationJPQL(PageRequest.of(0, 5, Sort.by("id")));
        LOGGER.info(page1.toString());//Page 1 of 15 containing pulson.hibernate.pagination.Song instances
        LOGGER.info(page1.getContent().toString());


        Page<Song> page2 = songRepository.findAllWithPaginationNative("The Life", PageRequest.of(0, 5, Sort.by("id")));
        LOGGER.info(page2.toString());//Page 1 of 4 containing pulson.hibernate.pagination.Song instances
        LOGGER.info(page2.getContent().toString());
    }



    private List<Song> getDondaSongs(Artist ye){
        List<String> titles = Stream.of("Donda Chant", "Jail", "God Breathed", "Off the Grid",
                        "Hurricane", "Praise God", "Jonah", "Ok Ok", "Junya", "Believe What I Say", "24",
                        "Remote Control", "Moon", "Heaven and Hell", "Donda", "Keep My Spirit Alive", "Jesus Lord",
                        "New Again", "Tell the Vision", "Lord I Need You", "Pure Souls", "Come to Life",
                        "No Child Left Behind", "Jail, Pt. 2", "Ok Ok, Pt. 2", "Junya, Pt. 2", "Jesus Lord, Pt. 2")
                .collect(Collectors.toList());
        return titles.stream().map(title -> new Song(ye, title, "Donda 2021")).collect(Collectors.toList());
    }
    private List<Song> getLifeOfPabloSongs(Artist ye){
        List<String> titles = Stream.of("Ultralight Beam", "Father Stretch My Hands, Pt. 1", "Pt. 2", "Famous", "Feedback", "Low Lights",
                        "Highlights", "Freestyle 4", "I Love Kanye", "Waves", "FML", "Real Friends", "Wolves", "Frank's Track",
                        "Siiiiiiiiilver Surffffeeeeer Intermission", "30 Hours", "No More Parties in LA",
                        "Facts (Charlie Heat Version)", "Fade", "Saint Pablo")
                .collect(Collectors.toList());
        return titles.stream().map(title -> new Song(ye, title, "The Life Of Pablo 2016")).collect(Collectors.toList());
    }
    private List<Song> getMyBeautifulDarkTwistedFantasySongs(Artist ye){
        List<String> titles = Stream.of("Dark Fantasy", "Gorgeous", "Power", "All of the Lights (Interlude)", "All of the Lights",
                        "Monster", "So Appalled", "Devil in a New Dress", "Runaway", "Hell of a Life", "Blame Game",
                        "Lost in the World", "Who Will Survive in America")
                .collect(Collectors.toList());
        return titles.stream().map(title -> new Song(ye, title, "My Beautiful Dark Twisted Fantasy 2010")).collect(Collectors.toList());
    }
    private List<Song> getGraduationSongs(Artist ye){
        List<String> titles = Stream.of("Good Morning", "Champion", "Stronger", "I Wonder", "Good Life", "Can't Tell Me Nothing", "Barry Bonds",
                        "Drunk and Hot Girls", "Flashing Lights", "Everything I Am", "The Glory", "Homecoming", "Big Brother")
                .collect(Collectors.toList());
        return titles.stream().map(title -> new Song(ye, title, "Graduation 2007")).collect(Collectors.toList());
    }
}

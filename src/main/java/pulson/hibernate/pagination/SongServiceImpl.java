package pulson.hibernate.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SongServiceImpl.class);
    private final SongRepository songRepository;

    @Autowired
    public SongServiceImpl(SongRepository songRepository){
        this.songRepository = songRepository;
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    @Override
    public List<Song> getAllSongsWithPagination(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Song> pagedResult = songRepository.findAll(pageable);
        LOGGER.info(String.valueOf(pagedResult.getTotalElements()));//73
        LOGGER.info(String.valueOf(pagedResult.getTotalPages()));//15 bo 73:5 = 14.6 czyli 15 stron z contentem
        LOGGER.info(String.valueOf(pagedResult.getSize()));//5
        LOGGER.info(String.valueOf(pagedResult.getNumber()));//0
        LOGGER.info(String.valueOf(pagedResult.getNumberOfElements()));//5
        LOGGER.info(String.valueOf(pagedResult.getSort()));//id: ASC

        return pagedResult.getContent();
    }
}

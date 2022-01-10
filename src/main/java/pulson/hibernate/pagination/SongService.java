package pulson.hibernate.pagination;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SongService {
    List<Song> getAllSongs();
    List<Song> getAllSongsWithPagination(int pageNo, int pageSize, String sortBy);
}

package pulson.hibernate.pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(SongService songService){
        this.songService = songService;
    }


    @GetMapping("/ye")
    public ResponseEntity<List<Song>> getAllYeSongs(){
        return ResponseEntity.ok().body(songService.getAllSongs());
    }


    //Wymusza występowanie wszystkich trzech parametrów w każdym zapytaniu
    //@GetMapping(value = "/paginated-ye", params = {"pageNo", "pageSize", "sortBy"})
    @GetMapping(value = "/paginated-ye") //dowolna ilość parametów - samo sortowanie, sam numer strony lub sam rozmiar strony i wszystkie inne kombinacje
    public ResponseEntity<List<Song>> getPaginatedYeSongs(@RequestParam(defaultValue = "0", name = "pageNo") int pageNo,//name musi zgadzać się z query param key
                                                          @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
                                                          @RequestParam(defaultValue = "id", name = "sortBy") String sortBy) {
        List<Song> songs = songService.getAllSongsWithPagination(pageNo, pageSize, sortBy);
        return ResponseEntity.ok().body(songs);
    }
}

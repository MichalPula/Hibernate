package pulson.hibernate.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class AnimalTransactionController {

    private final AnimalService animalService;

    @Autowired
    public AnimalTransactionController(AnimalService animalService) {
        this.animalService = animalService;
    }


    @GetMapping("/rollback-test1")
    public ResponseEntity<List<Animal>> testSaveTransactional() throws FileNotFoundException {
        animalService.deleteAll();
        animalService.saveTransactionalWithRollbackFor();
        return ResponseEntity.ok(animalService.getAll());
    }

    @GetMapping("/rollback-test2")
    public ResponseEntity<List<Animal>> testSaveTransactionalOnly(){
        animalService.deleteAll();
        animalService.saveTransactionalOnly();
        return ResponseEntity.ok(animalService.getAll());
    }

    @GetMapping("/rollback-test3")
    public ResponseEntity<List<Animal>> testSaveManualTransaction(){
        animalService.deleteAll();
        animalService.saveWithEntityManagerTransaction();
        return ResponseEntity.ok(animalService.getAll());
    }

    @GetMapping("/rollback-test4")
    public ResponseEntity<List<Animal>> testTransactionalWithTryCatchBlock(){
        animalService.deleteAll();
        animalService.transactionalWithTryCatchBlock();
        return ResponseEntity.ok(animalService.getAll());
    }
}

package pulson.hibernate.inheritance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Component
public class InheritanceInitializer {

    private final HumanRepository humanRepository;
    private final JuniorRepository juniorRepository;
    private final SeniorRepository seniorRepository;

    @Autowired
    public InheritanceInitializer(HumanRepository humanRepository, JuniorRepository juniorRepository, SeniorRepository seniorRepository) throws ParseException {
        this.humanRepository = humanRepository;
        this.juniorRepository = juniorRepository;
        this.seniorRepository = seniorRepository;

        //initialize();
    }

    private void initialize() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Junior junior1 = new Junior("Jason", "Brody", format.parse("1987-05-24"), "11111", "1A", "Best School US");
        Junior junior2 = new Junior("Oliver", "Carswell", format.parse("1989-12-7"), "22222", "2B", "Best School US");
        Junior junior3 = new Junior("Keith", "Ramsey", format.parse("1988-02-12"), "33333", "3C", "Best School US");
        juniorRepository.saveAll(Arrays.asList(junior1, junior2, junior3));

        Senior senior1 = new Senior("Senior", "One", format.parse("1971-10-3"), "Malmo University", 10);
        Senior senior2 = new Senior("Senior", "Two", format.parse("1955-05-28"), "San Francisco University", 20);
        Senior senior3 = new Senior("Senior", "Three", format.parse("1947-02-12"), "Stockholm University", 30);
        seniorRepository.saveAll(Arrays.asList(senior1, senior2, senior3));

        Human human1 = new Human("Jason", "Brody", format.parse("1987-05-24"));
        Human human2 = new Human("Jason", "Brody", format.parse("1987-05-24"));
        Human human3 = new Human("Jason", "Brody", format.parse("1987-05-24"));
        humanRepository.saveAll(Arrays.asList(human1, human2, human3));
    }
}

package pulson.hibernate.index_speed_tester;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class IndexSpeedTester {

    @PersistenceContext
    private final EntityManager entityManager;

    private final PolicemanRepository policemanRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexSpeedTester.class);

    @Autowired
    public IndexSpeedTester(EntityManager entityManager, PolicemanRepository policemanRepository){
        this.entityManager = entityManager;
        this.policemanRepository = policemanRepository;

        //generatePolicemen(1_000_00);
        //testIndexesSpeed(10);
    }


    public void testIndexesSpeed(Integer numberOfTests){
        List<Double> SQLExecutionTimesList = new ArrayList<>();
        for (int i = 0; i < numberOfTests; i++) {
            List<Object> resultList = entityManager.createNativeQuery("EXPLAIN ANALYZE SELECT * from index_speed_tester.policemen p WHERE p.first_name = 'Emma'").getResultList();
            String fullString = resultList.toString();
            String trimmedExecutionTime = "";
            String executionTimeString = "";
            if(fullString.startsWith("[Seq Scan")) {
                executionTimeString = (String) resultList.get(4);
            } else if(fullString.startsWith("[Index Scan")) {
                executionTimeString = (String) resultList.get(3);
            } else if(fullString.startsWith("[Bitmap Heap Scan")) {
                executionTimeString = (String) resultList.get(6);
            } else if(fullString.startsWith("[Gather")) {
                executionTimeString = (String) resultList.get(7);
            }
            trimmedExecutionTime = executionTimeString.replaceAll("Execution Time: ", "").replaceAll(" ms", "");
            SQLExecutionTimesList.add(Double.valueOf(trimmedExecutionTime));
            LOGGER.info(executionTimeString);
        }
        SQLExecutionTimesList.remove(0);//deleting 1st slowest query result
        Double averageExecutionTime = SQLExecutionTimesList.stream().mapToDouble(value -> value).average().orElse(0.0);
        LOGGER.info("Average SELECT execution time = " + averageExecutionTime.toString() + "ms");
    }


    private void generatePolicemen(Integer numberOfPolicemen) {
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();
        try {
            FileReader firstNamesReader = new FileReader("src/main/resources/firstNames.json");
            FileReader lastNamesReader = new FileReader("src/main/resources/lastNames.json");
            firstNames = new Gson().fromJson(firstNamesReader, new TypeToken<List<String>>() {}.getType());
            lastNames = new Gson().fromJson(lastNamesReader, new TypeToken<List<String>>() {}.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        List<Policeman> policemenList = new ArrayList<>();
        for(int i = 0; i < numberOfPolicemen; i++) {
            policemenList.add(new Policeman(
                    firstNames.get(new Random().nextInt(firstNames.size())),
                    lastNames.get(new Random().nextInt(lastNames.size())),
                    UUID.randomUUID().toString()));
        }
        policemanRepository.saveAll(policemenList);
        long endTime = System.currentTimeMillis();
        LOGGER.info(String.valueOf(endTime - startTime) + "ms = time of execution");
    }
}

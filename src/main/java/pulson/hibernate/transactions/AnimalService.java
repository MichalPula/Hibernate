package pulson.hibernate.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AnimalService {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    @PersistenceUnit
    private final EntityManagerFactory emf;

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalService(EntityManager entityManager, EntityManagerFactory emf, AnimalRepository animalRepository) {
        this.entityManager = entityManager;
        this.emf = emf;
        this.animalRepository = animalRepository;

        //initializeAnimalsNormally();
    }


    @Transactional(rollbackFor = {RuntimeException.class, FileNotFoundException.class})
    //rollbackFor jako argumenty przyjmuje klasy rozszerzające klasę Throwable
    //Pozwala to na ustalenie przy wystąpieniu których wyjątków ma zostać wykonany rollback
    //Tutaj ustawione są wszystkie wyjątki dziedziczące po RuntimeException i FileNotFoundException
    public void saveTransactionalWithRollbackFor() throws FileNotFoundException {
        Animal animal1 = new Animal("Turtle");
        Animal animal2 = new Animal("Elephant");
        Animal animal3 = new Animal("Lion");
        Animal animal4 = new Animal("Hippo");
        Animal animal5 = new Animal("Tiger");

        animalRepository.save(animal1);
        animalRepository.save(animal2);
        animalRepository.save(animal3);
        String s = null;
        System.out.println(s.length());
        //NullPointerException extends RuntimeException
        //Bez @Transactional do bazy danych zostaną zapisane 3 pierwsze rekordy - zapisywane przed wystąpieniem wyjątku
        //Z @Transactional - nic nie zostanie zapisane do bazy
        animalRepository.save(animal4);
        animalRepository.save(animal5);

        //throw new FileNotFoundException();
        //bez zadeklarowania rollbackFor = {FileNotFoundException.class}
        //wyjątek zostałby rzucony, ale wszystkie zmiany zostałyby wprowadzone do bazy danych
    }

    @Transactional
    //Z samą adnotacją @Transactional rollback nastąpi przy RuntimeException i Error, ale nie w wypadku checked exceptions
    //Działa z prostym configiem w app.properties
    //i z konfiguracją beanów DataSource, LocalContainerEntityManagerFactoryBean i JpaTransactionManager
    //jednak przy java-based configu tabela nie resetuje id. Kolejne wywołania powodują zwiększanie się id
    public void saveTransactionalOnly() {
        Animal tiger = new Animal("Tiger");
        animalRepository.save(tiger);

        String s = null;
        System.out.println(s.length());
    }

    //Działa z prostym configiem w app.properties
    //i z konfiguracją beanów DataSource, LocalContainerEntityManagerFactoryBean i JpaTransactionManager
    public void saveWithEntityManagerTransaction() {
        Animal tiger = new Animal("Tiger");
        EntityManager localManager = emf.createEntityManager();
        try{
            localManager.getTransaction().begin();

            localManager.persist(tiger);
            localManager.createNativeQuery("insert into spring_transactions.animals (name) values ('Tiger')").executeUpdate();
            String s = null;
            System.out.println(s.length());

            //Wyjątek rzucony po commicie nie spowoduje rollbacku
            localManager.getTransaction().commit();
        } catch (Exception e) {
            localManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Transactional
    public void transactionalWithTryCatchBlock(){
        Animal tiger1 = new Animal("Tiger1");
        Animal tiger2 = new Animal("Tiger2");
        animalRepository.save(tiger1);
        try{
            String s = null;
            System.out.println(s.length());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        animalRepository.save(tiger2);
        //try catch spowoduje, że rollback nie nastąpi
    }


    public List<Animal> getAll() {
        return animalRepository.findAll();
    }
    public void deleteAll() {
        animalRepository.deleteAll();
    }
    public void initializeAnimalsNormally() {
        List<Animal> animals = new ArrayList<>(Arrays.asList(
            new Animal("Turtle"),
            new Animal("Elephant"),
            new Animal("Lion"),
            new Animal("Hippo"),
            new Animal("Tiger")
        ));
        animalRepository.saveAll(animals);
    }
}

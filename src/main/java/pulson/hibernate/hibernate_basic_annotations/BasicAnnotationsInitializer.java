package pulson.hibernate.hibernate_basic_annotations;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@Component
public class BasicAnnotationsInitializer {

    private final CustomerRepository customerRepository;

    @Autowired
    public BasicAnnotationsInitializer(CustomerRepository customerRepository) throws IOException, ParseException {
        this.customerRepository = customerRepository;

        //initialize();
        //testRemove();
    }

    @Transactional
    void initialize() throws IOException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        InputStream inputStream = BasicAnnotationsInitializer.class.getClassLoader().getResourceAsStream("photo.jpg");
        byte[] photoByte = IOUtils.toByteArray(inputStream);


        Customer customer1 = new Customer(new BigDecimal("111.1111111"),"Michael", "Jordan", "11111", format.parse("1998-07-14"),
                22, Rating.EXCELLENT, photoByte, new Address("USA", "Los Angeles", "Hoover Street", "51"), new ArrayList<>(),
                Set.of("nickname1", "nickname11","nickname111"));

        Order order1 = new Order(1234);
        Order order2 = new Order(453425);
        Order order3 = new Order(199);
        Order order4 = new Order(35555);
        Order order5 = new Order(2455);
        customer1.addOrder(order1);
        customer1.addOrder(order2);
        customer1.addOrder(order3);
        customer1.addOrder(order4);
        customer1.addOrder(order5);


        Customer customer2 = new Customer(new BigDecimal("222.222222"),"Steph", "Curry", "22222", format.parse("2004-02-24"),
                27, Rating.LEGIT, photoByte, new Address("USA", "San Francisco", "San Street", "36"), new ArrayList<>(),
                Set.of("nickname2", "nickname22","nickname222"));
        Order order6 = new Order(8526);
        Order order7 = new Order(5866);
        customer2.addOrder(order6);
        customer2.addOrder(order7);

        Customer customer3 = new Customer(new BigDecimal("333.333333"),"Kevin", "Durant", "33333", format.parse("1987-10-02"),
                99, Rating.GOOD, photoByte, new Address("USA", "Brooklyn", "Brook Street", "78"), new ArrayList<>(),
                Set.of("nickname3", "nickname33","nickname333"));
        Order order8 = new Order(999999999);
        customer3.addOrder(order8);

        customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
    }

    public void testRemove() {

        Customer customerCascadeREMOVEAndOrphan = customerRepository.findById(12L).orElseThrow();
        customerCascadeREMOVEAndOrphan.getOrders().set(0, new Order(10));
        //customerCascadeREMOVEAndOrphan.getOrders().set(0, null);

        //Order orderToRemove = customerCascadeREMOVEAndOrphan.getOrders().get(0);
        //customerCascadeREMOVEAndOrphan.removeOrder(orderToRemove);
        customerRepository.save(customerCascadeREMOVEAndOrphan);
        //podanie obiektu Order znalezionego wcześniej NIE DZIAłA!!!! MUSI być to order wyciągnięty z customera
    }
}

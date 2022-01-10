package pulson.hibernate.mapped_superclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MappedSuperclassInitializer {

    private  final EmployeeRepository employeeRepository;

    @Autowired
    public MappedSuperclassInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;

        //initialize();
    }

    private void initialize() {
        Employee employee1 = new Employee("John", "MediaMarkt");
        Employee employee2 = new Employee("Michael", "Morele.net");
        Employee employee3 = new Employee("Dan", "X-KOM");
        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));
    }
}

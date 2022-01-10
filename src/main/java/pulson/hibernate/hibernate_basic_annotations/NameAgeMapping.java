package pulson.hibernate.hibernate_basic_annotations;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NameAgeMapping {
    private String name;
    private Integer age;

    public NameAgeMapping(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}

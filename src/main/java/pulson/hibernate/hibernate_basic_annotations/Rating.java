package pulson.hibernate.hibernate_basic_annotations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rating {
    BAD(1),
    OK(2),
    GOOD(3),
    LEGIT(4),
    EXCELLENT(5);

    private final Integer numberValue;
}

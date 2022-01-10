package pulson.hibernate.hibernate_basic_annotations;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class RatingConverter implements AttributeConverter<Rating, String> {
    @Override
    public String convertToDatabaseColumn(Rating rating) {
        return switch (rating) {
            case BAD -> "bad";
            case OK -> "ok";
            case GOOD -> "good";
            case LEGIT -> "legit";
            case EXCELLENT -> "excellent";
        };
    }

    @Override
    public Rating convertToEntityAttribute(String string) {
        return switch (string) {
            case "bad" -> Rating.BAD;
            case "ok" -> Rating.OK;
            case "good" -> Rating.GOOD;
            case "legit" -> Rating.LEGIT;
            case "excellent" -> Rating.EXCELLENT;
            default -> throw new IllegalArgumentException("Unknown " + string);
        };
    }
}

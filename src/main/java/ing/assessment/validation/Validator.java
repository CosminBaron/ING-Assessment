package ing.assessment.validation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Validator<T>{

    private final T object;
    private final Set<String> errors = new HashSet<>();

    private Validator(T object){
        if(object == null){
            throw new ValidatorException("Provided object is null");
        }
        this.object = object;
    }

    public static <T> Validator<T> of(T object) {
        return new Validator<>(object);
    }

    public Validator<T> validate (boolean invalid, String message){
        if(invalid){
            errors.add(message);
        }
        return this;
    }

    public T get(){
        if(errors.isEmpty()){
            return object;
        }
        throw new ValidatorException(errors);
    }

}

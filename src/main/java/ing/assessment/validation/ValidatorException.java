package ing.assessment.validation;

import java.util.HashSet;
import java.util.Set;

public class ValidatorException extends RuntimeException{

    private Set<String> errorsCode = new HashSet<>();

    public ValidatorException (String errorCode) {
        this.errorsCode.add(errorCode);
    }

    public ValidatorException (Set<String> errorsCode){
        this.errorsCode = errorsCode;
    }

}

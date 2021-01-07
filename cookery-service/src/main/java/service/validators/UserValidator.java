package service.validators;

import cyclops.control.Validated;
import service.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    public static Validated<Error,String> validName(User user){
        if(user.getName() == null) { // null
            return Validated.invalid(Error.NULL);
        }
        else if(user.getName().length() < 4) { // invalid
            return Validated.invalid(Error.INVALID_PASS);
        }
        else { // valid
            return Validated.valid("Valid name");
        }
    }

    public static Validated<Error,String> validEmail(User user){
        if(user.getEmail() == null) { // null
            return Validated.invalid(Error.NULL);
        }

        String regex = "^[a-zA-Z0-9_!#$%&�*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(user.getEmail());

        if(matcher.matches()) { // valid
            return Validated.valid("email ok");
        }
        else { // invalid
            return Validated.invalid(Error.NO_EMAIL);
        }
    }


    public static Validated<Error,String> validPassword(User user){
        if(user.getPassword() == null) { // null
            return Validated.invalid(Error.NULL);
        }

        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(user.getPassword());

        if(matcher.matches()) { // valid
            return Validated.valid("password ok");
        }
        else { // invalid
            return Validated.invalid(Error.INVALID_PASS);
        }
    }
}

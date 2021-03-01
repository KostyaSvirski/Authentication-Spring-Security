package com.epam.esm.validator.realisation.user;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.validator.realisation.IntermediateUserLink;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidatorLink extends IntermediateUserLink {

    private static final String REGEXP_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    @Override
    public boolean validate(UserDTO bean) {
        Pattern pattern = Pattern.compile(REGEXP_EMAIL);
        Matcher matcher = pattern.matcher(bean.getEmail());
        if(!matcher.matches()) {
            return false;
        }
        return checkNextLink(bean);
    }
}

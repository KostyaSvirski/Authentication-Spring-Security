package com.epam.esm.validator.realisation.user;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.validator.realisation.IntermediateUserLink;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidatorLink extends IntermediateUserLink {

    private static final String REGEXP_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    @Override
    public boolean validate(UserDTO bean) {
        Pattern pattern = Pattern.compile(REGEXP_PASSWORD);
        Matcher matcher = pattern.matcher(bean.getPassword());
        if (!matcher.matches()) {
            return false;
        }
        return checkNextLink(bean);
    }
}

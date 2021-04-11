package com.epam.esm.validator.realisation.user;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.validator.realisation.IntermediateUserLink;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurnameValidatorLink extends IntermediateUserLink {

    private static final String REG_EXP_NAME = "^[A-Za-z]+$";

    @Override
    public boolean validate(UserDTO bean) {
        if (bean.getSurname() != null) {
            Pattern pattern = Pattern.compile(REG_EXP_NAME);
            Matcher matcher = pattern.matcher(bean.getSurname());
            if (!matcher.matches()) {
                return false;
            }
        }
        return checkNextLink(bean);
    }
}

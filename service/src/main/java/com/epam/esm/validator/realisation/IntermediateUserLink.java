package com.epam.esm.validator.realisation;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.validator.PreparedValidatorChain;

public class IntermediateUserLink extends PreparedValidatorChain<UserDTO> {

    @Override
    public boolean validate(UserDTO bean) {
        if (bean.getEmail() == null || bean.getPassword() == null || bean.getPasswordConfirm() == null) {
            return false;
        }
        if (!bean.getPasswordConfirm().equals(bean.getPassword())) {
            return false;
        }
        return checkNextLink(bean);
    }
}

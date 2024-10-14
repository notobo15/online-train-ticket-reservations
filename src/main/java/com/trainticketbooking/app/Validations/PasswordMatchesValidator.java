package com.trainticketbooking.app.Validations;

import com.trainticketbooking.app.Models.ResetPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ResetPassword> {

    @Override
    public boolean isValid(ResetPassword resetPassword, ConstraintValidatorContext context) {
        if (resetPassword.getPassword() == null || resetPassword.getConfirmPassword() == null) {
            return false;
        }

        boolean isValid = resetPassword.getPassword().equals(resetPassword.getConfirmPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
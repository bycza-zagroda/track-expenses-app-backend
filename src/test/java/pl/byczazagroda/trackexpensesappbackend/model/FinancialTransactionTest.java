package pl.byczazagroda.trackexpensesappbackend.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class FinancialTransactionTest {

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("when FT amount is equal to 0.0 or lower it will not validate")
    public void testFTAmount_shouldNotValidateFT_whenAmountIsEqualOrLowerThen0(){
        FinancialTransaction ft = new FinancialTransaction();
        ft.setAmount(new BigDecimal("0.0"));
        Set<ConstraintViolation<FinancialTransaction>> violations = validator.validate(ft);
        Assertions.assertFalse(violations.isEmpty());
    }

}
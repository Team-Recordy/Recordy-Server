package org.recordy.server.exhibition.controller.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ExhibitionCreateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void 각_필드는_비어있거나_null일_경우_Validator에_의해_걸러진다() {
        // given
        ExhibitionCreateRequest request = new ExhibitionCreateRequest(
                "",
                null,
                null,
                false,
                null
        );

        // when
        Set<ConstraintViolation<ExhibitionCreateRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(3);
    }
}
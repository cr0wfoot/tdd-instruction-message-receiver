package com.epam.instruction.message.support;

import com.epam.instruction.message.InstructionMessage;
import com.epam.instruction.message.support.exceptions.InstructionMessageValidationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.fail;

public class InstructionMessageValidatorTest {

    private static final String ERROR_MESSAGE_INSTRUCTION_MESSAGE_NULL = "Instruction message is null";
    private static final String ERROR_MESSAGE_INSTRUCTION_TYPE_INVALID = "Instruction type is not valid";
    private static final String ERROR_MESSAGE_PRODUCT_CODE_INVALID = "Product code is not valid";
    private static final String ERROR_MESSAGE_QUANTITY_INVALID = "Quantity is not valid";
    private static final String ERROR_MESSAGE_UOM_INVALID = "UOM is not valid";
    private static final String ERROR_MESSAGE_TIMESTAMP_INVALID = "Timestamp is not valid";
    public static final String UNEXPECTED_EXCEPTION = "Unexpected exception was thrown";

    private static final String ILLEGAL_INSTRUCTION_TYPE = "-";
    private static final String TOO_LONG_INSTRUCTION_TYPE = "Ab";
    private static final String PATTERN_NOT_MATCH_INSTRUCTION_TYPE = "E";
    private static final String VALID_INSTRUCTION_TYPE = "A";
    private static final String TOO_LONG_PRODUCT_CODE = "AB123";
    private static final String ILLEGAL_PRODUCT_CODE = "AB-D";
    private static final String PATTERN_NOT_MATCH_PRODUCT_CODE = "ab12";
    private static final String VALID_PRODUCT_CODE = "MZ89";

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_UOM = 255;
    private static final int MIN_UOM = 0;
    private static final LocalDateTime UNIX_EPOCH = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
    private static final LocalDateTime AFTER_NOW = LocalDateTime.now().plusDays(1);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private InstructionMessageValidator validator = new InstructionMessageValidator();
    private InstructionMessage instructionMessage;

    @Before
    public void setUp() {
        instructionMessage = createValidInstructionMessage();
    }

    private InstructionMessage createValidInstructionMessage() {
        InstructionMessage instructionMessage = new InstructionMessage();
        instructionMessage.setInstructionType(VALID_INSTRUCTION_TYPE);
        instructionMessage.setProductCode(VALID_PRODUCT_CODE);
        instructionMessage.setQuantity(MIN_QUANTITY);
        instructionMessage.setUom(MIN_UOM);
        instructionMessage.setTimestamp(UNIX_EPOCH.plusDays(1));
        return instructionMessage;
    }

    @Test
    public void shouldThrowExceptionWithMessageIfInstructionMessageIsNull() {
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INSTRUCTION_MESSAGE_NULL);

        validator.validate(null);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfInstructionTypeIsNull() {
        instructionMessage.setInstructionType(null);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INSTRUCTION_TYPE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfInstructionTypeHasIllegalCharacters() {
        instructionMessage.setInstructionType(ILLEGAL_INSTRUCTION_TYPE);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INSTRUCTION_TYPE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfInstructionTypeHasIllegalLength() {
        instructionMessage.setInstructionType(TOO_LONG_INSTRUCTION_TYPE);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INSTRUCTION_TYPE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfInstructionTypeIsOutOfRange() {
        instructionMessage.setInstructionType(PATTERN_NOT_MATCH_INSTRUCTION_TYPE);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INSTRUCTION_TYPE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfProductCodeIsNull() {
        instructionMessage.setProductCode(null);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_PRODUCT_CODE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfProductCodeHasIllegalLength() {
        instructionMessage.setProductCode(TOO_LONG_PRODUCT_CODE);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_PRODUCT_CODE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfProductCodeHasIllegalCharacters() {
        instructionMessage.setProductCode(ILLEGAL_PRODUCT_CODE);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_PRODUCT_CODE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfProductCodeIsOutOfRange() {
        instructionMessage.setProductCode(PATTERN_NOT_MATCH_PRODUCT_CODE);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_PRODUCT_CODE_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfQuantityBelowMinimum() {
        instructionMessage.setQuantity(MIN_QUANTITY - 1);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_QUANTITY_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfUomBelowMinimum() {
        instructionMessage.setUom(MIN_UOM - 1);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_UOM_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfUomAboveMaximum() {
        instructionMessage.setUom(MAX_UOM + 1);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_UOM_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfTimestampIsNull() {
        instructionMessage.setTimestamp(null);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_TIMESTAMP_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfTimestampIsAfterNow() {
        instructionMessage.setTimestamp(AFTER_NOW);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_TIMESTAMP_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfTimestampEqualsUnixEpoch() {
        instructionMessage.setTimestamp(UNIX_EPOCH);
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(ERROR_MESSAGE_TIMESTAMP_INVALID);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldThrowExceptionWithMessagesIfSeveralFieldsInvalid() {
        instructionMessage.setTimestamp(UNIX_EPOCH);
        instructionMessage.setUom(MAX_UOM + 1);
        instructionMessage.setProductCode(null);
        String expectedMessage = ERROR_MESSAGE_PRODUCT_CODE_INVALID + System.lineSeparator() +
                ERROR_MESSAGE_UOM_INVALID + System.lineSeparator() + ERROR_MESSAGE_TIMESTAMP_INVALID;
        expectedException.expect(InstructionMessageValidationException.class);
        expectedException.expectMessage(expectedMessage);

        validator.validate(instructionMessage);
    }

    @Test
    public void shouldNotThrowExceptionIfInstructionMessageIsValid() {
        try {
            validator.validate(instructionMessage);
        } catch (InstructionMessageValidationException e) {
            fail(UNEXPECTED_EXCEPTION);
        }
    }

}
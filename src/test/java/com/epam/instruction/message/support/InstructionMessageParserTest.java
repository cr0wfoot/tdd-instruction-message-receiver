package com.epam.instruction.message.support;

import com.epam.instruction.message.InstructionMessage;
import com.epam.instruction.message.support.exceptions.InstructionMessageParsingException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InstructionMessageParserTest {

    private static final String ERROR_MESSAGE_INPUT_IS_EMPTY = "The message is empty";
    private static final String ERROR_MESSAGE_INCORRECT_NUMBER_OF_ARGUMENTS = "Incorrect number of arguments in message";
    private static final String ERROR_MESSAGE_HEADER_IS_INVALID = "Message header is missing or invalid";

    private static final String MESSAGE_WITH_NUMBER_OF_ELEMENTS_LESS_THEN_REQUIRED = "0";
    private static final String MESSAGE_WITH_NUMBER_OF_ELEMENTS_MORE_THEN_REQUIRED = "0 1 2 3 4 5 6";
    private static final String MESSAGE_WITH_INCORRECT_HEADER = "0 1 2 3 4 5";
    private static final String MESSAGE_WITH_THIRD_ELEMENT_NOT_INTEGER = "InstructionMessage 1 2 a 4 5";
    private static final String MESSAGE_WITH_FOURTH_ELEMENT_NOT_INTEGER = "InstructionMessage 1 2 3 a 5";
    private static final String MESSAGE_WITH_LAST_ELEMENT_NOT_DATE_IN_FORMAT = "InstructionMessage 1 2 3 4 a";

    private static final String CORRECT_HEADER = "InstructionMessage";
    private static final String CORRECT_INSTRUCTION_TYPE = "1";
    private static final String CORRECT_PRODUCT_CODE = "2";
    private static final int CORRECT_QUANTITY = 3;
    private static final int CORRECT_UOM = 4;
    private static final String CORRECT_DATE = "2015-03-05T10:04:56.012Z";
    private static final LocalDateTime CORRECT_TIMESTAMP = LocalDateTime.parse(CORRECT_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    private static final String MESSAGE_ARGUMENTS_DELIMITER = " ";
    private static final String CORRECT_INPUT_MESSAGE = CORRECT_HEADER + MESSAGE_ARGUMENTS_DELIMITER + CORRECT_INSTRUCTION_TYPE +
            MESSAGE_ARGUMENTS_DELIMITER + CORRECT_PRODUCT_CODE + MESSAGE_ARGUMENTS_DELIMITER + CORRECT_QUANTITY +
            MESSAGE_ARGUMENTS_DELIMITER + CORRECT_UOM + MESSAGE_ARGUMENTS_DELIMITER + CORRECT_DATE;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private InstructionMessageParser parser = new InstructionMessageParser();

    @Test
    public void shouldThrowExceptionWithMessageIfInputMessageIsNull() {
        expectedException.expect(InstructionMessageParsingException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INPUT_IS_EMPTY);

        parser.parse(null);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfInputMessageIsEmpty() {
        expectedException.expect(InstructionMessageParsingException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INPUT_IS_EMPTY);

        parser.parse(StringUtils.EMPTY);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfNumberOfArgumentsLessThanRequired() {
        expectedException.expect(InstructionMessageParsingException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INCORRECT_NUMBER_OF_ARGUMENTS);

        parser.parse(MESSAGE_WITH_NUMBER_OF_ELEMENTS_LESS_THEN_REQUIRED);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfNumberOfArgumentsMoreThanRequired() {
        expectedException.expect(InstructionMessageParsingException.class);
        expectedException.expectMessage(ERROR_MESSAGE_INCORRECT_NUMBER_OF_ARGUMENTS);

        parser.parse(MESSAGE_WITH_NUMBER_OF_ELEMENTS_MORE_THEN_REQUIRED);
    }

    @Test
    public void shouldThrowExceptionWithMessageIfHeaderIsIncorrect() {
        expectedException.expect(InstructionMessageParsingException.class);
        expectedException.expectMessage(ERROR_MESSAGE_HEADER_IS_INVALID);

        parser.parse(MESSAGE_WITH_INCORRECT_HEADER);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowExceptionIfQuantityNotInteger() {
        parser.parse(MESSAGE_WITH_THIRD_ELEMENT_NOT_INTEGER);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowExceptionIfUomNotInteger() {
        parser.parse(MESSAGE_WITH_FOURTH_ELEMENT_NOT_INTEGER);
    }

    @Test(expected = DateTimeParseException.class)
    public void shouldThrowExceptionIfTimestampNotOfRequiredFormat() {
        parser.parse(MESSAGE_WITH_LAST_ELEMENT_NOT_DATE_IN_FORMAT);
    }

    @Test
    public void shouldReturnNotNullObjectWithFilledFieldsIfMessageIsCorrect() {
        InstructionMessage result = parser.parse(CORRECT_INPUT_MESSAGE);

        assertNotNull(result);
        assertEquals(CORRECT_INSTRUCTION_TYPE, result.getInstructionType());
        assertEquals(CORRECT_PRODUCT_CODE, result.getProductCode());
        assertEquals(CORRECT_QUANTITY, result.getQuantity());
        assertEquals(CORRECT_UOM, result.getUom());
        assertEquals(CORRECT_TIMESTAMP, result.getTimestamp());
    }

}
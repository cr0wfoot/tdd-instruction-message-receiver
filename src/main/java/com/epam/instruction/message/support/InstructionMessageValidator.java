package com.epam.instruction.message.support;

import com.epam.instruction.message.InstructionMessage;
import com.epam.instruction.message.support.exceptions.InstructionMessageValidationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InstructionMessageValidator {

    private static final String INSTRUCTION_TYPE_REGEX = "[A-D]";
    private static final String PRODUCT_CODE_REGEX = "[A-Z]{2}[0-9]{2}";
    private static final int QUANTITY_MIN_VALUE = 0;
    private static final int UOM_MIN_VALUE = 0;
    private static final int UOM_MAX_VALUE = 256;
    private static final LocalDateTime UNIX_EPOCH = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());

    private static final String ERROR_MESSAGES_DELIMITER = System.lineSeparator();
    private static final String ERROR_MESSAGE_INSTRUCTION_MESSAGE_NULL = "Instruction message is null";
    private static final String ERROR_MESSAGE_INSTRUCTION_TYPE_INVALID = "Instruction type is not valid";
    private static final String ERROR_MESSAGE_PRODUCT_CODE_INVALID = "Product code is not valid";
    private static final String ERROR_MESSAGE_QUANTITY_INVALID = "Quantity is not valid";
    private static final String ERROR_MESSAGE_UOM_INVALID = "UOM is not valid";
    private static final String ERROR_MESSAGE_TIMESTAMP_INVALID = "Timestamp is not valid";

    public void validate(InstructionMessage instructionMessage) {
        StringBuilder errors = new StringBuilder();
        if (instructionMessage == null) {
            addErrorMessage(errors, ERROR_MESSAGE_INSTRUCTION_MESSAGE_NULL);
        } else {
            validateInstructionMessageFields(instructionMessage, errors);
        }
        handleErrorsIfPresent(errors);
    }

    private void addErrorMessage(StringBuilder errors, String newErrorMessage) {
        errors.append(ERROR_MESSAGES_DELIMITER);
        errors.append(newErrorMessage);
    }

    private void validateInstructionMessageFields(InstructionMessage instructionMessage, StringBuilder errors) {
        validateInstructionType(instructionMessage.getInstructionType(), errors);
        validateProductCode(instructionMessage.getProductCode(), errors);
        validateQuantity(instructionMessage.getQuantity(), errors);
        validateUom(instructionMessage.getUom(), errors);
        validateTimestamp(instructionMessage.getTimestamp(), errors);
    }

    private void handleErrorsIfPresent(StringBuilder errors) {
        if (errors.length() != 0) {
            throw new InstructionMessageValidationException(errors.toString());
        }
    }

    private void validateInstructionType(String instructionType, StringBuilder errors) {
        if (instructionType == null || !instructionType.matches(INSTRUCTION_TYPE_REGEX)) {
            addErrorMessage(errors, ERROR_MESSAGE_INSTRUCTION_TYPE_INVALID);
        }
    }

    private void validateProductCode(String productCode, StringBuilder errors) {
        if (productCode == null || !productCode.matches(PRODUCT_CODE_REGEX)) {
            addErrorMessage(errors, ERROR_MESSAGE_PRODUCT_CODE_INVALID);
        }
    }

    private void validateQuantity(int quantity, StringBuilder errors) {
        if (quantity <= QUANTITY_MIN_VALUE) {
            addErrorMessage(errors, ERROR_MESSAGE_QUANTITY_INVALID);
        }
    }

    private void validateUom(int uom, StringBuilder errors) {
        if (uom < UOM_MIN_VALUE || uom >= UOM_MAX_VALUE) {
            addErrorMessage(errors, ERROR_MESSAGE_UOM_INVALID);
        }
    }

    private void validateTimestamp(LocalDateTime timestamp, StringBuilder errors) {
        if (timestamp == null || !timestamp.isAfter(UNIX_EPOCH) || timestamp.isAfter(LocalDateTime.now())) {
            addErrorMessage(errors, ERROR_MESSAGE_TIMESTAMP_INVALID);
        }
    }

}
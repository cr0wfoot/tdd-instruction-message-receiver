package com.epam.instruction.message.support;

import com.epam.instruction.message.InstructionMessage;
import com.epam.instruction.message.support.exceptions.InstructionMessageParsingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InstructionMessageParser {

    private static final String INPUT_MESSAGE_ARGUMENTS_DELIMITER = " ";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String MESSAGE_HEADER = "InstructionMessage";
    private static final int REQUIRED_NUMBER_OF_ARGUMENTS = 6;
    private static final int MESSAGE_HEADER_INDEX = 0;
    private static final int INSTRUCTION_TYPE_INDEX = 1;
    private static final int PRODUCT_CODE_INDEX = 2;
    private static final int QUANTITY_INDEX = 3;
    private static final int UOM_INDEX = 4;
    private static final int TIMESTAMP_INDEX = 5;

    public InstructionMessage parse(String message) {
        checkEmptyMessage(message);
        String[] arguments = message.split(INPUT_MESSAGE_ARGUMENTS_DELIMITER);
        checkNumberOfArgumentsInMessage(arguments.length);
        String messageHeader = arguments[MESSAGE_HEADER_INDEX];
        checkHeaderOfMessage(messageHeader);
        return createInstructionMessage(arguments);
    }

    private void checkEmptyMessage(String message) {
        if (message == null || message.isEmpty()) {
            throw new InstructionMessageParsingException("The message is empty");
        }
    }

    private void checkNumberOfArgumentsInMessage(int numberOfArguments) {
        if (numberOfArguments != REQUIRED_NUMBER_OF_ARGUMENTS) {
            throw new InstructionMessageParsingException("Incorrect number of arguments in message");
        }
    }

    private void checkHeaderOfMessage(String argument) {
        if (!MESSAGE_HEADER.equals(argument)) {
            throw new InstructionMessageParsingException("Message header is missing or invalid");
        }
    }

    private InstructionMessage createInstructionMessage(String[] arguments) {
        InstructionMessage instructionMessage = new InstructionMessage();
        instructionMessage.setInstructionType(arguments[INSTRUCTION_TYPE_INDEX]);
        instructionMessage.setProductCode(arguments[PRODUCT_CODE_INDEX]);
        instructionMessage.setQuantity(Integer.parseInt(arguments[QUANTITY_INDEX]));
        instructionMessage.setUom(Integer.parseInt(arguments[UOM_INDEX]));
        instructionMessage.setTimestamp(LocalDateTime.parse(arguments[TIMESTAMP_INDEX], DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)));
        return instructionMessage;
    }

}
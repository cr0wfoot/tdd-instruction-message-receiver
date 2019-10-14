package com.epam.instruction.message.receiver;

import com.epam.instruction.message.storage.InstructionQueue;
import com.epam.instruction.message.support.InstructionMessageParser;
import com.epam.instruction.message.support.InstructionMessageValidator;
import com.epam.instruction.message.support.exceptions.InstructionMessageParsingException;
import com.epam.instruction.message.support.exceptions.InstructionMessageValidationException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InstructionMessageReceiverTest {

    private InstructionMessageParser parser;
    private InstructionMessageValidator validator;
    private InstructionQueue queue;
    private MessageReceiver receiver;

    private static final String INCORRECT_INSTRUCTION_MESSAGE = "InstructionMessage A MZ89 5678 50";
    private static final String INVALID_INSTRUCTION_MESSAGE = "InstructionMessage A B 5678 50 2015-03-05T10:04:56.012Z";
    private static final String VALID_INSTRUCTION_MESSAGE = "InstructionMessage A MZ89 5678 50 2015-03-05T10:04:56.012Z";
    private static final String EXCEPTION_WAS_NOT_THROWN = "Expected exception was not thrown";

    @Before
    public void setUp() {
        this.parser = new InstructionMessageParser();
        this.validator = new InstructionMessageValidator();
        this.queue = new InstructionQueue();
        this.receiver = new InstructionMessageReceiver(parser, validator, queue);
    }

    @Test(expected = InstructionMessageParsingException.class)
    public void shouldThrowParsingExceptionIfInputMessageIsIncorrect() {
        receiver.receive(INCORRECT_INSTRUCTION_MESSAGE);
    }

    @Test(expected = InstructionMessageValidationException.class)
    public void shouldThrowValidationExceptionIfInputMessageIsInvalid() {
        receiver.receive(INVALID_INSTRUCTION_MESSAGE);
    }

    @Test
    public void shouldAddElementToTheStorageIfInputIsCorrectAndValid() {
        int sizeBeforeReceive = queue.count();

        receiver.receive(VALID_INSTRUCTION_MESSAGE);
        int result = queue.count();

        assertEquals(sizeBeforeReceive + 1, result);
    }

    @Test
    public void shouldNotAddElementToTheStorageIfInputIsIncorrect() {
        int sizeBeforeReceive = queue.count();

        try {
            receiver.receive(INCORRECT_INSTRUCTION_MESSAGE);
            fail(EXCEPTION_WAS_NOT_THROWN);
        } catch (InstructionMessageParsingException e) {
            assertEquals(sizeBeforeReceive, queue.count());
        }
    }

    @Test
    public void shouldNotAddElementToTheStorageIfInputIsInvalid() {
        int sizeBeforeReceive = queue.count();

        try {
            receiver.receive(INVALID_INSTRUCTION_MESSAGE);
            fail(EXCEPTION_WAS_NOT_THROWN);
        } catch (InstructionMessageValidationException e) {
            assertEquals(sizeBeforeReceive, queue.count());
        }
    }

}
package com.epam.instruction.message.receiver;

import com.epam.instruction.message.InstructionMessage;
import com.epam.instruction.message.storage.InstructionQueue;
import com.epam.instruction.message.support.InstructionMessageParser;
import com.epam.instruction.message.support.InstructionMessageValidator;

public class InstructionMessageReceiver implements MessageReceiver {

    private InstructionMessageParser parser;
    private InstructionMessageValidator validator;
    private InstructionQueue queue;

    public InstructionMessageReceiver() {
        this(new InstructionMessageParser(), new InstructionMessageValidator(), new InstructionQueue());
    }

    public InstructionMessageReceiver(InstructionMessageParser parser, InstructionMessageValidator validator, InstructionQueue queue) {
        this.parser = parser;
        this.validator = validator;
        this.queue = queue;
    }

    @Override
    public void receive(String message) {
        InstructionMessage instructionMessage = parser.parse(message);
        validator.validate(instructionMessage);
        queue.enqueue(instructionMessage);
    }

}
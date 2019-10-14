package com.epam.instruction.message.storage;

import com.epam.instruction.message.InstructionMessage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InstructionQueueTest {

    private static final int ZERO_SIZE = 0;
    private static final String INSTRUCTION_TYPE_A = "A";
    private static final String INSTRUCTION_TYPE_B = "B";
    private static final String INSTRUCTION_TYPE_C = "C";
    private static final String INSTRUCTION_TYPE_D = "D";

    private InstructionQueue queue = new InstructionQueue();

    private InstructionMessage messageA;
    private InstructionMessage messageB;
    private InstructionMessage messageB2;
    private InstructionMessage messageB3;
    private InstructionMessage messageC;
    private InstructionMessage messageC2;
    private InstructionMessage messageC3;
    private InstructionMessage messageD;

    @Before
    public void setUp() {
        messageA = createInstructionMessageWithType(INSTRUCTION_TYPE_A);
        messageB = createInstructionMessageWithType(INSTRUCTION_TYPE_B);
        messageB2 = createInstructionMessageWithType(INSTRUCTION_TYPE_B);
        messageB3 = createInstructionMessageWithType(INSTRUCTION_TYPE_B);
        messageC = createInstructionMessageWithType(INSTRUCTION_TYPE_C);
        messageC2 = createInstructionMessageWithType(INSTRUCTION_TYPE_C);
        messageC3 = createInstructionMessageWithType(INSTRUCTION_TYPE_C);
        messageD = createInstructionMessageWithType(INSTRUCTION_TYPE_D);
    }

    private InstructionMessage createInstructionMessageWithType(String type) {
        InstructionMessage instructionMessage = new InstructionMessage();
        instructionMessage.setInstructionType(type);
        return instructionMessage;
    }

    @Test
    public void shouldReturnZeroSizeIfQueueIsEmpty() {
        assertEquals(ZERO_SIZE, queue.count());
    }

    @Test
    public void shouldReturnTrueIfQueueIsEmpty() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void shouldReturnFalseIfQueueNotEmpty() {
        queue.enqueue(messageA);

        assertFalse(queue.isEmpty());
    }

    @Test
    public void shouldNotEnqueueNull() {
        queue.enqueue(null);

        assertTrue(queue.isEmpty());
    }

    @Test
    public void shouldIncreaseSizeByOneIfElementEnqueued() {
        int sizeBeforeEnqueue = queue.count();

        queue.enqueue(messageA);

        assertEquals(sizeBeforeEnqueue + 1, queue.count());
    }

    @Test
    public void shouldReturnNullIfPeekElementFromEmptyQueue() {
        assertNull(queue.peek());
    }

    @Test
    public void shouldPeekElementEnqueuedToEmptyQueue() {
        queue.enqueue(messageA);

        InstructionMessage result = queue.peek();

        assertEquals(messageA, result);
    }

    @Test
    public void shouldReturnNullIfDequeueEmptyQueue() {
        assertNull(queue.dequeue());
    }

    @Test
    public void shouldDequeueElementEnqueuedToEmptyQueue() {
        queue.enqueue(messageA);

        InstructionMessage result = queue.dequeue();

        assertEquals(messageA, result);
    }

    @Test
    public void shouldRemoveElementIfDequeueNotEmptyQueue() {
        queue.enqueue(messageA);
        int sizeBeforeDequeue = queue.count();

        queue.dequeue();

        assertEquals(sizeBeforeDequeue - 1, queue.count());
    }

    @Test
    public void shouldDequeueElementsAccordingToPriority() {
        queue.enqueue(messageB);
        queue.enqueue(messageA);
        queue.enqueue(messageC);

        assertEquals(messageA, queue.dequeue());
        assertEquals(messageB, queue.dequeue());
        assertEquals(messageC, queue.dequeue());
    }

    @Test
    public void shouldDequeueElementsInFifoOrderIfTheyHaveEqualPriority() {
        queue.enqueue(messageD);
        queue.enqueue(messageC);

        assertEquals(messageD, queue.dequeue());
        assertEquals(messageC, queue.dequeue());
    }

    @Test
    public void shouldDequeueElementsInFifoOrderIfTheyHaveEqualInstructionTypes() {
        queue.enqueue(messageB);
        queue.enqueue(messageB2);
        queue.enqueue(messageB3);

        assertEquals(messageB, queue.dequeue());
        assertEquals(messageB2, queue.dequeue());
        assertEquals(messageB3, queue.dequeue());
    }

    @Test
    public void shouldReturnElementsInFifoOrderAfterDequeueAndEnqueue() {
        queue.enqueue(messageC);
        queue.enqueue(messageC2);
        queue.dequeue();
        queue.enqueue(messageC3);

        assertEquals(messageC2, queue.dequeue());
        assertEquals(messageC3, queue.dequeue());
    }

}
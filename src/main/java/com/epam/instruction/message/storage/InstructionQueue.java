package com.epam.instruction.message.storage;

import com.epam.instruction.message.InstructionMessage;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.PriorityQueue;

public class InstructionQueue {

    private PriorityQueue<Pair<Long, InstructionMessage>> queue;
    private long lastInsertedElementNumber = 0;

    public InstructionQueue() {
        this.queue = new PriorityQueue<>(new InstructionMessageComparatorByType());
    }

    public int count() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void enqueue(InstructionMessage message) {
        if (message != null) {
            lastInsertedElementNumber++;
            queue.add(Pair.of(lastInsertedElementNumber, message));
        }
    }

    public InstructionMessage peek() {
        InstructionMessage instructionMessage = null;
        if (!isEmpty()) {
            instructionMessage = queue.peek().getValue();
        }
        return instructionMessage;
    }

    public InstructionMessage dequeue() {
        InstructionMessage instructionMessage = null;
        if (!isEmpty()) {
            instructionMessage = queue.poll().getValue();
        }
        return instructionMessage;
    }

    private static class InstructionMessageComparatorByType implements Comparator<Pair<Long, InstructionMessage>> {

        private enum InstructionType {

            A(Priority.HIGH),
            B(Priority.MEDIUM),
            C(Priority.LOW),
            D(Priority.LOW);

            private Priority priority;

            InstructionType(Priority priority) {
                this.priority = priority;
            }

            int compareToByPriority(InstructionType that) {
                return that.priority.level.compareTo(this.priority.level);
            }

        }

        private enum Priority {

            HIGH(3),
            MEDIUM(2),
            LOW(1);

            private Integer level;

            Priority(Integer level) {
                this.level = level;
            }

        }

        @Override
        public int compare(Pair<Long, InstructionMessage> firstPair, Pair<Long, InstructionMessage> secondPair) {
            int compareResult = compareByInstructionType(firstPair, secondPair);
            if (compareResult == 0) {
                compareResult = compareByLastInsertion(firstPair, secondPair);
            }
            return compareResult;
        }

        private int compareByInstructionType(Pair<Long, InstructionMessage> firstPair, Pair<Long, InstructionMessage> secondPair) {
            String firstMessageType = firstPair.getValue().getInstructionType();
            String secondMessageType = secondPair.getValue().getInstructionType();
            return InstructionType.valueOf(firstMessageType).compareToByPriority(InstructionType.valueOf(secondMessageType));
        }

        private int compareByLastInsertion(Pair<Long, InstructionMessage> firstPair, Pair<Long, InstructionMessage> secondPair) {
            return firstPair.getKey().compareTo(secondPair.getKey());
        }

    }

}
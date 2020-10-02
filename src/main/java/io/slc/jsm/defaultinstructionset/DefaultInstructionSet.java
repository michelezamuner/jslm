package io.slc.jsm.defaultinstructionset;

import java.util.List;

import io.slc.jsm.defaultinterpreter.InstructionSet;
import io.slc.jsm.defaultinterpreter.Instruction;
import io.slc.jsm.defaultinterpreter.InvalidInstructionException;

public class DefaultInstructionSet implements InstructionSet
{
    private final InstructionFactory factory;

    public DefaultInstructionSet(final InstructionFactory factory)
    {
        this.factory = factory;
    }

    public Instruction get(final List<Integer> instructionData)
        throws InvalidInstructionException
    {
        final int opcode = instructionData.get(0);
        final List<Integer> operands = instructionData.subList(1, instructionData.size());

        try {
            return factory.create(opcode, operands);
        } catch (IllegalArgumentException e) {
            throw new InvalidInstructionException(instructionData, e);
        }
    }
}
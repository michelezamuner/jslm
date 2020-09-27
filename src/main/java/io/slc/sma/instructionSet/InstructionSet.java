package io.slc.sma.instructionset;

import java.util.List;

import io.slc.sma.Instruction;

public class InstructionSet
{
    private final InstructionFactory factory;

    public InstructionSet(final InstructionFactory factory)
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

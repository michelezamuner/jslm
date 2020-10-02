package io.slc.jsm.defaultinterpreter;

import java.util.List;

public interface InstructionSet
{
    Instruction get(final List<Integer> instructionData)
        throws InvalidInstructionException;
}
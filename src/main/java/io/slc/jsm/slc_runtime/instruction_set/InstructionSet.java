package io.slc.jsm.slc_runtime.instruction_set;

public interface InstructionSet
{
    Instruction get(final int opcode)
        throws InvalidInstructionException;
}

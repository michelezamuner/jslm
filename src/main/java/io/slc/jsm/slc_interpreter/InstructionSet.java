package io.slc.jsm.slc_interpreter;

public interface InstructionSet<R extends Runtime>
{
    int getInstructionSize();

    Instruction<R> get(final int opcode)
        throws InvalidInstructionException;
}

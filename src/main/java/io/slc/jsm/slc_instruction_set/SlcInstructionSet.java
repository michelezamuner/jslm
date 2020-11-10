package io.slc.jsm.slc_instruction_set;

import io.slc.jsm.slc_interpreter.InstructionSet;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_interpreter.Instruction;
import io.slc.jsm.slc_interpreter.InvalidInstructionException;

public class SlcInstructionSet implements InstructionSet<SlcRuntime>
{
    private static final int INSTRUCTION_SIZE = 4;

    public int getInstructionSize()
    {
        return INSTRUCTION_SIZE;
    }

    public Instruction<SlcRuntime> get(final int opcode)
        throws InvalidInstructionException
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

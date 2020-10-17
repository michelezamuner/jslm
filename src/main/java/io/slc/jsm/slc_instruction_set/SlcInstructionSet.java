package io.slc.jsm.slc_instruction_set;

import io.slc.jsm.slc_runtime.instruction_set.InstructionSet;
import io.slc.jsm.slc_runtime.instruction_set.Instruction;
import io.slc.jsm.slc_runtime.instruction_set.InvalidInstructionException;

class SlcInstructionSet implements InstructionSet
{
    public Instruction get(final int opcode)
        throws InvalidInstructionException
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

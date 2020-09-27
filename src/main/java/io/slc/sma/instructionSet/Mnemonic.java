package io.slc.sma.instructionset;

import io.slc.sma.Instruction;
import io.slc.sma.instructionset.instructions.Syscall;

public enum Mnemonic
{
    SYSCALL (0xF0, Syscall.class);

    private final int opcode;
    private final Class<? extends Instruction> instructionClass;

    Mnemonic(final int opcode, final Class<? extends Instruction> instructionClass)
    {
        this.opcode = opcode;
        this.instructionClass = instructionClass;
    }

    public int getOpcode()
    {
        return this.opcode;
    }

    public Class<? extends Instruction> getInstructionClass()
    {
        return this.instructionClass;
    }
}

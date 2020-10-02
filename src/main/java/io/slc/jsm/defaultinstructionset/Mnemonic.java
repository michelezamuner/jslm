package io.slc.jsm.defaultinstructionset;

import io.slc.jsm.defaultinterpreter.Instruction;
import io.slc.jsm.defaultinstructionset.instructions.Syscall;

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

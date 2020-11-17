package io.slc.jsm.slc_instruction_set;

import java.util.HashMap;
import java.util.Map;

import io.slc.jsm.slc_instruction_set.instructions.Movi;
import io.slc.jsm.slc_instruction_set.instructions.Syscall;
import io.slc.jsm.slc_interpreter.InvalidInstructionException;

class InstructionSelector {
    @SuppressWarnings("serial")
    private static final Map<Integer, Class<? extends SlcInstruction>> instructions = new HashMap<Integer, Class<? extends SlcInstruction>>() {{
        put(Mnemonic.MOVI, Movi.class);
        put(Mnemonic.SYSCALL, Syscall.class);
    }};

    public Class<? extends SlcInstruction> select(final int opcode)
        throws InvalidInstructionException
    {
        final Class<? extends SlcInstruction> instructionClass = instructions.get(opcode);
        if (instructionClass == null) {
            throw new InvalidInstructionException("Invalid instruction opcode: " + opcode);
        }

        return instructionClass;
    }
}

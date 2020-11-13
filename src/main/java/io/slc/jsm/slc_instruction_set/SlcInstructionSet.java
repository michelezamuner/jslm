package io.slc.jsm.slc_instruction_set;

import io.slc.jsm.slc_interpreter.InstructionSet;
import io.slc.jsm.slc_runtime.SlcRuntime;

import io.slc.jsm.slc_interpreter.Instruction;
import io.slc.jsm.slc_interpreter.InvalidInstructionException;

public class SlcInstructionSet implements InstructionSet<SlcRuntime>
{
    private static final int INSTRUCTION_SIZE = 4;

    private final InstructionSelector selector = new InstructionSelector();

    @Override
    public int getInstructionSize()
    {
        return INSTRUCTION_SIZE;
    }

    @Override
    public Instruction<SlcRuntime> get(final int opcode)
        throws InvalidInstructionException
    {
        final Class<? extends SlcInstruction> instructionClass = selector.select(opcode);

        try {
            return instructionClass.newInstance();
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException("Error instantiating instruction", e);
        }
    }
}

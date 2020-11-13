package io.slc.jsm.slc_instruction_set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import io.slc.jsm.slc_interpreter.InvalidInstructionException;

import io.slc.jsm.slc_instruction_set.instructions.Syscall;

@SuppressWarnings("initialization")
public class InstructionSelectorTest
{
    private final InstructionSelector selector = new InstructionSelector();

    @Test
    public void selectsInstructionFromOpcode()
        throws InvalidInstructionException
    {
        final int opcode = Mnemonic.SYSCALL;

        final Class<? extends SlcInstruction> instructionClass = selector.select(opcode);
        assertEquals(Syscall.class, instructionClass);
    }

    @Test
    public void failsWhenSelectingInvalidInstruction()
        throws InvalidInstructionException
    {
        final int invalidOpcode = -1;

        final InvalidInstructionException exception = assertThrows(InvalidInstructionException.class, () -> {
            selector.select(invalidOpcode);
        });
        assertEquals("Invalid instruction opcode: " + invalidOpcode, exception.getMessage());
    }
}

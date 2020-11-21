package io.slc.jsm.slc_instruction_set.instructions.syscall;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

import io.slc.jsm.slc_instruction_set.SlcInstruction;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.Registers;
import io.slc.jsm.slc_runtime.SlcRuntime;

import org.mockito.Mock;

@SuppressWarnings("initialization")
@ExtendWith(MockitoExtension.class)
public class SelectorTest
{
    private final Selector selector = new Selector();
    @Mock private SlcRuntime runtime;
    @Mock private Registers registers;

    @BeforeEach
    public void setUp()
    {
        when(runtime.getRegisters()).thenReturn(registers);
    }

    @Test
    public void producesSyscallInstructionFromEAXRegister()
        throws InstructionExecutionException
    {
        when(registers.read(Register.EAX)).thenReturn(new int[]{0, 0, 0, Type.EXIT});

        final Class<? extends SlcInstruction> syscallClass = selector.select(runtime);
        assertEquals(Exit.class, syscallClass);
    }

    @Test
    public void failsWhenRequestingInvalidSyscallCode()
    {
        final int invalidSyscallCode = 0xff;
        when(registers.read(Register.EAX)).thenReturn(new int[]{0, 0, 0, invalidSyscallCode});

        final InstructionExecutionException exception = assertThrows(InstructionExecutionException.class, () -> {
            selector.select(runtime);
        });
        assertEquals("Invalid syscall code: " + invalidSyscallCode, exception.getMessage());
    }
}

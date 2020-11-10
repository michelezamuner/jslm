package io.slc.jsm.slc_instruction_set.instructions.syscall;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.SlcRuntime;

import org.mockito.Mock;

@SuppressWarnings({"initialization"})
@ExtendWith(MockitoExtension.class)
public class SyscallMapTest
{
    private SyscallMap map = new SyscallMap();

    @Mock private SlcRuntime runtime;

    @Test
    public void producesSyscallInstructionFromEAXRegister()
        throws InstructionExecutionException
    {
        when(runtime.readRegister(Register.EAX)).thenReturn(SyscallMap.SYSCALL_EXIT_CODE);

        assertEquals(SyscallExit.class, map.get(runtime));
    }

    @Test
    public void failsWhenRequestingInvalidSyscallCode()
    {
        final int invalidSyscallCode = -1;

        when(runtime.readRegister(Register.EAX)).thenReturn(invalidSyscallCode);

        final InstructionExecutionException exception = assertThrows(InstructionExecutionException.class, () -> {
            map.get(runtime);
        });
        assertEquals("Invalid syscall code: " + invalidSyscallCode, exception.getMessage());
    }
}

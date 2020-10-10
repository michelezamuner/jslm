package io.slc.jsm.slc_instructions.syscall;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import io.slc.jsm.slc_runtime.VirtualMachine;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.InstructionExecutionException;

@SuppressWarnings({"initialization"})
@ExtendWith(MockitoExtension.class)
public class SyscallMapTest
{
    private SyscallMap map = new SyscallMap();

    @Mock private VirtualMachine vm;

    @Test
    public void supportsSyscallExit()
        throws InstructionExecutionException
    {
        when(vm.readRegister(Register.EAX)).thenReturn(SyscallMap.SYSCALL_EXIT_CODE);

        assertEquals(SyscallExit.class, map.get(vm));
    }

    @Test
    public void failsWhenRequestingInvalidSyscallCode()
    {
        final int invalidSyscallCode = -1;

        when(vm.readRegister(Register.EAX)).thenReturn(invalidSyscallCode);

        final InstructionExecutionException exception = assertThrows(InstructionExecutionException.class, () -> {
            map.get(vm);
        });
        assertEquals("Invalid syscall code: " + invalidSyscallCode, exception.getMessage());
    }
}
package io.slc.jsm.slc_instruction_set.instructions.syscall;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.Registers;
import io.slc.jsm.slc_runtime.RegistersException;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_instruction_set.SlcInstruction;
import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;

@SuppressWarnings("initialization")
@ExtendWith(MockitoExtension.class)
public class SyscallExitTest
{
    private SyscallExit instruction = new SyscallExit();
    @Mock private SlcRuntime runtime;
    @Mock private Registers registers;

    @BeforeEach
    public void setUp()
    {
        lenient().when(runtime.getRegisters()).thenReturn(registers);
    }

    @Test
    public void isInstruction()
    {
        assertTrue(instruction instanceof SlcInstruction);
    }

    @Test
    public void producesResultWithExitRequest()
        throws InstructionExecutionException, RegistersException
    {
        final List<Integer> operands = new ArrayList<>();
        final int exitStatus = 192;
        when(registers.read(Register.EBX)).thenReturn(Arrays.asList(0, 0, 0, exitStatus));

        final ExecutionResult result = instruction.exec(runtime, operands);
        assertTrue(result.shouldExit());
        assertEquals(exitStatus, result.getExitStatus());
    }

    @Test
    public void failsIfReadingRegisterFails()
        throws RegistersException
    {
        final String message = "error message";
        final RegistersException original = new RegistersException(message);
        when(registers.read(Register.EBX)).thenThrow(original);

        InstructionExecutionException exception = assertThrows(InstructionExecutionException.class, () -> {
            instruction.exec(runtime, new ArrayList<Integer>());
        });
        assertEquals(message, exception.getMessage());
        assertSame(original, exception.getCause());
    }
}

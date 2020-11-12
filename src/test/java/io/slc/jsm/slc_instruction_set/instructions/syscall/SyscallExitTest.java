package io.slc.jsm.slc_instruction_set.instructions.syscall;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.List;
import java.util.ArrayList;

import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.SlcRuntime;
import io.slc.jsm.slc_instruction_set.SlcInstruction;
import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;

@SuppressWarnings({"initialization"})
@ExtendWith(MockitoExtension.class)
public class SyscallExitTest
{
    private SyscallExit instruction = new SyscallExit();

    @Mock private SlcRuntime runtime;

    @Test
    public void isInstruction()
    {
        assertTrue(instruction instanceof SlcInstruction);
    }

    @Test
    public void producesResultWithExitRequest()
        throws InstructionExecutionException
    {
        final List<Integer> operands = new ArrayList<>();
        final int exitStatus = 192;
        when(runtime.readRegister(Register.EBX)).thenReturn(exitStatus);

        final ExecutionResult result = instruction.exec(runtime, operands);
        assertTrue(result.shouldExit());
        assertEquals(exitStatus, result.getExitStatus());
    }
}

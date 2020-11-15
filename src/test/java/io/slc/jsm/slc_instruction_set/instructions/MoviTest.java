package io.slc.jsm.slc_instruction_set.instructions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import io.slc.jsm.slc_interpreter.ExecutionResult;
import io.slc.jsm.slc_interpreter.InstructionExecutionException;
import io.slc.jsm.slc_runtime.Register;
import io.slc.jsm.slc_runtime.Registers;
import io.slc.jsm.slc_runtime.RegistersException;
import io.slc.jsm.slc_runtime.SlcRuntime;

@SuppressWarnings("initialization")
@ExtendWith(MockitoExtension.class)
public class MoviTest
{
    private final Movi instruction = new Movi();
    @Mock private SlcRuntime runtime;
    @Mock private Registers registers;

    @BeforeEach
    public void setUp()
    {
        when(runtime.getRegisters()).thenReturn(registers);
    }

    @Test
    public void doesNotCauseJumpNorExit()
        throws InstructionExecutionException
    {
        final List<Integer> operands = Arrays.asList(Register.EAX, 0x00, 0x00);

        final ExecutionResult result = instruction.exec(runtime, operands);
        assertFalse(result.shouldJump());
        assertFalse(result.shouldExit());
    }

    @Test
    public void failsIfRegisterDoesNotExist()
        throws RegistersException
    {
        final String message = "Error message";
        final RegistersException original = new RegistersException(message);
        final int register = 0xff;
        final int valueFirstByte = 0x00;
        final int valueSecondByte = 0x00;
        final List<Integer> operands = Arrays.asList(register, valueFirstByte, valueSecondByte);
        doThrow(original).when(registers).write(register, valueFirstByte, valueSecondByte);

        final InstructionExecutionException exception = assertThrows(InstructionExecutionException.class, () -> {
            instruction.exec(runtime, operands);
        });
        assertEquals(message, exception.getMessage());
        assertSame(original, exception.getCause());
    }
}

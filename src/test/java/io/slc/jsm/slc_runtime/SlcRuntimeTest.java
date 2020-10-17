package io.slc.jsm.slc_runtime;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import io.slc.jsm.slc_interpreter.runtime.Runtime;
import io.slc.jsm.slc_interpreter.runtime.ExecutionResult;
import io.slc.jsm.slc_interpreter.runtime.RuntimeExecutionException;

import io.slc.jsm.slc_runtime.virtual_machine.VirtualMachine;
import io.slc.jsm.slc_runtime.instruction_set.InstructionSet;
import io.slc.jsm.slc_runtime.instruction_set.Instruction;
import io.slc.jsm.slc_runtime.instruction_set.InvalidInstructionException;
import io.slc.jsm.slc_runtime.instruction_set.InstructionExecutionException;

@SuppressWarnings("initialization")
@ExtendWith(MockitoExtension.class)
public class SlcRuntimeTest
{
    @Mock private InstructionSet instructionSet;
    @Mock private VirtualMachine vm;
    @InjectMocks private SlcRuntime runtime;

    public void isRuntime()
    {
        assertTrue(runtime instanceof Runtime);
    }

    @ParameterizedTest
    @MethodSource("provideInstructionsData")
    public void execInstructionsFromInstructionData(final List<Integer> instructionData, final int opcode, final List<Integer> operands)
        throws RuntimeExecutionException, InvalidInstructionException, InstructionExecutionException
    {
        final Instruction instruction = mock(Instruction.class);
        final ExecutionResult result = mock(ExecutionResult.class);

        when(instructionSet.get(opcode)).thenReturn(instruction);
        when(instruction.exec(vm, operands)).thenReturn(result);

        assertSame(result, runtime.exec(instructionData));
    }

    @Test
    public void failsIfInvalidOpcode()
        throws InvalidInstructionException
    {
        final String error = "Error message";
        final InvalidInstructionException originalException = new InvalidInstructionException(error);

        final List<Integer> instructionData = Arrays.asList(0, 1, 2, 3);
        final int opcode = 0;

        when(instructionSet.get(opcode)).thenThrow(originalException);

        final RuntimeExecutionException exception = assertThrows(RuntimeExecutionException.class, () -> {
            runtime.exec(instructionData);
        });
        assertSame(error, exception.getMessage());
        assertSame(originalException, exception.getCause());
    }

    @Test
    public void failsIfInstructionException()
        throws InvalidInstructionException, InstructionExecutionException
    {
        final String error = "Error message";
        final InstructionExecutionException originalException = new InstructionExecutionException(error);

        final List<Integer> instructionData = Arrays.asList(0, 1, 2, 3);
        final int opcode = 0;
        final List<Integer> operands = Arrays.asList(1, 2, 3);
        final Instruction instruction = mock(Instruction.class);

        when(instructionSet.get(opcode)).thenReturn(instruction);
        when(instruction.exec(vm, operands)).thenThrow(originalException);

        final RuntimeExecutionException exception = assertThrows(RuntimeExecutionException.class, () -> {
            runtime.exec(instructionData);
        });
        assertSame(error, exception.getMessage());
        assertSame(originalException, exception.getCause());
    }

    private static Stream<Arguments> provideInstructionsData()
    {
        return Stream.of(
            Arguments.of(Arrays.asList(0, 1, 2, 3), 0, Arrays.asList(1, 2, 3)),
            Arguments.of(Arrays.asList(4, 5, 6, 7), 4, Arrays.asList(5, 6, 7))
        );
    }
}

package io.slc.jsm.slc_interpreter;

import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.lenient;

import io.slc.jsm.vm.interpreter.Interpreter;
import io.slc.jsm.vm.interpreter.ProgramException;
import io.slc.jsm.vm.interpreter.Buffer;
import io.slc.jsm.slc_interpreter.Runtime;

@SuppressWarnings({"initialization", "unchecked"})
@ExtendWith(MockitoExtension.class)
public class SlcInterpreterTest
{
    private class R implements Runtime
    {
    }

    private static final int INSTRUCTION_SIZE = 4;

    @Mock private Loader<R> loader;
    @Mock private InstructionSet<R> instructionSet;
    @InjectMocks private SlcInterpreter<R> interpreter;
    private final String[] args = new String[]{ "some", "args" };
    @Mock private R runtime;
    @Mock private Buffer program;

    @BeforeEach
    public void setUp()
    {
        lenient().when(loader.load(args)).thenReturn(runtime);
        lenient().when(instructionSet.getInstructionSize()).thenReturn(INSTRUCTION_SIZE);
    }

    @Test
    public void isInterpreter()
    {
        assertTrue(interpreter instanceof Interpreter);
    }

    @Test
    public void executesSequentiallyUntilTheEnd()
        throws ProgramException, InvalidInstructionException, InstructionExecutionException
    {
        final int exitStatus = 0;
        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };
        final List<List<Integer>> instructionsData = Arrays.asList(Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 6, 7));
        final List<Instruction<R>> instructions = Arrays.asList(mock(Instruction.class), mock(Instruction.class));
        final ExecutionResult[] results = new ExecutionResult[]{ mock(ExecutionResult.class), mock(ExecutionResult.class) };

        // First instruction, does not jump
        when(program.read(ips[0], INSTRUCTION_SIZE)).thenReturn(instructionsData.get(0));
        when(instructionSet.get(instructionsData.get(0).get(0))).thenReturn(instructions.get(0));
        when(instructions.get(0).exec(runtime, instructionsData.get(0).subList(1, INSTRUCTION_SIZE))).thenReturn(results[0]);

        // Second instruction, does not jump
        when(program.read(ips[1], INSTRUCTION_SIZE)).thenReturn(instructionsData.get(1));
        when(instructionSet.get(instructionsData.get(1).get(0))).thenReturn(instructions.get(1));
        when(instructions.get(1).exec(runtime, instructionsData.get(1).subList(1, INSTRUCTION_SIZE))).thenReturn(results[1]);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void failsIfInvalidInstruction()
        throws InvalidInstructionException
    {
        final String message = "Error message";
        final InvalidInstructionException originalException = new InvalidInstructionException(message);

        final int numberOfInstructions = 1;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int ip = 0;
        final List<Integer> instructionData = Arrays.asList(0, 1, 2, 3);

        // Single instruction, fails to retrieve instruction
        when(program.read(ip, INSTRUCTION_SIZE)).thenReturn(instructionData);
        when(instructionSet.get(instructionData.get(0))).thenThrow(originalException);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals(message, exception.getMessage());
        assertSame(originalException, exception.getCause());
    }

    @Test
    public void failsIfInstructionExecutionFails()
        throws InvalidInstructionException, InstructionExecutionException
    {
        final String message = "Error message";
        final InstructionExecutionException originalException = new InstructionExecutionException(message);

        final int numberOfInstructions = 1;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int ip = 0;
        final List<Integer> instructionData = Arrays.asList(0, 1, 2 ,3);
        final Instruction<R> instruction = mock(Instruction.class);

        // Single instruction, fails when executing
        when(program.read(ip, INSTRUCTION_SIZE)).thenReturn(instructionData);
        when(instructionSet.get(instructionData.get(0))).thenReturn(instruction);
        when(instruction.exec(runtime, instructionData.subList(1, INSTRUCTION_SIZE))).thenThrow(originalException);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals(message, exception.getMessage());
        assertSame(originalException, exception.getCause());
    }

    @Test
    public void executesWithJumpsUntilTheEnd()
        throws ProgramException, InvalidInstructionException, InstructionExecutionException
    {
        final int exitStatus = 0;
        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };
        final List<List<Integer>> instructionsData = Arrays.asList(Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 6, 7));
        final List<Instruction<R>> instructions = Arrays.asList(mock(Instruction.class), mock(Instruction.class));
        final ExecutionResult[] results = new ExecutionResult[]{ mock(ExecutionResult.class), mock(ExecutionResult.class) };

        // First instruction, jumps to the next
        when(program.read(ips[0], INSTRUCTION_SIZE)).thenReturn(instructionsData.get(0));
        when(instructionSet.get(instructionsData.get(0).get(0))).thenReturn(instructions.get(0));
        when(instructions.get(0).exec(runtime, instructionsData.get(0).subList(1, INSTRUCTION_SIZE))).thenReturn(results[0]);
        when(results[0].shouldJump()).thenReturn(true);
        when(results[0].getJumpAddress()).thenReturn(INSTRUCTION_SIZE);

        // Second instruction, does not jump
        when(program.read(ips[1], INSTRUCTION_SIZE)).thenReturn(instructionsData.get(1));
        when(instructionSet.get(instructionsData.get(1).get(0))).thenReturn(instructions.get(1));
        when(instructions.get(1).exec(runtime, instructionsData.get(1).subList(1, INSTRUCTION_SIZE))).thenReturn(results[1]);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void failsIfInvalidJump()
        throws InvalidInstructionException, InstructionExecutionException
    {
        final int numberOfInstructions = 1;
        final int invalidAddress = (numberOfInstructions + 1) * INSTRUCTION_SIZE;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int ip = 0;
        final List<Integer> instructionData = Arrays.asList(0, 1, 2, 3);
        final Instruction<R> instruction = mock(Instruction.class);
        final ExecutionResult result = mock(ExecutionResult.class);

        // Single instruction, jump past the end of the program
        when(program.read(ip, INSTRUCTION_SIZE)).thenReturn(instructionData);
        when(instructionSet.get(instructionData.get(0))).thenReturn(instruction);
        when(instruction.exec(runtime, instructionData.subList(1, INSTRUCTION_SIZE))).thenReturn(result);
        when(result.shouldJump()).thenReturn(true);
        when(result.getJumpAddress()).thenReturn(invalidAddress);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals(String.format("Invalid jump to address %d", invalidAddress), exception.getMessage());
    }

    @Test
    public void executesWithExitInstruction()
        throws ProgramException, InvalidInstructionException, InstructionExecutionException
    {
        final int exitStatus = 192;
        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };
        final List<List<Integer>> instructionsData = Arrays.asList(Arrays.asList(0, 1, 2, 3), Arrays.asList(4, 5, 6, 6));
        final List<Instruction<R>> instructions = Arrays.asList(mock(Instruction.class), mock(Instruction.class));
        final ExecutionResult[] results = new ExecutionResult[]{ mock(ExecutionResult.class), mock(ExecutionResult.class) };

        // First instruction, does not jump
        when(program.read(ips[0], INSTRUCTION_SIZE)).thenReturn(instructionsData.get(0));
        when(instructionSet.get(instructionsData.get(0).get(0))).thenReturn(instructions.get(0));
        when(instructions.get(0).exec(runtime, instructionsData.get(0).subList(1, INSTRUCTION_SIZE))).thenReturn(results[0]);

        // Second instruction, exits
        when(program.read(ips[1], INSTRUCTION_SIZE)).thenReturn(instructionsData.get(1));
        when(instructionSet.get(instructionsData.get(1).get(0))).thenReturn(instructions.get(1));
        when(instructions.get(1).exec(runtime, instructionsData.get(1).subList(1, INSTRUCTION_SIZE))).thenReturn(results[1]);
        when(results[1].shouldExit()).thenReturn(true);
        when(results[1].getExitStatus()).thenReturn(exitStatus);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }
}

package io.slc.jsm.slc_interpreter;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;

import java.util.List;
import java.util.Arrays;

import io.slc.jsm.vm.interpreter.Buffer;
import io.slc.jsm.vm.interpreter.Interpreter;
import io.slc.jsm.vm.interpreter.ProgramException;

@SuppressWarnings({"initialization", "unchecked"})
@ExtendWith(MockitoExtension.class)
public class SlcInterpreterTest
{
    private static final int INSTRUCTION_SIZE = 4;

    @Mock private Loader loader;
    @Mock private Configuration configuration;
    @InjectMocks private SlcInterpreter interpreter;

    @Mock private Buffer program;
    private final String[] args = new String[]{ "test" };
    @Mock private Runtime runtime;

    @BeforeEach
    public void setUp()
    {
        lenient().when(configuration.getInstructionSize()).thenReturn(INSTRUCTION_SIZE);
        lenient().when(loader.load(args)).thenReturn(runtime);
    }

    @Test
    public void isInterpreter()
    {
        assertTrue(interpreter instanceof Interpreter);
    }

    @Test
    public void executesSequentiallyUntilTheEnd()
        throws RuntimeExecutionException, ProgramException
    {
        final int exitStatus = 0;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };
        final List<List<Integer>> instructions = Arrays.asList(mock(List.class), mock(List.class));
        final ExecutionResult[] results = new ExecutionResult[]{ mock(ExecutionResult.class), mock(ExecutionResult.class) };

        // First instruction, does not jump
        when(program.read(ips[0], INSTRUCTION_SIZE)).thenReturn(instructions.get(0));
        when(runtime.exec(instructions.get(0))).thenReturn(results[0]);

        // Second instruction, does not jump
        when(program.read(ips[1], INSTRUCTION_SIZE)).thenReturn(instructions.get(1));
        when(runtime.exec(instructions.get(1))).thenReturn(results[1]);

        final int actualExitStatus = interpreter.run(program, args);
        assertSame(exitStatus, actualExitStatus);
    }

    @Test
    public void executesWithJumpsUntilTheEnd()
        throws RuntimeExecutionException, ProgramException
    {
        final int exitStatus = 0;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };
        final List<List<Integer>> instructions = Arrays.asList(mock(List.class), mock(List.class));
        final ExecutionResult[] results = new ExecutionResult[]{ mock(ExecutionResult.class), mock(ExecutionResult.class) };

        // First instructions, jumps to the next
        when(program.read(ips[0], INSTRUCTION_SIZE)).thenReturn(instructions.get(0));
        when(runtime.exec(instructions.get(0))).thenReturn(results[0]);
        when(results[0].shouldJump()).thenReturn(true);
        when(results[0].getJumpAddress()).thenReturn(INSTRUCTION_SIZE);

        // Second instruction, does not jump
        when(program.read(ips[1], INSTRUCTION_SIZE)).thenReturn(instructions.get(1));
        when(runtime.exec(instructions.get(1))).thenReturn(results[1]);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void executesWithExit()
        throws RuntimeExecutionException, ProgramException
    {
        final int exitStatus = 192;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };
        final List<List<Integer>> instructions = Arrays.asList(mock(List.class), mock(List.class));
        final ExecutionResult[] results = new ExecutionResult[]{ mock(ExecutionResult.class), mock(ExecutionResult.class) };

        // First instruction, does not jump
        when(program.read(ips[0], INSTRUCTION_SIZE)).thenReturn(instructions.get(0));
        when(runtime.exec(instructions.get(0))).thenReturn(results[0]);

        // Second instruction, exits
        when(program.read(ips[1], INSTRUCTION_SIZE)).thenReturn(instructions.get(1));
        when(runtime.exec(instructions.get(1))).thenReturn(results[1]);
        when(results[1].shouldExit()).thenReturn(true);
        when(results[1].getExitStatus()).thenReturn(exitStatus);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void failsIfInvalidJump()
        throws RuntimeExecutionException
    {
        final int numberOfInstructions = 1;
        final int programSize = numberOfInstructions * INSTRUCTION_SIZE;
        when(program.getSize()).thenReturn(programSize);

        final List<Integer> instruction = mock(List.class);
        final ExecutionResult result = mock(ExecutionResult.class);

        // Single instruction, jump past end of program
        when(program.read(0, INSTRUCTION_SIZE)).thenReturn(instruction);
        when(runtime.exec(instruction)).thenReturn(result);
        when(result.shouldJump()).thenReturn(true);
        when(result.getJumpAddress()).thenReturn(programSize);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals("Invalid jump", exception.getMessage());
    }

    @Test
    public void failsIfInvalidInstruction()
        throws RuntimeExecutionException
    {
        final String error = "Error message";
        final RuntimeExecutionException originalException = new RuntimeExecutionException(error);

        final int numberOfInstructions = 1;
        final int programSize = numberOfInstructions * INSTRUCTION_SIZE;
        when(program.getSize()).thenReturn(programSize);

        final List<Integer> instruction = mock(List.class);
        final ExecutionResult result = mock(ExecutionResult.class);

        when(program.read(0, INSTRUCTION_SIZE)).thenReturn(instruction);
        when(runtime.exec(instruction)).thenThrow(originalException);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals(error, exception.getMessage());
        assertSame(originalException, exception.getCause());
    }
}

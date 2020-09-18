package io.slc.sma;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;

import io.slc.jsm.interpreter.Program;
import io.slc.jsm.interpreter.Interpreter;
import io.slc.jsm.interpreter.ProgramException;

@SuppressWarnings("initialization")
@ExtendWith(MockitoExtension.class)
public class SMAInterpreterTest
{
    private static final int INSTRUCTION_SIZE = 4;

    @Mock private Loader loader;
    @Mock private Fetcher fetcher;
    @Mock private Configuration configuration;
    @InjectMocks private SMAInterpreter interpreter;

    @Mock private Program program;
    private final String[] args = new String[]{ "test" };
    @Mock private Runtime runtime;

    @BeforeEach
    public void setUp()
    {
        lenient().when(configuration.getInstructionSize()).thenReturn(INSTRUCTION_SIZE);
        lenient().when(loader.load(program, args)).thenReturn(runtime);
    }

    @Test
    public void isInterpreter()
    {
        assertTrue(interpreter instanceof Interpreter);
    }

    @Test
    public void executesSequentiallyUntilTheEnd()
    {
        final int exitStatus = 0;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final Instruction[] instructions = new Instruction[]{ mock(Instruction.class), mock(Instruction.class) };
        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };

        // First instruction, does not jump
        when(fetcher.fetch(program, ips[0])).thenReturn(instructions[0]);
        when(instructions[0].exec(runtime)).thenReturn(runtime);

        // Second instruction, does not jump
        when(fetcher.fetch(program, ips[1])).thenReturn(instructions[1]);
        when(instructions[1].exec(runtime)).thenReturn(runtime);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void executesWithJumpsUntilTheEnd()
    {
        final int exitStatus = 0;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final Instruction[] instructions = new Instruction[]{ mock(Instruction.class), mock(Instruction.class) };
        final Runtime[] runtimes = new Runtime[]{ mock(Runtime.class), mock(Runtime.class), mock(Runtime.class) };
        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };

        when(loader.load(program, args)).thenReturn(runtimes[0]);

        // First instructions, jumps to the next
        when(fetcher.fetch(program, ips[0])).thenReturn(instructions[0]);
        when(instructions[0].exec(runtimes[0])).thenReturn(runtimes[1]);
        when(runtimes[1].shouldJump()).thenReturn(true);
        when(runtimes[1].getJumpAddress()).thenReturn(INSTRUCTION_SIZE);

        // Second instruction, does not jump
        when(fetcher.fetch(program, ips[1])).thenReturn(instructions[1]);
        when(instructions[1].exec(runtimes[1])).thenReturn(runtimes[2]);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void executesWithExit()
    {
        final int exitStatus = 192;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final Instruction[] instructions = new Instruction[]{ mock(Instruction.class), mock(Instruction.class) };
        final Runtime[] runtimes = new Runtime[]{ mock(Runtime.class), mock(Runtime.class), mock(Runtime.class) };
        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };

        when(loader.load(program, args)).thenReturn(runtimes[0]);

        // First instruction, does not jump
        when(fetcher.fetch(program, ips[0])).thenReturn(instructions[0]);
        when(instructions[0].exec(runtimes[0])).thenReturn(runtimes[1]);

        // Second instruction, exits
        when(fetcher.fetch(program, ips[1])).thenReturn(instructions[1]);
        when(instructions[1].exec(runtimes[1])).thenReturn(runtimes[2]);
        when(runtimes[2].shouldExit()).thenReturn(true);
        when(runtimes[2].getExitStatus()).thenReturn(exitStatus);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void failsIfInvalidJump()
    {
        final int numberOfInstructions = 1;
        final int programSize = numberOfInstructions * INSTRUCTION_SIZE;
        when(program.getSize()).thenReturn(programSize);

        final Instruction instruction = mock(Instruction.class);
        final Runtime[] runtimes = new Runtime[]{ mock(Runtime.class), mock(Runtime.class) };

        when(loader.load(program, args)).thenReturn(runtimes[0]);

        // Single instruction, jump past end of program
        when(fetcher.fetch(program, 0)).thenReturn(instruction);
        when(instruction.exec(runtimes[0])).thenReturn(runtimes[1]);
        when(runtimes[1].shouldJump()).thenReturn(true);
        when(runtimes[1].getJumpAddress()).thenReturn(programSize);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals("Invalid jump instruction", exception.getMessage());
    }
}
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
    public void executesUntilExit()
    {
        final int exitStatus = 192;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final Instruction[] instructions = new Instruction[]{ mock(Instruction.class), mock(Instruction.class) };
        final RuntimeStatus[] statuses = new RuntimeStatus[]{ mock(RuntimeStatus.class), mock(RuntimeStatus.class) };
        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };

        // First instruction, does not exit nor jump
        when(fetcher.fetch(program, ips[0])).thenReturn(instructions[0]);
        when(instructions[0].exec(runtime)).thenReturn(statuses[0]);

        // Second instruction, exits
        when(fetcher.fetch(program, ips[1])).thenReturn(instructions[1]);
        when(instructions[1].exec(runtime)).thenReturn(statuses[1]);
        when(statuses[1].shouldExit()).thenReturn(true);
        when(statuses[1].getExitStatus()).thenReturn(exitStatus);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void executesWithJumps()
    {
        final int exitStatus = 192;

        final int numberOfInstructions = 2;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final Instruction[] instructions = new Instruction[]{ mock(Instruction.class), mock(Instruction.class) };
        final RuntimeStatus[] statuses = new RuntimeStatus[]{ mock(RuntimeStatus.class), mock(RuntimeStatus.class) };
        final int[] ips = new int[]{ 0, INSTRUCTION_SIZE };

        // First instructions, jumps to the next
        when(fetcher.fetch(program, ips[0])).thenReturn(instructions[0]);
        when(instructions[0].exec(runtime)).thenReturn(statuses[0]);
        when(statuses[0].shouldJump()).thenReturn(true);
        when(statuses[0].getJumpAddress()).thenReturn(INSTRUCTION_SIZE);

        // Second instruction, exits
        when(fetcher.fetch(program, ips[1])).thenReturn(instructions[1]);
        when(instructions[1].exec(runtime)).thenReturn(statuses[1]);
        when(statuses[1].shouldExit()).thenReturn(true);
        when(statuses[1].getExitStatus()).thenReturn(exitStatus);

        final int actualExitStatus = interpreter.run(program, args);
        assertEquals(exitStatus, actualExitStatus);
    }

    @Test
    public void failsIfMissingExit()
    {
        final int numberOfInstructions = 1;
        when(program.getSize()).thenReturn(numberOfInstructions * INSTRUCTION_SIZE);

        final Instruction instruction = mock(Instruction.class);
        final RuntimeStatus status = mock(RuntimeStatus.class);

        // Single instruction, not exit
        when(fetcher.fetch(program, 0)).thenReturn(instruction);
        when(instruction.exec(runtime)).thenReturn(status);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals("Missing exit instruction", exception.getMessage());
    }

    @Test
    public void failsIfInvalidJump()
    {
        final int numberOfInstructions = 1;
        final int programSize = numberOfInstructions * INSTRUCTION_SIZE;
        when(program.getSize()).thenReturn(programSize);

        final Instruction instruction = mock(Instruction.class);
        final RuntimeStatus status = mock(RuntimeStatus.class);

        // Single instruction, jump past end of program
        when(fetcher.fetch(program, 0)).thenReturn(instruction);
        when(instruction.exec(runtime)).thenReturn(status);
        when(status.shouldJump()).thenReturn(true);
        when(status.getJumpAddress()).thenReturn(programSize);

        final ProgramException exception = assertThrows(ProgramException.class, () -> {
            interpreter.run(program, args);
        });
        assertEquals("Invalid jump instruction", exception.getMessage());
    }
}
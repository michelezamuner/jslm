package io.slc.jsm.defaultinstructionset;

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

import io.slc.jsm.defaultinterpreter.Instruction;
import io.slc.jsm.defaultinterpreter.InstructionSet;
import io.slc.jsm.defaultinterpreter.InvalidInstructionException;

@SuppressWarnings("initialization")
@ExtendWith(MockitoExtension.class)
public class DefaultInstructionSetTest
{
    @Mock private InstructionFactory factory;
    @InjectMocks private DefaultInstructionSet instructionSet;

    @Mock private Instruction instruction;

    public void isInstructionSet()
    {
        assertTrue(instructionSet instanceof InstructionSet);
    }

    @ParameterizedTest
    @MethodSource("provideInstructionsData")
    public void getsInstructionsFromInstructionData(final List<Integer> instructionData, final int opcode, final List<Integer> operands)
        throws InvalidInstructionException
    {
        when(factory.create(opcode, operands)).thenReturn(instruction);
        assertSame(instruction, instructionSet.get(instructionData));
    }

    @Test
    public void failsIfInvalidOpcode()
    {
        final List<Integer> instructionData = Arrays.asList(0, 1, 2, 3);
        final int opcode = 0;
        final List<Integer> operands = Arrays.asList(1, 2, 3);

        when(factory.create(opcode, operands)).thenThrow(new IllegalArgumentException(""));

        final InvalidInstructionException exception = assertThrows(InvalidInstructionException.class, () -> {
            instructionSet.get(instructionData);
        });
        assertSame(instructionData, exception.getInstructionData());
    }

    private static Stream<Arguments> provideInstructionsData()
    {
        return Stream.of(
            Arguments.of(Arrays.asList(0, 1, 2, 3), 0, Arrays.asList(1, 2, 3)),
            Arguments.of(Arrays.asList(4, 5, 6, 7), 4, Arrays.asList(5, 6, 7))
        );
    }
}

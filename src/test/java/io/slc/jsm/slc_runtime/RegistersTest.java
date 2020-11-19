package io.slc.jsm.slc_runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("initialization")
public class RegistersTest
{
    private final Registers registers = new Registers();

    @ParameterizedTest
    @MethodSource("provideReadRegistersData")
    public void writesAndReadsRegisters(final Register register, final List<Integer> expected)
        throws RegistersException
    {
        registers.write(register.getAddress(), expected);

        final List<Integer> data = registers.read(register.getAddress());
        assertEquals(expected, data);

        final List<Integer> uncheckedData = registers.read(register);
        assertEquals(expected, uncheckedData);
    }

    public static Stream<Arguments> provideReadRegistersData()
    {
        return Stream.of(
            Arguments.of(Register.EAX, Arrays.asList(1, 2, 3, 4)),
            Arguments.of(Register.AX, Arrays.asList(1, 2)),
            Arguments.of(Register.AH, Arrays.asList(1)),
            Arguments.of(Register.AL, Arrays.asList(1)),
            Arguments.of(Register.EBX, Arrays.asList(5, 6, 7, 8)),
            Arguments.of(Register.BX, Arrays.asList(1, 2)),
            Arguments.of(Register.BH, Arrays.asList(1)),
            Arguments.of(Register.BL, Arrays.asList(1)),
            Arguments.of(Register.ECX, Arrays.asList(9, 10, 11, 12)),
            Arguments.of(Register.CX, Arrays.asList(1, 2)),
            Arguments.of(Register.CH, Arrays.asList(1)),
            Arguments.of(Register.CL, Arrays.asList(1)),
            Arguments.of(Register.EDX, Arrays.asList(13, 14, 15, 16)),
            Arguments.of(Register.DX, Arrays.asList(1, 2)),
            Arguments.of(Register.DH, Arrays.asList(1)),
            Arguments.of(Register.DL, Arrays.asList(1))
        );
    }

    @Test
    public void failsWritingToInvalidRegister()
    {
        final int address = 0xff;
        final RegistersException exception = assertThrows(RegistersException.class, () -> {
            registers.write(address, Arrays.asList(1, 2, 3, 4));
        });
        assertEquals("Invalid register address: " + address, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideWriteRegisterDataWithInvalidSize")
    public void failsWritingDataWithInvalidSize(final int address, final List<Integer> data, final int expectedSize)
    {
        final RegistersException exception = assertThrows(RegistersException.class, () -> {
            registers.write(address, data);
        });
        assertEquals(
            String.format("Invalid register data size: should be %d, got %d", expectedSize, data.size()),
            exception.getMessage()
        );
    }

    public static Stream<Arguments> provideWriteRegisterDataWithInvalidSize()
    {
        return Stream.of(
            Arguments.of(Register.EAX.getAddress(), Arrays.asList(0, 1), 4),
            Arguments.of(Register.AX.getAddress(), Arrays.asList(0, 1, 2, 3), 2),
            Arguments.of(Register.AH.getAddress(), Arrays.asList(0, 1), 1),
            Arguments.of(Register.AL.getAddress(), Arrays.asList(0, 1), 1)
        );
    }

    @Test
    public void failsReadingInvalidRegister()
    {
        final int address = 0xff;
        final RegistersException exception = assertThrows(RegistersException.class, () -> {
            registers.read(address);
        });
        assertEquals("Invalid register address: " + address, exception.getMessage());
    }
}

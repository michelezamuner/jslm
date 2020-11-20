package io.slc.jsm.slc_runtime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("initialization")
public class RegistersTest
{
    private Registers registers;

    @BeforeEach
    public void setUp()
    {
        registers = new Registers();
    }

    @ParameterizedTest
    @MethodSource("provideRegistersData")
    public void writesAndReadsRegisters(final Register register, final int[] expected)
        throws RegistersException
    {
        registers.write(register.getAddress(), expected);

        final int[] data = registers.read(register.getAddress());
        assertArrayEquals(expected, data);

        final int[] uncheckedData = registers.read(register);
        assertArrayEquals(expected, uncheckedData);
    }

    public static Stream<Arguments> provideRegistersData()
    {
        return Stream.of(
            Arguments.of(Register.EAX, new int[]{1, 2, 3, 4}),
            Arguments.of(Register.AX, new int[]{0, 0, 1, 2}),
            Arguments.of(Register.AH, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.AL, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.EBX, new int[]{5, 6, 7, 8}),
            Arguments.of(Register.BX, new int[]{0, 0, 1, 2}),
            Arguments.of(Register.BH, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.BL, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.ECX, new int[]{9, 10, 11, 12}),
            Arguments.of(Register.CX, new int[]{0, 0, 1, 2}),
            Arguments.of(Register.CH, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.CL, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.EDX, new int[]{13, 14, 15, 16}),
            Arguments.of(Register.DX, new int[]{0, 0, 1, 2}),
            Arguments.of(Register.DH, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.DL, new int[]{0, 0, 0, 1})
        );
    }

    @ParameterizedTest
    @MethodSource("provideDefaultRegistersData")
    public void readsDefaultRegistersData(final Register register, final int[] expected)
    {
        final int[] data = registers.read(register);
        assertArrayEquals(expected, data);
    }

    public static Stream<Arguments> provideDefaultRegistersData()
    {
        return Stream.of(
            Arguments.of(Register.EAX, new int[]{0, 0, 0, 0}),
            Arguments.of(Register.EBX, new int[]{0, 0, 0, 0}),
            Arguments.of(Register.ECX, new int[]{0, 0, 0, 0}),
            Arguments.of(Register.EDX, new int[]{0, 0, 0, 0})
        );
    }

    @ParameterizedTest
    @MethodSource("provideNotMatchingRegistersData")
    public void writesAndReadsNotMatchingRegisters(
        final Register writeRegister,
        final int[] writeData,
        final Register readRegister,
        final int[] expected
    )
        throws RegistersException
    {
        registers.write(writeRegister.getAddress(), writeData);

        final int[] data = registers.read(readRegister);
        assertArrayEquals(data, expected);
    }

    public static Stream<Arguments> provideNotMatchingRegistersData()
    {
        return Stream.of(
            Arguments.of(Register.EAX, new int[]{1, 2, 3, 4}, Register.AX, new int[]{0, 0, 3, 4}),
            Arguments.of(Register.EAX, new int[]{1, 2, 3, 4}, Register.AH, new int[]{0, 0, 0, 3}),
            Arguments.of(Register.EAX, new int[]{1, 2, 3, 4}, Register.AL, new int[]{0, 0, 0, 4}),
            Arguments.of(Register.AX, new int[]{0, 0, 1, 2}, Register.EAX, new int[]{0, 0, 1, 2}),
            Arguments.of(Register.AX, new int[]{0, 0, 1, 2}, Register.AH, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.AX, new int[]{0, 0, 1, 2}, Register.AL, new int[]{0, 0, 0, 2}),
            Arguments.of(Register.AH, new int[]{0, 0, 0, 1}, Register.EAX, new int[]{0, 0, 1, 0}),
            Arguments.of(Register.AH, new int[]{0, 0, 0, 1}, Register.AX, new int[]{0, 0, 1, 0}),
            Arguments.of(Register.AH, new int[]{0, 0, 0, 1}, Register.AL, new int[]{0, 0, 0, 0}),
            Arguments.of(Register.AL, new int[]{0, 0, 0, 1}, Register.EAX, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.AL, new int[]{0, 0, 0, 1}, Register.AX, new int[]{0, 0, 0, 1}),
            Arguments.of(Register.AL, new int[]{0, 0, 0, 1}, Register.AH, new int[]{0, 0, 0, 0})
        );
    }

    @Test
    public void failsWritingToInvalidRegister()
    {
        final int address = 0xff;
        final RegistersException exception = assertThrows(RegistersException.class, () -> {
            registers.write(address, new int[]{1, 2, 3, 4});
        });
        assertEquals("Invalid register address: " + address, exception.getMessage());
    }

    @Test
    public void failsWritingDataWithInvalidSize()
    {
        final RegistersException exception = assertThrows(RegistersException.class, () -> {
            registers.write(Register.AX.getAddress(), new int[]{});
        });
        assertEquals("Invalid register data size: should be 4, got 0", exception.getMessage());
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

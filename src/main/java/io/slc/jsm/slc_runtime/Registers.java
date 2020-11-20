package io.slc.jsm.slc_runtime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Registers
{
    private final static Set<Integer> ROWS_ADDRESSES = new HashSet<>(Arrays.asList(0b00, 0b01, 0b10, 0b11));
    private final int[][] data = new int[4][4];

    public void write(final int address, final int[] bytes)
        throws RegistersException
    {
        final int rowAddress = getRowAddress(address);
        final int dataAddress = getDataAddress(address);
        if (bytes.length != 4) {
            throw new RegistersException("Invalid register data size: should be 4, got " + bytes.length);
        }

        // E*X
        if (dataAddress == 0b00) {
            for (int i = 0; i < 4; i++) {
                data[rowAddress][i] = bytes[i];
            }
            return;
        }

        // *X
        if (dataAddress == 0b01) {
            for (int i = 2; i < 4; i++) {
                data[rowAddress][i] = bytes[i];
            }
            return;
        }

        // *H
        if (dataAddress == 0b10) {
            data[rowAddress][2] = bytes[3];
            return;
        }

        // *L
        data[rowAddress][3] = bytes[3];
    }

    public int[] read(final int address)
        throws RegistersException
    {
        return getData(getRowAddress(address), getDataAddress(address));
    }

    public int[] read(final Register register)
    {
        final int address = register.getAddress();

        return getData(getUncheckedRowAddress(address), getDataAddress(address));
    }

    private int[] getData(final int rowAddress, final int dataAddress)
    {
        final int[] original = data[rowAddress];
        // E*X
        if (dataAddress == 0b00) {
            return new int[]{original[0], original[1], original[2], original[3]};
        }

        // *X
        if (dataAddress == 0b01) {
            return new int[]{0, 0, original[2], original[3]};
        }

        // *H
        if (dataAddress == 0b10) {
            return new int[]{0, 0, 0, original[2]};
        }

        // *L
        return new int[]{0, 0, 0, original[3]};
    }

    private int getRowAddress(final int address)
        throws RegistersException
    {
        final int rowAddress = getUncheckedRowAddress(address);
        if (!ROWS_ADDRESSES.contains(rowAddress)) {
            throw new RegistersException("Invalid register address: " + address);
        }

        return rowAddress;
    }

    private int getUncheckedRowAddress(final int address)
    {
        return address >> 2;
    }

    private int getDataAddress(final int address)
    {
        return address & 0b11;
    }
}

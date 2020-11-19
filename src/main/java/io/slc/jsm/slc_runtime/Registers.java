package io.slc.jsm.slc_runtime;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Registers
{
    private final static Set<Integer> ROWS_ADDRESSES = new HashSet<>(Arrays.asList(0b00, 0b01, 0b10, 0b11));
    private final Integer[][] data = new Integer[4][4];

    public void write(final int address, final List<Integer> bytes)
        throws RegistersException
    {
        final int rowAddress = getRowAddress(address);
        final int dataAddress = getDataAddress(address);

        // E*X
        if (dataAddress == 0b00) {
            if (bytes.size() != 4) {
                throw new RegistersException("Invalid register data size: should be 4, got " + bytes.size());
            }
            for (int i = 0; i < 4; i++) {
                data[rowAddress][i] = bytes.get(i);
            }
            return;
        }

        // *X
        if (dataAddress == 0b01) {
            if (bytes.size() != 2) {
                throw new RegistersException("Invalid register data size: should be 2, got " + bytes.size());
            }
            for (int i = 2; i < 4; i++) {
                data[rowAddress][i] = bytes.get(i - 2);
            }
            return;
        }

        // *H
        if (dataAddress == 0b10) {
            if (bytes.size() != 1) {
                throw new RegistersException("Invalid register data size: should be 1, got " + bytes.size());
            }
            data[rowAddress][2] = bytes.get(0);
            return;
        }

        // *L
        if (bytes.size() != 1) {
            throw new RegistersException("Invalid register data size: should be 1, got " + bytes.size());
        }
        data[rowAddress][3] = bytes.get(0);
    }

    public List<Integer> read(final int address)
        throws RegistersException
    {
        return getData(getRowAddress(address), getDataAddress(address));
    }

    public List<Integer> read(final Register register)
    {
        final int address = register.getAddress();
        return getData(getUncheckedRowAddress(address), getDataAddress(address));
    }

    private List<Integer> getData(final int rowAddress, final int dataAddress)
    {
         // E*X
        if (dataAddress == 0b00) {
            return Arrays.asList(data[rowAddress]);
        }

        // *X
        if (dataAddress == 0b01) {
            return Arrays.asList(data[rowAddress]).subList(2, 4);
        }

        // *H
        if (dataAddress == 0b10) {
            return Collections.singletonList(data[rowAddress][2]);
        }

        // *L
        return Collections.singletonList(data[rowAddress][3]);
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

    private int getDataAddress(final int address)
    {
        return address & 0b11;
    }

    private int getUncheckedRowAddress(final int address)
    {
        return address >> 2;
    }
}

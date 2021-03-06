package io.slc.jsm.vm.interpreter;

import java.util.List;

public interface Buffer
{
    int getSize();

    List<Integer> read(final int address, final int size);
}

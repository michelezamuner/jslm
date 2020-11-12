package io.slc.jsm.slc_interpreter.stubs;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import io.slc.jsm.vm.interpreter.Buffer;

public class StubBuffer implements Buffer
{
    private final List<Integer> bytes;

    public StubBuffer(final int[] bytes)
    {
        this.bytes = IntStream.of(bytes).boxed().collect(Collectors.toList());
    }

    public int getSize()
    {
        return bytes.size();
    }

    public List<Integer> read(final int address, final int size)
    {
        return bytes.subList(address, address + size);
    }
}

package io.slc.jsm.slc_interpreter;

public interface Loader<R extends Runtime>
{
    R load(final String... args);
}

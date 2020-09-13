package io.slc.jslm.interpreter;

public interface Interpreter
{
    int run(final Program program, final String... args)
        throws ProgramException;
}
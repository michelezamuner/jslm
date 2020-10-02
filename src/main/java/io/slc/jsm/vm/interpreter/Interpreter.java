package io.slc.jsm.vm.interpreter;

public interface Interpreter
{
    int run(final Buffer program, final String... args)
        throws ProgramException;
}
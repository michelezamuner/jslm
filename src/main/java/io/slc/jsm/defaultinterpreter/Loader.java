package io.slc.jsm.defaultinterpreter;

import io.slc.jsm.vm.interpreter.Buffer;

public interface Loader
{
    Runtime load(final Buffer program, final String... args);
}
package io.slc.sma;

import io.slc.jslm.interpreter.Program;
import io.slc.jslm.interpreter.Interpreter;
import io.slc.jslm.interpreter.ProgramException;

public class SMAInterpreter implements Interpreter
{
    private final Loader loader;
    private final Fetcher fetcher;
    private final Configuration configuration;

    public SMAInterpreter(
        final Loader loader,
        final Fetcher fetcher,
        final Configuration configuration
    ) {
        this.loader = loader;
        this.fetcher = fetcher;
        this.configuration = configuration;
    }
    
    public int run(final Program program, final String... args)
        throws ProgramException
    {
        int ip = 0;
        int exitStatus = 0;
        final int maximumAddress = program.getSize() - configuration.getInstructionSize();
        final Runtime runtime = loader.load(program, args);

        while (true) {
            final Instruction instruction = fetcher.fetch(program, ip);
            final RuntimeStatus status = instruction.exec(runtime);

            if (status.shouldJump()) {
                ip = status.getJumpAddress();
                if (ip > maximumAddress) {
                    throw new ProgramException("Invalid jump instruction");
                }
                continue;
            }

            if (status.shouldExit()) {
                exitStatus = status.getExitStatus();
                break;
            }

            ip += configuration.getInstructionSize();
            if (ip > maximumAddress) {
                throw new ProgramException("Missing exit instruction");
            }
        }

        return exitStatus;
    }
}
package io.slc.jsm.defaultinterpreter;

import java.util.List;

import io.slc.jsm.vm.interpreter.Buffer;
import io.slc.jsm.vm.interpreter.Interpreter;
import io.slc.jsm.vm.interpreter.ProgramException;

public class DefaultInterpreter implements Interpreter
{
    private final Loader loader;
    private final Configuration configuration;

    public DefaultInterpreter(final Loader loader, final Configuration configuration)
    {
        this.loader = loader;
        this.configuration = configuration;
    }
    
    public int run(final Buffer program, final String... args)
        throws ProgramException
    {
        final int instructionSize = configuration.getInstructionSize();
        final int maximumAddress = program.getSize() - instructionSize;
        final Runtime runtime = loader.load(args);

        int ip = 0;
        int exitStatus = 0;
        while (true) {
            final List<Integer> instruction = program.read(ip, instructionSize);
            final ExecutionResult result = exec(runtime, instruction);
            
            if (result.shouldJump()) {
                ip = result.getJumpAddress();
                if (ip > maximumAddress) {
                    throw new ProgramException("Invalid jump");
                }
                continue;
            }
                    
            if (result.shouldExit()) {
                exitStatus = result.getExitStatus();
                break;
            }

            ip += instructionSize;
            if (ip > maximumAddress) {
                break;
            }
        }

        return exitStatus;
    }

    private ExecutionResult exec(Runtime runtime, List<Integer> instruction)
        throws ProgramException
    {
        try {
            return runtime.exec(instruction);
        } catch (RuntimeExecutionException e) {
            throw new ProgramException(e.getMessage(), e);
        }
    }
}

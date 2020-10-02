package io.slc.jsm.defaultinterpreter;

import java.util.List;

import io.slc.jsm.vm.interpreter.Buffer;
import io.slc.jsm.vm.interpreter.Interpreter;
import io.slc.jsm.vm.interpreter.ProgramException;

public class DefaultInterpreter implements Interpreter
{
    private final Loader loader;
    private final InstructionSet instructionSet;
    private final Configuration configuration;

    public DefaultInterpreter(final Loader loader, final InstructionSet instructionSet, final Configuration configuration)
    {
        this.loader = loader;
        this.instructionSet = instructionSet;
        this.configuration = configuration;
    }
    
    public int run(final Buffer program, final String... args)
        throws ProgramException
    {
        int ip = 0;
        int exitStatus = 0;
        final int maximumAddress = program.getSize() - configuration.getInstructionSize();
        Runtime runtime = loader.load(program, args);

        while (true) {
            final Instruction instruction = fetch(ip, program);
            runtime = instruction.exec(runtime);

            if (runtime.shouldJump()) {
                ip = runtime.getJumpAddress();
                if (ip > maximumAddress) {
                    throw new ProgramException("Invalid jump");
                }
                continue;
            }

            if (runtime.shouldExit()) {
                exitStatus = runtime.getExitStatus();
                break;
            }

            ip += configuration.getInstructionSize();
            if (ip > maximumAddress) {
                break;
            }
        }

        return exitStatus;
    }

    private Instruction fetch(final int ip, final Buffer program)
        throws ProgramException
    {
        final List<Integer> instructionData = program.read(ip, configuration.getInstructionSize());

        try {
            return instructionSet.get(instructionData);
        } catch (InvalidInstructionException e) {
            throw new ProgramException(e.getMessage(), e);
        }
    }
}

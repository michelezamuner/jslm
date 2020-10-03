# Default Interpreter

## Dependencies
- `vm.interpreter`

## Requirements
- `defaultinterpreter.Loader`
- `defaultinterpreter.Runtime`

## Description

Provides an implementation for `vm.interpreter.Interpreter` representing a virtual machine interpreter with these characteristics:
- all components of the virtual machine are abstracted away inside the `Runtime` object, thus supporting any virtual machine architecture (with or without registers, stack, etc.)
- instructions are read directly from the given program `Buffer`, without storing it in any virtual memory: this will make it not possible to write self-modifying programs
- there is a `Loader` component, but it will just store the context arguments somehow in the runtime: since the program is directly read from the given object, it makes no sense to have it stored in the virtual machine as well
- the instruction size is fixed, and provided by a configuration object
- the opcode is located at the first "byte" of the instruction
- there is no IP, nor IR registers, instead they're replaced by local variables for simplicity
- jumps and exit instructions are supported
- jumps and exit requests are not modeled with any domain mechanism, instead a simple `ExecutionResult` object is used
- if no exit instruction is provided by the program, the execution will terminate with exit code `0` after reaching the last instruction of the program

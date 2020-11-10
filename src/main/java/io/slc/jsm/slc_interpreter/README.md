# SLC Interpreter

## Dependencies
- `vm.interpreter.Interpreter`
- `vm.interpreter.Buffer`

## Description

This module provides an implementation for `vm.interpreter.Interpreter`, representing a virtual machine interpreter:
- all details of the virtual machine's runtime architecture are abstracted behind the `Loader`, `InstructionSet` and `Runtime` interfaces, making it possible for this interpreter to support any runtime architecture (with or without registers, stack, etc.)
- to allow components down the line to use any specific implementation of `Runtime` without having to downcast the one coming from the shared interpreter, we define our components as generics, taking the specific `Runtime` as parameter: this way a module that implements `InstructionSet` or `Loader`, will be able to specify the precise runtime to use, for example with `MyInstructionSet<MyRuntime>` and `MyLoader<MyRuntime>`
- instructions data is read directly from the given program `vm.interpreter.Buffer`, without storing it in any virtual memory: this will make it not possible for this interpreter to execute self-modifying programs
- the `Runtime` component has the purpose of taking care of all details about the specific virtual machine, like registers, memory, access to the system, etc.; since none of this information is needed by the interpreter, the `Runtime` interface is just a tag interface, and concrete instances will be passed as parameters of the generic components
- the `Loader` component initializes and provides the `Runtime` that will be used by the instructions throughout the execution; in particular it should load the provided execution arguments into it
- the `InstructionSet` component provides all information about how instructions work in the specific runtime; in particular, the instruction set must provide the size of the instruction data that can be used to produce instructions executors, and generate these executors given the opcode
- the instruction size is fixed, and provided by the instruction set
- instructions are decoded always taking only the first byte as opcode
- there is no IP, nor IR registers, instead they're replaced by local variables for simplicity
- jumps and exit instructions are supported
- jumps and exit requests are not modeled with any domain mechanism (registers, etc.), instead a simple `ExecutionResult` object is returned from the instruction execution, contaning this information
- if no exit instruction is provided by the program, the execution will terminate with exit code `0` after reaching the last instruction of the program

## Design

![SLC Interpreter design](slc_interpreter_design.png)

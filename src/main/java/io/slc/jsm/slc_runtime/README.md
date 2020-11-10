# SLC Runtime

## Dependencies

- `slc_interpreter.Runtime`

## Description

This module provides implementations for the `slc_interpreter.Runtime` component, and in particular the `SlcRuntime` concrete runtime, providing a facade for accessing all the concrete virtual machine's components, like registers, memory, etc.
- the runtime supports static memory, stack and heap
- execution arguments should be pushed to the stack during the loading phase
- communication with the system should also be handled, for example working with files, etc., except for process and threads handling, which is instead taken care of by the clients that pass each instruction to the runtime

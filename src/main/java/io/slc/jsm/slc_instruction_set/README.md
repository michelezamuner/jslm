# SLC Instruction Set

## Dependencies

- `slc_runtime.instruction_set.InstructionSet`
- `slc_runtime.instruction_set.Instruction`


## Description

This module provides implementations for the `slc_runtime.instruction_set` port, including a `SlcInstructionSet` component, representing a factory that creates specific instruction handlers, and many different implementations of `Instruction`, like `Syscall`, each representing a specific instruction handler.

## Design

![SLC Instruction Set design](slc_instruction_set_design.png)

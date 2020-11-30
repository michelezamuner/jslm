# Instruction set as factory

## Context

Since we'll have a big number of instructions to implement, we don't want to put all their definitions inside the same class, but we want to be able to easily add and remove different instructions.

want to nicely put each implementation to its own file/class. This means that there will be some lower level, language-dependent construction (i.e. dynamic class instantiation) to be done, that might mess up unit tests a little bit.

## Decision

We will put each instruction implementation into its own file/class. This means that there will be some lower level, language-dependent construction (i.e. dynamic class instantiation) to be done, that might mess up unit tests a little bit.

For this reason, we'll have two elements that take care of instruction creation: an instruction repository will have the responsibility of knowing what are all the instructions available, and their classes, and an instruction factory will contain all the lower level details of how classes are dynamically instantiated in Java.

## Status

Accepted

## Consequences

The repository will contain all the business logic, i.e. knowing what are the instructions available, and as such will have to be unit tested, while the factory will contain no business logic, and will be extremely simple, just containing the instantiation details, and thus won't need any unit testing.

The factory will hardly need to be modified, while the repository will have to be modified every time a new instruction is defined.

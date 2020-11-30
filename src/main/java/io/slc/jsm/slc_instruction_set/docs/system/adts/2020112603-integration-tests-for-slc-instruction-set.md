# Integration tests for SLC Instruction Set

## Context

We want to provide the SLC Instruction Set package with its own set of integration tests, in order for it to be independently developed. However, how to create meaningful integration tests on such a lower-level component?

## Decision

The fact that the SLC Instruction Set package is at a lower level in the hierarchy of dependencies of the virtual machine doesn't mean it must be tested in isolation.

In actuality, the SLC Instruction Set is designed to be a specific implementation of the Instruction Set concept defined in the SLC Interpreter, and furthermore, it's designed to be the specific implementation for the SLC Runtime.

This means that the SLC Instruction Set by design has to know of the existence of the SLC Interpreter, and of the SLC Runtime: thus, we can freely write integration tests using real instances of these components, since there will never be the case that the SLC Instruction Set will use components different than those.

## Status

Accepted

## Consequences

We can write meaningful integration tests using only a limited set of stubs. Actually, the most meaningful integration tests will be written at this level, since it's at this level that we have the most information on what actual concrete components will be used.

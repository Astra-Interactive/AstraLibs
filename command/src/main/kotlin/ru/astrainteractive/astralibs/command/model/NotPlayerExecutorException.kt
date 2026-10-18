package ru.astrainteractive.astralibs.command.model

/** Thrown when a command that requires a player executor is run by a non-player sender. */
public class NotPlayerExecutorException : CommandException("Executor should be player")

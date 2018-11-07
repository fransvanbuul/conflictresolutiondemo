package com.example.conflictresolvedemo.command.impl;

import com.example.conflictresolvedemo.command.api.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.conflictresolution.ConflictResolver;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
@Slf4j
public class Person {

    @AggregateIdentifier
    private UUID id;

    @CommandHandler
    public Person(CreatePersonCmd cmd) {
        log.debug("handling {}", cmd);
        apply(new PersonCreatedEvt(cmd.getId(), cmd.getName(), cmd.getDateOfBirth()));
    }

    @CommandHandler
    public void handle(CorrectNameCmd cmd, ConflictResolver conflictResolver) {
        log.debug("handling {}", cmd);
        /* If NameCorrected events have taken place since the targeted version, that's a conflict.
         * other events like DateOfBirthCorrected are no problem.
         */
        conflictResolver.detectConflicts(
                events -> events.stream().anyMatch(
                        event -> event.getPayloadType().equals(NameCorrectedEvt.class)));
        apply(new NameCorrectedEvt(cmd.getId(), cmd.getName()));
    }

    @CommandHandler
    public void handle(CorrectDateOfBirthCmd cmd, ConflictResolver conflictResolver) {
        log.debug("handling {}", cmd);
        /* If DateOfBirthCorrected events have taken place since the targeted version, that's a conflict.
         * other events like NameCorrected are no problem.
         */
        conflictResolver.detectConflicts(
                events -> events.stream().anyMatch(
                        event -> event.getPayloadType().equals(DateOfBirthCorrectedEvt.class)));
        apply(new DateOfBirthCorrectedEvt(cmd.getId(), cmd.getDateOfBirth()));
    }

    @EventSourcingHandler
    public void on(PersonCreatedEvt evt) {
        id = evt.getId();
    }

}

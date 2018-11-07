package com.example.conflictresolvedemo.command.api;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.modelling.command.TargetAggregateVersion;

import java.time.LocalDate;
import java.util.UUID;

@Value
public class CorrectDateOfBirthCmd {

    @TargetAggregateIdentifier UUID id;
    @TargetAggregateVersion long version;
    LocalDate dateOfBirth;

}

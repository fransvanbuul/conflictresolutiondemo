package com.example.conflictresolvedemo.command.api;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.modelling.command.TargetAggregateVersion;

import java.util.UUID;

@Value
public class NameCorrectedEvt {

    UUID id;
    String name;

}

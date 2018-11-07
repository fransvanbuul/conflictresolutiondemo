package com.example.conflictresolvedemo;

import com.example.conflictresolvedemo.command.api.CorrectDateOfBirthCmd;
import com.example.conflictresolvedemo.command.api.CorrectNameCmd;
import com.example.conflictresolvedemo.command.api.CreatePersonCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.axonserver.connector.command.AxonServerCommandBus;
import org.axonframework.axonserver.connector.command.AxonServerRemoteCommandHandlingException;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class ConflictresolvedemoApplication implements CommandLineRunner {

    private final CommandGateway commandGateway;
    private final EventStore eventStore;

    public static void main(String[] args) {
        SpringApplication.run(ConflictresolvedemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        UUID id = UUID.randomUUID();
        commandGateway.sendAndWait(new CreatePersonCmd(
                id,
                "Franz",
                LocalDate.of(1977, Month.SEPTEMBER, 3)));

        commandGateway.sendAndWait(new CorrectNameCmd(
                id,
                0L,
                "Frans"));

        commandGateway.sendAndWait(new CorrectDateOfBirthCmd(
                id,
                0L,
                LocalDate.of(1977, Month.MARCH, 9)));

        try {
            commandGateway.sendAndWait(new CorrectNameCmd(
                    id,
                    0L,
                    "Franciscus"));
        } catch(AxonServerRemoteCommandHandlingException ex) {
            log.debug("got exception processing cmd: {}", ex.toString());
            log.debug("exception descriptions: {}", ex.getExceptionDescriptions());
        }
    }

}

package org.plantict.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets", schema = "amusementparkdb")
@Data
@NoArgsConstructor
public class TicketEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_ticket", nullable = false)
    private UUID idTicket;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_attraction")
    private AttractionEntity attraction;

    @NotNull
    @Column(name = "number")
    private Long number;

    @Column(name = "emission_date")
    @NotNull
    private LocalDateTime emissionDate;

    @Column(name = "expiration_date")
    @NotNull
    private LocalDateTime expirationDate;

    @Column(name = "calling_date")
    private LocalDateTime callingDate;
}

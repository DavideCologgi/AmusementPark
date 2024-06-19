package org.plantict.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "attractions", schema = "amusementparkdb")
@Data
@NoArgsConstructor
public class AttractionEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_attraction", nullable = false)
    private UUID idAttraction;

    @NotNull
    @Column(name = "name")
    @Size(max = 255)
    private String name;

    @NotNull
    @Column(name = "description")
    @Size(max = 255)
    private String description;

    @NotNull
    @Column(name = "ticket_duration")
    private Integer duration;

    @NotNull
    @Column(name = "max_ticket")
    private Integer maxTicket;

    @OneToMany(mappedBy = "attraction")
    private List<TicketEntity> tickets;
}

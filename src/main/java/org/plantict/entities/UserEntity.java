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
@Table(name = "users", schema = "amusementparkdb")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id_user", nullable = false)
    private UUID idUser;

    @NotNull
    @Column(name = "name")
    @Size(max = 50)
    private String name;

    @NotNull
    @Column(name = "email")
    @Size(max = 50)
    private String email;

    @OneToMany(mappedBy = "user")
    private List<TicketEntity> tickets;
}

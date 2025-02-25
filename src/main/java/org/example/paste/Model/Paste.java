package org.example.paste.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Paste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String data;
    private String hash;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime accessTime;
}

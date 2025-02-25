package org.example.paste.repository;

import org.example.paste.Model.Paste;
import org.example.paste.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PasteRepository extends JpaRepository<Paste, Long> {
    Paste getPasteByHash(String hash);
    List<Paste> findTop10ByStatusAndAccessTimeAfterOrderByIdDesc(Status status, LocalDateTime now);
}

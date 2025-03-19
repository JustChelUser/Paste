package org.example.paste.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.paste.Model.User;
import org.example.paste.dto.PasteDtoRequest;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.dto.PasteDtoResponseUrl;
import org.example.paste.service.PasteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/paste")
@RequiredArgsConstructor
@Validated
public class PasteController {

    private final PasteService pasteService;

    @GetMapping()
    public List<PasteDtoResponse> getPublicPasteList() {
        return pasteService.getPublicPastes();
    }

    @GetMapping("/{hash}")
    public PasteDtoResponse getPasteByHash(@PathVariable String hash) {
        return pasteService.getPasteByHash(hash);
    }

    @PostMapping()
    public PasteDtoResponseUrl createPaste(@Valid @RequestBody PasteDtoRequest dtoRequest,
                                           @AuthenticationPrincipal User user) {
        return pasteService.createPaste(dtoRequest, user);
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public PasteDtoResponseUrl updatePaste(@PathVariable String id,
                                           @Valid @RequestBody PasteDtoRequest dtoRequest,
                                           @AuthenticationPrincipal User user) {
        return pasteService.updatePaste(dtoRequest, user, id);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePaste(@PathVariable String id,
                                            @AuthenticationPrincipal User user) {
        pasteService.deletePaste(user, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/pastes")
    @PreAuthorize("isAuthenticated()")
    public List<PasteDtoResponse> getPastes(@AuthenticationPrincipal User user) {
        return pasteService.getPastes(user);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public PasteDtoResponse getUserPaste(@PathVariable String id,
                                         @AuthenticationPrincipal User user) {
        return pasteService.getPaste(user, id);
    }
}

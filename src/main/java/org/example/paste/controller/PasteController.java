package org.example.paste.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.paste.dto.PasteDtoRequest;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.dto.PasteDtoResponseUrl;
import org.example.paste.service.PasteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class PasteController {

    private final PasteService pasteService;

    @GetMapping("/")
    public List<PasteDtoResponse> getPublicPasteList() {
        return pasteService.getPublicPastes();
    }

    @GetMapping("/{hash}")
    public PasteDtoResponse getPasteByHash(@PathVariable String hash) {
        return pasteService.getPasteByHash(hash);
    }

    @PostMapping("/")
    public PasteDtoResponseUrl createPaste(@Valid @RequestBody PasteDtoRequest dtoRequest) {
        return pasteService.createPaste(dtoRequest);
    }
}

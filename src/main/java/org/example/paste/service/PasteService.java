package org.example.paste.service;

import org.example.paste.dto.PasteDtoRequest;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.dto.PasteDtoResponseUrl;

import java.util.List;

public interface PasteService {
    PasteDtoResponse getPasteByHash(String hash);
    List<PasteDtoResponse> getPublicPastes();
    PasteDtoResponseUrl createPaste(PasteDtoRequest pasteDtoRequest);
}

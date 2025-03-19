package org.example.paste.service;

import org.example.paste.Model.User;
import org.example.paste.dto.PasteDtoRequest;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.dto.PasteDtoResponseUrl;

import java.util.List;

public interface PasteService {
    PasteDtoResponse getPasteByHash(String hash);

    List<PasteDtoResponse> getPublicPastes();

    PasteDtoResponseUrl createPaste(PasteDtoRequest pasteDtoRequest, User user);

    PasteDtoResponseUrl updatePaste(PasteDtoRequest pasteDtoRequest, User user, String id);

    void deletePaste(User user, String id);

    List<PasteDtoResponse> getPastes(User user);

    PasteDtoResponse getPaste(User user, String id);

}

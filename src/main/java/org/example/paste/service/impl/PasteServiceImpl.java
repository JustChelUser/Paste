package org.example.paste.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import org.example.paste.Model.Paste;
import org.example.paste.Model.Status;
import org.example.paste.dto.PasteDtoRequest;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.dto.PasteDtoResponseUrl;
import org.example.paste.exception.PasteNotFoundException;
import org.example.paste.exception.PasteTimeLimitExceeded;
import org.example.paste.repository.PasteRepository;
import org.example.paste.service.PasteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasteServiceImpl implements PasteService {

    private final PasteRepository repository;

    @Value("${spring.host}")
    private String host;

    @Override
    public PasteDtoResponse getPasteByHash(String hash) {
        Paste paste = repository.getPasteByHash(hash);
        if (paste == null) {
            throw new PasteNotFoundException("Paste not found with hash: " + hash);
        }
        LocalDateTime now = LocalDateTime.now();
        if (paste.getAccessTime().isBefore(now)) {
            throw new PasteTimeLimitExceeded("Time limit for past with hash: " + hash + " exceeded");
        }
        return new PasteDtoResponse(paste.getData(), paste.getStatus());
    }

    @Override
    public List<PasteDtoResponse> getPublicPastes() {
        LocalDateTime now = LocalDateTime.now();
        List<Paste> pastes = repository.findTop10ByStatusAndAccessTimeAfterOrderByIdDesc(Status.PUBLIC, now);
        return pastes.stream().map(paste -> new PasteDtoResponse(paste.getData(), paste.getStatus())).toList();
    }

    @Override
    public PasteDtoResponseUrl createPaste(PasteDtoRequest pasteDtoRequest) {
        Paste paste = new Paste();
        paste.setData(pasteDtoRequest.getData());
        paste.setHash(NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 10));
        paste.setStatus(pasteDtoRequest.getStatus());
        paste.setAccessTime(LocalDateTime.now().plusSeconds(pasteDtoRequest.getTimeToLiveSeconds()));
        repository.save(paste);
        return new PasteDtoResponseUrl(host + "/" + paste.getHash());
    }
}

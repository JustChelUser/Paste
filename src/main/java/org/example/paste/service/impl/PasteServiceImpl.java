package org.example.paste.service.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import org.example.paste.Model.Paste;
import org.example.paste.Model.Status;
import org.example.paste.Model.User;
import org.example.paste.dto.PasteDtoRequest;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.dto.PasteDtoResponseUrl;
import org.example.paste.exception.InvalidPasteIdException;
import org.example.paste.exception.PasteNotFoundException;
import org.example.paste.exception.PasteTimeLimitExceeded;
import org.example.paste.repository.PasteRepository;
import org.example.paste.service.PasteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasteServiceImpl implements PasteService {

    private static final Logger logger = LoggerFactory.getLogger(PasteServiceImpl.class);

    private final PasteRepository repository;

    @Value("${spring.hostForUrl}")
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
        logger.info("Paste was requested : id = {}, hash = {}, status = {}", paste.getId(), paste.getHash(), paste.getStatus());
        return new PasteDtoResponse(paste.getData(), paste.getStatus(), paste.getId(), paste.getHash(),
                paste.getUser() != null ? paste.getUser().getUsername() : null,
                paste.getUser() != null ? paste.getUser().getId() : null);
    }

    @Override
    public List<PasteDtoResponse> getPublicPastes() {
        LocalDateTime now = LocalDateTime.now();
        List<Paste> pastes = repository.findTop10ByStatusAndAccessTimeAfterOrderByIdDesc(Status.PUBLIC, now);
        logger.info("Last 10 public pastes was requested");
        return pastes.stream().map(paste -> new PasteDtoResponse
                (paste.getData(), paste.getStatus(), paste.getId(), paste.getHash(),
                        paste.getUser() != null ? paste.getUser().getUsername() : null,
                        paste.getUser() != null ? paste.getUser().getId() : null)).toList();
    }

    @Override
    public PasteDtoResponseUrl createPaste(PasteDtoRequest pasteDtoRequest, User user) {
        LocalDateTime now = LocalDateTime.now();
        Paste paste = new Paste();
        paste = fillPasteObject(paste, pasteDtoRequest, user);
        repository.save(paste);
        logger.info("Paste created : id = {}, hash = {}, status = {}", paste.getId(), paste.getHash(), paste.getStatus());
        return new PasteDtoResponseUrl(host + "/" + paste.getHash());
    }

    @Override
    public PasteDtoResponseUrl updatePaste(PasteDtoRequest pasteDtoRequest, User user, String id) {
        long pasteId = tryParsePasteId(id);
        Paste paste = checkPasteOwnership(pasteId, user);
        paste = fillPasteObject(paste, pasteDtoRequest, user);
        repository.save(paste);
        logger.info("Paste updated : id = {}, hash = {}, status = {}", paste.getId(), paste.getHash(), paste.getStatus());
        return new PasteDtoResponseUrl(host + "/" + paste.getHash());
    }

    @Override
    public void deletePaste(User user, String id) {
        long pasteId = tryParsePasteId(id);
        Paste paste = checkPasteOwnership(pasteId, user);
        repository.delete(paste);
        logger.info("Paste deleted : id = {}", pasteId);
    }

    @Override
    public List<PasteDtoResponse> getPastes(User user) {
        logger.info("User's pastes requested : id = {}", user.getId());
        List<Paste> pastes = repository.getPastesByUser(user);
        return pastes.stream().map(
                paste -> new PasteDtoResponse(
                        paste.getData(),
                        paste.getStatus(),
                        paste.getId(),
                        paste.getHash(),
                        paste.getUser() != null ? paste.getUser().getUsername() : null,
                        paste.getUser() != null ? paste.getUser().getId() : null
                )).toList();
    }

    @Override
    public PasteDtoResponse getPaste(User user, String id) {
        long pasteId = tryParsePasteId(id);
        Paste paste = checkPasteOwnership(pasteId, user);
        logger.info("Paste was requested : id = {}, hash = {}, status = {}", paste.getId(), paste.getHash(), paste.getStatus());
        return new PasteDtoResponse(paste.getData(), paste.getStatus(), paste.getId(), paste.getHash(),
                paste.getUser() != null ? paste.getUser().getUsername() : null,
                paste.getUser() != null ? paste.getUser().getId() : null);
    }

    private long tryParsePasteId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new InvalidPasteIdException("Paste id is not a number");
        }
    }

    private Paste checkPasteOwnership(long pasteId, User user) {
        Paste paste = repository.findById(pasteId).orElseThrow(() ->
                new PasteNotFoundException("Paste not found with id: " + pasteId));
        if (paste.getUser() == null || paste.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("You can manage only your own pastes");
        }
        return paste;
    }

    private Paste fillPasteObject(Paste paste, PasteDtoRequest pasteDtoRequest, User user) {
        LocalDateTime now = LocalDateTime.now();
        paste.setData(pasteDtoRequest.getData());
        paste.setHash(NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 10));
        paste.setStatus(pasteDtoRequest.getStatus());
        paste.setAccessTime(now.plusSeconds(pasteDtoRequest.getTimeToLiveSeconds()));
        if (user != null) {
            paste.setUser(user);
        }
        return paste;
    }
}

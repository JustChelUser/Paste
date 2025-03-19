package org.example.paste.service.impl;

import org.example.paste.Model.Paste;
import org.example.paste.Model.Status;
import org.example.paste.Model.User;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.repository.PasteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasteServiceImplTest {

    @Mock
    private PasteRepository pasteRepository;

    @InjectMocks
    private PasteServiceImpl pasteService;

    @Test
    public void getPublicPastes() {
        LocalDateTime now = LocalDateTime.now();
        String hash1 = "hash$asdv2", hash2 = "hash2c2c";
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");

        Paste paste1 = new Paste(1L, "data1", hash1, Status.PUBLIC, now.plusSeconds(200), user1);
        Paste paste2 = new Paste(2L, "data2", hash2, Status.PUBLIC, now.plusSeconds(250), user2);
        List<Paste> pastes = List.of(paste1, paste2);

        PasteDtoResponse expectedResponse1 = new PasteDtoResponse("data1", Status.PUBLIC, 1L, hash1, user1.getUsername(), user1.getId());
        PasteDtoResponse expectedResponse2 = new PasteDtoResponse("data2", Status.PUBLIC, 2L, hash2, user2.getUsername(), user2.getId());
        List<PasteDtoResponse> expectedResponses = List.of(expectedResponse1, expectedResponse2);

        when(pasteRepository.findTop10ByStatusAndAccessTimeAfterOrderByIdDesc(eq(Status.PUBLIC), any(LocalDateTime.class)))
                .thenReturn(pastes);
        List<PasteDtoResponse> result = pasteService.getPublicPastes();
        assertNotNull(result);
        assertEquals(expectedResponses, result);
        verify(pasteRepository, times(1)).
                findTop10ByStatusAndAccessTimeAfterOrderByIdDesc(eq(Status.PUBLIC), any(LocalDateTime.class));

    }
}


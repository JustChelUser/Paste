package org.example.paste.service.impl;

import org.example.paste.Model.Paste;
import org.example.paste.Model.Status;
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
        Paste paste1 = new Paste(1L, "data1", "hash1c1c", Status.PUBLIC, now.plusSeconds(200));
        Paste paste2 = new Paste(2L, "data2", "hash2c2c", Status.PUBLIC, now.plusSeconds(250));
        List<Paste> pastes = List.of(paste1, paste2);

        PasteDtoResponse expectedResponse1 = new PasteDtoResponse("data1", Status.PUBLIC);
        PasteDtoResponse expectedResponse2 = new PasteDtoResponse("data2", Status.PUBLIC);
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


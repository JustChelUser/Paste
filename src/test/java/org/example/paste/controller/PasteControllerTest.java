package org.example.paste.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.paste.Model.Status;
import org.example.paste.dto.PasteDtoRequest;
import org.example.paste.dto.PasteDtoResponse;
import org.example.paste.dto.PasteDtoResponseUrl;
import org.example.paste.exception.PasteNotFoundException;
import org.example.paste.exception.PasteTimeLimitExceeded;
import org.example.paste.service.PasteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(PasteController.class)
public class PasteControllerTest {

    @MockitoBean
    private PasteService pasteService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPaste() throws Exception {
        PasteDtoRequest request = new PasteDtoRequest("textExample", Status.PUBLIC, 100);
        String requestJson = objectMapper.writeValueAsString(request);
        PasteDtoResponseUrl expectedResponse = new PasteDtoResponseUrl("http://paste.ru/ZfVnrG5pBZ");

        when(pasteService.createPaste(request)).thenReturn(expectedResponse);
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(expectedResponse.getUrl()));
        verify(pasteService, times(1)).createPaste(request);
    }

    @Test
    void createPasteWithInvalidTime() throws Exception {
        PasteDtoRequest requestInvalidTime = new PasteDtoRequest("textExample", Status.PUBLIC, 0);
        String requestJson = objectMapper.writeValueAsString(requestInvalidTime);
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
        verify(pasteService, times(0)).createPaste(requestInvalidTime);
    }

    @Test
    void getPasteByHash() throws Exception {
        String hash = "hash$asdv2";
        PasteDtoResponse expectedResponse = new PasteDtoResponse("someData", Status.PUBLIC);
        when(pasteService.getPasteByHash(hash)).thenReturn(expectedResponse);

        mockMvc.perform(get("/{hash}", hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedResponse.getData()))
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus().toString()));
        verify(pasteService, times(1)).getPasteByHash(hash);
    }

    @Test
    void getPasteByHash_NotFound() throws Exception {
        String hash = "hash$asdv2";
        PasteNotFoundException expectedException = new PasteNotFoundException("Paste not found with hash: " + hash);
        when(pasteService.getPasteByHash(hash)).thenThrow(expectedException);
        mockMvc.perform(get("/{hash}", hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
        verify(pasteService, times(1)).getPasteByHash(hash);
    }
    @Test
    void getPasteByHash_TimeLimitExceeded() throws Exception {
        String hash = "hash$asdv2";
        PasteTimeLimitExceeded expectedException = new PasteTimeLimitExceeded("Paste not found with hash: " + hash);
        when(pasteService.getPasteByHash(hash)).thenThrow(expectedException);
        mockMvc.perform(get("/{hash}", hash)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedException.getMessage()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
        verify(pasteService, times(1)).getPasteByHash(hash);
    }
}

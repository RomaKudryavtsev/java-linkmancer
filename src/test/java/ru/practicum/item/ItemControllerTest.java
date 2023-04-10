package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.item.request_tags.TagsRequest;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private ItemServiceImpl itemService;
    @InjectMocks
    private ItemController controller;
    private MockMvc mvc;
    @Mock
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        itemDto = new ItemDto();
        itemDto.setUrl("https://practicum.yandex.ru/java-developer/");
        itemDto.setTags(Set.of("Education", "IT", "Java"));
    }

    @Test
    void saveItem() throws Exception {
        when(itemService.addNewItem(any(), any())).thenReturn(itemDto);
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Later-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url", is(itemDto.getUrl())));
    }

    @Test
    void updateItem() throws Exception {
        itemDto.setTags(Set.of("Yandex"));
        TagsRequest tagsRequest = new TagsRequest();
        tagsRequest.setTags(Set.of("Yandex"));
        when(itemService.modifyItem(any())).thenReturn(itemDto);
        mvc.perform(patch("/items/{id}/modify", 1L)
                        .content(mapper.writeValueAsString(tagsRequest))
                        .header("X-Later-User-Id", 1L)
                        .param("replace", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags", Matchers.hasItem("Yandex")))
                .andExpect(jsonPath("$.tags", hasSize(1)));
    }

    @Test
    void deleteItem() throws Exception {
        doNothing().when(itemService).deleteById(1L, 1L);
        mvc.perform(delete("/items/{id}/delete", 1L)
                        .header("X-Later-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted"));
        verify(itemService, times(1)).deleteById(1L, 1L);
    }
}


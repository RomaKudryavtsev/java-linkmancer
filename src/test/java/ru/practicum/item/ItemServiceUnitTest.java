package ru.practicum.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.item.url_retriever.UrlMetadataRetriever;
import ru.practicum.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ItemServiceUnitTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UrlMetadataRetriever retriever;
    @InjectMocks
    ItemServiceImpl itemService;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void saveItem() {

    }

    @Test
    public void searchItem() {

    }

    @Test
    public void updateItem() {

    }

    @Test
    public void deleteItem() {

    }
}

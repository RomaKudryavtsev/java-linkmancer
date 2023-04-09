package ru.practicum.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import javax.persistence.Tuple;

@Component
@ReadingConverter
@WritingConverter
@Slf4j
public class ItemCountByUserConverter implements Converter<Tuple, ItemCountByUser> {

    @Override
    public ItemCountByUser convert(Tuple source) {
        log.info("Conversion is enabled");
        Long userId = source.get(0, Long.class);
        Long count = source.get(1, Long.class);
        return new ItemCountByUser(userId, count);
    }
}

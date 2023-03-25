package ru.practicum.item.request_dates;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DatesRequest {
    private String from;
    private String to;
}

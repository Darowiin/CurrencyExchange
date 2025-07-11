package com.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencyDto {
    Long id;

    String code;
    String name;
    String sign;
}

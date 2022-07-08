package com.peer39.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor

public class UrlDto {
    String inputUrl;

    @Override
    public String toString() {
        return "\n inputUrl='" + inputUrl + '\'' +
                '}';
    }
}

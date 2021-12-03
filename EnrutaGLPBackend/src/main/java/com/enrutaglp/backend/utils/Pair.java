package com.enrutaglp.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair<T1, T2> {
    private final T1 valor1;
    private final T2 valor2;
}
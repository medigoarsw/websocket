package edu.escuelaing.arsw.medigo.handler.dto;

import java.math.BigDecimal;

public record PlaceBidRequest(
    Long userId,
    String userName,
    BigDecimal amount
) {}

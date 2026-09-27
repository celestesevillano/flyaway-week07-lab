package com.utec.flyaway.dto;

// Formato exacto exigido por la misión 3: { "token": "<jwt>" }
public record LoginResponse(String token) {
}

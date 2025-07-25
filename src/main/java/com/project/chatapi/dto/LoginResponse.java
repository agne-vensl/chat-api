package com.project.chatapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
  @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0ZXIzIiwicm9sZSI6IlVTRVIiLCJwdWJsaWNJZCI6IjkxNjdmNjMyLTZmMTEtNDY5Mi1iOWZlLTMwOWJhMDc0ZjViOCIsImlhdCI6MTc1MzI2Njk4NywiZXhwIjoxNzUzMjcwNTg3fQ.AwipfOsR3ln6wn9d65BMMcmw8_zWlYuPJeLI0N_JVly65E3tWaESvmjl2KaNrjBr_4BIEiqbJ-OycU2OokTqew") String token
) {}

package com.project.chatapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatapi.dto.UserStatisticsResponse;
import com.project.chatapi.service.StatisticsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
  private StatisticsService statisticsService;

  public AdminController(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }
  
  @GetMapping("/statistics/{publicId}")
  public UserStatisticsResponse getMethodName(@PathVariable("publicId") UUID publicId) {
    return statisticsService.getUserStatistics(publicId);
  }
}

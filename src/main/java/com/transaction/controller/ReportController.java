package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.PnLReportDTO;
import com.transaction.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/pnl")
    public ResponseEntity<ApiResponse<PnLReportDTO>> getPnLReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String groupBy) {

        log.info("Generating PnL report: from={}, to={}, start={}, end={}, groupBy={}", from, to, start, end, groupBy);
        PnLReportDTO report = reportService.getPnLReport(from, to, start, end, groupBy);
        return ResponseEntity.ok(new ApiResponse<>("success", "PnL report generated successfully", report));
    }
}

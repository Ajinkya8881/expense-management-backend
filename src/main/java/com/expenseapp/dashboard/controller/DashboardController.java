package com.expenseapp.dashboard.controller;


import com.expenseapp.common.response.ApiResponse;
import com.expenseapp.dashboard.dto.DashboardResponse;
import com.expenseapp.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails
    ) {
        DashboardResponse response =
                dashboardService.getDashboard(
                        userDetails.getUsername(),
                        year,
                        month
                );

        return ApiResponse.<DashboardResponse>builder()
                .success(true)
                .message("Dashboard fetched successfully")
                .data(response)
                .build();

    }
}

package org.example.conge.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.conge.Services.ILeaveStatisticsService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/leaveStatistics")
public class LeaveStatisticsController {
    ILeaveStatisticsService leaveStatisticsService;
/*
    @Autowired
    public LeaveStatisticsController(ILeaveStatisticsService leaveStatisticsService) {
        this.leaveStatisticsService = leaveStatisticsService;
    }

    @PutMapping("/calculate")
    public LeaveStatistics calculateTotalDaysTaken() {
        return leaveStatisticsService.calculateAndUpdateTotalDaysTaken();
    }

    @GetMapping("/total")
    public LeaveStatistics getTotalDaysTaken() {
        return leaveStatisticsService.getTotalDaysTaken();
    }
*/
}

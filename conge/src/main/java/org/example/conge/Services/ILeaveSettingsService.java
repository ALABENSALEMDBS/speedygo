package org.example.conge.Services;

import org.example.conge.entities.LeaveSettings;

public interface ILeaveSettingsService {
    public LeaveSettings getSettings();
    public LeaveSettings updateMaxAllowedDays(int days);
}

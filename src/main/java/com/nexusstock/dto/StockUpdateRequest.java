package com.nexusstock.dto;

import com.nexusstock.entity.InventoryLog.ActionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequest {

    @NotNull(message = "Change amount is required")
    private Integer changeAmount;

    @NotNull(message = "Action type is required")
    private ActionType actionType;
}

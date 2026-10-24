package com.transaction.dto.projection;

import java.math.BigDecimal;

public interface PeriodAggregateProjection {
    String getPeriod();
    BigDecimal getTotal();
    Long getCount();
}

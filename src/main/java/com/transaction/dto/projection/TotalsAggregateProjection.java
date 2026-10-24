package com.transaction.dto.projection;

import java.math.BigDecimal;

public interface TotalsAggregateProjection {
    BigDecimal getTotal();
    Long getCount();
    BigDecimal getCash();
    BigDecimal getUpi();
}

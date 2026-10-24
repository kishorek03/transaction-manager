package com.transaction.dto.projection;

import java.math.BigDecimal;

public interface CategoryAggregateProjection {
    Long getCategoryId();
    String getCategoryName();
    BigDecimal getTotal();
}

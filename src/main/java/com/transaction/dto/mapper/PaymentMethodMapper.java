package com.transaction.dto.mapper;

import com.transaction.entity.PaymentMethod;
import com.transaction.dto.PaymentMethodDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodDTO toDto(PaymentMethod paymentMethod);

    PaymentMethod toEntity(PaymentMethodDTO paymentMethodDTO);
}

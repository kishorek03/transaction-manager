package com.transaction.service;

import com.transaction.entity.Sales;
import com.transaction.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    public Sales save(Sales sale) {
        return salesRepository.save(sale);
    }

    public List<Sales> findAll() {
        return salesRepository.findAll();
    }

    public Sales findById(Long id) {
        return salesRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    public Sales update(Long id, Sales updatedSale) {
        Sales sale = findById(id);
        sale.setProduct(updatedSale.getProduct());
        sale.setQuantity(updatedSale.getQuantity());
        sale.setUnitPrice(updatedSale.getUnitPrice());
        sale.setAmount(updatedSale.getAmount());
        sale.setPaymentMethod(updatedSale.getPaymentMethod());
        sale.setParcel(updatedSale.getParcel());
        sale.setDate(updatedSale.getDate());
        return salesRepository.save(sale);
    }

    public void delete(Long id) {
        salesRepository.deleteById(id);
    }
}

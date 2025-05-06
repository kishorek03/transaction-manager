package com.transaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., Sugarcane Juice
    private BigDecimal basePrice; // Basic product price

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit; // Linked to Unit table (e.g., ml, liter, piece)

    private BigDecimal unitPrice; // Price per unit

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flavour> flavours;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddOn> addOns;

    private BigDecimal parcelPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<Flavour> getFlavours() {
        return flavours;
    }

    public void setFlavours(List<Flavour> flavours) {
        this.flavours = flavours;
    }

    public List<AddOn> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<AddOn> addOns) {
        this.addOns = addOns;
    }

    public BigDecimal getParcelPrice() {
        return parcelPrice;
    }

    public void setParcelPrice(BigDecimal parcelPrice) {
        this.parcelPrice = parcelPrice;
    }
}

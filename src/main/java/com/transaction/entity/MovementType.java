package com.transaction.entity;

public enum MovementType {
    IN,       // Cash added manually
    OUT,      // Cash removed manually
    ADJUSTMENT, // Corrections
    TRANSFER   // Transfer between cash sources
}
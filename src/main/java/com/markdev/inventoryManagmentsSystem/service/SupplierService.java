package com.markdev.inventoryManagmentsSystem.service;


import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.dto.SupplierDto;

public interface SupplierService {

    Response createSupplier(SupplierDto supplierDto);
    Response getSuppliers();
    Response getSupplierById(Long id);
    Response updateSupplier(Long id,SupplierDto supplierDto);
    Response deleteSupplier(Long id);

}

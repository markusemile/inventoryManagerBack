package com.markdev.inventoryManagmentsSystem.controller;

import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.dto.SupplierDto;
import com.markdev.inventoryManagmentsSystem.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/supplier")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllSupplier(){
        return ResponseEntity.ok(supplierService.getSuppliers());
    }

    @PostMapping("/new")
    public ResponseEntity<Response> addNewSupplier(@RequestBody @Valid SupplierDto supplierToSave){
        return ResponseEntity.ok(supplierService.createSupplier(supplierToSave));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateCategory(@PathVariable Long id, @RequestBody SupplierDto supplierToUpdate){
        return ResponseEntity.ok(supplierService.updateSupplier(id,supplierToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteSupplier(@PathVariable Long id){
        return ResponseEntity.ok(supplierService.deleteSupplier(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getSupplierById(@PathVariable Long id){
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }


}

package com.markdev.inventoryManagmentsSystem.service.impl;

import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.dto.SupplierDto;
import com.markdev.inventoryManagmentsSystem.entity.Supplier;
import com.markdev.inventoryManagmentsSystem.exceptions.NotFoundException;
import com.markdev.inventoryManagmentsSystem.repository.SupplierRepository;
import com.markdev.inventoryManagmentsSystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response createSupplier(SupplierDto supplierDto) {
        Supplier supplierToSave = modelMapper.map(supplierDto, Supplier.class);
        supplierRepository.save(supplierToSave);
        return Response.builder()
                .status(200)
                .message("New Supplier successfully saved")
                .build();
    }

    @Override
    public Response getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierDto> supplierDtoList = modelMapper.map(suppliers, new TypeToken<List<SupplierDto>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .suppliers(supplierDtoList)
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Supplier id:"+id+" not found !"));
        SupplierDto supplierDto = modelMapper.map(existingSupplier,SupplierDto.class);

        return Response.builder()
                .status(200)
                .message("success")
                .supplier(supplierDto)
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDto supplierDto) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Supplier id "+id+" not found !"));

        if(supplierDto.getName() != null) existingSupplier.setName(supplierDto.getName());
        if(supplierDto.getAddress() != null) existingSupplier.setAddress(supplierDto.getAddress());

        supplierDto = modelMapper.map(existingSupplier,SupplierDto.class);

        supplierRepository.save(existingSupplier);

        return Response.builder()
                .status(200)
                .message("Supplier updated successfully")
                .supplier(supplierDto)
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {

        supplierRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Supplier id "+id+" not found !"));

        supplierRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("Supplier deleted successfully")
                .build();
    }
}

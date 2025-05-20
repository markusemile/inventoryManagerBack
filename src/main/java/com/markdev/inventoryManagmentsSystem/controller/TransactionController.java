package com.markdev.inventoryManagmentsSystem.controller;

import com.markdev.inventoryManagmentsSystem.dto.CategoryDto;
import com.markdev.inventoryManagmentsSystem.dto.Response;
import com.markdev.inventoryManagmentsSystem.dto.TransactionRequest;
import com.markdev.inventoryManagmentsSystem.enums.TransactionStatus;
import com.markdev.inventoryManagmentsSystem.service.CategoryService;
import com.markdev.inventoryManagmentsSystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/purchase")
    public ResponseEntity purchaseStock(@RequestBody @Valid TransactionRequest transaction){
        return ResponseEntity.ok(transactionService.restockInventory(transaction));
    }
    @PostMapping("/sell")
    public ResponseEntity sellProduct(@RequestBody @Valid TransactionRequest transaction){
        return ResponseEntity.ok(transactionService.sell(transaction));
    }
    @PostMapping("/return")
    public ResponseEntity ReturnProduct(@RequestBody @Valid TransactionRequest transaction){
        return ResponseEntity.ok(transactionService.returnToSupplier(transaction));
    }
    @GetMapping("/all")
    public ResponseEntity getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1000") int size,
            @RequestParam(required = false) String textToSearch)
    {
        return ResponseEntity.ok(transactionService.getAllTransaction(page,size, textToSearch));
    }

    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable Long id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/by-date")
    public ResponseEntity getAllTransactionsByDate(
            @RequestParam int month,
            @RequestParam int year)
    {
        return ResponseEntity.ok(transactionService.getAllTransactionByMonthAndYear(month,year));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateTransactionById(
            @PathVariable Long id,
            @RequestBody @Valid TransactionStatus transactionStatus){
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id,transactionStatus));
    }










}

package com.markdev.inventoryManagmentsSystem.service;


import com.markdev.inventoryManagmentsSystem.dto.*;
import com.markdev.inventoryManagmentsSystem.enums.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

public interface TransactionService {
    Response restockInventory(TransactionRequest transactionRequest);
    Response sell(TransactionRequest transactionRequest);
    Response returnToSupplier(TransactionRequest transactionRequest);
    Response getAllTransaction(int page,int size, String searchText);
    Response getTransactionById(Long id);
    Response getAllTransactionByMonthAndYear(int mont,int year);
    Response updateTransactionStatus  (Long transaction, TransactionStatus transactionStatus);



}

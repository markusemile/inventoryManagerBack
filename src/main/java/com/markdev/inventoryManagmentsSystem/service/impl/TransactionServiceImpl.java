package com.markdev.inventoryManagmentsSystem.service.impl;

import com.markdev.inventoryManagmentsSystem.dto.*;
import com.markdev.inventoryManagmentsSystem.entity.Product;
import com.markdev.inventoryManagmentsSystem.entity.Supplier;
import com.markdev.inventoryManagmentsSystem.entity.Transaction;
import com.markdev.inventoryManagmentsSystem.entity.User;
import com.markdev.inventoryManagmentsSystem.enums.TransactionStatus;
import com.markdev.inventoryManagmentsSystem.enums.TransactionType;
import com.markdev.inventoryManagmentsSystem.exceptions.NameValueRequiredException;
import com.markdev.inventoryManagmentsSystem.exceptions.NotFoundException;
import com.markdev.inventoryManagmentsSystem.repository.ProductRepository;
import com.markdev.inventoryManagmentsSystem.repository.SupplierRepository;
import com.markdev.inventoryManagmentsSystem.repository.TransactionRepository;
import com.markdev.inventoryManagmentsSystem.service.TransactionService;
import com.markdev.inventoryManagmentsSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    public final TransactionRepository transactionRepository;
    public final ModelMapper modelMapper;
    public final SupplierRepository supplierRepository;
    public final ProductRepository productRepository;
    public final UserService userService;


    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {
        Long productionId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if(supplierId==null) throw new NameValueRequiredException("Supplier Id is required");

        Product product = productRepository.findById(productionId)
                .orElseThrow(()->new NotFoundException("Product not found"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found"));

        User user = userService.getCurrentLoggedInUser();

        // update the quantity and re-save
        product.setStockQuantity(product.getStockQuantity()+quantity);
        productRepository.save(product);

        // create Transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .supplier(supplier)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description((transactionRequest.getDescription()))
                .build();
        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Transaction made successfully")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productionId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();


        Product product = productRepository.findById(productionId)
                .orElseThrow(()->new NotFoundException("Product not found"));

        User user = userService.getCurrentLoggedInUser();

        // update the quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // create Transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description((transactionRequest.getDescription()))
                .build();
        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Transaction sold successfully")
                .build();
    }

    /** @noinspection DuplicatedCode*/
    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {

        Long productionId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if(supplierId==null) throw new NameValueRequiredException("Supplier Id is required");

        Product product = productRepository.findById(productionId)
                .orElseThrow(()->new NotFoundException("Product not found"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(()->new NotFoundException("Supplier not found"));

        User user = userService.getCurrentLoggedInUser();

        // update the quantity and re-save
        product.setStockQuantity(product.getStockQuantity()-quantity);
        productRepository.save(product);

        // create Transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .supplier(supplier)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description((transactionRequest.getDescription()))
                .build();
        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Transaction returned successfully initialized")
                .build();
    }

    @Override
    public Response getAllTransaction(int page, int size, String searchText) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepository.searchTransaction(searchText,pageable);

        List<TransactionDto>  transactionDTOS = modelMapper.map(transactionPage.getContent(), new TypeToken<List<TransactionDto>>() {}.getType());

        transactionDTOS.forEach(transactionDtoItem -> {
            transactionDtoItem.setUser(null);
            transactionDtoItem.setProduct(null);
            transactionDtoItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();

    }

    @Override
    public Response getTransactionById(Long id) {

        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Transaction id:"+id+" not found"));

        TransactionDto transactionDto = modelMapper.map(existingTransaction,TransactionDto.class);
        transactionDto.getUser().setTransactions(null);

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDto)
                .build();
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {

        List<Transaction> transactionList = transactionRepository.findAllByMonthAndYear(month,year);

        List<TransactionDto>  transactionListDto = modelMapper.map(transactionList, new TypeToken<List<TransactionDto>>() {}.getType());

        transactionListDto.forEach(transactionDtoItem -> {
            transactionDtoItem.setUser(null);
            transactionDtoItem.setProduct(null);
            transactionDtoItem.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionListDto)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long transaction, TransactionStatus transactionStatus) {

        Transaction existingTransaction = transactionRepository.findById(transaction)
                .orElseThrow(()->new NotFoundException("Transaction id:"+transaction+" not found"));

        existingTransaction.setStatus(transactionStatus);
        existingTransaction.setUpdateAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction status updated successfully")
                .build();
    }
}

package com.ivan.flexipay.controller;

import com.ivan.flexipay.constant.HttpStatuses;
import com.ivan.flexipay.dto.CurrencyDto;
import com.ivan.flexipay.dto.account.AccountDto;
import com.ivan.flexipay.entity.Account;
import com.ivan.flexipay.service.account.AccountService;
import com.ivan.flexipay.service.openExchangeApi.OpenExchangeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final OpenExchangeApiService openExchangeApiService;
    private final AccountService accountService;

    @Operation(summary = "create an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
            content = @Content(examples = @ExampleObject(Account.defaultJson))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST)))
    })
    @PostMapping
    public ResponseEntity<Account> createAccount(
            @Schema(
                    description = "accountDto",
                    name = "accountDto",
                    type = "object",
                    example = AccountDto.defaultJson) @RequestBody @Valid AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.save(accountDto));
    }

    @Operation(summary = "get an account by accountId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))

    })
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getByAccountId(accountId));
    }

    @Operation(summary = "update account currencies by accountId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @PatchMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountId,
                                                 @RequestBody @Valid List<CurrencyDto> currencies) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateById(accountId, currencies));
    }

    @Operation(summary = "delete account by accountId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.NO_CONTENT),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

package com.practice.simplified_picpay.transaction;


import com.practice.simplified_picpay.authorization.AuthorizerService;
import com.practice.simplified_picpay.notification.NotificationService;
import com.practice.simplified_picpay.wallet.Wallet;
import com.practice.simplified_picpay.wallet.WalletRepository;
import com.practice.simplified_picpay.wallet.WalletType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizerService authorizerService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        // Validar
        validate(transaction);

        // criar transação
        var newTransaction = transactionRepository.save(transaction);

        // debitar na wallet
        var wallet = walletRepository.findById(transaction.payer()).get();
        walletRepository.save(wallet.debit(transaction.value()));


        // chamar serviços externos
        // authorize transaction

        authorizerService.authorize(transaction);

        //notificação
        notificationService.notify(transaction);

        return newTransaction;
    }


    /*
    rules:
    the payer has a common wallet
    the payer has enough balance
    the payer is not the payee
     */
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                        .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction)));
    }

    private static boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMMON.getValue() &&
                payer.balance().compareTo(transaction.value()) >= 0 &&
                !payer.id().equals(transaction.payee());
    }
}

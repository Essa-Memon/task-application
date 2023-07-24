package com.smallworld;

public class CodingTest {
    public static void main(String [] args) {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();

        transactionDataFetcher.getTotalTransactionAmount();
        transactionDataFetcher.getTotalTransactionAmountSentBy("Billy Kimber");
        transactionDataFetcher.getTotalTransactionAmountSentBy("Grace Burgess");
        transactionDataFetcher.getMaxTransactionAmount();
        transactionDataFetcher.countUniqueClients();
        transactionDataFetcher.hasOpenComplianceIssues("Grace Burgess");
        transactionDataFetcher.getTransactionsByBeneficiaryName();
        transactionDataFetcher.getUnsolvedIssueIds();
        transactionDataFetcher.getAllSolvedIssueMessages();
        transactionDataFetcher.getTop3TransactionsByAmount();
        transactionDataFetcher.getTopSender();

    }
}

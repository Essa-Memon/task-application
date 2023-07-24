package com.smallworld;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Issue;
import com.smallworld.data.TransactionData;
import com.smallworld.data.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TransactionDataFetcher {

    private final List<TransactionData> transactionDataList;
    private final Map<String, Transaction> transactionMap;

    public TransactionDataFetcher() {
        transactionDataList = populateTransactionList();
        transactionMap = populateTransactionMap();
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        Double totalAmout = 0.0d;
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            totalAmout += transaction.getValue().getAmount();
        };

        return totalAmout;
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        Double totalAmout = 0.0d;
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            if(transaction.getValue().getSenderFullName().equals(senderFullName)) {
                totalAmout += transaction.getValue().getAmount();
            }
        };

        return totalAmout;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        Double highestAmout = 0.0d;
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            if(transaction.getValue().getAmount() > highestAmout) {
                highestAmout = transaction.getValue().getAmount();
            }
        };

        return highestAmout;
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        Set<String> clients = new HashSet<>();
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            clients.add(transaction.getValue().getSenderFullName());
            clients.add(transaction.getValue().getBeneficiaryFullName());
        };

        return clients.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        boolean issueExists = false;
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            if(transaction.getValue().getSenderFullName().equals(clientFullName)
                    || transaction.getValue().getBeneficiaryFullName().equals(clientFullName)) {
                for(Issue issue : transaction.getValue().getIssuesList()) {
                    if(issue.getIssueSolved() == false) {
                        issueExists = true;
                        return issueExists;
                    }
                }
            }
        }

        return issueExists;
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        Map<String, List<Transaction>> transactionBeneficiaryMap = new HashMap<>();
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            List<Transaction> transactionList = transactionBeneficiaryMap.get(transaction.getValue().getBeneficiaryFullName());
            if(transactionList == null) {
                transactionList = new ArrayList<>();
            }
            Transaction transactionData = transaction.getValue();
            transactionList.add(transactionData);

            transactionBeneficiaryMap.put(transaction.getValue().getBeneficiaryFullName(), transactionList);
        }

        return transactionBeneficiaryMap;
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> unsolvedIssues = new HashSet<>();
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            for (Issue issue : transaction.getValue().getIssuesList()) {
                if(issue.getIssueSolved() == false) {
                    unsolvedIssues.add(issue.getIssueId().intValue());
                }
            }
        }

        return unsolvedIssues;
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<String> solvedIssuesMessages = new ArrayList<>();
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            for (Issue issue : transaction.getValue().getIssuesList()) {
                if(issue.getIssueSolved() == true && issue.getIssueMessage() != null) {
                    solvedIssuesMessages.add(issue.getIssueMessage());
                }
            }
        }

        return solvedIssuesMessages;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = transactionMap.values().stream().toList()
                .stream()
                .sorted(Comparator.comparing(Transaction::getAmount))
                .skip(transactionMap.size() - 3)
                .collect(Collectors.toList());

        return top3Transactions;
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public String getTopSender() {
        String topSenderName = null;
        Double topSenderTotalAmount = 0.0d;
        Map<String, Double> sendersTotalAmount = new HashMap<>();
        for(Map.Entry<String, Transaction> transaction : transactionMap.entrySet()) {
            Double totalAmount = sendersTotalAmount.get(transaction.getValue().getSenderFullName());
            if(totalAmount == null) {
                totalAmount = 0.0d;
            }

            totalAmount += transaction.getValue().getAmount();
            sendersTotalAmount.put(transaction.getValue().getSenderFullName(), totalAmount);

            if(totalAmount > topSenderTotalAmount) {
                topSenderName = transaction.getValue().getSenderFullName();
                topSenderTotalAmount = totalAmount;
            }
        };

        return topSenderName;
    }

    public List<TransactionData> populateTransactionList() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory f = new JsonFactory();
        List<TransactionData> transactionDataList = null;
        try {
            JsonParser json = f.createJsonParser(new File("transactions.json"));
            transactionDataList = Arrays.asList(objectMapper.readValue(json, TransactionData[].class));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactionDataList;
    }

    public Map<String, Transaction> populateTransactionMap() {
        Map<String, Transaction> transactionDataMap = new ConcurrentHashMap<>();
        for(TransactionData transactionData : transactionDataList) {
            Transaction transaction = transactionDataMap.get(transactionData.getMtn());
            if(transaction != null) {
                List<Issue> issuesList = transaction.getIssuesList();
                Issue issue = new Issue(transactionData.getIssueId(), transactionData.getIssueSolved(),
                        transactionData.getIssueMessage());

                issuesList.add(issue);

            } else {
                transaction = new Transaction();
                List<Issue> issuesList = new ArrayList<>();
                Issue issue = new Issue(transactionData.getIssueId(), transactionData.getIssueSolved(),
                        transactionData.getIssueMessage());

                issuesList.add(issue);
                transaction.setMtn(transactionData.getMtn());
                transaction.setAmount(transactionData.getAmount());
                transaction.setSenderFullName(transactionData.getSenderFullName());
                transaction.setSenderAge(transactionData.getSenderAge());
                transaction.setBeneficiaryFullName(transactionData.getBeneficiaryFullName());
                transaction.setBeneficiaryAge(transactionData.getBeneficiaryAge());
                transaction.setIssuesList(issuesList);

                transactionDataMap.put(transactionData.getMtn(), transaction);
            }
        }

        return transactionDataMap;
    }
}

package com.jcondotta.bank_account_transfers;

public enum TestBankAccountDetail {

    JEFFERSON(buildBankAccountResponse(TestBankAccount.JEFFERSON)),

    PATRIZIO(buildBankAccountResponse(TestBankAccount.PATRIZIO));

    private final String bankAccountJSONDetail;

    TestBankAccountDetail(String bankAccountJSONDetail) {
        this.bankAccountJSONDetail = bankAccountJSONDetail;
    }

    public String getBankAccountJSONDetail() {
        return bankAccountJSONDetail;
    }

    private static String buildBankAccountResponse(TestBankAccount account) {
        return String.format("""
            {
              "bankAccountId": "%s",
//              "accountHolders": [
//                {
//                  "accountHolderName": "%s",
//                }
//              ],
//              "iban": "%s",
            }
            """,
            account.getBankAccountId(),
            account.getAccountHolderName(),
            account.getIban()
        );
    }
}

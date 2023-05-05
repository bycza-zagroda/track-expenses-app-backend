ALTER TABLE financial_transactions ADD CHECK (amount > 0);
ALTER TABLE financial_transactions MODIFY transaction_type ENUM('INCOME', 'EXPENSE');
ALTER TABLE financial_transaction_categories MODIFY transaction_type ENUM('INCOME', 'EXPENSE');
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    display_name VARCHAR(120) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS bill_groups (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS participants (
    id UUID PRIMARY KEY,
    group_id UUID NOT NULL REFERENCES bill_groups(id),
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(120) NOT NULL,
    position INT NOT NULL,
    CONSTRAINT uq_participant_user UNIQUE (group_id, user_id),
    CONSTRAINT uq_participant_position UNIQUE (group_id, position)
);

CREATE TABLE IF NOT EXISTS expenses (
    id UUID PRIMARY KEY,
    group_id UUID NOT NULL REFERENCES bill_groups(id),
    description VARCHAR(240) NOT NULL,
    input_amount DECIMAL(18, 2) NOT NULL CHECK (input_amount > 0),
    tax_mode VARCHAR(16) NOT NULL CHECK (tax_mode IN ('NONE', 'EXCLUSIVE', 'INCLUSIVE')),
    tax_rate_pct DECIMAL(5, 2) NOT NULL CHECK (tax_rate_pct >= 0 AND tax_rate_pct <= 100),
    subtotal DECIMAL(18, 2) NOT NULL CHECK (subtotal > 0),
    tax_amount DECIMAL(18, 2) NOT NULL CHECK (tax_amount >= 0),
    amount DECIMAL(18, 2) NOT NULL CHECK (amount > 0),
    paid_by UUID NOT NULL REFERENCES participants(id),
    split_type VARCHAR(16) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS expense_shares (
    expense_id UUID NOT NULL REFERENCES expenses(id),
    participant_id UUID NOT NULL REFERENCES participants(id),
    subtotal DECIMAL(18, 2) NOT NULL CHECK (subtotal >= 0),
    tax_amount DECIMAL(18, 2) NOT NULL CHECK (tax_amount >= 0),
    amount DECIMAL(18, 2) NOT NULL CHECK (amount >= 0),
    PRIMARY KEY (expense_id, participant_id)
);

CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    group_id UUID NOT NULL REFERENCES bill_groups(id),
    payer_id UUID NOT NULL REFERENCES participants(id),
    receiver_id UUID NOT NULL REFERENCES participants(id),
    amount DECIMAL(18, 2) NOT NULL CHECK (amount > 0),
    payment_type VARCHAR(16) NOT NULL CHECK (payment_type IN ('PAYMENT', 'REVERSAL')),
    reversal_of UUID REFERENCES payments(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT chk_payment_parties CHECK (payer_id <> receiver_id),
    CONSTRAINT chk_payment_reversal CHECK (
        (payment_type = 'PAYMENT' AND reversal_of IS NULL)
        OR (payment_type = 'REVERSAL' AND reversal_of IS NOT NULL)
    ),
    CONSTRAINT uq_payment_reversal UNIQUE (reversal_of)
);

-- Compatibility upgrades for databases created by an earlier version of the project.
ALTER TABLE bill_groups ADD COLUMN IF NOT EXISTS currency VARCHAR(3);
UPDATE bill_groups SET currency = 'IDR' WHERE currency IS NULL;
ALTER TABLE bill_groups ALTER COLUMN currency SET NOT NULL;

ALTER TABLE participants ADD COLUMN IF NOT EXISTS user_id UUID;
INSERT INTO users (id, username, display_name, created_at)
SELECT p.id,
       'legacy_' || LOWER(SUBSTRING(REPLACE(CAST(p.id AS VARCHAR), '-', ''), 1, 23)),
       p.name,
       CURRENT_TIMESTAMP
FROM participants p
WHERE p.user_id IS NULL
  AND NOT EXISTS (SELECT 1 FROM users u WHERE u.id = p.id);
UPDATE participants SET user_id = id WHERE user_id IS NULL;
ALTER TABLE participants ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE participants ADD CONSTRAINT IF NOT EXISTS fk_participant_user
    FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE participants ADD CONSTRAINT IF NOT EXISTS uq_participant_user
    UNIQUE (group_id, user_id);
ALTER TABLE participants DROP CONSTRAINT IF EXISTS uq_participant_name;

ALTER TABLE expenses ADD COLUMN IF NOT EXISTS input_amount DECIMAL(18, 2);
ALTER TABLE expenses ADD COLUMN IF NOT EXISTS tax_mode VARCHAR(16);
ALTER TABLE expenses ADD COLUMN IF NOT EXISTS tax_rate_pct DECIMAL(5, 2);
ALTER TABLE expenses ADD COLUMN IF NOT EXISTS subtotal DECIMAL(18, 2);
ALTER TABLE expenses ADD COLUMN IF NOT EXISTS tax_amount DECIMAL(18, 2);

UPDATE expenses SET input_amount = amount WHERE input_amount IS NULL;
UPDATE expenses SET tax_mode = 'NONE' WHERE tax_mode IS NULL;
UPDATE expenses SET tax_rate_pct = 0.00 WHERE tax_rate_pct IS NULL;
UPDATE expenses SET subtotal = amount WHERE subtotal IS NULL;
UPDATE expenses SET tax_amount = 0.00 WHERE tax_amount IS NULL;

ALTER TABLE expense_shares ADD COLUMN IF NOT EXISTS subtotal DECIMAL(18, 2);
ALTER TABLE expense_shares ADD COLUMN IF NOT EXISTS tax_amount DECIMAL(18, 2);

UPDATE expense_shares SET subtotal = amount WHERE subtotal IS NULL;
UPDATE expense_shares SET tax_amount = 0.00 WHERE tax_amount IS NULL;

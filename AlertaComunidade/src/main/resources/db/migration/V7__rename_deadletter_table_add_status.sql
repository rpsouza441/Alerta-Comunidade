ALTER TABLE quarantined_messages RENAME TO dead_letter_messages;
ALTER TABLE dead_letter_messages ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING';
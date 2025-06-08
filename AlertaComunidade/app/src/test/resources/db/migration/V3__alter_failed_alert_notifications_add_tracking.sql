
-- V3__alter_failed_alert_notifications_add_tracking.sql

ALTER TABLE failed_alert_notifications
    ADD COLUMN reprocessed_at TIMESTAMP NULL AFTER failed_at;

ALTER TABLE failed_alert_notifications
    ADD COLUMN original_id BIGINT AFTER id;

UPDATE failed_alert_notifications
SET status = 'FAILED'
WHERE status IS NULL
   OR status NOT IN ('REPROCESSED', 'FAILED');

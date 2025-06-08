CREATE TABLE alert_notifications (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    message VARCHAR(255) NOT NULL,
                                    origin VARCHAR(255) NOT NULL,
                                    alert_type VARCHAR(50) NOT NULL,
                                    status VARCHAR(50) NOT NULL,
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

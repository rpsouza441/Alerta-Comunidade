CREATE TABLE quarantined_messages (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      queue_name VARCHAR(255) NOT NULL,
                                      payload TEXT NOT NULL,
                                      headers TEXT NOT NULL,
                                      received_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
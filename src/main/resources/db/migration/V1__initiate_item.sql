CREATE TABLE ITEM (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    PRICE DECIMAL(10, 2) NOT NULL,
    STOCK INT NOT NULL,
    IS_DELETED BOOLEAN
);
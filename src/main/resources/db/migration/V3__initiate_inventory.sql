CREATE TABLE INVENTORY (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ITEM_ID INT NOT NULL,
    QTY INT NOT NULL,
    TYPE VARCHAR(255) NOT NULL,
    IS_DELETED BOOLEAN
);
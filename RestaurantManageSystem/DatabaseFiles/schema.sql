-- Employee related tables start --
CREATE TABLE IF NOT EXISTS employee
(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    mobile VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    joining_date DATE DEFAULT CURRENT_DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS waiter
(
    id SERIAL PRIMARY KEY,
    salary INT NOT NULL,
    employee_id INT NOT NULL REFERENCES employee(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS admin
(
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS customer
(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    mobile VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    joining_date DATE DEFAULT CURRENT_DATE NOT NULL
);

CREATE TYPE ITEM_TYPES AS ENUM ('beverage', 'food');
CREATE TABLE IF NOT EXISTS item
(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    price INT NOT NULL,
    type ITEM_TYPES NOT NULL
);

CREATE TYPE STATUS_TYPES AS ENUM ('received', 'ongoing', 'complete');
CREATE TABLE IF NOT EXISTS "order"
(
    id SERIAL PRIMARY KEY,
    total INT NOT NULL,
    status STATUS_TYPES NOT NULL,
    waiter_id INT NOT NULL REFERENCES waiter(id) ON DELETE SET NULL,
    customer_id INT NOT NULL REFERENCES customer(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS order_item
(
    order_id INT NOT NULL REFERENCES "order"(id) ON DELETE SET NULL,
    item_id INT NOT NULL REFERENCES item(id) ON DELETE SET NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, item_id)
);

-- Indexes
CREATE INDEX order_status_idx on "order"(status);

-- Clusters
CLUSTER "order" USING order_status_idx;
CLUSTER order_item USING order_item_pkey;
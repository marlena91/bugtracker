CREATE TABLE test_table(
                          id SERIAL PRIMARY KEY,
                          username VARCHAR(255) NOT NULL UNIQUE,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL
);
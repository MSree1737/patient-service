-- Ensure the 'users' table exists
CREATE TABLE IF NOT EXISTS "users" (
                                       id UUID PRIMARY KEY,
                                       email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

-- Insert the user if no existing user with the same id or email exists
INSERT INTO "users" (id, email, password, role)
SELECT '223e4567-e89b-12d3-a456-426614174006', 'testuser@test.com',
       '$2b$12$7hoRZfJrRKD2nIm2vHLs7OBETy.LWenXXMLKf99W8M4PUwO6KB7fu', 'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '223e4567-e89b-12d3-a456-426614174006'
       OR email = 'testuser@test.com'
);

INSERT INTO "users" (id, email, password, role)
SELECT '11111111-1111-1111-1111-111111111111', 'bankai1@gmail.com',
       '$2a$10$pCdlrzBt1BE9HZrWCpvI0euDJn3JHypSipXScYpVg0HZp815fsbT.', 'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '11111111-1111-1111-1111-111111111111'
       OR email = 'bankai1@gmail.com'
);

INSERT INTO "users" (id, email, password, role)
SELECT '22222222-2222-2222-2222-222222222222', 'test21Billing@gmail.com',
       '$2a$10$pCdlrzBt1BE9HZrWCpvI0euDJn3JHypSipXScYpVg0HZp815fsbT.', 'USER'
    WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '22222222-2222-2222-2222-222222222222'
       OR email = 'test21Billing@gmail.com'
);

INSERT INTO "users" (id, email, password, role)
SELECT '33333333-3333-3333-3333-333333333333', 'test20Billing@gmail.com',
       '$2a$10$pCdlrzBt1BE9HZrWCpvI0euDJn3JHypSipXScYpVg0HZp815fsbT.', 'USER'
    WHERE NOT EXISTS (
    SELECT 1
    FROM "users"
    WHERE id = '33333333-3333-3333-3333-333333333333'
       OR email = 'test20Billing@gmail.com'
);



CREATE TABLE user_types (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(25) NOT NULL
);

INSERT INTO user_types (description) VALUES
('customer'),
('support operator'),
('technician'),
('administrator');

CREATE TABLE user_status (
     id INT PRIMARY KEY AUTO_INCREMENT,
     description VARCHAR(25) NOT NULL
);

INSERT INTO user_status (description) VALUES
('active'),
('inactive'),
('suspended');

CREATE TABLE technicians_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(25) NOT NULL
);

INSERT INTO technicians_status (description) VALUES
('available'),
('busy'),
('absent');

CREATE TABLE states (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    abbreviation VARCHAR(2) NOT NULL
);

INSERT INTO states (name, abbreviation) VALUES
('Acre', 'AC'),
('Alagoas', 'AL'),
('Amapá', 'AP'),
('Amazonas', 'AM'),
('Bahia', 'BA'),
('Ceará', 'CE'),
('Distrito Federal', 'DF'),
('Espírito Santo', 'ES'),
('Goiás', 'GO'),
('Maranhão', 'MA'),
('Mato Grosso', 'MT'),
('Mato Grosso do Sul', 'MS'),
('Minas Gerais', 'MG'),
('Pará', 'PA'),
('Paraíba', 'PB'),
('Paraná', 'PR'),
('Pernambuco', 'PE'),
('Piauí', 'PI'),
('Rio de Janeiro', 'RJ'),
('Rio Grande do Norte', 'RN'),
('Rio Grande do Sul', 'RS'),
('Rondônia', 'RO'),
('Roraima', 'RR'),
('Santa Catarina', 'SC'),
('São Paulo', 'SP'),
('Sergipe', 'SE'),
('Tocantins', 'TO');

CREATE TABLE cities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    state_id INT NOT NULL,
    FOREIGN KEY (state_id) REFERENCES states(id)
);

INSERT INTO cities (name, state_id) VALUES
('Rio Branco', 1),
('Maceió', 2),
('Macapá', 3),
('Manaus', 4),
('Salvador', 5),
('Fortaleza', 6),
('Brasília', 7),
('Vitória', 8),
('Goiânia', 9),
('São Luís', 10),
('Cuiabá', 11),
('Campo Grande', 12),
('Belo Horizonte', 13),
('Belém', 14),
('João Pessoa', 15),
('Curitiba', 16),
('Recife', 17),
('Teresina', 18),
('Rio de Janeiro', 19),
('Natal', 20),
('Porto Alegre', 21),
('Porto Velho', 22),
('Boa Vista', 23),
('Florianópolis', 24),
('São Paulo', 25),
('Aracaju', 26),
('Palmas', 27);

CREATE TABLE ticket_status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(25) NOT NULL
);

INSERT INTO ticket_status (description) VALUES
('open'),
('in progress'),
('awaiting evaluation'),
('closed');

CREATE TABLE ticket_priorities (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(25) NOT NULL
);

INSERT INTO ticket_priorities (description) VALUES
('low'),
('medium'),
('high');

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    cpf VARCHAR(20) UNIQUE NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    phone_number VARCHAR(15) UNIQUE,
    user_password VARCHAR(20) NOT NULL,
    type_id INT NOT NULL DEFAULT 1,
    status_id INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (type_id) REFERENCES user_types(id),
    FOREIGN KEY (status_id) REFERENCES user_status(id)
);

CREATE TABLE user_permissions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_type_id INT NOT NULL,
    can_create_ticket BOOLEAN NOT NULL DEFAULT FALSE,
    can_edit_ticket BOOLEAN NOT NULL DEFAULT FALSE,
    can_assign_ticket BOOLEAN NOT NULL DEFAULT FALSE,
    can_close_ticket BOOLEAN NOT NULL DEFAULT FALSE,
    can_manager_reports BOOLEAN NOT NULL DEFAULT FALSE,
    can_manage_users BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_type_id) REFERENCES user_types(id)
);

INSERT INTO user_permissions (user_type_id, can_create_ticket, can_edit_ticket, can_assign_ticket, can_close_ticket, can_manager_reports, can_manage_users) VALUES
(1, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE),
(2, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE),
(3, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE),
(4, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE);

CREATE TABLE technicians (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    assigned_tickets_count INT DEFAULT 0,
    status_id INT NOT NULL,
    work_shift_start TIME NOT NULL,
    work_shift_end TIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (status_id) REFERENCES technicians_status(id)
);

CREATE TABLE customers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    address VARCHAR(100) NOT NULL,
    city_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (city_id) REFERENCES cities(id)
);

CREATE TABLE tickets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    technician_id BIGINT,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status_id INT NOT NULL,
    priority_id INT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    rating INT,
    rating_comment VARCHAR(255),
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (technician_id) REFERENCES technicians(id),
    FOREIGN KEY (status_id) REFERENCES ticket_status(id),
    FOREIGN KEY (priority_id) REFERENCES ticket_priorities(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE ticket_update_types (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(25) NOT NULL
);

INSERT INTO ticket_update_types (description) VALUES
('status change'),
('priority change'),
('technician assignment'),
('comment added');

CREATE TABLE ticket_update_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ticket_id BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    update_type_id INT NOT NULL,
    old_value VARCHAR(255),
    new_value VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment VARCHAR(255),
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    FOREIGN KEY (update_type_id) REFERENCES ticket_update_types(id)
);
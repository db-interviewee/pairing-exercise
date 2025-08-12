-- Demo organizations for testing
INSERT INTO organisations_schema.contact_details (id, phone_number, fax, email)
VALUES ('550e8400-e29b-41d4-a716-446655440000', '+49 30 12345678', '+49 30 12345679', 'demo@merchant.com');

INSERT INTO organisations_schema.contact_details (id, phone_number, fax, email)
VALUES ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', '+49 30 87654321', '+49 30 87654322', 'buyer@company.com');

INSERT INTO organisations_schema.organisations (id, name, date_founded, country_code, vat_number, registration_number, legal_entity_type, contact_details_id)
VALUES ('6dc7acb9-3fba-4c5b-bd0c-6898b6ec152a', 'Demo Merchant GmbH', '2020-01-15', 'DE', 'DE123456789', 'HRB12345', 'LIMITED_LIABILITY_COMPANY', '550e8400-e29b-41d4-a716-446655440000');

INSERT INTO organisations_schema.organisations (id, name, date_founded, country_code, vat_number, registration_number, legal_entity_type, contact_details_id)
VALUES ('46aac123-9cee-4a5f-9986-386eb36bd70e', 'Demo Buyer UG', '2019-03-22', 'DE', 'DE987654321', 'HRB67890', 'LIMITED_LIABILITY_COMPANY', '6ba7b810-9dad-11d1-80b4-00c04fd430c8');

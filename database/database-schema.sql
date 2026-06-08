-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "postgis";

-- Create custom types
CREATE TYPE restaurant_status AS ENUM ('ACTIVE', 'CLOSED', 'MAINTENANCE', 'BUSY');
CREATE TYPE cart_status AS ENUM ('ACTIVE', 'ABANDONED', 'COMPLETED');

-- User table
CREATE TABLE users (
   user_id BIGINT PRIMARY KEY,
   username VARCHAR(255) UNIQUE NOT NULL,
   userpassword VARCHAR(68) NOT NULL, -- For BCrypt hashed passwords
   user_email VARCHAR(255) UNIQUE NOT NULL,
   user_status SMALLINT DEFAULT 1,
   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Role table
CREATE TABLE role (
  role_id BIGINT PRIMARY KEY,
  role_name VARCHAR(50) UNIQUE NOT NULL,
  description TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  created_by BIGINT,
  updated_by BIGINT
);

-- User role junction table
CREATE TABLE user_role (
   user_role_id BIGINT PRIMARY KEY,
   user_id BIGINT,
   role_id BIGINT,
   UNIQUE (user_id, role_id)
);

-- Customer table
CREATE TABLE customer (
      customer_id BIGINT PRIMARY KEY,
      user_id BIGINT UNIQUE NOT NULL,
      customer_name VARCHAR(255) NOT NULL,
      customer_phone VARCHAR(20),
      created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
      created_by BIGINT,
      updated_by BIGINT
);

-- Address table
CREATE TABLE address (
     address_id BIGINT PRIMARY KEY,
     customer_id BIGINT NOT NULL,
     address_location GEOGRAPHY(Point, 4326) NOT NULL,
     address_line TEXT NOT NULL,
     is_default BOOLEAN DEFAULT FALSE,
     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     created_by BIGINT,
     updated_by BIGINT
);

-- Payment type table
CREATE TABLE payment_type (
  payment_type_id BIGINT PRIMARY KEY,
  payment_type_name VARCHAR(50) NOT NULL,
  payment_type_description TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  created_by BIGINT,
  updated_by BIGINT
);

-- Payment integration configuration
CREATE TABLE payment_integration_configuration (
   payment_config_id BIGINT PRIMARY KEY,
   payment_integration_type_id BIGINT NOT NULL,
   public_key VARCHAR(500),
   secret_key VARCHAR(500),
   webhook_url VARCHAR(500),
   payment_config_is_active SMALLINT DEFAULT 1,
   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   created_by BIGINT,
   updated_by BIGINT
);

-- Preferred payment setting
CREATE TABLE preferred_payment_setting (
   preferred_payment_id BIGINT PRIMARY KEY,
   customer_id BIGINT NOT NULL,
   payment_config_id BIGINT NOT NULL,
   preferred_payment_alias VARCHAR(255),
   preferred_payment_is_default BOOLEAN DEFAULT FALSE,
   preferred_payment_is_active BOOLEAN DEFAULT TRUE,
   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   created_by BIGINT,
   updated_by BIGINT
);

-- Payment status
CREATE TABLE payment_status (
    payment_status_id BIGINT PRIMARY KEY,
    payment_status_name VARCHAR(50) NOT NULL,
    payment_status_description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Restaurant category
CREATE TABLE restaurant_category (
     restaurant_category_id BIGINT PRIMARY KEY,
     restaurant_category_name VARCHAR(255) NOT NULL,
     restaurant_category_icon_url VARCHAR(500),
     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     created_by BIGINT,
     updated_by BIGINT
);

-- Restaurant
CREATE TABLE restaurant (
    restaurant_id  BIGINT PRIMARY KEY,
    restaurant_category_id BIGINT NOT NULL,
    restaurant_name VARCHAR(255) NOT NULL,
    restaurant_description TEXT,
    location GEOGRAPHY(Point, 4326) NOT NULL,
    restaurant_logo_image_url VARCHAR(500),
    restaurant_cover_image_url VARCHAR(500),
    status restaurant_status DEFAULT 'ACTIVE',
    restaurant_address TEXT NOT NULL,
    restaurant_phone VARCHAR(20),
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Menu
CREATE TABLE menu (
    menu_id BIGINT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    menu_name VARCHAR(255) NOT NULL,
    menu_description TEXT,
    menu_is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Menu category
CREATE TABLE menu_category (
   menu_category_id BIGINT PRIMARY KEY,
   menu_id BIGINT NOT NULL,
   menu_category_name VARCHAR(255) NOT NULL,
   menu_category_image_url VARCHAR(500),
   menu_category_display_order INTEGER DEFAULT 0,
   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   created_by BIGINT,
   updated_by BIGINT
);

-- Menu item
CREATE TABLE menu_item (
   menu_item_id BIGINT PRIMARY KEY,
   menu_category_id BIGINT NOT NULL,
   menu_item_name VARCHAR(255) NOT NULL,
   menu_item_description TEXT,
   menu_item_image_url VARCHAR(500),
   menu_item_price DECIMAL(10,2) NOT NULL,
   menu_item_stock_quantity INTEGER DEFAULT 0,
   menu_item_is_active BOOLEAN DEFAULT TRUE,
   menu_item_display_order INTEGER DEFAULT 0,
   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   created_by BIGINT,
   updated_by BIGINT
);

-- Order status
CREATE TABLE order_status (
  order_status_id BIGINT PRIMARY KEY,
  order_status_name VARCHAR(50) NOT NULL,
  order_status_description TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  created_by BIGINT,
  updated_by BIGINT
);

-- Order
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    order_total_amount DECIMAL(10,2) NOT NULL,
    order_status_id BIGINT NOT NULL,
    order_delivery_address TEXT NOT NULL,
    order_payment_method_snapshot TEXT,
    customer_phone VARCHAR(20),
    order_special_instructions TEXT,
    estimated_delivery_time TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Order item
CREATE TABLE order_item (
    order_item_id BIGINT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    order_item_quantity INTEGER NOT NULL CHECK (order_item_quantity > 0),
    order_item_unit_price DECIMAL(10,2) NOT NULL,
    order_item_total_price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
);

-- Transaction
CREATE TABLE transaction (
     transaction_id BIGINT PRIMARY KEY,
     order_id BIGINT NOT NULL,
     customer_id BIGINT NOT NULL,
     transaction_amount DECIMAL(10,2) NOT NULL,
     payment_status_id BIGINT NOT NULL,
     payment_type_id BIGINT NOT NULL,
     external_transaction_id VARCHAR(100),
     integration_type_id BIGINT NOT NULL,
     raw_request JSONB,
     raw_response JSONB,
     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
     created_by BIGINT,
     updated_by BIGINT
);

-- Cart
CREATE TABLE cart (
      cart_id BIGINT PRIMARY KEY,
      customer_id BIGINT NOT NULL,
      restaurant_id BIGINT NOT NULL,
      status cart_status DEFAULT 'ACTIVE',
      created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
      created_by BIGINT,
      updated_by BIGINT
);

-- Cart item
CREATE TABLE cart_item (
   cart_item_id BIGINT PRIMARY KEY,
   cart_id BIGINT NOT NULL,
   menu_item_id BIGINT NOT NULL,
   cart_item_quantity INTEGER NOT NULL CHECK (cart_item_quantity > 0),
   cart_item_special_instructions TEXT,
   cart_item_unit_price DECIMAL(10,2) NOT NULL,
   cart_item_total_price DECIMAL(10,2) NOT NULL,
   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   created_by BIGINT,
   updated_by BIGINT
);

-- Audit log
CREATE TABLE audit_log (
   audit_id BIGINT PRIMARY KEY,
   entity_type VARCHAR(50) NOT NULL,
   entity_id VARCHAR(100) NOT NULL,
   transaction_id BIGINT,
   user_id BIGINT,
   action VARCHAR(50) NOT NULL,
   previous_state JSONB,
   new_state JSONB,
   provider_request JSONB,
   provider_response JSONB,
   details JSONB,
   created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
   created_by BIGINT,
   updated_by BIGINT
);

-- -- Indexes
CREATE INDEX idx_user_email ON users(user_email);
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_customer_user_id ON customer(user_id);
CREATE INDEX idx_address_customer_id ON address(customer_id);
-- CREATE INDEX idx_address_location ON address USING GIST(location);
CREATE INDEX idx_restaurant_location ON restaurant USING GIST(location);
CREATE INDEX idx_restaurant_category_id ON restaurant(restaurant_category_id);
CREATE INDEX idx_menu_restaurant_id ON menu(restaurant_id);
CREATE INDEX idx_menu_item_category_id ON menu_item(menu_category_id);
CREATE INDEX idx_order_customer_id ON orders(customer_id);
CREATE INDEX idx_order_restaurant_id ON orders(restaurant_id);
CREATE INDEX idx_order_status_id ON orders(order_status_id);
CREATE INDEX idx_order_item_order_id ON order_item(order_id);
CREATE INDEX idx_transaction_order_id ON transaction(order_id);
CREATE INDEX idx_transaction_customer_id ON transaction(customer_id);
CREATE INDEX idx_cart_customer_id ON cart(customer_id);
CREATE INDEX idx_cart_item_cart_id ON cart_item(cart_id);
CREATE INDEX idx_audit_log_entity ON audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_log_created_at ON audit_log(created_at);

-- Trigger function for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language plpgsql;

-- Triggers for updated_at
CREATE TRIGGER update_user_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_role_updated_at BEFORE UPDATE ON role FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_customer_updated_at BEFORE UPDATE ON customer FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_address_updated_at BEFORE UPDATE ON address FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_payment_type_updated_at BEFORE UPDATE ON payment_type FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_payment_config_updated_at BEFORE UPDATE ON payment_integration_configuration FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_preferred_payment_updated_at BEFORE UPDATE ON preferred_payment_setting FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_payment_status_updated_at BEFORE UPDATE ON payment_status FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_restaurant_category_updated_at BEFORE UPDATE ON restaurant_category FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_restaurant_updated_at BEFORE UPDATE ON restaurant FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_menu_updated_at BEFORE UPDATE ON menu FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_menu_category_updated_at BEFORE UPDATE ON menu_category FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_menu_item_updated_at BEFORE UPDATE ON menu_item FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_order_status_updated_at BEFORE UPDATE ON order_status FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_order_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_order_item_updated_at BEFORE UPDATE ON order_item FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_transaction_updated_at BEFORE UPDATE ON transaction FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_cart_updated_at BEFORE UPDATE ON cart FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_cart_item_updated_at BEFORE UPDATE ON cart_item FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
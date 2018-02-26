# Create the database
CREATE DATABASE IF NOT EXISTS grizzle;
# Create the user
CREATE USER IF NOT EXISTS 'grizzle'@'localhost' IDENTIFIED BY 'grizzle-1234';
# Grant all permissions for this database to the user
GRANT ALL PRIVILEGES ON grizzle.* TO 'grizzle'@'localhost';
# How to build service

Run `mvn clean install`

# How to start service locally

1. Run `docker-compose -d up` This will ensure that MySQL 8.x started in Docker container with name mysql-container and 
initialized with initial values in Databse `restaurant_reservation`

2. Run command mvn spring-boot:run This will start Reservation API service
To ensure it’s up and running run curl command

```shell

curl --location 'http://localhost:8080/api/reservations' \
--header 'Content-Type: application/json' \
--data-raw '{
  "date": "2024-10-01T14:30:00",
  "persons": 2,
  "email": "your_email_here@gmail.com",
  "phone": "1234567890",
  "name": "John Doe"
}'

```
 
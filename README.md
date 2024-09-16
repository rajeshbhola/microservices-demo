# microservices-demo

### Testing in Postman
#### Register

POST url: http://localhost:8081/api/users/register
```
{
    "username": "rajesh1",
    "password": "test123",
    "role": "SELLER"
}
```

#### Login

POST url: http://localhost:8081/api/users/login
```
{
    "username": "rajesh1",
    "password": "test123"
}
```

#### Add Product
POST url: http://localhost:8082/api/products/add

Authorization -> select Bearer Token -> paste token value getting from login endpoint
```
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwic3ViIjoicmFqZXNoMSIsImlhdCI6MTcyNjM4NjI4NywiZXhwIjoxNzI2Mzg5ODg3fQ._2qLKwhZ2VEV0Zr6dYQxuYCRvvvrbrJWZR1st3K1ZtM
```
```
{
    "name": "Laptop",
    "price": 50000,
    "quantity": 1,
    "description": "HP Pavilion"
}
```


#### View Product
GET url: http://localhost:8082/api/products/view

Authorization -> select Bearer Token -> paste token value getting from login endpoint
```
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwic3ViIjoicmFqZXNoMSIsImlhdCI6MTcyNjM4NjI4NywiZXhwIjoxNzI2Mzg5ODg3fQ._2qLKwhZ2VEV0Zr6dYQxuYCRvvvrbrJWZR1st3K1ZtM
```

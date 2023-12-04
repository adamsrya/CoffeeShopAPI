# CoffeeshopApi
## API Spec
info :

    title : Coffee Shop
    version : 1.0.0

## Product
### Create Product
Request:
- Method : POST
- Endpoint : `/api/products`
- Header :
    - Content-Type: application/json
    - Accept: application/json
  
Body :

```json
{
  "id" : "string, unique",
  "category" : "enum", 
  "name" : "string",
  "price" : "long",
  "description" :"string",
  "extras": "string",
  "quantity" : "integer"
}

```
Response :

```json
{
  "message": "string",
  "data": {
    "id": "string, unique",
    "category": "enum",
    "name": "string",
    "price": "long",
    "description": "string",
    "extras": "string",
    "quantity": "integer",
    "created": "date",
    "updated": "date"
  },
  "paging": null
}
```
### Get Product
Request:
- Method : GET
- Endpoint : `/api/products/{id}`
- Header :
    - Accept: application/json

Response :

```json
{
  "message": "string",
  "data": {
    "id" : "string, unique",
    "category" : "enum",
    "name" : "string",
    "price" : "long",
    "description" :"string",
    "extras": "string",
    "quantity" : "integer",
    "createdAt" : "date",
    "updateAT": "date"
  },
  "paging": null
}
```
### Update Products
Request:
- Method : PUT
- Endpoint : `/api/products/{id}`
- Header :
    - Content-Type: application/json
    - Accept: application/json

Body :

```json
{
  "category" : "enum", 
  "name" : "string",
  "price" : "long",
  "description" :"string",
  "extras": "string",
  "quantity" : "integer"
}
```
Respones :
```json
{
  "message": "string",
  "data": {
    "id": "string, unique",
    "category": "enum",
    "name": "string",
    "price": "long",
    "description": "string",
    "extras": "string",
    "quantity": "integer",
    "created": "date",
    "updated": "date"
  },
  "paging": null
}
```

### List Products
Request:
- Method : GET
- Endpoint : `/api/products`
- Header :
  - Accept: application/json`
- Query Param : 
  - name : text,
  - description : text,
  - page : number,
  - size : number

Response :

```json
{
  "message": null,
  "data": [
    {
      "id": "string, unique",
      "category": "enum",
      "name": "string",
      "price": "long",
      "description": "string",
      "extras": "string",
      "quantity": "integer",
      "created": "date",
      "updated": "date"
    }
  ],
  "paging": {
    "currentPage": "number",
    "totalPage": "number",
    "size": "number"
  }
}
```
### Delete Products
Request:
- Method : DELETE
- Endpoint : `/api/products/{id}`
- Header :
  - Accept: application/json

Response :

```json
{
  "message": "string",
  "data": "string",
  "paging": null
}


```

## USER
### Create User 
Request:
- Method : POST
- Endpoint: `/api/users`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  
Body :
```json
{
    "firstname" : "string",
    "lasttname" : "string",
    "email" : "string",
    "password": "string",
    "repassword": "string"
}
```
Response :
```json
{
    "message": "string",
    "data": null,
    "paging": null
}
```
### Get User
Request:
- Method : Get
- Endpoint: `/api/users/current`
- Header :
  - Accept: application/json
  - X-TOKEN-API : "secret token"
  
Body :
```json
{
    "firstname" : "string",
    "lasttname" : "string",
    "email" : "string",
    "password": "string",
    "repassword": "string"
}
```
Response :
```json
{
    "message": "string",
    "data": null,
    "paging": null
}
```
### Change Password User
Request:
- Method : PUT
- Endpoint: `/api/users/current/auth`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  - X-TOKEN-API : "secret token"
    
Body :
```json
{
  "password": "String",
  "repassword": "String"
}
```
Response :
```json
{
    "message": "string",
    "data": null,
    "paging": null
}
```
### Change Profile User
Request:
- Method : PUT
- Endpoint: `/api/users/current/profile`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Body :
```json
{
  "firstname" : "string",
  "lasttname" : "string"
}
```
Response :
```json
{
    "message": "string",
    "data": null,
    "paging": null
}
```
## Authentication
### Login User
Request:
- Method : POST
- Endpoint: `/api/auth/login`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  

Body :
```json
{
  "email": "string",
  "password": "string"

}
```
Response :
```json
{
  "message": null,
  "data": {
    "token": "string, Secret Token",
    "expiretAt": "number"
  },
  "paging": null
}
```
### Logout User
Request:
- Method : Delete
- Endpoint: `/api/auth/logout`
- Header :
  - Accept: application/json
  
Response :
```json
{
  "message": null,
  "data": "string",
  "paging": null
}
```
## Carts
### Add Item to Carts
Request:
- Method : POST
- Endpoint : `/api/cart/add`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Body :

```json
{
  "productid":"string",
  "quantity":"integer"
}
```
Response :

```json
{
  "message": "string",
  "data": "string",
  "paging": null
}
```
### Get Carts
Request:
- Method : Get
- Endpoint : `/api/cart`
- Header :
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Response :

```json
{
  "message": "String",
  "data": {
    "cartItems": [
      {
        "quantity": "number",
        "product": {
          "id": "string",
          "category": "enum",
          "name": "string",
          "price": "long",
          "description": "string",
          "extras": "string",
          "quantity": "integer",
          "created": "date",
          "updated": "date"
        }
      },
      {
        "quantity": "number",
        "product": {
          "id": "string",
          "category": "enum",
          "name": "string",
          "price": "long",
          "description": "string",
          "extras": "string",
          "quantity": "integer",
          "created": "date",
          "updated": "date"
        }
      }
    ],
    "amount": "number"
  },
  "paging": null
}
```
### Update Item in Carts
Request:
- Method : PATCH
- Endpoint : `/api/cart/{Id_product}`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Body :

```json
{
  "quantity":"integer"
}
```
Respones :
```json
{
  "message": "String",
  "data": {
    "cartItems": [
      {
        "quantity": "number",
        "product": {
          "id": "string",
          "category": "enum",
          "name": "string",
          "price": "long",
          "description": "string",
          "extras": "string",
          "quantity": "integer",
          "created": "date",
          "updated": "date"
        }
      },
      {
        "quantity": "number",
        "product": {
          "id": "string",
          "category": "enum",
          "name": "string",
          "price": "long",
          "description": "string",
          "extras": "string",
          "quantity": "integer",
          "created": "date",
          "updated": "date"
        }
      }
    ],
    "amount": "number"
  },
  "paging": null
}
```

### Delete Carts
Request:
- Method : DELETE
- Endpoint : `/api/cart`
- Header :
  - Accept: application/json
  - X-TOKEN-API : "secret token"
  
Response :

```json
{
  "message": "string",
  "data": "string",
  "paging": null
}
```
## Address
### Create Address
Request:
- Method : POST
- Endpoint : `/api/address`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Body :

```json
{
  "name":"string",
  "handphone":"string",
  "country":"string",
  "province":"string",
  "city":"string",
  "streetname":"string",
  "postalcode":"string",
  "otherdetails":"string",
  "labelas":"enum"
}
```
Response :

```json
{
  "message": "string",
  "data": {
    "name":"string",
    "handphone":"string",
    "country":"string",
    "province":"string",
    "city":"string",
    "streetname":"string",
    "postalcode":"string",
    "otherdetails":"string",
    "labelas":"enum",
    "created": "date",
    "updated": "date"
  },
  "paging": null
}
```
### Get Address
Request:
- Method : Get
- Endpoint : `/api/address/{id}`
- Header :
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Response :

```json
{
  "message": "string",
  "data": {
    "name":"string",
    "handphone":"string",
    "country":"string",
    "province":"string",
    "city":"string",
    "streetname":"string",
    "postalcode":"string",
    "otherdetails":"string",
    "labelas":"enum",
    "created": "date",
    "updated": "date"
  },
  "paging": null
}
```
### Update Address
Request:
- Method : PUT
- Endpoint : `/api/address/{id}`
- Header :
  - Content-Type: application/json
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Body :

```json
{
  "name":"string",
  "handphone":"string",
  "country":"string",
  "province":"string",
  "city":"string",
  "streetname":"string",
  "postalcode":"string",
  "otherdetails":"string",
  "labelas":"enum"
}
```
Response :

```json
{
  "message": "string",
  "data": {
    "name":"string",
    "handphone":"string",
    "country":"string",
    "province":"string",
    "city":"string",
    "streetname":"string",
    "postalcode":"string",
    "otherdetails":"string",
    "labelas":"enum",
    "created": "date",
    "updated": "date"
  },
  "paging": null
}
```
### Delete Carts
Request:
- Method : DELETE
- Endpoint : `/api/address/{id}`
- Header :
  - Accept: application/json
  - X-TOKEN-API : "secret token"

Response :

```json
{
  "message": "string",
  "data": "string",
  "paging": null
}
```
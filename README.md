# Cassandra Testing

Project tests interlok-cassandra, interlok-varsub, interlok-json features

## What it does

This project has a single workflow that implements a simple rest api to manage (CRUD) data in mongodb.

```mermaid
graph LR
  subgraph Cassandra DB API
    J_C(Jetty Consumer) --> JR(Jetty Router)
    JR --> O[Open API]
    O --> R[Response]
    JR --> A[Add]
    A --> R[Response]
    JR --> U[Update]
    U --> R[Response]
    JR --> D[Delete]
    D --> R[Response]
    JR --> G[Get]
    G --> R[Response]
    JR --> L[List]
    L --> R[Response]
  end

  style J_C fill:#FF6C6C
  style JR fill: #F89347
  style O fill:#F89347
  style A fill:#F89347
  style U fill:#F89347
  style D fill:#F89347
  style G fill:#F89347
  style L fill:#F89347
  style R fill:#F89347

```


It's not intended to be a fully working api and a lot of use cases are not handled.

The rest API uses an Open API yaml file to describe its endpoints although not all errors are handled.

## Getting started

Before starting Interlok you need to create a Cassandra docker container with

* `docker-compose up`

Then start Interlok

* `./gradlew clean build`
* `(cd ./build/distribution && java -jar lib/interlok-boot.jar)`

The Cassandra entries will be validated with the following schema:

```
{   
  "$id":"json-schema",
  "$schema":"http://json-schema.org/draft-07/schema#",
  "title":"Account",
  "type":"object",
  "properties":{
    "firstName":{
      "type":"string",
      "description":"The person's first name."
    },
    "lastName":{
      "type":"string",
      "description":"The person's last name."
    },
    "username":{
      "type":"string",
      "description":"The person's username"
    },
    "password":{
      "type":"string",
      "description":"The person's password"
    }
  },
  "required": ["firstName", "lastName", "username", "password"]
}
```

The API is available at `http://localhost:8080/api/cassandra` and the Open API yaml desc at `http://localhost:8080/api/cassandra\openapi.yml`

## Try it

You can call the api with the following curl commands:

### Add an entry

`curl --location --request POST 'http://localhost:8080/api/cassandra' \
--header 'Content-Type: text/plain' \
--data-raw '{
    "username":"username",
    "firstName":"firstName",
    "lastName":"lastName",
    "password":"password"
}'`

### Update an entry

`curl --location --request PUT 'http://localhost:8080/api/cassandra' \
--header 'Content-Type: text/plain' \
--data-raw '{
    "username":"username",
    "firstName":"firstName",
    "lastName":"lastName",
    "password":"password"
}'`


### Delete an entry

`curl --location --request DELETE 'http://localhost:8080/api/cassandra/username'`

### Get an entry

`curl --location --request GET 'http://localhost:8080/api/cassandra/username'`

### List all entries

`curl --location --request GET 'http://localhost:8080/api/cassandra'`


# RecurringScheduler

[![Webapp Testsuite](https://github.com/Ernstsen/recurringscheduler/actions/workflows/recurringscheduler_test.yml/badge.svg)](https://github.com/Ernstsen/recurringscheduler/actions/workflows/recurringscheduler_test.yml)

Scheduler for recurring events

# Running the application

As with most software, you decide how you want to run it. Here are some suggestions.

## Running the application with docker-compose for development

The easiest way to run the application is to use docker-compose. This will start the application and a postgres
database.

```bash
docker-compose up
```

## Running the application with docker-compose for hobby-use

To run the application in a hobby environment, you can simply use the dev setup.

For more resilient setup, you should host a postgres DB as a separate service.

You should then set the following environment variables:

```
- QUARKUS_DATASOURCE_DB_VERSION={{db_version}}
- QUARKUS_DATASOURCE_USERNAME={{db_username}}
- QUARKUS_DATASOURCE_PASSWORD={{db_password}}
- QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://{{db_host}}:{{db_port}}/{{db_name}}
```

Note that thes ``docker-compose.yml`` file contains the DB version used in development, and is the only one
guaranteed to work with the application.
 

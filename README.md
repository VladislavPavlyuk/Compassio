# Compassio

Web app for collecting and browsing profession data for Slovakia (SK ISCO-08_2020).

## Stack
- Spring Boot + Thymeleaf (frontend)
- Postgres

## Run
1) Create database and user:

```
createdb compassio
createuser -P compassio
```

2) Configure credentials in `src/main/resources/application.yml` if needed.

3) Start the app:

```
mvn spring-boot:run
```

The seed loads from `src/main/resources/data/SK ISCO-08_2020.xlsx` on first run.

## API
`GET /api/professions?q=legis&page=0&size=50`

# Repo Sentences Paula Altavo

## Steps to run the project:

```bash
cp .env.example .env
```

```bash
docker-compose -f ./dockercompose.yml up 
```
It is possible that Spring will fail on the first boot. This is because the database is not ready yet. Just restart the Spring container and it should work on the second boot without any problems.

> Note: If you encounter permission issues run: chmod +x mvnw

For creating the first collection:
```bash
curl --location 'http://localhost:8080/collections/add' \
--form 'name="Kurze Märchen"'
```

For viewing all existing collections:
```bash
curl --location 'http://localhost:8080/collections/'
```

For creating the first sentence:
```bash
curl --location 'http://localhost:8080/sentences/add' \
--form 'text="Es war einmal..."' \
--form 'pronunciation="pronunciation text"' \
--form 'collectionId="1"'
```

For viewing all existing sentences:
```bash
curl --location 'http://localhost:8080/sentences/'
```

For uploading a file containing a collection of sentences:
```bash
curl --location 'http://localhost:8080/collections/upload' \
--form 'file=@/path/to/your/file.csv;type=text/csv' 
```

You can search by id, name, ... as well as update and delete the collections and sentences.

## Technologies used:

- Java 21
- Spring Boot
- Docker
- MySQL

## Possible improvements:

- add authentication
- verify user input
- add tests
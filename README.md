# Run locally

1. Start Mongo container `docker compose up`


2. Start the application `./gradlew run`

This populates database `snackable-test` collection `file` with files that have successfully finished processing

Now it's possible to send requests e.g `curl http://0.0.0.0:8080/api/file/metadata/08448513-b980-4267-abeb-2445b4069a0c`
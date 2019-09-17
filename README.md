# CSV to SQLite

Reading input CSV-file, validating records, writing valid records into SQLite DB and writing invalid records in another CSV-file

## Getting Started

This project was made using: 
IntelliJ IDEA 2019.2 (Ultimate Edition)
Build #IU-192.5728.98, built on July 23, 2019

### Prerequisites

Apache Commons CSV lib was used in this project, it is in dependency list.

Also you'll need SQLite JDBC Drivers - "sqlite-jdbc-3.27.2.1.jar" was used in this project, it is included in project folder

### Installing

Open this project in your IDE using "clone" or download ZIP with entire project

### Usage

There are some constants in this project used for naming all files that we need

In class CSVReaderWithAutoHeader:
CSV_FILE_INPUT - this CSV is an input and located in project folder, named by default "info.csv"
CSV_FILE_BAD_DATA - this is an output CSV with bad data, named "bad-data" + timestamp

In class DatabaseManager:
URL_TO_FILE_DIRECTORY - url to the folder where will be created SQLite DB, by default it will be created in project folder
DB_NAME - name of the SQLite DB file

After creating DB, we also create a table, it's name is "csv_imported_data" and is hardcoded (feel free to change it if you need)

Run this project and you will get this:
- SQLite DB "testdb" with all valid data in table "csv_imported_data"
- CSV-file with invalid data
- Log file with information about data processed

## Built With

* [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/) - IDE used
* [Maven](https://maven.apache.org/) - Dependency Management

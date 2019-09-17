import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CSVReaderWithAutoHeader {

    private static final String CSV_FILE_INPUT = "./info.csv";
    private static final String CSV_FILE_BAD_DATA = "./bad-data-" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date().getTime()) + ".csv";

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        final int batchSize = 1000;
        int validRecords = 0;
        int invalidRecords = 0;
        int totalRecords = 0;

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        DatabaseManager.createNewDatabase();
        DatabaseManager.createNewTable();

        String sqlInsert = "INSERT INTO csv_imported_data(A, B, C, D, E, F, G, H, I, J) VALUES(?,?,?,?,?,?,?,?,?,?)";

        Connection connection = DatabaseManager.connect();
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement(sqlInsert);

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE_BAD_DATA));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));

        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_INPUT));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());

        ) {
            // check every record
            for (CSVRecord csvRecord : csvParser) {
                boolean isValidRecord = true;
                for (String s : csvRecord
                     ) {
                    // if there is no value in some column we write this record within file with bad data
                    if (s.isBlank() || s.isEmpty()) {
                        isValidRecord = false;

                        csvPrinter.printRecord(csvRecord.get("A"), csvRecord.get("B"),
                                csvRecord.get("C"), csvRecord.get("D"),
                                csvRecord.get("E"), csvRecord.get("F"),
                                csvRecord.get("G"), csvRecord.get("H"),
                                csvRecord.get("I"), csvRecord.get("J"));

                        invalidRecords++;
                        break;
                    }

                }

                // prepare valid records for writing in DB
                if (isValidRecord) {
                    validRecords++;
                    ps.setString(1, csvRecord.get("A"));
                    ps.setString(2, csvRecord.get("B"));
                    ps.setString(3, csvRecord.get("C"));
                    ps.setString(4, csvRecord.get("D"));
                    ps.setString(5, csvRecord.get("E"));
                    ps.setString(6, csvRecord.get("F"));
                    ps.setString(7, csvRecord.get("G"));
                    ps.setString(8, csvRecord.get("H"));
                    ps.setString(9, csvRecord.get("I"));
                    ps.setString(10, csvRecord.get("J"));

                    // use batches instead of executeQuery for optimization
                    ps.addBatch();

                    if (validRecords % batchSize == 0) {
                        ps.executeBatch();
                    }
                }
                totalRecords++;
                }
            }

            csvPrinter.flush();

        // this line execution writes all remaining records
        ps.executeBatch();

        connection.commit();
        ps.close();
        connection.close();

        // here we make a log file
        try {
            String lineSeparator = System.getProperty("line.separator");

            // this block configure the logger with handler and formatter
            fh = new FileHandler("./InfoLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log message into log file
            logger.info("Valid records: " + validRecords + lineSeparator +
                    "Invalid records: " + invalidRecords + lineSeparator +
                    "Total records: " + totalRecords);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
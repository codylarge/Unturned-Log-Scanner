import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Log {

    private File logFile;
    private List<String> fileLines;

    public Log(int logNumber) {
        createFile(logNumber);
        if (logFile.isFile())
            createFileLines();
    }

    // Initialize this.logFile
    private void createFile(int logNumber) {
        String filePath = "resources/logs/Server_saved (" + logNumber + ")" + ".log";
        logFile = new File(filePath);

        if (!logFile.exists()) {
            System.err.println("Log file not found: " + filePath);
        } else {
            System.out.println("Log file found: " + filePath);
        }
    }

    // Initialize this.fileLines
    private void createFileLines() {
        if (logFile.exists() && logFile.isFile()) {
            try {
                fileLines = Files.readAllLines(Paths.get(logFile.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Unable to read file: " + logFile.getPath());
        }
    }

    public ArrayList<String> getPlayerConnects() {
        boolean nextLineGood = false;
        ArrayList<String> connectionLines = new ArrayList<>();
        for (String line : fileLines) {
            if (line.contains("Accepting queued player")) {
                connectionLines.add(line);
                nextLineGood = true;
            } else if (nextLineGood) {
                nextLineGood = false;
                connectionLines.add(line);

                // Uncommon case where user spawns obstructed in which case IP is not displayed. Must be ignored with Account removed to avoid mismatch in data
                if (line.contains("it was obstructed")) {
                    connectionLines.removeLast(); // remove the current line
                    if (!connectionLines.isEmpty()) {
                        connectionLines.removeLast(); // remove the previous line
                    }
                }
            }
        }
        return connectionLines;
    }

    public Object[][] extractAccountIpPair(){
        ArrayList<String> connectionData = this.getPlayerConnects();
        ArrayList<String> ips = new ArrayList<>();
        ArrayList<Account> accounts = new ArrayList<>(); // Corresponding accounts to ips

        // for(String line : connectionData) System.out.println("Connection data line: " + line); // DEBUG
        return null;
    }

    public ArrayList<String> extractIps() {
        ArrayList<String> connectionData = this.getPlayerConnects();
        ArrayList<String> ips = new ArrayList<>();
        for (String line : connectionData) {
            if (line.contains("BattlEye Print: Player")) {
                int endIndex = line.lastIndexOf(")");
                int startIndex = line.lastIndexOf("(", endIndex);
                if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                    String ipPort = line.substring(startIndex + 1, endIndex);
                    String ip = ipPort.split(":")[0];
                    ips.add(ip);
                }
            }
        }
        return ips;
    }


    public ArrayList<Account> extractAccounts() {
        ArrayList<String> connectionData = this.getPlayerConnects();

        ArrayList<Account> accounts = new ArrayList<>(); // Corresponding accounts to ips
        String steamID = null;
        String playerName = "";

        for(String line : connectionData) {
            if (line.contains("Accepting queued player")) {
                // Extract SteamID
                int startIndex = line.indexOf("player ") + 7; // starting at player skip 7 spaces and start id there
                int endIndex = line.indexOf("[0]"); // Find the index of [0] to end the SteamID extraction
                if (endIndex != -1) {
                    steamID = line.substring(startIndex, endIndex).trim(); // Extract and trim the SteamID
                }

                // Extract player's name
                int nameStartIndex = line.indexOf("\"") + 1; // Find the index of the first quote
                int nameEndIndex = line.indexOf("\"", nameStartIndex); // Find the index of the second quote

                if (nameEndIndex != -1 && nameEndIndex > nameStartIndex) {
                    playerName = line.substring(nameStartIndex, nameEndIndex);
                }
                accounts.add(new Account(steamID, playerName));
                //System.out.println("Adding line: " + line); // DEBUG
            }
        }
        return accounts;
    }

    public void removeDates() {
        this.fileLines.replaceAll(s -> {
            return s.replaceFirst("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] ", "");
        });
    }

    public void printFileLines() {
        for (String line : fileLines)
            System.out.println(line);
    }

    public List<String> getFileLines() {
        return fileLines;
    }

    public File getLogFile() {
        return logFile;
    }


}

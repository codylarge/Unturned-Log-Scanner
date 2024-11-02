import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerData
{
    private HashMap<String, ArrayList<Account>> playersMap;

    public ServerData() {
        this.playersMap = new HashMap<>();
        this.loadPlayerData();
    }

    public void addAccount(String ip, Account account) {
        // System.out.println("Adding account " + account.getName() + " with IP: " + ip); // DEBUG
        ArrayList<Account> accounts = playersMap.get(ip);
        // If there is no existing account list, create one
        if (accounts == null) {
            accounts = new ArrayList<>();
            accounts.add(account);
            playersMap.put(ip, accounts);
        } else {
            Account existingAccount = null;
            for (Account acc : accounts) {
                if (acc.getSteamId().equals(account.getSteamId())) {
                    existingAccount = acc;
                    break;
                }
            }
            if (existingAccount != null) {
                // Previous Player
                existingAccount.increaseInstances();
            } else {
                // Alt account
                accounts.add(account);
            }
        }
    }

    public void logConnectionData(ArrayList<String> ips, ArrayList<Account> accounts) {
        // System.out.println("ips: " + ips.size() + " accounts: " + accounts.size()); // DEBUG
        for (int i = 0; i < ips.size(); i++) {
            String ip = ips.get(i);
            Account account = accounts.get(i);

            // Add the IP-account pair to the playersMap
            addAccount(ip, account);
        }
    }

    public void printPlayersMap() {
        for (String ip : playersMap.keySet()) {
            System.out.println("IP: " + ip);
            ArrayList<Account> accounts = playersMap.get(ip);

            for (Account account : accounts) {
                System.out.println(account);
            }
        }
    }

    // Write playersMap to players.txt file
    public void outputPlayerData() {
        File file = new File("resources/outputs/players.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String ip : playersMap.keySet()) {
                writer.write("IP: " + ip + "\n");
                for (Account account : playersMap.get(ip)) {
                    writer.write(" - " + account + "\n");
                }
            }
            System.out.println("Player data successfully written to players.txt");
        } catch (IOException e) {
            System.err.println("Error writing player data to file: " + e.getMessage());
        }
    }

    // Load the playersMap from the file
    public void loadPlayerData() {
        File file = new File("resources/outputs/players.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentIP = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("IP: ")) {
                    currentIP = line.substring(4); // Extract IP address
                    this.playersMap.put(currentIP, new ArrayList<>());
                } else if (line.startsWith(" - ") && currentIP != null) {
                    String accountData = line.substring(3); // Get account details after " - "
                    Account account = Account.parseAccount(accountData);
                    this.playersMap.get(currentIP).add(account);
                }
            }
            System.out.println("Player data successfully loaded from players.txt");
        } catch (IOException e) {
            System.err.println("Error loading player data from file: " + e.getMessage());
        }
    }

}

import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) {
        ServerData data = new ServerData();

        readAllLogs(data, 1, 60);

        data.printPlayersMap();
        data.outputPlayerData();
    }

    static void readAllLogs(ServerData data, int min, int max) {
        ArrayList<Account> accounts;
        ArrayList<String> ips;

        for (int i = min; i < max; i++) {
            Log log = new Log(i);
            if(log.getLogFile().exists()) {
                // System.out.println("Viewing log " + i); // DEBUG
                accounts = log.extractAccounts();
                ips = log.extractIps();
                data.logConnectionData(ips, accounts);
            }
        }
    }
}
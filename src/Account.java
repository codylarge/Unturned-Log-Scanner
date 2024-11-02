public class Account {
    private final String name;
    private final String steamId;   // User ID
    private int instances; // Number of times user joined the server on this account

    public Account(String steamId, String name) {
        this.steamId = steamId;
        this.name = name;
        this.instances = 1;
    }

    public Account(String steamId, String name, int instances) {
        this.steamId = steamId;
        this.name = name;
        this.instances = instances;
    }

    public void increaseInstances() {
        this.instances++;
    }

    public String getSteamId() {
        return steamId;
    }

    public String getName() {
        return name;
    }

    public int getInstances() {
        return instances;
    }

    @Override
    public String toString() {
        return "User: " + name + "(" + steamId + "), Instances: " + instances;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Account comp)
            return this.steamId.equals(comp.getSteamId());
        return false;
    }

    public static Account parseAccount(String line) {
        String playerName = null;
        String steamID = null;
        int instances = 1;

        int userStartIndex = line.indexOf("User: ") + 6; // 6 is the length of "User: "
        int steamIdStartIndex = line.lastIndexOf("(");

        playerName = line.substring(userStartIndex, steamIdStartIndex).trim();

        int steamIdEndIndex = line.lastIndexOf(")");
        steamID = line.substring(steamIdStartIndex + 1, steamIdEndIndex).trim();

        int instancesStartIndex = line.indexOf("Instances: ") + 11;
        instances = Integer.parseInt(line.substring(instancesStartIndex));

        return new Account(steamID, playerName, instances);
    }
}

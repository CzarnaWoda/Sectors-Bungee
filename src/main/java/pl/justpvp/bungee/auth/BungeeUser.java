package pl.justpvp.bungee.auth;



import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.util.GsonUtil;

import java.sql.SQLException;
import java.util.UUID;

public class BungeeUser {

    private UUID uuid;
    private String lastName, firstIP, lastIP, password, lastSector;
    private boolean hasReservation;

    private boolean premium;

    public BungeeUser(UUID uuid, String name, String firstip, String lastip, String password, boolean premium){
        this.uuid = uuid;
        this.lastName = name;
        this.firstIP = firstip;
        this.lastIP = lastip;
        this.password = password;
        this.premium = premium;
        this.hasReservation = false;
        this.lastSector = "DEFAULT";
    }



    public void insert() {
        RedisChannel.INSTANCE.PROXY_USERS.putAsync(getUuid(), GsonUtil.toJson(this));
        //BungeePlugin.INSTANCE.getMySQL().generateStatement("INSERT INTO `proxy_users` (`id`,`uuid`,`firstIP`,`lastIP`,`lastName`,`password`,`premium`) VALUES (NULL,'" + uuid + "', '" + firstIP + "', '" + lastIP + "', '" + lastName + "', '" + password + "', '" + (premium ? 1 : 0) + "');").executeUpdate();
    }


    public boolean hasSlotReservation() {
        return hasReservation;
    }

    public void setHasReservation(boolean hasReservation) {
        this.hasReservation = hasReservation;
    }

    public void updateall() {
        insert();
    }

    public boolean isPremium() {
        return premium;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPassword() {
        return password;
    }

    public String getLastIP() {
        return lastIP;
    }

    public String getFirstIP() {
        return firstIP;
    }

    public String getLastName() {
        return lastName;
    }


    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setFirstIP(String firstIP) {
        this.firstIP = firstIP;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastIP(String lastIP) {
        this.lastIP = lastIP;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getLastSector() {
        return lastSector;
    }

    public void setLastSector(String lastSector) {
        this.lastSector = lastSector;
    }
}

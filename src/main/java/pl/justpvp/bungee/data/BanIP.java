package pl.justpvp.bungee.data;

import lombok.Data;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.util.GsonUtil;

@Data
public class BanIP {

    private String reason,admin,ip;
    private long createTime, expireTime;
    private boolean unban;

    public BanIP(String ip, String reason, String admin, long expireTime){

        this.ip = ip;
        this.reason = reason;
        this.admin = admin;
        this.createTime = System.currentTimeMillis();
        this.expireTime = expireTime;
        this.unban = false;

        insert();

    }

    public BanIP(String reason, String admin, String ip, long createTime, long expireTime, boolean unban){

        this.reason = reason;
        this.admin = admin;
        this.ip = ip;
        this.createTime = createTime;
        this.expireTime = expireTime;
        this.unban = unban;

    }
    public void insert(){
        RedisChannel.INSTANCE.IP_BANS.putAsync(this.ip, GsonUtil.toJson(this));
    }

    public void delete(){
        RedisChannel.INSTANCE.IP_BANS.removeAsync(this.ip);
    }
}

package pl.justpvp.bungee.data;

import lombok.Data;
import pl.justpvp.bungee.redis.channels.RedisChannel;
import pl.justpvp.bungee.util.GsonUtil;

import java.util.UUID;

@Data
public class Ban {

    private UUID uuid;
    private String reason,admin;
    private long createTime,expireTime;
    private boolean unban;

    public Ban(UUID uuid, String reason, String admin, long createTime, long expireTime){
        this.uuid = uuid;
        this.reason = reason;
        this.admin = admin;
        this.createTime = createTime;
        this.expireTime = expireTime;
        this.unban = false;
        insert();
    }

    public boolean isAlive() {
        return this.unban || (this.expireTime != 0L && this.expireTime < System.currentTimeMillis());
    }

    public Ban(UUID uuid, String reason, String admin, long createTime, long expireTime, boolean unban){
        this.uuid = uuid;
        this.reason = reason;
        this.admin = admin;
        this.createTime = createTime;
        this.expireTime = expireTime;
        this.unban = unban;
    }

    public void insert() {
        RedisChannel.INSTANCE.BANS.putAsync(this.uuid, GsonUtil.toJson(this));
    }
    public void delete(){
        RedisChannel.INSTANCE.BANS.removeAsync(this.uuid);
    }
}

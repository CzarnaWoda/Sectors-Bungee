# 🔌 Sectors-Bungee

> BungeeCord plugin dla systemu wielosektorowego - proxy layer z Redis Pub/Sub

[![BungeeCord](https://img.shields.io/badge/BungeeCord-1.8--1.20-orange.svg)](https://www.spigotmc.org/wiki/bungeecord/)
[![Redis](https://img.shields.io/badge/Redis-Powered-red.svg)](https://redis.io/)
[![Redisson](https://img.shields.io/badge/Redisson-3.x-blue.svg)](https://redisson.org/)

## 📋 O Projekcie

Plugin BungeeCord który jest proxy layer dla systemu sektorów. Obsługuje:
- Multi-proxy setup (kilka BungeeCord proxy)
- Tracking online players przez Redis RSet
- Ban system (UUID + IP bans)
- Whitelist system
- Authentication system (login/register)
- Komunikację między Spigot sectors a Bungee proxies

**Legacy code** z 2019-2020, ale działający w produkcji z 2000+ graczy.

## 🏗️ Architektura

```
┌─────────────────────────────────────────────┐
│            Redis Cluster                    │
│  - RTopic (Pub/Sub messaging)              │
│  - RMap (Bans, Users)                       │
│  - RSet (Online players, Whitelist)         │
└──────────────┬──────────────────────────────┘
               │
    ┌──────────┼──────────┐
    │          │          │
┌───▼────┐ ┌──▼─────┐ ┌──▼─────┐
│ Proxy1 │ │ Proxy2 │ │ Proxy3 │
│ Bungee │ │ Bungee │ │ Bungee │
└────┬───┘ └───┬────┘ └───┬────┘
     │         │          │
     └─────────┴──────────┘
          Load Balanced
               ↓
    ┌──────────────────────┐
    │  Spigot Sectors      │
    │  (PVP-1, PVP-2, etc) │
    └──────────────────────┘
```

## 📦 Struktura Pakietów

```
pl.justpvp.bungee/
│
├── BungeePlugin.java              # Main class
│
├── auth/                           # Authentication system
│   ├── BungeeUser.java             # User object (UUID, IPs, password)
│   ├── BungeeUserManager.java      # User management
│   ├── LoginManager.java           # Login/Register logic
│   └── MotdCommand.java            # MOTD configuration
│
├── commands/                       # Admin commands
│   ├── BanCommand.java             # /ban <player> <reason>
│   ├── BanIPCommand.java           # /banip <ip> <reason>
│   ├── TempBanCommand.java         # /tempban <player> <time> <reason>
│   ├── UnBanCommand.java           # /unban <player>
│   ├── UnBanIPCommand.java         # /unbanip <ip>
│   ├── BanInfoCommand.java         # /baninfo <player>
│   ├── WhitelistCommand.java       # /whitelist <add/remove> <player>
│   ├── RegisterCommand.java        # /register <password>
│   ├── LoginCommand.java           # /login <password>
│   └── BuildAddCommand.java        # Allow player to build
│
├── configs/                        # Configuration files
│   ├── ProxyConfig.java            # Current proxy config
│   ├── ProxiesConfig.java          # Multi-proxy setup
│   └── SectorsConfig.java          # Sectors mapping
│
├── data/                           # Data models
│   ├── Ban.java                    # Ban object (UUID, reason, admin, times)
│   └── BanIP.java                  # IP Ban object
│
├── listeners/                      # Event listeners
│   ├── ConnectEvent.java           # Player join handling
│   ├── DisconnectEvent.java        # Player quit handling
│   ├── PingEvent.java              # Server list ping
│   ├── TabCompleteListener.java    # Tab complete blocker
│   └── BlazingPackAuthListener.java # BlazingPack auth
│
├── managers/                       # Management systems
│   ├── BanManager.java             # Ban storage & checks
│   ├── BanIPManager.java           # IP ban storage
│   ├── WhitelistManager.java       # Whitelist storage
│   └── AllowBuildManager.java      # Build permission
│
├── packets/                        # ⭐ Packet system
│   ├── RedisPacket.java            # Base packet class
│   ├── handler/
│   │   ├── PacketHandler.java      # Handler interface
│   │   └── PacketHandlerImpl.java  # ⭐ Handler logic
│   ├── manager/
│   │   └── PacketManager.java      # Packet ID registry
│   └── impl/                       # Packet implementations
│       ├── proxy/                  # Proxy packets
│       │   ├── ProxyEnablePacket.java
│       │   ├── ProxyDisablePacket.java
│       │   └── ProxyStatusPacket.java
│       ├── user/                   # User packets
│       │   ├── ProxyUserRegisterPacket.java
│       │   ├── UserChangeSectorPacket.java
│       │   ├── ProxyJoinPacket.java
│       │   └── ProxyLeavePacket.java
│       ├── bans/                   # Ban packets
│       │   ├── CreateBanPacket.java
│       │   ├── UnBanPacket.java
│       │   ├── DeleteBanPacket.java
│       │   ├── CreateIPBanPacket.java
│       │   └── DeleteIPBanPacket.java
│       ├── sectors/                # Sector packets
│       │   └── SectorStatusPacket.java
│       ├── configs/                # Config packets
│       │   └── ConfigPacket.java
│       ├── whitelist/              # Whitelist packets
│       │   ├── WhitelistAddPacket.java
│       │   ├── WhitelistRemovePacket.java
│       │   ├── WhitelistEnablePacket.java
│       │   └── WhitelistDisablePacket.java
│       └── chat/                   # Chat packets
│           └── GlobalChatMessage.java
│
├── proxies/                        # ⭐ Proxy management
│   ├── Proxy.java                  # Proxy instance
│   └── ProxyManager.java           # Registry
│
├── redis/                          # ⭐ Redis layer
│   ├── RedisManager.java           # Main Redis manager
│   ├── channels/
│   │   └── RedisChannel.java       # Redis structures
│   ├── client/
│   │   └── RedisClient.java        # Send packets
│   ├── listeners/                  # Redis listeners
│   │   ├── GlobalPacketListener.java
│   │   └── ProxiesPacketListener.java
│   └── factory/
│       └── RedisFactory.java       # Redisson setup
│
├── sectors/                        # Sector tracking
│   ├── Sector.java                 # Sector object
│   └── SectorManager.java          # Sector registry
│
├── thread/                         # Async tasks
│   ├── ProxyStatusTask.java        # Status updates
│   └── api/
│       └── ScheduledTask.java      # Task interface
│
└── util/                           # Utilities
    ├── ChatUtil.java               # Color codes
    ├── GsonUtil.java               # JSON serialization
    └── Util.java                   # Date formatting
```

## ⚙️ Kluczowe Klasy

### BungeePlugin.java - Main Class

```java
public class BungeePlugin extends Plugin {
    
    @Override
    public void onEnable() {
        // 1. Setup configs
        ProxyConfig.saveDefaultConfig();
        new ProxyConfig();
        
        // 2. Setup Redis
        RedisManager.setup();
        RedisChannel.INSTANCE.setupChannels();
        
        // 3. Register Redis listeners
        new GlobalPacketListener(ChannelType.GLOBAL_PACKETS, ...);
        new ProxiesPacketListener(ChannelType.PACKET_TO_PROXIES, ...);
        
        // 4. Setup managers
        bungeeUserManager = new BungeeUserManager(this);
        loginManager = new LoginManager(this);
        BanManager.setup();
        WhitelistManager.setup();
        
        // 5. Clear proxy online players
        Proxy proxy = ProxyManager.getCurrentProxy();
        proxy.getRedisOnlinePlayers().clear();
        
        // 6. Send ProxyEnablePacket
        RedisClient.sendProxiesPacket(new ProxyEnablePacket(proxyName));
        
        // 7. Start tasks
        new ProxyStatusTask(executorService).runTask();
        
        // 8. Register listeners & commands
        registerListeners();
        registerCommands();
    }
}
```

### RedisChannel.java - Redis Structures

```java
public class RedisChannel {
    public static RedisChannel INSTANCE = new RedisChannel();
    
    // Pub/Sub Topics
    public RTopic globalPacketTopic;      // Global packets
    public RTopic proxiesPacketTopic;     // Proxy-specific packets
    
    // Persistent Maps
    public RMap<UUID, String> BANS;       // Bans by UUID
    public RMap<UUID, String> PROXY_USERS; // Users
    public RMap<String, String> IP_BANS;  // IP bans
    
    // Sets
    public RSet<String> WHITELISTED;      // Whitelisted players
    public RSet<String> ALLOWED_BUILD;    // Build permissions
    public RSet<String> ONLINE_PLAYERS;   // All online players
    
    // BitSet
    public RBitSet WHITELIST_ENABLED;     // Whitelist on/off
    
    public void setupChannels() {
        globalPacketTopic = RedisManager.getRedisConnection()
                                        .getTopic("globalPacketTopic");
        proxiesPacketTopic = RedisManager.getRedisConnection()
                                         .getTopic("proxiesPacketTopic");
        
        BANS = RedisManager.getRedisConnection().getMap("BANS");
        PROXY_USERS = RedisManager.getRedisConnection().getMap("PROXY_USERS");
        // ... etc
    }
}
```

### Proxy.java - Proxy Instance

```java
public class Proxy {
    private final String proxyName;
    private final RSet<String> redisOnlinePlayers;
    private Long lastUpdate;
    
    public Proxy(String proxyName) {
        this.proxyName = proxyName;
        // RSet w Redis - "{proxyName}-online"
        this.redisOnlinePlayers = RedisManager.getRedisConnection()
                                              .getSet(proxyName + "-online");
        this.lastUpdate = -1L;
    }
    
    public void addOnlinePlayerToRedis(String name) {
        redisOnlinePlayers.addAsync(name);
    }
    
    public void removeOnlinePlayerToRedis(String name) {
        redisOnlinePlayers.removeAsync(name);
    }
}
```

### RedisClient.java - Send Packets

```java
public class RedisClient {
    
    // Broadcast do wszystkich (Bungee + Spigot)
    public static void sendGlobalPacket(final RedisPacket packet) {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.GLOBAL_PACKETS)
                    .sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }
    
    // Do wszystkich Spigot sectors
    public static void sendSectorsPacket(final RedisPacket packet) {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_SPIGOTS)
                    .sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }
    
    // Do wszystkich Bungee proxies
    public static void sendProxiesPacket(final RedisPacket packet) {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_PROXIES)
                    .sendMessage(packetID + "@" + GsonUtil.toJson(packet));
    }
    
    // Do konkretnego sektora
    public static void sendPacket(final RedisPacket packet, final String serverName) {
        final int packetID = PacketManager.getPacketID(packet.getClass());
        RedisManager.getRedisListener(ChannelType.PACKET_TO_SINGLE_SECTOR)
                    .sendMessage(serverName + "#" + packetID + "@" + GsonUtil.toJson(packet));
    }
}
```

### GlobalPacketListener.java - Receive Packets

```java
public class GlobalPacketListener<T extends String> extends RedisListener<T> {
    
    final PacketHandler packetHandler = new PacketHandlerImpl();
    
    public GlobalPacketListener(ChannelType type, RTopic rTopic) {
        super(type, rTopic);
        
        getTopic().addListener(String.class, (channel, packet) -> {
            // Format: "packetID@json"
            final String[] split = packet.split("@");
            
            // Get packet class by ID
            Class<? extends RedisPacket> clzPacket = 
                PacketManager.getPacketClass(Integer.parseInt(split[0]));
            
            if (clzPacket == null) return;
            
            // Deserialize
            final RedisPacket p = GsonUtil.fromJson(split[1], clzPacket);
            
            // Handle
            p.handlePacket(packetHandler);
        });
    }
}
```

## 🔄 Packet Flow Example

### Ban Player Flow

```
Admin on Spigot PVP-1: /ban Player123 Cheating
    ↓
Spigot creates: CreateBanPacket(uuid, "Cheating", "Admin", times)
    ↓
sectorClient.sendGlobalPacket(packet)
    ↓
Redis publish to globalPacketTopic
    ↓
═══════════════════════════════════════════════════
ALL BungeeCord proxies receive packet
═══════════════════════════════════════════════════
    ↓
GlobalPacketListener deserializes
    ↓
packet.handlePacket(handler)
    ↓
PacketHandlerImpl.handle(CreateBanPacket packet):
  1. Create Ban object
  2. BanManager.getBans().put(uuid, ban)
  3. Find player on THIS proxy
  4. If online → disconnect with ban message
```

### Proxy Status Update

```java
// ProxyStatusTask.java - runs every 3 seconds
executorService.scheduleWithFixedDelay(() -> {
    ProxyStatusPacket packet = new ProxyStatusPacket(proxyName);
    RedisClient.sendProxiesPacket(packet);
}, 3, 3, TimeUnit.SECONDS);

// All proxies receive and update:
@Override
public void handle(ProxyStatusPacket packet) {
    final Proxy proxy = ProxyManager.getProxy(packet.getProxyName());
    if (proxy != null) {
        proxy.setLastUpdate(System.currentTimeMillis());
    }
}
```

## 🔑 Key Features

### Multi-Proxy Support

```java
// ProxiesConfig.java - defines all proxies
proxies:
  - name: "PROXY-1"
    online: []
  - name: "PROXY-2"
    online: []
  - name: "PROXY-3"
    online: []
```

Każdy proxy:
- Ma własny RSet w Redis: `"{proxyName}-online"`
- Wysyła ProxyStatusPacket co 3 sekundy
- Trackuje swoich graczy
- Total online = suma wszystkich proxy

### Ban System

**UUID Bans:**
```java
public class Ban {
    private UUID uuid;
    private String reason;
    private String admin;
    private long createTime;
    private long expireTime;  // -1 = permanent
    private boolean unban;
}
```

**IP Bans:**
```java
public class BanIP {
    private String ip;
    private String reason;
    private String admin;
    private long createTime;
    private long expireTime;
    private boolean unban;
}
```

**Check on connect:**
```java
// ConnectEvent.java
if (BanManager.isBanned(uuid) || BanIPManager.isBanned(ip)) {
    event.setCancelled(true);
    event.setCancelReason(formatBanMessage(ban));
}
```

### Whitelist System

```java
// WhitelistManager.java
private static RSet<String> WHITELISTED;
private static RBitSet WHITELIST_ENABLED;

public static boolean isEnabled() {
    return WHITELIST_ENABLED.get(0);
}

public static boolean isWhitelisted(String name) {
    return WHITELISTED.contains(name.toLowerCase());
}

// ConnectEvent.java
if (WhitelistManager.isEnabled() && 
    !WhitelistManager.isWhitelisted(playerName)) {
    event.setCancelled(true);
    event.setCancelReason("§cWhitelista jest włączona!");
}
```

### Authentication System

```java
// BungeeUser.java
public class BungeeUser {
    private UUID uuid;
    private String lastName;
    private String firstIP;
    private String lastIP;
    private String password;      // hashed
    private boolean premium;
    private boolean logged;
    private String lastSector;
    private boolean hasReservation;
}

// LoginManager.java - join throttling
private int currentJoins = 0;
private final int maxJoinsPerInterval = 10;  // 10 joins per 5 seconds

public boolean canJoin() {
    return currentJoins < maxJoinsPerInterval;
}
```

## 📊 Konfiguracja

### proxy.yml

```yaml
currentproxy:
  name: "PROXY-1"

redis:
  host: "localhost"
  port: 6379
  password: ""

motd:
  playersinfo:
    - "&7Online: &a{ONLINE}"
    - "&7Graczy: &a{MAX}"
```

### proxies.yml

```yaml
proxies:
  - name: "PROXY-1"
  - name: "PROXY-2"
  - name: "PROXY-3"
```

### sectors.yml

```yaml
sectors:
  - name: "PVP-1"
    online: []
  - name: "PVP-2"
    online: []
  - name: "SPAWN"
    online: []
```

## 🚀 Features

✅ **Multi-proxy setup** - wiele BungeeCord dla load balancing  
✅ **Redis online tracking** - RSet per proxy  
✅ **Ban system** - UUID + IP bans  
✅ **Whitelist system** - Redis RSet  
✅ **Authentication** - Login/Register  
✅ **Join throttling** - Anti-spam protection  
✅ **Packet communication** - Redis Pub/Sub  
✅ **Build permissions** - Allow specific players  
✅ **Status monitoring** - Proxy & Sector status  

## 📝 Example Usage

### Ban player from Spigot

```java
// On Spigot server
CreateBanPacket packet = new CreateBanPacket(
    uuid, 
    "Hacking", 
    "Admin", 
    System.currentTimeMillis(),
    System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)
);
sectorClient.sendGlobalPacket(packet);

// All proxies receive and store ban
// If player online on any proxy → disconnect
```

### Update config from Spigot

```java
ConfigPacket packet = new ConfigPacket(
    "motd.playersinfo",
    "&7Online: &a{ONLINE}"
);
RedisClient.sendProxiesPacket(packet);

// All proxies update their config
```

## 🐛 Known Issues

- Legacy code (2019-2020)
- Some commented out code (local player tracking)
- Hardcoded "justpvp.pl" references
- No async ban checks on join (może lagować przy dużej bazie)

## 👨‍💻 Autor

**Mateusz (CzarnaWoda)**
- Projekt: **JustPVP Network**
- Period: 2019-2022
- Scale: 2000+ concurrent players

---

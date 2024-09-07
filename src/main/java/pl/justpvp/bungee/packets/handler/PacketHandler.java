package pl.justpvp.bungee.packets.handler;


import pl.justpvp.bungee.packets.bans.*;
import pl.justpvp.bungee.packets.chat.GlobalChatMessage;
import pl.justpvp.bungee.packets.impl.allowedbuild.AllowBuildPacket;
import pl.justpvp.bungee.packets.impl.bans.BanAddPacket;
import pl.justpvp.bungee.packets.impl.configs.ConfigPacket;
import pl.justpvp.bungee.packets.impl.itemshop.AddSlotReservationPacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyDisablePacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyEnablePacket;
import pl.justpvp.bungee.packets.impl.proxy.ProxyStatusPacket;
import pl.justpvp.bungee.packets.impl.sectors.SectorStatusPacket;
import pl.justpvp.bungee.packets.impl.user.*;
import pl.justpvp.bungee.packets.whitelist.WhitelistAddPacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistDisablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistEnablePacket;
import pl.justpvp.bungee.packets.whitelist.WhitelistRemovePacket;

public interface PacketHandler {

    void handle(ProxyUserRegisterPacket packet);

    void handle(ProxyEnablePacket packet);

    void handle(ProxyDisablePacket packet);

    void handle(ProxyJoinPacket packet);

    void handle(ProxyLeavePacket packet);

    void handle(UserChangeSectorPacket packet);

    void handle(final ProxyStatusPacket packet);

    void handle(final SectorStatusPacket packet);

    void handle(final CreateBanPacket packet);

    void handle(final UnBanPacket packet);

    void handle(final DeleteBanPacket packet);

    void handle(final GlobalChatMessage packet);

    void handle(final BungeeUserUpdatePacket packet);

    void handle(final CreateIPBanPacket packet);

    void handle(final DeleteIPBanPacket packet);

    void handle(final AddSlotReservationPacket packet);

    void handle(final ConfigPacket packet);

    void handle(final WhitelistAddPacket packet);

    void handle(final WhitelistRemovePacket packet);

    void handle(final WhitelistEnablePacket packet);

    void handle(final WhitelistDisablePacket packet);

    void handle(final AllowBuildPacket allowBuildPacket);

    void handle(final BanAddPacket packet);

}

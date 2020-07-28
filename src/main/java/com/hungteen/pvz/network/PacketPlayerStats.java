package com.hungteen.pvz.network;

import java.util.function.Supplier;

import com.hungteen.pvz.capabilities.player.PVZGuiTabPlayerData;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketPlayerStats{

	private int type;
	private int data;
	
	public PacketPlayerStats(int x,int y) {
		this.type=x;
		this.data=y;
//		PVZMod.LOGGER.debug(type+" x "+data);
	}
	
	public PacketPlayerStats(PacketBuffer buffer) {
		this.type=buffer.readInt();
		this.data=buffer.readInt();
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.type);
		buffer.writeInt(this.data);
	}

	public static class Handler {
		public static void onMessage(PacketPlayerStats message, Supplier<NetworkEvent.Context> ctx) {
//			PVZMod.LOGGER.debug(message.type+" y "+message.data);
		    ctx.get().enqueueWork(()->{
			    PVZGuiTabPlayerData.setPlayerData(message.type,message.data);
//			    PVZMod.LOGGER.debug(message.type+" z "+message.data);
		    });
		    ctx.get().setPacketHandled(true);
	    }
	}
}

package de.maxhenkel.fakeblocks;

import de.maxhenkel.fakeblocks.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main{
    public static final String MODID = "fakeblocks";
    public static final String VERSION = "1.1.0";
    public static final int VERSION_NUMBER = 2;
    
    private Config config;
    private Events events;

	@Instance
    private static Main instance;

	@SidedProxy(clientSide="de.maxhenkel.fakeblocks.proxy.ClientProxy", serverSide="de.maxhenkel.fakeblocks.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event){
    	Configuration c=null;
    	try{
			c=new Configuration(event.getSuggestedConfigurationFile());
		}catch(Exception e){
			e.printStackTrace();
		}
		config=new Config(c);
		
    	instance=this;
    	this.events=new Events();
    	MinecraftForge.EVENT_BUS.register(events);

		proxy.preinit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	 proxy.init(event);
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event){
		proxy.postinit(event);
    }
    
    public Config getConfig() {
		return config;
	}
	
    public Events getEvents() {
		return events;
	}

	public static Main getInstance() {
		return instance;
	}
	
}

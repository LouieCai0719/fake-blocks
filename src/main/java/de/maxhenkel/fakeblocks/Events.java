package de.maxhenkel.fakeblocks;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Events {

	public static final String KEY_CHECK_UPDATES = "check_updates";

	private boolean checkUpdates;
	private boolean updateShown;

	public Events() {
		this.checkUpdates = Main.getInstance().getConfig().getBoolean(KEY_CHECK_UPDATES, true);
		this.updateShown = false;
	}

	@SubscribeEvent
	public void playerJoin(EntityJoinWorldEvent event) {
		if (event.isCanceled()) {
			return;
		}

		if (!(event.getEntity() instanceof EntityPlayer)) {
			return;
		}

		if (!event.getWorld().isRemote) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) event.getEntity();

		if (player.isDead) {
			return;
		}

		if (!checkUpdates) {
			return;
		}

		if (updateShown) {
			return;
		}

		UpdateChecker checker = new UpdateChecker(new UpdateChecker.IUpdateCheckResult() {
			@Override
			public void onResult(boolean isAvailable) {
				if (isAvailable) {

					String msg = "[" + new TextComponentTranslation("message.name", new Object[0]).getFormattedText()
							+ "] " + new TextComponentTranslation("message.update", new Object[0]).getFormattedText()
							+ " ";

					ClickEvent openUrl = new ClickEvent(Action.OPEN_URL,
							"https://minecraft.curseforge.com/projects/fakeblocks");
					Style style = new Style();

					style.setClickEvent(openUrl);
					style.setUnderlined(true);
					style.setColor(TextFormatting.GREEN);
					style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(
							new TextComponentTranslation("message.update.hover", new Object[0]).getFormattedText())));
					TextComponentString comp = new TextComponentString("[Download]");
					comp.setStyle(style);
					player.addChatMessage(new TextComponentString(msg).appendSibling(comp));
				}
			}
		}, Main.VERSION_NUMBER, "http://maxhenkel.de/update/fakeblocks_1.11.txt");
		checker.start();
		updateShown = true;
	}

}

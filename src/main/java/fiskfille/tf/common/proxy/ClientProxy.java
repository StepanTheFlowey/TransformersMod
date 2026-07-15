package fiskfille.tf.common.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fiskfille.tf.TFReflection;
import fiskfille.tf.client.gui.GuiOverlay;
import fiskfille.tf.client.keybinds.TFKeyBinds;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.client.render.block.*;
import fiskfille.tf.client.render.entity.*;
import fiskfille.tf.client.render.item.*;
import fiskfille.tf.client.render.tileentity.*;
import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.common.entity.*;
import fiskfille.tf.common.event.ClientEventHandler;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.item.armor.ItemTransformerArmor;
import fiskfille.tf.common.tick.ClientTickHandler;
import fiskfille.tf.common.tileentity.*;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import static fiskfille.tf.TransformersMod.mc;

public class ClientProxy extends CommonProxy {
	public static final GuiOverlay guiOverlay = new GuiOverlay();
	public static EntityClientPlayerMP fakePlayer;

	@Override
	public void preInit() {
		super.preInit();
		TFReflection.client();
		TFKeyBinds.register();

		registerEventHandler(new ClientEventHandler());
		registerEventHandler(new ClientTickHandler());
		registerEventHandler(guiOverlay);
	}

	@Override
	public void init() {
		super.init();
		TFModelRegistry.registerModels();

		RenderingRegistry.registerEntityRenderingHandler(EntityTankShell.class, new RenderTankShell());
		RenderingRegistry.registerEntityRenderingHandler(EntityMissile.class, new RenderMissile());
		RenderingRegistry.registerEntityRenderingHandler(EntityTransformiumSeed.class, new RenderTransformiumSeedEntity());
		RenderingRegistry.registerEntityRenderingHandler(EntityFlamethrowerFire.class, new RenderBlank());
		RenderingRegistry.registerEntityRenderingHandler(EntityLaserBeam.class, new RenderBlank());
		RenderingRegistry.registerEntityRenderingHandler(EntityBassCharge.class, new RenderBassCharge());
		RenderingRegistry.registerEntityRenderingHandler(EntityLaser.class, new RenderLaser());

		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.displayStation), new RenderItemDisplayStation());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.assemblyTable), new RenderItemTileEntity(TFBlocks.assemblyTable));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.energonProcessor), new RenderItemTileEntity(TFBlocks.energonProcessor));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.energonFluidTank), new RenderItemEnergonTank());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.transmitter), new RenderItemTransmitter());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.relayTower), new RenderItemRelayTower());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.relayTorch), new RenderItemTileEntity(TFBlocks.relayTorch));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.energyColumn), new RenderItemColumn());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.energyPort), new RenderItemTileEntity(TFBlocks.energyPort));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.isoCondenser), new RenderItemTileEntity(TFBlocks.isoCondenser));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TFBlocks.groundBridgeControlPanel), new RenderItemControlPanel());
		MinecraftForgeClient.registerItemRenderer(TFItems.displayVehicle, new RenderItemDisplayVehicle());
		MinecraftForgeClient.registerItemRenderer(TFItems.dataCore, new RenderItemDataCore());
		MinecraftForgeClient.registerItemRenderer(TFItems.powerCanister, new RenderItemPowerCanister());
		MinecraftForgeClient.registerItemRenderer(TFItems.skystrikesCrossbow, new RenderItemSkystrikesCrossbow());
		MinecraftForgeClient.registerItemRenderer(TFItems.purgesKatana, new RenderItemPurgesKatana());
		MinecraftForgeClient.registerItemRenderer(TFItems.vurpsSniper, new RenderItemVurpsSniper());
		MinecraftForgeClient.registerItemRenderer(TFItems.subwoofersBassBlaster, new RenderItemBassBlaster());
		MinecraftForgeClient.registerItemRenderer(TFItems.cloudtrapsFlamethrower, new RenderItemFlamethrower());
		registerArmorRenderer(TFItems.cloudtrapHelmet, TFItems.cloudtrapChestplate, TFItems.cloudtrapLeggings, TFItems.cloudtrapBoots);
		registerArmorRenderer(TFItems.skystrikeHelmet, TFItems.skystrikeChestplate, TFItems.skystrikeLeggings, TFItems.skystrikeBoots);
		registerArmorRenderer(TFItems.purgeHelmet, TFItems.purgeChestplate, TFItems.purgeLeggings, TFItems.purgeBoots);
		registerArmorRenderer(TFItems.vurpHelmet, TFItems.vurpChestplate, TFItems.vurpLeggings, TFItems.vurpBoots);
		registerArmorRenderer(TFItems.subwooferHelmet, TFItems.subwooferChestplate, TFItems.subwooferLeggings, TFItems.subwooferBoots);

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCrystal.class, new RenderCrystal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisplayPedestal.class, new RenderDisplayPedestal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransformiumSeed.class, new RenderTransformiumSeed());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisplayStation.class, new RenderDisplayStation());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAssemblyTable.class, new RenderAssemblyTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergonProcessor.class, new RenderEnergonProcessor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergonTank.class, new RenderEnergonTank());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransmitter.class, new RenderTransmitter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRelayTower.class, new RenderRelayTower());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColumn.class, new RenderColumn());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnergyPort.class, new RenderEnergyPort());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIsoCondenser.class, new RenderIsoCondenser());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityControlPanel.class, new RenderControlPanel());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGroundBridgeTeleporter.class, new RenderGroundBridgeTeleporter());

		RenderingRegistry.registerBlockHandler(RenderBlockGroundBridgeFrame.instance);
		RenderingRegistry.registerBlockHandler(RenderBlockEnergonTank.instance);
		RenderingRegistry.registerBlockHandler(RenderBlockAlloyCrucible.instance);
		RenderingRegistry.registerBlockHandler(RenderBlockDisplayPedestal.instance);
		RenderingRegistry.registerBlockHandler(RenderBlockEnergonOre.instance);
	}

	public void registerArmorRenderer(ItemTransformerArmor helmet, ItemTransformerArmor chest, ItemTransformerArmor legs, ItemTransformerArmor boots) {
		MinecraftForgeClient.registerItemRenderer(helmet, new RenderItemArmor(helmet.getTransformer(), 0));
		MinecraftForgeClient.registerItemRenderer(chest, new RenderItemArmor(chest.getTransformer(), 1));
		MinecraftForgeClient.registerItemRenderer(legs, new RenderItemArmor(legs.getTransformer(), 2));
		MinecraftForgeClient.registerItemRenderer(boots, new RenderItemArmor(boots.getTransformer(), 3));
	}

	@Override
	public World getWorld() {
		return mc.theWorld;
	}

	@Override
	public EntityPlayer getPlayer() {
		return mc.thePlayer;
	}

	@Override
	public float getRenderTick() {
		return ClientTickHandler.renderTick;
	}

	@Override
	public void runTasks() {
		if(mc.thePlayer != null) {
			super.runTasks();
		}
	}
}

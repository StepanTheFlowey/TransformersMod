package fiskfille.tf.helper;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.client.model.transformer.definition.TransformerModel;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.energon.power.*;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.item.armor.ItemTransformerArmor;
import fiskfille.tf.common.tick.ClientTickHandler;
import fiskfille.tf.common.tileentity.TileEntityMachine;
import fiskfille.tf.common.tileentity.TileEntityRelayTower;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

public final class TFRenderHelper {
	public static final int LIGHTING_LUMINOUS = 0xF0F0;
	private static final RenderItem itemRender = new RenderItem();
	private static final Map<EntityPlayer, Double> previousMotionY = new WeakHashMap<>();
	private static float lastBrightnessX;
	private static float lastBrightnessY;

	public static void setLighting(final int lighting) {
		lastBrightnessX = OpenGlHelper.lastBrightnessX;
		lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lighting % 65536 / 255F, lighting / 65536F / 255F);
	}

	public static void resetLighting() {
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
	}

	public static float[] hexToRGB(final int hex) {
		final float r = ((hex & 0xFF0000) >> 16) / 255F;
		final float g = ((hex & 0xFF00) >> 8) / 255F;
		final float b = (hex & 0xFF) / 255F;
		return new float[]{r, g, b};
	}

	public static void setupRenderLayers(final Entity entity, final ItemStack itemstack, final ModelRenderer model) {
		if(itemstack != null && itemstack.getItem() instanceof ItemTransformerArmor) {
			final TransformerModel tfModel = TFModelRegistry.getModel(((ItemTransformerArmor) itemstack.getItem()).getTransformer());

			if(TFTextureHelper.isBoundTexture(TFTextureHelper.RES_ITEM_GLINT)) {
				model.render(0.0625F);
			}
			else {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				if(TFArmorDyeHelper.isDyed(itemstack)) {
					final float[] primaryColor = TFRenderHelper.hexToRGB(TFArmorDyeHelper.getPrimaryColor(itemstack));
					final float[] secondaryColor = TFRenderHelper.hexToRGB(TFArmorDyeHelper.getSecondaryColor(itemstack));

					GL11.glColor3f(primaryColor[0], primaryColor[1], primaryColor[2]);
					Minecraft.getMinecraft().getTextureManager().bindTexture(tfModel.getTexture(entity, "_primary"));
					model.render(0.0625F);

					GL11.glColor3f(secondaryColor[0], secondaryColor[1], secondaryColor[2]);
					Minecraft.getMinecraft().getTextureManager().bindTexture(tfModel.getTexture(entity, "_secondary"));
					model.render(0.0625F);

					GL11.glColor3f(1F, 1F, 1F);
					Minecraft.getMinecraft().getTextureManager().bindTexture(tfModel.getTexture(entity, "_base"));
				}
				else {
					Minecraft.getMinecraft().getTextureManager().bindTexture(tfModel.getTexture(entity, ""));
				}

				model.render(0.0625F);

				if(tfModel.hasLightsLayer()) {
					setLighting(LIGHTING_LUMINOUS);
					Minecraft.getMinecraft().getTextureManager().bindTexture(tfModel.getTexture(entity, "_lights"));
					model.render(0.0625F);
					resetLighting();
				}

				GL11.glDisable(GL11.GL_BLEND);
			}
		}
	}

	public static void startGlScissor(int x, int y, final int width, final int height) {
		final ScaledResolution reso = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		final double scaleW = Minecraft.getMinecraft().displayWidth / reso.getScaledWidth_double();
		final double scaleH = Minecraft.getMinecraft().displayHeight / reso.getScaledHeight_double();

		if(width <= 0 || height <= 0) {
			return;
		}
		if(x < 0) {
			x = 0;
		}
		if(y < 0) {
			y = 0;
		}

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(
						MathHelper.floor_double(x * scaleW),
						MathHelper.floor_double(Minecraft.getMinecraft().displayHeight - (y + height) * scaleH),
						MathHelper.floor_double((x + width) * scaleW) - MathHelper.floor_double(x * scaleW),
						MathHelper.floor_double(Minecraft.getMinecraft().displayHeight - y * scaleH) - MathHelper.floor_double(Minecraft.getMinecraft().displayHeight - (y + height) * scaleH)
		);
	}

	public static void endGlScissor() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public static double getMotionY(final EntityPlayer player) {
		final double current = player == Minecraft.getMinecraft().thePlayer ? player.motionY : player.posY - player.prevPosY;
		final double previous = previousMotionY.getOrDefault(player, 0D);

		return TFHelper.median(current, previous, ClientTickHandler.renderTick);
	}

	public static void updateMotionY(final EntityPlayer player) {
		previousMotionY.put(player, player == Minecraft.getMinecraft().thePlayer ? player.motionY : player.posY - player.prevPosY);
	}

	public static void renderTag(final String s, final float x, final float y, final float z) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
		GL11.glNormal3f(0, 1, 0);
		final EntityClientPlayerMP thePlayer = Minecraft.getMinecraft().thePlayer;
		GL11.glRotatef(thePlayer.rotationYaw + 180, 0, 1, 0);
		GL11.glRotatef(-thePlayer.rotationPitch, 1, 0, 0);
		GL11.glScalef(0.02F, 0.02F, -0.02F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		final FontRenderer fontrenderer = RenderManager.instance.getFontRenderer();
		final double i = fontrenderer.getStringWidth(s) / 2D;
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA(0, 0, 0, 64);
		tessellator.addVertex(-i - 1, -1, 0);
		tessellator.addVertex(-i - 1, 8, 0);
		tessellator.addVertex(i + 1, 8, 0);
		tessellator.addVertex(i + 1, -1, 0);
		tessellator.draw();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(true);

		fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, -1);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor3f(1, 1, 1);
		GL11.glPopMatrix();
	}

	public static void faceVec(final Vec3 src, final Vec3 dst) {
		final double d0 = dst.xCoord - src.xCoord;
		final double d1 = dst.yCoord - src.yCoord;
		final double d2 = dst.zCoord - src.zCoord;
		final double d3 = Math.hypot(d0, d2);

		GL11.glRotated(Math.toDegrees(Math.atan2(d2, d0)) - 90D, 0, -1, 0);
		GL11.glRotated(Math.toDegrees(Math.atan2(d1, d3)), -1, 0, 0);
	}

	public static void renderEnergyTransmissions(final TileEntity transmitterTile, final double x, final double y, final double z) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		setLighting(LIGHTING_LUMINOUS);

		final IEnergyTransmitter transmitter = (IEnergyTransmitter) transmitterTile;
		final TransmissionHandler transmissionHandler = transmitter.getTransmissionHandler();

		boolean renderBeams;
		if(transmitterTile instanceof TileEntityRelayTower) {
			renderBeams = ((TileEntityRelayTower) transmitterTile).data.isPowered;
		}
		else {
			renderBeams = transmitter.getEnergy() > 0;
		}

		if(transmitterTile instanceof TileEntityMachine) {
			renderBeams &= ((TileEntityMachine) transmitterTile).canActivate();
		}

		if(renderBeams) {
			for(ReceiverEntry entry : transmissionHandler.getReceivers()) {
				final boolean invertCurrent = transmitterTile instanceof TileEntityRelayTower && ((TileEntityRelayTower) transmitterTile).data.invertCurrent.contains(entry.getCoords());
				final boolean canReach = entry.canReach();

				if(entry.getTile() == null) {
					continue;
				}

				TileEntity tile = transmitterTile;
				Vec3 srcOffset1 = transmitter.getEnergyOutputOffset();
				Vec3 dstOffset1 = entry.getReceiver().getEnergyInputOffset();

				if(tile instanceof ITransmitterRender) {
					srcOffset1 = ((ITransmitterRender) tile).getRenderOutputOffset();
				}

				if(entry.getTile() instanceof IReceiverRender) {
					dstOffset1 = ((IReceiverRender) entry.getTile()).getRenderInputOffset();
				}

				final Vec3 srcOffset;
				final Vec3 dstOffset;
				if(invertCurrent) {
					tile = entry.getTile();
					entry = new ReceiverEntry(transmitterTile);
					entry.setCanReach(canReach);

					srcOffset = dstOffset1.addVector(0, 0, 0);
					dstOffset = srcOffset1.addVector(0, 0, 0);
				}
				else {
					srcOffset = srcOffset1.addVector(0, 0, 0);
					dstOffset = dstOffset1.addVector(0, 0, 0);
				}

				final DimensionalCoords coords = entry.getCoords();
				Vec3 src = srcOffset.addVector(tile.xCoord + 0.5F, tile.yCoord + 0.5F, tile.zCoord + 0.5F);
				Vec3 dst = dstOffset.addVector(coords.posX + 0.5F, coords.posY + 0.5F, coords.posZ + 0.5F);

				if(!canReach) {
					final double d = 1D / dst.distanceTo(src);
					src = Vec3.createVectorHelper(src.xCoord + (dst.xCoord - src.xCoord) * d, src.yCoord + (dst.yCoord - src.yCoord) * d, src.zCoord + (dst.zCoord - src.zCoord) * d);

					final MovingObjectPosition mop = TFEnergyHelper.rayTraceBlocks(tile.getWorldObj(), src, dst);
					if(mop != null) {
						dst = mop.hitVec;
					}
				}

				final double x1 = 0.5D + srcOffset.xCoord;
				final double y1 = 0.5D + srcOffset.yCoord;
				final double z1 = 0.5D + srcOffset.zCoord;
				final double deltaX = dst.xCoord - tile.xCoord;
				final double deltaY = dst.yCoord - tile.yCoord;
				final double deltaZ = dst.zCoord - tile.zCoord;

				src = Vec3.createVectorHelper(x1, y1, z1);
				dst = Vec3.createVectorHelper(deltaX, deltaY, deltaZ);

				int primary = 0x57ABAF;
				int secondary = 0x7BF2F8;
				final int parentPrimary = primary;
				final int parentSecondary = secondary;

				if(!canReach) {
					primary = 0xAF5B57;
					secondary = 0xF8817B;
				}
//			else if (!(receiverTile instanceof IEnergyTransmitter) && receiver.getEnergy() >= receiver.getMaxEnergy()) {
//				primary = 0x62AF57;
//				secondary = 0x8AF87B;
//			}

				GL11.glPushMatrix();
				GL11.glTranslated(x + x1, y + y1, z + z1);

				if(invertCurrent) {
					GL11.glTranslated(
									tile.xCoord - coords.posX,
									tile.yCoord - coords.posY,
									tile.zCoord - coords.posZ
					);
				}

				renderEnergyBeam(src, dst, primary, secondary, parentPrimary, parentSecondary);
				GL11.glPopMatrix();
			}
		}

		resetLighting();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	public static void renderEnergyBeam(final Vec3 src, final Vec3 dst, final int primaryColor, final int secondaryColor, final int primaryParentColor, final int secondaryParentColor) {
		final Tessellator tessellator = Tessellator.instance;
		final float partialTicks = ClientTickHandler.renderTick;
		final float[] primary = hexToRGB(primaryColor);
		final float[] secondary = hexToRGB(secondaryColor);
		final float[] parentPrimary = hexToRGB(primaryParentColor);
		final float[] parentSecondary = hexToRGB(secondaryParentColor);

		final double width = 1D / 16D;
		final double length = src.distanceTo(dst);
		final int segments = MathHelper.floor_double(length * 8);

		faceVec(src, dst);

		for(int i = 0; i < segments; ++i) {
			final double segmentLength = length / segments;
			final double start = i * segmentLength;
			final double end = (i + 1) * segmentLength;
			final float f = (float) Math.cos(i / (segments * 0.15625F) - (Minecraft.getMinecraft().thePlayer.ticksExisted + partialTicks) / 5);
			final float f1 = 1 - f;
			final float f2 = Math.min((float) i / segments * 3, 1);
			final float f3 = 1 - f2;

			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F((primary[0] * f + secondary[0] * f1) * f2 + (parentPrimary[0] * f + parentSecondary[0] * f1) * f3, (primary[1] * f + secondary[1] * f1) * f2 + (parentPrimary[1] * f + parentSecondary[1] * f1) * f3, (primary[2] * f + secondary[2] * f1) * f2 + (parentPrimary[2] * f + parentSecondary[2] * f1) * f3, 1);

			tessellator.addVertex(width, width, end);
			tessellator.addVertex(width, width, start);
			tessellator.addVertex(-width, width, start);
			tessellator.addVertex(-width, width, end);
			tessellator.addVertex(-width, -width, start);
			tessellator.addVertex(width, -width, start);
			tessellator.addVertex(width, -width, end);
			tessellator.addVertex(-width, -width, end);
			tessellator.addVertex(-width, width, start);
			tessellator.addVertex(-width, -width, start);
			tessellator.addVertex(-width, -width, end);
			tessellator.addVertex(-width, width, end);
			tessellator.addVertex(width, -width, end);
			tessellator.addVertex(width, -width, start);
			tessellator.addVertex(width, width, start);
			tessellator.addVertex(width, width, end);

			if(i == segments - 1) {
				tessellator.addVertex(width, -width, end);
				tessellator.addVertex(width, width, end);
				tessellator.addVertex(-width, width, end);
				tessellator.addVertex(-width, -width, end);
			}
			else if(i == 0) {
				tessellator.addVertex(-width, width, start);
				tessellator.addVertex(width, width, start);
				tessellator.addVertex(width, -width, start);
				tessellator.addVertex(-width, -width, start);
			}

			tessellator.draw();
		}
	}

	public static void renderEnergyStatic(Vec3 src, Vec3 dst, final double width, final float intensity, final int segments, final long seed) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		setLighting(LIGHTING_LUMINOUS);

		GL11.glTranslated(-src.xCoord, -src.yCoord, -src.zCoord);
		faceVec(dst, src);
		GL11.glRotatef(90, 1, 0, 0);

		final float[] primary = hexToRGB(0x57ABAF);
		final float[] secondary = hexToRGB(0x7BF2F8);
		final double length = src.distanceTo(dst);

		final Random rand = new Random(seed + Minecraft.getMinecraft().thePlayer.ticksExisted * 10L);
		final Random randPrev = new Random(seed + (Minecraft.getMinecraft().thePlayer.ticksExisted - 1) * 10L);

		src = Vec3.createVectorHelper(0, 0, 0);

		for(int i = 0; i < segments; ++i) {
			dst = Vec3.createVectorHelper(0, (i + 1) * length / segments, 0);

			if(i < segments - 1) {
				final float f = (float) i / segments;
				final float angle = (float) Math.toRadians(90 * intensity) * (1 - f);
				dst.rotateAroundX((TFHelper.median(rand.nextFloat(), randPrev.nextFloat(), ClientTickHandler.renderTick) - 0.5F) * 2 * angle);
				dst.rotateAroundY((TFHelper.median(rand.nextFloat(), randPrev.nextFloat(), ClientTickHandler.renderTick) - 0.5F) * 2 * angle);
				dst.rotateAroundZ((TFHelper.median(rand.nextFloat(), randPrev.nextFloat(), ClientTickHandler.renderTick) - 0.5F) * 2 * angle);
			}
			else {
				dst = Vec3.createVectorHelper(0, length, 0);
			}

			dst.xCoord = MathHelper.clamp_double(dst.xCoord, -0.0625F * 1.25F, 0.0625F * 1.25F);
			dst.zCoord = MathHelper.clamp_double(dst.zCoord, -0.0625F * 1.25F, 0.0625F * 1.25F);
			dst.yCoord = MathHelper.clamp_double(dst.yCoord, 0, length);

			final double segmentLength = src.distanceTo(dst);
			final float f1 = (float) Math.cos(i / (segments * 0.15625F));
			final float f2 = 1 - f1;

			final Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(primary[0] * f1 + secondary[0] * f2, primary[1] * f1 + secondary[1] * f2, primary[2] * f1 + secondary[2] * f2, 1);
			tessellator.addVertex(width, width, segmentLength);
			tessellator.addVertex(width, width, 0);
			tessellator.addVertex(-width, width, 0);
			tessellator.addVertex(-width, width, segmentLength);
			tessellator.addVertex(-width, -width, 0);
			tessellator.addVertex(width, -width, 0);
			tessellator.addVertex(width, -width, segmentLength);
			tessellator.addVertex(-width, -width, segmentLength);
			tessellator.addVertex(-width, width, 0);
			tessellator.addVertex(-width, -width, 0);
			tessellator.addVertex(-width, -width, segmentLength);
			tessellator.addVertex(-width, width, segmentLength);
			tessellator.addVertex(width, -width, segmentLength);
			tessellator.addVertex(width, -width, 0);
			tessellator.addVertex(width, width, 0);
			tessellator.addVertex(width, width, segmentLength);
			tessellator.addVertex(width, -width, segmentLength);
			tessellator.addVertex(width, width, segmentLength);
			tessellator.addVertex(-width, width, segmentLength);
			tessellator.addVertex(-width, -width, segmentLength);
			tessellator.addVertex(-width, width, 0);
			tessellator.addVertex(width, width, 0);
			tessellator.addVertex(width, -width, 0);
			tessellator.addVertex(-width, -width, 0);

			GL11.glPushMatrix();
			GL11.glTranslated(src.xCoord, src.yCoord, src.zCoord);
			faceVec(src, dst);
			tessellator.draw();
			GL11.glPopMatrix();
			src = dst;
		}

		resetLighting();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	public static int getBlockDestroyProgress(final World world, final int x, final int y, final int z) {
		final Map damagedBlocks = ObfuscationReflectionHelper.getPrivateValue(RenderGlobal.class, Minecraft.getMinecraft().renderGlobal, "damagedBlocks", "field_72738_E", "O");

		if(!damagedBlocks.isEmpty()) {
			for(final Object o : damagedBlocks.values()) {
				final DestroyBlockProgress progress = (DestroyBlockProgress) o;
				final int metadata = world.getBlockMetadata(progress.getPartialBlockX(), progress.getPartialBlockY(), progress.getPartialBlockZ());
				final int[] offsets = TFTileHelper.getTileBaseOffsets(world.getTileEntity(x, y, z), metadata);

				if(x == progress.getPartialBlockX() + offsets[0] && y == progress.getPartialBlockY() + offsets[1] && z == progress.getPartialBlockZ() + offsets[2]) {
					return progress.getPartialBlockDamage();
				}
			}
		}

		return -1;
	}

	public static void renderBlock(final Block block, final IIcon icon, final RenderBlocks renderer) {
		final Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, -1F, 0F);
		renderer.renderFaceYNeg(block, 0D, 0D, 0D, icon);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 1F, 0F);
		renderer.renderFaceYPos(block, 0D, 0D, 0D, icon);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, -1F);
		renderer.renderFaceZNeg(block, 0D, 0D, 0D, icon);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, 1F);
		renderer.renderFaceZPos(block, 0D, 0D, 0D, icon);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0F, 0F);
		renderer.renderFaceXNeg(block, 0D, 0D, 0D, icon);
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1F, 0F, 0F);
		renderer.renderFaceXPos(block, 0D, 0D, 0D, icon);
		tessellator.draw();
	}

	public static void renderBlock(final Block block, final int meta, final RenderBlocks renderer) {
		final Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, -1F, 0F);
		renderer.renderFaceYNeg(block, 0D, 0D, 0D, block.getIcon(0, meta));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 1F, 0F);
		renderer.renderFaceYPos(block, 0D, 0D, 0D, block.getIcon(1, meta));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, -1F);
		renderer.renderFaceZNeg(block, 0D, 0D, 0D, block.getIcon(2, meta));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0F, 0F, 1F);
		renderer.renderFaceZPos(block, 0D, 0D, 0D, block.getIcon(3, meta));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0F, 0F);
		renderer.renderFaceXNeg(block, 0D, 0D, 0D, block.getIcon(4, meta));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(1F, 0F, 0F);
		renderer.renderFaceXPos(block, 0D, 0D, 0D, block.getIcon(5, meta));
		tessellator.draw();
	}

	public static void renderBlockAllFaces(final RenderBlocks renderer, final Block block, final int x, final int y, final int z, final IIcon icon) {
		renderer.renderFaceYNeg(block, x, y, z, icon);
		renderer.renderFaceYPos(block, x, y, z, icon);
		renderer.renderFaceZNeg(block, x, y, z, icon);
		renderer.renderFaceZPos(block, x, y, z, icon);
		renderer.renderFaceXNeg(block, x, y, z, icon);
		renderer.renderFaceXPos(block, x, y, z, icon);
	}

	public static boolean shouldOverrideView(final EntityPlayer player) {
		return TFHelper.getHeight(player) != 1.8F || TFHelper.getScale(player) != 1;
	}

	public static boolean shouldOverrideThirdPersonDistance(final EntityPlayer player) {
		return player.ridingEntity == null && (TFHelper.getTransformer(player) != null || TFData.PREV_TRANSFORMER.get(player) != null);
	}

	public static void renderItemIntoGUI(final int x, final int y, final ItemStack itemstack) {
		if(itemstack == null) {
			return;
		}

		FontRenderer font = itemstack.getItem().getFontRenderer(itemstack);
		if(font == null) {
			font = Minecraft.getMinecraft().fontRenderer;
		}

		itemRender.renderItemAndEffectIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), itemstack, x, y);
		if(itemstack.stackSize > 1) {
			itemRender.renderItemOverlayIntoGUI(font, Minecraft.getMinecraft().getTextureManager(), itemstack, x, y, itemstack.stackSize + "");
		}
	}

	public static void setupRenderItemIntoGUI() {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		RenderHelper.enableGUIStandardItemLighting();
	}

	public static void finishRenderItemIntoGUI() {
		GL11.glColor3f(1, 1, 1);
		GL11.glPopAttrib();
	}
}

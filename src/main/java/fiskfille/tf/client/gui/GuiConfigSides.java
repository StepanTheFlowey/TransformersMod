package fiskfille.tf.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.block.BlockMachineBase;
import fiskfille.tf.common.container.ContainerEmpty;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.network.MessageTileTrigger;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.tick.ClientTickHandler;
import fiskfille.tf.common.tileentity.TileEntityMachine;
import fiskfille.tf.common.tileentity.TileEntityMachine.EnumIO;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFRenderHelper;
import fiskfille.tf.helper.TFTextureHelper;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

import javax.vecmath.Vector3d;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiConfigSides extends GuiContainerTF {
	private static final ResourceLocation guiTextures = new ResourceLocation(TransformersMod.MODID, "textures/gui/container/configure.png");
	private final TileEntityMachine machine;
	private final GuiScreen parent;
	private final RenderBlocks renderBlocks;
	private final World world;
	private final List<ChunkCoordinates> neighbors = Lists.newArrayList();
	private final List<ChunkCoordinates> configurables = Lists.newArrayList();
	private final Vector3d camera;

	public GuiConfigSides(final InventoryPlayer inventoryPlayer, final GuiScreen gui, final TileEntityMachine tile) {
		super(new ContainerEmpty(inventoryPlayer, 16));
		machine = tile;
		parent = gui;
		ySize = 182;

		world = tile.getWorldObj();
		renderBlocks = new RenderBlocks(world);
		camera = new Vector3d(-machine.xCoord - 0.5F, -machine.yCoord - 0.5F, -machine.zCoord - 0.5F);
		configurables.add(new DimensionalCoords(tile));
	}

	@Override
	public void initGui() {
		super.initGui();

		final int x = (width - xSize) / 2, y = (height - ySize) / 2;
		buttonList.add(new GuiButtonDistribution(0, x + 111, y + 66, machine));
		for(final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			final int index = dir.ordinal();
			buttonList.add(new GuiButtonIO(index + 1, x + 108 + index % 2 * 15, y + 18 + index / 2 * 15, machine, dir));
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		for(final GuiButton button : (List<GuiButton>) buttonList) {
			if(button instanceof GuiButtonIO) {
				final GuiButtonIO iobutton = (GuiButtonIO) button;
				button.enabled = iobutton.machine.canTransfer(iobutton.side);
			}
		}

		neighbors.clear();

		for(final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			final int x = machine.xCoord + dir.offsetX;
			final int y = machine.yCoord + dir.offsetY + (dir.offsetY > 0 ? machine.getBlockType().getBlockHeight() - 1 : 0);
			final int z = machine.zCoord + dir.offsetZ;

			if(!world.isAirBlock(x, y, z)) {
				neighbors.add(new ChunkCoordinates(x, y, z));
			}
		}
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		final int id = button.id;

		if(id == 0) {
			TFNetworkManager.networkWrapper.sendToServer(new MessageTileTrigger(new DimensionalCoords(machine), mc.thePlayer, -machine.io.length - 3));
		}
		else if(id > 0 && id <= ForgeDirection.VALID_DIRECTIONS.length) {
			TFNetworkManager.networkWrapper.sendToServer(new MessageTileTrigger(new DimensionalCoords(machine), mc.thePlayer, -id));
		}
	}

	@Override
	protected void keyTyped(final char c, final int key) {
		if(key == 1 || key == mc.gameSettings.keyBindInventory.getKeyCode()) {
			mc.displayGuiScreen(parent);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		final int x = (width - xSize) / 2, y = (height - ySize) / 2;

		final String s = I18n.format("gui.tf.io");
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 94, 4210752);

		final float height = machine.getBlockType().getBlockHeight();
		final float scale = 30 - (height - 1) * 5;
		GL11.glPushMatrix();
		GL11.glTranslatef(73, 51, 100);
		GL11.glRotatef(-TFHelper.median(mc.thePlayer.rotationPitch, mc.thePlayer.prevRotationPitch, ClientTickHandler.renderTick), 1, 0, 0);
		GL11.glRotatef(TFHelper.median(mc.thePlayer.rotationYaw, mc.thePlayer.prevRotationYaw, ClientTickHandler.renderTick), 0, 1, 0);
		GL11.glScalef(-scale, -scale, -scale);
		GL11.glTranslatef(0, 0.5F - height / 2, 0);
		TFRenderHelper.startGlScissor(x + 41, y + 19, 64, 64);
		renderScene();
		TFRenderHelper.endGlScissor();
		GL11.glPopMatrix();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GL11.glColor3f(1, 1, 1);
		mc.getTextureManager().bindTexture(guiTextures);
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}

	private void renderScene() {
		mc.entityRenderer.disableLightmap(0);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		for(int pass = 0; pass < 2; ++pass) {
			setGlStateForPass(pass, false);
			doTileEntityRenderPass(configurables, pass);

			setGlStateForPass(pass, true);
			doTileEntityRenderPass(neighbors, pass);
		}

		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		GL11.glDisable(GL11.GL_LIGHTING);

		for(int pass = 0; pass < 1; ++pass) {
			setGlStateForPass(pass, false);
			doWorldRenderPass(configurables, pass);

			setGlStateForPass(pass, true);
			doWorldRenderPass(neighbors, pass);
		}

		ForgeHooksClient.setRenderPass(-1);
		setGlStateForPass(0, false);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		final int x = machine.xCoord, y = machine.yCoord, z = machine.zCoord;
		final Tessellator tessellator = Tessellator.instance;
		tessellator.setTranslation(-x - 0.5F, -y - 0.5F, -z - 0.5F);
		renderBlocks.setRenderAllFaces(true);

		final BlockMachineBase block = machine.getBlockType();
		for(final GuiButton button : (List<GuiButton>) buttonList) {
			if(button instanceof GuiButtonIO) {
				final GuiButtonIO iobutton = (GuiButtonIO) button;

				if(iobutton.func_146115_a()) {
					if(iobutton.enabled) {
						GL11.glColor4f(0.2F, 1, 0.2F, 0.5F);
					}
					else {
						GL11.glColor4f(1, 0.2F, 0.2F, 0.5F);
					}

					block.setBlockBoundsBasedOnState(world, x, y, z);
					renderBlocks.setRenderBoundsFromBlock(block);

					final ForgeDirection dir = iobutton.side;
					IIcon icon = renderBlocks.getBlockIcon(Blocks.wool);
					for(int i = 0; i < 2; ++i) {
						if(i == 1) {
							final EnumIO io = machine.getInOutMode(dir);

							if(io.ordinal() == 0) {
								break;
							}

							GL11.glColor3f(1, 1, 1);
							renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);
							icon = TFTextureHelper.ioIcons[io.ordinal()];
						}

						tessellator.startDrawingQuads();
						renderFace(dir, block, x, y + (i == 1 && dir.offsetY > 0 ? block.getBlockHeight() - 1 : 0), z, icon);
						tessellator.draw();
					}
				}
			}
		}

		//        GL11.glColor3f(1, 1, 1);
		//
		//        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		//        {
		//            EnumIO io = machine.getInOutMode(dir);
		//
		//            if (io.ordinal() > 0)
		//            {
		//                renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);
		//
		//                tessellator.startDrawingQuads();
		//                renderFace(dir, block, x, y, z, TFTextureHelper.ioIcons[io.ordinal()]);
		//                tessellator.draw();
		//            }
		//        }

		tessellator.setTranslation(0, 0, 0);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderFace(final ForgeDirection dir, final Block block, final int x, final int y, final int z, final IIcon icon) {
		switch(dir) {
			case DOWN:
				renderBlocks.renderFaceYNeg(block, x, y, z, icon);
				break;
			case UP:
				renderBlocks.renderFaceYPos(block, x, y, z, icon);
				break;
			case NORTH:
				renderBlocks.renderFaceZNeg(block, x, y, z, icon);
				break;
			case SOUTH:
				renderBlocks.renderFaceZPos(block, x, y, z, icon);
				break;
			case EAST:
				renderBlocks.renderFaceXPos(block, x, y, z, icon);
				break;
			case WEST:
				renderBlocks.renderFaceXNeg(block, x, y, z, icon);
				break;
			default:
				break;
		}
	}

	private void doTileEntityRenderPass(final List<ChunkCoordinates> blocks, final int pass) {
		ForgeHooksClient.setRenderPass(pass);

		for(final ChunkCoordinates coords : blocks) {
			final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(coords.posX, coords.posY, coords.posZ));

			if(tile != null) {
				GL11.glColor3f(1, 1, 1);
				TileEntityRendererDispatcher.instance.renderTileEntityAt(tile, tile.xCoord + camera.x, tile.yCoord + camera.y, tile.zCoord + camera.z, 0);
			}
		}
	}

	private void doWorldRenderPass(final List<ChunkCoordinates> blocks, final int pass) {
		ForgeHooksClient.setRenderPass(pass);

		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setTranslation(camera.x, camera.y, camera.z);
		tessellator.setBrightness(15 << 20 | 15 << 4);

		for(final ChunkCoordinates coords : blocks) {
			final Block block = world.getBlock(coords.posX, coords.posY, coords.posZ);

			if(block != null) {
				if(block.canRenderInPass(pass)) {
					renderBlocks.renderAllFaces = true;
					renderBlocks.setRenderAllFaces(true);
					renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);
					renderBlocks.renderBlockByRenderType(block, coords.posX, coords.posY, coords.posZ);
				}
			}
		}

		tessellator.draw();
		tessellator.setTranslation(0, 0, 0);
	}

	private void setGlStateForPass(final int pass, final boolean isNeighbour) {
		GL11.glColor3f(1, 1, 1);

		if(isNeighbour) {
			final float alpha = 0.6F;

			if(pass == 0) {
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glBlendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_CONSTANT_COLOR);
				GL14.glBlendColor(1, 1, 1, alpha);
				GL11.glDepthMask(true);
			}
			else {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_CONSTANT_COLOR);
				GL14.glBlendColor(1, 1, 1, 0.4F);
				GL14.glBlendColor(1, 1, 1, alpha);
				GL11.glDepthMask(false);
			}

			return;
		}

		if(pass == 0) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
		}
		else {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(false);
		}
	}
}

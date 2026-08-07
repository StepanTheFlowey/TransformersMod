package fiskfille.tf.client.gui;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.energon.power.IEnergyReceiver;
import fiskfille.tf.common.energon.power.IEnergyTransmitter;
import fiskfille.tf.common.energon.power.ReceiverEntry;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.network.MessageConnectReceiver;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.helper.TFEnergyHelper;
import fiskfille.tf.helper.TFRenderHelper;
import fiskfille.tf.helper.TFVectorHelper;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSelectReceivers extends GuiScreen {
	private final int SPACING = 1;
	private final int SIZE = 3;

	public final TileEntity owner;
	public final IEnergyTransmitter transmitter;
	public final List<Integer> layers = Lists.newArrayList();
	public DimensionalCoords[] coordArray;
	public GuiVerticalHeightSlider heightSlider;

	public GuiSelectReceivers(final TileEntity tile) {
		owner = tile;
		transmitter = (IEnergyTransmitter) owner;
	}

	@Override
	public void initGui() {
		super.initGui();

		final int boardWidth = 1 + transmitter.getRange() * 2;
		final int baseX = MathHelper.floor_float((width - (SPACING + SIZE) * boardWidth) / 2F);
		final int baseY = MathHelper.floor_float((height - (SPACING + SIZE) * boardWidth) / 2F);

		buttonList.add(
						new GuiButton(
										0,
										width / 2 - 100,
										height - height / 7,
										I18n.format("gui.done")
						)
		);
		buttonList.add(
						heightSlider = new GuiVerticalHeightSlider(
										1,
										this,
										baseX + boardWidth * (SPACING + SIZE),
										baseY - 1,
										boardWidth * (SPACING + SIZE) + 1,
										this::updateBlocks
						)
		);

		coordArray = new DimensionalCoords[boardWidth * boardWidth];
		layers.clear();
		layers.add(owner.yCoord);

		final List<TileEntity> tiles = mc.theWorld.loadedTileEntityList;
		for(final TileEntity loadedTile : tiles) {
			if(loadedTile instanceof IEnergyReceiver && ((IEnergyReceiver) loadedTile).canReceiveEnergy(owner) && TFEnergyHelper.isInRange(owner, loadedTile)) {
				if(!layers.contains(loadedTile.yCoord)) {
					layers.add(loadedTile.yCoord);
				}
			}
		}

		Collections.sort(layers);

		heightSlider.enabled = layers.size() > 1;

		updateBlocks();
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		updateBlocks();
	}

	protected void updateBlocks() {
		final int boardWidth = 1 + transmitter.getRange() * 2;
		coordArray = new DimensionalCoords[boardWidth * boardWidth];

		final int dimension = owner.getWorldObj().provider.dimensionId;
		final int center = MathHelper.floor_float(boardWidth / 2F);
		final int xBase = owner.xCoord - center;
		final int zBase = owner.zCoord - center;
		for(int i = 0; i < boardWidth; ++i) {
			for(int j = 0; j < boardWidth; ++j) {
				final DimensionalCoords coords = new DimensionalCoords(xBase + i, getLayer(), zBase + j, dimension);
				if(TFEnergyHelper.isInRange(owner, coords)) {
					coordArray[i + j * boardWidth] = coords;
				}
			}
		}

		final int direction = MathHelper.floor_float(mc.thePlayer.rotationYaw * 4F / 360F + 2.5F) & 3;
		if(direction > 0) {
			final DimensionalCoords[] coordArray1 = coordArray.clone();

			for(int i = 0; i < boardWidth; ++i) {
				for(int j = 0; j < boardWidth; ++j) {
					DimensionalCoords coords = coordArray[i + j * boardWidth];

					switch(direction) {
						case 1:
							coords = coordArray[boardWidth - 1 - j + i * boardWidth];
							break;

						case 2:
							coords = coordArray[boardWidth - 1 - i + (boardWidth - 1 - j) * boardWidth];
							break;

						case 3:
							coords = coordArray[j + (boardWidth - 1 - i) * boardWidth];
							break;
					}

					coordArray1[i + j * boardWidth] = coords;
				}
			}

			coordArray = coordArray1;
		}
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		if(button.id == 0) {
			mc.thePlayer.closeScreen();
		}
	}

	@Override
	protected void keyTyped(final char c, final int key) {
		if(key == 1 || key == mc.gameSettings.keyBindInventory.getKeyCode()) {
			mc.thePlayer.closeScreen();
		}
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int button) {
		super.mouseClicked(mouseX, mouseY, button);

		if(button == 0) {
			final int boardWidth = 1 + getRange() * 2;
			final int baseX = MathHelper.floor_float((width - (SPACING + SIZE) * boardWidth) / 2F);
			final int baseY = MathHelper.floor_float((height - (SPACING + SIZE) * boardWidth) / 2F);

			if(coordArray != null) {
				for(int i = 0; i < boardWidth; ++i) {
					for(int j = 0; j < boardWidth; ++j) {
						final int x = baseX + (SPACING + SIZE) * i;
						final int y = baseY + (SPACING + SIZE) * j;

						if(new Rectangle(x, y, SIZE, SIZE).contains(mouseX, mouseY)) {
							final DimensionalCoords coords = coordArray[i + j * boardWidth];

							if(coords != null && !coords.equals(new DimensionalCoords(owner))) {
								final TileEntity tile = mc.theWorld.getTileEntity(coords.posX, coords.posY, coords.posZ);

								if(tile instanceof IEnergyReceiver && ((IEnergyReceiver) tile).canReceiveEnergy(owner) && !(tile instanceof IEnergyTransmitter && TFEnergyHelper.getDescendants((IEnergyTransmitter) tile).contains(new DimensionalCoords(owner)))) {
									TFNetworkManager.networkWrapper.sendToServer(new MessageConnectReceiver(new DimensionalCoords(owner), coords));
									mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1));
								}
							}
						}
					}
				}
			}
		}
	}

	public int getLayer() {
		final float f = 1 - heightSlider.percentage;
		final int amount = layers.size() - 1;

		for(int i = 0; i < layers.size(); ++i) {
			if(f >= (i - 0.5F) / amount && f < (i + 0.5F) / amount) {
				return layers.get(i);
			}
		}

		return owner.yCoord;
	}

	public int getRange() {
		return MathHelper.floor_float(transmitter.getRange());
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, I18n.format("gui.transmitter.select_receivers"), width / 2, 15, 16777215);

		if(coordArray == null || layers.isEmpty()) {
			updateBlocks();
		}

		final int boardWidth = 1 + getRange() * 2;
		final int baseX = MathHelper.floor_double(width / 2F - (SPACING + SIZE) * boardWidth / 2F);
		final int baseY = MathHelper.floor_double(height / 2F - (SPACING + SIZE) * boardWidth / 2F);

		if(coordArray != null) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glColor3f(0.1F, 0.1F, 0.1F);
			drawTexturedModalRect(baseX - SPACING, baseY - SPACING, 0, 0, MathHelper.floor_float((SPACING + SIZE) * boardWidth) + SPACING, MathHelper.floor_float((SPACING + SIZE) * boardWidth) + SPACING);
			final Vec3 src = Vec3.createVectorHelper(SIZE * getRange() - 0.5F, SIZE * getRange() - 0.5F, 0);

			int maxWidth = 0;

			for(int i = 0; i < boardWidth; ++i) {
				for(int j = 0; j < boardWidth; ++j) {
					final DimensionalCoords coords = coordArray[i + j * boardWidth];

					if(coords != null) {
						final Vec3 src1 = Vec3.createVectorHelper(owner.xCoord + 0.5F, 0, owner.zCoord + 0.5F);
						final Vec3 dst = Vec3.createVectorHelper(coords.posX + 0.5F, 0, coords.posZ + 0.5F);
						maxWidth = Math.max(maxWidth, MathHelper.floor_double(src1.distanceTo(dst)));
					}
				}
			}

			maxWidth = 1 + maxWidth * 2;

			for(int i = 0; i < boardWidth; ++i) {
				for(int j = 0; j < boardWidth; ++j) {
					final Vec3 dst = Vec3.createVectorHelper(SIZE * i, SIZE * j, 0);
					final float opacity = MathHelper.clamp_float((1 - (float) src.distanceTo(dst) / maxWidth * 2 / SIZE) * 2.5F, 0, 1);
					final int x = baseX + (SPACING + SIZE) * i;
					final int y = baseY + (SPACING + SIZE) * j;

					GL11.glColor3f(0.075F, 0.075F, 0.075F);
					drawTexturedModalRect(x, y, 0, 0, SIZE, SIZE);

					final DimensionalCoords coords = coordArray[i + j * boardWidth];

					if(coords != null) {
						if(!mc.theWorld.isAirBlock(coords.posX, coords.posY, coords.posZ)) {
							final float color = 0x70 / 255F;

							GL11.glColor4f(color, color, color, opacity);
							drawTexturedModalRect(x, y, 0, 0, SIZE, SIZE);
						}
					}
				}
			}

			final HashSet<DimensionalCoords> receiverCoords = new HashSet<>();
			for(final ReceiverEntry entry : transmitter.getTransmissionHandler().getReceivers()) {
				receiverCoords.add(entry.getCoords());
			}

			final Tessellator tessellator = Tessellator.instance;
			final float prevWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);

			final ArrayList<DimensionalCoords> coordList = Lists.newArrayList(coordArray);
			final float f = 0.5F;
			final float angle = 35;
			final float length = 4;

			GL11.glLineWidth(SIZE);
			GL11.glColor3f(0F, 1F, 1F);
			for(int i = 0; i < boardWidth; ++i) {
				for(int j = 0; j < boardWidth; ++j) {
					final DimensionalCoords coords = coordArray[i + j * boardWidth];
					final int x = baseX + (SPACING + SIZE) * i;
					final int y = baseY + (SPACING + SIZE) * j;

					if(coords != null) {
						final TileEntity tile = mc.theWorld.getTileEntity(coords.posX, coords.posY, coords.posZ);

						if(tile instanceof IEnergyTransmitter && tile != owner) {
							final IEnergyTransmitter transmitter1 = (IEnergyTransmitter) tile;
							int color = 0x00FFFF;

							if(tile instanceof IEnergyReceiver) {
								color = ((IEnergyReceiver) tile).getMapColor();
							}

							for(final ReceiverEntry entry : transmitter1.getTransmissionHandler().getReceivers()) {
								final int index = coordList.indexOf(entry.getCoords());

								if(index >= 0 && entry.getCoords().posY == getLayer()) {
									int k = index;
									int l = 0;
									for(; k >= boardWidth; ++l) {
										k -= boardWidth;
									}

									final Vec3 vec3 = Vec3.createVectorHelper(x + (float) SIZE / 2, y + (float) SIZE / 2, 0);
									final Vec3 vec31 = Vec3.createVectorHelper(baseX + (SPACING + SIZE) * k + (float) SIZE / 2, baseY + (SPACING + SIZE) * l + (float) SIZE / 2, 0);
									Vec3 vec32 = vec31.subtract(vec3);
									Vec3 vec33 = vec31.subtract(vec3);
									Vec3 vec34 = vec31.subtract(vec3);
									vec33.xCoord *= f;
									vec33.yCoord *= f;
									vec33 = TFVectorHelper.add(vec33, vec31);
									vec32.rotateAroundZ((float) Math.toRadians(angle));
									vec34.rotateAroundZ((float) Math.toRadians(-angle));
									vec32 = vec32.normalize();
									vec34 = vec34.normalize();
									vec32.xCoord *= length;
									vec32.yCoord *= length;
									vec34.xCoord *= length;
									vec34.yCoord *= length;
									vec32 = TFVectorHelper.add(vec32, vec33);
									vec34 = TFVectorHelper.add(vec34, vec33);

									tessellator.startDrawing(GL11.GL_LINE_STRIP);
									tessellator.setColorRGBA(0, 255, 255, 50);
									tessellator.addVertex(vec3.xCoord, vec3.yCoord, 0);
									tessellator.addVertex(vec31.xCoord, vec31.yCoord, 0);
									tessellator.draw();

									tessellator.startDrawing(GL11.GL_TRIANGLES);
									tessellator.setColorRGBA_I(color, 150);
									tessellator.addVertex(vec32.xCoord, vec32.yCoord, 0);
									tessellator.addVertex(vec33.xCoord, vec33.yCoord, 0);
									tessellator.addVertex(vec34.xCoord, vec34.yCoord, 0);
									tessellator.draw();
								}
							}
						}
					}
				}
			}

			for(final DimensionalCoords coords : receiverCoords) {
				final int index = coordList.indexOf(coords);

				if(index >= 0 && coords.posY == getLayer()) {
					int k = index;
					int l = 0;

					for(; k >= boardWidth; ++l) {
						k -= boardWidth;
					}

					final Vec3 vec3 = Vec3.createVectorHelper(baseX + (SPACING + SIZE) * boardWidth / 2F - 0.5F, baseY + (SPACING + SIZE) * boardWidth / 2F - 0.5F, 0);
					final Vec3 vec31 = Vec3.createVectorHelper(baseX + (SPACING + SIZE) * k + (float) SIZE / 2, baseY + (SPACING + SIZE) * l + (float) SIZE / 2, 0);
					Vec3 vec32 = vec31.subtract(vec3);
					Vec3 vec33 = vec31.subtract(vec3);
					Vec3 vec34 = vec31.subtract(vec3);
					vec33.xCoord *= f;
					vec33.yCoord *= f;
					vec33 = TFVectorHelper.add(vec33, vec31);
					vec32.rotateAroundZ((float) Math.toRadians(angle));
					vec34.rotateAroundZ((float) Math.toRadians(-angle));
					vec32 = vec32.normalize();
					vec34 = vec34.normalize();
					vec32.xCoord *= length;
					vec32.yCoord *= length;
					vec34.xCoord *= length;
					vec34.yCoord *= length;
					vec32 = TFVectorHelper.add(vec32, vec33);
					vec34 = TFVectorHelper.add(vec34, vec33);

					tessellator.startDrawing(GL11.GL_LINE_STRIP);
					tessellator.setColorRGBA(0, 255, 255, 200);
					tessellator.addVertex(vec3.xCoord, vec3.yCoord, 0);
					tessellator.addVertex(vec31.xCoord, vec31.yCoord, 0);
					tessellator.draw();

					tessellator.startDrawing(GL11.GL_TRIANGLES);
					tessellator.setColorRGBA(0, 255, 255, 150);
					tessellator.addVertex(vec32.xCoord, vec32.yCoord, 0);
					tessellator.addVertex(vec33.xCoord, vec33.yCoord, 0);
					tessellator.addVertex(vec34.xCoord, vec34.yCoord, 0);
					tessellator.draw();
				}
			}

			for(int i = 0; i < boardWidth; ++i) {
				for(int j = 0; j < boardWidth; ++j) {
					final Vec3 dst = Vec3.createVectorHelper(SIZE * i, SIZE * j, 0);
					final float opacity = MathHelper.clamp_float((1 - (float) src.distanceTo(dst) / boardWidth * 2 / SIZE) * 2.5F, 0, 1);
					final int x = baseX + (SPACING + SIZE) * i;
					final int y = baseY + (SPACING + SIZE) * j;

					final DimensionalCoords coords = coordArray[i + j * boardWidth];
					if(coords != null) {
						final TileEntity tile = mc.theWorld.getTileEntity(coords.posX, coords.posY, coords.posZ);

						if(receiverCoords.contains(coords)) {
							GL11.glColor4f(0, 1, 1, opacity);
							drawTexturedModalRect(x - SPACING, y - SPACING, 0, 0, SIZE + SPACING * 2, SIZE + SPACING * 2);
						}

						if(tile == owner) {
							GL11.glColor4f(0, 0.6F, 0, opacity);
						}
						else {
							if(tile instanceof IEnergyReceiver && ((IEnergyReceiver) tile).canReceiveEnergy(owner)) {
								final float[] afloat = TFRenderHelper.hexToRGB(((IEnergyReceiver) tile).getMapColor());

								if(tile instanceof IEnergyTransmitter && TFEnergyHelper.isPowering((IEnergyTransmitter) tile, owner)) {
									for(int k = 0; k < afloat.length; ++k) {
										afloat[k] *= 0.3F;
									}
								}

								GL11.glColor4f(afloat[0], afloat[1], afloat[2], opacity);
							}
							else if(tile instanceof IEnergyTransmitter && TFEnergyHelper.getDescendants((IEnergyTransmitter) tile).contains(new DimensionalCoords(owner))) {
								GL11.glColor4f(0.2F, 0, 0.2F, opacity);
							}
							else {
								continue;
							}
						}

						drawTexturedModalRect(x, y, 0, 0, SIZE, SIZE);
					}
				}
			}

			GL11.glLineWidth(prevWidth);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

		if(layers.size() > 1) {
			for(int i = 0; i < layers.size(); i += layers.size() - 1) {
				final float f = (float) i / (layers.size() - 1);
				drawString(mc.fontRenderer, layers.get(i) + "", heightSlider.xPosition + heightSlider.width + 3, heightSlider.yPosition + (int) ((1 - f) * (heightSlider.height - 8)), 0x4C4C4C);
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);

		final int direction = MathHelper.floor_float(mc.thePlayer.rotationYaw * 4F / 360F + 2.5F) & 3;
		final String[] dirs = {"north", "east", "south", "west"};
		final String[] astring = new String[dirs.length];

		for(int i = 0; i < dirs.length; ++i) {
			astring[i] = I18n.format("direction." + dirs[(i + direction) % dirs.length] + ".short");
		}

		drawCenteredString(fontRendererObj, astring[0], baseX + (SPACING + SIZE) * boardWidth / 2, baseY - fontRendererObj.FONT_HEIGHT / 2, -1);
		drawCenteredString(fontRendererObj, astring[1], baseX + (SPACING + SIZE) * boardWidth, baseY + (SPACING + SIZE) * boardWidth / 2 - fontRendererObj.FONT_HEIGHT / 2, -1);
		drawCenteredString(fontRendererObj, astring[2], baseX + (SPACING + SIZE) * boardWidth / 2, baseY + (SPACING + SIZE) * boardWidth - fontRendererObj.FONT_HEIGHT / 2, -1);
		drawCenteredString(fontRendererObj, astring[3], baseX, baseY + (SPACING + SIZE) * boardWidth / 2 - fontRendererObj.FONT_HEIGHT / 2, -1);
	}
}

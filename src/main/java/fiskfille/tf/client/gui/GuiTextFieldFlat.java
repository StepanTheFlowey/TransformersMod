package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiTextFieldFlat extends GuiTextField {
	public final int xPosition;
	public final int yPosition;
	public final int width;
	public final int height;

	private final FontRenderer fontRendererObj;
	private String text = "";
	private int maxStringLength = 32;
	private int cursorCounter;
	private int lineScrollOffset;
	private int cursorPosition;
	private int selectionEnd;
	private int enabledColor = 0x373737;
	private int disabledColor = 0x7F7F7F;
	private boolean visible = true;
	private boolean enableBackgroundDrawing = true;
	private boolean canLoseFocus = true;
	private boolean isFocused;
	private boolean isEnabled = true;

	public GuiTextFieldFlat(final FontRenderer fontRenderer, final int x, final int y, final int w) {
		super(fontRenderer, x, y, w, 13);
		fontRendererObj = fontRenderer;
		xPosition = x;
		yPosition = y;
		width = w;
		height = 13;
	}

	@Override
	public void updateCursorCounter() {
		++cursorCounter;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(final String s) {
		if(s.length() > maxStringLength) {
			text = s.substring(0, maxStringLength);
		}
		else {
			text = s;
		}

		setCursorPositionEnd();
	}

	@Override
	public String getSelectedText() {
		return text.substring(Math.min(cursorPosition, selectionEnd), Math.max(cursorPosition, selectionEnd));
	}

	@Override
	public void writeText(final String s) {
		String s1 = "";
		final String s2 = ChatAllowedCharacters.filerAllowedCharacters(s);
		final int i = Math.min(cursorPosition, selectionEnd);
		final int j = Math.max(cursorPosition, selectionEnd);
		final int k = maxStringLength - text.length() - (i - selectionEnd);
		if(!text.isEmpty()) {
			s1 = s1 + text.substring(0, i);
		}

		final int l;

		if(k < s2.length()) {
			s1 = s1 + s2.substring(0, k);
			l = k;
		}
		else {
			s1 = s1 + s2;
			l = s2.length();
		}

		if(!text.isEmpty() && j < text.length()) {
			s1 = s1 + text.substring(j);
		}

		text = s1;
		moveCursorBy(i - selectionEnd + l);
	}

	@Override
	public void deleteWords(final int num) {
		if(text.isEmpty()) {
			return;
		}

		if(selectionEnd != cursorPosition) {
			writeText("");
		}
		else {
			deleteFromCursor(getNthWordFromCursor(num) - cursorPosition);
		}
	}

	@Override
	public void deleteFromCursor(final int num) {
		if(text.isEmpty()) {
			return;
		}

		if(selectionEnd != cursorPosition) {
			writeText("");
		}
		else {
			final boolean flag = num < 0;
			final int j = flag ? cursorPosition + num : cursorPosition;
			final int k = flag ? cursorPosition : cursorPosition + num;
			String s = "";

			if(j >= 0) {
				s = text.substring(0, j);
			}

			if(k < text.length()) {
				s = s + text.substring(k);
			}

			text = s;

			if(flag) {
				moveCursorBy(num);
			}
		}
	}

	@Override
	public int getNthWordFromCursor(final int num) {
		return getNthWordFromPos(num, getCursorPosition());
	}

	@Override
	public int getNthWordFromPos(final int num, final int pos) {
		return func_146197_a(num, getCursorPosition(), true);
	}

	@Override
	public int func_146197_a(final int i, final int j, final boolean flag) {
		int k = j;
		final boolean flag1 = i < 0;
		final int l = Math.abs(i);

		for(int i1 = 0; i1 < l; ++i1) {
			if(flag1) {
				while(flag && k > 0 && text.charAt(k - 1) == 32) {
					--k;
				}

				while(k > 0 && text.charAt(k - 1) != 32) {
					--k;
				}
			}
			else {
				final int j1 = text.length();
				k = text.indexOf(32, k);

				if(k == -1) {
					k = j1;
				}
				else {
					while(flag && k < j1 && text.charAt(k) == 32) {
						++k;
					}
				}
			}
		}

		return k;
	}

	@Override
	public void moveCursorBy(final int amount) {
		setCursorPosition(selectionEnd + amount);
	}

	@Override
	public void setCursorPositionZero() {
		setCursorPosition(0);
	}

	@Override
	public void setCursorPositionEnd() {
		setCursorPosition(text.length());
	}

	@Override
	public boolean textboxKeyTyped(final char c, final int key) {
		if(!isFocused) {
			return false;
		}
		else {
			switch(c) {
				case 1:
					setCursorPositionEnd();
					setSelectionPos(0);
					return true;

				case 3:
					GuiScreen.setClipboardString(getSelectedText());
					return true;

				case 22:
					if(isEnabled) {
						writeText(GuiScreen.getClipboardString());
					}

					return true;

				case 24:
					GuiScreen.setClipboardString(getSelectedText());

					if(isEnabled) {
						writeText("");
					}

					return true;

				default:
					switch(key) {
						case 14:
							if(GuiScreen.isCtrlKeyDown()) {
								if(isEnabled) {
									deleteWords(-1);
								}
							}
							else if(isEnabled) {
								deleteFromCursor(-1);
							}
							return true;

						case 199:
							if(GuiScreen.isShiftKeyDown()) {
								setSelectionPos(0);
							}
							else {
								setCursorPositionZero();
							}
							return true;

						case 203:
							if(GuiScreen.isShiftKeyDown()) {
								if(GuiScreen.isCtrlKeyDown()) {
									setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
								}
								else {
									setSelectionPos(getSelectionEnd() - 1);
								}
							}
							else if(GuiScreen.isCtrlKeyDown()) {
								setCursorPosition(getNthWordFromCursor(-1));
							}
							else {
								moveCursorBy(-1);
							}
							return true;

						case 205:
							if(GuiScreen.isShiftKeyDown()) {
								if(GuiScreen.isCtrlKeyDown()) {
									setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
								}
								else {
									setSelectionPos(getSelectionEnd() + 1);
								}
							}
							else if(GuiScreen.isCtrlKeyDown()) {
								setCursorPosition(getNthWordFromCursor(1));
							}
							else {
								moveCursorBy(1);
							}
							return true;

						case 207:
							if(GuiScreen.isShiftKeyDown()) {
								setSelectionPos(text.length());
							}
							else {
								setCursorPositionEnd();
							}

							return true;

						case 211:
							if(GuiScreen.isCtrlKeyDown()) {
								if(isEnabled) {
									deleteWords(1);
								}
							}
							else if(isEnabled) {
								deleteFromCursor(1);
							}
							return true;

						default:
							if(ChatAllowedCharacters.isAllowedCharacter(c)) {
								if(isEnabled) {
									writeText(Character.toString(c));
								}

								return true;
							}

							return false;
					}
			}
		}
	}

	/**
	 * Args: x, y, buttonClicked
	 */
	@Override
	public void mouseClicked(final int mouseX, final int mouseY, final int button) {
		final boolean flag = mouseX >= xPosition && mouseX < xPosition + width && mouseY >= yPosition && mouseY < yPosition + height;

		if(canLoseFocus) {
			setFocused(flag);
		}

		if(isFocused && button == 0) {
			int l = mouseX - xPosition;

			if(enableBackgroundDrawing) {
				l -= 4;
			}

			final String s = fontRendererObj.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
			setCursorPosition(fontRendererObj.trimStringToWidth(s, l).length() + lineScrollOffset);
		}
	}

	@Override
	public void drawTextBox() {
		if(getVisible()) {
			if(getEnableBackgroundDrawing()) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(GuiButtonFlat.tfButtonTextures);

				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor3f(1, 1, 1);

				if(width % 2 == 0) {
					drawTexturedModalRect(xPosition, yPosition, 60, 39 + (isEnabled ? 13 : 0), width / 2, height);
					drawTexturedModalRect(xPosition + width / 2, yPosition, 60 + 150 - width / 2, 39 + (isEnabled ? 13 : 0), width / 2, height);
				}
				else {
					drawTexturedModalRect(xPosition, yPosition, 60, 39 + (isEnabled ? 13 : 0), width / 2 + 1, height);
					drawTexturedModalRect(xPosition + width / 2 + 1, yPosition, 60 + 150 - width / 2, 39 + (isEnabled ? 13 : 0), width / 2, height);
				}
			}

			final int i = isEnabled ? enabledColor : disabledColor;
			final int j = cursorPosition - lineScrollOffset;
			int k = selectionEnd - lineScrollOffset;
			final String s = fontRendererObj.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
			final boolean flag = j >= 0 && j <= s.length();
			final boolean flag1 = isFocused && cursorCounter / 6 % 2 == 0 && flag;
			final int l = enableBackgroundDrawing ? xPosition + 4 : xPosition;
			final int i1 = enableBackgroundDrawing ? yPosition + (height - 8) / 2 + 1 : yPosition;
			int j1 = l;

			if(k > s.length()) {
				k = s.length();
			}

			if(!s.isEmpty()) {
				final String s1 = flag ? s.substring(0, j) : s;
				j1 = fontRendererObj.drawString(s1, l, i1, i);
			}

			final boolean flag2 = cursorPosition < text.length() || text.length() >= getMaxStringLength();
			int k1 = j1;

			if(!flag) {
				k1 = j > 0 ? l + width : l;
			}
			else if(flag2) {
				k1 = j1 - 1;
				--j1;
			}

			if(!s.isEmpty() && flag && j < s.length()) {
				fontRendererObj.drawString(s.substring(j), j1, i1, i);
			}

			if(flag1) {
				if(flag2) {
					Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRendererObj.FONT_HEIGHT, -3092272);
				}
				else {
					fontRendererObj.drawString("_", k1, i1, i);
				}
			}

			if(k != j) {
				final int l1 = l + fontRendererObj.getStringWidth(s.substring(0, k));
				drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + fontRendererObj.FONT_HEIGHT);
			}
		}
	}

	private void drawCursorVertical(int x, int y, int x1, int y1) {
		int i1;

		if(x < x1) {
			i1 = x;
			x = x1;
			x1 = i1;
		}

		if(y < y1) {
			i1 = y;
			y = y1;
			y1 = i1;
		}

		if(x1 > xPosition + width) {
			x1 = xPosition + width;
		}

		if(x > xPosition + width) {
			x = xPosition + width;
		}

		GL11.glColor3f(0, 0, 1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glLogicOp(GL11.GL_OR_REVERSE);

		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertex(x, y1, 0);
		tessellator.addVertex(x1, y1, 0);
		tessellator.addVertex(x1, y, 0);
		tessellator.addVertex(x, y, 0);
		tessellator.draw();

		GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public int getMaxStringLength() {
		return maxStringLength;
	}

	@Override
	public void setMaxStringLength(final int length) {
		maxStringLength = length;

		if(text.length() > length) {
			text = text.substring(0, length);
		}
	}

	@Override
	public int getCursorPosition() {
		return cursorPosition;
	}

	@Override
	public void setCursorPosition(final int pos) {
		cursorPosition = pos;
		final int j = text.length();

		if(cursorPosition < 0) {
			cursorPosition = 0;
		}

		if(cursorPosition > j) {
			cursorPosition = j;
		}

		setSelectionPos(cursorPosition);
	}

	@Override
	public boolean getEnableBackgroundDrawing() {
		return enableBackgroundDrawing;
	}

	@Override
	public void setEnableBackgroundDrawing(final boolean enabled) {
		enableBackgroundDrawing = enabled;
	}

	@Override
	public void setTextColor(final int color) {
		enabledColor = color;
	}

	@Override
	public void setDisabledTextColour(final int color) {
		disabledColor = color;
	}

	@Override
	public boolean isFocused() {
		return isFocused;
	}

	@Override
	public void setFocused(final boolean focused) {
		if(focused && !isFocused) {
			cursorCounter = 0;
		}

		isFocused = focused;
	}

	@Override
	public void setEnabled(final boolean enabled) {
		isEnabled = enabled;
	}

	@Override
	public int getSelectionEnd() {
		return selectionEnd;
	}

	@Override
	public int getWidth() {
		return getEnableBackgroundDrawing() ? width - 8 : width;
	}

	@Override
	public void setSelectionPos(int pos) {
		final int j = text.length();

		if(pos > j) {
			pos = j;
		}

		if(pos < 0) {
			pos = 0;
		}

		selectionEnd = pos;

		if(fontRendererObj != null) {
			if(lineScrollOffset > j) {
				lineScrollOffset = j;
			}

			final int k = getWidth();
			final String s = fontRendererObj.trimStringToWidth(text.substring(lineScrollOffset), k);
			final int l = s.length() + lineScrollOffset;

			if(pos == lineScrollOffset) {
				lineScrollOffset -= fontRendererObj.trimStringToWidth(text, k, true).length();
			}

			if(pos > l) {
				lineScrollOffset += pos - l;
			}
			else if(pos <= lineScrollOffset) {
				lineScrollOffset -= lineScrollOffset - pos;
			}

			if(lineScrollOffset < 0) {
				lineScrollOffset = 0;
			}

			if(lineScrollOffset > j) {
				lineScrollOffset = j;
			}
		}
	}

	@Override
	public void setCanLoseFocus(final boolean loseFocuse) {
		canLoseFocus = loseFocuse;
	}

	@Override
	public boolean getVisible() {
		return visible;
	}

	@Override
	public void setVisible(final boolean isVisible) {
		visible = isVisible;
	}
}

package me.REDSTONER85.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import me.REDSTONER85.Shark;
import me.REDSTONER85.utils.AudioManager;
import me.REDSTONER85.utils.FileManager;
import me.REDSTONER85.utils.settings.SCharacter;

public class CharacterGui extends GuiScreen {

	private static final int COL_COUNT = 7;
	
	public boolean prevMouseDown = true;
	public boolean escDown = false;
	public boolean upDown = false;
	public boolean prevUpDown = true;
	public boolean downDown = false;
	public boolean prevDownDown = true;
	public int scroll = 0;
	
	@Override
	public void drawFrame(Graphics2D g, double scaler) {
		
		double cycle = 60000d / 35;
		double mult = ((System.currentTimeMillis() - AudioManager.lastRepeat) - 50) % cycle;
		mult -= cycle;
		mult *= -1;
		mult -= cycle * 0.75;
		if (mult < 0) mult = 0;
		mult /= cycle * 0.25;
		
		g.setColor(new Color(60 + (int)(mult * 100), 50, 60));
		g.fillRect(0, 0, windowWidth(), windowHeight());
		
		int mouseX = Shark.frame.mouseX();
		int mouseY = Shark.frame.mouseY();
		boolean prevMouseDown = this.prevMouseDown;
		this.prevMouseDown = Shark.frame.isMouseDown;
		
		g.setColor(new Color(230, 230, 230));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(28 * scaler)));
		g.drawString("character select", width() / 2 - (int)(16 * 8 * scaler) + originX(), (int)(height() * 0.1) + originY());
		
		int btnWidth = (int)(100 * scaler);
		int btnHeight = (int)(43 * scaler);
		int btnX = width() / 2 - btnWidth / 2 + originX();
		int btnY = (int)(height() * 0.8 + originY());
		
		boolean buttonHovered = mouseX >= btnX && mouseX <= btnX + btnWidth && mouseY >= btnY && mouseY <= btnY + btnHeight;
		
		g.setColor(buttonHovered ? new Color(185, 60, 80) : new Color(100, 60, 80));
		g.fillRect(btnX, btnY, btnWidth, btnHeight);
		g.setStroke(new BasicStroke((float)(3 * scaler)));
		g.setColor(buttonHovered ? new Color(255, 60, 70) : new Color(170, 60, 70));
		g.drawRect(btnX, btnY, btnWidth, btnHeight);
		g.setColor(buttonHovered ? new Color(255, 230, 200) : new Color(255, 100, 100));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(28 * scaler)));
		g.drawString("BACK", width() / 2 - (int)(4 * 8 * scaler) + originX(), btnY + btnHeight / 2 + (int)(8 * scaler));
		
		if ((buttonHovered && !prevMouseDown && Shark.frame.isMouseDown) || this.escDown) Shark.currentScreen = new MainMenuGui();
		
		this.handleScroll();
		
		for (int i = 0; i < SCharacter.characters.size(); i++) this.drawCharacter(i, g, scaler, mouseX, mouseY, prevMouseDown);
		
	}
	
	private void drawCharacter(int idx, Graphics2D g, double scaler, int mouseX, int mouseY, boolean prevMouseDown) {
		
		SCharacter character = SCharacter.characters.get(idx);
		String name = character.getName();
		BufferedImage tex = character.getTexture();
		
		int row = idx / COL_COUNT;
		
		row -= this.scroll;
		if (row < 0 || row > 3) return;
		
		if (idx - row * COL_COUNT < 0) return;
		
		int col = (idx - row * COL_COUNT) % COL_COUNT;
		
		int width = (int)(height() * 0.15);
		int height = width;
		int x = (int)(originX() + width() / 2 - width * (COL_COUNT / 2.0) + width * col);
		int y = (int)(originY() + 50 * scaler + height * row);
		
		int rippleOffset = 200;
		long time = System.currentTimeMillis();
		time += rippleOffset * col;
		time -= rippleOffset * row;
		long cycleTime = 2000;
		double mult = time % cycleTime;
		mult /= cycleTime;
		mult *= Math.PI * 2;
		mult = Math.sin(mult);
		mult++;
		mult /= 2;
		
		boolean hovered = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
		boolean isCurrentCharacter = idx == SCharacter.currentCharacter.getID();
		
		if (isCurrentCharacter) g.setColor(new Color(230, (int)(80 + 100 * mult), (int)(180 - 100 * mult)));
		else if (hovered) g.setColor(new Color(100, (int)(50 * mult), (int)(50 - 50 * mult)));
		else if ((row + this.scroll) % 2 == 0) g.setColor(col % 2 == 0 ? new Color(30, 25, 25) : new Color(20, 15, 15));
		else g.setColor(col % 2 == 0 ? new Color(20, 15, 15) : new Color(10, 5, 5));
		g.fillRect(x, y, width, height);
		
		int texSize = (int)(width - 12 * scaler);
		int texX = (int)(x + 6 * scaler);
		int texY = (int)(y + 1 * scaler);
		g.drawImage(tex, texX, texY, texSize, texSize, null);
		
		if (!isCurrentCharacter) g.setColor(new Color(230, (int)(80 + 100 * mult), (int)(180 - 100 * mult)));
		else g.setColor(new Color(255, (int)(155 + 100 * mult), (int)(255 - 100 * mult)));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(12 * scaler)));
		g.drawString(name, (int)(x + width / 2 - (name.length() * 6 * scaler) / 2), (int)(y + height - 2 * scaler));
		
		if (hovered && !isCurrentCharacter && !prevMouseDown && Shark.frame.isMouseDown) {
			SCharacter.currentCharacter = SCharacter.characters.get(idx);
			FileManager.writeFile("", "character", SCharacter.currentCharacter.getName());
		}
		
	}
	
	private void handleScroll() {
		
		boolean prevUp = this.prevUpDown;
		this.prevUpDown = this.upDown;
		boolean prevDown = this.prevDownDown;
		this.prevDownDown = this.downDown;
		boolean up = this.upDown && !prevUp;
		boolean down = this.downDown && !prevDown;
		
		int rows = SCharacter.characters.size() / COL_COUNT;
		if (SCharacter.characters.size() % COL_COUNT != 0) rows++;
		
		int maxScroll = rows - 4;
		
		if (up) this.scroll--;
		if (down) this.scroll++;
		
		if (this.scroll > maxScroll) this.scroll = maxScroll;
		if (this.scroll < 0) this.scroll = 0;
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == 27) this.escDown = true;
		if (key == 38) this.upDown = true;
		if (key == 40) this.downDown = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == 27) this.escDown = false;
		if (key == 38) this.upDown = false;
		if (key == 40) this.downDown = false;
	}
	
}

package me.REDSTONER85.screens.keybinds;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import me.REDSTONER85.Shark;
import me.REDSTONER85.screens.GuiScreen;
import me.REDSTONER85.screens.MainMenuGui;
import me.REDSTONER85.utils.AudioManager;
import me.REDSTONER85.utils.settings.Keybinds;

public class KeybindGui extends GuiScreen {
	
	public boolean prevMouseDown = true;
	public boolean escDown = false;
	public boolean upDown = false;
	public boolean prevUpDown = true;
	public boolean downDown = false;
	public boolean prevDownDown = true;
	public int scroll = 0;
	
	public KeybindGui() {}
	public KeybindGui(int scrollIn) {
		this.scroll = scrollIn;
	}
	
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
		g.drawString("keybinds", width() / 2 - (int)(8 * 8 * scaler) + originX(), (int)(height() * 0.1) + originY());
		
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
		
		for (int i = 0; i < Keybinds.keybinds.size(); i++) this.drawKeybind(i, g, scaler, mouseX, mouseY, prevMouseDown);
		
	}
	
	private void drawKeybind(int idx, Graphics2D g, double scaler, int mouseX, int mouseY, boolean prevMouseDown) {
		
		String name = Keybinds.getKeybindName(idx);
		int key = Keybinds.getKeybind(idx);
		
		idx -= this.scroll;
		if (idx < 0 || idx > 3) return;
		
		int width = (int)(width() - 100 * scaler);
		int height = (int)(height() * 0.15);
		int x = (int)(50 * scaler + originX());
		int y = (int)(originY() + 50 * scaler + height * idx);
		
		g.setColor((idx + this.scroll) % 2 == 0 ? new Color(30, 25, 25) : new Color(20, 15, 15));
		g.fillRect(x, y, width, height);
		g.setColor((idx + this.scroll) % 2 == 0 ? new Color(10, 5, 5) : new Color(5, 0, 0));
		g.fillRect(x, y, (int)((name.length() + 1) * 20 * scaler), height);
		g.setColor(new Color(230, 230, 180));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(35 * scaler)));
		g.drawString(name, (int)(x + 10 * scaler), (int)(y + 40 * scaler));
		g.setColor(new Color(230, 230, 230));
		g.drawString(KeyEvent.getKeyText(key), (int)(x + 10 * scaler + (name.length() + 1) * 20 * scaler), (int)(y + 40 * scaler));
		
		int btnWidth = height;
		int btnHeight = height;
		int btnX = x + width - btnWidth;
		int btnY = y;
		
		boolean buttonHovered = mouseX >= btnX && mouseX <= btnX + btnWidth && mouseY >= btnY && mouseY < btnY + btnHeight;
		
		if (buttonHovered) g.setColor(new Color(185, 60, 80));
		else g.setColor((idx + this.scroll) % 2 == 0 ? new Color(100, 60, 80) : new Color(90, 50, 70));
		g.fillRect(btnX, btnY, btnWidth, btnHeight);
		if (buttonHovered) g.setColor(new Color(255, 230, 200));
		else g.setColor((idx + this.scroll) % 2 == 0 ? new Color(255, 100, 100) : new Color(245, 90, 90));
		g.setFont(new Font("Consolas", Font.PLAIN, (int)(60 * scaler)));
		g.drawString((char)916 + "", (int)(btnX + 11 * scaler), (int)(btnY + 48 * scaler));
		
		if (buttonHovered && !prevMouseDown && Shark.frame.isMouseDown) Shark.currentScreen = new KeybindSetGui(idx + this.scroll, this.scroll);
		
	}
	
	private void handleScroll() {
		
		boolean prevUp = this.prevUpDown;
		this.prevUpDown = this.upDown;
		boolean prevDown = this.prevDownDown;
		this.prevDownDown = this.downDown;
		boolean up = this.upDown && !prevUp;
		boolean down = this.downDown && !prevDown;
		
		int maxScroll = Keybinds.keybinds.size() - 4;
		
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

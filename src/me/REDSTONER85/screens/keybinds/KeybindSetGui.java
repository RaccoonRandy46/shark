package me.REDSTONER85.screens.keybinds;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import me.REDSTONER85.Shark;
import me.REDSTONER85.screens.GuiScreen;
import me.REDSTONER85.utils.AudioManager;
import me.REDSTONER85.utils.FileManager;
import me.REDSTONER85.utils.settings.Keybinds;

public class KeybindSetGui extends GuiScreen {

	public int keybind;
	public String name;
	public int returnScroll;
	
	public boolean prevMouseDown = true;
	public boolean escDown = false;
	public int newKey = -1;
	
	public KeybindSetGui(int keybindIn, int returnScrollIn) {
		this.keybind = keybindIn;
		this.name = Keybinds.getKeybindName(keybindIn);
		this.returnScroll = returnScrollIn;
	}
	
	@Override
	public void drawFrame(Graphics2D g, double scaler) {
		
		if (this.newKey != -1) {
			Keybinds.setKeybind(this.keybind, this.newKey);
			FileManager.writeFile("keybinds", this.name, this.newKey);
			Shark.currentScreen = new KeybindGui(this.returnScroll);
		}
		
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
		g.drawString("CANCEL", width() / 2 - (int)(6 * 8 * scaler) + (int)(3 * scaler) + originX(), btnY + btnHeight / 2 + (int)(8 * scaler));
		
		if ((buttonHovered && !prevMouseDown && Shark.frame.isMouseDown) || this.escDown) Shark.currentScreen = new KeybindGui(this.returnScroll);
		
		g.setColor(new Color(30, 25, 25));
		g.fillRect(originX() + (int)(50 * scaler), originY() + height() / 2 - (int)(130 * scaler), width() - (int)(100 * scaler), (int)(200 * scaler));
		
		g.setColor(new Color(255, 200, 200));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(38 * scaler)));
		g.drawString("press a key to bind to", width() / 2 - (int)(22 * 11 * scaler) + (int)(3 * scaler) + originX(), originY() + height() / 2 - (int)(50 * scaler));
		
		g.setColor(new Color(255, 100, 100));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(50 * scaler)));
		g.drawString(this.name, width() / 2 - (int)(this.name.length() * 14 * scaler) + (int)(3 * scaler) + originX(), originY() + height() / 2);
		
		String dots = "";
		for (int i = 0; i < System.currentTimeMillis() % 999 / 333 + 1; i++) dots += '.';
		
		g.setColor(new Color(150, 50, 70));
		g.setFont(new Font("Consolas", Font.PLAIN, (int)(38 * scaler)));
		g.drawString(dots, width() / 2 - (int)(dots.length() * 11 * scaler) + (int)(3 * scaler) + originX(), originY() + height() / 2 + (int)(30 * scaler));
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == 27) {
			this.escDown = true;
			return;
		}
		this.newKey = key;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == 27) {
			this.escDown = false;
			return;
		}
	}
	
}

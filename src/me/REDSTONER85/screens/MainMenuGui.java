package me.REDSTONER85.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import me.REDSTONER85.Shark;
import me.REDSTONER85.screens.keybinds.KeybindGui;
import me.REDSTONER85.utils.AudioManager;
import me.REDSTONER85.utils.settings.SCharacter;

public class MainMenuGui extends GuiScreen {

	public boolean prevMouseDown = true;
	public boolean zDown = false;
	public boolean prevZdown = true;
	public boolean spaceDown = false;
	public boolean kDown = false;
	public boolean cDown = false;
	public boolean sDown = false;
	
	@Override
	public void drawFrame(Graphics2D g, double scaler) {
		
		if (this.kDown) Shark.currentScreen = new KeybindGui();
		if (this.cDown) Shark.currentScreen = new CharacterGui();
		if (this.sDown) Shark.currentScreen = new SettingsGui();
		
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
		
		int btnWidth = (int)(350 * scaler);
		int btnHeight = (int)(150 * scaler);
		int btnX = width() / 2 - btnWidth / 2 + originX();
		int btnY = height() / 2 + originY();
		
		boolean buttonHovered = mouseX >= btnX && mouseX <= btnX + btnWidth && mouseY >= btnY && mouseY <= btnY + btnHeight;
		
		boolean zenSwitch = this.zDown && !this.prevZdown;
		this.prevZdown = this.zDown;
		if (zenSwitch) SharkGui.zen = !SharkGui.zen;
		
		g.setColor(buttonHovered ? new Color(185, 60, 80) : new Color(100, 60, 80));
		g.fillRect(btnX, btnY, btnWidth, btnHeight);
		g.setStroke(new BasicStroke((float)(10 * scaler)));
		g.setColor(buttonHovered ? new Color(255, 60, 70) : new Color(170, 60, 70));
		g.drawRect(btnX, btnY, btnWidth, btnHeight);
		g.setColor(buttonHovered ? new Color(255, 230, 200) : new Color(255, 100, 100));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(100 * scaler)));
		if (!SharkGui.zen) g.drawString("PLAY", width() / 2 - (int)(4 * 28 * scaler) + originX(), btnY + btnHeight / 2 + (int)(27 * scaler));
		else g.drawString("ZEN", width() / 2 - (int)(3 * 28 * scaler) + originX(), btnY + btnHeight / 2 + (int)(27 * scaler));
		
		boolean prevMouseDown = this.prevMouseDown;
		this.prevMouseDown = Shark.frame.isMouseDown;
		
		if ((buttonHovered && !prevMouseDown && Shark.frame.isMouseDown) || this.spaceDown) Shark.currentScreen = new SharkGui();
		
		drawTitle(g, scaler);
		
	}
	
	private void drawTitle(Graphics2D g, double scaler) {
		
		float rotationMult = System.currentTimeMillis() % 2500;
		rotationMult /= 2500;
		rotationMult *= Math.PI * 2;
		rotationMult = (float)Math.sin(rotationMult) + 1;
		rotationMult /= 2;
		
		int imgSize = (int)(100 * scaler);
		g.translate(width() / 2 + originX(), (int)(110 * scaler) + originY());
		
		double rotationAngle = Math.toRadians(-30) + Math.toRadians(60) * rotationMult;
		g.rotate(rotationAngle);
		g.drawImage(SCharacter.currentCharacter.getTexture(), -imgSize / 2, -imgSize / 2, imgSize, imgSize, null);
		
		g.rotate(-rotationAngle);
		g.translate(width() / -2, (int)(-110 * scaler));
		
		String titleText = SCharacter.currentCharacter.getName();
		int stringLength = (int)(titleText.length() * 20 * scaler);
		g.translate(width() / 2, (int)(110 * scaler));
		g.rotate(-rotationAngle * 0.4);
		
		long cycleTime = 2000;
		double mult = System.currentTimeMillis() % cycleTime;
		mult /= cycleTime;
		mult *= Math.PI * 2;
		mult = Math.sin(mult);
		mult++;
		mult /= 2;
		
		g.setColor(new Color(230, 80, (int)(80 + 50 * mult)));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(70 * scaler)));
		g.drawString(titleText, -stringLength, (int)(20 * scaler));
		
		g.rotate(rotationAngle * 0.4);
		g.translate(width() / -2, (int)(-110 * scaler));
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == 67) this.cDown = true;
		if (key == 75) this.kDown = true;
		if (key == 83) this.sDown = true;
		if (key == 32) this.spaceDown = true;
		if (key == 90) this.zDown = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == 67) this.cDown = false;
		if (key == 75) this.kDown = false;
		if (key == 83) this.sDown = false;
		if (key == 32) this.spaceDown = false;
		if (key == 90) this.zDown = false;
	}

}

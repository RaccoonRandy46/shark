package me.REDSTONER85.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import me.REDSTONER85.Shark;
import me.REDSTONER85.utils.ArrowFlash;
import me.REDSTONER85.utils.AudioManager;
import me.REDSTONER85.utils.FileManager;
import me.REDSTONER85.utils.LevelChecker;
import me.REDSTONER85.utils.Num;
import me.REDSTONER85.utils.TextureBin;
import me.REDSTONER85.utils.Utils;
import me.REDSTONER85.utils.settings.Keybinds;
import me.REDSTONER85.utils.settings.SCharacter;
import me.REDSTONER85.utils.settings.Settings;

public class SharkGui extends GuiScreen {

	public static boolean zen = false;
	
	public static final int BLANK = 0;
	public static final int SHARK = 1;
	public static final int WALL = 2;
	public static final int ARROW_UP = 3;
	public static final int ARROW_DOWN = 4;
	public static final int ARROW_LEFT = 5;
	public static final int ARROW_RIGHT = 6;
	public static final int FLAG = 7;
	
	private static final float SHARK_SPEED = 200;
	
	private int round = 0;
	private long roundStartTime;
	private long roundTimeout = 10000;
	private boolean gameOver = false;
	private boolean endByTimeout = false;
	private boolean endByX = false;
	
	private List<List<Integer>> board = new ArrayList<>();
	private int sharkX;
	private int sharkY;
	private int prevSharkX;
	private int prevSharkY;
	private long timeSharkMoved = 0;
	private int flagY;
	private int flagX;
	private List<ArrowFlash> arrowFlashes = new ArrayList<>();
	
	private boolean wHeld = false;
	private boolean aHeld = false;
	private boolean sHeld = false;
	private boolean dHeld = false;
	private boolean xHeld = false;
	
	private boolean prevWheld = false;
	private boolean prevAheld = false;
	private boolean prevSheld = false;
	private boolean prevDheld = false;
	private boolean prevXheld = false;
	
	private boolean shiftHeld = false;
	private boolean eHeld = false;
	private boolean spaceHeld = false;
	private boolean escHeld = false;
	
	private boolean prevMouseDown = false;
	
	private LevelChecker checker = null;
	
	public SharkGui() {
		this.startNextRound();
	}
	
	private static int tileSize(double scaler) {
		return (int)(46 * scaler);
	}
	
	@Override
	public void drawFrame(Graphics2D g, double scaler) {
		
		if (this.escHeld) Shark.currentScreen = new MainMenuGui();
		
		Color wallColor = getWallColor();
		
		float[] hsv = Utils.RGBtoHSV(wallColor.getRed(), wallColor.getGreen(), wallColor.getBlue());
		float[] rgb = Utils.HSVtoRGB(hsv[0] - 30, 100, 24);
		Color bgColor = new Color((int)rgb[0], (int)rgb[1], (int)rgb[2]);
		if (hsv[1] == 0) bgColor = new Color(60, 60, 60);
		
		g.setColor(bgColor);
		g.fillRect(0, 0, windowWidth(), windowHeight());
		
		float roundTime = System.currentTimeMillis() - this.roundStartTime;
		
		float mult = roundTime / 300f;
		mult--;
		mult *= -1;
		if (mult < 0) mult = 0;
		int alpha = (int)(mult * 255f);
		
		g.setColor(new Color(wallColor.getRed(), wallColor.getGreen(), wallColor.getBlue(), alpha / 3));
		g.fillRect(0, 0, windowWidth(), windowHeight());
		
		this.drawBoard(g, scaler);
		
		if (this.gameOver) {
			if (this.sharkX != -1) this.prevSharkX = this.sharkX;
			if (this.sharkY != -1) this.prevSharkY = this.sharkY;
			this.sharkX = -1;
			this.sharkY = -1;
			this.drawDeathScreen(g, scaler);
			return;
		}
		
		g.setColor(wallColor);
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(24 * scaler)));
		String roundText = "Round " + this.round;
		g.drawString(roundText, width() / 2 + originX() - roundText.length() * 6 * (float)scaler, (float)(25 * scaler) + originY());
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(18 * scaler)));
		if (!zen) g.drawString("PB: " + Settings.highScore, originX() + (int)(10 * scaler), originY() + (int)(20 * scaler));
		
		float timeMult = (float)(System.currentTimeMillis() - this.roundStartTime) / this.roundTimeout;
		int barLength = (int)(windowWidth() * timeMult);
		if (!zen) g.fillRect(0, windowHeight() - 20, barLength, 20);
		
		if (!zen && this.round % 10 == 0 && this.round <= 80) {
			int x = 130;
			int y = 218;
			int stroke = (int)(5 * scaler);
			g.setFont(new Font("Consolas", Font.ITALIC, (int)(70 * scaler)));
			g.setColor(new Color(30, 30, 30, alpha));
			g.drawString("Faster!", width() / 2 - (int)((x - stroke) * scaler) + originX(), (int)((y + stroke) * scaler) + originY());
			g.drawString("Faster!", width() / 2 - (int)((x + stroke) * scaler) + originX(), (int)((y - stroke) * scaler) + originY());
			g.drawString("Faster!", width() / 2 - (int)((x + stroke) * scaler) + originX(), (int)((y + stroke) * scaler) + originY());
			g.drawString("Faster!", width() / 2 - (int)((x - stroke) * scaler) + originX(), (int)((y - stroke) * scaler) + originY());
			g.setColor(new Color(wallColor.getRed(), wallColor.getGreen(), wallColor.getBlue(), alpha));
			g.drawString("Faster!", width() / 2 - (int)(x * scaler) + originX(), (int)(y * scaler) + originY());
		}
		
		this.handleInput();
		
		if (!zen && roundTime >= this.roundTimeout) {
			this.gameOver = true;
			this.endByTimeout = true;
		}
		
	}
	
	private void handleInput() {
		
		boolean prevXheld = this.prevXheld;
		this.prevXheld = this.xHeld;
		
		if (this.xHeld && !prevXheld) {
			
			this.prevSharkX = this.sharkX;
			this.prevSharkY = this.sharkY;
			
			if (this.checker.isPossible()) {
				this.endByX = true;
				this.gameOver = true;
				return;
			}
			
			this.startNextRound();
			return;
			
		}
		
		int x = this.sharkX;
		int y = this.sharkY;
		
		boolean prevWheld = this.prevWheld;
		this.prevWheld = this.wHeld;
		
		boolean prevAheld = this.prevAheld;
		this.prevAheld = this.aHeld;
		
		boolean prevSheld = this.prevSheld;
		this.prevSheld = this.sHeld;
		
		boolean prevDheld = this.prevDheld;
		this.prevDheld = this.dHeld;
		
		boolean wPressed = this.wHeld && !prevWheld;
		boolean aPressed = this.aHeld && !prevAheld;
		boolean sPressed = this.sHeld && !prevSheld;
		boolean dPressed = this.dHeld && !prevDheld;
		
		if (!wPressed && !aPressed && !sPressed && !dPressed) return;
		
		int movement = this.shiftHeld ? 2 : 1;
		if (!Settings.getNPSetting(Settings.NP_ALLOW_DASHES)) movement = 1;
		
		if (aPressed) x -= movement;
		if (dPressed) x += movement;
		
		int tile = this.getTile(x, y);
		
		if (tile == WALL) {
			this.gameOver = true;
			return;
		}
		
		int arrowMovements = 0;
		while (tile >= 3 && tile <= 6) {
			
			tile = this.getTile(x, y);
			
			if (++arrowMovements >= 50 || tile == WALL) {
				this.gameOver = true;
				return;
			}
			
			this.arrowFlashes.add(new ArrowFlash(x, y));
			
			switch (tile) {
			case ARROW_UP:
				y--;
				break;
			case ARROW_DOWN:
				y++;
				break;
			case ARROW_LEFT:
				x--;
				break;
			case ARROW_RIGHT:
				x++;
				break;
			}
			
		}
		
		if (tile == FLAG) {
			this.startNextRound();
			return;
		}
		
		if (wPressed) y -= movement;
		if (sPressed) y += movement;
		
		tile = this.getTile(x, y);
		
		if (tile == SHARK) return;
		if (tile == WALL) {
			this.gameOver = true;
			return;
		}
		
		arrowMovements = 0;
		while (tile >= 3 && tile <= 6) {
			
			tile = this.getTile(x, y);
			
			if (++arrowMovements >= 50 || tile == WALL) {
				this.gameOver = true;
				return;
			}
			
			this.arrowFlashes.add(new ArrowFlash(x, y));
			
			switch (tile) {
			case ARROW_UP:
				y--;
				break;
			case ARROW_DOWN:
				y++;
				break;
			case ARROW_LEFT:
				x--;
				break;
			case ARROW_RIGHT:
				x++;
				break;
			}
			
		}
		
		if (tile == FLAG) {
			this.startNextRound();
			return;
		}
		
		this.moveShark(x, y);
		
	}
	
	private void moveShark(int x, int y) {
		this.timeSharkMoved = System.currentTimeMillis();
		this.prevSharkX = this.sharkX;
		this.prevSharkY = this.sharkY;
		this.sharkX = x;
		this.sharkY = y;
	}
	
	private void drawDeathScreen(Graphics2D g, double scaler) {
		
		if (this.spaceHeld) Shark.currentScreen = new SharkGui();
		
		if (this.eHeld) {
			
			int tileSize = tileSize(scaler);
			
			if (!this.checker.isPossible()) {
				
				int displaySharkX = this.prevSharkX * tileSize + originX() + (int)(50 * scaler) + tileSize;
				int displaySharkY = this.prevSharkY * tileSize + originY() + (int)(33 * scaler) + tileSize;
				
				g.drawImage(SCharacter.currentCharacter.getTexture(), displaySharkX, displaySharkY, tileSize, tileSize, null);
				
				g.setColor(new Color(255, 0, 0, 255));
				
				g.setStroke(new BasicStroke(5 * (float)scaler));
				int _3o = (int)(30 * scaler);
				g.drawLine(_3o + originX(), _3o + originY(), width() - _3o + originX(), height() - _3o + originY());
				g.drawLine(width() - _3o + originX(), _3o + originY(), _3o + originX(), height() - _3o + originY());
				
				return;
				
			}
			
			List<int[]> pathFromDeath = this.checker.getPath();
			
			double cycle = 60000d / 35;
			double mult = ((System.currentTimeMillis() - AudioManager.lastRepeat) - 50) % cycle;
			if (mult < 0) mult = 0;
			mult /= cycle + 1000;
			mult = Math.sqrt(mult);
			float stroke = (float)(6 * scaler * mult);
			stroke *= -1;
			stroke += 6 * scaler;
			
			if (pathFromDeath != null) {
				for (int i = -1; i < pathFromDeath.size() - 1; i++) {
					
					double colorCycle = 500;
					double colorGradientOffset = 50;
					double colorMult = (System.currentTimeMillis() + i * colorGradientOffset) % colorCycle;
					colorMult /= colorCycle;
					
					int[] pos = i >= 0 ? pathFromDeath.get(i) : new int[] {this.flagX, this.flagY};
					int[] nextPos = pathFromDeath.get(i + 1);
					g.setStroke(new BasicStroke(stroke));
					g.setColor(new Color(255, (int)(200 - colorMult * 200), (int)(100 - colorMult * 100)));
					int x1 = tileSize + pos[0] * tileSize + originX() + tileSize / 2 + (int)(50 * scaler);
					int y1 = tileSize + pos[1] * tileSize + originY() + tileSize / 2 + (int)(33 * scaler);
					int x2 = tileSize + nextPos[0] * tileSize + originX() + tileSize / 2 + (int)(50 * scaler);
					int y2 = tileSize + nextPos[1] * tileSize + originY() + tileSize / 2 + (int)(33 * scaler);
					g.drawLine(x1, y1, x2, y2);
					
				}
			}
			
			int displaySharkX = this.prevSharkX * tileSize + originX() + (int)(50 * scaler) + tileSize;
			int displaySharkY = this.prevSharkY * tileSize + originY() + (int)(33 * scaler) + tileSize;
			
			g.drawImage(SCharacter.currentCharacter.getTexture(), displaySharkX, displaySharkY, tileSize, tileSize, null);
			
			return;
			
		}
		
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, windowWidth(), windowHeight());
		
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(80 * scaler)));
		g.setColor(new Color(20, 20, 20));
		g.drawString("Game over!", width() / 2 - (int)(22 * 10 * scaler) + (int)(5 * scaler) + originX(), (int)(155 * scaler) + originY());
		g.setColor(new Color(255, 120, 120));
		g.drawString("Game over!", width() / 2 - (int)(22 * 10 * scaler) + originX(), (int)(150 * scaler) + originY());
		
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(30 * scaler)));
		
		String message = "You crashed!";
		if (this.endByTimeout) message = "You ran out of time!";
		else if (this.endByX) message = "It was possible!";
		
		g.setColor(new Color(255, 200, 200));
		g.drawString(message, width() / 2 - (int)(8 * scaler) * message.length() + originX(), (int)(180 * scaler) + originY());
		
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(24 * scaler)));
		String roundText = "Survived to round " + this.round;
		g.drawString(roundText, width() / 2 - roundText.length() * (int)(7 * scaler) + originX(), (int)(50 * scaler) + originY());
		
		int x = Shark.frame.mouseX();
		int y = Shark.frame.mouseY();
		
		int btnWidth = (int)(350 * scaler);
		int btnHeight = (int)(100 * scaler);
		int btnX = width() / 2 - btnWidth / 2 + originX();
		int btnY = height() / 2 + (int)(20 * scaler) + originY();
		
		boolean buttonHovered = x >= btnX && x <= btnX + btnWidth && y >= btnY && y <= btnY + btnHeight;
		
		g.setColor(buttonHovered ? new Color(185, 60, 80) : new Color(100, 60, 80));
		g.fillRect(btnX, btnY, btnWidth, btnHeight);
		g.setStroke(new BasicStroke(10 * (float)scaler));
		g.setColor(buttonHovered ? new Color(255, 60, 70) : new Color(170, 60, 70));
		g.drawRect(btnX, btnY, btnWidth, btnHeight);
		g.setColor(buttonHovered ? new Color(255, 230, 200) : new Color(255, 100, 100));
		g.setFont(new Font("Consolas", Font.ITALIC, (int)(100 * scaler)));
		g.drawString("RETRY", width() / 2 - (int)(5 * 28 * scaler) + originX(), btnY + btnHeight / 2 + (int)(30 * scaler));
		
		boolean prevMouseDown = this.prevMouseDown;
		this.prevMouseDown = Shark.frame.isMouseDown;
		if (prevMouseDown || !Shark.frame.isMouseDown || !buttonHovered) return;
		
		Shark.currentScreen = new SharkGui();
		
	}
	
	private void drawBoard(Graphics2D g, double scaler) {
		
		int tileSize = tileSize(scaler);
		
		for (int tileY = 0; tileY < 7; tileY++) {
			for (int tileX = 0; tileX < 11; tileX++) {
				
				int x = (tileX) * tileSize + originX() + (int)(50 * scaler);
				int y = (tileY) * tileSize + originY() + (int)(33 * scaler);
				
				if (tileY == 0 || tileY == 6 || tileX == 0 || tileX == 10) {
					drawTile(WALL, x, y, g, scaler);
					continue;
				}
				
				int tile = getTile(tileX - 1, tileY - 1);
				drawTile(tile, x, y, g, scaler);
				
			}
		}
		
		List<ArrowFlash> newFlashes = new ArrayList<>();
		for (ArrowFlash flash : this.arrowFlashes) {
			if (!flash.isFinished()) newFlashes.add(flash);
		}
		
		this.arrowFlashes = newFlashes;
		for (ArrowFlash flash : this.arrowFlashes) {
			
			int x = tileSize + flash.x * tileSize + originX() + (int)(50 * scaler);
			int y = tileSize + flash.y * tileSize + originY() + (int)(33 * scaler);
			
			g.setColor(new Color(0, 255, 255, flash.flashAlpha()));
			g.fillRoundRect(x, y, tileSize, tileSize, (int)(10 * scaler), (int)(10 * scaler));
			
		}
		
		if (this.gameOver) return;
		
		long moveTime = System.currentTimeMillis() - this.timeSharkMoved;
		float moveMult = 1;
		
		if (moveTime < SHARK_SPEED) {
			moveMult = moveTime / SHARK_SPEED;
			moveMult *= Math.PI / 2;
			moveMult = (float)Math.sin(moveMult);
		}
		
		int xDist = this.sharkX - this.prevSharkX;
		int yDist = this.sharkY - this.prevSharkY;
		xDist = (int)(xDist * moveMult * tileSize);
		yDist = (int)(yDist * moveMult * tileSize);
		
		int displaySharkX = this.prevSharkX * tileSize + xDist + originX() + (int)(50 * scaler) + tileSize;
		int displaySharkY = this.prevSharkY * tileSize + yDist + originY() + (int)(33 * scaler) + tileSize;
		
		g.drawImage(SCharacter.currentCharacter.getTexture(), displaySharkX, displaySharkY, tileSize, tileSize, null);
		
	}
	
	private void drawTile(int tile, int x, int y, Graphics2D g, double scaler) {
		
		if (tile == BLANK || tile == SHARK) return;
		
		int tileSize = tileSize(scaler);
		
		if (tile == WALL) {
			g.setColor(this.getWallColor());
			g.fillRoundRect(x, y, tileSize, tileSize, (int)(10 * scaler), (int)(10 * scaler));
			return;
		}
		
		BufferedImage ig = null;
		
		switch (tile) {
		case FLAG:
			ig = TextureBin.FLAG;
			break;
		case ARROW_UP:
			ig = TextureBin.ARROW_UP;
			break;
		case ARROW_DOWN:
			ig = TextureBin.ARROW_DOWN;
			break;
		case ARROW_LEFT:
			ig = TextureBin.ARROW_LEFT;
			break;
		case ARROW_RIGHT:
			ig = TextureBin.ARROW_RIGHT;
			break;
		}
		
		g.drawImage(ig, x, y, tileSize, tileSize, null);
		
	}
	
	private Color getWallColor() {
		return getWallColor(zen ? this.round % 90 : this.round);
	}
	
	private Color getWallColor(int roundIn) {
		
		double cycleTime = 2000;
		double mult = System.currentTimeMillis() % cycleTime;
		mult /= cycleTime;
		mult -= 0.5;
		if (mult < 0) mult *= -1;
		int offset = 70;
		offset = (int)(mult * offset);
		
		if (zen && roundIn < 10) return getZenColor();
		if (roundIn < 20) return new Color(255 - offset, 100 - offset, 100);
		if (roundIn < 30) return new Color(100 - offset, 255, 100 + offset);
		if (roundIn < 40) return new Color(100 - offset / 2, 180 - offset, 255);
		if (roundIn < 50) return new Color(140 + offset / 2, 100 - offset, 255);
		if (roundIn < 60) return new Color(255, 180 - offset, 100 - offset);
		if (roundIn < 70) return new Color(255, 255 - offset, 100 - offset);
		if (roundIn < 80) return new Color(255 - offset, 120 - offset, 180);
		return new Color(255, 255, 255);
		
	}
	
	private static Color getZenColor() {
		
		double cycleTime = 2000;
		double mult = System.currentTimeMillis() % cycleTime;
		mult /= cycleTime;
		mult *= Math.PI * 2;
		mult = Math.sin(mult);
		mult += 1;
		mult /= 2;
		int r = (int)(mult * 255);
		
		double cycleTime2 = 2500;
		double mult2 = System.currentTimeMillis() % cycleTime2;
		mult2 /= cycleTime2;
		mult2 *= Math.PI * 2;
		mult2 = Math.sin(mult2);
		mult2 += 1;
		mult2 /= 2;
		int g = (int)(mult2 * 255);
		
		double cycleTime3 = 3000;
		double mult3 = System.currentTimeMillis() % cycleTime3;
		mult3 /= cycleTime3;
		mult3 *= Math.PI * 2;
		mult3 = Math.sin(mult3);
		mult3 += 1;
		mult3 /= 2;
		int b = (int)(mult3 * 255);
		
		return new Color(r, g, b);
		
	}
	
	public int getTile(int x, int y) {
		if (x < 0 || x > 8 || y < 0 || y > 4) return WALL;
		if (x == this.sharkX && y == this.sharkY) return SHARK;
		return this.board.get(y).get(x);
	}
	
	public int getSharkX() {
		return this.sharkX;
	}
	
	public int getSharkY() {
		return this.sharkY;
	}
	
	public int getPrevSharkX() {
		return this.prevSharkX;
	}
	
	public int getPrevSharkY() {
		return this.prevSharkY;
	}
	
	public int getFlagX() {
		return this.flagX;
	}
	
	public int getFlagY() {
		return this.flagY;
	}
	
	private void startNextRound() {
		
		this.round++;
		if (!zen && this.round > Settings.highScore) {
			Settings.highScore = this.round;
			FileManager.writeFile("", "pb", this.round);
		}
		
		this.roundStartTime = System.currentTimeMillis();
		this.arrowFlashes = new ArrayList<>();
		if (this.round % 10 == 0 && this.round <= 80) this.roundTimeout -= 1000;
		if (this.round >= 80) this.roundTimeout = 2500;
		
		this.generateRound();
		
	}
	
	private void generateRound() {
		
		this.sharkX = (int)Num.ran(0, 8);
		this.sharkY = (int)Num.ran(0, 4);
		this.prevSharkX = this.sharkX;
		this.prevSharkY = this.sharkY;
		this.flagX = (int)Num.ran(0, 8);
		this.flagY = (int)Num.ran(0, 4);
		
		while (this.flagX == this.sharkX && this.flagY == this.sharkY) {
			this.flagX = (int)Num.ran(0, 8);
			this.flagY = (int)Num.ran(0, 4);
		}
		
		List<List<Integer>> rows = new ArrayList<>();
		for (int y = 0; y < 5; y++) {
			List<Integer> row = new ArrayList<>();
			for (int x = 0; x < 9; x++) {
				
				if (x == this.flagX && y == this.flagY) {
					row.add(FLAG);
					continue;
				}
				
				if (x == this.sharkX && y == this.sharkY) {
					row.add(BLANK);
					continue;
				}
				
				int tile = (int)Num.ran(2, 12);
				if (tile > 6) tile = 0;
				
				row.add(tile);
				
			}
			rows.add(row);
		}
		
		this.board = rows;
		
		this.checker = new LevelChecker(this);
		
		if (!Settings.getNPSetting(Settings.NP_ALLOW_IMPOSSIBLE)) {
			if (this.checker.isPossible()) {
				this.checker = new LevelChecker(this);
				return;
			}
			this.generateRound();
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == Keybinds.getKeybind("up")) this.wHeld = true;
		if (key == Keybinds.getKeybind("left")) this.aHeld = true;
		if (key == Keybinds.getKeybind("down")) this.sHeld = true;
		if (key == Keybinds.getKeybind("right")) this.dHeld = true;
		if (key == Keybinds.getKeybind("dash")) this.shiftHeld = true;
		if (key == Keybinds.getKeybind("see solution")) this.eHeld = true;
		if (key == Keybinds.getKeybind("pass")) this.xHeld = true;
		if (key == 32) this.spaceHeld = true;
		if (key == 27) this.escHeld = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == Keybinds.getKeybind("up")) this.wHeld = false;
		if (key == Keybinds.getKeybind("left")) this.aHeld = false;
		if (key == Keybinds.getKeybind("down")) this.sHeld = false;
		if (key == Keybinds.getKeybind("right")) this.dHeld = false;
		if (key == Keybinds.getKeybind("dash")) this.shiftHeld = false;
		if (key == Keybinds.getKeybind("see solution")) this.eHeld = false;
		if (key == Keybinds.getKeybind("pass")) this.xHeld = false;
		if (key == 32) this.spaceHeld = false;
	}

}

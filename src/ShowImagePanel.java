import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ShowImagePanel extends JPanel {
	private final Image image;

	public ShowImagePanel(Image image) {
		this.image = image;
	}

	public void paintComponent(Graphics graphics) {
		graphics.drawImage(image, 0, 0, this);
	}
}

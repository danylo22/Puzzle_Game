import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PuzzlePanel extends JPanel implements MouseListener {
	private final JFrame frame;
	private final List<JButton> imageButtons;
	private final List<Point2D> buttonPositions;
	private JButton buttonPressed;
	private JButton buttonEntered;

	public PuzzlePanel(JFrame frame, ArrayList<JButton> imageButtons, ArrayList<Point2D> buttonPositions) {
		this.frame = frame;
		this.imageButtons = imageButtons;
		this.buttonPositions = buttonPositions;
		setLayout(new GridLayout(4, 4));
		setPreferredSize(new Dimension(500, 500));
		addMouseListener(this);
	}

	public void mousePressed(MouseEvent mouseEvent) {
		buttonPressed = (JButton) mouseEvent.getSource();
	}

	public void mouseEntered(MouseEvent mouseEvent) {
		buttonEntered = (JButton) mouseEvent.getSource();
	}

	public void mouseReleased(MouseEvent mouseEvent) {
		int buttonPressedIndex = imageButtons.indexOf(buttonPressed);
		int buttonEnteredIndex = imageButtons.indexOf(buttonEntered);

		Collections.swap(imageButtons, buttonPressedIndex, buttonEnteredIndex);

		removeAll();

		imageButtons.forEach(this::add);

		validate();

		repaint();

		checkComplete();
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		// do nothing
	}

	public void mouseExited(MouseEvent mouseEvent) {
		// do nothing
	}

	public void checkComplete() {
		ArrayList<Point2D> currentButtonPositions = new ArrayList<>(imageButtons.size());
		for (JComponent imageButton : imageButtons) {
			Point temp = (Point) imageButton.getClientProperty("position");
			currentButtonPositions.add(temp);
		}

		if (buttonPositions.equals(currentButtonPositions)) {
			JOptionPane.showMessageDialog(frame,"You Win!");
			System.exit(0);
		}
	}
}

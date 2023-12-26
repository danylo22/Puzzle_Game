import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Puzzle {
	private JFrame frame;
	private final JPanel basePanel = new JPanel();
	private final JPanel topPanel = new JPanel();
	private final ArrayList<JButton> imageButtons = new ArrayList<>();
	private final ArrayList<Point2D> buttonPositions = new ArrayList<>();

	private JButton LoadImageBtn, ShowImageBtn,AutoSolveBtn;
	private Image originalImage;
	private final PuzzlePanel MyImagePanel = new PuzzlePanel(frame, imageButtons, buttonPositions);

	public static void main(String[] args) {
		Puzzle ImagePuzzleApp = new Puzzle();
		ImagePuzzleApp.start();
	}
	

	private void start() {
		
		frame = new JFrame ("Puzzle Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		LoadImageBtn = new JButton("Load New Image");
		ShowImageBtn = new JButton("Show Original Image");
		AutoSolveBtn = new JButton("Auto Solve");
		
		LoadImageBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent actionEvent) {
					GetImageFile();
					ClearData();
					BuildPuzzle();
					ShuffleButtons(imageButtons);
					MyImagePanel.validate();
			}
		});
				
		ShowImageBtn.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent actionEvent) {
				JFrame jf2 = new JFrame();
				jf2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ShowImagePanel newPanel = new ShowImagePanel(originalImage);
				jf2.getContentPane().add(newPanel);
				jf2.setSize(500,500);
				jf2.setVisible(true);
			}
		});


		AutoSolveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AutoSolvePuzzle();
				MyImagePanel.checkComplete();
			}
		});

		basePanel.setLayout(new GridLayout(1,3));
		basePanel.add(LoadImageBtn);
		basePanel.add(ShowImageBtn);

		basePanel.add(AutoSolveBtn);

		frame.getContentPane().add(BorderLayout.SOUTH, basePanel);

		GetImageFile();
		BuildPuzzle();
		ShuffleButtons(imageButtons);

		topPanel.setLayout(new GridBagLayout());
		topPanel.add(MyImagePanel);
		frame.add(topPanel);

		frame.setSize(570, 570);
		frame.setVisible(true);
		frame.pack();
		
	}
	

	public String returnFilePath(int check, JFileChooser cf) {
		if (JFileChooser.CANCEL_OPTION== check) {
			System.exit(1);
		}
		else if (JFileChooser.APPROVE_OPTION==check) {
			File ActualFile = cf.getSelectedFile();
			return  ActualFile.getAbsolutePath().toString();
		}
		return "";	
	}

	public void ShuffleButtons(ArrayList<JButton> imageButtons) {
		Collections.shuffle(imageButtons);
		for (JButton button: imageButtons) {
			MyImagePanel.add(button);
		}
	}

	public void UnShuffleButtons() {
		MyImagePanel.removeAll();
		Collections.sort(imageButtons, Comparator.comparing(button -> (Point) button.getClientProperty("position"), this::comparePoints));
		for (JButton button: imageButtons) {
			MyImagePanel.add(button);
		}
	}
	private int comparePoints(Point point1, Point point2) {
		// Compare points based on row and column values
		if (point1.getX() > point2.getX()) {
			return 1;
		} else if (point1.getX() < point2.getX()) {
			return -1;
		} else {
			return Double.compare(point1.getY(), point2.getY());
		}
	}

	public void ClearData(){
		MyImagePanel.removeAll();
		imageButtons.clear();
		buttonPositions.clear();
	}

	public void BuildPuzzle() {

		for(int rows=0;rows<4;++rows) {
			for(int cols=0; cols<4; ++cols) {
				Image croppedImg = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(originalImage.getSource(), new CropImageFilter(cols*125,rows*125,125,125)));
				JButton button = new JButton(new ImageIcon(croppedImg));
				imageButtons.add(button);
				Point currentPosition= new Point(rows,cols);
				button.putClientProperty("position", currentPosition);
				buttonPositions.add(currentPosition);
				button.addMouseListener(MyImagePanel);
			}
		}
	}

	public void GetImageFile() {
		FileNameExtensionFilter AcceptableFormat = new FileNameExtensionFilter("Image Files", "jpg", "gif", "png","tif");
		JFileChooser ChooseFile = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		ChooseFile.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		ChooseFile.addChoosableFileFilter(AcceptableFormat);
		ChooseFile.setAcceptAllFileFilterUsed(false);
		int check = ChooseFile.showOpenDialog(null);		
		String filepath = returnFilePath(check,ChooseFile);
		ImageIcon tempImageIcon = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(500,500, Image.SCALE_SMOOTH));
		System.out.println(tempImageIcon);
		originalImage = tempImageIcon.getImage();
	}

	private void AutoSolvePuzzle() {
		UnShuffleButtons();
		MyImagePanel.validate();
	}
}
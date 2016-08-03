package ViewControl;
/*
 *  constructs a prototype Lane View
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Model.Bowler;
import Model.Lane;
import Model.Party;

import java.util.*;

public class LaneView implements ActionListener, Observer {

	private boolean initDone = false;
	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int cur;
	Iterator bowlIt;
	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;
	JButton maintenance;
	Lane lane;

	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});
		cpanel.add(new JPanel());
	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
						BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
					BorderFactory.createTitledBorder(
							((Bowler) bowlers.get(i)).getNick()));
			pins[i].setLayout(new GridLayout(0, 10));
			for (int k = 0; k != 10; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
						BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
	}

	@Override
	public void update(Observable o, Object arg) {		
		if(!(o instanceof Lane)){
			return;
		}
		Lane le = (Lane)o;

		if (lane.isPartyAssigned() && !lane.isGameFinished()) {
			
			if(!initDone){
				cpanel.removeAll();
				cpanel.add(makeFrame(le.getParty()), "Center");
			}
			
			int numBowlers = le.getParty().getMembers().size();
		
			if (le.getFrameNumber() == 0 && le.getBall() == 0 && le.getBowlIndex() == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(le.getParty()), "Center");
				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				Insets buttonMargin = new Insets(4, 4, 4, 4);

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = new JPanel();
				maintenancePanel.setLayout(new FlowLayout());
				maintenance.addActionListener(this);
				maintenancePanel.add(maintenance);

				buttonPanel.add(maintenancePanel);

				cpanel.add(buttonPanel, "South");

				frame.pack();
				cpanel.setVisible(true);
			}
			
			int[][] lescores = le.getCumulScores();
			HashMap scores = le.getScores();
			this.bowlers = le.getParty().getMembers();
			for (int k = 0; k < numBowlers; k++) {
				for (int i = 0; i <= le.getFrameNumber() - 1; i++) {
					if (lescores[k][i] != 0){
						this.scoreLabel[k][i].setText((new Integer(lescores[k][i])).toString());
					}
				}
				for (int i = 0; i < 21; i++) {
					if (((int[]) (scores.get(bowlers.get(k))))[i] != -1){
						if (((int[]) scores.get(bowlers.get(k)))[i] == 10 && (i % 2 == 0 || i == 19)){
							ballLabel[k][i].setText("X");
						}					
						else if (i > 0 && ((int[]) scores.get(bowlers.get(k)))[i] + ((int[]) scores.get(bowlers.get(k)))[i - 1] == 10 && i % 2 == 1){
							ballLabel[k][i].setText("/");
						}
						else if ( ((int[])scores.get(bowlers.get(k)))[i] == -2 ){
							ballLabel[k][i].setText("F");
						} 
						else{
							ballLabel[k][i].setText((new Integer(((int[]) scores.get(bowlers.get(k)))[i])).toString());
						}
					}
				}
			}
		}
		else{
			initDone = false;
		}
	}
}

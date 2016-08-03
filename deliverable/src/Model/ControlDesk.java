package Model;

/**
 * Class that represents control desk
 *
 */

import java.util.*;
import java.io.*;

public class ControlDesk extends Observable implements Runnable {

	/** The collection of Lanes */
	private HashSet<Lane> lanes;
	/** The party wait queue */
	private Queue partyQueue;
	/** The number of lanes represented */
	private int numLanes;

	/**
	 * Constructor for the ControlDesk class
	 *
	 * @param numlanes	the numbler of lanes to be represented
	 *
	 */
	public ControlDesk(int numLanes) {
		this.numLanes = numLanes;
		lanes = new HashSet(numLanes);
		partyQueue = new Queue();
		for (int i = 0; i < numLanes; i++) {
			lanes.add(new Lane());
		}
		(new Thread(this, "Control Desk Thread")).start();
	}

	/**
	 * Main loop for ControlDesk's thread
	 * 
	 */
	public void run() {
		while (true) {
			assignLane();
			try {
				Thread.sleep(250);
			} catch (Exception e) {}
		}
	}

	/**
	 * Retrieves a matching Bowler from the bowler database.
	 *
	 * @param nickName	The NickName of the Bowler
	 *
	 * @return a Bowler object.
	 *
	 */
	private Bowler registerPatron(String nickName) {
		Bowler patron = null;
		try {
			patron = BowlerFile.getBowlerInfo(nickName);
		} catch (FileNotFoundException e) {
			System.err.println("Error..." + e);
		} catch (IOException e) {
			System.err.println("Error..." + e);
		}
		return patron;
	}

	/**
	 * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
	 *
	 */
	public void assignLane() {
		Iterator it = lanes.iterator();
		while (it.hasNext() && partyQueue.hasMoreElements()) {
			Lane curLane = (Lane) it.next();

			if (curLane.isPartyAssigned() == false) {
				System.out.println("ok... assigning this party");
				curLane.assignParty(((Party) partyQueue.next()));
			}
		}
		publish();
	}

	/**
	 * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
	 *
	 * @param partyNicks	A Vector of NickNames
	 *
	 */
	public void addPartyQueue(Vector partyNicks) {
		Vector partyBowlers = new Vector();
		for (int i = 0; i < partyNicks.size(); i++) {
			Bowler newBowler = registerPatron(((String) partyNicks.get(i)));
			partyBowlers.add(newBowler);
		}
		Party newParty = new Party(partyBowlers);
		partyQueue.add(newParty);
		publish();
	}

	/**
	 * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
	 *
	 * @return a Vecotr of Strings
	 *
	 */
	public Vector getPartyQueue() {
		Vector displayPartyQueue = new Vector();
		for ( int i=0; i < ( (Vector)partyQueue.asVector()).size(); i++ ) {
			String nextParty =
					((Bowler) ((Vector) ((Party) partyQueue.asVector().get( i ) ).getMembers())
							.get(0))
					.getNickName() + "'s Party";
			displayPartyQueue.addElement(nextParty);
		}
		return displayPartyQueue;
	}

	/**
	 * Accessor for the number of lanes represented by the ControlDesk
	 * 
	 * @return an int containing the number of lanes represented
	 *
	 */
	public int getNumLanes() {
		return numLanes;
	}

	/**
	 * Broadcast an event to subscribing objects.
	 *
	 */
	public void publish() {
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Accessor method for lanes
	 * 
	 * @return a HashSet of Lanes
	 *
	 */
	public HashSet getLanes() {
		return lanes;
	}
}

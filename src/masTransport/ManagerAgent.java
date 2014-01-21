/*package masTransport;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class ManagerAgent extends Agent {
	final int carsNumber = 3;
	public String graphStringMsg;
	
	protected void setup(){
		addBehaviour(new OneShotBehaviour(this){
			public void action(){
				Parse parse = new Parse();
				graphStringMsg = parse.readFromFile();
				
				ACLMessage matrixMsg = new ACLMessage(ACLMessage.INFORM);
				for (int id = 1; id < carsNumber + 1; id++) {
					matrixMsg.addReceiver(new AID("car" + id, AID.ISLOCALNAME));
				}
				matrixMsg.setContent(graphStringMsg);
				send(matrixMsg);
			}
		});
	}
}*/

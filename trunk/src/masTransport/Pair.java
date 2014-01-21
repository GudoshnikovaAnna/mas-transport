package masTransport;

import jade.lang.acl.ACLMessage;

public class Pair {
	int cost;
	ACLMessage messageFromCar;
	
	public Pair(int cost, ACLMessage messageFromCar){
		this.cost = cost;
		this.messageFromCar = messageFromCar;
	}
}

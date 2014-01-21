package masTransport;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.lang.Object;
import java.util.*;

public class WeightAgent extends Agent {
	public int from; // начальная вершина для груза
	public int to; // конечная вершина

	public String myCar; // имя машины, которая повезет груз

	public static final String CARCHOSENMSG = "Your car is chosen";
	private ArrayList<Pair> costCar = new ArrayList<Pair>(); // цена перевозки,
																// которую
																// предложила
																// машина
	final int carsNumber = 3;
	int counter = 0;

	protected void setup() {

		// считали аргументы - начальную и конечную координаты (номера вершин)
		// для груза
		Object[] array = getArguments();

		if (array.length == 1) {
			StringTokenizer tokenizer = new StringTokenizer((String) array[0],
					" ");
			from = Integer.parseInt(tokenizer.nextToken());
			to = Integer.parseInt(tokenizer.nextToken());
		} else if (array.length == 2) {

			from = Integer.parseInt((String) array[0]);
			to = Integer.parseInt((String) array[1]);

		} else {
			System.out.println("Incorrect coordinate format");
		} // закончили считывать аргументы

		System.out.println("Hello, I'm " + getLocalName());

		// отправляем машинам сообщение с координатами груза
		ACLMessage initialMsg = new ACLMessage(ACLMessage.INFORM);
		for (int id = 1; id < carsNumber + 1; id++) {
			initialMsg.addReceiver(new AID("car" + id, AID.ISLOCALNAME));
		}
		initialMsg.setContent(from + "-" + to);
		send(initialMsg);

		System.out.println("ГРУЗ отправил МАШИНЕ координаты");

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// получили сообщение от машины
				ACLMessage message = receive(MessageTemplate
						.MatchPerformative(ACLMessage.INFORM));
				if (message != null) {
					if (message.getContent().equals("")) {
						System.out.println("The message is empty");
					} else {
						// если сообщение не пусто, парсим стоимость перевозки

						int cost;

						cost = Integer.parseInt(message.getContent());
						costCar.add(new Pair(cost, message));// добавили машину
																// и цену от неё
																// в пару, пару
																// - в список
						counter++;

						System.out
								.println("ГРУЗ получил (и распарсил) СТОИМОСТЬ");
					}

					// действуем, когда получены сообщения с ценой от всех машин
					if (counter == carsNumber) {
						arrayListSort(costCar);

						// OTLADKA
						/*
						 * System.out.println("costCar:"); for (int i = 0; i <
						 * costCar.size(); i++){ System.out.print(" " +
						 * costCar.get(i).cost); } System.out.println();
						 */

						// block();

						// отправляем сообщение машине, выставившей наименьшую
						// цену
						ACLMessage reply = costCar.get(0).messageFromCar
								.createReply();
						reply.setContent(CARCHOSENMSG);
						send(reply);

						System.out.println("ГРУЗ выбрал МАШИНУ");

						myCar = costCar.get(0).messageFromCar.getSender()
								.getLocalName();
						System.out.println(getLocalName() + " - " + myCar
								+ " - " + costCar.get(0).cost);
					} else {
						// block();
					}
				} else {
					block();
				}

			}
		});
	}

	void swap(ArrayList<Pair> list, int i, int j) {
		Pair t = list.get(i);
		list.add(i, list.get(j));
		list.add(j, t);
	}

	void arrayListSort(ArrayList<Pair> list) {
		for (int i = list.size() - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (list.get(j).cost > list.get(j + 1).cost)
					swap(list, j, j + 1);
			}
		}
	}
}

package masTransport;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.lang.Object;
import java.util.*;

public class WeightAgent extends Agent {
	public int from; // ��������� ������� ��� �����
	public int to; // �������� �������

	public String myCar; // ��� ������, ������� ������� ����

	public static final String CARCHOSENMSG = "Your car is chosen";
	private ArrayList<Pair> costCar = new ArrayList<Pair>(); // ���� ���������,
																// �������
																// ����������
																// ������
	final int carsNumber = 3;
	int counter = 0;

	protected void setup() {

		// ������� ��������� - ��������� � �������� ���������� (������ ������)
		// ��� �����
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
		} // ��������� ��������� ���������

		System.out.println("Hello, I'm " + getLocalName());

		// ���������� ������� ��������� � ������������ �����
		ACLMessage initialMsg = new ACLMessage(ACLMessage.INFORM);
		for (int id = 1; id < carsNumber + 1; id++) {
			initialMsg.addReceiver(new AID("car" + id, AID.ISLOCALNAME));
		}
		initialMsg.setContent(from + "-" + to);
		send(initialMsg);

		System.out.println("���� �������� ������ ����������");

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// �������� ��������� �� ������
				ACLMessage message = receive(MessageTemplate
						.MatchPerformative(ACLMessage.INFORM));
				if (message != null) {
					if (message.getContent().equals("")) {
						System.out.println("The message is empty");
					} else {
						// ���� ��������� �� �����, ������ ��������� ���������

						int cost;

						cost = Integer.parseInt(message.getContent());
						costCar.add(new Pair(cost, message));// �������� ������
																// � ���� �� ��
																// � ����, ����
																// - � ������
						counter++;

						System.out
								.println("���� ������� (� ���������) ���������");
					}

					// ���������, ����� �������� ��������� � ����� �� ���� �����
					if (counter == carsNumber) {
						arrayListSort(costCar);

						// OTLADKA
						/*
						 * System.out.println("costCar:"); for (int i = 0; i <
						 * costCar.size(); i++){ System.out.print(" " +
						 * costCar.get(i).cost); } System.out.println();
						 */

						// block();

						// ���������� ��������� ������, ����������� ����������
						// ����
						ACLMessage reply = costCar.get(0).messageFromCar
								.createReply();
						reply.setContent(CARCHOSENMSG);
						send(reply);

						System.out.println("���� ������ ������");

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

package masTransport;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.FileNotFoundException;
import java.util.*;

//import masTransport.Dijkstra;

public class CarsAgent extends Agent {
	public static final String mPrefixName = "car";
	public int fromCar; //старт машины
	public int toCar; //куда едет машина
	public int cost;
	private ArrayList<Integer> actualPath = new ArrayList<Integer>(); // путь
																		// машины
	private ArrayList<Integer> tempPath = new ArrayList<Integer>(); // путь
																	// машины с
																	// грузом
	private FindPath fp = new FindPath();

	protected void setup() {
		
		System.out.println("Hello, I'm " + getLocalName());
		
		Object[] array = getArguments();
		
		if (array.length == 2)
		{
			fromCar = Integer.parseInt((String) array[0]);
			toCar = Integer.parseInt((String) array[1]);
		}
		else 
		{
			System.out.println("Incorrect coordinate format");
		}

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				
				//получили сообщение от груза
				ACLMessage message = receive(MessageTemplate
						.MatchPerformative(ACLMessage.INFORM));
				
				Path fromCarToCar = fp.findShortestPath(fromCar, toCar);
				actualPath = fromCarToCar.Item1();
				
				if (message != null) {
					
					//если выбрана эта машина, записываем временный путь в постоянный
					if (message.getContent().equals(WeightAgent.CARCHOSENMSG)) {
						
						System.out.println("МАШИНА повезет ГРУЗ");
						
						System.out.println("Prev actualPath:");
						for (int i = 0; i < actualPath.size(); i++){
							System.out.print(actualPath.get(i) + " -> ");
						}
						
						actualPath = (ArrayList<Integer>) tempPath.clone();
						
						System.out.println("Last actualPath:");
						for (int i = 0; i < actualPath.size(); i++){
							System.out.print(actualPath.get(i) + " -> ");
						}
						
					} else {
						//иначе получаем координаты груза
						
						int[] array = parseMsg(message.getContent());
						
						System.out.println("Получили КООРДИНАТЫ груза (и распарсили)");
						
						int fromWeight = array[0];
						int toWeight = array[1];
						
						Path fromCarFromWeight = fp.findShortestPath(fromCar, fromWeight);
						Path fromWeightToWeight = fp.findShortestPath(fromWeight, toWeight);
						Path toWeightToCar = fp.findShortestPath(toWeight, toCar);
						
						System.out.println("Посчитали пути");
						
						//посчитали все отрезки путей
						ArrayList<Integer> pathFromCarFromWeight = fromCarFromWeight.Item1(); 
						ArrayList<Integer> pathFromWeightToWeight = fromWeightToWeight.Item1();
						ArrayList<Integer> pathToWeightToCar = toWeightToCar.Item1();
						
						int valueFromCarFromWeight = fromCarFromWeight.Item2();
						int valueFromWeightToWeight = fromWeightToWeight.Item2();
						int valueToWeightToCar = toWeightToCar.Item2();
						
						//добавили новые вершины (где груз) в путь машинки
						tempPath.addAll(pathFromCarFromWeight);
						tempPath.remove(tempPath.size() - 1);
						tempPath.addAll(pathFromWeightToWeight);
						tempPath.remove(tempPath.size() - 1);
						tempPath.addAll(pathToWeightToCar);	
						//tempPath.remove(tempPath.size() - 1);
						
						//посчитали стоимость перевозки
						cost = Math.abs(valueFromCarFromWeight + valueFromWeightToWeight
								+ valueToWeightToCar - fromCarToCar.Item2());
						
						System.out.println("Посчитали СТОИМОСТЬ перевозки");
						
						//отправили сообщение о стоимости грузу
						ACLMessage costMsg = message.createReply();
						costMsg.setContent(Integer.toString(cost));
						send(costMsg);
						
						System.out.println("МАШИНА отправила ГРУЗУ стоимость");
					}
				} 
				block();
			}
		});
	}

	private int[] parseMsg(String msg) {
		
		int[] result = new int[2];
		String tempNum = "";
		int from = -1, to = -1;
		
		for (int i = 0; i < msg.length(); i++){
			if (Character.isDigit(msg.charAt(i))){
				tempNum += msg.charAt(i);
			} else if (msg.charAt(i) == '-'){
				from = Integer.parseInt(tempNum);
				tempNum = "";
			} 
		}
		//System.out.println("tempNum = " + tempNum);
		//if (tempNum != ""){
			to = Integer.parseInt(tempNum);
		//}
		
		result[0] = from;
		result[1] = to;
		
		//System.out.println("from = " + from);
		//System.out.println("to = " + to);
		
		return result;
	}
}

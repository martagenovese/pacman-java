package mcv;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        EventManager eventManager = new EventManager();
        Table grafica = new Table();

        grafica.setEventManager(eventManager);
        eventManager.setModel(model);
        eventManager.setTable(grafica);
    }
}

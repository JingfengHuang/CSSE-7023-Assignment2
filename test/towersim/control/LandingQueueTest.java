package towersim.control;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import towersim.aircraft.Aircraft;
import towersim.aircraft.AircraftCharacteristics;
import towersim.aircraft.FreightAircraft;
import towersim.aircraft.PassengerAircraft;
import towersim.tasks.Task;
import towersim.tasks.TaskList;
import towersim.tasks.TaskType;
import java.util.List;

public class LandingQueueTest {
    //Aircraft
    private Aircraft passengerAircraftLanding;
    private Aircraft passengerAircraftLanding2;
    private Aircraft passengerAircraftLandingEmergency;
    private Aircraft passengerAircraftLandingNoFuel;
    private Aircraft passengerAircraftLandingNoFuel2;
    private Aircraft helicopterAircraftLanding;
    private Aircraft helicopterAircraftLanding2;
    private Aircraft helicopterAircraftLandingEmergency;


    //Queue
    private LandingQueue landingQueue;

    @Before
    public void setup() {
        landingQueue = new LandingQueue();

        TaskList taskListLand = new TaskList(List.of(
                new Task(TaskType.LAND),
                new Task(TaskType.WAIT),
                new Task(TaskType.WAIT),
                new Task(TaskType.LOAD, 100),
                new Task(TaskType.TAKEOFF),
                new Task(TaskType.AWAY),
                new Task(TaskType.AWAY)));


        this.passengerAircraftLanding = new PassengerAircraft("LAN001",
                AircraftCharacteristics.AIRBUS_A320,
                taskListLand,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 2, 100);

        this.passengerAircraftLanding2 = new PassengerAircraft("LAN002",
                AircraftCharacteristics.AIRBUS_A320,
                taskListLand,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 2, 100);

        this.passengerAircraftLandingEmergency = new PassengerAircraft("ABC001",
                AircraftCharacteristics.AIRBUS_A320,
                taskListLand,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 2, 0);
        this.passengerAircraftLandingEmergency.declareEmergency();

        this.passengerAircraftLandingNoFuel = new PassengerAircraft("ABC002",
                AircraftCharacteristics.AIRBUS_A320,
                taskListLand,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 5, 0);

        this.passengerAircraftLandingNoFuel2 = new PassengerAircraft("ABC003",
                AircraftCharacteristics.AIRBUS_A320,
                taskListLand,
                0, 0);

        this.helicopterAircraftLanding = new FreightAircraft("LAN003",
                AircraftCharacteristics.SIKORSKY_SKYCRANE,
                taskListLand,
                AircraftCharacteristics.SIKORSKY_SKYCRANE.fuelCapacity / 2, 1000);

        this.helicopterAircraftLanding2 = new FreightAircraft("LAN004",
                AircraftCharacteristics.SIKORSKY_SKYCRANE,
                taskListLand,
                AircraftCharacteristics.SIKORSKY_SKYCRANE.fuelCapacity / 2, 1000);

        this.helicopterAircraftLandingEmergency = new FreightAircraft("LAN005",
                AircraftCharacteristics.SIKORSKY_SKYCRANE,
                taskListLand,
                AircraftCharacteristics.SIKORSKY_SKYCRANE.fuelCapacity / 2, 1000);
        helicopterAircraftLandingEmergency.declareEmergency();
    }

    @Test
    public void initialisationTest() {
        assertEquals(List.of(), landingQueue.getAircraftInOrder());
    }

    @Test
    public void addAircraftTest1() {
        assertEquals(0, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void addAircraftTest2() {
        landingQueue.addAircraft(passengerAircraftLanding);
        assertEquals(1, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void addAircraftTest3() {
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding);
        assertEquals(2, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void addAircraftTest4() {
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding2);
        assertEquals(3, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void addAircraftTest5() {
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        assertEquals(4, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void addAircraftTest6() {
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        landingQueue.addAircraft(passengerAircraftLandingEmergency);
        assertEquals(5, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void peekAircraftTest() {
        assertNull(landingQueue.peekAircraft());
        assertEquals(0, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(helicopterAircraftLanding);
        assertEquals(helicopterAircraftLanding, landingQueue.peekAircraft());
        assertEquals(1, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(passengerAircraftLanding);
        assertEquals(passengerAircraftLanding, landingQueue.peekAircraft());
        assertEquals(2, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(helicopterAircraftLanding2);
        assertEquals(passengerAircraftLanding, landingQueue.peekAircraft());
        assertEquals(3, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        assertEquals(passengerAircraftLandingNoFuel, landingQueue.peekAircraft());
        assertEquals(4, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(passengerAircraftLanding2);
        assertEquals(passengerAircraftLandingNoFuel, landingQueue.peekAircraft());
        assertEquals(5, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(helicopterAircraftLandingEmergency);
        assertEquals(helicopterAircraftLandingEmergency, landingQueue.peekAircraft());
        assertEquals(6, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(passengerAircraftLandingNoFuel2);
        assertEquals(helicopterAircraftLandingEmergency, landingQueue.peekAircraft());
        assertEquals(7, landingQueue.getAircraftInOrder().size());

        landingQueue.addAircraft(passengerAircraftLandingEmergency);
        assertEquals(helicopterAircraftLandingEmergency, landingQueue.peekAircraft());
        assertEquals(8, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraftTest1() {
        assertNull(this.landingQueue.removeAircraft());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraftTest2() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        assertEquals(helicopterAircraftLanding, landingQueue.removeAircraft());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraft3() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        assertEquals(passengerAircraftLanding, landingQueue.removeAircraft());
        assertEquals(1, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraft4() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding2);
        assertEquals(passengerAircraftLanding, landingQueue.removeAircraft());
        assertEquals(2, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraft5() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        assertEquals(passengerAircraftLandingNoFuel, landingQueue.removeAircraft());
        assertEquals(3, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraft6() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        landingQueue.addAircraft(passengerAircraftLanding2);
        assertEquals(passengerAircraftLandingNoFuel, landingQueue.removeAircraft());
        assertEquals(4, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraft7() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        landingQueue.addAircraft(passengerAircraftLanding2);
        landingQueue.addAircraft(helicopterAircraftLandingEmergency);
        assertEquals(helicopterAircraftLandingEmergency, landingQueue.removeAircraft());
        assertEquals(5, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraft8() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        landingQueue.addAircraft(passengerAircraftLanding2);
        landingQueue.addAircraft(helicopterAircraftLandingEmergency);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel2);
        assertEquals(helicopterAircraftLandingEmergency, landingQueue.removeAircraft());
        assertEquals(6, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void removeAircraft9() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        landingQueue.addAircraft(passengerAircraftLanding2);
        landingQueue.addAircraft(helicopterAircraftLandingEmergency);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel2);
        landingQueue.addAircraft(passengerAircraftLandingEmergency);
        assertEquals(helicopterAircraftLandingEmergency, landingQueue.removeAircraft());
        assertEquals(7, landingQueue.getAircraftInOrder().size());
    }

    @Test
    public void getAircraftInOrderTest() {
        landingQueue.addAircraft(helicopterAircraftLanding);
        landingQueue.addAircraft(passengerAircraftLanding);
        landingQueue.addAircraft(helicopterAircraftLanding2);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        landingQueue.addAircraft(passengerAircraftLanding2);
        landingQueue.addAircraft(helicopterAircraftLandingEmergency);
        landingQueue.addAircraft(passengerAircraftLandingNoFuel2);
        landingQueue.addAircraft(passengerAircraftLandingEmergency);

        assertEquals(8, landingQueue.getAircraftInOrder().size());

        List<Aircraft> orderedAircraft = landingQueue.getAircraftInOrder();

        assertEquals(8, landingQueue.getAircraftInOrder().size());

        assertEquals(orderedAircraft.get(0), helicopterAircraftLandingEmergency);
        assertEquals(orderedAircraft.get(1), passengerAircraftLandingEmergency);
        assertEquals(orderedAircraft.get(2), passengerAircraftLandingNoFuel);
        assertEquals(orderedAircraft.get(3), passengerAircraftLandingNoFuel2);
        assertEquals(orderedAircraft.get(4), passengerAircraftLanding);
        assertEquals(orderedAircraft.get(5), passengerAircraftLanding2);
        assertEquals(orderedAircraft.get(6), helicopterAircraftLanding);
        assertEquals(orderedAircraft.get(7), helicopterAircraftLanding2);
    }

    @Test
    public void containsAircraftTest() {
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(helicopterAircraftLandingEmergency);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(passengerAircraftLandingEmergency);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(passengerAircraftLandingNoFuel);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(passengerAircraftLandingNoFuel2);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(passengerAircraftLanding);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLanding));
        assertFalse(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(passengerAircraftLanding2);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLanding));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(helicopterAircraftLanding);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLanding));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertFalse(landingQueue.containsAircraft(helicopterAircraftLanding2));

        landingQueue.addAircraft(helicopterAircraftLanding2);
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingEmergency));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLandingNoFuel2));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLanding));
        assertTrue(landingQueue.containsAircraft(passengerAircraftLanding2));
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLanding));
        assertTrue(landingQueue.containsAircraft(helicopterAircraftLanding2));
    }
}

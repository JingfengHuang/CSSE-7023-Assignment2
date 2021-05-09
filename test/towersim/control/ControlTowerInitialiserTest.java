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
import towersim.util.MalformedSaveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class ControlTowerInitialiserTest {
    private Aircraft takeoffAircraft1;
    private Aircraft takeoffAircraft2;
    private Aircraft takeoffAircraft3;
    private Aircraft landAircraft1;
    private Aircraft landAircraft2;
    private Aircraft landAircraft3;
    private Aircraft loadingAircraft1;
    private Aircraft loadingAircraft2;
    private Aircraft loadingAircraft3;

    private List<Aircraft> allAircraft;

    @Before
    public void setup() {
        TaskList taskListTakeoff = new TaskList(List.of(
                new Task(TaskType.TAKEOFF),
                new Task(TaskType.AWAY),
                new Task(TaskType.LAND),
                new Task(TaskType.WAIT),
                new Task(TaskType.WAIT),
                new Task(TaskType.LOAD, 100),
                new Task(TaskType.TAKEOFF),
                new Task(TaskType.AWAY),
                new Task(TaskType.AWAY),
                new Task(TaskType.LAND),
                new Task(TaskType.LOAD)));

        TaskList taskListLand = new TaskList(List.of(
                new Task(TaskType.LAND),
                new Task(TaskType.WAIT),
                new Task(TaskType.WAIT),
                new Task(TaskType.LOAD, 100),
                new Task(TaskType.TAKEOFF),
                new Task(TaskType.AWAY),
                new Task(TaskType.AWAY)));

        TaskList taskListLoad = new TaskList(List.of(
                new Task(TaskType.LOAD, 100),
                new Task(TaskType.TAKEOFF),
                new Task(TaskType.AWAY),
                new Task(TaskType.AWAY),
                new Task(TaskType.LAND),
                new Task(TaskType.WAIT),
                new Task(TaskType.WAIT),
                new Task(TaskType.LOAD, 100),
                new Task(TaskType.TAKEOFF),
                new Task(TaskType.AWAY),
                new Task(TaskType.AWAY),
                new Task(TaskType.LAND)));


        this.takeoffAircraft1 = new PassengerAircraft("TK001",
                AircraftCharacteristics.AIRBUS_A320,
                taskListTakeoff,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 2, 100);

        this.takeoffAircraft2 = new PassengerAircraft("TK002",
                AircraftCharacteristics.AIRBUS_A320,
                taskListTakeoff,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 2, 100);

        this.takeoffAircraft3 = new PassengerAircraft("TK003",
                AircraftCharacteristics.AIRBUS_A320,
                taskListTakeoff,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 2, 0);

        this.landAircraft1 = new PassengerAircraft("LAN001",
                AircraftCharacteristics.AIRBUS_A320,
                taskListLand,
                AircraftCharacteristics.AIRBUS_A320.fuelCapacity / 5, 0);

        this.landAircraft2 = new PassengerAircraft("LAN002",
                AircraftCharacteristics.AIRBUS_A320,
                taskListLand,
                0, 0);

        this.landAircraft3 = new FreightAircraft("LAN003",
                AircraftCharacteristics.SIKORSKY_SKYCRANE,
                taskListLand,
                AircraftCharacteristics.SIKORSKY_SKYCRANE.fuelCapacity / 2, 1000);

        this.loadingAircraft1 = new FreightAircraft("LOD001",
                AircraftCharacteristics.SIKORSKY_SKYCRANE,
                taskListLoad,
                AircraftCharacteristics.SIKORSKY_SKYCRANE.fuelCapacity / 2, 1000);

        this.loadingAircraft2 = new FreightAircraft("LOD002",
                AircraftCharacteristics.SIKORSKY_SKYCRANE,
                taskListLoad,
                AircraftCharacteristics.SIKORSKY_SKYCRANE.fuelCapacity / 2, 1000);

        this.loadingAircraft3 = new FreightAircraft("LOD003",
                AircraftCharacteristics.SIKORSKY_SKYCRANE,
                taskListLoad,
                AircraftCharacteristics.SIKORSKY_SKYCRANE.fuelCapacity / 2, 1000);

        allAircraft = new ArrayList<Aircraft>();
        allAircraft.add(takeoffAircraft1);
        allAircraft.add(takeoffAircraft2);
        allAircraft.add(takeoffAircraft3);
        allAircraft.add(landAircraft1);
        allAircraft.add(landAircraft2);
        allAircraft.add(landAircraft3);
        allAircraft.add(loadingAircraft1);
        allAircraft.add(loadingAircraft2);
        allAircraft.add(loadingAircraft3);
    }

    @Test
    public void loadTickNormalTest() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"1");
        try {
            ControlTowerInitialiser.loadTick(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(1, ControlTowerInitialiser.loadTick(new StringReader(fileContents)));
    }

    @Test
    public void loadTickNormalTest2() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"100");
        try {
            ControlTowerInitialiser.loadTick(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(100, ControlTowerInitialiser.loadTick(new StringReader(fileContents)));
    }

    @Test
    public void loadTickNormalTest3() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"0");
        try {
            ControlTowerInitialiser.loadTick(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, ControlTowerInitialiser.loadTick(new StringReader(fileContents)));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTickFailTest() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"this is not integer");
        ControlTowerInitialiser.loadTick(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTickFailTest2() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"@#$%^");
        ControlTowerInitialiser.loadTick(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTickFailTest3() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"1.1");
        ControlTowerInitialiser.loadTick(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTickFailTest4() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"-1");
        ControlTowerInitialiser.loadTick(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTickFailTest5() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(),"-1.1");
        ControlTowerInitialiser.loadTick(new StringReader(fileContents));
    }

    @Test
    public void loadAircraftNormalTest() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "0");
        try {
            ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, ControlTowerInitialiser.loadAircraft(new StringReader(fileContents)).size());
    }

    @Test
    public void loadAircraftNormalTest2() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "1",
                "QFA481:AIRBUS_A320:AWAY,AWAY,LAND,WAIT,WAIT,LOAD@60,TAKEOFF,AWAY:10000.00:false:132");
        try {
            ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(1, ControlTowerInitialiser.loadAircraft(new StringReader(fileContents)).size());
    }

    @Test
    public void loadAircraftNormalTest3() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "1",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0");
        try {
            ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(1, ControlTowerInitialiser.loadAircraft(new StringReader(fileContents)).size());
    }

    @Test
    public void loadAircraftNormalTest4() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "2",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4");
        try {
            ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(2, ControlTowerInitialiser.loadAircraft(new StringReader(fileContents)).size());
    }

    @Test
    public void loadAircraftNormalTest5() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "3",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4",
                "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:10000.00:false:0");
        try {
            ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(3, ControlTowerInitialiser.loadAircraft(new StringReader(fileContents)).size());
    }

    @Test(expected = MalformedSaveException.class)
    public void loadAircraftFailTest() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "abc",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4",
                "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:10000.00:false:0");
        ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadAircraftFailTest2() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "@#$%%^",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4",
                "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:10000.00:false:0");
        ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadAircraftFailTes3() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "3.4",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4",
                "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:10000.00:false:0");
        ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadAircraftFailTest4() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "1",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4",
                "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:10000.00:false:0");
        ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadAircraftFailTest5() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "6",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4",
                "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:10000.00:false:0");
        ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
    }

    @Test(expected = MalformedSaveException.class)
    public void loadAircraftFailTest6() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "-1",
                "UPS119:BOEING_747_8F:WAIT,LOAD@50,TAKEOFF,AWAY,AWAY,AWAY,LAND:4000.00:true:0",
                "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:false:4",
                "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:10000.00:false:0");
        ControlTowerInitialiser.loadAircraft(new StringReader(fileContents));
    }

    @Test
    public void loadQueuesNormalTest1() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:0",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest2() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:1", "TK003",
                "LandingQueue:0",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(1, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest3() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:2", "TK001,TK002",
                "LandingQueue:0",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(2, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest4() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:0",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(3, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest5() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:1", "LAN003",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(1, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest6() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:2", "LAN002,LAN003",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(2, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest7() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(3, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest8() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:0",
                "LoadingAircraft:1", "LOD001:2");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(1, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest9() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:0",
                "LoadingAircraft:2", "LOD001:2,LOD003:3");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(2, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest10() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:0",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(3, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest11() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:0");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(3, takeoffQueue.getAircraftInOrder().size());
        assertEquals(3, landingQueue.getAircraftInOrder().size());
        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest12() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:0",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(3, takeoffQueue.getAircraftInOrder().size());
        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(3, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTest13() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
        assertEquals(3, landingQueue.getAircraftInOrder().size());
        assertEquals(3, loadingAircraft.size());
    }

    @Test
    public void loadQueuesNormalTestAll() {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        try {
            ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(3, takeoffQueue.getAircraftInOrder().size());
        assertEquals(3, landingQueue.getAircraftInOrder().size());
        assertEquals(3, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest1() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:abc", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                    allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest2() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:#$", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest3() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:ajsdija", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest4() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3.2", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest5() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3.1", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest6() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3.7", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest7() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:-3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest8() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:-5", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest9() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:-3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest10() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:0", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTes11() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:0", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest12() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:0", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest13() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TaeOffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest14() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQu3eue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    /*@Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest15() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadRingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }*/

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest16() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "Takeoff:Queue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest162() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest17() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "Landing:Queue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest172() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest18() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "Loading:Aircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest182() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest19() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest20() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:2", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest21() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest22() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:1", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest23() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest24() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:1", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest25() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK000,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest26() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN005,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest27() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD023:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest28() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001:TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002:LAN003",
                "LoadingAircraft:3", "LOD001:2:LOD002:1:LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest29() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:0,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest30() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:0");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest31() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:-2,LOD002:-1,LOD003:-3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest32() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:a,LOD002:1,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest33() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1.3,LOD003:3");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadQueuesFailTest34() throws MalformedSaveException, IOException {
        TakeoffQueue takeoffQueue = new TakeoffQueue();
        LandingQueue landingQueue = new LandingQueue();
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        String fileContents = String.join(System.lineSeparator(),
                "TakeoffQueue:3", "TK001,TK002,TK003",
                "LandingQueue:3", "LAN001,LAN002,LAN003",
                "LoadingAircraft:3", "LOD001:2,LOD002:1,LOD003:");

        ControlTowerInitialiser.loadQueues(new StringReader(fileContents),
                allAircraft, takeoffQueue, landingQueue, loadingAircraft);
    }

    @Test
    public void loadTerminalWithGatesNormalTest() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "0");
        try {
            ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(0, ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft).size());
    }

    @Test
    public void loadTerminalWithGatesNormalTest2() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "5",
                "AirplaneTerminal:1:false:6", "1:LOD001",
                "2:empty", "3:empty", "4:empty", "5:empty", "6:empty",
                "HelicopterTerminal:2:false:5", "7:empty", "8:empty", "9:empty", "10:empty",
                "11:empty", "AirplaneTerminal:3:false:2", "12:empty", "13:LOD002",
                "HelicopterTerminal:4:true:0", "HelicopterTerminal:5:false:0");
        try {
            ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }
        assertEquals(5, ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft).size());
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTerminalWithGatesFailTest1() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "a",
                "AirplaneTerminal:1:false:6", "1:LOD001",
                "2:empty", "3:empty", "4:empty", "5:empty", "6:empty",
                "HelicopterTerminal:2:false:5", "7:empty", "8:empty", "9:empty", "10:empty",
                "11:empty", "AirplaneTerminal:3:false:2", "12:empty", "13:LOD002",
                "HelicopterTerminal:4:true:0", "HelicopterTerminal:5:false:0");
        ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTerminalWithGatesFailTest2() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "#$%",
                "AirplaneTerminal:1:false:6", "1:LOD001",
                "2:empty", "3:empty", "4:empty", "5:empty", "6:empty",
                "HelicopterTerminal:2:false:5", "7:empty", "8:empty", "9:empty", "10:empty",
                "11:empty", "AirplaneTerminal:3:false:2", "12:empty", "13:LOD002",
                "HelicopterTerminal:4:true:0", "HelicopterTerminal:5:false:0");
        ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTerminalWithGatesFailTest3() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "5.4",
                "AirplaneTerminal:1:false:6", "1:LOD001",
                "2:empty", "3:empty", "4:empty", "5:empty", "6:empty",
                "HelicopterTerminal:2:false:5", "7:empty", "8:empty", "9:empty", "10:empty",
                "11:empty", "AirplaneTerminal:3:false:2", "12:empty", "13:LOD002",
                "HelicopterTerminal:4:true:0", "HelicopterTerminal:5:false:0");
        ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTerminalWithGatesFailTest4() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "7",
                "AirplaneTerminal:1:false:6", "1:LOD001",
                "2:empty", "3:empty", "4:empty", "5:empty", "6:empty",
                "HelicopterTerminal:2:false:5", "7:empty", "8:empty", "9:empty", "10:empty",
                "11:empty", "AirplaneTerminal:3:false:2", "12:empty", "13:LOD002",
                "HelicopterTerminal:4:true:0", "HelicopterTerminal:5:false:0");
        ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTerminalWithGatesFailTest5() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "2",
                "AirplaneTerminal:1:false:6", "1:LOD001",
                "2:empty", "3:empty", "4:empty", "5:empty", "6:empty",
                "HelicopterTerminal:2:false:5", "7:empty", "8:empty", "9:empty", "10:empty",
                "11:empty", "AirplaneTerminal:3:false:2", "12:empty", "13:LOD002",
                "HelicopterTerminal:4:true:0", "HelicopterTerminal:5:false:0");
        ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
    }

    @Test(expected = MalformedSaveException.class)
    public void loadTerminalWithGatesFailTest6() throws MalformedSaveException, IOException {
        String fileContents = String.join(System.lineSeparator(), "0",
                "AirplaneTerminal:1:false:6", "1:LOD001",
                "2:empty", "3:empty", "4:empty", "5:empty", "6:empty",
                "HelicopterTerminal:2:false:5", "7:empty", "8:empty", "9:empty", "10:empty",
                "11:empty", "AirplaneTerminal:3:false:2", "12:empty", "13:LOD002",
                "HelicopterTerminal:4:true:0", "HelicopterTerminal:5:false:0");
        ControlTowerInitialiser.loadTerminalsWithGates(new StringReader(fileContents), allAircraft);
    }

    @Test
    public void readAircraftNormalTest() throws MalformedSaveException {
        String line = "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:126206.00:false:0";
        try {
            ControlTowerInitialiser.readAircraft(line);
        } catch (MalformedSaveException e) {
            fail();
        }

        Aircraft aircraft = ControlTowerInitialiser.readAircraft(line);
        assertEquals("UTD302", aircraft.getCallsign());
        assertEquals("BOEING_787", aircraft.getCharacteristics().name());
        assertEquals(100, aircraft.getFuelPercentRemaining());
        assertEquals(TaskType.WAIT, aircraft.getTaskList().getCurrentTask().getType());
        assertEquals(TaskType.LOAD, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.TAKEOFF, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.AWAY, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.AWAY, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.AWAY, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.LAND, aircraft.getTaskList().getNextTask().getType());
        assertFalse(aircraft.hasEmergency());
        assertEquals(0, aircraft.calculateOccupancyLevel());
    }

    @Test
    public void readAircraftNormalTest2() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:true:4";
        try {
            ControlTowerInitialiser.readAircraft(line);
        } catch (MalformedSaveException e) {
            fail();
        }

        Aircraft aircraft = ControlTowerInitialiser.readAircraft(line);
        assertEquals("VH-BFK", aircraft.getCallsign());
        assertEquals("ROBINSON_R44", aircraft.getCharacteristics().name());
        assertEquals(21, aircraft.getFuelPercentRemaining());
        assertEquals(TaskType.LAND, aircraft.getTaskList().getCurrentTask().getType());
        assertEquals(TaskType.WAIT, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.LOAD, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.TAKEOFF, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.AWAY, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(TaskType.AWAY, aircraft.getTaskList().getNextTask().getType());
        aircraft.getTaskList().moveToNextTask();
        assertEquals(100, aircraft.calculateOccupancyLevel());
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest1() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY40.00:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest2() throws MalformedSaveException {
        String line = "VH-BFK:ROBIN:SON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest3() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R43:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest4() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44_ABC:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest5() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:a:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest6() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:$%^:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest7() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:-1:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest8() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:191.00:true:4";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest9() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:true:4.7";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest10() throws MalformedSaveException {
        String line = "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:126206.00:false:200.1";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest11() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:true:5";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest12() throws MalformedSaveException {
        String line = "VH-BFK:ROBINSON_R44:LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY:40.00:true:-1";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest13() throws MalformedSaveException {
        String line = "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:126206.00:false:243";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readAircraftFailTest14() throws MalformedSaveException {
        String line = "UTD302:BOEING_787:WAIT,LOAD@100,TAKEOFF,AWAY,AWAY,AWAY,LAND:126206.00:false:-2";
        ControlTowerInitialiser.readAircraft(line);
    }

    @Test
    public void readTaskListNormalTest() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,AWAY,LAND";
        try {
            TaskList taskList = ControlTowerInitialiser.readTaskList(line);
        } catch (MalformedSaveException e) {
            fail();
        }
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
        assertEquals(TaskType.WAIT, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        assertEquals(TaskType.LOAD, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        taskList.moveToNextTask();
        assertEquals(TaskType.AWAY, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        taskList.moveToNextTask();
        assertEquals(TaskType.LAND, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        assertEquals(TaskType.WAIT, taskList.getCurrentTask().getType());
    }

    @Test
    public void readTaskListNormalTest2() throws MalformedSaveException {
        String line = "AWAY,LAND,WAIT,LOAD@75,TAKEOFF,AWAY,AWAY,LAND";
        try {
            TaskList taskList = ControlTowerInitialiser.readTaskList(line);
        } catch (MalformedSaveException e) {
            fail();
        }
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
        assertEquals(TaskType.AWAY, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        assertEquals(TaskType.LAND, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        assertEquals(TaskType.WAIT, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        assertEquals(TaskType.LOAD, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        taskList.moveToNextTask();
        assertEquals(TaskType.AWAY, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        taskList.moveToNextTask();
        assertEquals(TaskType.LAND, taskList.getCurrentTask().getType());
        taskList.moveToNextTask();
        assertEquals(TaskType.AWAY, taskList.getCurrentTask().getType());
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest1() throws MalformedSaveException {
        String line = "WANT,LOAD@75,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest2() throws MalformedSaveException {
        String line = "WAIT,LAND@75,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest3() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWM,AWM,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest4() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,AWAY,LAUGH";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest5() throws MalformedSaveException {
        String line = "WAIT,LOAD@CC,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest6() throws MalformedSaveException {
        String line = "WAIT,LOAD@,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest7() throws MalformedSaveException {
        String line = "WAIT,LOAD@!#,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest8() throws MalformedSaveException {
        String line = "WAIT,LOAD@75.3,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest9() throws MalformedSaveException {
        String line = "WAIT,LOAD@75pc,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest10() throws MalformedSaveException {
        String line = "WAIT,LOAD@-1,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest11() throws MalformedSaveException {
        String line = "WAIT,LOAD@-0.1,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest111() throws MalformedSaveException {
        String line = "WAIT,LOAD@1@2,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest12() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKE@OFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest13() throws MalformedSaveException {
        String line = "WA@IT,LOAD@75,TAKE@OFF,AW@AY,AW@AY,LA@ND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest14() throws MalformedSaveException {
        String line = "WAIT,LAND,LOAD@75,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest15() throws MalformedSaveException {
        String line = "WAIT,AWAY,LOAD@75,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest16() throws MalformedSaveException {
        String line = "WAIT,TAKEOFF,LOAD@75,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest17() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,WAIT,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest18() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,AWAY,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest19() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,LAND,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest191() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,LOAD@80,TAKEOFF,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest20() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,WAIT,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest21() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,LOAD@80,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest22() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,LAND,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest221() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,TAKEOFF,LAND,AWAY,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest23() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,WAIT,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest24() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,LOAD@20,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest25() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,TAKEOFF,AWAY,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest26() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,AWAY,LAND,LAND";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest27() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,AWAY,LAND,TAKEOFF";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test(expected = MalformedSaveException.class)
    public void readTaskListFailTest28() throws MalformedSaveException {
        String line = "WAIT,LOAD@75,TAKEOFF,AWAY,AWAY,LAND,AWAY";
        TaskList taskList = ControlTowerInitialiser.readTaskList(line);
    }

    @Test
    public void readQueueNormalTest() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
    }

    @Test
    public void readQueueNormalTest2() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:1", "LAN001");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(1, landingQueue.getAircraftInOrder().size());
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
    }

    @Test
    public void readQueueNormalTest3() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:2", "LAN001,LAN002");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(2, landingQueue.getAircraftInOrder().size());
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
    }

    @Test
    public void readQueueNormalTest4() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:3", "LAN001,LAN002,LAN003");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(3, landingQueue.getAircraftInOrder().size());
        assertEquals(0, takeoffQueue.getAircraftInOrder().size());
    }

    @Test
    public void readQueueNormalTest5() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:1", "TK001");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(1, takeoffQueue.getAircraftInOrder().size());
    }

    @Test
    public void readQueueNormalTest6() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:2", "TK001,TK002");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(2, takeoffQueue.getAircraftInOrder().size());
    }

    @Test
    public void readQueueNormalTest7() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:3","TK001,TK002,TK003");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(0, landingQueue.getAircraftInOrder().size());
        assertEquals(3, takeoffQueue.getAircraftInOrder().size());
    }

    @Test
    public void readQueueNormalTest8() {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:3","LAN001,LAN002,LAN003");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:3","TK001,TK002,TK003");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        try {
            ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        try {
            ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(3, landingQueue.getAircraftInOrder().size());
        assertEquals(3, takeoffQueue.getAircraftInOrder().size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest9() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LaningQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest10() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "landingqueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "takeoffqueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest11() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LANDINGQUEUE:0");
        String takeoffContent = String.join(System.lineSeparator(), "TAKEOFFQUEUE:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest12() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandinQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest13() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TaeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest14() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), ":0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest15() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), ":0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest16() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest17() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest18() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "Landing:Queue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest19() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "Takeoff:Queue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest20() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest21() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest22() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest23() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest24() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:a");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest25() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:b");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest26() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0.3");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest27() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:4.9");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest28() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:1");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest29() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:39");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest30() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:-3", "LAN001");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest31() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:-1", "TK001");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest32() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:2", "TK001");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest33() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:2", "LAN002");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest34() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:2");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest35() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:1");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest36() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:2", "LAN001,LNA002");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest37() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:2", "TK01,TK002");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest38() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:2", "LAN001, LAN002");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:0");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test(expected = MalformedSaveException.class)
    public void readQueueNormalTest39() throws MalformedSaveException, IOException {
        String landContent = String.join(System.lineSeparator(), "LandingQueue:0");
        String takeoffContent = String.join(System.lineSeparator(), "TakeoffQueue:2", "TK001, TK002");
        StringReader landQueueStringReader = new StringReader(landContent);
        BufferedReader landReader = new BufferedReader(landQueueStringReader);
        StringReader takeoffQueueStringReader = new StringReader(takeoffContent);
        BufferedReader takeoffReader = new BufferedReader(takeoffQueueStringReader);

        LandingQueue landingQueue = new LandingQueue();
        TakeoffQueue takeoffQueue = new TakeoffQueue();

        ControlTowerInitialiser.readQueue(landReader, allAircraft, landingQueue);
        ControlTowerInitialiser.readQueue(takeoffReader, allAircraft, takeoffQueue);
    }

    @Test
    public void readLoadingAircraftNormalTest() {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:0");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        try {
            ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(0, loadingAircraft.size());
    }

    @Test
    public void readLoadingAircraftNormalTest2() {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:1", "LOD001:1");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        try {
            ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(1, loadingAircraft.size());
    }

    @Test
    public void readLoadingAircraftNormalTest3() {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:1,LOD002:3");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        try {
            ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(2, loadingAircraft.size());
    }

    @Test
    public void readLoadingAircraftNormalTest4() {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:3", "LOD001:1,LOD002:2,LOD003:5");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        try {
            ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);
        } catch (MalformedSaveException | IOException e) {
            fail();
        }

        assertEquals(3, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest1() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(3, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest2() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"", "LoadingAircraft:0");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest3() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft0");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest4() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"Loading:Aircraft:0");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest5() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft::0");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest6() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:0:");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest7() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),":LoadingAircraft:0");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest8() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:a");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest9() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:0.4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest10() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:_+{");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest11() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:1");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest12() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest13() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:0", "LOD001:2");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest14() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:1", "LOD001:2,LOD002:3");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest15() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:2");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest16() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:5", "LOD001:2,LOD002:3");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest17() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:1", ":LOD001:2");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest18() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:1", "LOD:001:2");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest19() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:1", "LOD001::2");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest20() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:1", "LOD001:2:");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest21() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD01:2,LOD002:4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest22() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:2,LOD00O2:4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest23() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "2:LOAD001,LOD002:4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest24() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:a,LOD002:4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest25() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:2,LOD002:b");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest26() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:2.2,LOD002:4.7");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest27() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:-2,LOD002:4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest28() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:2,LOD002:-4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest29() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:0,LOD002:4");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }

    @Test(expected = MalformedSaveException.class)
    public void readLoadingAircraftFailTest30() throws MalformedSaveException, IOException {
        String fileContent = String.join(System.lineSeparator(),"LoadingAircraft:2", "LOD001:2,LOD002:0");
        StringReader stringReader = new StringReader(fileContent);
        BufferedReader reader = new BufferedReader(stringReader);
        Map<Aircraft, Integer> loadingAircraft = new TreeMap<>(Comparator.comparing(Aircraft::getCallsign));

        ControlTowerInitialiser.readLoadingAircraft(reader, allAircraft, loadingAircraft);

        assertEquals(0, loadingAircraft.size());
    }
}

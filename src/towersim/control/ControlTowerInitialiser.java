package towersim.control;

import towersim.aircraft.Aircraft;
import towersim.aircraft.AircraftCharacteristics;
import towersim.aircraft.FreightAircraft;
import towersim.aircraft.PassengerAircraft;
import towersim.ground.AirplaneTerminal;
import towersim.ground.Gate;
import towersim.ground.HelicopterTerminal;
import towersim.ground.Terminal;
import towersim.tasks.Task;
import towersim.tasks.TaskList;
import towersim.tasks.TaskType;
import towersim.util.MalformedSaveException;
import towersim.util.NoSpaceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class that contains static methods for loading a control tower
 * and associated entities from files.
 */
public class ControlTowerInitialiser {
    /**
     * Loads the number of ticks elapsed from the given reader instance.
     *
     * The contents of the reader should match the format specified in the
     * tickWriter row of in the table shown in ViewModel.saveAs().
     *
     * The contents read from the reader are invalid if any of the following conditions are true:
     * The number of ticks elapsed is not an integer (i.e. cannot be parsed by Long.parseLong(String)).
     * Or: The number of ticks elapsed is less than zero.
     *
     * @param reader - reader from which to load the number of ticks elapsed
     * @return number of ticks elapsed
     * @throws MalformedSaveException - if the format of the text read from
     * the reader is invalid according to the rules above
     * @throws IOException - if an IOException is encountered when reading from the reader
     */
    public static long loadTick(Reader reader) throws MalformedSaveException, IOException {
        long tick;
        try {
            BufferedReader bufferedreader = new BufferedReader(reader);
            String line = bufferedreader.readLine();
            try {
                tick = Long.parseLong(line);
                if (tick < 0) {
                    throw new MalformedSaveException();
                }
            } catch (NumberFormatException nfe) {
                throw new MalformedSaveException();
            }
        } catch (IOException ioe) {
            throw new IOException();
        }

        return tick;
    }

    /**
     * Loads the list of all aircraft managed by the control tower from the given reader instance.
     *
     * The contents of the reader should match the format specified in the aircraftWriter
     * row of in the table shown in ViewModel.saveAs().
     *
     * The contents read from the reader are invalid if any of the following conditions are true:
     *
     * 1. The number of aircraft specified on the first line of the reader is not an integer
     * (i.e. cannot be parsed by Integer.parseInt(String)).
     *
     * 2. The number of aircraft specified on the first line is not equal to the number
     * of aircraft actually read from the reader.
     *
     * 3. Any of the conditions listed in the Javadoc for readAircraft(String) are true.
     *
     * @param reader - reader from which to load the list of aircraft
     * @return list of aircraft read from the reader
     * @throws MalformedSaveException - if the format of the text read from the reader
     * is invalid according to the rules above
     * @throws IOException - if an IOException is encountered when reading from the reader
     */
    public static List<Aircraft> loadAircraft(Reader reader) throws IOException, MalformedSaveException {
        List<Aircraft> aircraftList = new ArrayList<Aircraft>();
        BufferedReader bufferedReader = new BufferedReader(reader);
        int aircraftCounter = 0;
        int aircraftNum;

        //First line
        String line = bufferedReader.readLine();
        if (line == null) {
            throw new MalformedSaveException();
        }

        try {
            aircraftNum = Integer.parseInt(line);
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        //Read encoded Aircraft
        for (int i = 0; i < aircraftNum; i++) {
            line = bufferedReader.readLine();

            aircraftList.add(readAircraft(line));

            aircraftCounter ++;
        }

        if (aircraftCounter != aircraftNum) {
            throw new MalformedSaveException();
        }

        return aircraftList;
    }


    /**
     * Reads an aircraft from its encoded representation in the given string.
     *
     * If the AircraftCharacteristics.passengerCapacity of the encoded aircraft is
     * greater than zero, then a PassengerAircraft should be created and returned.
     * Otherwise, a FreightAircraft should be created and returned.
     *
     * The format of the string should match the encoded representation of an aircraft,
     * as described in Aircraft.encode().
     *
     * The encoded string is invalid if any of the following conditions are true:
     * More/fewer colons (:) are detected in the string than expected.
     * The aircraft's AircraftCharacteristics is not valid.
     * The aircraft's fuel amount is not a double
     * The aircraft's fuel amount is less than zero or greater than the aircraft's maximum fuel capacity.
     * The amount of cargo (freight/passengers) onboard the aircraft is not an integer
     * The amount of cargo (freight/passengers) onboard the aircraft is less than
     * zero or greater than the aircraft's maximum freight/passenger capacity.
     * Any of the conditions listed in the Javadoc for readTaskList(String) are true.
     *
     * @param line - line of text containing the encoded aircraft
     * @return decoded aircraft instance
     * @throws MalformedSaveException - if the format of the given string is invalid
     * according to the rules above
     */
    public static Aircraft readAircraft(String line) throws MalformedSaveException {
        Aircraft aircraft;
        String[] decodedAircraft = line.split(":");

        //Check the number of ":"
        if (decodedAircraft.length != 6) {
            throw new MalformedSaveException();
        }

        String callsign = decodedAircraft[0];
        String model = decodedAircraft[1];
        String tasks = decodedAircraft[2];
        String fuelNumber = decodedAircraft[3];
        String emergency = decodedAircraft[4];
        String cargoNumber = decodedAircraft[5];


        AircraftCharacteristics aircraftCharacteristics = null;
        TaskList taskList;
        double fuelAmount;
        boolean emergencyStatus;
        int cargoAmount;

        //Check if AircraftCharacteristics is valid or not
        for (AircraftCharacteristics aircraftCharacteristic : AircraftCharacteristics.values()) {
            if (aircraftCharacteristic.toString().equals(model)) {
                aircraftCharacteristics = aircraftCharacteristic;
            }
        }
        if (aircraftCharacteristics == null) {
            throw new MalformedSaveException();
        }

        //Check if the fuel amount is valid or not
        try {
            fuelAmount = Double.parseDouble(fuelNumber);
            if (fuelAmount < 0 || fuelAmount > aircraftCharacteristics.fuelCapacity) {
                throw new MalformedSaveException();
            }
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        taskList = readTaskList(tasks);

        //Check if the cargo amount is valid or not
        try {
            cargoAmount = Integer.parseInt(cargoNumber);
            if (cargoAmount < 0) {
                throw new MalformedSaveException();
            } else if (aircraftCharacteristics.passengerCapacity <= 0) {
                if (cargoAmount > aircraftCharacteristics.freightCapacity) {
                    throw new MalformedSaveException();
                }
            } else {
                if (cargoAmount > aircraftCharacteristics.passengerCapacity) {
                    throw new MalformedSaveException();
                }
            }
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        //Create aircraft based on their characteristics
        if (aircraftCharacteristics.passengerCapacity <= 0) {
            aircraft = new FreightAircraft(callsign, aircraftCharacteristics, taskList, fuelAmount, cargoAmount);
        } else {
            aircraft = new PassengerAircraft(callsign, aircraftCharacteristics, taskList, fuelAmount, cargoAmount);
        }

        //Declare emergency if the aircraft is in emergency
        if (emergency.equals("true")) {
            aircraft.declareEmergency();
        } else if (emergency.equals("false")) {
            // ignore
        } else {
            throw new MalformedSaveException();
        }

        return aircraft;
    }

    /**
     * Loads the takeoff queue, landing queue and map of loading aircraft
     * from the given reader instance.
     *
     * Rather than returning a list of queues, this method does not return anything.
     * Instead, it should modify the given takeoff queue, landing queue
     * and loading map by adding aircraft, etc.
     *
     * The contents read from the reader are invalid if any of the conditions listed in
     * the Javadoc for readQueue(BufferedReader, List, AircraftQueue) and
     * readLoadingAircraft(BufferedReader, List, Map) are true.
     *
     * @param reader - reader from which to load the queues and loading map
     * @param aircraft - list of all aircraft, used when validating that callsigns exist
     * @param takeoffQueue - empty takeoff queue that aircraft will be added to
     * @param landingQueue - empty landing queue that aircraft will be added to
     * @param loadingAircraft - empty map that aircraft and loading times will be added to
     * @throws MalformedSaveException - if the format of the text read from the reader
     * is invalid according to the rules above
     * @throws IOException - if an IOException is encountered when reading from the reader
     */
    public static void loadQueues(Reader reader, List<Aircraft> aircraft,
                                  TakeoffQueue takeoffQueue, LandingQueue landingQueue,
                                  Map<Aircraft,Integer> loadingAircraft)
            throws MalformedSaveException, IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        readQueue(bufferedReader, aircraft, takeoffQueue);
        readQueue(bufferedReader, aircraft, landingQueue);
        readLoadingAircraft(bufferedReader, aircraft, loadingAircraft);
    }

    /**
     * Loads the list of terminals and their gates from the given reader instance.
     *
     * The contents of the reader should match the format specified in the
     * terminalsWithGatesWriter row of in the table shown in ViewModel.saveAs().
     *
     * The contents read from the reader are invalid if any of the following conditions are true:
     * 1. The number of terminals specified at the top of the file is not an integer
     *
     * 2. The number of terminals specified is not equal to the number of
     * terminals actually read from the reader.
     *
     * 3. Any of the conditions listed in the Javadoc for
     * readTerminal(String, BufferedReader, List) and readGate(String, List) are true.
     * @param reader - reader from which to load the list of terminals and their gates
     * @param aircraft - list of all aircraft, used when validating that callsigns exist
     * @return list of terminals (with their gates) read from the reader
     * @throws MalformedSaveException - if the format of the text read from the
     * reader is invalid according to the rules above
     * @throws IOException - if an IOException is encountered when reading from the reader
     */
    public static List<Terminal> loadTerminalsWithGates(Reader reader,
                                                        List<Aircraft> aircraft)
            throws MalformedSaveException, IOException {
        List<Terminal> terminals = new ArrayList<Terminal>();
        int terminalNumber;

        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();

        try {
            terminalNumber = Integer.parseInt(line);
        } catch(NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        for (int i = 0; i < terminalNumber; i++) {
            line = bufferedReader.readLine();
            terminals.add(readTerminal(line, bufferedReader, aircraft));
        }

        return terminals;
    }

    /**
     * Creates a control tower instance by reading various airport entities from the given readers.
     *
     * @param tick - reader from which to load the number of ticks elapsed
     * @param aircraft - reader from which to load the list of aircraft
     * @param queues - reader from which to load the aircraft queues and map of loading aircraft
     * @param terminalsWithGates - reader from which to load the terminals and their gates
     * @return control tower created by reading from the given readers
     * @throws MalformedSaveException - if reading from any of the given readers results
     * in a MalformedSaveException, indicating the contents of that reader are invalid
     * @throws IOException - if an IOException is encountered when reading from any of the readers
     */
    public static ControlTower createControlTower(Reader tick, Reader aircraft,
                                                  Reader queues, Reader terminalsWithGates)
            throws MalformedSaveException, IOException {

    }

    /**
     * Reads a task list from its encoded representation in the given string.
     *
     * The format of the string should match the encoded representation of a task list,
     * as described in TaskList.encode().
     *
     * The encoded string is invalid if any of the following conditions are true:
     * The task list's TaskType is not valid.
     * A task's load percentage is not an integer
     * A task's load percentage is less than zero.
     * More than one at-symbol (@) is detected for any task in the task list.
     * The task list is invalid according to the rules specified in TaskList(List).
     *
     * @param taskListPart - string containing the encoded task list
     * @return decoded task list instance
     * @throws MalformedSaveException - if the format of the given string is
     * invalid according to the rules above
     */
    public static TaskList readTaskList(String taskListPart) throws MalformedSaveException {
        List<Task> tasks = new ArrayList<Task>();
        String[] splitTasks = taskListPart.split(",");
        int loadPercentage;

        for (String taskName : splitTasks) {
            if (!(taskName.equals("WAIT") || taskName.startsWith("LOAD@")
                    || taskName.equals("TAKEOFF") || taskName.equals("AWAY")
                    || taskName.equals("LAND"))) {
                throw new MalformedSaveException();
            } else {
                if (taskName.startsWith("LOAD@")) {
                    String[] splitLoadTask = taskName.split("@");
                    try {
                        loadPercentage = Integer.parseInt(splitLoadTask[1]);
                    } catch (NumberFormatException nfe) {
                        throw new MalformedSaveException();
                    }

                    if (loadPercentage < 0) {
                        throw new MalformedSaveException();
                    }

                    tasks.add(new Task(TaskType.LOAD, loadPercentage));
                } else if (taskName.equals("WAIT")) {
                    tasks.add(new Task(TaskType.WAIT));
                } else if (taskName.equals("TAKEOFF")) {
                    tasks.add(new Task(TaskType.TAKEOFF));
                } else if (taskName.equals("AWAY")) {
                    tasks.add(new Task(TaskType.AWAY));
                } else if (taskName.equals("LAND")) {
                    tasks.add(new Task(TaskType.LAND));
                } else {
                    throw new MalformedSaveException();
                }
            }
        }

        String[] splitByAt = taskListPart.split("@");
        if (splitByAt.length > 2) {
            throw new MalformedSaveException();
        }

        try {
            return new TaskList(tasks);
        } catch (IllegalArgumentException e) {
            throw new MalformedSaveException();
        }
    }

    /**
     * Reads an aircraft queue from the given reader instance.
     *
     * Rather than returning a queue, this method does not return anything.
     * Instead, it should modify the given aircraft queue by adding aircraft to it.
     *
     * The contents of the text read from the reader should match the encoded
     * representation of an aircraft queue.
     *
     * The contents read from the reader are invalid if any of the following conditions are true:
     * 1. The first line read from the reader is null.
     *
     * 2. The first line contains more/fewer colons (:) than expected.
     *
     * 3. The queue type specified in the first line is not equal to the simple class
     * name of the queue provided as a parameter.
     *
     * 4. The number of aircraft specified on the first line is not an integer
     *
     * 5. The number of aircraft specified is greater than zero and the second line read is null.
     *
     * 6. The number of callsigns listed on the second line is not equal to
     * the number of aircraft specified on the first line.
     *
     * 7. A callsign listed on the second line does not correspond to the callsign of
     * any aircraft contained in the list of aircraft given as a parameter.
     *
     * @param reader - reader from which to load the aircraft queue
     * @param aircraft - list of all aircraft, used when validating that callsigns exist
     * @param queue - empty queue that aircraft will be added to
     *
     * @throws MalformedSaveException - if the format of the text read from the
     * reader is invalid according to the rules above
     * @throws IOException - if an IOException is encountered when reading from the reader
     */
    public static void readQueue(BufferedReader reader, List<Aircraft> aircraft,
                                 AircraftQueue queue) throws IOException, MalformedSaveException {
        int aircraftNumber;

        boolean correctCallsign;

        //Read first line
        String line = reader.readLine();

        //Check if the first line is null
        if (line == null) {
            throw new MalformedSaveException();
        }

        //Check if the first line contains more/fewer colons (:) than expected
        String[] typeAndNumber = line.split(":");
        if (typeAndNumber.length != 1) {
            throw new MalformedSaveException();
        }

        //Check if the queue type is correct
        if (!typeAndNumber[0].equals(queue.getClass().getSimpleName())) {
            throw new MalformedSaveException();
        }

        //Check if the number of aircraft on the first line is valid
        try {
            aircraftNumber = Integer.parseInt(typeAndNumber[1]);
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        //Read second line
        line = reader.readLine();

        //Check if the number of callsign is correct
        if (line == null && aircraftNumber > 0) {
            throw new MalformedSaveException();
        }

        if (line != null) {
            String[] callsigns = line.split(",");

            if (callsigns.length != aircraftNumber) {
                throw new MalformedSaveException();
            }

            //Check if the callsign exist
            for (String callsign : callsigns) {
                correctCallsign = false;
                for (Aircraft craft : aircraft) {
                    if (craft.getCallsign().equals(callsign)) {
                        correctCallsign = true;
                        break;
                    }
                }

                if (!correctCallsign) {
                    throw new MalformedSaveException();
                }
            }

            //Add aircraft to queue if everything is correct
            for (String callsign : callsigns) {
                for (Aircraft craft : aircraft) {
                    if (craft.getCallsign().equals(callsign)) {
                        queue.addAircraft(craft);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Reads the map of currently loading aircraft from the given reader instance.
     *
     * Rather than returning a map, this method does not return anything.
     * Instead, it should modify the given map by adding entries (aircraft/integer pairs) to it.
     *
     * The contents of the text read from the reader should match the format specified
     * in the queuesWriter row of in the table shown in ViewModel.saveAs().
     * Note that this method should only read the map of loading aircraft,
     * not the takeoff queue or landing queue. Reading these queues is handled
     * in the readQueue(BufferedReader, List, AircraftQueue) method.
     *
     * The contents read from the reader are invalid if any of the following conditions are true:
     * 1. The first line read from the reader is null.
     *
     * 2. The number of colons (:) detected on the first line is more/fewer than expected.
     *
     * 3. The number of aircraft specified on the first line is not an integer
     *
     * 4. The number of aircraft is greater than zero and the second line read from the reader is null.
     *
     * 5. The number of aircraft specified on the first line is not equal to
     * the number of callsigns read on the second line.
     *
     * 6. For any callsign/loading time pair on the second line,
     * the number of colons detected is not equal to one.
     *
     * 7. A callsign listed on the second line does not correspond to the callsign
     * of any aircraft contained in the list of aircraft given as a parameter.
     *
     * 8. Any ticksRemaining value on the second line is not an integer
     *
     * 9. Any ticksRemaining value on the second line is less than one (1).
     *
     * @param reader - reader from which to load the map of loading aircraft
     * @param aircraft - list of all aircraft, used when validating that callsigns exist
     * @param loadingAircraft - empty map that aircraft and their loading times will be added to
     * @throws MalformedSaveException - if the format of the text read from the reader
     * is invalid according to the rules above
     * @throws IOException - if an IOException is encountered when reading from the reader
     */
    public static void readLoadingAircraft(BufferedReader reader, List<Aircraft> aircraft,
                                           Map<Aircraft,Integer> loadingAircraft)
            throws IOException, MalformedSaveException {
        int queueLength;
        int ticksRemaining;

        //Read the first line
        String line = reader.readLine();

        //Check if the first line is null
        if (line == null) {
            throw new MalformedSaveException();
        }

        //Check if the first line is valid
        String[] queueTypeAndNumber = line.split(":");
        if (queueTypeAndNumber.length != 2) {
            throw new MalformedSaveException();
        }

        try {
            queueLength = Integer.parseInt(queueTypeAndNumber[1]);
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        //Read the second line
        line = reader.readLine();

        //Check if the queue length declared is correct
        if (queueLength > 0 && line == null) {
            throw new MalformedSaveException();
        }

        String[] callsignTickRemainingPairs = line.split(",");
        if (queueLength != callsignTickRemainingPairs.length) {
            throw new MalformedSaveException();
        }

        for (String callsignTickRemainingPair : callsignTickRemainingPairs) {
            String[] callsignAndTickRemaining = callsignTickRemainingPair.split(":");

            //Check if each callsign tick remaining pair is correct
            if (callsignAndTickRemaining.length != 2) {
                throw new MalformedSaveException();
            }

            //Check if each callsign is valid
            Aircraft targetAircraft = null;
            for (Aircraft craft : aircraft) {
                if (craft.getCallsign().equals(callsignAndTickRemaining[0])) {
                    targetAircraft = craft;
                    break;
                }
            }

            if (targetAircraft == null) {
                throw new MalformedSaveException();
            }

            //Check if each ticks remaining is valid
            try {
                ticksRemaining = Integer.parseInt(callsignAndTickRemaining[1]);
            } catch (NumberFormatException nfe) {
                throw new MalformedSaveException();
            }

            if (ticksRemaining < 1) {
                throw new MalformedSaveException();
            }

            //If everything is correct, add entries to the given map
            loadingAircraft.put(targetAircraft, ticksRemaining);
        }
    }

    /**
     * Reads a terminal from the given string and reads its gates from the given reader instance.
     *
     * The format of the given string and the text read from the reader should match
     * the encoded representation of a terminal, as described in Terminal.encode().
     *
     * The encoded terminal is invalid if any of the following conditions are true:
     * 1. The number of colons (:) detected on the first line is more/fewer than expected.
     *
     * 2. The terminal type specified on the first line is
     * neither AirplaneTerminal nor HelicopterTerminal.
     *
     * 3. The terminal number is not an integer
     *
     * 4. The terminal number is less than one (1).
     *
     * 5. The number of gates in the terminal is not an integer.
     *
     * 6. The number of gates is less than zero or is greater than Terminal.MAX_NUM_GATES.
     *
     * 7. A line containing an encoded gate was expected, but EOF (end of file) was received
     *
     * 8. Any of the conditions listed in the Javadoc for readGate(String, List) are true.
     *
     * @param line - string containing the first line of the encoded terminal
     * @param reader - reader from which to load the gates of the terminal (subsequent lines)
     * @param aircraft - list of all aircraft, used when validating that callsigns exist
     * @return decoded terminal with its gates added
     * @throws MalformedSaveException - if the format of the given string or the text read
     * from the reader is invalid according to the rules above.
     * @throws IOException - if an IOException is encountered when reading from the reader
     */
    public static Terminal readTerminal(String line, BufferedReader reader,
                                        List<Aircraft> aircraft)
            throws IOException, MalformedSaveException {
        String[] terminalInfo = line.split(":");
        int terminalNumber;
        int numberOfGates;

        String nextLine;

        Terminal terminal = null;

        if (terminalInfo.length != 4) {
            throw new MalformedSaveException();
        }

        if (!terminalInfo[0].equals("HelicopterTerminal")) {
            if (!terminalInfo[0].equals("AirplaneTerminal")) {
                throw new MalformedSaveException();
            }
        }

        try {
            terminalNumber = Integer.parseInt(terminalInfo[1]);
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        if (terminalNumber < 1) {
            throw new MalformedSaveException();
        }

        if (terminalInfo[0].equals("HelicopterTerminal")) {
            terminal = new HelicopterTerminal(terminalNumber);
        } else if (terminalInfo[0].equals("AirplaneTerminal")) {
            terminal = new AirplaneTerminal(terminalNumber);
        }

        try {
            numberOfGates = Integer.parseInt(terminalInfo[3]);
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        if (numberOfGates < 0 || numberOfGates > Terminal.MAX_NUM_GATES) {
            throw new MalformedSaveException();
        }

        if (numberOfGates > 0) {
            for (int i = 0; i < numberOfGates; i++) {
                nextLine = reader.readLine();
                if (nextLine == null) {
                    throw new MalformedSaveException();
                } else {
                    try {
                        terminal.addGate(readGate(line, aircraft));
                    } catch (NoSpaceException nse) {
                        //Ignore. New terminal has no gate, and gate number has been checked
                    }
                }
            }
        }

        if (terminalInfo[2].equals("true")) {
            terminal.declareEmergency();
        }

        return terminal;
    }

    /**
     * Reads a gate from its encoded representation in the given string.
     *
     * The format of the string should match the encoded representation of a gate
     *
     * The encoded string is invalid if any of the following conditions are true:
     * 1. The number of colons (:) detected was more/fewer than expected.
     *
     * 2. The gate number is not an integer
     *
     * 3. The gate number is less than one (1).
     *
     * 4. The callsign of the aircraft parked at the gate is not empty and the callsign
     * does not correspond to the callsign of any aircraft contained in the list
     * of aircraft given as a parameter.
     *
     * @param line - string containing the encoded gate
     * @param aircraft - list of all aircraft, used when validating that callsigns exist
     * @return decoded gate instance
     * @throws MalformedSaveException - if the format of the given string is
     * invalid according to the rules above
     */
    public static Gate readGate(String line, List<Aircraft> aircraft)
            throws MalformedSaveException {
        String[] encodedGate = line.split(":");
        int gateNumber;
        String callsign = null;
        Gate gate;
        Aircraft aircraftAtGate = null;

        if (encodedGate.length != 2) {
            throw new MalformedSaveException();
        }

        try {
            gateNumber = Integer.parseInt(encodedGate[0]);
        } catch (NumberFormatException nfe) {
            throw new MalformedSaveException();
        }

        if (gateNumber < 1) {
            throw new MalformedSaveException();
        }

        if (!encodedGate[1].equals("empty")) {
            for (Aircraft craft : aircraft) {
                if (craft.getCallsign().equals(encodedGate[1])) {
                    callsign = encodedGate[1];
                    aircraftAtGate = craft;
                    break;
                }
            }
        } else {
            callsign = "empty";
        }

        if (callsign == null) {
            throw new MalformedSaveException();
        }

        gate = new Gate(gateNumber);
        if (!callsign.equals("empty")) {
            try {
                gate.parkAircraft(aircraftAtGate);
            } catch (NoSpaceException nse) {
                //Ignore. New Gate object is always unoccupied
            }
        }

        return gate;
    }
}

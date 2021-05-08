package towersim.control;

import towersim.aircraft.Aircraft;
import towersim.aircraft.PassengerAircraft;
import towersim.util.Encodable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rule-based queue of aircraft waiting in the air to land.
 *
 * The rules in the landing queue are designed to ensure that aircraft are
 * prioritised for landing based on "urgency" factors such as
 * remaining fuel onboard, emergency status and cargo type.
 */
public class LandingQueue extends AircraftQueue {
    /**
     * The landing queue list.
     */
    private ArrayList<Aircraft> landingQueue;

    /**
     * Constructs a new LandingQueue with an initially empty queue of aircraft.
     */
    public LandingQueue() {
        landingQueue = new ArrayList<Aircraft>();
    }

    /**
     * Adds the given aircraft to the queue.
     *
     * @param aircraft - aircraft to add to queue
     */
    public void addAircraft(Aircraft aircraft) {
        landingQueue.add(aircraft);
    }

    /**
     * Returns the aircraft at the front of the queue without removing it
     * from the queue, or null if the queue is empty.
     *
     * If an aircraft is currently in a state of emergency, it should be returned.
     * If more than one aircraft are in an emergency, return the one added to the queue first.
     *
     * If an aircraft has less than or equal to 20 percent fuel remaining,
     * a critical level, it should be returned
     *
     *  If more than one aircraft have a critical level of fuel onboard,
     *  return the one added to the queue first.
     *
     *  If there are any passenger aircraft in the queue, return the passenger aircraft
     *  that was added to the queue first.
     *
     *  If this point is reached and no aircraft has been returned,
     *  return the aircraft that was added to the queue first.
     *
     * @return aircraft at front of queue
     */
    public Aircraft peekAircraft() {
        if (this.landingQueue.isEmpty()) {
            return null;
        }

        for (Aircraft aircraft : this.landingQueue) {
            if (aircraft.hasEmergency()) {
                return aircraft;
            }
        }

        for (Aircraft aircraft : this.landingQueue) {
            if (aircraft.getFuelPercentRemaining() <= 20) {
                return aircraft;
            }
        }

        for (Aircraft aircraft : this.landingQueue) {
            if (aircraft instanceof PassengerAircraft) {
                return aircraft;
            }
        }

        return this.landingQueue.get(0);
    }

    /**
     * Removes and returns the aircraft at the front of the queue.
     * Returns null if the queue is empty.
     *
     * The same rules as described in peekAircraft() should be used
     * for determining which aircraft to remove and return.
     *
     * @return aircraft at front of queue
     */
    public Aircraft removeAircraft() {
        Aircraft poppedAircraft;
        if (this.landingQueue.isEmpty()) {
            return null;
        }

        for (Aircraft aircraft : this.landingQueue) {
            if (aircraft.hasEmergency()) {
                poppedAircraft = aircraft;
                this.landingQueue.remove(aircraft);
                return poppedAircraft;
            }
        }

        for (Aircraft aircraft : this.landingQueue) {
            if (aircraft.getFuelPercentRemaining() <= 20) {
                poppedAircraft = aircraft;
                this.landingQueue.remove(aircraft);
                return poppedAircraft;
            }
        }

        for (Aircraft aircraft : this.landingQueue) {
            if (aircraft instanceof PassengerAircraft) {
                poppedAircraft = aircraft;
                this.landingQueue.remove(aircraft);
                return poppedAircraft;
            }
        }

        poppedAircraft = this.landingQueue.get(0);
        this.landingQueue.remove(0);
        return poppedAircraft;
    }

    /**
     * Returns a list containing all aircraft in the queue, in order.
     *
     * Adding or removing elements from the returned list should not affect the original queue.
     *
     * @return list of all aircraft in queue, in queue order
     */
    public List<Aircraft> getAircraftInOrder() {
        ArrayList<Aircraft> landingQueueCopy = new ArrayList<Aircraft>(this.landingQueue);
        ArrayList<Aircraft> orderedAircraft = new ArrayList<Aircraft>();
        if (landingQueueCopy.size() > 0) {
            for (int i = 0; i < landingQueueCopy.size(); i++) {
                orderedAircraft.add(this.removeAircraft());
            }
        }

        this.landingQueue = landingQueueCopy;

        return orderedAircraft;
    }

    /**
     * Returns true if the given aircraft is in the queue.
     *
     * @param aircraft - aircraft to find in queue
     * @return true if aircraft is in queue; false otherwise
     */
    public boolean containsAircraft(Aircraft aircraft) {
        return this.landingQueue.contains(aircraft);
    }
}

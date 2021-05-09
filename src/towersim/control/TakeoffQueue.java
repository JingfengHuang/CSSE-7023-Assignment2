package towersim.control;

import towersim.aircraft.Aircraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a first-in-first-out (FIFO) queue of aircraft waiting to take off.
 *
 * FIFO ensures that the order in which aircraft are allowed to take off is based
 * on long they have been waiting in the queue. An aircraft that has been waiting for
 * longer than another aircraft will always be allowed to take off before the other aircraft.
 */
public class TakeoffQueue extends AircraftQueue {
    /**
     * The Takeoff queue list
     */
    private ArrayList<Aircraft> takeoffQueue;

    /**
     * Constructs a new TakeoffQueue with an initially empty queue of aircraft.
     */
    public TakeoffQueue() {
        takeoffQueue = new ArrayList<Aircraft>();
    }

    /**
     * Adds the given aircraft to the queue.
     *
     * @param aircraft - aircraft to add to queue
     */
    public void addAircraft(Aircraft aircraft) {
        this.takeoffQueue.add(aircraft);
    }

    /**
     * Returns the aircraft at the front of the queue without removing it
     * from the queue, or null if the queue is empty.
     *
     * Aircraft returned by peekAircraft() should be in the same order
     * that they were added via addAircraft().
     *
     * @return  aircraft at front of queue
     */
    public Aircraft peekAircraft() {
        if (this.takeoffQueue.isEmpty()) {
            return null;
        } else {
            return this.takeoffQueue.get(0);
        }
    }

    /**
     * Removes and returns the aircraft at the front of the queue.
     * Returns null if the queue is empty.
     *
     * Aircraft returned by removeAircraft() should be in the same order
     * that they were added via addAircraft().
     *
     * @return aircraft at front of queue
     */
    public Aircraft removeAircraft() {
        Aircraft firstAircraft;
        if (this.takeoffQueue.isEmpty()) {
            return null;
        } else {
            firstAircraft = this.takeoffQueue.get(0);
            this.takeoffQueue.remove(0);
            return firstAircraft;
        }
    }

    /**
     * Returns a list containing all aircraft in the queue, in order.
     *
     * That is, the first element of the returned list should be the
     * first aircraft that would be returned by calling removeAircraft(), and so on.
     *
     * Adding or removing elements from the returned list should not affect the original queue.
     *
     * @return list of all aircraft in queue, in queue order
     */
    public List<Aircraft> getAircraftInOrder() {
        ArrayList<Aircraft> takeoffQueueCopy = new ArrayList<Aircraft>(this.takeoffQueue);
        ArrayList<Aircraft> orderedTakeoffQueue = new ArrayList<Aircraft>();

        if (!(this.takeoffQueue.isEmpty())) {
            for (int i = 0; i < takeoffQueueCopy.size(); i++) {
                orderedTakeoffQueue.add(this.removeAircraft());
            }
        }

        this.takeoffQueue = takeoffQueueCopy;

        return orderedTakeoffQueue;
    }

    /**
     * Returns true if the given aircraft is in the queue.
     *
     * @param aircraft - aircraft to find in queue
     * @return true if aircraft is in queue; false otherwise
     */
    public boolean containsAircraft(Aircraft aircraft) {
        return this.takeoffQueue.contains(aircraft);
    }
}

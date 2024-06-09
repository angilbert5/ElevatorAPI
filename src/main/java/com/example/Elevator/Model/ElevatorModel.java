package com.example.Elevator.Model;

import io.vertx.core.json.JsonObject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;

public class ElevatorModel {
  private int _maxFloors;
  private int _currentFloor;
  private String _status;

  public enum STATUS {
    IDLE,
    PENDING_MOVE,
    MOVING_UP,
    MOVING_DOWN;
  }

  public enum ElevatorDirection{
    UP,
    DOWN;
  }
  public ElevatorModel(int maxFloors, STATUS status, int currentFloor) {
    _maxFloors = maxFloors;
    _status = status.name();
    _currentFloor = currentFloor;
  }

  public List<JsonObject> moveElevator(String requestedFloor) {
    List<JsonObject> elevatorJsonList = new ArrayList<>();
    int intRequestedFloor = Integer.parseInt(requestedFloor);
    //List<Integer> descendingFloors = requestedFloorsList.stream().filter( x -> x < _currentFloor).toList();
    if(intRequestedFloor < _currentFloor){
      return moveElevatorUp(intRequestedFloor);
    }
    if(intRequestedFloor > _currentFloor){
      return moveElevatorDown(intRequestedFloor);
    }
    return elevatorJsonList;
  }

  private List<JsonObject> moveElevatorDown(Integer descendingFloor) {
    List<JsonObject> elevatorDownJson = new ArrayList<>();
      if(descendingFloor <= _maxFloors && descendingFloor >= 0) {
        elevatorDownJson.add(displayElevatorStatus(Optional.of(descendingFloor)));
        _status = STATUS.MOVING_DOWN.name();
        _currentFloor = descendingFloor;
        elevatorDownJson.add(displayElevatorStatus(Optional.of(descendingFloor)));
        elevatorDownJson.add(elevatorStopped());
      }

    return elevatorDownJson;
  }

  private List<JsonObject> moveElevatorUp(Integer ascendingFloor) {
    List<JsonObject> elevatorUpJson = new ArrayList<>();
      if(ascendingFloor <= _maxFloors) {
        elevatorUpJson.add(displayElevatorStatus(Optional.of(ascendingFloor)));
        _status = STATUS.MOVING_UP.name();
        _currentFloor = ascendingFloor;
        elevatorUpJson.add(displayElevatorStatus(Optional.of(ascendingFloor)));
        elevatorUpJson.add(elevatorPending());
      }
    elevatorUpJson.add(elevatorStopped());
    return elevatorUpJson;
  }

  private JsonObject elevatorPending() {
    _status = STATUS.PENDING_MOVE.name();
    return displayElevatorStatus(Optional.empty());
  }

  private JsonObject elevatorStopped() {
    _status = STATUS.IDLE.name();
    return displayElevatorStatus(Optional.empty());
  }

  public JsonObject displayElevatorStatus(Optional<Integer> requestedFloor){
    JsonObject elevatorJson = new JsonObject();
    elevatorJson.put("currentFloor", (_currentFloor == 0?"G":_currentFloor));
    elevatorJson.put("currentStatus: ",_status);
    if (requestedFloor.isPresent()) {
      elevatorJson.put("requestedFloor",(requestedFloor.get()==0?"G":requestedFloor.get()));
    } else {
      elevatorJson.put("requestedFloor","");
    }
    return elevatorJson;
  }
}


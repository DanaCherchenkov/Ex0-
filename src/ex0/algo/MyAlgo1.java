package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class MyAlgo1 implements ElevatorAlgo {

    private Building MyBuilding;
    private ArrayList<Elevator> MyElevators;
    private ArrayList<CallForElevator>[] callsPerElevator;
    private ArrayList<Integer> allCalls;
//    private Queue<CallForElevator> UPCalls;
//    private Queue<CallForElevator> DOWNCalls;
    private Elevator[] ElevatorsSpeedSort;
    private ArrayList<Integer> freeElevator; ////add to constructor




    //Constructor
    public MyAlgo1(Building B){
        this.MyBuilding = B;
        MyElevators = new ArrayList<Elevator>();
        callsPerElevator = new ArrayList[MyBuilding.numberOfElevetors()];
        allCalls = new ArrayList<Integer>();
        freeElevator = new ArrayList<Integer>();
//        UPCalls = new LinkedList<CallForElevator>();
//        DOWNCalls = new LinkedList<CallForElevator>();
        ElevatorsSpeedSort = new Elevator[MyBuilding.numberOfElevetors()];
        for (int i = 0; i < MyBuilding.numberOfElevetors(); i++) {
            MyElevators.add(MyBuilding.getElevetor(i));
            ElevatorsSpeedSort[i] = MyBuilding.getElevetor(i);
            callsPerElevator[i] = new ArrayList<CallForElevator>();
        }
        ElevatorsSpeedSort();
    }

    @Override
    public Building getBuilding() {
        return MyBuilding;
    }

    @Override
    public String algoName() {
        return "Ex0_OOP_dor_dana_Elevator_Algo";
    }


    @Override
    public int allocateAnElevator(CallForElevator c) {

        //if there is only one elevator
        if (MyBuilding.numberOfElevetors() == 1) {
            callsPerElevator[0].add(c);
            return 0;
        }

        //Start checking which elevator is available
        freeElevator.clear();
        for (int i = 0; i < this.MyBuilding.numberOfElevetors(); i++) {
            if (callsPerElevator[i].size() == 0) {
                freeElevator.add(i);
            }
        }
        if(freeElevator.isEmpty()){
            //first case to check if the gap between the src and the dest is big-take the most fast elevator.
            int howManyFloors = Math.abs(c.getDest() - c.getSrc());
            int howManyFloorsInTheBuilding = MyBuilding.maxFloor() - MyBuilding.minFloor();
            double checkGap = (howManyFloors / howManyFloorsInTheBuilding);
            if(checkGap > 0.5){
                callsPerElevator[theMostFastElevator()].add(c);
                return theMostFastElevator();
            }
            else {
                //allCalls.clear();
                int sum = 0;
                for (int i = 0; i < MyBuilding.numberOfElevetors(); i++) {
                    allCalls.add(callsPerElevator[i].size());
                    sum += callsPerElevator[i].size();
                }
                allCalls.add(sum);
                //check if there are the same calls in current time in other elevator that can take this call
                if(sameCallsAtThisMoment() != -1){
                    callsPerElevator[sameCallsAtThisMoment()].add(c);
                    return sameCallsAtThisMoment();
                }else {

                    //if there is no similar calls we will check the elevator with the less calls to take this one
                    int less = fewestCalls(c);
                    callsPerElevator[less].add(c);
                    return less;
                }
        }
    }else {
            int close = checkTheClosestElevator(c);
            callsPerElevator[close].add(c);
            return MyElevators.get(close).getID();
        }
}

    @Override
    public void cmdElevator(int elev) {

    }

    //This function is searching the closest elevator that is available to take the call
    private int checkTheClosestElevator(CallForElevator c){
        int ans = 0;
        int gap = Math.abs(MyBuilding.getElevetor(freeElevator.get(0)).getPos() - c.getSrc());
        for(int i = 0; i < freeElevator.size(); i++){
            if(gap > Math.abs(MyBuilding.getElevetor(freeElevator.get(i)).getPos())){
                ans = freeElevator.get(i);
                gap = Math.abs(MyBuilding.getElevetor(freeElevator.get(i)).getPos() - c.getSrc());
            }
        }
        return ans;
    }

    ///*********************???***********************//
    private int fewestCalls(CallForElevator c){
        //double average = allCalls.size() / MyBuilding.numberOfElevators();
        int ans = callsPerElevator[0].size();
        for(int i = 1; i < callsPerElevator.length; i++){
            if(callsPerElevator[i].size() > callsPerElevator[i+1].size()){
                ans = callsPerElevator[i+1].size();
            }
        }
        return ans;
    }


    //This function is sorting the array that representing the from the fastest elevator to the most slow elevator
    private void ElevatorsSpeedSort() {
        for (int i = 0; i < ElevatorsSpeedSort.length; i++) {
            for (int j = 0; j < ElevatorsSpeedSort.length - 1 - i; j++)
                if (ElevatorsSpeedSort[j].getSpeed() > ElevatorsSpeedSort[j + 1].getSpeed())
                    swap(ElevatorsSpeedSort, j, j + 1);
        }
    }

    private void swap(Elevator[] elevatorsSpeedSort, int j, int i) {
        Elevator temp = elevatorsSpeedSort[i];
        elevatorsSpeedSort[i] = elevatorsSpeedSort[j];
        elevatorsSpeedSort[j] = temp;
    }

    //This function returns the fastest elevator of all the elevators in the array
    private int theMostFastElevator(){
        int ans=0;
        double temp= MyElevators.get(0).getSpeed();
        for (int i = 1; i< MyBuilding.numberOfElevetors(); i++){
            if(MyElevators.get(i).getSpeed() > temp){
                temp = MyElevators.get(i).getSpeed();
                ans = i;
            }
        }
        return ans;
    }

    //In this function we can see which calls exist that can optimize the calls on the way, and this will be done by checking that if the source
    // and destination of the call is between the source and the destination of the current call in which the elevator is located, considering the direction of the elevator.
    private int sameCallsAtThisMoment() {
        double average = allCalls.size() / MyBuilding.numberOfElevetors();
        for (int i = 0; i < MyBuilding.numberOfElevetors(); i++) {
            if (callsPerElevator[i].get(0).getType() == CallForElevator.UP) { //check if my call is up
                for (int j = 0; j < callsPerElevator[j].size(); j++) {
                    for (int j = 0; j < callsPerElevator[i].size(); j++) {
                        if ((callsPerElevator[i].get(j).getType() == CallForElevator.UP) &&
                                (callsPerElevator[i].get(0).getSrc() <= callsPerElevator[i].get(j).getSrc()) &&
                                (callsPerElevator[i].get(j).getDest() <= callsPerElevator[i].get(0).getDest()) &&
                                (average > callsPerElevator[i].size()))
                            return i;
                    }
                }
            } else if (callsPerElevator[i].get(0).getType() == CallForElevator.DOWN) { //check if my call is down
                for (int j = 0; j < callsPerElevator[i].size(); j++) {
                    if (callsPerElevator[i].get(j).getType() == CallForElevator.DOWN) {
                        if ((callsPerElevator[i].get(0).getSrc() >= callsPerElevator[i].get(j).getSrc()) && (callsPerElevator[i].get(j).getDest() >= callsPerElevator[i].get(0).getDest())) {
                            return i;
                        } else if ((callsPerElevator[i].get(0).getDest() == callsPerElevator[i].get(j).getDest()) && (callsPerElevator[i].get(j).getSrc() < callsPerElevator[i].get(0).getSrc())) {
                            if (average > callsPerElevator[i].size())
                                return i;
                        }
                    }
                }
            }
        }
        return -1;
    }
}

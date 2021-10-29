package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class MyAlgo implements ElevatorAlgo {

    private Building MyBuilding;
    private ArrayList<Elevator> MyElevators;
    private ArrayList<CallForElevator>[] callsPerElevator;
    private ArrayList<Integer> allCalls;
    private Elevator[] ElevatorsSpeedSort;
    private ArrayList<Integer> freeElevator;


    //Constructor
    public MyAlgo(Building B) {
        this.MyBuilding = B;
        MyElevators = new ArrayList<Elevator>();
        callsPerElevator = new ArrayList[MyBuilding.numberOfElevetors()];
        allCalls = new ArrayList<Integer>();
        freeElevator = new ArrayList<Integer>();
        ElevatorsSpeedSort = new Elevator[MyBuilding.numberOfElevetors()];
        for (int i = 0; i < MyBuilding.numberOfElevetors(); i++) {
            MyElevators.add(MyBuilding.getElevetor(i));
            ElevatorsSpeedSort[i] = MyBuilding.getElevetor(i);
            callsPerElevator[i] = new ArrayList<CallForElevator>();
        }
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

        if (freeElevator.isEmpty()) {
            //case 1
            //check if the gap between the src and the dest is big-take the most fast elevator.
            int howManyFloors = Math.abs(c.getDest() - c.getSrc());
            int howManyFloorsInTheBuilding = MyBuilding.maxFloor() - MyBuilding.minFloor();
            double checkGap = (howManyFloors / howManyFloorsInTheBuilding);
            if (checkGap > 0.5) {
                callsPerElevator[theMostFastElevator()].add(c);
                return theMostFastElevator();
            }
            allCalls.clear();
            int sum = 0;
            for (int i = 0; i < MyBuilding.numberOfElevetors(); i++) {
                allCalls.add(callsPerElevator[i].size());
                sum += callsPerElevator[i].size();
            }
            allCalls.add(sum);

            //case 2
            //check if there are the same calls in current time in other elevator that can take this call
            if (sameCallsAtThisMoment() != -1) {
                callsPerElevator[sameCallsAtThisMoment()].add(c);
                return sameCallsAtThisMoment();
            } else {
                //case 3
                //if there is no similar calls we will check the elevator with the less calls to take this one
                if (sameCallsAtThisMoment() == -1) {
                    int less = fewestCalls(c);
                    callsPerElevator[less].add(c);
                    return less;
                } else {
                    //case 4
                    //if there are no similar calls, and all the elevators have the same amount of calls, so this is the last option just go to the closest elevator.
                    int close = checkTheClosestElevator(c);
                    callsPerElevator[close].add(c);
                    return MyElevators.get(close).getID();
                }
            }
        } else {//if the freeElevator is not empty
            callsPerElevator[freeElevator.get(0)].add(c);
            return freeElevator.get(0);

        }
    }


    @Override
    public void cmdElevator(int elev) {
        //        "no calls";
        if (callsPerElevator[elev].size() == 0){
            return;

        } else {

//            if(callsPerElevator[elev].get(0).getState())

//          "done to src then go to dest";
            if(MyElevators.get(elev).getState()==0&&callsPerElevator[elev].get(0).getState()==2){
                MyElevators.get(elev).goTo(callsPerElevator[elev].get(0).getDest());
                return;
            }

//          "done the mission then remove from the callsPerElevator  the call";
            if(MyElevators.get(elev).getState()==0&&callsPerElevator[elev].get(0).getState()==3){
                callsPerElevator[elev].remove(0);
                cmdElevator(elev);
                return;
            }


            if(MyElevators.get(elev).getState()==0&&callsPerElevator[elev].get(0).getState()!=2){
                MyElevators.get(elev).goTo(callsPerElevator[elev].get(0).getSrc());
                for(int i=1;i<callsPerElevator[elev].size();i++){

                    if(callsPerElevator[elev].get(i).getType()==callsPerElevator[elev].get(0).getType()&&callsPerElevator[elev].get(0).getType()==1){
                        if(MyElevators.get(elev).getPos() == callsPerElevator[elev].get(i).getSrc()&&callsPerElevator[elev].get(i).getDest()<callsPerElevator[elev].get(0).getDest()){
                            MyElevators.get(elev).stop(callsPerElevator[elev].get(i).getDest());
                            break;
                        }
                    }

                    if(callsPerElevator[elev].get(i).getType()==callsPerElevator[elev].get(0).getType()&&callsPerElevator[elev].get(0).getType()==-1){
                        if(callsPerElevator[elev].get(i).getSrc()==callsPerElevator[elev].get(0).getSrc()&&callsPerElevator[elev].get(i).getDest()>callsPerElevator[elev].get(0).getDest()){
                            MyElevators.get(elev).stop(callsPerElevator[elev].get(i).getDest());
                            callsPerElevator[elev].remove(i);
                            break;
                        }
                        else if(callsPerElevator[elev].get(i).getDest()==callsPerElevator[elev].get(0).getDest()&&callsPerElevator[elev].get(i).getSrc()<callsPerElevator[elev].get(0).getSrc()){
                            MyElevators.get(elev).stop(callsPerElevator[elev].get(i).getSrc());
                            callsPerElevator[elev].remove(i);
                            break;
                        }
                    }

                }
                return;
            }
        }
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

    //This function return which elevator has the less calls to do.
    private int fewestCalls(CallForElevator c){
        int all = allCalls.get(0);
        int ans=0;
        for (int i = 1; i < MyElevators.size(); i++) {
            if (allCalls.get(i) < all) {
                all = callsPerElevator[i].size();
                ans=i;
            }
            return ans;
        }
        return checkTheClosestElevator(c);
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
                for (int j = 0; j < callsPerElevator[i].size(); j++) {
                    if ((callsPerElevator[i].get(j).getType() == CallForElevator.UP) &&
                            (callsPerElevator[i].get(0).getSrc() <= callsPerElevator[i].get(j).getSrc()) &&
                            (callsPerElevator[i].get(j).getDest() <= callsPerElevator[i].get(0).getDest()) &&
                            (average > callsPerElevator[i].size()))
                        return i;
                }
            }
            else if (callsPerElevator[i].get(0).getType() == CallForElevator.DOWN) { //check if my call is down
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

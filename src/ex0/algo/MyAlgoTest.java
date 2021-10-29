package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.simulator.Simulator_A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyAlgoTest {

    @Test
    void allocateAnElevator() {
        for (int i = 0; i < 100; i++) {
            CallForElevator c1 = new CallForElevator() {
                @Override
                public int getState() {
                    return 0;
                }

                @Override
                public double getTime(int state) {
                    return 0;
                }

                @Override
                public int getSrc() {
                    return (int) Math.random() * 10;
                }

                @Override
                public int getDest() {
                    return (int) Math.random() * 10;
                }

                @Override
                public int getType() {
                    return -1;
                }

                @Override
                public int allocatedTo() {
                    return -1;
                }
            };
            Simulator_A.initData(0, null);
            Building b1 = Simulator_A.getBuilding();
            MyAlgo r = new MyAlgo(b1);
            assertEquals(r.allocateAnElevator(c1), 0);
        }
        for (int i = 0; i < 100; i++) {
            CallForElevator c2 = new CallForElevator() {
                @Override
                public int getState() {
                    return 0;
                }

                @Override
                public double getTime(int state) {
                    return 0;
                }

                @Override
                public int getSrc() {
                    return (int) Math.random() * 10;
                }

                @Override
                public int getDest() {
                    return (int) Math.random() * 10;
                }

                @Override
                public int getType() {
                    return 1;
                }

                @Override
                public int allocatedTo() {
                    return -1;
                }
            };
            Simulator_A.initData(1, null);
            Building b2 = Simulator_A.getBuilding();
            MyAlgo r = new MyAlgo(b2);
            assertEquals(r.allocateAnElevator(c2), 0);
        }
    }

    @Test
    void cmdElevator() {
        if(true){
            int stage = 0;  // any case in [0,9].
            System.out.println("Ex0 Simulator: isStarting, stage="+stage+") ... =  ");
            String callFile = null; // use the predefined cases [0-9].
            //String callFile = "data/Ex0_stage_2__.csv"; //
            Simulator_A.initData(stage, callFile);  // init the simulator data: {building, calls}.

            // ElevatorAlgo ex0_alg = new ShabatElevAlgo(Simulator_A.getBuilding());  // The simplest algo ever (Shabat Elev).
            // ElevatorAlgo ex0_alg1 = new ShabatElev2Algo(Simulator_A.getBuilding()); // Shabat Elev with a minor twist
            // ElevatorAlgo ex0_alg2 = new MyAlgo(Simulator_A.getBuilding());
            ElevatorAlgo ex0_alg2 = new MyAlgo(Simulator_A.getBuilding());
            // Shabat Elev with two trick - replace with your code;
            Simulator_A.initAlgo(ex0_alg2); // init the algorithm to be used by the simulator
            //  Simulator_A.initAlgo(ex0_alg1);
            //  Simulator_A.initAlgo(ex0_alg2);

            Simulator_A.runSim(); // run the simulation - should NOT take more than few seconds.
            assertEquals(ex0_alg2.getBuilding().getElevetor(0).getPos(),1);

            stage = 1;  // any case in [0,9].
            System.out.println("Ex0 Simulator: isStarting, stage="+stage+") ... =  ");
            //String callFile = "data/Ex0_stage_2__.csv"; //
            Simulator_A.initData(stage, callFile);  // init the simulator data: {building, calls}.

            // ElevatorAlgo ex0_alg = new ShabatElevAlgo(Simulator_A.getBuilding());  // The simplest algo ever (Shabat Elev).
            // ElevatorAlgo ex0_alg1 = new ShabatElev2Algo(Simulator_A.getBuilding()); // Shabat Elev with a minor twist
            // ElevatorAlgo ex0_alg2 = new MyAlgo(Simulator_A.getBuilding());
            ElevatorAlgo my = new MyAlgo(Simulator_A.getBuilding());
            // Shabat Elev with two trick - replace with your code;
            Simulator_A.initAlgo(my); // init the algorithm to be used by the simulator
            //  Simulator_A.initAlgo(ex0_alg1);
            //  Simulator_A.initAlgo(ex0_alg2);

            Simulator_A.runSim(); // run the simulation - should NOT take more than few seconds.
            assertEquals(ex0_alg2.getBuilding().getElevetor(0).getPos(),1);

        }
        if(true){
            int stage = 1;  // any case in [0,9].
            System.out.println("Ex0 Simulator: isStarting, stage="+stage+") ... =  ");
            String callFile = null; // use the predefined cases [0-9].
            //String callFile = "data/Ex0_stage_2__.csv"; //
            Simulator_A.initData(stage, callFile);  // init the simulator data: {building, calls}.

            // ElevatorAlgo ex0_alg = new ShabatElevAlgo(Simulator_A.getBuilding());  // The simplest algo ever (Shabat Elev).
            // ElevatorAlgo ex0_alg1 = new ShabatElev2Algo(Simulator_A.getBuilding()); // Shabat Elev with a minor twist
            // ElevatorAlgo ex0_alg2 = new MyAlgo(Simulator_A.getBuilding());
            ElevatorAlgo ex0_alg2 = new MyAlgo(Simulator_A.getBuilding());
            // Shabat Elev with two trick - replace with your code;
            Simulator_A.initAlgo(ex0_alg2); // init the algorithm to be used by the simulator
            //  Simulator_A.initAlgo(ex0_alg1);
            //  Simulator_A.initAlgo(ex0_alg2);

            Simulator_A.runSim(); // run the simulation - should NOT take more than few seconds.
            assertEquals(ex0_alg2.getBuilding().getElevetor(0).getPos(),10);

        }
    }


    }



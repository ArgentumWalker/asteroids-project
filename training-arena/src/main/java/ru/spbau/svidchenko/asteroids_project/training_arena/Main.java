package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.AgentSaveLoader;
import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long GAME_COUNT = 40;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<GunnerAgent> gunnerAgents = getGunnerAgents();
        List<PilotAgent> pilotAgents = AgentSaveLoader.loadPilots();//getPilotAgents();
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT, executor, System.out::println);
        trainingPool.start();
        //костыль
        //gunnerAgents.remove(3);
        ///костыль
        trainingPool.join();
        executor.shutdown();
        //AgentSaveLoader.savePilots(pilotAgents);
        //AgentSaveLoader.saveGunners(gunnerAgents);
        System.out.println("Completed");
    }

    private static List<PilotAgent> getPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        result.addAll(getQLearningPolarPilots());
        //result.add(new DoNothingPilotAgent());
        //result.add(new FlyForwardPilotAgent());
        //result.add(new RandomFlyPilotAgent());
        //result.add(new LeftRollNFlyPilotAgent());
        //result.add(new RightRollNFlyPilotAgent());
        return result;
    }

    private static List<GunnerAgent> getGunnerAgents() {
        List<GunnerAgent> result = new ArrayList<>();
        //result.addAll(getQLearningPolarGunners());
        result.add(new DoNothingGunnerAgent());
        //result.add(new LeftRollNShootGunnerAgent());
        //result.add(new RandomShootGunnerAgent());
        //result.add(new RightRollNShootGunnerAgent());
        //result.add(new ShootForwardGunnerAgent());
        //result.add(new ShootClosestGunnerAgent());
        return result;
    }

    private static List<QLearningPolarPilotAgent> getQLearningPolarPilots() {
        long max = Constants.VEHICLE_TURNS_TO_TURN_AROUND;
        PolarGridDescriptor descriptor1 = new PolarGridDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.splitAngle(4);
        descriptor1.splitAngle(max - 4);
        descriptor1.splitAngle(20);
        descriptor1.splitAngle(max - 20);
        descriptor1.splitDistance(Constants.STONE_RADIUS * 5);
        descriptor1.splitDistance(Constants.STONE_RADIUS * 10);
        PolarGridDescriptor descriptor2 = new PolarGridDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.splitAngle(2);
        descriptor2.splitAngle(max - 2);
        descriptor2.splitAngle(12);
        descriptor2.splitAngle(max - 12);
        descriptor2.splitAngle(30);
        descriptor2.splitAngle(max - 30);
        PolarGridDescriptor descriptor3 = new PolarGridDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.splitAngle(2);
        descriptor3.splitAngle(max - 2);
        descriptor3.splitAngle(8);
        descriptor3.splitAngle(max - 8);
        descriptor3.splitAngle(16);
        descriptor3.splitAngle(max - 16);
        descriptor3.splitAngle(26);
        descriptor3.splitAngle(max - 26);
        descriptor3.splitDistance(Constants.STONE_RADIUS * 5);
        descriptor3.splitDistance(Constants.STONE_RADIUS * 10);
        PolarGridDescriptor descriptor4 = new PolarGridDescriptor();
        descriptor4.setMaxAngle(max);
        descriptor4.splitAngle(3);
        descriptor4.splitAngle(max - 3);
        descriptor4.splitAngle(9);
        descriptor4.splitAngle(max - 9);
        descriptor4.splitAngle(15);
        descriptor4.splitAngle(max - 15);
        descriptor4.splitAngle(25);
        descriptor4.splitAngle(max - 25);
        descriptor4.splitAngle(40);
        descriptor4.splitDistance(Constants.STONE_RADIUS * 5);
        descriptor4.splitDistance(Constants.STONE_RADIUS * 10);
        PolarGridDescriptor descriptor5 = new PolarGridDescriptor();
        descriptor5.setMaxAngle(max);
        descriptor5.splitAngle(3);
        descriptor5.splitAngle(max - 3);
        descriptor5.splitAngle(9);
        descriptor5.splitAngle(max - 9);
        descriptor5.splitAngle(15);
        descriptor5.splitAngle(max - 15);
        descriptor5.splitAngle(25);
        descriptor5.splitAngle(max - 25);
        descriptor5.splitAngle(40);
        descriptor5.splitDistance(Constants.STONE_RADIUS * 5);
        PolarGridDescriptor descriptor6 = new PolarGridDescriptor();
        descriptor6.setMaxAngle(max);
        descriptor6.splitAngle(2);
        descriptor6.splitAngle(max - 2);
        descriptor6.splitAngle(8);
        descriptor6.splitAngle(max - 8);
        descriptor6.splitAngle(16);
        descriptor6.splitAngle(max - 16);
        descriptor6.splitAngle(26);
        descriptor6.splitAngle(max - 26);
        descriptor6.splitDistance(Constants.STONE_RADIUS * 4);
        List<QLearningPolarPilotAgent> result = new ArrayList<>();
        result.add(new QLearningPolarPilotAgent(descriptor1, new Eps(), 0.3, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor2, new Eps(), 0.3, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor3, new Eps(), 0.3, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor4, new Eps(), 0.3, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor5, new Eps(), 0.3, 0.995));
        result.add(new QLearningPolarPilotAgent(descriptor6, new Eps(), 0.3, 0.995));
        return result;
    }

    public static List<QLearningPolarGunnerAgent> getQLearningPolarGunners() {
        long max = Constants.WEAPON_TURNS_TO_TURN_AROUND;
        PolarGridDescriptor descriptor1 = new PolarGridDescriptor();
        descriptor1.setMaxAngle(max);
        descriptor1.splitAngle(4);
        descriptor1.splitAngle(max - 4);
        descriptor1.splitAngle(20);
        descriptor1.splitAngle(max - 20);
        descriptor1.splitDistance(Constants.STONE_RADIUS * 5);
        descriptor1.splitDistance(Constants.STONE_RADIUS * 10);
        PolarGridDescriptor descriptor2 = new PolarGridDescriptor();
        descriptor2.setMaxAngle(max);
        descriptor2.splitAngle(2);
        descriptor2.splitAngle(max - 2);
        descriptor2.splitAngle(12);
        descriptor2.splitAngle(max - 12);
        descriptor2.splitAngle(30);
        descriptor2.splitAngle(max - 30);
        PolarGridDescriptor descriptor3 = new PolarGridDescriptor();
        descriptor3.setMaxAngle(max);
        descriptor3.splitAngle(2);
        descriptor3.splitAngle(max - 2);
        descriptor3.splitAngle(8);
        descriptor3.splitAngle(max - 8);
        descriptor3.splitAngle(16);
        descriptor3.splitAngle(max - 16);
        descriptor3.splitAngle(26);
        descriptor3.splitAngle(max - 26);
        descriptor3.splitDistance(Constants.STONE_RADIUS * 5);
        descriptor3.splitDistance(Constants.STONE_RADIUS * 10);
        List<QLearningPolarGunnerAgent> result = new ArrayList<>();
        result.add(new QLearningPolarGunnerAgent(descriptor1, new Eps(), 0.3, 0.995));
        result.add(new QLearningPolarGunnerAgent(descriptor2, new Eps(), 0.3, 0.995));
        result.add(new QLearningPolarGunnerAgent(descriptor3, new Eps(), 0.3, 0.995));
        return result;
    }

    private static class Eps implements Callable<Double> {
        private long counter;
        @Override
        public Double call() {
            counter++;
            return 1.0 / (1.0 + (19.0 * counter)/(Constants.TURNS_IN_GAME * GAME_COUNT));
        }
    }
}

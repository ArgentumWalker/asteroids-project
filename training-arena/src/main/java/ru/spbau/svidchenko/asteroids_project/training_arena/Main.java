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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final long GAME_COUNT = 40;
    private static final AgentsBuilder AGENTS_BUILDER = new AgentsBuilder();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<GunnerAgent> gunnerAgents = AGENTS_BUILDER.getDefaultGunnerAgents();
        List<PilotAgent> pilotAgents = AGENTS_BUILDER.getPilotAgents(GAME_COUNT);
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
}

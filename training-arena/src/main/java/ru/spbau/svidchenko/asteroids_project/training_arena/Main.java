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

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final long GAME_COUNT = 1200;
    private static final AgentsBuilder AGENTS_BUILDER = new AgentsBuilder();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<GunnerAgent> gunnerAgents = AGENTS_BUILDER.getGunnerAgents(GAME_COUNT);
        List<PilotAgent> pilotAgents = AGENTS_BUILDER.getDefaultPilotAgents();//AGENTS_BUILDER.getPilotAgents(GAME_COUNT);
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT, executor,
                System.out::println, Main::saveStatistic);
        trainingPool.start();
        trainingPool.join();
        executor.shutdown();
        AgentSaveLoader.rewritePilots(pilotAgents);
        AgentSaveLoader.rewriteGunners(gunnerAgents);
        System.out.println("Completed");
    }

    public static void saveStatistic(TrainingPool.StatisticHolder statisticHolder) {
        try {
            File file = new File("agents/statistic/" + statisticHolder.agentName + ".stat");
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (PrintStream outputStream = new PrintStream(new FileOutputStream(file))) {
                for (HashMap.Entry<Long, Long> statisticEntry : statisticHolder.game2score.entrySet()) {
                    outputStream.println(statisticEntry.getKey().toString() + " " + statisticEntry.getValue().toString());
                }
            }
        } catch (Exception e) {}
    }
}

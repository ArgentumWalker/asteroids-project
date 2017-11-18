package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.AgentSaveLoader;
import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.polar_model.QLearningPolarPilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qlearning.sorted_model.QLearningSortedGunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.parameters_functions.ExplorationProbability1;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.pilot_agents.*;
import ru.spbau.svidchenko.asteroids_project.agentmodel.world_representation.polar_grid.PolarGridDescriptor;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Main {
    private static final long GAME_COUNT = 4000;
    private static final long STATISTIC_GAMES = 2000;
    private static final long TEST_GAME_COUNT = 300;
    private static final AgentsBuilder AGENTS_BUILDER = new AgentsBuilder();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<GunnerAgent> gunnerAgents = AGENTS_BUILDER.getGunnerAgents(GAME_COUNT);
        List<PilotAgent> pilotAgents = AGENTS_BUILDER.getDefaultPilotAgents();
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT + STATISTIC_GAMES,
                GAME_COUNT / STATISTIC_GAMES, executor,
                System.out::println, buildStatisticSaver("test23/"), false, true);
        trainingPool.start();
        trainingPool.join();
        AgentSaveLoader.rewriteGunners(gunnerAgents);

        /*List<Pair<List<PilotAgent>, String>> testPilots = new ArrayList<>();
        testPilots.add(Pair.of(AGENTS_BUILDER.getDefaultPilotAgents(), "FlyForward"));
        testPilots.add(Pair.of(AGENTS_BUILDER.getAlternateDefaultPilotAgents(), "DoNothing"));
        List<PilotAgent> all = new ArrayList<>();
        all.addAll(AGENTS_BUILDER.getDefaultPilotAgents());
        all.addAll(AGENTS_BUILDER.getAlternateDefaultPilotAgents());


        gunnerAdaptationTest("test20", AGENTS_BUILDER.getGunnerAgents(GAME_COUNT),
                AGENTS_BUILDER.getDefaultPilotAgents(), testPilots, executor);
        gunnerAdaptationTest("test21", AGENTS_BUILDER.getGunnerAgents(GAME_COUNT),
                AGENTS_BUILDER.getAlternateDefaultPilotAgents(), testPilots, executor);
        gunnerAdaptationTest("test22", AGENTS_BUILDER.getGunnerAgents(GAME_COUNT),
                all, testPilots, executor);*/
        executor.shutdown();
        System.out.println("Completed");
    }

    public static void gunnerAdaptationTest (
            String test,
            List<GunnerAgent> gunners,
            List<PilotAgent> learningPilots,
            List<Pair<List<PilotAgent>, String>> testPilots,
            ExecutorService executor
    ) throws IOException, ClassNotFoundException {
        //Test learning
        List<GunnerAgent> gunnerAgents = gunners;
        List<PilotAgent> pilotAgents = learningPilots;
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT + STATISTIC_GAMES,
                GAME_COUNT / STATISTIC_GAMES, executor,
                System.out::println, buildStatisticSaver(test + "/learning"), false, true);
        trainingPool.start();
        trainingPool.join();
        AgentSaveLoader.rewriteGunners(gunnerAgents);
        //Test adopt
        for (Pair<List<PilotAgent>, String> pilots : testPilots) {
            pilotAgents = pilots.first();
            gunnerAgents = AgentSaveLoader.loadGunners();
            gunnerAgents.forEach(GunnerAgent::disableLearning);
            trainingPool = new TrainingPool(gunnerAgents, pilotAgents, TEST_GAME_COUNT,
                    GAME_COUNT / STATISTIC_GAMES, executor,
                    System.out::println, buildStatisticSaver(test + "/" + pilots.second() + "_withoutLearning"),
                    false, false);
            trainingPool.start();
            trainingPool.join();
            gunnerAgents.forEach(GunnerAgent::enableLearning);
            trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT + STATISTIC_GAMES,
                    GAME_COUNT / STATISTIC_GAMES, executor,
                    System.out::println, buildStatisticSaver(test + "/" + pilots.second() + "_withLearning"),
                    false, true
            );
            trainingPool.start();
            trainingPool.join();
        }
    }

    public static Consumer<TrainingPool.StatisticHolder> buildStatisticSaver(String testName) {
        return (TrainingPool.StatisticHolder holder) -> {
            try {
                File file = new File("agents/" + testName + "/statistic/" + holder.agentName + ".stat");
                file.getParentFile().mkdirs();
                file.createNewFile();
                try (PrintStream outputStream = new PrintStream(new FileOutputStream(file))) {
                    for (HashMap.Entry<Long, Long> statisticEntry : holder.game2score.entrySet()) {
                        outputStream.println(statisticEntry.getKey().toString() + " " + statisticEntry.getValue().toString());
                    }
                }
            } catch (Exception e) {}
        };
    }
}

package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.AgentSaveLoader;
import ru.spbau.svidchenko.asteroids_project.agentmodel.AgentsBuilder;
import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.simple_testing_agents.gunner_agents.*;
import ru.spbau.svidchenko.asteroids_project.commons.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Main {
    private static final long GAME_COUNT = 6000;
    private static final long STATISTIC_GAMES = 1000;
    private static final long TEST_GAME_COUNT = 140;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<PilotAgent> loadedPilots = AgentsBuilder.getImprovedPilotAgents();
        /*loadedPilots.add(AgentSaveLoader.loadPilots("learnedPilots/").get(0));
        loadedPilots.add(AgentSaveLoader.loadPilots("learnedPilotsAlt/").get(3));
        loadedPilots.forEach(PilotAgent::disableLearning);*/

        List<PilotAgent> learningPilots = new ArrayList<>();
        List<Pair<List<PilotAgent>, String>> testPilots = new ArrayList<>();
        //for (PilotAgent agent : loadedPilots) {
        //    testPilots.add(Pair.of(Collections.singletonList(agent), agent.getName()));
        //}

        learningPilots.clear();
        learningPilots.addAll(loadedPilots);

        standartGunnerTest("test38/standard", learningPilots, testPilots, executor);
        //TEST 1: LEARNING WITH ALL
        /*learningPilots.clear();
        learningPilots.addAll(loadedPilots);
        gunnerAdaptationTest("test37/try2",
                //AGENTS_BUILDER.getSortedQLearningGunners(GAME_COUNT, AGENTS_BUILDER.getSortedDescriptors1()),
                AGENTS_BUILDER.getQNetSelectiveGunners(),
                learningPilots, testPilots, executor);*/
        /*//TEST 2: NO FORWARD
        learningPilots.clear();
        learningPilots.add(loadedPilots.get(0));
        learningPilots.add(loadedPilots.get(1));
        gunnerAdaptationTest("test36/NoForward",
                AGENTS_BUILDER.getSortedQLearningGunners(GAME_COUNT, AGENTS_BUILDER.getSortedDescriptors1()),
                learningPilots, testPilots, executor);
        //TEST 3: NO STAY
        learningPilots.clear();
        learningPilots.add(loadedPilots.get(2));
        learningPilots.add(loadedPilots.get(1));
        gunnerAdaptationTest("test36/NoStay",
                AGENTS_BUILDER.getSortedQLearningGunners(GAME_COUNT, AGENTS_BUILDER.getSortedDescriptors1()),
                learningPilots, testPilots, executor);
        //TEST 4: NO NORMAL
        learningPilots.clear();
        learningPilots.add(loadedPilots.get(2));
        learningPilots.add(loadedPilots.get(0));
        gunnerAdaptationTest("test36/NoNormal",
                AGENTS_BUILDER.getSortedQLearningGunners(GAME_COUNT, AGENTS_BUILDER.getSortedDescriptors1()),
                learningPilots, testPilots, executor);
        */
        executor.shutdown();
        System.out.println("Completed");
    }


    public static void simplePilotTest (
            String test,
            List<GunnerAgent> gunnerAgents,
            List<PilotAgent> pilotAgents,
            ExecutorService executor,
            boolean resetExploration
    ) {
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT + STATISTIC_GAMES,
                GAME_COUNT / STATISTIC_GAMES, executor,
                System.out::println, buildStatisticSaver(test + "/learning"), true, true,
                resetExploration);
        trainingPool.start();
        trainingPool.join();
    }

    public static void simpleGunnerTest (
            String test,
            List<GunnerAgent> gunnerAgents,
            List<PilotAgent> pilotAgents,
            ExecutorService executor,
            boolean resetExploration
    ) {
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT + STATISTIC_GAMES,
                GAME_COUNT / STATISTIC_GAMES, executor,
                System.out::println, buildStatisticSaver(test + "/learning"), false, true,
                resetExploration);
        trainingPool.start();
        trainingPool.join();
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
                System.out::println, buildStatisticSaver(test + "/learning"), false, true, false);
        trainingPool.start();
        trainingPool.join();
        AgentSaveLoader.rewriteGunners(gunnerAgents);
        //Test adopt
        for (Pair<List<PilotAgent>, String> pilots : testPilots) {
            pilotAgents = pilots.first();
            gunnerAgents = AgentSaveLoader.loadGunners();
            gunnerAgents.forEach(GunnerAgent::disableLearning);
            trainingPool = new TrainingPool(gunnerAgents, pilotAgents, TEST_GAME_COUNT,
                    1, executor,
                    System.out::println, buildStatisticSaver(test + "/" + pilots.second() + "_withoutLearning"),
                    false, false, false);
            trainingPool.start();
            trainingPool.join();
            gunnerAgents.forEach(GunnerAgent::enableLearning);
            /*trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT + STATISTIC_GAMES,
                    GAME_COUNT / STATISTIC_GAMES, executor,
                    System.out::println, buildStatisticSaver(test + "/" + pilots.second() + "_withLearning"),
                    false, true, false
            );
            trainingPool.start();
            trainingPool.join();
            gunnerAgents = AgentSaveLoader.loadGunners();*/
            gunnerAgents.forEach(GunnerAgent::resetLearningProbability);
            trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT + STATISTIC_GAMES,
                    GAME_COUNT / STATISTIC_GAMES, executor,
                    System.out::println, buildStatisticSaver(test + "/" + pilots.second() + "_withResetLearning"),
                    false, true, false
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

    public static void standartGunnerTest (
            String test,
            List<PilotAgent> learningPilots,
            List<Pair<List<PilotAgent>, String>> testPilots,
            ExecutorService executor
    ) throws IOException, ClassNotFoundException {
        //Test learning
        List<GunnerAgent> gunnerAgents = Collections.singletonList(new ShootClosestGunnerAgent());
        List<PilotAgent> pilotAgents = learningPilots;
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT,
                1, executor,
                System.out::println, buildStatisticSaver(test + "/learning"),
                false, false, false);
        trainingPool.start();
        trainingPool.join();
        AgentSaveLoader.rewriteGunners(gunnerAgents);
        //Test adopt
        for (Pair<List<PilotAgent>, String> pilots : testPilots) {
            pilotAgents = pilots.first();
            gunnerAgents = AgentSaveLoader.loadGunners();
            gunnerAgents.forEach(GunnerAgent::disableLearning);
            trainingPool = new TrainingPool(gunnerAgents, pilotAgents, TEST_GAME_COUNT,
                    1, executor,
                    System.out::println, buildStatisticSaver(test + "/" + pilots.second() + "_withoutLearning"),
                    false, false, false);
            trainingPool.start();
            trainingPool.join();

            gunnerAgents.forEach(GunnerAgent::enableLearning);
            gunnerAgents.forEach(GunnerAgent::resetLearningProbability);
            trainingPool = new TrainingPool(gunnerAgents, pilotAgents, GAME_COUNT,
                    1, executor,
                    System.out::println, buildStatisticSaver(test + "/" + pilots.second() + "_withResetLearning"),
                    false, true, false
            );
            trainingPool.start();
            trainingPool.join();
        }
    }
}

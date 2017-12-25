package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.commons.Constants;
import ru.spbau.svidchenko.asteroids_project.commons.RandomGod;
import ru.spbau.svidchenko.asteroids_project.game_logic.WorldDescriptor;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.ShipCrew;
import ru.spbau.svidchenko.asteroids_project.standalone_game_client.TrainingExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class TrainingPool {
    private final RandomGod random = RandomGod.ask;
    private final List<GunnerAgent> gunnerAgents;
    private final List<PilotAgent> pilotAgents;
    private final ConcurrentHashMap<GunnerAgent, AtomicLong> gunnerAgent2gameCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<PilotAgent, AtomicLong> pilotAgent2gameCount = new ConcurrentHashMap<>();
    private final ExecutorService executor;
    private final long gamesPerAgent;
    private final long learningGamesPerStatisticGame;
    private final boolean pilots;
    private final boolean disableLearningForStatistic;
    private final boolean resetExploration;
    private final Consumer<String> logFunction;
    private final Consumer<StatisticHolder> statisticFunction;

    public TrainingPool(
            List<GunnerAgent> gunnerAgents,
            List<PilotAgent> pilotAgents,
            long gamesPerAgent,
            long learningGamesPerStatisticGame,
            ExecutorService executor,
            Consumer<String> logFunction,
            Consumer<StatisticHolder> statisticFunction,
            boolean pilots,
            boolean disableLearningForStatistic,
            boolean resetExploration
    ) {
        this.disableLearningForStatistic = disableLearningForStatistic;
        this.learningGamesPerStatisticGame = learningGamesPerStatisticGame;
        this.pilots = pilots;
        this.executor = executor;
        this.gunnerAgents = gunnerAgents;
        this.pilotAgents = pilotAgents;
        this.gamesPerAgent = gamesPerAgent;
        this.logFunction = logFunction;
        this.statisticFunction = statisticFunction;
        this.resetExploration = resetExploration;
    }

    public void start() {
        if (pilots) {
            for (PilotAgent agent : pilotAgents) {
                pilotAgent2gameCount.put(agent, new AtomicLong(0));
            }
            for (PilotAgent agent : pilotAgents) {
                new RestartPilot(agent).run();
            }
        } else {
            for (GunnerAgent agent : gunnerAgents) {
                gunnerAgent2gameCount.put(agent, new AtomicLong(0));
            }
            for (GunnerAgent agent : gunnerAgents) {
                new RestartGunner(agent).run();
            }
        }
    }

    public void join() {
        if (pilots) {
            for (PilotAgent agent : pilotAgents) {
                boolean completed = false;
                while (!completed) {
                    completed = pilotAgent2gameCount.get(agent).get() > gamesPerAgent;
                    if (!completed) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(3000);
                        } catch (InterruptedException e) {}
                    }
                }
            }
        } else {
            for (GunnerAgent agent : gunnerAgents) {
                boolean completed = false;
                while (!completed) {
                    completed = gunnerAgent2gameCount.get(agent).get() > gamesPerAgent;
                    if (!completed) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(3000);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }
    }

    private class RestartGunner implements Runnable {
        private final GunnerAgent agent;
        private final HashMap<Long, Long> scoreStatistic = new HashMap<>();
        private ShipCrew currentShipCrew;
        private boolean isStatisticGame = false;
        private long lastStartTime = System.currentTimeMillis();
        private double avg = -1e5;
        private double sum = 0.0;
        private long cnt = 0;

        RestartGunner(GunnerAgent agent) {
            this.agent = agent;
        }

        @Override
        public void run() {
            long game = gunnerAgent2gameCount.get(agent).incrementAndGet();
            if (game <= gamesPerAgent) {
                String scoreStat = "";
                if (currentShipCrew != null) {
                    if (isStatisticGame || !disableLearningForStatistic) {
                        scoreStat = ", Score = " + currentShipCrew.getScore();
                        sum += currentShipCrew.getScore();
                        scoreStatistic.put(game - cnt, currentShipCrew.getScore());
                        if (disableLearningForStatistic) {
                            isStatisticGame = false;
                            agent.enableLearning();
                        }
                        if (resetExploration && cnt % 50 == 0) {
                            double newAvg = sum / 50;
                            sum = 0;
                            if (Math.abs(avg - newAvg) < 1.0e3) {
                                agent.resetLearningProbability();
                            }
                            avg = newAvg;
                        }
                    }
                }
                logFunction.accept("Gunner agent " + agent.getName()
                        + " starts " + game + " game. Execution time = "
                        + (System.currentTimeMillis() - lastStartTime) + " ms" + scoreStat);
                PilotAgent pilot;
                if (disableLearningForStatistic && game % (learningGamesPerStatisticGame + 1) == 0) {
                    isStatisticGame = true;
                    cnt++;
                    pilot = pilotAgents.get((int)(cnt) % pilotAgents.size());
                    agent.disableLearning();
                } else {
                    pilot = pilotAgents.get((int)(game - cnt) % pilotAgents.size());
                }

                WorldDescriptor worldDescriptor = new WorldDescriptor();
                currentShipCrew = new ShipCrew(pilot.buildPlayer(1), agent.buildPlayer(2));
                worldDescriptor.players.add(currentShipCrew);

                executor.execute(new TrainingExecutor(
                        worldDescriptor,
                        Constants.TURNS_IN_GAME,
                        RestartGunner.this
                ));
                lastStartTime = System.currentTimeMillis();
            } else {
                StatisticHolder statisticHolder = new StatisticHolder();
                statisticHolder.agentName = agent.getName();
                statisticHolder.game2score = scoreStatistic;
                statisticFunction.accept(statisticHolder);
            }
        }
    }

    private class RestartPilot implements Runnable {
        private final PilotAgent agent;
        private final HashMap<Long, Long> scoreStatistic = new HashMap<>();
        private ShipCrew currentShipCrew;
        private boolean isStatisticGame = false;
        private long lastStartTime = System.currentTimeMillis();
        private double avg = -1e5;
        private double sum = 0.0;
        private long cnt = 0;

        RestartPilot(PilotAgent agent) {
            this.agent = agent;
        }

        @Override
        public void run() {
            long game = pilotAgent2gameCount.get(agent).incrementAndGet();
            if (game <= gamesPerAgent) {
                String scoreStat = "";
                if (currentShipCrew != null) {
                    if (isStatisticGame || !disableLearningForStatistic) {
                        scoreStat = ", Score = " + currentShipCrew.getScore();
                        sum += currentShipCrew.getScore();
                        scoreStatistic.put(game - cnt, currentShipCrew.getScore());
                        if (disableLearningForStatistic) {
                            isStatisticGame = false;
                            agent.enableLearning();
                        }
                        if (resetExploration && cnt % 50 == 0) {
                            double newAvg = sum / 50;
                            sum = 0;
                            if (Math.abs(avg - newAvg) < 1.0e3) {
                                agent.resetLearningProbability();
                            }
                            avg = newAvg;
                        }
                    }
                }
                logFunction.accept("Pilot agent " + agent.getName()
                        + " starts " + game + " game. Execution time = "
                        + (System.currentTimeMillis() - lastStartTime) + " ms" + scoreStat);
                GunnerAgent gunner;
                if (disableLearningForStatistic && game % (learningGamesPerStatisticGame + 1) == 0) {
                    isStatisticGame = true;
                    cnt++;
                    gunner = gunnerAgents.get((int)(cnt) % gunnerAgents.size());
                    agent.disableLearning();
                } else {
                    gunner = gunnerAgents.get((int)(game - cnt) % gunnerAgents.size());
                }

                WorldDescriptor worldDescriptor = new WorldDescriptor();
                currentShipCrew = new ShipCrew(agent.buildPlayer(1), gunner.buildPlayer(2));
                worldDescriptor.players.add(currentShipCrew);

                executor.execute(new TrainingExecutor(
                        worldDescriptor,
                        Constants.TURNS_IN_GAME,
                        RestartPilot.this
                ));
                lastStartTime = System.currentTimeMillis();
            } else {
                StatisticHolder statisticHolder = new StatisticHolder();
                statisticHolder.agentName = agent.getName();
                statisticHolder.game2score = scoreStatistic;
                statisticFunction.accept(statisticHolder);
            }
        }
    }

    public static class StatisticHolder {
        public String agentName;
        public HashMap<Long, Long> game2score;
    }
}

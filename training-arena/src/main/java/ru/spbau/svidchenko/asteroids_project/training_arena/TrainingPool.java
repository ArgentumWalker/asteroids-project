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
    private final Consumer<String> logFunction;

    public TrainingPool(
            List<GunnerAgent> gunnerAgents,
            List<PilotAgent> pilotAgents,
            long gamesPerAgent,
            ExecutorService executor,
            Consumer<String> logFunction
    ) {
        this.executor = executor;
        this.gunnerAgents = gunnerAgents;
        this.pilotAgents = pilotAgents;
        this.gamesPerAgent = gamesPerAgent;
        this.logFunction = logFunction;
    }

    public void start() {
        for (GunnerAgent agent : gunnerAgents) {
            gunnerAgent2gameCount.put(agent, new AtomicLong(0));
        }
        for (PilotAgent agent : pilotAgents) {
            pilotAgent2gameCount.put(agent, new AtomicLong(0));
        }
        for (GunnerAgent agent : gunnerAgents) {
            new RestartGunner(agent).run();
        }
        for (PilotAgent agent : pilotAgents) {
            new RestartPilot(agent).run();
        }
    }

    public void join() {
        for (GunnerAgent agent : gunnerAgents) {
            boolean completed = false;
            while (!completed) {
                completed = gunnerAgent2gameCount.get(agent).get() == gamesPerAgent + 1;
                if (!completed) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException e) {}
                }
            }
        }
        for (PilotAgent agent : pilotAgents) {
            boolean completed = false;
            while (!completed) {
                completed = pilotAgent2gameCount.get(agent).get() == gamesPerAgent + 1;
                if (!completed) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException e) {}
                }
            }
        }
    }

    private class RestartGunner implements Runnable {
        private final GunnerAgent agent;
        private long lastStartTime = System.currentTimeMillis();

        RestartGunner(GunnerAgent agent) {
            this.agent = agent;
        }

        @Override
        public void run() {
            if (gunnerAgent2gameCount.get(agent).getAndIncrement() <= gamesPerAgent) {
                WorldDescriptor worldDescriptor = new WorldDescriptor();
                worldDescriptor.players.add(new ShipCrew(random.chooseRandom(pilotAgents).buildPlayer(1), agent.buildPlayer(2)));
                logFunction.accept("Gunner agent " + agent.getName()
                        + " starts " + gunnerAgent2gameCount.get(agent).get() + " game. Execution time = "
                        + (System.currentTimeMillis() - lastStartTime) + " ms");
                lastStartTime = System.currentTimeMillis();
                executor.execute(new TrainingExecutor(
                        worldDescriptor,
                        Constants.TURNS_IN_GAME,
                        RestartGunner.this
                ));
            }
        }
    }

    private class RestartPilot implements Runnable {
        private final PilotAgent agent;
        private long lastStartTime = System.currentTimeMillis();

        RestartPilot(PilotAgent agent) {
            this.agent = agent;
        }

        @Override
        public void run() {
            if (pilotAgent2gameCount.get(agent).incrementAndGet() <= gamesPerAgent) {
                WorldDescriptor worldDescriptor = new WorldDescriptor();
                worldDescriptor.players.add(new ShipCrew(agent.buildPlayer(1), random.chooseRandom(gunnerAgents).buildPlayer(2)));
                logFunction.accept("Pilot agent " + agent.getName()
                        + " starts " + pilotAgent2gameCount.get(agent).get() + " game. Execution time = "
                        + (System.currentTimeMillis() - lastStartTime) + " ms");
                lastStartTime = System.currentTimeMillis();
                executor.execute(new TrainingExecutor(
                        worldDescriptor,
                        Constants.TURNS_IN_GAME,
                        RestartPilot.this
                ));
            }
        }
    }
}

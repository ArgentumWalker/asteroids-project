package ru.spbau.svidchenko.asteroids_project.training_arena;

import ru.spbau.svidchenko.asteroids_project.agentmodel.AgentSaveLoader;
import ru.spbau.svidchenko.asteroids_project.agentmodel.GunnerAgent;
import ru.spbau.svidchenko.asteroids_project.agentmodel.PilotAgent;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.GunnerPlayer;
import ru.spbau.svidchenko.asteroids_project.game_logic.player.PilotPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<GunnerAgent> gunnerAgents = getGunnerrAgents();
        List<PilotAgent> pilotAgents = getPilotAgents();
        TrainingPool trainingPool = new TrainingPool(gunnerAgents, pilotAgents, 100, executor, System.out::println);
        trainingPool.start();
        trainingPool.join();
        AgentSaveLoader.savePilots(pilotAgents);
        AgentSaveLoader.saveGunners(gunnerAgents);
        System.out.println("Completed");
    }

    private static List<PilotAgent> getPilotAgents() {
        List<PilotAgent> result = new ArrayList<>();
        result.add(new PilotAgent() {
            @Override
            public String getName() {
                return "Tmp";
            }

            @Override
            public PilotPlayer buildPlayer(long id) {
                return new PilotPlayer(id) {
                    @Override
                    protected Action chooseAction() {
                        return new Action();
                    }
                };
            }
        });
        return result;
    }

    private static List<GunnerAgent> getGunnerrAgents() {
        List<GunnerAgent> result = new ArrayList<>();
        result.add(new GunnerAgent() {
            @Override
            public String getName() {
                return "Tmp";
            }

            @Override
            public GunnerPlayer buildPlayer(long id) {
                return new GunnerPlayer(id) {
                    @Override
                    protected Action chooseAction() {
                        return new Action();
                    }
                };
            }
        });
        return result;
    }
}

package ru.spbau.svidchenko.asteroids_project.agentmodel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AgentSaveLoader {
    private static final String PILOTS_FILE = "agents/pilots.ag";
    private static final String GUNNERS_FILE = "agents/gunners.ag";

    public static void savePilots(List<PilotAgent> agents) throws IOException {
        List<PilotAgent> oldAgents = new ArrayList<>();
        try {
            oldAgents = loadPilots();
        } catch (Exception e) {
            //Well, we'll rewrite file
            File pilotsFile = new File(PILOTS_FILE);
            pilotsFile.getParentFile().mkdirs();
            pilotsFile.createNewFile();
        }
        oldAgents.addAll(agents);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(PILOTS_FILE, false))) {
            objectOutputStream.writeLong(oldAgents.size());
            for (PilotAgent agent : oldAgents) {
                objectOutputStream.writeObject(agent);
            }
        }
    }

    public static void saveGunners(List<GunnerAgent> agents) throws IOException {
        List<GunnerAgent> oldAgents = new ArrayList<>();
        try {
            oldAgents = loadGunners();
        } catch (Exception e) {
            //Well, we'll rewrite file
            File gunnersFile = new File(GUNNERS_FILE);
            gunnersFile.getParentFile().mkdirs();
            gunnersFile.createNewFile();
        }
        oldAgents.addAll(agents);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(GUNNERS_FILE, false))) {
            objectOutputStream.writeLong(oldAgents.size());
            for (GunnerAgent agent : oldAgents) {
                objectOutputStream.writeObject(agent);
            }
        }
    }

    public static List<PilotAgent> loadPilots() throws IOException, ClassNotFoundException {
        List<PilotAgent> agents = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(PILOTS_FILE))) {
            long count = objectInputStream.readLong();
            for (long i = 0; i < count; i++) {
                agents.add((PilotAgent) objectInputStream.readObject());
            }
            return agents;
        }
    }

    public static List<GunnerAgent> loadGunners() throws IOException, ClassNotFoundException {
        List<GunnerAgent> agents = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(GUNNERS_FILE))) {
            long count = objectInputStream.readLong();
            for (long i = 0; i < count; i++) {
                agents.add((GunnerAgent) objectInputStream.readObject());
            }
            return agents;
        }
    }
}

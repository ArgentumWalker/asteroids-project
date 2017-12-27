package ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.selective_model;

import ru.spbau.svidchenko.asteroids_project.agentmodel.learning_methods.qnetwork.QNet;
import ru.spbau.svidchenko.asteroids_project.commons.Callable;
import ru.spbau.svidchenko.asteroids_project.commons.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SelectiveQNet extends QNet {
    private final int featureProductions;

    public SelectiveQNet(
            Callable<Double> explorationProbability,
            Callable<Double> alpha,
            double gamma,
            int actionCount,
            int powersCount,
            Callable<Boolean> isLearningDisabled,
            int featureProductions
    ) {
        super(explorationProbability, alpha, gamma, actionCount,
                powSum(powersCount * powerFeatures.size(), featureProductions + 1), isLearningDisabled);
        this.featureProductions = featureProductions;
    }

    public void refresh(double reward, int action, List<Point> oldState, List<Point> newState) {
        refreshNet(reward, action, getCommonState(oldState), getCommonState(newState));
    }

    public int getAction(List<Point> state) {
        return getNetAction(getCommonState(state));
    }

    public List<Double> getCommonState(List<Point> state) {
        List<Double> realState = new ArrayList<>();
        List<Double> features = new ArrayList<>();
        state.forEach(p -> features.addAll(getPowerFeatures(p)));
        getProds(features, realState, 0, 1.);
        return realState;
    }

    private void getProds(List<Double> features, List<Double> result, int deep, double currentProd) {
        result.add(currentProd);
        if (deep > featureProductions) {
            return;
        }
        for (double feature : features) {
            getProds(features, result, deep + 1, currentProd * feature);
        }
    }

    private List<Double> getPowerFeatures(Point power) {
        List<Double> result = new ArrayList<>();
        PowerBaseFeatures features = new PowerBaseFeatures(power);
        powerFeatures.forEach(lambda -> result.add(lambda.apply(features)));
        return result;
    }

    private static int powSum(int q, int p) {
        int result = 1;
        int pow = 1;
        for (int i = 0; i < p; i++) {
            pow *= q;
            result += pow;
        }
        return result;
    }
    private final static List<Function<PowerBaseFeatures, Double>> powerFeatures = Arrays.asList(
            power -> power.angle,
            power -> power.angleAbs,
            power -> power.angle * power.angleAbs,
            //power -> power.angle * power.angleAbs * power.angleAbs,
            power -> power.x,
            power -> power.y,
            power -> power.x * power.x,
            power -> power.y * power.y,
            power -> power.xabs,
            power -> power.yabs,
            power -> power.x * power.xabs,
            power -> power.y * power.yabs,
            //power -> power.x * power.xabs * power.xabs,
            //power -> power.y * power.yabs * power.yabs,
            power -> power.size,
            power -> power.cos,
            power -> power.sin,
            power -> power.cosAbs,
            power -> power.sinAbs,
            power -> power.cos * power.cos,
            power -> power.sin * power.sin,
            power -> power.cos * power.cosAbs,
            power -> power.sin * power.sinAbs,
            //power -> power.cos * power.cosAbs * power.cosAbs,
            //power -> power.sin * power.sinAbs * power.sinAbs,
            power -> power.absCos,
            power -> power.absCosAbs,
            power -> power.angleAbs > 1e-10 ? 1. / power.angle : 0,
            power -> power.cosAbs > 1e-10 ? 1. / power.cos : 0,
            power -> power.sinAbs > 1e-10 ? 1. / power.sin : 0,
            power -> power.angleAbs > 1e-10 ? power.size / power.angle : 0,
            power -> power.cosAbs > 1e-10 ? power.size / power.cos : 0,
            power -> power.sinAbs > 1e-10 ? power.size / power.sin : 0,
            //power -> power.angleAbs > 1e-10 ? power.size * power.size / power.angle : 0,
            //power -> power.cosAbs > 1e-10 ? power.size * power.size / power.cos : 0,
            //power -> power.sinAbs > 1e-10 ? power.size * power.size / power.sin : 0,
            power -> power.angleAbs > 1e-10 ? power.xabs / power.angle : 0,
            power -> power.cosAbs > 1e-10 ? power.xabs / power.cos : 0,
            power -> power.sinAbs > 1e-10 ? power.xabs / power.sin : 0,
            //power -> power.angleAbs > 1e-10 ? power.xabs * power.xabs / power.angle : 0,
            ///power -> power.cosAbs > 1e-10 ? power.xabs * power.xabs / power.cos : 0,
            //power -> power.sinAbs > 1e-10 ? power.xabs * power.xabs / power.sin : 0,
            power -> power.angleAbs > 1e-10 ? power.yabs / power.angle : 0,
            power -> power.cosAbs > 1e-10 ? power.yabs / power.cos : 0,
            power -> power.sinAbs > 1e-10 ? power.yabs / power.sin : 0
            //power -> power.angleAbs > 1e-10 ? power.yabs * power.yabs / power.angle : 0,
            //power -> power.cosAbs > 1e-10 ? power.yabs * power.yabs / power.cos : 0,
            //power -> power.sinAbs > 1e-10 ? power.yabs * power.yabs / power.sin : 0
    );

    private class PowerBaseFeatures {
        public final double angle;
        public final double angleAbs;
        public final double x;
        public final double y;
        public final double xabs;
        public final double yabs;
        public final double size;
        public final double cos;
        public final double sin;
        public final double cosAbs;
        public final double sinAbs;
        public final double absCos;
        public final double absCosAbs;

        public PowerBaseFeatures(Point power) {
            angle = Math.atan2(power.getY(), power.getX());
            angleAbs = Math.abs(angle);
            x = power.getX();
            y = power.getY();
            xabs = Math.abs(power.getX());
            yabs = Math.abs(power.getY());
            size = Math.sqrt(x * x + y * y);
            cos = Math.cos(angle);
            sin = Math.sin(angle);
            cosAbs = Math.abs(cos);
            sinAbs = Math.abs(sin);
            absCos = Math.cos(angleAbs);
            absCosAbs = Math.abs(absCos);
        }
    }
}

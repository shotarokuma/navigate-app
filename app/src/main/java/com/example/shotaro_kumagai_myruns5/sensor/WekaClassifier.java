package com.example.shotaro_kumagai_myruns5.sensor;

class WekaClassifier {

    public static double classify(Object[]i)
            throws Exception {
        double p = Double.NaN;
        p = WekaClassifier._classify(i);
        return p;
    }
    private static double _classify(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 4.573082) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() > 4.573082) {
            p = 1;
        }
        return p;
    }
}
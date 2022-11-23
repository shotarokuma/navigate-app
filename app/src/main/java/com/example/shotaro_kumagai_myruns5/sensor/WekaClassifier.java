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
            p = 0;
        } else if (((Double) i[64]).doubleValue() <= 4.573082) {
            p = 0;
        } else if (((Double) i[64]).doubleValue() > 4.573082 && ((Double) i[64]).doubleValue() < 9.146164  ) {
            p = 1;
        }else if( ((Double) i[64]).doubleValue() > 9.146164){
            p = 2;
        }
        return p;
    }
}
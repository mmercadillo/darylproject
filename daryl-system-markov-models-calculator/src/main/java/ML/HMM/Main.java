package ML.HMM;

import java.util.Hashtable;
import java.util.Vector;

import DA.Processing.DataDecoding;
import Util.Parser.JsonParser;

public class Main {
    public static void main(String[] args) throws Exception {
        JsonParser jp = new JsonParser("F:\\Hidden-Markov-Model-master\\Resources\\test_HMM.json");
        
        String name = DataDecoding.getInstance().getModelName(jp.getName());
        Vector<String> states = DataDecoding.getInstance().getStates(jp.getStates());
        Vector<String> observations = DataDecoding.getInstance().getObservations(jp.getObservations());
        Hashtable<String, Double> initialProbabilities = DataDecoding.getInstance().getInitialProbabilities(jp.getInitialProbabilities());
        Hashtable<Pair<String, String>, Double> transitionMatrix = DataDecoding.getInstance().getTransitionMatrix(jp.getTransitionMatrix());
        Hashtable<Pair<String, String>, Double> emissionMatrix = DataDecoding.getInstance().getEmissionMatrix(jp.getEmissionMatrix());

        HiddenMarkovModel hmm = new HiddenMarkovModel(name, states, observations, initialProbabilities, transitionMatrix, emissionMatrix);
        Vector<String>sampleStates = new Vector<String>();
        sampleStates.add("HL");
        sampleStates.add("LH");
        sampleStates.add("LH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        sampleStates.add("HL");
        sampleStates.add("LH");
        sampleStates.add("LH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        sampleStates.add("HL");
        sampleStates.add("LH");
        sampleStates.add("LH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        sampleStates.add("HL");
        sampleStates.add("LH");
        sampleStates.add("LH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        sampleStates.add("HH");
        //        sampleStates.add("HH");
//        sampleStates.add("LL");
//        sampleStates.add("HL");
//        sampleStates.add("LH");
//        sampleStates.add("LH");
        



        Vector<String> sampleO = new Vector<String>();
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PU");
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PD");
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PU");
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PD");
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PU");
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PD");
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PU");
        sampleO.add("PD");
        sampleO.add("PU");
        sampleO.add("PD");
        //        sampleO.add("PU");
//        sampleO.add("PD");
//        sampleO.add("PD");
//        sampleO.add("PU");
//        sampleO.add("PU");


        System.out.println(hmm.getInitialProbabilities());
        System.out.println(hmm.getTransitionMatrix());
        System.out.println(hmm.getEmissionMatrix());
        System.out.println(hmm.getOptimalStateSequenceUsingViterbiAlgorithm(states, sampleO));
        
        
        System.out.println(hmm.evaluateUsingBruteForce(sampleStates, sampleO));
        System.out.println(hmm.evaluateUsingForwardAlgorithm(sampleStates, sampleO));
        System.out.println(hmm.getOptimalStateSequenceUsingViterbiAlgorithm(sampleStates, sampleO));
        hmm.estimateParametersUsingBaumWelchAlgorithm(sampleStates, sampleO, false);
        System.out.println(hmm.getInitialProbabilities());
        System.out.println(hmm.getTransitionMatrix());
        System.out.println(hmm.getEmissionMatrix());
        System.out.println(hmm.evaluateUsingBruteForce(sampleStates, sampleO));
        System.out.println(hmm.evaluateUsingForwardAlgorithm(sampleStates, sampleO));

    }

}

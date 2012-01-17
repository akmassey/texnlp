///////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2007 Jason Baldridge, The University of Texas at Austin
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
//////////////////////////////////////////////////////////////////////////////
package texnlp.taggers;

import texnlp.util.MathUtil;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;

/**
 * Wrapper for Counts object that permits counts only for the given set of tag
 * bigrams
 * 
 * @author Dan Garrette
 */
public class GrammarConstrainedCounts implements Counts {
    private Counts delegateCounts;

    protected TIntSet validInitials;
    protected TIntObjectMap<TIntSet> validTransitions;
    protected TIntSet validFinals;

    public GrammarConstrainedCounts(Counts delegateCounts, TIntSet validInitials,
            TIntObjectMap<TIntSet> validTransitions, TIntSet validFinals) {
        this.delegateCounts = delegateCounts;
        this.validInitials = validInitials;
        this.validTransitions = validTransitions;
        this.validFinals = validFinals;
    }

    private boolean validTagBigram(int a, int b) {
        TIntSet validBs = validTransitions.get(a);
        return validBs != null && validBs.contains(b);
    }

    @Override
    public Counts copy() {
        return new GrammarConstrainedCounts(delegateCounts.copy(), validInitials, validTransitions, validFinals);
    }

    @Override
    public void increment(String w) {
        delegateCounts.increment(w);
    }

    @Override
    public void addToSeenWords(String w) {
        delegateCounts.addToSeenWords(w);
    }

    @Override
    public void increment(String w, double amount) {
        delegateCounts.increment(w, amount);
    }

    @Override
    public void increment(int t_i, String w) {
        delegateCounts.increment(t_i, w);
    }

    @Override
    public double probTagGivenWord(int t_i, String w) {
        return delegateCounts.probTagGivenWord(t_i, w);
    }

    @Override
    public void increment(int t_i, String w, double amount) {
        delegateCounts.increment(t_i, w, amount);
    }

    @Override
    public int getNumEmissions(int t_i) {
        return delegateCounts.getNumEmissions(t_i);
    }

    @Override
    public void increment(int t_i) {
        delegateCounts.increment(t_i);
    }

    @Override
    public void increment(int t_i, double amount) {
        delegateCounts.increment(t_i, amount);
    }

    @Override
    public void increment(int t_i, int t_j) {
        if (validTagBigram(t_i, t_j))
            delegateCounts.increment(t_i, t_j);
    }

    @Override
    public void increment(int t_i, int t_j, double amount) {
        if (validTagBigram(t_i, t_j))
            delegateCounts.increment(t_i, t_j, amount);
    }

    @Override
    public void incrementInitial(int t_i) {
        if (validInitials.contains(t_i))
            delegateCounts.incrementInitial(t_i);
    }

    @Override
    public void incrementInitial(int t_i, double amount) {
        if (validInitials.contains(t_i))
            delegateCounts.incrementInitial(t_i, amount);
    }

    @Override
    public void incrementFinal(int t_i) {
        if (validFinals.contains(t_i))
            delegateCounts.incrementFinal(t_i);
    }

    @Override
    public void incrementFinal(int t_i, double amount) {
        if (validFinals.contains(t_i))
            delegateCounts.incrementFinal(t_i, amount);
    }

    @Override
    public void prepare() {
        delegateCounts.prepare();
    }

    private double[] zeroInvalidInitials(double[] dist) {
        for (int i = 0; i < dist.length; i++)
            if (!validInitials.contains(i))
                dist[i] = MathUtil.LOG_ZERO;
        return dist;
    }

    @Override
    public double[] getInitialLogDist() {
        return zeroInvalidInitials(delegateCounts.getInitialLogDist());
    }

    @Override
    public double[] getInitialLogDist(boolean startWithPrior) {
        return zeroInvalidInitials(delegateCounts.getInitialLogDist(startWithPrior));
    }

    private double[] zeroInvalidFinals(double[] dist) {
        for (int i = 0; i < dist.length; i++)
            if (!validFinals.contains(i))
                dist[i] = MathUtil.LOG_ZERO;
        return dist;
    }

    @Override
    public double[] getFinalLogDist() {
        return zeroInvalidFinals(delegateCounts.getFinalLogDist());
    }

    @Override
    public double[] getFinalLogDist(boolean startWithPrior) {
        return zeroInvalidFinals(delegateCounts.getFinalLogDist(startWithPrior));
    }

    private double[][] zeroInvalidTransitions(double[][] dist) {
        for (int i = 0; i < dist.length; i++) {
            TIntSet validJs = validTransitions.get(i);
            double[] disti = dist[i];
            if (validJs == null) {
                for (int j = 0; j < dist.length; j++)
                    disti[j] = MathUtil.LOG_ZERO;
            }
            else {
                for (int j = 0; j < dist.length; j++) {
                    if (!validJs.contains(j))
                        disti[j] = MathUtil.LOG_ZERO;
                }
            }
        }
        return dist;
    }

    @Override
    public double[][] getTransitionLogDist() {
        return zeroInvalidTransitions(delegateCounts.getTransitionLogDist());
    }

    @Override
    public double[][] getTransitionLogDist(boolean startWithPrior) {
        return zeroInvalidTransitions(delegateCounts.getTransitionLogDist(startWithPrior));
    }

    @Override
    public EmissionProbs getEmissionLogDist() {
        return delegateCounts.getEmissionLogDist();
    }

    @Override
    public EmissionProbs getEmissionLogDist(TIntSet validTagsForUnknowns) {
        return delegateCounts.getEmissionLogDist(validTagsForUnknowns);
    }

    @Override
    public void createTransitionPrior(String[] stateNames, TObjectIntHashMap<String> states, double amount) {
        delegateCounts.createTransitionPrior(stateNames, states, amount);
    }

    @Override
    public double get_c_t(int i) {
        return delegateCounts.get_c_t(i);
    }

}

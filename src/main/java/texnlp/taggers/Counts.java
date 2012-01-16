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

import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;

/**
 * Count events for HMMs. Doubles are used so that partial counts obtained from
 * Baum-Welch can be added.
 * 
 * @author Jason Baldridge
 * @author Dan Garrette
 */
public interface Counts {

    public Counts copy();

    public void increment(String w);

    public void addToSeenWords(String w);

    public void increment(String w, double amount);

    public void increment(int t_i, String w);

    public double probTagGivenWord(int t_i, String w);

    public void increment(int t_i, String w, double amount);

    public int getNumEmissions(int t_i);

    public void increment(int t_i);

    public void increment(int t_i, double amount);

    public void increment(int t_i, int t_j);

    public void increment(int t_i, int t_j, double amount);

    public void incrementInitial(int t_i);

    public void incrementInitial(int t_i, double amount);

    public void incrementFinal(int t_i);

    public void incrementFinal(int t_i, double amount);

    public void prepare();

    public double[] getInitialLogDist();

    public double[] getInitialLogDist(boolean startWithPrior);

    public double[] getFinalLogDist();

    public double[] getFinalLogDist(boolean startWithPrior);

    public double[][] getTransitionLogDist();

    public double[][] getTransitionLogDist(boolean startWithPrior);

    public EmissionProbs getEmissionLogDist();

    public EmissionProbs getEmissionLogDist(TIntSet validTagsForUnknowns);

    public void createTransitionPrior(String[] stateNames, TObjectIntHashMap<String> states, double amount);

    public double get_c_t(int i);

}



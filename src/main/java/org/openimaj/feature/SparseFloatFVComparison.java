/*
	AUTOMATICALLY GENERATED BY jTemp FROM
	C:/Users/Hamdi/Documents/NetBeansProjects/Openimaj/openimaj-master/core/core-feature/src/main/jtemp/org/openimaj/feature/Sparse#T#FVComparison.jtemp
*/
/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.feature;

import gnu.trove.set.hash.TFloatHashSet;

import org.openimaj.math.util.distance.HammingUtils;
import org.openimaj.util.array.SparseFloatArray;

/**
 * Comparison/distance methods for DoubleFV objects.
 * 
 * @author Jonathon Hare (jsh2@ecs.soton.ac.uk)
 */
public enum SparseFloatFVComparison implements FVComparator<SparseFloatFV> {
	/**
	 * Euclidean distance
	 * d(H1,H2) = Math.sqrt( sumI( (H1(I)-H2(I))^2 ) )
	 */
	EUCLIDEAN(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			double d = 0;
			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				double diff = e.value - e.otherValue;
				d += (diff * diff);
			}

			return Math.sqrt(d);
		}
	}, 
	/**
	 * Correlation
	 *
	 * d(H1,H2) = sumI( H'1(I) * H'2(I) ) / sqrt( sumI[H'1(I)2]^2 * sumI[H'2(I)^2] )
	 * where
	 * H'k(I) = Hk(I) - (1/N) * sumJ( Hk(J) ); N=number of FeatureVector bins
	 *
	 */
	CORRELATION(false) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			double N = h1.length;
			double SH1=0, SH2=0;

			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				SH1 += e.value;
				SH2 += e.otherValue;
			}
			SH1 /= N;
			SH2 /= N;

			double d = 0;
			double SH1S = 0;
			double SH2S = 0;

			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				double h1prime = e.value - SH1;
				double h2prime = e.otherValue - SH2;

				d += (h1prime * h2prime);
				SH1S += (h1prime * h1prime);
				SH2S += (h2prime * h2prime);
			}

			return d / Math.sqrt(SH1S * SH2S);
		}
	},
	/**
	 * Chi-squared distance
	 * d(H1,H2) = 0.5 * sumI[(H1(I)-H2(I))^2 / (H1(I)+H2(I))]
	 */
	CHI_SQUARE(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			double d = 0;

			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
			    double a = e.value - e.otherValue;
			    double b = e.value + e.otherValue;
			    
			    if (Math.abs(b) > 0) d += a*a/b;
			}

			return d / 2;
		}
	},
	/**
	 * Histogram intersection; assumes all values > 0.
	 * d(H1,H2) = sumI( min(H1(I), H2(I)) )
	 */
	INTERSECTION(false) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			double d = 0;

			for (SparseFloatArray.DualEntry e : h1.intersectEntries(h2)) {
				d += Math.min(e.value, e.otherValue);
			}

			return d;
		}
	},
	/**
	 * Bhattacharyya distance
	 * d(H1,H2) = sqrt( 1 - (1 / sqrt(sumI(H1(I)) * sumI(H2(I))) ) * sumI( sqrt(H1(I) * H2(I)) ) )
	 */
	BHATTACHARYYA(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			double SH1 = 0;
			double SH2 = 0;
			double d = 0;
			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				SH1 += e.value;
				SH2 += e.otherValue;
				d += Math.sqrt(e.value * e.otherValue);
			}

			double den = SH1 * SH2;
			if (den == 0) return 1;

			d /= Math.sqrt(den);

			return Math.sqrt(1.0 - d);
		}
	},
	/**
	 * Hamming Distance
	 * d(H1,H2) = sumI(H1(I) == H2(I) ? 1 : 0)
	 */
	HAMMING(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			int d = 0;

			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2))
				if (e.value != e.otherValue) d++;

			return d;
		}		
	},
	/**
	 * Hamming Distance for packed bit strings
	 * d(H1,H2) = sumI(H1(I) == H2(I) ? 1 : 0)
	 */
	PACKED_HAMMING(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			int d = 0;

			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				d += HammingUtils.packedHamming(e.value, e.otherValue);
			}

			return d;
		}	
	},
	/**
	 * City-block (L1) distance
	 * d(H1,H2) = sumI( abs(H1(I)-H2(I)) )
	 */
	CITY_BLOCK(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			double d = 0;

			for (SparseFloatArray.DualEntry e : h1.intersectEntries(h2)) {
				d += Math.abs(e.value - e.otherValue);
			}

			return d;
		}
	},
	/**
	 * Cosine similarity (sim of 1 means identical)
	 * d(H1,H2)=sumI(H1(I) * H2(I))) / (sumI(H1(I)^2) sumI(H2(I)^2))
	 */
	COSINE_SIM(false) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
			    throw new IllegalArgumentException("Vectors have differing lengths");

			double h12 = 0;
			double h11 = 0;
			double h22 = 0;
			
			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				h12 += e.value * e.otherValue;
				h11 += e.value * e.value;
				h22 += e.otherValue * e.otherValue;
			}

			return h12 / (Math.sqrt(h11) * Math.sqrt(h22));
		}
	},
	/**
	 * Cosine distance (-COSINE_SIM)
	 */
	COSINE_DIST(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			return -1 * COSINE_SIM.compare(h1, h2);
		}
	},
	/**
	 * The arccosine of the cosine similarity
	 */
	ARCCOS(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			return Math.acos( COSINE_SIM.compare(h1, h2) );
		}
	},
	/**
	 * The symmetric Kullback-Leibler divergence. Vectors must only contain
	 * positive values; internally they will be converted to double arrays 
	 * and normalised to sum to unit length.
	 */
	SYMMETRIC_KL_DIVERGENCE(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			if (h1.length != h2.length)
				throw new IllegalArgumentException("Vectors have differing lengths");
			
			double sum1 = 0;
			double sum2 = 0;
			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				sum1 += e.value;
				sum2 += e.otherValue;
			}
			
			double d = 0;
			for (SparseFloatArray.DualEntry e : h1.unionEntries(h2)) {
				double h1n = e.value / sum1;
				double h2n = e.otherValue / sum2;
				
				double q1 = h1n / h2n;
				double q2 = h2n / h1n;
				
				if (h1n != 0) d += (h1n * Math.log(q1) / Math.log(2)); 
				if (h2n != 0) d += (h2n * Math.log(q2) / Math.log(2));
			}

			return d / 2.0;
		}
	},
	/**
	 * Jaccard distance. Converts each vector to a set
	 * for comparison.
	 */
	JACCARD_DISTANCE(true) {
		@Override
		public double compare(final SparseFloatArray h1, final SparseFloatArray h2) {
			float[] h1v = h1.values();
			float[] h2v = h2.values();
			
			TFloatHashSet union = new TFloatHashSet(h1v);
			union.addAll(h2v);
			if (h1v.length != h1.length || h2v.length != h2.length)
				union.add((float)0);

			TFloatHashSet intersection = new TFloatHashSet(h1v);
			intersection.retainAll(h2v);
			if (h1v.length != h1.length && h2v.length != h2.length)
				union.add((float)0);

			return 1.0 - (((double)intersection.size()) / (double)union.size());
		}
	}
	;

	private boolean isDistance; 
	SparseFloatFVComparison(boolean isDistance) {
		this.isDistance = isDistance;
	}

	@Override
	public boolean isDistance() {
		return isDistance;
	}

	@Override
	public double compare(SparseFloatFV h1, SparseFloatFV h2) {
		return compare(h1.values, h2.values);
	}

	/**
	 * Compare two feature vectors in the form of sparse arrays, 
	 * returning a score or distance.
	 * 
	 * @param h1 the first feature array
	 * @param h2 the second feature array
	 * @return a score or distance
	 */	
	public abstract double compare(SparseFloatArray h1, SparseFloatArray h2);
}

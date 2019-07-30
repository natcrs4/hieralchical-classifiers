package com.mfl.sem.classifier;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mfl.sem.model.Dataset;

import no.uib.cipr.matrix.sparse.SparseVector;

public class DatasetTest {
	
	@Test
	public void testBalance1() {
		
		Dataset dataset = new Dataset(new SparseVector[3], new int[3],2,2);
		dataset.getCategories()[0]=0;
		dataset.getCategories()[1]=0;
		dataset.getCategories()[2]=1;
		Dataset balanced = dataset.balance();
		assertTrue(balanced.getData().length==4);
	}

	@Test
	public void testBalance2() {
		
		Dataset dataset = new Dataset(new SparseVector[4], new int[4],2,3);
		dataset.getCategories()[0]=0;
		dataset.getCategories()[1]=0;
		dataset.getCategories()[2]=1;
		dataset.getCategories()[3]=2;
		Dataset balanced = dataset.balance();
		assertTrue(balanced.getData().length==6);
	}
	
	@Test
	public void testBalance3() {
		
		Dataset dataset = new Dataset(new SparseVector[4], new int[4],2,4);
		dataset.getCategories()[0]=0;
		dataset.getCategories()[1]=0;
		dataset.getCategories()[2]=1;
		dataset.getCategories()[3]=2;
		Dataset balanced = dataset.balance();
		assertTrue(balanced.getData().length==6);
	}
}

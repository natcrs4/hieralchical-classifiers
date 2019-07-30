package com.mfl.sem.classifier.text;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;

import com.crs4.sem.model.Documentable;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.model.ScoredItem;
import com.mfl.sem.text.model.Doc;

public interface TextClassifier {
	public TextClassifier train(Documents document) throws IOException, ClassifierException;
    public List<ScoredItem> classify(Documentable doc)throws IOException;
}

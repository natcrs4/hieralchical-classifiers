package com.mfl.sem.classifier.text.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.crs4.sem.analysis.AuxAttribute;
import com.crs4.sem.model.Documentable;
import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.classifier.model.Dictionary;
import com.mfl.sem.classifier.model.Term;
import com.mfl.sem.classifier.text.DocItem;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.TextClassifier;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.uib.cipr.matrix.sparse.SparseVector;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextClassifierImpl implements TextClassifier {
	private Classifier classifier;
	private Analyzer analyzer;
	private Dictionary dictionary;
	private CategoryDictionary categoryDictionary;
	private Dataset problem;

	public TextClassifierImpl(Analyzer analyzer, Classifier classifier) {
		this.setAnalyzer(analyzer);
		this.setDictionary(new Dictionary());
		this.setClassifier(classifier);
		this.setCategoryDictionary(new CategoryDictionary());
	}

	public TextClassifierImpl(Analyzer analyzer, Classifier hclassifier, CategoryDictionary categoryDictionary) {
		this.setAnalyzer(analyzer);
		this.setDictionary(new Dictionary());
		this.setClassifier(hclassifier);
		this.setCategoryDictionary(categoryDictionary);
	}

	@Override
	public TextClassifier train(Documents documents) throws IOException, ClassifierException {
		Dataset problem = makeProblem(documents);
		this.setProblem(problem);
		this.getClassifier().train(problem);
		return this;
	}

	public Dataset makeProblem(Documents documents) throws IOException {
		Iterator<DocItem> iter = documents.iterator();
		SparseVector vectors[] = new SparseVector[documents.size()];
		int categories[] = new int[documents.size()];
		int k = 0;
		while (iter.hasNext()) {
			DocItem doc = iter.next();
			try {
				this.analizeToBuildDictionary(doc.get(), this.getDictionary());
				Category category = this.categoryDictionary.newCategory(doc.get().getCategories()[0]);
				categories[k] = category.getCode();
				// System.out.println(doc.get().getCategories());
				k++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		iter = documents.iterator();
		int j = 0;
		while (iter.hasNext()) {
			DocItem doc = iter.next();
			SparseVector current = this.analize(doc.get(), this.dictionary);
			vectors[j] = current;
			j++;
		}
		Dataset problem = new Dataset(vectors, categories, this.getDictionary().size(),
				this.getCategoryDictionary().size());
		return problem;
	}

	public Dictionary analizeToBuildDictionary(Documentable doc, Dictionary dictionary) throws IOException {
		// List<Term> terms= new ArrayList<Term>();
		String[] fields = { "title", "description", "url" };

		for (String field : fields) {
			String text = "";
			if (field.equals("description"))
				text = doc.getDescription();
			if (field.equals("title"))
				text = doc.getTitle();
			if (field.equals("url"))
				text = doc.getUrl();
			
			if (text != null) {
				TokenStream stream = analyzer.tokenStream(field, new StringReader(text));
				stream.reset();
				try {
					while (stream.incrementToken()) {
						String forma = stream.getAttribute(CharTermAttribute.class).toString();
						AuxAttribute att = stream.getAttribute(AuxAttribute.class)
								;
						Term term = dictionary.newTerm(forma);
						if(att!=null) {
							Term term2 = dictionary.newTerm(att.type());
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					stream.close();
				}
			}
		}
		return dictionary;
	}

	public SparseVector analize(Documentable doc, Dictionary dictionary) throws IOException {
		SparseVector vector = new SparseVector(dictionary.getSize());
		String[] fields = { "title", "description", "url" };

		for (String field : fields) {
			String text = "";
			if (field.equals("description"))
				text = doc.getDescription();
			if (field.equals("title"))
				text = doc.getTitle();
			if (field.equals("url"))
				text = doc.getUrl();
			if (text != null) {
				TokenStream stream = analyzer.tokenStream(field, new StringReader(text));
				stream.reset();

				try {
					while (stream.incrementToken()) {
						String forma = stream.getAttribute(CharTermAttribute.class).toString();
						AuxAttribute att = stream.getAttribute(AuxAttribute.class);
						Term term = dictionary.get(forma);
						if (term != null)
							vector.add(term.getIndex(), 1.0d);
						if(att!=null) {
						Term term2 = dictionary.get(att.type());
						if (term2 != null)
							vector.add(term2.getIndex(), 1.0d);
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					stream.close();
				}
			}
		}
		return vector;
	}

	@Override
	public List<ScoredItem> classify(Documentable doc) throws IOException {
		SparseVector document = this.analize(doc, this.dictionary);
		if (document.getIndex().length == 0)
			return new ArrayList<ScoredItem>();
		List<ScoredItem> result = this.getClassifier().classify(document);
		for (ScoredItem sc : result) {
			sc.setLabel(this.getCategoryDictionary().getCategory(sc.getIndex()).getLabel());
		}
		return result;
	}

}

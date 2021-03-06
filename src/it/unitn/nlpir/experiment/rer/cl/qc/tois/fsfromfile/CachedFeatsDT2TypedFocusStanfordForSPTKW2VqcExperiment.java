package it.unitn.nlpir.experiment.rer.cl.qc.tois.fsfromfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.unitn.nlpir.experiment.AnalyzerConfig;
import it.unitn.nlpir.experiment.IFeatsFromFile;
import it.unitn.nlpir.experiment.TrecQAExperiment;
import it.unitn.nlpir.features.FeatureSets;
import it.unitn.nlpir.features.builder.FeaturesBuilder;
import it.unitn.nlpir.projectors.Projectors;
import it.unitn.nlpir.pruners.StartsWithOrContainsTagPruningRule;
import it.unitn.nlpir.tree.PhraseDependencyTreeBuilder;
import it.unitn.nlpir.tree.leaffinalizers.LeafSPTKFullAllLeavesFinalizer;
import it.unitn.nlpir.uima.AnalysisEngineList;

/**
 * <li> DT2 + FOCUS + REL
 * <li> Features read from the external file
 * <li> SPTK fine-grained QC, using vectors by W2V as a similarity matrix
 * <li> <b>Example:</b> [ROOT [root [attr [NP [WP [who::w]]]] [VP [VBZ [be::v]]] [nsubj [REL-FOCUS-HUM-NP [DT [the::d]] [NN [author::n]]] [prep [NP [IN [of::i]] [DT [the::d]] [NN [book::n]]] [appos [REL-NP [DT [the::d]] [REL-NN [iron::n]] [REL-NN [lady::n]]] [dep [REL-NP [DT [a::d]] [REL-NN [biography::n]]] [prep [REL-NP [IN [of::i]] [REL-NNP [margaret::n]] [REL-NNP [thatcher::n]]]]]]]]]]	[ROOT [root [REL-NP [DT [the::d]] [REL-NNP [iron::n]] [REL-NN [lady::n]]] [dep [REL-NP [DT [a::d]] [REL-NN [biography::n]]] [prep [REL-FOCUS-HUM-NP [IN [of::i]] [REL-NNP [margaret::n]] [REL-NNP [thatcher::n]]] [prep [REL-FOCUS-HUM-NP [IN [by::i]] [NNP [hugo::n]] [NNP [young::n]]] [appos [REL-FOCUS-HUM-NP [NNP [farrar::n]]] [dep [REL-FOCUS-HUM-NP [NNP [straus::n]] [CC [&::c]] [NNP [giroux::n]]]]]]]]]]
* @author IKernels group
 *
 */
public class CachedFeatsDT2TypedFocusStanfordForSPTKW2VqcExperiment extends TrecQAExperiment implements IFeatsFromFile {
	private final String MODELS_FILE = "data/question-classifier/lct-dep-fine-keepcase/expsptk_fine_sptk_w2v";
	protected static final Logger logger = LoggerFactory.getLogger(CachedFeatsDT2TypedFocusStanfordForSPTKW2VqcExperiment.class);
	protected String modelsFile;
	
	protected void setupProjector() {
		this.pruningRay = 0;		
		this.projector = Projectors.getFocusProjector(new PhraseDependencyTreeBuilder(), pruningRay,
				new StartsWithOrContainsTagPruningRule(), new LeafSPTKFullAllLeavesFinalizer());
		this.modelsFile = MODELS_FILE;
	}
	
	@Override
	public AnalysisEngineList getAnalysisEngineList() {
		return AnalyzerConfig.getStanfordQAAnalysisEngineListWithSPTKQC(modelsFile, 
				"it.unitn.nlpir.tree.LCTBuilder","it.unitn.nlpir.tree.SPTKFullTokenNodesKeepCaseFinalizer");
	}

	protected void setupFeatures() {
		fb = new FeaturesBuilder();
	}

	@Override
	public void setFeaturesSource(String idFile, String featureFile) {
		logger.info(String.format("Reading features from %s, %s", idFile, featureFile));
		fb.extend(FeatureSets.buildFeaturesFromExternalFile(idFile, featureFile));
		
	}
}

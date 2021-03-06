**RelTextRank** is a flexible Java pipeline for converting pairs of raw texts into structured representations and enriching them with semantic information about the relations between the two pieces of text (e.g., lexical exact match). 

See the [System](https://github.com/iKernels/RelTextRank/wiki/System-module) page in the [project wiki](https://github.com/iKernels/RelTextRank/wiki) for the details on how to run the application for reranking and classification.

This readme provides [Installation instructions](#installation) and  [End-to-end example usage](#running-an-end-to-end-example).



# Installation

### Prerequisites
The tool requires the following prerequisites:
*	JDK 1.8+
*	Apache Maven > 3.3.9. Refer to http://maven.apache.org/install.html for the installation instructions
*	Additional DKPro resources for computing semantic Wikipedia and WordNet-based DKPro similarity features. **(This is not needed for running the end-to-end example.)** Refer to https://dkpro.github.io/dkpro-similarity/settinguptheresources/ for the instructions on how to setup the following DKPro resources 
(Please, remember to set up the DKPRO_HOME environment variable as described in the installation instructions web-page):

    * **WordNet Lexical Semantic Resource index.**  Follow all the official installation instructions, but substitute the original wordnet_properties.xml file supplied within WordNet resource graph archive, with the following file instead: https://raw.githubusercontent.com/dkpro/dkpro-lsr/master/de.tudarmstadt.ukp.dkpro.lexsemresource.wordnet-asl/src/main/resources/resource/WordNet_3/wordnet_properties.xml.

    We do not employ Wiktionary in the pipeline. Therefore, you need to remove the following lines from the resources.xml file (or, alternatively, you may download and install the Wiktionary resources as described in the DKPro installation instructions):
     ```xml
    <bean id="wiktionary-en" lazy-init="true" class="de.tudarmstadt.ukp.dkpro.lexsemresource.wiktionary.WiktionaryResource">
    <constructor-arg value="ENGLISH"/>
    <constructor-arg value="${DKPRO_HOME}/LexSemResources/Wiktionary/jwktl_0.15.2_en20100403"/>
    </bean>
    ```
    *	**Wikipedia Explicit Semantic Analysis index.** If you want to be able to access to the full range of features available in this pipeline, please, download the precompiled the Wikipedia Explicit Semantic Analysis index (see the Explicit Semantic Analysis: Vector Indexes section of the DKPro installation instructions).
    *	**Apache UIMA.** Follow the instructions at https://uima.apache.org/downloads.cgi. Note that **you need to install Apache UIMA only if you are planning to modify or recompile the UIMA type system supplied with the RelationalTextRanking (this is not needed if you want to run an end-to-end example)**. Otherwise, all the relevant UIMA libraries are already included into the dependencies, and you do not need to install anything.

### Installation steps
After making sure that you have all the necessary prerequisites, install the module as follows.
#### Step 1 
Check out the project repository and set ```JAVA_HOME``` variable
```bash
git clone https://github.com/iKernels/RelationalTextRanking.git
cd ./RelationalTextRanking

export JAVA_HOME=<path to your JDK distribution>
```


#### Step 2: Build the Maven project.
Import PTK.jar, available in the ./RelationalTextRanking/lib folder into your local Maven repository using the following command:
```bash
mvn install:install-file -Dfile=lib/PTK.jar -DgroupId=it.unitn.kernels.ptk -DartifactId=ptk -Dversion=1.0 -Dpackaging=jar
```
Then download other dependencies and compile
```bash
mvn clean install
mvn clean dependency:copy-dependencies package
```

#### Step 3:	Generate the UIMA Annotation Type classes (optional)
We provide the pre-generated UIMA type classes. They are identified by the UIMA Type System descriptor in desc/PipelineTypeSystem.xml.
In case if you modify the type system for your purposes, you need to regenerate the classes, following the instructions in https://uima.apache.org/d/uimaj-2.4.0/tutorials_and_users_guides.html#ugr.tug.aae.generating_jcas_sources.

#### Step 4: Build the SVMLight-TK library
(please make sure that the Java classpath includes the ```tools/SVM-Light-TK-1.5.Lib/``` folder)
```bash
cd tools/SVM-Light-TK-1.5.Lib/
make clean; make
cd ../..
```

## Running an end-to-end example
The example employs data from the [WikiQA](https://www.microsoft.com/en-us/research/publication/wikiqa-a-challenge-dataset-for-open-domain-question-answering/) dataset.

First you need to download the WikiQA data [here](https://www.microsoft.com/en-us/download/details.aspx?id=52419).
Then run the following commands from the root of the RelTextRank distribution.

```bash
export wikiqa_location=<folder to which you unpacked the WikiQa distribution>
mkdir data/wikiQA
python scripts/converters/wikiqa_convert.py ${wikiqa_location}/WikiQA-train.tsv data/wikiQA/WikiQA-train.questions.txt  data/wikiQA/WikiQA-train.tsv.resultset
python scripts/converters/wikiqa_convert.py ${wikiqa_location}/WikiQA-test.tsv data/wikiQA/WikiQA-test.questions.txt  data/wikiQA/WikiQA-test.tsv.resultset
python scripts/converters/wikiqa_convert.py ${wikiqa_location}/WikiQA-dev.tsv data/wikiQA/WikiQA-dev.questions.txt  data/wikiQA/WikiQA-dev.tsv.resultset
```

Set the classpath:

```bash
export CLASSPATH=bin/:target/dependency/*:target/classes
```

Generate a training file:
```bash
java -Xmx5G -Xss512m it.unitn.nlpir.system.core.RERTextPairConversion -questionsPath data/wikiQA/WikiQA-train.questions.txt -answersPath data/wikiQA/WikiQA-train.tsv.resultset -outputDir data/examples/wikiqa -filePersistence CASes/wikiQA -candidatesToKeep 10 -mode train -expClassName it.unitn.nlpir.experiment.fqa.CHExperiment -featureExtractorClass it.unitn.nlpir.features.presets.BaselineFeatures
```
Please refer to the  [System](https://github.com/iKernels/RelTextRank/wiki/System-module) page in the [project wiki](https://github.com/iKernels/RelTextRank/wiki) for the parameters' descriptions.

You may find the generated file in ```data/examples/wikiqa/svm.train```. The ```data/examples/wikiqa/svm.train.res``` will contain the labels of the corresponding text pairs from the files specified by ```-questionsPath``` and ```-answersPath``` parameters.


Train a model:
If you are using the software for the first time and you want to use the SVMLight-TK distribution supplied with this pipeline, you must compile it first:
```bash
cd tools/SVM-Light-1.5-rer
make clean; make
cd ../..
```

Train a reranking SVM model with  Partial Tree Kernel applied to the trees and a polynomial kernel applied to the feature vectors (refer to http://disi.unitn.it/moschitti/Tree-Kernel.htm for the full list of options description.
```bash
tools/SVM-Light-1.5-rer/svm_learn -W R -V R -t 5 -F 3 -C + -m 5000  data/examples/wikiqa/svm.train data/wikiQA/wikiqa-ch-rer-baselinefeats.model  data/examples/wikiqa/wikiqa-ch-rer-baselinefeats.pred
```

Run classification on the test data:
```bash
java -Xmx5G -Xss512m  it.unitn.nlpir.system.core.TextPairPrediction -expClassName it.unitn.nlpir.experiment.fqa.CHExperiment -candidatesToKeep 1000 -svmModel data/wikiQA/wikiqa-ch-rer-baselinefeats.model -featureExtractorClass it.unitn.nlpir.features.presets.BaselineFeatures -questionsPath data/wikiQA/WikiQA-test.questions.txt -answersPath data/wikiQA/WikiQA-test.tsv.resultset -outputDir data/examples/wikiqa -outputFile wikiqa-ch-rer-baselinefeats.pred  -mode reranking -filePersistence CASes/wikiQA/test
```

Evaluate:
``` bash
python scripts/eval/ev.py --ignore_noanswer --ignore_allanswer -t 1000 data/wikiQA/WikiQA-test.tsv.resultset data/examples/wikiqa/wikiqa-ch-rer-baselinefeats.pred
```
* ``--ignore_noanswer`` means that the questions which have no correct answer passage in ``data/wikiQA/WikiQA-test.tsv.resultset`` will be excluded from the evaluation, as they are not useful for comparing the performance of the answer passage reranking systems 
* ``--ignore_allanswer`` means that the questions which have only correct answer  passage in ``data/wikiQA/WikiQA-test.tsv.resultset`` will be excluded from the evaluation, as they are not useful for comparing the performance of the answer passage reranking systems 
* ``-t 1000`` means that we evaluate using only 1000 top-ranked answer passages per question (i.e. all of them in case of WikiQA corpus)

If you have followed all of the instructions above, the output must be as follows:

| System | MRR | MAP  | P@1 |
|----|----|----|----|
|REF_FILE |  100.00 | 100.00 | 100.00 |
|SVM   |  71.69 |  70.31 |  56.12 |

Here MRR, MAP and P@1 are Mean Reciprocal Rank, Mean Average Precision and Precision at rank 1, respectively.
* *SVM* line reports the output of the system (``data/examples/wikiqa/wikiqa-ch-rer-baselinefeats.pred``).
* *REF_FILE* is the performance evaluated on the gold standard file  (``data/wikiQA/WikiQA-test.tsv.resultset``). Given that this is the file with the gold labels and we leave the questions without the correct answer passage out of the consideration (``--ignore_noanswer``), the the values of all the metrics are 100.00.

# License
This software is licensed under [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0) license.



/* First created by JCasGen Wed Nov 23 17:36:58 CET 2016 */
package it.unitn.it.nlpir.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Nov 23 17:36:58 CET 2016
 * XML source: /Users/kateryna/Documents/workspace/RelationalTextRanking/desc/PipelineTypeSystem.xml
 * @generated */
public class UserMention extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(UserMention.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected UserMention() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public UserMention(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public UserMention(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public UserMention(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: name

  /** getter for name - gets 
   * @generated
   * @return value of the feature 
   */
  public String getName() {
    if (UserMention_Type.featOkTst && ((UserMention_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "it.unitn.it.nlpir.types.UserMention");
    return jcasType.ll_cas.ll_getStringValue(addr, ((UserMention_Type)jcasType).casFeatCode_name);}
    
  /** setter for name - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setName(String v) {
    if (UserMention_Type.featOkTst && ((UserMention_Type)jcasType).casFeat_name == null)
      jcasType.jcas.throwFeatMissing("name", "it.unitn.it.nlpir.types.UserMention");
    jcasType.ll_cas.ll_setStringValue(addr, ((UserMention_Type)jcasType).casFeatCode_name, v);}    
   
    
  //*--------------*
  //* Feature: isAuthor

  /** getter for isAuthor - gets 
   * @generated
   * @return value of the feature 
   */
  public boolean getIsAuthor() {
    if (UserMention_Type.featOkTst && ((UserMention_Type)jcasType).casFeat_isAuthor == null)
      jcasType.jcas.throwFeatMissing("isAuthor", "it.unitn.it.nlpir.types.UserMention");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((UserMention_Type)jcasType).casFeatCode_isAuthor);}
    
  /** setter for isAuthor - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setIsAuthor(boolean v) {
    if (UserMention_Type.featOkTst && ((UserMention_Type)jcasType).casFeat_isAuthor == null)
      jcasType.jcas.throwFeatMissing("isAuthor", "it.unitn.it.nlpir.types.UserMention");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((UserMention_Type)jcasType).casFeatCode_isAuthor, v);}    
  }

    
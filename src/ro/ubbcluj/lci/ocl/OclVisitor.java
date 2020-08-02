package ro.ubbcluj.lci.ocl;

import ro.ubbcluj.lci.ocl.nodes.OclROOT;
import ro.ubbcluj.lci.ocl.nodes.OclTEXT;
import ro.ubbcluj.lci.ocl.nodes.actualParameterList;
import ro.ubbcluj.lci.ocl.nodes.addOperator;
import ro.ubbcluj.lci.ocl.nodes.additiveExpression;
import ro.ubbcluj.lci.ocl.nodes.classifierContext;
import ro.ubbcluj.lci.ocl.nodes.collectionItem;
import ro.ubbcluj.lci.ocl.nodes.collectionKind;
import ro.ubbcluj.lci.ocl.nodes.collectionType;
import ro.ubbcluj.lci.ocl.nodes.constraint;
import ro.ubbcluj.lci.ocl.nodes.contextDeclaration;
import ro.ubbcluj.lci.ocl.nodes.declarator;
import ro.ubbcluj.lci.ocl.nodes.dottedPathName;
import ro.ubbcluj.lci.ocl.nodes.enumLiteral;
import ro.ubbcluj.lci.ocl.nodes.expression;
import ro.ubbcluj.lci.ocl.nodes.formalParameterList;
import ro.ubbcluj.lci.ocl.nodes.freeConstraint;
import ro.ubbcluj.lci.ocl.nodes.ifExpression;
import ro.ubbcluj.lci.ocl.nodes.letExpression;
import ro.ubbcluj.lci.ocl.nodes.literal;
import ro.ubbcluj.lci.ocl.nodes.literalCollection;
import ro.ubbcluj.lci.ocl.nodes.literalTuple;
import ro.ubbcluj.lci.ocl.nodes.logicalExpression;
import ro.ubbcluj.lci.ocl.nodes.logicalOperator;
import ro.ubbcluj.lci.ocl.nodes.multiplicativeExpression;
import ro.ubbcluj.lci.ocl.nodes.multiplyOperator;
import ro.ubbcluj.lci.ocl.nodes.name;
import ro.ubbcluj.lci.ocl.nodes.number;
import ro.ubbcluj.lci.ocl.nodes.oclExpression;
import ro.ubbcluj.lci.ocl.nodes.oclExpressions;
import ro.ubbcluj.lci.ocl.nodes.oclFile;
import ro.ubbcluj.lci.ocl.nodes.oclPackage;
import ro.ubbcluj.lci.ocl.nodes.operationContext;
import ro.ubbcluj.lci.ocl.nodes.operationName;
import ro.ubbcluj.lci.ocl.nodes.option;
import ro.ubbcluj.lci.ocl.nodes.pathName;
import ro.ubbcluj.lci.ocl.nodes.postfixExpression;
import ro.ubbcluj.lci.ocl.nodes.primaryExpression;
import ro.ubbcluj.lci.ocl.nodes.propertyCall;
import ro.ubbcluj.lci.ocl.nodes.propertyCallParameters;
import ro.ubbcluj.lci.ocl.nodes.qualifiers;
import ro.ubbcluj.lci.ocl.nodes.relationalExpression;
import ro.ubbcluj.lci.ocl.nodes.relationalOperator;
import ro.ubbcluj.lci.ocl.nodes.returnType;
import ro.ubbcluj.lci.ocl.nodes.simpleTypeSpecifier;
import ro.ubbcluj.lci.ocl.nodes.stereotype;
import ro.ubbcluj.lci.ocl.nodes.string;
import ro.ubbcluj.lci.ocl.nodes.timeExpression;
import ro.ubbcluj.lci.ocl.nodes.tupleType;
import ro.ubbcluj.lci.ocl.nodes.typeSpecifier;
import ro.ubbcluj.lci.ocl.nodes.unaryExpression;
import ro.ubbcluj.lci.ocl.nodes.unaryOperator;
import ro.ubbcluj.lci.ocl.nodes.untilExpression;

public class OclVisitor {
   public OclVisitor() {
   }

   public boolean visitPre(OclROOT nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(OclROOT nod) throws ExceptionChecker {
   }

   public boolean visitPre(OclTEXT nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(OclTEXT nod) throws ExceptionChecker {
   }

   public boolean visitPre(oclFile nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(oclFile nod) throws ExceptionChecker {
   }

   public boolean visitPre(option nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(option nod) throws ExceptionChecker {
   }

   public boolean visitPre(oclPackage nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(oclPackage nod) throws ExceptionChecker {
   }

   public boolean visitPre(freeConstraint nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(freeConstraint nod) throws ExceptionChecker {
   }

   public boolean visitPre(oclExpressions nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(oclExpressions nod) throws ExceptionChecker {
   }

   public boolean visitPre(constraint nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(constraint nod) throws ExceptionChecker {
   }

   public boolean visitPre(contextDeclaration nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(contextDeclaration nod) throws ExceptionChecker {
   }

   public boolean visitPre(classifierContext nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(classifierContext nod) throws ExceptionChecker {
   }

   public boolean visitPre(operationContext nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(operationContext nod) throws ExceptionChecker {
   }

   public boolean visitPre(stereotype nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(stereotype nod) throws ExceptionChecker {
   }

   public boolean visitPre(operationName nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(operationName nod) throws ExceptionChecker {
   }

   public boolean visitPre(formalParameterList nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(formalParameterList nod) throws ExceptionChecker {
   }

   public boolean visitPre(oclExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(oclExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(returnType nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(returnType nod) throws ExceptionChecker {
   }

   public boolean visitPre(letExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(letExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(typeSpecifier nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(typeSpecifier nod) throws ExceptionChecker {
   }

   public boolean visitPre(collectionType nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(collectionType nod) throws ExceptionChecker {
   }

   public boolean visitPre(tupleType nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(tupleType nod) throws ExceptionChecker {
   }

   public boolean visitPre(expression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(expression nod) throws ExceptionChecker {
   }

   public boolean visitPre(ifExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(ifExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(untilExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(untilExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(logicalExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(logicalExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(relationalExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(relationalExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(additiveExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(additiveExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(multiplicativeExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(multiplicativeExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(unaryExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(unaryExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(postfixExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(postfixExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(primaryExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(primaryExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(propertyCallParameters nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(propertyCallParameters nod) throws ExceptionChecker {
   }

   public boolean visitPre(literal nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(literal nod) throws ExceptionChecker {
   }

   public boolean visitPre(enumLiteral nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(enumLiteral nod) throws ExceptionChecker {
   }

   public boolean visitPre(simpleTypeSpecifier nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(simpleTypeSpecifier nod) throws ExceptionChecker {
   }

   public boolean visitPre(literalCollection nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(literalCollection nod) throws ExceptionChecker {
   }

   public boolean visitPre(collectionItem nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(collectionItem nod) throws ExceptionChecker {
   }

   public boolean visitPre(literalTuple nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(literalTuple nod) throws ExceptionChecker {
   }

   public boolean visitPre(propertyCall nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(propertyCall nod) throws ExceptionChecker {
   }

   public boolean visitPre(qualifiers nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(qualifiers nod) throws ExceptionChecker {
   }

   public boolean visitPre(declarator nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(declarator nod) throws ExceptionChecker {
   }

   public boolean visitPre(pathName nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(pathName nod) throws ExceptionChecker {
   }

   public boolean visitPre(dottedPathName nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(dottedPathName nod) throws ExceptionChecker {
   }

   public boolean visitPre(timeExpression nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(timeExpression nod) throws ExceptionChecker {
   }

   public boolean visitPre(actualParameterList nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(actualParameterList nod) throws ExceptionChecker {
   }

   public boolean visitPre(logicalOperator nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(logicalOperator nod) throws ExceptionChecker {
   }

   public boolean visitPre(collectionKind nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(collectionKind nod) throws ExceptionChecker {
   }

   public boolean visitPre(relationalOperator nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(relationalOperator nod) throws ExceptionChecker {
   }

   public boolean visitPre(addOperator nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(addOperator nod) throws ExceptionChecker {
   }

   public boolean visitPre(multiplyOperator nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(multiplyOperator nod) throws ExceptionChecker {
   }

   public boolean visitPre(unaryOperator nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(unaryOperator nod) throws ExceptionChecker {
   }

   public boolean visitPre(name nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(name nod) throws ExceptionChecker {
   }

   public boolean visitPre(string nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(string nod) throws ExceptionChecker {
   }

   public boolean visitPre(number nod) throws ExceptionChecker {
      return true;
   }

   public void visitPost(number nod) throws ExceptionChecker {
   }
}

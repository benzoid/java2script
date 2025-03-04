/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Zhou Renjian - initial API and implementation
 *******************************************************************************/
package net.sf.j2s.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

// BH 2019.10.18 fix for "P$.licationShutdownHooks"
// BH 2019.09.07 adds optimization for lambda methods that do not have finals
// BH 2019.08.29 fix for boxing of binary representation 0b01... (Google Closure Compiler bug)
// BH 2019.05.13 fix for Math.getExponent, ulp, nextDown, nextUp, nextAfter needing qualification
// BH 2019.05.13 fix for Function reference in   new Foo()::test(...)
// BH 2019.04.03 fix for @j2sIgnore not including {}
// note to self: It is an annoyance that Eclipse does not recognize an annotation edit
//               as a need to recompile automatically
// TODO: Create a j2s configuration tag "j2s.jaxb.packages= package;package;package..."
//       that will direct the transpiler to create __ANN__ in any class within this package. 
/**
 * 
 * @author zhou renjian 2006-12-3
 * @author Bob Hanson 2017-08,09,10
 *
 * 
 *
 */
public class Java2ScriptVisitor extends ASTVisitor {

	private static final String VERSION = CorePlugin.VERSION;

	/**
	 * The idea of simplifying the functional interfaces using Clazz.newLambda did
	 * not work out as well as I had hoped. The code is still here, but
	 * Clazz.newLambda has been commented out, and setting this false bypasses the
	 * code that is here as well.
	 * 
	 */
	private static final boolean ALLOW_NEW_LAMBDA = false;

	/**
	 * If there are no finals for a lambda method, then we can reuse the object. 
	 * This can be huge for preventing repetitive object creation
	 */
	private static final boolean ALLOW_LAMBDA_OBJECT_REUSE = true;

	private static final int NOT_LAMBDA = 0;
	private static final int LAMBDA_METHOD = 1;
	private static final int LAMBDA_CREATION = 3;
	private static final int LAMBDA_EXPRESSION = 5;

	private final static int METHOD_NOTSPECIAL = 0;
	private final static int METHOD_LITERAL = 1;
	private final static int METHOD_LAMBDA_C = 3;

	private final static int METHOD_CONSTRUCTOR = 4;
	private final static int METHOD_INDEXOF = 8;
	private final static int METHOD_ISQUALIFIED = 16;
	private final static int METHOD_NULLEXPRESSION = 32;

	private final static String CHARCODEAT0 = ".$c()";

	private static final String NULL_PACKAGE = "_";

	static final int JAXB_TYPE_UNKNOWN = -1;
	final static int JAXB_TYPE_NONE = 0;
	final static int JAXB_TYPE_FIELD = 1;
	final static int JAXB_TYPE_PUBLIC_MEMBER = 2;
	final static int JAXB_TYPE_PROPERTY = 3;
	static final int JAXB_TYPE_ENUM = 4;
	/**
	 * UNSPECIFIED indicates that no XMLAccesorType was indicated for the class, so
	 * this must be determined at run time based on the package. In this case we
	 * introduce the !XMLPublic(true|false) annotation into C$.__ANN__ so that at
	 * least we know whether this field is public or not. This is then a flag to the
	 * marshaller to filter fields based on whether we are using PUBLIC_MEMBER or
	 * not from the package or superclass. Could work. Not tested.
	 */
	static final int JAXB_TYPE_UNSPECIFIED = 5;

	private IJavaProject global_project;

	/**
	 * multipurpose flag for testing development ideas.
	 * 
	 */
	private boolean global_testing;
	// make private fields properties of p1$ ?
	// the problem only shows up with a2s subclasses of Swing components
	// because they could declare the same private variable and not get
	// caught by the compiler, since they do not subclass that class.
	//
	// possible issues:
	// 1) We may have changed some awt, sun, and javax fields from private to public
	// 2) Is there an issue with inner classes referencing outer-class private
	// fields?

	private String package_name;
	private int package_blockLevel = 0;
	private int package_currentBlockForVisit = -1;
	private Stack<String> package_methodStackForFinals = new Stack<String>();

	/**
	 * track the names for I$$[...]
	 */
	private StringBuffer package_includes = new StringBuffer();

	/**
	 * map class names to I$$[] index
	 * 
	 */
	private Map<String, Integer> package_htIncludeNames = new Hashtable<>();

	/**
	 * I$$[] index counter
	 * 
	 */
	private int[] package_includeCount = new int[1];

	/**
	 * List of variables that are declared as final or are found to be effectively
	 * final.
	 */
	private List<LocalVariable> package_finalVars = new ArrayList<LocalVariable>();

	/**
	 * The final variables that are actually referenced inside an anonymous or local
	 * class
	 */
	private List<LocalVariable> class_visitedVars = new ArrayList<LocalVariable>();

	int class_jaxbAccessorType = JAXB_TYPE_UNKNOWN;

	/**
	 * annotations collected for a class
	 */
	private List<ClassAnnotation> class_annotations;

	/**
	 * a flag to indicate that the expression being evaluated is an ArrayAccess type
	 * and so must be integerized with |0
	 */
	private boolean temp_processingArrayIndex;

	/**
	 * functionalInterface methods add the name$ qualifier even if they are
	 * parameterized
	 * 
	 */
	private boolean temp_add$UnqualifiedMethod;

	// the three key elements of any class

	private String class_fullName = ""; // test.Test_
	private String class_shortName = ""; // Test_
	private ITypeBinding class_typeBinding;

	/**
	 * Set the three key elements for the current class. Called only by
	 * addClsasOrInterface.
	 * 
	 * @param className
	 * @param binding
	 */
	private void setClassAndBinding(String className, ITypeBinding binding) {
		class_typeBinding = binding;
		class_shortName = className;
		class_fullName = package_name + '.' + class_shortName;
	}

	private Java2ScriptVisitor setInnerGlobals(Java2ScriptVisitor parent, ASTNode node) {
		package_name = parent.package_name;
		package_htIncludeNames = parent.package_htIncludeNames;
		package_includeCount = parent.package_includeCount;
		package_includes = parent.package_includes;
		package_mapBlockJavadoc = parent.package_mapBlockJavadoc;

		// final and effectively final references

		package_currentBlockForVisit = parent.package_currentBlockForVisit;
		package_methodStackForFinals = parent.package_methodStackForFinals;
		package_blockLevel = parent.package_blockLevel;
		package_finalVars = parent.package_finalVars;

		class_visitedVars = parent.class_visitedVars;
		// inner class temporary visitor business

		this$0Name = parent.class_fullName;
		innerNode = node;

		return this;
	}

	private ASTNode innerNode;
	private String this$0Name;
	private boolean class_isAnonymousOrLocal;
	private boolean inNewLambdaExpression;

	/**
	 * default constructor found by visit(MethodDeclaration)
	 */
	private boolean haveDefaultConstructor;

	private static IType appletType;

	public Java2ScriptVisitor() {
		// default constructor is necessary for addClassOrInterface

		// TODO how to compare a type with a subclass of JApplet?
//		try {
//			appletType = project.findType("javax.swing.JApplet");
//		} catch (JavaModelException e) {
//			logErr("Java2ScriptVisitor could not find javax.swing.JApplet");
//		}
	}

	public Java2ScriptVisitor setProject(IJavaProject project, boolean testing) {
		this.global_testing = testing;
		this.global_project = project;
		return this;
	}

	public String getMyPackageName() {
		return package_name;
	}

	/**
	 * Buffer that keeps all compiled *.js.
	 */
	private StringBuffer buffer = new StringBuffer();

	private char getLastCharInBuffer() {
		return (buffer.length() == 0 ? '\0' : buffer.charAt(buffer.length() - 1));
	}

	private StringBuffer init0Buffer;

	private ArrayList<String> applets, apps;

	private void addApplication() {
		if (apps == null)
			apps = new ArrayList<String>();
		apps.add(class_fullName);
	}

	private boolean checkAddApplet(ITypeBinding binding) {
		if (Modifier.isAbstract(binding.getModifiers()))
			return false;
		IType bound = (IType) binding.getJavaElement();
		// How to compare this with JApplet?
		ITypeBinding b = binding;
		while ((binding = binding.getSuperclass()) != null) {
			String name = binding.getQualifiedName();
			if ("javax.swing.JApplet".equals(name) || "java.applet.Applet".equals(name)) {
				if (applets == null)
					applets = new ArrayList<String>();
				applets.add(class_fullName);
				return true;
			}
			if (name.startsWith("java.") || name.startsWith("javax"))
				return false;
		}
		return false;
	}

	public ArrayList<String> getAppList(boolean isApplets) {
		return (isApplets ? applets : apps);
	}

	private boolean isUserApplet;

	public boolean visit(CompilationUnit node) {
		resetPrivateVars();
		return true;
	}

	public boolean visit(PackageDeclaration node) {
		setMapJavaDoc(node);
		List annotations = node.annotations();
		if (annotations != null && annotations.size() > 0) {
			for (int i = 0; i < annotations.size(); i++)
				addAnnotation((Annotation) annotations.get(i), node, CHECK_ANNOTATIONS_ONLY);
			if (class_jaxbAccessorType == JAXB_TYPE_UNKNOWN)
				class_jaxbAccessorType = JAXB_TYPE_PUBLIC_MEMBER;
		}
		setPackage(node.getName().toString());
		return false;
	}

//	public boolean visit(AnonymousClassDeclaration node) {
	// anonymous will never come through here. It will be routed directly to
	// addClassOrInterface
//		addClassOrInterface(node, node.resolveBinding(), node.bodyDeclarations(), 'a');
//		return false;
//	}

	/**
	 * Set the package name and start the package header
	 * 
	 * @param packageName package name or null to check to see if this is a
	 *                    top-level window. applet
	 * 
	 */
	private void setPackage(String packageName) {
		if (packageName == null) {
			if (package_name != null)
				return;
			packageName = NULL_PACKAGE;
		}
		package_name = packageName;
		package_includes = new StringBuffer();
		buffer.append("var P$=");
		if (NameMapper.isJ2sClazzPackage(package_name)) {
			buffer.append(packageName);
		} else {
			buffer.append("Clazz.newPackage(\"").append(packageName).append("\")");
		}
		buffer.append(",I$=[];\r\n");
	}

	public boolean visit(AssertStatement node) {
		buffer.append("Clazz.assert(C$, this, function(){return ");
		addExpressionAsTargetType(node.getExpression(), Boolean.TYPE, "r", null);
		Expression msg = node.getMessage();
		if (msg != null) {
			buffer.append("}, function(){return ");
			msg.accept(this);
		}
		buffer.append("});\r\n");
		trailingBuffer.hasAssert = true;
		return false;
	}

	/**
	 * Only specially process blocks if they are method declarations. Never process
	 * these for constructors.
	 */

	public boolean visit(Block node) {
		if (inNewLambdaExpression)
			return true;
		package_blockLevel++;
		buffer.append("{\r\n");
		ASTNode parent = node.getParent();
		if (parent instanceof MethodDeclaration && !((MethodDeclaration) parent).isConstructor()
				|| parent instanceof Initializer) {
			Javadoc javadoc = ((BodyDeclaration) parent).getJavadoc();
			if (javadoc != null) {
				List<Javadoc> list = new ArrayList<Javadoc>();
				list.add(javadoc);
				return !NativeDoc.addJ2sJavadocs(buffer, list, false);
			}
		}
		return true;
	}

	public void endVisit(Block node) {
		if (inNewLambdaExpression)
			return;
		// look for trailing j2sNative block just before the end of a block
		getJ2sJavadoc(node, DOC_ADD_POST);
		buffer.append("}");
		clearVariables(package_finalVars);
		package_blockLevel--;
	}

	public boolean visit(BreakStatement node) {
		buffer.append("break");
		addLabel(node.getLabel(), false);
		return false;
	}

	/**
	 * top-level new Foo() inner static new Foo.Bar()
	 * 
	 * lambda expression Runnable r2 = () -> System. out.println("Hello world
	 * two!");
	 * 
	 * new MouseListener() {...}
	 * 
	 */
	public boolean visit(ClassInstanceCreation node) {
		AnonymousClassDeclaration anonDeclare = node.getAnonymousClassDeclaration();
		ITypeBinding binding = node.resolveTypeBinding();
		if (binding == null)
			return false;
		if (binding.isLocal()) {
			// method-local classes or anonymous classes
			// includes anonymous, despite JLS spec that anonymous classes are not local
			processLocalInstance(node, anonDeclare, binding, binding, getJavaClassNameQualified(binding), NOT_LAMBDA,
					anonDeclare == null);
			return false;
		}
		IMethodBinding constructorMethodBinding = node.resolveConstructorBinding();
		if (binding.isTopLevel() || isStatic(binding)) {
			// standard new Foo() or new ClassX.Foo()
			addConstructor(binding,
					// node.getType(),
					constructorMethodBinding, node.arguments(), -1);
		} else {
			// inner nonstatic class
			addInnerTypeInstance(node, binding, getJavaClassNameQualified(binding), node.getExpression(), null,
					(constructorMethodBinding == null ? null : constructorMethodBinding.getMethodDeclaration()), null,
					null);
		}
		return false;
	}

	private void addConstructor(ITypeBinding javaClass,
			// Type type,
			IMethodBinding constructorMethodBinding, List<?> arguments, int lambdaArity) {
		String javaClassName = getJavaClassNameQualified(javaClass);
		if ("java.lang.Object".equals(javaClassName)) {
			buffer.append(" Clazz.new_()");
			return;
		}
		String finalQualifiedClassName = getFinalJ2SClassName(javaClassName, FINAL_RAW);
		String prefix = null, postfix = null;
		boolean isDefault = false;
		if ("String".equals(finalQualifiedClassName)) {
			// special treatment for String -- see j2sSwingJS.js
			buffer.append(" String.instantialize(");
		} else if (NameMapper.isOneOf(finalQualifiedClassName, NameMapper.noConstructorNames)) {
			// look out for java.lang.Integer and the like -- just pass it
			// directly
			// Replace new Boolean with Boolean.from because new
			// Boolean("false") returns true in JavaScript.
			// JavaScript considers any string to be true while java only
			// considers the string "true" to be true
			if (finalQualifiedClassName.equals("Boolean"))
				buffer.append(" Boolean.from(");
			else
				buffer.append(" new ").append(finalQualifiedClassName).append("(");
		} else {
			openNew(javaClass, javaClassName, null, constructorMethodBinding, lambdaArity);
			isDefault = (arguments != null && arguments.isEmpty());
			prefix = ",[";
			postfix = "]";
		}
		if (lambdaArity >= 0) {
			buffer.append(",[");
			String finals = htLocalFinals.get(getNormalizedKey(javaClass));
			String params = getLambdaParamList(constructorMethodBinding, lambdaArity);
			if (finals != null) {
				buffer.append("this,").append(finals);
				if (params.length() > 0)
					buffer.append(",");
			}
			buffer.append(params).append("]");
		} else if (!isDefault) {
			IMethodBinding constructorMethodDeclaration = (constructorMethodBinding == null ? null
					: constructorMethodBinding.getMethodDeclaration());
			addMethodParameterList(arguments, constructorMethodDeclaration, prefix, postfix, METHOD_CONSTRUCTOR);
		}
		buffer.append(")");
	}

	/**
	 * Start a new Clazz.new_() call for class creation or inner classes. Uses
	 * Clazz.load for dynamic loading
	 * 
	 * @param javaClassName            the class name to use if there is no
	 *                                 anonymous class name
	 * @param anonJavaName             the name of the anonymous class or lambda
	 *                                 class that has just been defined
	 * @param constructorMethodBinding the specified constructor for this
	 *                                 instantiation
	 * @param lambdaArity              parameterTypes[].length for a lambda method
	 *                                 or -1 for non-lambda classes
	 */
	private void openNew(ITypeBinding javaClass, String javaClassName, String anonJavaName,
			IMethodBinding constructorMethodBinding, int lambdaArity) {
		buffer.append("Clazz.new_(");
		String finalQualifiedName;
		if (anonJavaName == null) {
			// not lambda
			finalQualifiedName = getFinalJ2SClassName(javaClassName, FINAL_PC);
			if (!finalQualifiedName.equals("C$"))
				finalQualifiedName = getFinalJ2SClassNameQualifier(null, javaClass, javaClassName, FINAL_ESCAPECACHE);
		} else {
			// use the lambda name directly without caching or changing.
			finalQualifiedName = getFinalJ2SClassName(anonJavaName, FINAL_P);
		}
		if (constructorMethodBinding == null) {
			// an interface instance, so no constructor
			buffer.append(finalQualifiedName + ".$init$");
			return;
		}
		String qName = getFinalMethodNameWith$Params(finalQualifiedName + ".c$", null, constructorMethodBinding, null,
				false, lambdaArity >= 0 ? METHOD_LAMBDA_C : METHOD_NOTSPECIAL);
		// if no parameters, we just give the name of the class, not the constructor
		buffer.append(qName.endsWith(".c$") ? finalQualifiedName : qName);
	}

	/**
	 * Construct the class name from a Type.
	 * 
	 * @param type
	 * @return
	 */
	private String getClassJavaNameForType(Type type) {
		if (type instanceof QualifiedType) {
			QualifiedType qualType = (QualifiedType) type;
			return getClassJavaNameForType(qualType.getQualifier()) + "." + qualType.getName().getIdentifier();
		}
		if (type instanceof ArrayType)
			return getClassJavaNameForType(((ArrayType) type).getElementType());
		if (type instanceof ParameterizedType)
			return getClassJavaNameForType(((ParameterizedType) type).getType());
		if (type instanceof SimpleType) {
			ITypeBinding binding = ((SimpleType) type).resolveBinding();
			return getJavaClassNameQualified(binding);
		}

		// PrimitiveType
		// WildcardType
		// other types?
		return null;
	}

	private Map<String, String> htLocalFinals = new HashMap<>();

//	private String localName; // temporary only

	/**
	 * Runnable r = new Runnable(){public void run(){System. out.println("OK");}};
	 * 
	 * or
	 * 
	 * Runnable r = () -> System. out.println("OK");
	 * 
	 * a class definition and an instantiation at the same time. "
	 * 
	 * @param node
	 * @param anonymousClassDeclaration
	 * @param binding
	 * @param javaInnerClassName        includes method-local class names and
	 *                                  anonymous class names
	 * @param isLambda
	 * @param isClassTrulyLocal
	 * 
	 * @return anonymous name only if there are no finals
	 */
	private String processLocalInstance(ASTNode node, ASTNode anonymousClassDeclaration, ITypeBinding binding,
			ITypeBinding innerClass, String javaInnerClassName, int lambdaType, boolean isClassTrulyLocal) {

		// In the case of local classes, the declaration is dissociated from the
		// instantiation, so we need to cache the final string "{m:m,b:b,...}" at
		// creation time and recover it here.

//		localName = null;
		String finals;
		
		if (isClassTrulyLocal) {
			// predefined by class creation step
			finals = htLocalFinals.get(getNormalizedKey(binding));
		} else {
			// lambda and anonymous classes are defined inline.
			finals = addInnerDeclaration(anonymousClassDeclaration == null ? node : anonymousClassDeclaration, binding,
					lambdaType, false, null);
			if (finals != null)
				allowClazzNewLambda = false;
		}
		IMethodBinding constructorDeclaration;
		String anonymousSuperclassName, anonName;
		if (lambdaType == NOT_LAMBDA) {
			// anonymous class
			IMethodBinding constructorBinding = ((ClassInstanceCreation) node).resolveConstructorBinding();
			anonymousSuperclassName = getJavaClassNameSuperNoBrackets(binding);
			if (anonymousSuperclassName != null)
				innerClass = binding.getSuperclass();
			constructorDeclaration = (constructorBinding == null || anonymousSuperclassName == null ? null
					: constructorBinding.getMethodDeclaration());
			// force direct .$init$() call rather than .c$()
			// only if the anonymous instance is for an interface
			anonName = (constructorDeclaration == null ? javaInnerClassName : null);
		} else {
			// force .$init$() call rather than .c$() for lambda classes
			constructorDeclaration = null;
			anonymousSuperclassName = null;
			anonName = getMyJavaClassNameLambda(false);
		}
		addInnerTypeInstance(node, innerClass, javaInnerClassName, null, finals, constructorDeclaration,
				anonymousSuperclassName, anonName);
		if (lambdaType != LAMBDA_METHOD && !isClassTrulyLocal)
			buffer.append(")"); // end of line (..., ...)
		return finals == null ? anonName : null;
	}

	/**
	 * Add the class declaration for inner, local, and lambda classes.
	 * 
	 * @param node
	 * @param declaration
	 * @param binding
	 * @param lambdaType
	 * @param isLocal
	 * @param bodyDeclarations
	 * @return
	 */
	private String addInnerDeclaration(ASTNode node, ITypeBinding binding, int lambdaType, boolean isLocal,
			List<?> bodyDeclarations) {
		boolean wasAnonymous = class_isAnonymousOrLocal;
		class_isAnonymousOrLocal = true;
		List<LocalVariable> lastVisitedVars = class_visitedVars;
		List<LocalVariable> myVisitedVars = class_visitedVars = new ArrayList<LocalVariable>();
		int currentBlock = package_currentBlockForVisit;
		package_currentBlockForVisit = package_blockLevel;
		package_methodStackForFinals.push(getNormalizedKey(binding));
		// localName = null;
		if (lambdaType != NOT_LAMBDA) {
			addClassOrInterface(node, binding, null, 'm');
			if (lambdaType == LAMBDA_METHOD)
				buffer.append("); return ");
			else
				buffer.append(", ");
		} else if (isLocal) {
			addClassOrInterface(node, binding, bodyDeclarations, 'l');
		} else {
			buffer.append("(");
			AnonymousClassDeclaration decl = (AnonymousClassDeclaration) node;
			addClassOrInterface(decl, decl.resolveBinding(), decl.bodyDeclarations(), 'a');
			buffer.append(", ");
		}
		package_methodStackForFinals.pop();
		// create the finals list {width: width; height: height} and add all
		// newly visited variables to class_visitedVars;
		String finals = listFinalVariables(myVisitedVars, lastVisitedVars, currentBlock);
		// restore class/package parameters
		class_visitedVars = lastVisitedVars;
		package_currentBlockForVisit = currentBlock;
		class_isAnonymousOrLocal = wasAnonymous;
		return finals;
	}
	
	static int test = 0;
	

	/**
	 * Generated final variable list for anonymous class creation. Update the
	 * lastVisitedVars list
	 * 
	 * @param visitedVars     the list of all variables that have been visited as
	 *                        final for this class or any of its inner classes
	 * @param lastVisitedVars
	 * @param currentBlock
	 * @param seperator
	 * @param scope
	 * @return
	 */
	private String listFinalVariables(List<LocalVariable> visitedVars, List<LocalVariable> lastVisitedVars,
			int currentBlock) {
		if (visitedVars.size() == 0)
			return null;
		String scope = (package_methodStackForFinals.size() == 0 ? null : (String) package_methodStackForFinals.peek());
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (Iterator<LocalVariable> iter = visitedVars.iterator(); iter.hasNext();) {
			iter.next().appendToBuffer(scope, buf);
			if (iter.hasNext()) {
				buf.append(", ");
			}
		}
		buf.append("}");

		// add the visited variables into last visited variables
		if (currentBlock >= 0) {
			for (int j = visitedVars.size(); --j >= 0;) {
				String javaName = visitedVars.get(j).getJavaName();
				for (int i = package_finalVars.size(); --i >= 0;) {
					LocalVariable v = package_finalVars.get(i);
					if (v.isFinalAccess(javaName, currentBlock) && !lastVisitedVars.contains(v))
						lastVisitedVars.add(v);
				}
			}
		}
		return buf.toString();
	}

	public boolean visit(ConstructorInvocation node) {
		IMethodBinding constructorBinding = node.resolveConstructorBinding();
		List<?> arguments = node.arguments();
		buffer.append(getFinalMethodNameWith$Params("C$.c$", null, constructorBinding, null, false, METHOD_NOTSPECIAL))
				.append(".apply(this");
		IMethodBinding methodDeclaration = (constructorBinding == null ? null
				: constructorBinding.getMethodDeclaration());
		addMethodParameterList(arguments, methodDeclaration, ", [", "]", METHOD_CONSTRUCTOR);
		buffer.append(");\r\n");
		return false;
	}

	public boolean visit(ContinueStatement node) {
		buffer.append("continue");
		addLabel(node.getLabel(), false);
		return false;
	}

	/**
	 * break foo; continue foo; foo: for/while/do
	 * 
	 * $var: ....
	 * 
	 * @param label
	 * @param isDefining
	 */
	private void addLabel(SimpleName label, boolean isDefining) {
		if (label != null) {
			buffer.append(' ');
			buffer.append(NameMapper.getJavaScriptCollisionIdentifier(label.getIdentifier(), true));
		}
		buffer.append(isDefining ? " : " : ";\r\n");
	}

	public boolean visit(DoStatement node) {
		buffer.append("do ");
		node.getBody().accept(this);
		buffer.append(" while (");
		node.getExpression().accept(this);
		buffer.append(");\r\n");
		return false;
	}

	public boolean visit(EmptyStatement node) {
		buffer.append(";");
		return false;
	}

	public boolean visit(EnhancedForStatement node) {
		// for (Integer v : ...)
		SimpleName name = node.getParameter().getName();
		ITypeBinding vtype = name.resolveTypeBinding();
		buffer.append("for (var ");
		String varName = acceptPossiblyFinalVar(name, 1);
		appendReplaceV(", $V = ", "V", varName, null, null);
		Expression exp = node.getExpression();
		ITypeBinding eType = exp.resolveTypeBinding();
		ITypeBinding arrayType = eType.getComponentType();

		if (arrayType == null) {
			exp.accept(this);
			appendReplaceV(".iterator$(); $V.hasNext$()&&((V=", "V", varName, null, null);
			appendReplaceV("($V.next$())", "V", varName, vtype, eType);
			appendReplaceV("),1);", "V", varName, null, null);
		} else {
			appendReplaceV("0, $$V = ", "V", varName, null, null);
			exp.accept(this);
			appendReplaceV("; $V<$$V.length&&((V=", "V", varName, null, null);
			appendReplaceV("($$V[$V])", "V", varName, vtype, arrayType);
			appendReplaceV("),1);$V++", "V", varName, null, null);
		}
		buffer.append(") ");
		node.getBody().accept(this);
		buffer.append("\r\n");
		return false;
	}

	/**
	 * For enhanced FOR only.
	 * 
	 * allow for primitive boxing or unboxing. See test.Test_Chars.java
	 * 
	 * @param template
	 * @param v
	 * @param varName
	 * @param vType
	 * @param eType
	 */
	private void appendReplaceV(String template, String v, String varName, ITypeBinding vType, ITypeBinding eType) {
		String s = template.replace(v, varName);
		if (vType != eType) {
			if (vType.isPrimitive()) {
				if (eType.isPrimitive()) {
					// this is a conversion of an char[] to an int -- the only
					// possibility allowed, I think
					s += CHARCODEAT0;
				} else {
					// So we know the expression is boxed -- Character, for
					// example
					// Character does not use .objectValue, but we implement it
					// here
					if (vType.getName().equals("int"))
						s += ".intValue$()";
					else
						s += ".objectValue$()";
				}
			} else if (eType.isPrimitive()) {
				// So we know the expression is unboxed -- char, for example
				s = "new " + NameMapper.getPrimitiveTYPE(eType.getName()) + s;
			}
		}
		buffer.append(s);
	}

	public boolean visit(EnumConstantDeclaration node) {
		// see addEnumConstants
		return false;
	}

	public boolean visit(EnumDeclaration node) {
		setPackage(null);
		addClassOrInterface(node, node.resolveBinding(), node.bodyDeclarations(), 'e');
		return false;
	}

	public boolean visit(ExpressionStatement node) {
		// e.g. test.Test_Anon.main(args);
		return true;
	}

	public void endVisit(ExpressionStatement node) {
		buffer.append(";\r\n");
	}

	@SuppressWarnings("unchecked")
	public boolean visit(ForStatement node) {

		buffer.append("for (");
		visitList(node.initializers(), ", ");
		buffer.append("; ");
		Expression expression = node.getExpression();
		if (expression != null) {
			expression.accept(this);
		}
		buffer.append("; ");
		visitList(node.updaters(), ", ");
		buffer.append(") ");
		node.getBody().accept(this);
		buffer.append("\r\n");
		return false;
	}

	public boolean visit(IfStatement node) {
		buffer.append("if (");
		appendBoxingNode(node.getExpression(), false);
		buffer.append(") ");
		node.getThenStatement().accept(this);
		Statement ifElse = node.getElseStatement();
		if (ifElse != null) {
			buffer.append(" else ");
			ifElse.accept(this);
		}
		return false;
	}

	/**
	 * {....}
	 * 
	 */
	public boolean visit(Initializer node) {
		// handled by addClassOrInterface
		return false;
	}

	/**
	 * out: while/for/do...
	 * 
	 */
	public boolean visit(LabeledStatement node) {
		addLabel(node.getLabel(), true);
		node.getBody().accept(this);
		return false;
	}

	public boolean visit(MethodDeclaration node) {
		// handled in addClassorInterface
		return false;
	}

	private final static int METHOD_FULLY_QUALIFIED = 0;
	private final static int METHOD_$_QUALIFIED = 1;
	private final static int METHOD_UNQUALIFIED = 2;

	/**
	 * Called by visit(MethodDeclaration) as well as addLambdaMethod().
	 * 
	 * @param mBinding
	 * @param parameters
	 * @param body
	 * @param isConstructor
	 * @param lambdaType
	 */
	@SuppressWarnings("null")
	private void processMethodDeclaration(IMethodBinding mBinding, List<ASTNode> parameters, ASTNode body,
			boolean isConstructor, int lambdaType) {
		int mods = mBinding.getModifiers();
		boolean isNative = Modifier.isNative(mods);
		if (body == null && !isNative && lambdaType == NOT_LAMBDA) {
			// Abstract method
			return;
		}
		boolean isPublic = Modifier.isPublic(mods);
		boolean isPrivate = !isPublic && !isConstructor && isPrivate(mBinding);
		boolean isStatic = isStatic(mBinding);
		int qualification = (lambdaType != NOT_LAMBDA ? METHOD_FULLY_QUALIFIED
				: temp_add$UnqualifiedMethod ? METHOD_$_QUALIFIED : METHOD_FULLY_QUALIFIED);
		if (isUserApplet && lambdaType == NOT_LAMBDA && !isConstructor && !isStatic && isPublic)
			qualification |= METHOD_UNQUALIFIED;
		String finalName = getFinalMethodNameOrArrayForDeclaration(mBinding, isConstructor, qualification);
		boolean isMain = isStatic && isPublic && mBinding.getName().equals("main")
				&& mBinding.getKey().endsWith(";.main([Ljava/lang/String;)V");
		if (isMain) {
			addApplication();
		}
		if (global_lstMethodsDeclared != null && !isPrivate)
			logMethodDeclared(finalName);

		String key = getNormalizedKey(mBinding);
		package_methodStackForFinals.push(key);
		try {
			ITypeBinding mClass = mBinding.getDeclaringClass();
			if (isConstructor && finalName.equals("'c$'")
					|| mBinding.isVarargs() && mBinding.getParameterTypes().length == 1)
				haveDefaultConstructor = true; // in case we are not qualifying
												// names here
			buffer.append("\r\nClazz.newMeth(C$, ").append(finalName).append(", function (");
			if (parameters == null)
				// lambda method
				buffer.append(getLambdaParamList(mBinding, -1));
			else
				visitList(parameters, ", ");
			buffer.append(") ");
			if (lambdaType != NOT_LAMBDA) {
				addLambdaBody(body);
				if (body == null)
					return;
			} else if (isConstructor) {
				@SuppressWarnings("unchecked")
				List<ASTNode> statements = ((Block) body).statements();
				ASTNode firstStatement;
				if (statements.size() == 0
						|| !((firstStatement = statements.get(0)) instanceof SuperConstructorInvocation)
								&& !(firstStatement instanceof ConstructorInvocation)) {
					buffer.append("{\r\n");
					String superclassName = getJavaClassNameSuperNoBrackets(mClass);
					if (superclassName == null)
						addCallInit();
					else
						addSuperConstructor(null, null);
					package_blockLevel++;
					visitList(statements, "");
					endVisit((Block) body);
				} else {
					body.accept(this);
				}
			} else if (body == null) {
				// not a constructor and no body -- native
				buffer.append("{\r\n");
				if (isNative) {
					buffer.append("alert('native method must be replaced! " + key + "');\r\n");
					log("native: " + key);
				}
				buffer.append("}\r\n");
			} else {
				body.accept(this);
			}
			if (isStatic || isConstructor)
				buffer.append(", ").append(isNative ? 2 : 1);
			else if (isPrivate)
				buffer.append(", " + getPrivateVar(mClass, false));
			buffer.append(");\r\n");
		} finally {
			package_methodStackForFinals.pop();
			removeVariableFinals(mBinding, parameters);
		}
	}

	/**
	 * Accept the SimpleName to add it to the buffer, and to get its final form.
	 * Then create a package_finalVars entry for this name if it is final or
	 * effectively final.
	 * 
	 * @param name
	 * @param offset 0 (this block) or 1 (upcoming block)
	 * @return the final form of the name
	 */
	private String acceptPossiblyFinalVar(SimpleName name, int offset) {
		int pt = buffer.length();
		name.accept(this);
		String j2sName = buffer.substring(pt);
		IBinding binding = name.resolveBinding();
		if (binding != null && isFinalOrEffectivelyFinal(binding))
			package_finalVars.add(new LocalVariable(package_blockLevel + offset, name.getIdentifier(),
					package_methodStackForFinals.size() == 0 ? null : package_methodStackForFinals.peek(), j2sName));
		return j2sName;
	}

	private static boolean isFinalOrEffectivelyFinal(IBinding binding) {
		return Modifier.isFinal(binding.getModifiers())
				|| binding instanceof IVariableBinding && ((IVariableBinding) binding).isEffectivelyFinal();
	}

	private void removeVariableFinals(IMethodBinding mBinding, List<ASTNode> parameters) {
		if (parameters == null)
			return;
		String methodSig = mBinding.getKey();
		for (int i = parameters.size() - 1; i >= 0; i--) {
			SimpleName name = ((VariableDeclaration) parameters.get(i)).getName();
			IBinding binding = name.resolveBinding();
			if (binding != null) {
				String identifier = name.getIdentifier();
				LocalVariable f = new LocalVariable(package_blockLevel + 1, identifier, methodSig,
						getFinalVarName(identifier));
				if (isFinalOrEffectivelyFinal(binding))
					package_finalVars.remove(f);
				class_visitedVars.remove(f);
			}
		}
	}

	private void clearVariables(List<LocalVariable> vars) {
		for (int i = vars.size(); --i >= 0;) {
			LocalVariable var = vars.get(i);
			if (var.getBlockLevel() >= package_blockLevel) {
				vars.remove(i);
			}
		}
	}

	public boolean visit(MethodInvocation node) {
		Expression expression = node.getExpression();
		IMethodBinding mBinding = node.resolveMethodBinding();
		addMethodInvocation(node.getName(), node.arguments(), mBinding, expression, -1);
		return false;
	}

	/**
	 * Called by visit(MethodDeclaration) as well as addLambdaMethod().
	 * 
	 * @param javaQualifier
	 * @param arguments
	 * @param mBinding
	 * @param expression
	 */
	private void addMethodInvocation(SimpleName javaQualifier, List<?> arguments, IMethodBinding mBinding,
			Expression expression, int lambdaArity) {
		if (javaQualifier == null) {
			// not possible?
			dumpStack("addMethodInvocation null simpleJavaName " + mBinding + " " + expression + " " + lambdaArity);
		}
		String methodName = mBinding.getName();
		ITypeBinding declaringClass = mBinding.getDeclaringClass();
		boolean isStatic = isStatic(mBinding);
		boolean isPrivate = isPrivate(mBinding);
		boolean isPrivateAndNotStatic = isPrivate && !isStatic;
		String privateVar = (isPrivateAndNotStatic ? getPrivateVar(declaringClass, false) : null);
		boolean doLog = (!isPrivate && global_htMethodsCalled != null);
		boolean needBname = (!isStatic && lambdaArity < 0 && (expression == null
				? !areEqual(declaringClass, class_typeBinding)
						&& !class_typeBinding.isAssignmentCompatible(declaringClass)
				: expression instanceof ThisExpression && ((ThisExpression) expression).getQualifier() != null));
		String bname = (needBname ? getThisRefOrSyntheticReference(javaQualifier, declaringClass, null) : null);
		// add the qualifier
		int pt = buffer.length();
		if (isPrivateAndNotStatic) {
			// note that the following expression will not work if the method is private:
			// (b ? classA : classB).method()
			// because we don't know at compile time which class is being run.
			buffer.append(privateVar);
			buffer.append(".");
		} else if (lambdaArity >= 0) {
			doLog = false;
			if (bname != null)
				allowClazzNewLambda = false;
		} else if (expression == null) {
			doLog = false;
			if (bname != null) {
				buffer.append(bname);
				buffer.append(".");
			} else if (!isStatic) {
				buffer.append("this.");
			} else {
				// this will be C$., I think.
			}
		} else {
			appendFinalMethodQualifier(expression, declaringClass, bname,
					(isStatic && !isPrivate ? FINAL_ESCAPECACHE : FINAL_CACHE));
			buffer.append(".");
		}
		// keep a pointer, because we may rewrite this
		int ptLog = (doLog ? buffer.length() : 0);

		// check for special Clazz.array or Clazz.forName
		// as well as special treatment for String.indexOf and String.lastIndexOf

		boolean isSpecialMethod = false;
		boolean isIndexOf = false;
		String declaringClassJavaClassName = getJavaClassNameQualified(declaringClass);
		if (lambdaArity < 0 && !isPrivateAndNotStatic) {
			String j2sName = NameMapper.getJ2SFinalMapClazzMethod(declaringClassJavaClassName, methodName);
			if (j2sName != null) {
				isSpecialMethod = true;
				// overwrite qualifier
				buffer.setLength(pt);
				buffer.append(j2sName);
				doLog = false;
				bname = null;
			} else if (declaringClassJavaClassName.equals("java.lang.String")
					&& (methodName.equals("indexOf") || methodName.equals("lastIndexOf"))) {
				// indicate to boxer method to use method "q" not "p" here.
				// This allows characters to be left as strings in String.indexOf$I and
				// String.lastIndexOf$I for faster processing.
				isIndexOf = true;
			}
		}

		// have xxxx.

		// now get the method name

		// record whether this.b$[.....] was used, and if so and it is private,
		// we need to use it again
		String term = ")";
		if (!isSpecialMethod) {
			// TODO: for now we are just returning name$ for all lambda methods
			// note that simpleNameInMethodBinding can return C$.xxxx

			String j2sName = getFinalDotQualifiedNameForMethod(javaQualifier, mBinding,
					(expression == null ? METHOD_NULLEXPRESSION : 0) | METHOD_ISQUALIFIED
							| (lambdaArity >= 0 ? LAMBDA_METHOD : 0));

			String finalMethodNameWith$Params = getFinalMethodNameWith$Params(j2sName, declaringClassJavaClassName,
					mBinding, null, true, METHOD_NOTSPECIAL);
			if (lambdaArity >= 0) {
				// The problem here is that we cannot apply a method from an interface
				// because those methods are not present in JavaScript.
				// Otherwise we would use $class$.prototype here. The assumption
				// in using "t.apply" is that t must be non-null.
				// if this does not work, then we can go to ($class$.prototype || t)
				//
				boolean classIsTarget = ((isStatic || isVariableBinding(expression)
						|| expression instanceof ClassInstanceCreation) // BH Added 2019.05.13
						&& lambdaArity == mBinding.getParameterTypes().length);
				buffer.append((classIsTarget ? "$class$." : "t.") + finalMethodNameWith$Params + ".apply("
						+ (isStatic ? "null" : classIsTarget ? "$class$" : "t") + ",[");
				buffer.append(getLambdaParamList(mBinding, lambdaArity));
				buffer.append("])");
				return;
			}
			if (finalMethodNameWith$Params.indexOf('|') >= 0) {
				// cover multiple parameter options to cover older versions of java
				// foo.xx$T$K || $o$.xx$O$O --> ($o$=foo).($o$.xx$T$K ||
				// $o$.xx$O$O)
				doLog = false;
				postFixGeneric$OMethodName(pt, finalMethodNameWith$Params, isPrivateAndNotStatic, privateVar);
				term = "])";
			} else {
				// standard call
				buffer.append(finalMethodNameWith$Params);
			}

			if (doLog) {
				String name = declaringClassJavaClassName + "." + buffer.substring(ptLog);
				logMethodCalled(name);
			}
			if (isPrivateAndNotStatic || bname != null) {
				// A call to a nonprivate outer-class method from an inner class
				// requires using apply on the object, which is either
				// this or this.b$["....."]
				//
				// all private calls are made to var p$x

				buffer.append(".apply(");
				appendFinalMethodQualifier(expression, declaringClass, bname, FINAL_CACHE);
				buffer.append(", [");
				term = "])";
			}
		}
		if (term == ")")
			buffer.append("(");
		addMethodParameterList(arguments, mBinding, null, null, isIndexOf ? METHOD_INDEXOF : METHOD_NOTSPECIAL);
		buffer.append(term);
	}

	/**
	 * Write the method expression to the buffer. If the qualifier is a variable,
	 * such as System.out, or an expression, such as (useF? f : g), then we can
	 * simply accept it. Othewise we need to qualify it.
	 * 
	 * @param qualifier      null for "this" or outer-class reference;
	 * 
	 * @param declaringClass
	 * @param bname
	 * @param flags          FINAL_LAMBDA | FINAL_ESCAPE | FINAL_CACHE
	 */
	private void appendFinalMethodQualifier(Expression qualifier, ITypeBinding declaringClass, String bname,
			int flags) {
		if (qualifier == null) {
			if ((flags & FINAL_LAMBDA) != 0) {
				buffer.append(getFinalJ2SClassNameQualifier(null, declaringClass, null, flags));
			} else {
				buffer.append(bname == null ? "this" : bname);
			}
		} else if (qualifier instanceof Name && !isVariableBinding(qualifier)) {
			buffer.append(getFinalJ2SClassNameQualifier((Name) qualifier, declaringClass, null, flags));
		} else {
			// xxxx.field.foo()
			// (x ? y : z).foo()
			// xxx.this.foo()
			qualifier.accept(this);
		}
	}

	private static boolean isVariableBinding(Expression qualifier) {
		return qualifier instanceof Name && ((Name) qualifier).resolveBinding() instanceof IVariableBinding;
	}

	public boolean visit(ReturnStatement node) {
		buffer.append("return");
		Expression expression = node.getExpression();
		if (expression != null) {
			buffer.append(' ');

			ASTNode parent = node.getParent();
			while (parent != null && !(parent instanceof MethodDeclaration)) {
				parent = parent.getParent();
			}
			IMethodBinding mBinding = (parent == null ? null : ((MethodDeclaration) parent).resolveBinding());
			ITypeBinding retType = (mBinding == null ? null : mBinding.getReturnType());
			addExpressionAsTargetType(expression, retType, "r", null);
		}
		buffer.append(";\r\n");
		return false;
	}

	/**
	 * method parameters or catch variables
	 */
	public boolean visit(SingleVariableDeclaration node) {
		acceptPossiblyFinalVar(node.getName(), 1);
		return false;
	}

	public boolean visit(SuperConstructorInvocation node) {
		IMethodBinding constructorBinding = node.resolveConstructorBinding();
		if (constructorBinding != null && !isObjectOrNull(constructorBinding.getDeclaringClass())) {
			addSuperConstructor(node, constructorBinding.getMethodDeclaration());
		} else {
			addCallInit();
		}
		return false;
	}

	public boolean visit(SuperMethodInvocation node) {
		IMethodBinding mBinding = node.resolveMethodBinding();
		String finalMethodNameWith$Params = getFinalMethodNameWith$Params(null, null, mBinding, null, false,
				METHOD_NOTSPECIAL);
		// BH if this is a call to super.clone() and there is no superclass, or
		// the superclass is Object,
		// then we need to invoke Clazz.clone(this) directly instead of calling
		// C$.superclazz.clone()
		if ("clone$".equals(finalMethodNameWith$Params) && getJavaClassSuper(mBinding.getDeclaringClass()) == null) {
			buffer.append("Clazz.clone(this)");
		} else {
			buffer.append("C$.superclazz.prototype." + finalMethodNameWith$Params + ".apply(this, ");
			buffer.append("[");
			addMethodParameterList(node.arguments(), mBinding, null, null, METHOD_NOTSPECIAL);
			buffer.append("])");
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean visit(SwitchStatement node) {
		buffer.append("switch (");
		addNonCharacter(node.getExpression());
		buffer.append(") {\r\n");
		visitList(node.statements(), "");
		buffer.append("}\r\n");
		return false;
	}

	public boolean visit(SwitchCase node) {
		if (node.isDefault()) {
			buffer.append("default");
		} else {
			buffer.append("case ");
			addNonCharacter(node.getExpression());
		}
		buffer.append(":\r\n");
		return false;
	}

	public boolean visit(SynchronizedStatement node) {
		// we could wrap this with a simple if() statement,
		// checking that it is not null, but that seems to me
		// to be unnecessary. When would one ever intentionally
		// produce a null pointer exception from synchronized(...)?

		Expression e = node.getExpression();
		if (!(e instanceof Name || e instanceof TypeLiteral || e instanceof ThisExpression)) {
			buffer.append("/*sync " + e.getClass().getName() + "*/");
			// get actual JavaScript code
			int pt = buffer.length();
			e.accept(this);
			String expr = buffer.substring(pt, buffer.length());
			buffer.setLength(pt);
			// ignore (treeLock())
			if (!(e instanceof MethodInvocation && expr.indexOf(".getTreeLock()") >= 0)) {
				buffer.append("(");
				buffer.append(expr);
				buffer.append(");\r\n");
			}
		}
		node.getBody().accept(this);
		return false;
	}

	public boolean visit(ThrowStatement node) {
		buffer.append("throw ");
		node.getExpression().accept(this);
		buffer.append(";\r\n");
		return false;
	}

	@SuppressWarnings({ "unchecked", "null" })
	public boolean visit(TryStatement node) {
		List<CatchClause> catchClauses = node.catchClauses();
		int size = catchClauses.size();
		Block finallyBlock = node.getFinally();
		buffer.append(size > 0 || finallyBlock != null ? "try " : "/*try*/ ");
		List<ASTNode> resources = node.resources();
		// Returns the live ordered list of resources for this try statement (added in
		// JLS4 API).
		// [ooh...JSL9 change...]
		// A resource is either a VariableDeclarationExpression or (since JLS9) a Name.

		int pt = (resources == null || resources.size() == 0 ? -1 : buffer.length());
		node.getBody().accept(this);
		if (pt >= 0) {
			// just after first "{"
			String buf = buffer.substring((pt = buffer.indexOf("{", pt) + 1));
			buffer.setLength(pt);
			for (int i = 0; i < resources.size(); i++) {
				ASTNode resource = resources.get(i);
				if (resource instanceof VariableDeclarationExpression) {
					resource.accept(this);
					buffer.append(";");
				} else {
					// Java 9 -- just a Name.
					buffer.append("/* Java 9 resource " + resource + "*/\r\n");
				}
			}
			buffer.append(buf);
		}
		if (size > 0) {
			String catchEName = "e$$";
			if (size == 1) {
				CatchClause element = catchClauses.get(0);
				SimpleName exName = element.getException().getName();
				catchEName = exName.getIdentifier();
			}
			buffer.append(" catch (" + catchEName + ") ");
			boolean scopeAdded = false;
			boolean endedWithThrowable = false;
			for (Iterator<CatchClause> iter = catchClauses.iterator(); iter.hasNext();) {
				CatchClause element = iter.next();
				List<Type> types;
				Type type = element.getException().getType();
				if (type instanceof UnionType) {
					types = ((UnionType) type).types();
				} else {
					(types = new ArrayList<Type>()).add(type);
				}
				boolean haveType = false;
				for (int j = 0; j < types.size(); j++) {
					type = types.get(j);
					if ("java.lang.Throwable".equals(type.resolveBinding().getQualifiedName())) {
						endedWithThrowable = true;
					} else {
						if (!scopeAdded) {
							buffer.append("{\r\n");
							scopeAdded = true;
						}
						buffer.append(haveType ? " || " : "if (");
						buffer.append("Clazz.exceptionOf(" + catchEName + ",\"");
						buffer.append(getFinalJ2SClassName(getClassJavaNameForType(type), FINAL_RAW));
						buffer.append("\")");
						haveType = true;
					}
				}
				if (haveType)
					buffer.append(")");
				SimpleName exName = element.getException().getName();
				String eName = exName.getIdentifier();
				boolean notEName = false;
				if (!catchEName.equals(eName)) {
					buffer.append("{\r\nvar " + eName + " = " + catchEName + ";\r\n");
					notEName = true;
				}
				element.getBody().accept(this);
				if (notEName) {
					buffer.append("\r\n}");
				}
				if (iter.hasNext()) {
					buffer.append(" else ");
				}
			}
			if (!endedWithThrowable) {
				buffer.append(" else {\r\nthrow " + catchEName + ";\r\n}");
			}
			if (scopeAdded) {
				buffer.append("\r\n}");
			}
		}
		if (finallyBlock != null) {
			buffer.append(" finally ");
			finallyBlock.accept(this);
		}
		buffer.append("\r\n");
		return false;
	}

	/**
	 * A class or interface is being declared.
	 * 
	 * 
	 */
	public boolean visit(TypeDeclaration node) {
		setPackage(null);
		// anonymous will never come through here. It will be routed directly to
		// addClassOrInterface
		List<?> declarations = node.bodyDeclarations();
		ITypeBinding binding = node.resolveBinding();
		addClassOrInterface(node, binding, declarations,
				node.isInterface() ? 'i' : node.isLocalTypeDeclaration() ? 'l' : 'c');
		return false;
	}

	/**
	 * <pre>
	 * VariableDeclarationStatement:
	 *    { ExtendedModifier } Type VariableDeclarationFragment
	 *        { <b>,</b> VariableDeclarationFragment } <b>;</b>
	 * </pre>
	 */
	public boolean visit(VariableDeclarationStatement node) {

		@SuppressWarnings("unchecked")
		List<ASTNode> fragments = node.fragments();
		for (Iterator<ASTNode> iter = fragments.iterator(); iter.hasNext();) {
			buffer.append("var ");
			ASTNode next = iter.next();
			next.accept(this);
			buffer.append(";\r\n");
		}
		return false;
	}

	public boolean visit(WhileStatement node) {

		buffer.append("while (");
		node.getExpression().accept(this);
		buffer.append(")");
		node.getBody().accept(this);
		buffer.append("\r\n");
		return false;
	}

	////////// END visit/endVisit ///////////

	private static final int FIELD_DECL_STATIC_NONDEFAULT = 1;
	private static final int FIELD_DECL_STATIC_DEFAULTS = 2;
	private static final int FIELD_DECL_NONSTATIC_ALL = 3;

	private void addAnonymousFunctionWrapper(boolean isOpen) {
		buffer.append(
				isOpen ? (buffer.lastIndexOf(")") >= buffer.length() - 3 ? ";" : "") + "\r\n(function(){" : "})()\r\n");
	}

	/**
	 * The call to C$.$init$ from a constructor is made in two cases:
	 * 
	 * a) as the second statement in a constructor, when the first line is a
	 * super(...) call
	 * 
	 * b) as the first statement in a constructor that does not call super(...) or
	 * this(...) (because the superclass is Object)
	 * 
	 * Note that it is not called when the first statement is this(...).
	 * 
	 */
	private void addCallInit() {
		buffer.append("C$.$init$.apply(this);\r\n");
	}

	/**
	 * Add Clazz.newInterface(...) or Clazz.newClass(...) for all classes and
	 * interfaces, including Enum and anonymous.
	 * 
	 * If this is an inner class, then iterate, just adding its definition to the
	 * current staticBuffer;
	 * 
	 * @param node
	 * @param binding
	 * @param BodyDeclarations
	 * @param type             'a' (anonymous class), 'e' (Enum), 'i' (Interface),
	 *                         'l' (local), 'm' (LambdaExpression), or 'c' (standard
	 *                         class)
	 * @return localName
	 */
	@SuppressWarnings({ "null", "unchecked" })
	private void addClassOrInterface(ASTNode node, ITypeBinding binding, List<?> bodyDeclarations, char type) {
		if (binding == null)
			return;

		checkGenericClass(binding, binding);

		ASTNode parent = node.getParent();

		boolean isEnum = (type == 'e');
		boolean isInterface = (type == 'i');
		boolean isLocal = (type == 'l');
		boolean isLambda = (type == 'm');
		boolean isAnonymous = (type == 'a' || isLambda);
		boolean isClass = (type == 'c');
		boolean isTopLevel = (!isLambda && binding.isTopLevel());

		if (!isTopLevel && !isAnonymous && node != innerNode) {
			// inner named class first pass only

			// includes EnumDeclaration

			// We use a temporary visitor to put the code
			// into a temporary buffer, produce the code in the
			// temporary visitor, append it to our TrailingBuffer,
			// and return.

			// get the visitor

			Java2ScriptVisitor tempVisitor = null;
			try {
				tempVisitor = getClass().newInstance().setProject(global_project, global_testing).setInnerGlobals(this,
						node);
			} catch (@SuppressWarnings("unused") Exception e) {
				// impossible
			}

			// set its className and binding

			String className;
			if (parent instanceof TypeDeclarationStatement) {
				String anonClassName = removeBrackets(getJavaClassNameQualified(binding));
				className = anonClassName.substring(anonClassName.lastIndexOf('.') + 1);
				// note - this className is NOT QUALIFIED
			} else {
				className = class_shortName + "." + binding.getName();
			}
			tempVisitor.setClassAndBinding(className, binding);

			// generate the code

			if (isLocal) {
				String finals = tempVisitor.addInnerDeclaration(node, binding, NOT_LAMBDA, true, bodyDeclarations);
				// when the class is called, we will need these
				htLocalFinals.put(getNormalizedKey(binding), finals);
			} else {
				tempVisitor.addClassOrInterface(node, binding, bodyDeclarations, type);
			}

			// append it to our TrailingBuffer

			trailingBuffer.append(tempVisitor.buffer.toString());

			return;
		}

		// set up key fields and local variables

		if (isLocal || isClass || isEnum) {
			checkAnnotations((BodyDeclaration) node, CHECK_ANNOTATIONS_ONLY);
		}
		ITypeBinding oldBinding = null;
		String oldShortClassName = null, this$0Name0 = null, finalShortClassName, finalPackageName;
		if (isTopLevel) {
			String javaName = binding.getName();
			appendElementKey(javaName);
			setClassAndBinding(javaName, binding);
			if (isClass)
				isUserApplet = checkAddApplet(binding);
		}
		if (isAnonymous) {
			oldShortClassName = class_shortName;
			oldBinding = class_typeBinding;
			// anonymous classes reference their package, not their outer class in
			// Clazz.newClass, so clazz.$this$0 is not assigned.
			this$0Name0 = this$0Name;
			this$0Name = null;
			finalShortClassName = getFinalJ2SClassName(
					(isLambda ? getMyJavaClassNameLambda(true) : getJavaClassNameQualified(binding)), FINAL_P);
			if (finalShortClassName.startsWith("P$.")) {
				// java.lang.x will return x, not P$.x
				finalShortClassName = finalShortClassName.substring(3);
			}
			setClassAndBinding(finalShortClassName, binding);
			if (isLambda)
				buffer.append("(");
			buffer.append("(P$." + finalShortClassName + "||");
			finalPackageName = "P$";
		} else {
			// Top or inner named classes are already set.
			// For inner classes, we are just loading up the temporary buffer.
			// The code will end up in the trailing buffer for the parent class.
			int pt1 = class_fullName.lastIndexOf('.');
			finalShortClassName = class_fullName.substring(pt1 + 1);
			finalPackageName = checkPackageP$Name(class_fullName.substring(0, pt1));
		}

		// add the anonymous wrapper if needed

		if (!isTopLevel) {
			addAnonymousFunctionWrapper(true);
		}

		// begin the class or interface definition

		buffer.append("var C$=" + (isInterface ? "Clazz.newInterface(" : "Clazz.newClass("));

		// arg1 is the package name
		// arg2 is the full class name in quotes
		// arg3 is the class definition function, C$, which is called in
		// Clazz.new_().
		// arg4 is the superclass
		// arg5 is the superinterface(s)
		// arg6 is the type: anonymous(1), local(2), or absent

		// Here we go...

		// arg1: package name or null
		// arg2: shortened class name in quotes

		buffer.append(finalPackageName + ", \"" + finalShortClassName + "\"");

		// set up func, superclass, and superInterface

		String func = "null";
		List<?> superInterfaceTypes = null;

		// arg3: class definition function, C$, or null to add the standard
		// function at run time

		boolean hasDependents = isEnum;
		buffer.append(", ");
		List<IMethodBinding> unqualifiedMethods = getUnqualifiedMethods(binding, null);
		List<AbstractTypeDeclaration> innerClasses = new ArrayList<>();
		if (isAnonymous) {
			if (!(parent instanceof EnumConstantDeclaration))
				func = "function(){Clazz.newInstance(this, arguments[0],1,C$);}";
			ITypeBinding[] declaredTypes = null;
			if (isLambda) {
				// we just have this one superinterface, which is the lambda binding itself
				declaredTypes = new ITypeBinding[] { binding };
			} else {
				declaredTypes = binding.getInterfaces();
			}
			if (declaredTypes != null && declaredTypes.length > 0) {
				List<ITypeBinding> types = new ArrayList<ITypeBinding>();
				ITypeBinding anonClassImplemented = declaredTypes[0];
				types.add(anonClassImplemented);
				superInterfaceTypes = types;
			}
		} else {
			for (Iterator<?> iter = bodyDeclarations.iterator(); iter.hasNext();) {
				BodyDeclaration bd = (BodyDeclaration) iter.next();
				if (bd instanceof TypeDeclaration || bd instanceof EnumDeclaration) {
					innerClasses.add((AbstractTypeDeclaration) bd);
				}
			}
			if (!isTopLevel || !innerClasses.isEmpty()) {
				func = null;
				buffer.append("function(){\r\n");

				// add all inner classes iteratively

				for (int i = 0; i < innerClasses.size(); i++)
					innerClasses.get(i).accept(this);

				// continue with Clazz.newClass...
				// add the Clazz.newInstance call, which:
				// (a) adds .valueOf() = function() {return this} for Number
				// subclasses
				// (b) sets objThis.__JSID__ to a unique number
				// (c) handles inner class final variables
				// (d) includes a call to the constructor c$() when called
				// directly by the user using new Foo()
				if (!isInterface) {
					buffer.append("Clazz.newInstance(this, arguments")
							.append(isTopLevel ? ",0" : "[0]," + !isStatic(binding)).append(",C$);\r\n");
				}
				buffer.append("}");
			}
			if (isEnum) {
				superInterfaceTypes = ((EnumDeclaration) node).superInterfaceTypes();
			} else {
				superInterfaceTypes = ((TypeDeclaration) node).superInterfaceTypes();
			}

		}

		if (func != null)
			buffer.append(func);

		// arg4: superclass or null

		buffer.append(", ");
		ITypeBinding superclass;
		if (isEnum) {
			buffer.append("'Enum'");
		} else if (isLambda || (superclass = getJavaClassSuper(binding)) == null) {
			buffer.append("null");
		} else {
			hasDependents = true;
			String superclassName = getJavaClassNameQualified(superclass);
			if (superclassName == null || superclassName.length() == 0 || "java.lang.Object".equals(superclassName)) {
				buffer.append("null");
			} else {
				if (isAnonymous) {
					buffer.append(getFinalJ2SClassNameQualifier(null, superclass, superclassName, FINAL_ESCAPE));
				} else {
					buffer.append(getFinalInnerClassList(superclass, superclassName));
				}
			}
		}

		// arg5: superinterface(s) if not null

		if (superInterfaceTypes != null && superInterfaceTypes.size() > 0) {
			hasDependents = true;
			buffer.append(", ");
			String term = "";
			if (superInterfaceTypes.size() > 1) {
				buffer.append("[");
				term = "]";
			}
			String sep = "";

			for (Iterator<?> iter = superInterfaceTypes.iterator(); iter.hasNext();) {
				buffer.append(sep);
				Object iface = iter.next();
				ITypeBinding ibinding = (iface instanceof Type ? ((Type) iface).resolveBinding()
						: (ITypeBinding) iface);
				String term1 = "";
				if (!ibinding.isTopLevel()) {
					if (sep == "" && term == "") {
						buffer.append("[");
						term = "]";
					}
					buffer.append("[");
					term1 = "]";
					ITypeBinding b = ibinding;
					int pt = buffer.length();
					while (!b.isTopLevel()) {
						b = b.getDeclaringClass();
						buffer.insert(pt, "'" + getFinalJ2SClassName(getJavaClassNameQualified(b), FINAL_RAW) + "',");
					}
				}
				buffer.append("'");
				buffer.append(getFinalJ2SClassName(getJavaClassNameQualified(ibinding), FINAL_RAW));
				buffer.append("'");
				buffer.append(term1);
				sep = ", ";
			}
			buffer.append(term);
		} else {
			buffer.append(", null");
		}

		// arg6: anonymous(1), local(2), or absent

		if (isLocal) {
			buffer.append(", 2");
		} else if (isAnonymous) {
			buffer.append(", 1");
		} else {
			// remove excessive null parameters
			int i;
			while (", null".equals(buffer.substring(i = buffer.length() - 6)))
				buffer.setLength(i);
		}

		// close the initializer

		buffer.append(");\r\n");

		// Add the class static initializer C$.$clinit$(), which
		// finalizes all field values and running static{...} initializers.
		// C$.$clinit$ is removed immediately when run so that it is only run
		// just
		// once per class. (In contrast, C$.$init$ is run once per instance.)

		List<BodyDeclaration> lstStatic = new ArrayList<BodyDeclaration>();

		// create a list of static fields and initializers

		// add the Java8 compatibility local variable $o$

		// also add the local var p$ short for C$.prototype if we have any
		// private methods

		// add all the methods

		TrailingBuffer oldTrailingBuffer = trailingBuffer;
		trailingBuffer = new TrailingBuffer();

		if (!isLambda) {
			for (Iterator<?> iter = bodyDeclarations.iterator(); iter.hasNext();) {
				BodyDeclaration element = (BodyDeclaration) iter.next();
				boolean isField = element instanceof FieldDeclaration;

				// All static fields that have initializers must be (re)initialized,
				// even if they are their default values. This is because
				// they might have been modified by other actions between the
				// time they were initially initialized and when $clinit$ is run.
				// This happens when the static fields in class A reference
				// static fields in class B, which in turn reference static fields
				// in Class A.

				if (isField || element instanceof Initializer) {
					if ((isInterface || isStatic(element)) && checkAnnotations(element, CHECK_J2S_IGNORE_ONLY)) {
						lstStatic.add(element);
						if (isField)
							addFieldDeclaration((FieldDeclaration) element, FIELD_DECL_STATIC_DEFAULTS);
					}
				}
			}
		}

		// for JAXB:
		List<EnumConstantDeclaration> enums = (isEnum ? new ArrayList<>() : null);
		List<FieldDeclaration> fields = (isInterface || isLambda || isEnum ? null : new ArrayList<>());
		List<IMethodBinding> methods = (fields == null ? null : new ArrayList<>());

		if (lstStatic.size() > 0 || hasDependents) {
			int pt = buffer.length();
			buffer.append("\r\nC$.$clinit$ = function() {Clazz.load(C$, 1);\r\n");
			boolean haveDeclarations = isEnum;
			if (isEnum)
				addEnumConstants((EnumDeclaration) node, enums);
			for (int i = lstStatic.size(); --i >= 0;) {
				BodyDeclaration element = lstStatic.remove(0);
				if (element instanceof Initializer) {
					((Initializer) element).getBody().accept(this);
					buffer.append(";\r\n");
					haveDeclarations = true;
				} else if (addFieldDeclaration((FieldDeclaration) element, FIELD_DECL_STATIC_NONDEFAULT)) {
					haveDeclarations = true;
				}
			}
			if (haveDeclarations || hasDependents)
				buffer.append("}\r\n");
			else
				buffer.setLength(pt);
		}

		if (isAnonymous) {
			setClassAndBinding(finalShortClassName, binding);
		}

		if (!isInterface) {

			// if this is not an interface, generate $init0$ and $init$ methods

			StringBuffer init0bufold = init0Buffer;
			init0Buffer = new StringBuffer();

			int len = buffer.length();
			buffer.append("\r\nClazz.newMeth(C$, '$init$', function () {\r\n");
			// we include all field definitions here and all nonstatic
			// initializers

			if (!isLambda)
				for (Iterator<?> iter = bodyDeclarations.iterator(); iter.hasNext();) {
					BodyDeclaration element = (BodyDeclaration) iter.next();
					boolean isField = element instanceof FieldDeclaration;
					if ((isField || element instanceof Initializer) && !isStatic(element)
							&& checkAnnotations(element, CHECK_J2S_IGNORE_AND_ANNOTATIONS)) {
						if (isField) {
							if (fields != null && !Modifier.isTransient(((FieldDeclaration) element).getModifiers()))
								fields.add((FieldDeclaration) element);
							addFieldDeclaration((FieldDeclaration) element, FIELD_DECL_NONSTATIC_ALL);
						} else {
							((Initializer) element).getBody().accept(this);
							buffer.append("\r\n");
						}
					}
				}
			buffer.append("}, 1);\r\n");

			if (init0Buffer.length() > 0) {
				String buf = buffer.substring(len);
				buffer.setLength(len);
				buffer.append("\r\nClazz.newMeth(C$, '$init0$', function () {\r\n");
				buffer.append("var c;if((c = C$.superclazz) && (c = c.$init0$))c.apply(this);\r\n");
				buffer.append(init0Buffer);
				buffer.append("}, 1);\r\n");
				buffer.append(buf);
			}

			init0Buffer = init0bufold;
		}

		// add all the methods

		StringBuffer defaults = new StringBuffer();

		if (isLambda) {
			addLambdaMethod(node, binding.getFunctionalInterfaceMethod());
		} else {
			for (Iterator<?> iter = bodyDeclarations.iterator(); iter.hasNext();) {
				ASTNode element = (ASTNode) iter.next();
				if (element instanceof MethodDeclaration) {
					MethodDeclaration mnode = (MethodDeclaration) element;
					IMethodBinding method = mnode.resolveBinding();
					if (method == null || !checkAnnotations(mnode, CHECK_J2S_IGNORE_AND_ANNOTATIONS))
						continue;
					if (methods != null) {
						String mname = method.getName();
						if (mname.startsWith("set") || mname.startsWith("get") || mname.startsWith("is"))
							methods.add(method);
					}
					int defpt = -1;
					if (Modifier.isDefault(method.getModifiers())) {
						log("default method " + method.getKey());
						defpt = buffer.length();
					}
					boolean addUnqualifiedCurrent = temp_add$UnqualifiedMethod;
					if (unqualifiedMethods != null) {
						// check for all methods that override a functional interface abstract method,
						// as those methods are to be qualified only with $

						for (int i = unqualifiedMethods.size(); --i >= 0;) {
							if (method.overrides(unqualifiedMethods.get(i))) {
								temp_add$UnqualifiedMethod = true;
								break;
							}
						}
					}
					processMethodDeclaration(method, mnode.parameters(), mnode.getBody(), mnode.isConstructor(),
							NOT_LAMBDA);
					if (defpt >= 0) {
						defaults.append(buffer.substring(defpt));
						buffer.setLength(defpt);
					}
					temp_add$UnqualifiedMethod = addUnqualifiedCurrent;
				}
			}
		}

		if (isInterface) {

			// Check for static type declarations in interfaces
			// This will create a new visitor.
			// Static field buffer may be filled with contents.

			for (Iterator<?> iter = bodyDeclarations.iterator(); iter.hasNext();) {
				ASTNode element = (ASTNode) iter.next();
				if (element instanceof TypeDeclaration)
					element.accept(this);
			}

			if (defaults.length() > 0) {
				buffer.append("C$.$defaults$ = function(C$){\n").append(defaults).append("};");
			}
		}

		// add any recently defined static field definitions, assert strings
		// and Enum constants

		if (class_jaxbAccessorType != JAXB_TYPE_UNKNOWN) {
			ClassAnnotation.addClassAnnotations(class_jaxbAccessorType, class_annotations, enums, fields, methods,
					innerClasses, trailingBuffer);
			class_annotations = null;
			class_jaxbAccessorType = JAXB_TYPE_UNKNOWN;
		}

		buffer.append(trailingBuffer); // also writes the assert string
		if (isAnonymous) {
			// if anonymous, restore old static def buffer
			trailingBuffer = oldTrailingBuffer;
		} else {
			// otherwise, dump the oldStatic buffer and start a new one
			buffer.append(oldTrailingBuffer);
			trailingBuffer = new TrailingBuffer();
			if (!isInterface)
				addDefaultConstructor();
			if (isEnum) {
				buffer.append("var $vals=[];\r\n");
				// implicit Enum methods added as trailer
				buffer.append("Clazz.newMeth(C$, 'values$', function() { return $vals }, 1);\r\n");
				buffer.append(
						"Clazz.newMeth(C$, 'valueOf$S', function(name) { for (var val in $vals){ if ($vals[val].name == name) return $vals[val]} return null }, 1);\r\n");
			}
		}

		getJ2sJavadoc(node, DOC_ADD_POST);

		if (!isTopLevel) {
			addAnonymousFunctionWrapper(false);
			if (isAnonymous) {
				buffer.append(")");
				this$0Name = this$0Name0;
				setClassAndBinding(oldShortClassName, oldBinding);
			}
		}
	}

//	private boolean checkDeclarationType(BodyDeclaration element, int type) {
//		switch (class_jaxbAccessorType) {
//		case JAXB_TYPE_FIELD:
//		case JAXB_TYPE_PROPERTY:
//			return (type == class_jaxbAccessorType);
//		case JAXB_TYPE_NONE:
//		case JAXB_TYPE_PUBLIC_MEMBER:
//			return true;
//			// can't check here, because if just one of set or get is annotated,
//			// then it doesn't matter if either is public or not
////			return Modifier.isPublic(element.getModifiers());
//		default:
//		case JAXB_TYPE_UNKNOWN:
//			return false;
//		}
//	}

	private String getPackageName(ITypeBinding javaClass) {
		IPackageBinding p = javaClass.getPackage();
		return (p == null ? "_" : p.getName());
	}

	/**
	 * Collect all names of all functional interface abstract methods that this
	 * class might refer to so that their unqualified. This is not perfect, as it is
	 * possible to have implementations of specific subtypes of parameterized
	 * methods. However, it will have to do for now.
	 * 
	 * @param type
	 * @param unqualifiedMethods
	 * @return List of methods that should have raw unparameterized alias
	 */
	private List<IMethodBinding> getUnqualifiedMethods(ITypeBinding type, List<IMethodBinding> unqualifiedMethods) {
		if (type.isArray() || type.isPrimitive()) {
			return unqualifiedMethods;
		}
		ITypeBinding superClass = type.getSuperclass();
		if (superClass != null)
			unqualifiedMethods = getUnqualifiedMethods(superClass, unqualifiedMethods);
		ITypeBinding[] superInterfaces = type.getInterfaces();
		for (int i = 0; i < superInterfaces.length; i++)
			unqualifiedMethods = getUnqualifiedMethods(superInterfaces[i], unqualifiedMethods);
		IMethodBinding functionalMethod = type.getFunctionalInterfaceMethod();
		if (functionalMethod != null) {
			if (unqualifiedMethods == null)
				unqualifiedMethods = new ArrayList<IMethodBinding>();
			unqualifiedMethods.add(functionalMethod);
		}
		return unqualifiedMethods;
	}

	/**
	 * If there is no Foo() or Foo(xxx... array), then we need to provide our own
	 * constructor.
	 * 
	 */
	private void addDefaultConstructor() {
		if (haveDefaultConstructor) {
			haveDefaultConstructor = false;
		} else {
			buffer.append("\r\nClazz.newMeth(C$);\r\n");
		}
	}

	/**
	 * Add all the Enum constants, and create C$.values$() and Enum.valueOf
	 * 
	 * @param constants
	 */

	private void addEnumConstants(EnumDeclaration e, List<EnumConstantDeclaration> enums) {
		List<?> constants = e.enumConstants();
		buffer.append("$vals=Clazz.array(C$,[0]);\r\n");
		for (int i = 0; i < constants.size(); i++) {
			EnumConstantDeclaration enumConst = (EnumConstantDeclaration) constants.get(i);
			enums.add(enumConst);
			checkAnnotations(enumConst, CHECK_ANNOTATIONS_ONLY); // for JAXB only
			IMethodBinding binding = enumConst.resolveConstructorBinding();
			AnonymousClassDeclaration anonDeclare = enumConst.getAnonymousClassDeclaration();
			String anonName = null;
			if (anonDeclare != null) {
				ITypeBinding dbinding = anonDeclare.resolveBinding();
				// BH: add the anonymous class definition inline!
				addClassOrInterface(anonDeclare, dbinding, anonDeclare.bodyDeclarations(), 'a');
//				anonDeclare.accept(this);
				anonName = getJavaClassNameQualified(dbinding);
				buffer.append("\r\n");
			}
			buffer.append("Clazz.newEnumConst($vals, ")
					.append(getFinalMethodNameWith$Params("C$.c$", null, binding, null, false, METHOD_NOTSPECIAL))
					.append(", \"");
			enumConst.getName().accept(this);
			buffer.append("\", " + i);
			addMethodParameterList(enumConst.arguments(), binding, ", [", "]", METHOD_CONSTRUCTOR);
			if (anonName != null)
				buffer.append(", ").append(anonName);
			buffer.append(");\r\n");
		}
	}

	/**
	 * 
	 * Handle all field declarations without visit(FieldDeclaration).
	 * 
	 * 
	 * @param field  the field being declared
	 * @param fields
	 * @param mode   FIELD_DECL_STATIC_NONDEFAULT static fields into $clinit$
	 *               (values) FIELD_DECL_STATIC_DEFAULTS static fields into buffer
	 *               directly (defaults) FIELD_DECL_NONSTATIC_ALL static variables
	 *               into $init$ (values) and $init0$ (defaults)
	 * @return true if anything was written to the buffer
	 */
	private boolean addFieldDeclaration(FieldDeclaration field, int mode) {

		boolean isStatic = (mode == FIELD_DECL_STATIC_NONDEFAULT || mode == FIELD_DECL_STATIC_DEFAULTS);
		boolean needDefault = (mode == FIELD_DECL_NONSTATIC_ALL || mode == FIELD_DECL_STATIC_DEFAULTS);
		List<?> fragments = field.fragments();
		VariableDeclarationFragment identifier = (VariableDeclarationFragment) fragments.get(0);
		IVariableBinding var = identifier.resolveBinding();
		Type nodeType = (var != null && var.getType().isArray() ? null : field.getType());
		boolean isPrimitive = (nodeType != null && nodeType.isPrimitiveType());
		Code code = (isPrimitive ? ((PrimitiveType) nodeType).getPrimitiveTypeCode() : null);
		// have to check here for final Object = "foo", as that must not be ignored.
		boolean checkFinalConstant = ((isPrimitive
				|| var != null && var.getType().getQualifiedName().equals("java.lang.String")) && isStatic
				&& Modifier.isFinal(field.getModifiers()));
		if (needDefault)
			addJ2SDoc(field);
		int len0 = buffer.length();
		for (Iterator<?> iter = fragments.iterator(); iter.hasNext();) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();
			Expression initializer = fragment.getInitializer();
			if (checkFinalConstant ? getConstantValue(initializer, false)
					: isStatic && initializer == null && !needDefault)
				continue;
			int len = buffer.length();

			String prefix = (isStatic ? "C$." : "this.") + getFinalFieldName(fragment.resolveBinding());

			buffer.append(prefix);
			buffer.append("="); // no space here; will check last char for lambda
			int len1 = buffer.length();

			if (initializer == null || needDefault) {
				// Route default for this to the $init0$ buffer if nonstatic, or
				// straight to the class if static
				// if static and not initialized

				buffer.append(code == null ? "null" : getPrimitiveDefault(code));
				buffer.append(";\r\n");
				//
				// $clinit$ -- statics; once only
				// $init0$ -- from within Clazz.newInstance, before any
				// constructors
				// $init$ -- from the constructor, just after any super()
				// call or whenever there is no this() call

				// com.falstad.Diffraction.CrossAperature initialization was
				// failing. Sequence was:

				// Aperature<init>: calls setDefaults() (new double[][]
				// lineXLocations)
				// BlockAperature<init> sets lineXLocations = null
				// CrossAperature<init> needs the defaults set and fails

				// but needed to be:

				// Aperature<init>: calls setDefaults() (new double[][]
				// lineXLocations)
				// BlockAperature<init> defines but does not set
				// lineXLocations
				// CrossAperature<init> sees the created lineXLocations
				// created in Aperature<init>

				if (isStatic)
					continue;
				init0Buffer.append(buffer.substring(len));
				if (initializer == null) {
					buffer.setLength(len);
					continue;
				}
				buffer.setLength(len1);
			}
			// not static and has an initializer
			addExpressionAsTargetType(initializer, field.getType(), "v", null);
			buffer.append(";\r\n");
		}
		return (buffer.length() > len0);
	}

	/**
	 * 
	 * generate the Clazz.new_(...) call for an inner class.
	 * 
	 * @param node
	 * @param javaInnerClassName
	 * @param outerClassExpr
	 * @param finals
	 * @param constructorDeclaration
	 * @param superAnonName          the name of the superclass of the anonymous
	 *                               method, as for example, in MouseAdapter adapter
	 *                               = new MouseAdapter() {....}
	 */
	private void addInnerTypeInstance(ASTNode node, ITypeBinding superAnonOrInnerClass, String javaInnerClassName,
			Expression outerClassExpr, String finals, IMethodBinding constructorMethodDeclaration, String superAnonName,
			String anonName) {
		openNew(superAnonOrInnerClass, (superAnonName == null ? javaInnerClassName : superAnonName), anonName,
				constructorMethodDeclaration, -1);

		// add constructor application arguments: [object, parameters]

		buffer.append(", [");
		
		
		if (outerClassExpr == null)
			buffer.append("this");
		else
			outerClassExpr.accept(this);

		// add final variable array

		buffer.append(", ").append(finals == null ? "null" : finals);

		// add parameters

		if (constructorMethodDeclaration != null) {
			List<?> args = ((ClassInstanceCreation) node).arguments();
			addMethodParameterList(args, constructorMethodDeclaration,
					args.size() > 0 || constructorMethodDeclaration.isVarargs() ? ", " : null, null,
					METHOD_CONSTRUCTOR);
		}
		buffer.append("]");

		// an anonymous class will be calling a constructor in another
		// class, so
		// we need to indicate its actual call explicitly

		if (superAnonName != null && javaInnerClassName != null)
			buffer.append(",").append(getFinalJ2SClassName(javaInnerClassName, FINAL_PC));

		buffer.append(")");
	}

	/**
	 * Add a method parameter list
	 * 
	 * @param arguments
	 * @param methodDeclaration
	 * @param prefix
	 * @param suffix
	 * @param flags
	 */
	private void addMethodParameterList(List<?> arguments, IMethodBinding methodDeclaration, String prefix,
			String suffix, int flags) {
		if (methodDeclaration == null)
			return;

		boolean methodIsVarArgs = methodDeclaration.isVarargs();
		int argCount = arguments.size();
		if ((flags & METHOD_CONSTRUCTOR) != 0 && argCount == 0) {
			// We allow use of a default constructor using new foo().
			// Here we always add a [] argument to a default constructor, as null
			// will indicate that we did not use Clazz.new_ and instead called new foo()
			// directly.
			if (prefix != null) {
				buffer.append(prefix);
				prefix = null;
				if (methodIsVarArgs)
					buffer.append("[]");
			}
		} else {
			ITypeBinding[] parameterTypes = methodDeclaration.getParameterTypes();
			int nparam = parameterTypes.length;
			if (prefix != null && (nparam > 0 || methodIsVarArgs)) {
				buffer.append(prefix);
				prefix = null;
			}
			addMethodArguments(parameterTypes, methodIsVarArgs, arguments, flags);
		}
		if (prefix == null && suffix != null)
			buffer.append(suffix);
	}

	private void addSuperConstructor(SuperConstructorInvocation node, IMethodBinding methodDeclaration) {
		if (node == null) {
			// default constructor
			buffer.append("Clazz.super_(C$, this,1);\r\n");
			return;
		}
		buffer.append(getFinalMethodNameWith$Params("C$.superclazz.c$", null, node.resolveConstructorBinding(), null,
				false, METHOD_NOTSPECIAL));
		buffer.append(".apply(this");
		addMethodParameterList(node.arguments(), methodDeclaration, ", [", "]", METHOD_CONSTRUCTOR);
		buffer.append(");\r\n");
		addCallInit();
	}

//	/**
//	 * Check whether the class represented by the given name is inherited from the
//	 * given type binding.
//	 * 
//	 * The algorithm: 1. Check binding self class name 2. Check binding super class
//	 * 3. Check binding interfaces
//	 * 
//	 * @param binding
//	 * @param name
//	 * @return
//	 */
//	static private boolean isInheritedClassName(ITypeBinding binding, String name) {
//		if (binding == null) {
//			return false;
//		}
//		String bindingName = removeBrackets(binding.getJavaClassNameQualified());
//		if (name.equals(bindingName)) {
//			return true;
//		}
//		ITypeBinding superclass = binding.getSuperclass();
//		if (isInheritedClassName(superclass, name)) {
//			return true;
//		}
//		ITypeBinding[] interfaces = binding.getInterfaces();
//		if (interfaces != null) {
//			for (int i = 0; i < interfaces.length; i++) {
//				if (isInheritedClassName(interfaces[i], name)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	/**
//	 * nonAnonymous classes only
//	 * 
//	 * @param binding
//	 * @return
//	 */
//	private String getNamedClassName(ITypeBinding binding) {
//		String innerClassName = getClassNameQualifiedAllowP$(
//				binding.isLocal() ? binding.getBinaryName() : binding.getJavaClassNameQualified());
//		return innerClassName.substring(innerClassName.lastIndexOf('.') + 1);
//	}

	/**
	 * log to sysout
	 * 
	 * @param msg
	 */
	static void log(String msg) {
		System.out.println(msg);
	}

	/**
	 * log to syserr
	 * 
	 * @param msg
	 */
	static void logErr(String msg) {
		System.err.println(msg);
	}

	private static boolean isObjectOrNull(ITypeBinding type) {
		return type == null || "java.lang.Object".equals(type.getQualifiedName());
	}

	/**
	 * holds all static field definitions for insertion at the end of the class def
	 * and allows setting of local typed integer arrays for fast processing of bytes
	 * 
	 */
	protected TrailingBuffer trailingBuffer = new TrailingBuffer();

	/**
	 * TrailingBuffer holds definitions that need to come after all methods are
	 * defined, with blocks defined just once for any given class.
	 * 
	 * The buffer also provides a very efficient way to do byte, short, and int
	 * operation processing by using the trick that if we have defined
	 * 
	 * var $b$ = new Int8Array(1)
	 * 
	 * then we can use that to "filter" a (necessarily) 32-bit integer JavaScript
	 * variable to reproduce the effect of being a byte or short or int. This is
	 * done as follows:
	 * 
	 * Java:
	 * 
	 * byte b = (byte) 300;
	 * 
	 * JavaScript:
	 * 
	 * var b = ($b$[0] = 300, $b$[0]);
	 * 
	 * This works because Int8Arrays really are bytes, and they can only hold bytes.
	 * So
	 * 
	 * $b$[0] = 300
	 * 
	 * sets $b$[0] to be 44, and ($b$[0] = 300, $b$[0]) then passes that value on to
	 * the receiving variable b (which itself is a 32-bit integer, actually).
	 * 
	 * It was a surprise to me that the "(..., $b$[0])" business was necessary.
	 * However, it turns out that
	 * 
	 * b = $b$[0] = 300;
	 * 
	 * is really short for the two (undesired) independent processes:
	 * 
	 * b = 300; $b$[0] = 300;
	 *
	 * not the (desired) sequential pair
	 * 
	 * $b$[0] = 300; b = $b$[0];
	 * 
	 * But
	 * 
	 * var b = ($b$[0] = 300, $b$[0]);
	 * 
	 * is precisely this second meaning.
	 * 
	 * 
	 * We turn this action off using the field isArray so that these don't get
	 * nested.
	 * 
	 * @author Bob Hanson
	 *
	 */
	class TrailingBuffer {

		StringBuffer buf;
		private String added = "";

		boolean hasAssert;

		TrailingBuffer() {
			buf = new StringBuffer();
		}

		TrailingBuffer append(String s) {
			buf.append(s);
			return this;
		}

		String getAssertString() {
			return (hasAssert ? "C$.$_ASSERT_ENABLED_ = ClassLoader.getClassAssertionStatus$(C$);\r\n" : "");
		}

		public String toString() {
			return getAssertString() + added + buf;
		}

		void addType(String name) {
			char a = name.charAt(0);
			// note that this character may not be in the phrase "new Int Array"
			if (added.indexOf(a) >= 0)
				return;
			added += "var $" + a + "$";
			switch (a) {
			case 'b': // $b$
				added += " = new Int8Array(1)";
				break;
			case 's': // $s$
				added += " = new Int16Array(1)";
				break;
			case 'i': // $i$ // abandoned - using |0
				added += " = new Int32Array(1)";
				break;
			default:
			case 'p': // $p$ // char temp
			case 'j': // $j$ // [pt++] temp
			case 'k': // $k$ // [3][pt++]
			case 'l': // $l$ // [3][4][pt++]
				break;
			}
			added += ";\r\n";
		}
	}

	public boolean visit(ArrayAccess node) {
		node.getArray().accept(this);
		buffer.append('[');
		addNonCharacter(node.getIndex());
		buffer.append(']');
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean visit(ArrayCreation node) {
		// new byte[] {'a', 2, 3, 4, 5};
		ArrayInitializer inode = node.getInitializer();
		if (inode == null) {
			addArrayConstructor(node.resolveTypeBinding(), node.dimensions());
		} else {
			visit(inode);
		}
		return false;
	}

	private void addArrayConstructor(ITypeBinding binding, List<ASTNode> dim) {
		boolean isLambda_C = (dim == null);
		@SuppressWarnings("null")
		int n = (isLambda_C ? 1 : dim.size());
		buffer.append(clazzArray(binding, ARRAY_DIM_ONLY));
		buffer.append(", [");
		if (isLambda_C)
			buffer.append("t.intValue()");
		else
			visitList(dim, ", ");
		for (int i = binding.getDimensions() - n; --i >= 0;)
			buffer.append(", null");
		buffer.append("])");
	}

	public boolean visit(ArrayInitializer node) {
		// as in: public String[] d = {"1", "2"};
		buffer.append(clazzArray(node.resolveTypeBinding(), ARRAY_INITIALIZED));
		buffer.append(", [");
		@SuppressWarnings("unchecked")
		List<ASTNode> expressions = node.expressions();
		visitList(expressions, ", ");
		buffer.append("])");
		return false;
	}

	public boolean visit(Assignment node) {
		// note that this is not
		// var x = ..... -- that is a visit(VariableDeclaration)
		//
		// includes: =
		// +=, -=, *=, /=, %=
		// &=, |=, ^=
		// <<=
		// >>=
		// >>>=

		Expression left = node.getLeftHandSide();
		Expression right = node.getRightHandSide();

//		buffer.append("/* assign left=" + left  + " " + left.getClass().getName() + " right=" + right + right.getClass().getName() + "*/");

		ITypeBinding leftTypeBinding = left.resolveTypeBinding();
		ITypeBinding rightTypeBinding = right.resolveTypeBinding();
		String rightName = (rightTypeBinding == null ? null : rightTypeBinding.getName());
		String leftName = (leftTypeBinding == null ? null : leftTypeBinding.getName());
		if (leftName == null || rightName == null)
			return false;
		boolean wasArray = temp_processingArrayIndex;
		temp_processingArrayIndex = (left instanceof ArrayAccess);
		ArrayAccess leftArray = (temp_processingArrayIndex ? (ArrayAccess) left : null);
		IVariableBinding toBinding = getLeftVariableBinding(left, leftTypeBinding);
		String op = node.getOperator().toString();
		String opType = (op.length() == 1 ? null : op.substring(0, op.length() - 1));
		boolean needNewStaticParenthesis = false;
		boolean isParenthesized = (right instanceof ParenthesizedExpression);
//		boolean haveDocRight = (getJ2sJavadoc(right, DOC_CHECK_ONLY) != null);
		if (isStaticBinding(toBinding)) {
			// Static def new Test_Static().y++;
			needNewStaticParenthesis = (!haveDirectStaticAccess(left)) && !(node.getParent() instanceof Statement);
			if (needNewStaticParenthesis) {
				buffer.append("(");
			}
			addLeftSidePrefixName(left);
		} else {
			toBinding = null;
		}

		if ("boolean".equals(leftName) && "boolean".equals(rightName)) {
			if (("^=".equals(op))) {
				opType = "!=";
			} else {
				// all boolean should be OK -- no automatic here
				left.accept(this);
				buffer.append((opType == null ? "=" : op));
				right.accept(this);
				leftName = null;
			}
		} else if (opType == null) {
			// = operator is no problem
			left.accept(this);
			buffer.append("=");
			addExpressionAsTargetType(right, leftTypeBinding, "=", null);
			leftName = null;
		}
		if (leftName == null) {
			// we are done
			if (needNewStaticParenthesis)
				buffer.append(")");
			temp_processingArrayIndex = wasArray;
			return false;
		}

		int ptArray = (temp_processingArrayIndex ? buffer.length() : -1);
		if (toBinding != null)
			addFieldName(left, toBinding);
		else
			left.accept(this);
		int ptArray2 = (temp_processingArrayIndex ? buffer.length() : -1);
		if (!"char".equals(leftName)) {
			if (isIntegerType(leftName) || "boolean".equals(leftName)) {
				// can't just use a |= b because that ends up as 1 or 0, not true or false.
				// byte|short|int|long += ...
				if (!addPrimitiveTypedExpression(left, toBinding, leftName, opType, right, rightName, null, true))
					ptArray = -1;
			} else {
				ptArray = -1;
				// not char x ....
				// not boolean x....
				// could be int, byte, short, long with =, ==, or !=
				// could be String x = .....
				buffer.append(' ');
				buffer.append(op);
				buffer.append(' ');
				boolean leftIsString = leftName.equals("String");
				if ("char".equals(rightName)) {
					if (right instanceof CharacterLiteral) {
						// ... = 'c'
						if (leftIsString) {
							getConstantValue(right, true);
						} else {
							if (isParenthesized)
								buffer.append("(");
							addJ2SDoc(right);
							buffer.append(0 + ((CharacterLiteral) right).charValue());
							if (isParenthesized)
								buffer.append(")");
						}
					} else if (leftIsString) {
						// String x = (char)....
						right.accept(this);
					} else {
						// dump ( right ) and check for right being
						// String.charAt(...);
						int pt = buffer.length();
						buffer.append('(');
						right.accept(this);
						buffer.append(")");
						addCharCodeAt(right, pt);
					}
				} else {
					// just add the right operand
					addOperandWithDoc(right, leftIsString);
				}
				if (needNewStaticParenthesis) {
					buffer.append(")");
				}
			}
			return fixAssignArray(leftArray, ptArray, ptArray2, wasArray);
		}

		// char left op right where op is not just "="

		// could be +=, -=, *=, /=, >>=, etc

		buffer.append(" = String.fromCharCode(");
		if (left instanceof SimpleName || left instanceof QualifiedName) {
			left.accept(this);
		} else {
			buffer.append("(");
			left.accept(this);
			buffer.append(")");
		}
		buffer.append(CHARCODEAT0); // .charCodeAt(0)
		buffer.append(opType);
		buffer.append(' ');
		boolean needCharCode = false;
		if (right instanceof InfixExpression) {
			if (getConstantValue(right, true)) {
				char c = getLastCharInBuffer();
				needCharCode = (c == '\'' || c == '"');
			} else {
				buffer.append("(");
				appendBoxingNode(right, true);
				buffer.append(")");
			}
		} else if ("char".equals(rightName)) {
			Object constValue = getConstant(right);
			if (constValue != null && constValue instanceof Character) {
				buffer.append(((Character) constValue).charValue() + 0);
			} else {
				boolean needParenthesis = !(right instanceof ParenthesizedExpression
						|| right instanceof PrefixExpression || right instanceof PostfixExpression);
				if (needParenthesis) {
					buffer.append("(");
					addJ2SDoc(right);
				}
				needCharCode = appendBoxingNode(right, false);
				if (needParenthesis) {
					buffer.append(")");
				}
			}
		} else {
			appendBoxingNode(right, true);
			needCharCode = false;
		}
		if (needCharCode)
			buffer.append(CHARCODEAT0);
		buffer.append(')');
		return fixAssignArray(leftArray, ptArray, ptArray2, wasArray);
	}

	private IVariableBinding getLeftVariableBinding(Expression left, IBinding leftTypeBinding) {
		if (left instanceof Name) {
			if (leftTypeBinding instanceof IVariableBinding) {
				return (IVariableBinding) leftTypeBinding;
			}
		} else if (left instanceof FieldAccess) {
			return ((FieldAccess) left).resolveFieldBinding();
		}
		return null;
	}

	/**
	 * We must fix:
	 * 
	 * this.ctype[low++] = (this.ctype[low++]|(4)|0);
	 * 
	 * to read:
	 * 
	 * this.ctype[$j$=low++] = (this.ctype[$j$]|(4)|0);
	 * 
	 * so that the index does not get operated upon twice.
	 * 
	 * But what if we had this.ctype[i++][3] += .... ? -- for now, not considering
	 * this! Also resets wasArray.
	 * 
	 * @param ptArray
	 * @param ptArray2
	 * @param wasArray
	 * @return
	 */
	private boolean fixAssignArray(ArrayAccess leftArray, int ptArray, int ptArray2, boolean wasArray) {
		if (ptArray >= 0) {

			// dyn_ltree[(Tree._length_code[lc] + LITERALS + 1) * 2]++;

			String left = buffer.substring(ptArray, ptArray2); // zzz[xxx]
			int ptIndex1 = left.indexOf("[");
			int ptIndex2 = left.lastIndexOf("]");
			if (ptIndex2 - ptIndex1 > 3) {
				// at least as long as zzz[i++]
				String right = buffer.substring(ptArray2);
				buffer.setLength(ptArray);
				String name = left.substring(0, ptIndex1);
				if (name.indexOf("(") >= 0) {
					buffer.append("($j$=" + name + ")");
					name = "$j$";
					trailingBuffer.addType("j");
				} else {
					buffer.append(name);
				}
				String newRight = addArrayTemps(leftArray);
				int ptIndex3 = right.indexOf(left);
				buffer.append(right.substring(0, ptIndex3));
				buffer.append(name);
				buffer.append(newRight);
				buffer.append(right.substring(ptIndex3 + left.length()));
			}
		}
		temp_processingArrayIndex = wasArray;
		return false;
	}

	private String addArrayTemps(ArrayAccess leftArray) {
		String right = "";
		char c = 'k';
		String left = "";
		while (leftArray != null) {
			Expression exp = leftArray.getIndex();
			int pt = buffer.length();
			exp.accept(this);
			int len = buffer.length() - pt;
			String index = buffer.substring(pt);
			buffer.setLength(pt);
			if (len < 3) {
				left = "[" + index + "]" + left;
				right = "[" + index + "]" + right;
			} else {
				left = "[$" + c + "$=" + index + "]" + left;
				right = "[$" + c + "$]" + right;
				trailingBuffer.addType("" + c++);
			}
			exp = leftArray.getArray();
			if (!(exp instanceof ArrayAccess))
				break;
			leftArray = (ArrayAccess) exp;
		}
		buffer.append(left);
		return right;
	}

	public boolean visit(BooleanLiteral node) {
		buffer.append(node.booleanValue());
		return false;
	}

	public boolean visit(CastExpression node) {
		Expression expression = node.getExpression();
		ITypeBinding expBinding = expression.resolveTypeBinding();
		Type typeTO = node.getType();
		String fromValue = "";
		String toValue = "";
		// assume that casting is intentional to adjust the integer type
		if (expBinding != null && typeTO.isPrimitiveType()) {
			String nameFROM = expBinding.getName();
			String nameTO = ((PrimitiveType) typeTO).getPrimitiveTypeCode().toString();
			if (!nameTO.equals(nameFROM)) {
				addPrimitiveTypedExpression(null, null, nameTO, null, expression, nameFROM, null, false);
				return false;
			}
		}
		buffer.append(fromValue);
		expression.accept(this);
		buffer.append(toValue);
		return false;
	}

	public boolean visit(CatchClause node) {
		buffer.append(" catch (");
		node.getException().accept(this);
		buffer.append(") ");
		node.getBody().accept(this);
		return false;
	}

	public boolean visit(CharacterLiteral node) {
		buffer.append(node.getEscapedValue());
		return false;
	}

	public boolean visit(ConditionalExpression node) {
		// tricky part here is that the overall expression should have a target,
		// not the individual ones.
		ITypeBinding binding = node.resolveTypeBinding();
		Expression expThen = node.getThenExpression();
		Expression expElse = node.getElseExpression();
		node.getExpression().accept(this);
		buffer.append(" ? ");
		addExpressionAsTargetType(expThen, binding, "e", null);
		buffer.append(" : ");
		addExpressionAsTargetType(expElse, binding, "e", null);
		return false;
	}

	public boolean visit(FieldAccess node) {
		// Field access expression AST node type.
		// FieldAccess:
		// Expression . Identifier
		//
		//
		// Note that there are several kinds of expressions that resemble field
		// a ccess expressions: qualified names, this expressions, and super
		// field access expressions. The following guidelines help with correct
		// usage:
		// -An expression like "foo.this" can only be represented as a this
		// expression (ThisExpression) containing a simple name. "this" is a
		// keyword, and therefore invalid as an identifier.
		// -An expression like "this.foo" can only be represented as a field
		// access expression (FieldAccess) containing a this expression and a
		// simple name. Again, this is because "this" is a keyword, and
		// therefore invalid as an identifier.
		// -An expression with "super" can only be represented as a super field
		// access expression (SuperFieldAccess). "super" is a also keyword, and
		// therefore invalid as an identifier.
		// -An expression like "foo.bar" can be represented either as a
		// qualified name (QualifiedName) or as a field access expression
		// (FieldAccess) containing simple names. Either is acceptable, and
		// there is no way to choose between them without information about what
		// the names resolve to (ASTParser may return either).
		// -Other expressions ending in an identifier, such as "foo().bar" can
		// only be represented as field access expressions (FieldAccess).

		IVariableBinding varBinding = node.resolveFieldBinding();
		Expression expression = node.getExpression();
		if (isStaticBinding(varBinding)) {
			// e.g. i += 3 + y + ++(new >>Test_Static<<().y);
			buffer.append('(');
		} else {
			varBinding = null;
		}

		expression.accept(this);

		if (varBinding != null) {
			buffer.append(", ");
			addQualifiedNameForBinding(varBinding, false);
			buffer.append(')');
		}
		buffer.append(".");
		node.getName().accept(this);
		return false;
	}

	/**
	 * Infix operators (typesafe enumeration).
	 * 
	 * <pre>
	 * InfixOperator:<code>
	 *    <b>*</b>	TIMES
	 *    <b>/</b>  DIVIDE
	 *    <b>%</b>  REMAINDER
	 *    <b>+</b>  PLUS
	 *    <b>-</b>  MINUS
	 *    <b>&lt;&lt;</b>  LEFT_SHIFT
	 *    <b>&gt;&gt;</b>  RIGHT_SHIFT_SIGNED
	 *    <b>&gt;&gt;&gt;</b>  RIGHT_SHIFT_UNSIGNED
	 *    <b>&lt;</b>  LESS
	 *    <b>&gt;</b>  GREATER
	 *    <b>&lt;=</b>  LESS_EQUALS
	 *    <b>&gt;=</b>  GREATER_EQUALS
	 *    <b>==</b>  EQUALS
	 *    <b>!=</b>  NOT_EQUALS
	 *    <b>^</b>  XOR
	 *    <b>&amp;</b>  AND
	 *    <b>|</b>  OR
	 *    <b>&amp;&amp;</b>  CONDITIONAL_AND
	 *    <b>||</b>  CONDITIONAL_OR</code>
	 * </pre>
	 */

	public boolean visit(InfixExpression node) {
		// includes
		//
		// * / % + -
		// << >> >>>
		// < > <= >= == !=
		// ^ & |
		// && ||
		// ( a == b )
		// (/** j2snative xxxx ||*/ a) == b

		Expression left = node.getLeftOperand();
		Expression right = node.getRightOperand();
		List<?> extendedOperands = node.extendedOperands();

		if (noDocProblem(left) && noDocProblem(right) && getConstantValue(node, true)) {
			return false;
		}
		ITypeBinding expTypeBinding = node.resolveTypeBinding();
		if (expTypeBinding == null)
			return false;
		String expTypeName = expTypeBinding.getName();
		boolean isToString = (expTypeName.indexOf("String") >= 0);

		String operator = node.getOperator().toString();
		boolean isBitwise = isBitwiseBinaryOperator(node);
		boolean isComparison = (!isBitwise && "!==<=>=".indexOf(operator) >= 0);
		ITypeBinding leftTypeBinding = left.resolveTypeBinding();
		ITypeBinding rightTypeBinding = right.resolveTypeBinding();
		if (leftTypeBinding == null || rightTypeBinding == null)
			return false;
		String leftName = leftTypeBinding.getName();
		String rightName = rightTypeBinding.getName();

		boolean leftIsInt = leftTypeBinding.isPrimitive() && isIntegerType(leftName);
		boolean rightIsInt = rightTypeBinding.isPrimitive() && isIntegerType(rightName);
		if ("/".equals(operator) && leftIsInt && rightIsInt) {
			// left and right are one of byte, short, int, or long
			// division must take care of this.
			addPrimitiveTypedExpression(left, null, leftName, operator, right, rightName, extendedOperands, false);
			return false;
		}

		boolean toBoolean = "boolean".equals(expTypeName);

		char pre = ' ';
		char post = ' ';
		if (isBitwise && toBoolean) {
			pre = '(';
			post = ')';
			buffer.append("!!(");
		}

		boolean isDirect = isBitwise && !toBoolean && leftIsInt && rightIsInt;
		if (isDirect || isComparison) {

			// we do not have to do a full conversion
			// possibilities include
			// numeric op numeric
			// char/Character op char/Character
			// String op String
			//
			if (!isDirect)
				switch (leftName) {
				case "char":
				case "Character":
				case "String":
					switch (rightName) {
					case "char":
					case "Character":
					case "String":
						isDirect = true;
						break;
					default:
						break;
					}
					break;
				default:
					if (isIntegerType(leftName) && isIntegerType(rightName))
						isDirect = true;
					break;
				}

			if (isDirect) {
				appendBoxingNode(left, false);
				buffer.append(' ');
				buffer.append(operator);
				buffer.append(' ');
				appendBoxingNode(right, false);
				addExtendedOperands(extendedOperands, operator, pre, post, isToString);
				return false;
			}
		}

//		boolean isToStringLeft = isToString && !isBitwise;
//		boolean isToStringRight = isToString && !isBitwise;

		// String s = "e";
		// s += 'c' | 'd';

		// left

		addOperandWithDoc(left, isToString && !isBitwise);
		buffer.append(' ');
		// op
		buffer.append(operator);
		if (("==".equals(operator) || "!=".equals(operator)) && !leftTypeBinding.isPrimitive()
				&& !(left instanceof NullLiteral) && !(right instanceof NullLiteral)) {
			buffer.append('=');
		}
		buffer.append(' ');
		// right
		if (right instanceof ParenthesizedExpression || getJ2sJavadoc(right, DOC_CHECK_ONLY) != null) {
			buffer.append("(");
			addJ2SDoc(right);
			if (right instanceof ParenthesizedExpression)
				right = ((ParenthesizedExpression) right).getExpression();
			addOperand(right, isToString && !isBitwise);
			buffer.append(")");
		} else {
			addOperand(right, isToString && !isBitwise);
		}

		// The extended operands is the preferred way of representing deeply
		// nested expressions of the form L op R op R2 op R3... where the same
		// operator appears between all the operands (the most common case being
		// lengthy string concatenation expressions). Using the extended
		// operands keeps the trees from getting too deep; this decreases the
		// risk is running out of thread stack space at runtime when traversing
		// such trees. ((a + b) + c) + d would be translated to: leftOperand: a
		// rightOperand: b extendedOperands: {c, d} operator: +

		addExtendedOperands(extendedOperands, operator, pre, post, isToString);
		if (toBoolean)
			buffer.append(post);
		return false;
	}

	private void addOperandWithDoc(Expression exp, boolean toString) {
		if (exp instanceof ParenthesizedExpression) {
			buffer.append("(");
			addJ2SDoc(exp);
			addOperand(((ParenthesizedExpression) exp).getExpression(), toString);
			buffer.append(")");
		} else {
			addOperand(exp, toString);
		}
	}

	public boolean visit(InstanceofExpression node) {
		Type right = node.getRightOperand();
		ITypeBinding binding = right.resolveBinding();
		if (binding == null)
			return false;
		buffer.append("Clazz.instanceOf(");
		node.getLeftOperand().accept(this);
		buffer.append(", ");
		if (right instanceof ArrayType) {
			buffer.append(clazzArray(binding, ARRAY_CLASS_LITERAL));
		} else {
			buffer.append("\"" + removeBracketsAndFixNullPackageName(getJavaClassNameQualified(binding)) + "\"");
			// right.accept(this);
		}
		buffer.append(")");
		return false;
	}

	public boolean visit(Modifier node) {
		return false;
	}

	public boolean visit(NumberLiteral node) {
		buffer.append(node.resolveConstantExpressionValue());
		return false;
	}

	public boolean visit(NullLiteral node) {
		ITypeBinding binding = node.resolveTypeBinding();
		if (binding != null)
			buffer.append("null");
		return true;
	}

	public boolean visit(ParenthesizedExpression node) {
		buffer.append("(");
		addJ2SDoc(node);
		node.getExpression().accept(this);
		buffer.append(")");
		return false;
	}

	/**
	 * Postfix operators (typesafe enumeration).
	 * 
	 * <pre>
	 * PostfixOperator:
	 *    <b><code>++</code></b>  <code>INCREMENT</code>
	 *    <b><code>--</code></b>  <code>DECREMENT</code>
	 * </pre>
	 */
	public boolean visit(PostfixExpression node) {
		return addPrePost(node, node.getOperand(), node.getOperator().toString(), true);
	}

	/**
	 * Prefix operators (typesafe enumeration).
	 * 
	 * <pre>
	 * PrefixOperator:
	 *    <b><code>++</code></b>  <code>INCREMENT</code>
	 *    <b><code>--</code></b>  <code>DECREMENT</code>
	 *    <b><code>+</code></b>  <code>PLUS</code>
	 *    <b><code>-</code></b>  <code>MINUS</code>
	 *    <b><code>~</code></b>  <code>COMPLEMENT</code>
	 *    <b><code>!</code></b>  <code>NOT</code>
	 * </pre>
	 */
	public boolean visit(PrefixExpression node) {
		Expression exp = node.getOperand();
// NO! Don't do this! !(/** @j2sNative true||*/false  does not work 
//		if (getConstantValue(node, true))
//			return false;
		String op = node.getOperator().toString();

		if ("~".equals(op)) {
			buffer.append(op);
			return true;
		}
		return addPrePost(node, exp, node.getOperator().toString(), false);
	}

	public boolean visit(QualifiedName node) {
		// page.x =...

		if (getConstantValue(node, true))
			return false;
		IBinding nameBinding = node.resolveBinding();
		IVariableBinding varBinding = (nameBinding instanceof IVariableBinding ? (IVariableBinding) nameBinding : null);
		Name qualifier = node.getQualifier();
		boolean skipQualifier = false;
		if (isStatic(nameBinding) && varBinding != null) {
			addQualifiedNameForBinding(varBinding, true);
			buffer.append('.');
			skipQualifier = true;
		} else if (!isStaticBinding(varBinding) || qualifier.resolveBinding() instanceof ITypeBinding) {
			varBinding = null;
		}
		String className = null;
		ASTNode parent = node.getParent();
		if (!skipQualifier && parent != null && !(parent instanceof QualifiedName)) {
			while (qualifier instanceof QualifiedName) {
				IBinding binding = qualifier.resolveBinding();
				if (binding != null && !(binding instanceof IVariableBinding)) {
					Name xqualifier = ((QualifiedName) qualifier).getQualifier();
					if (xqualifier instanceof QualifiedName) {
						IBinding xbinding = xqualifier.resolveBinding();
						if (xbinding != null && !(xbinding instanceof IVariableBinding)) {
							qualifier = xqualifier;
							continue;
						}
					}
				}
				break;
			}
			IBinding binding = qualifier.resolveBinding();
			if (binding != null && !(binding instanceof IVariableBinding)) {
				ITypeBinding typeBinding = qualifier.resolveTypeBinding();
				if (typeBinding != null) {
					// Compiling inner Class or enum type, like:
					// RadiusData.EnumType e = RadiusData.EnumType.THREE;
					className = getJavaClassNameQualified(typeBinding);
					if (isStatic(nameBinding)) {
						className = getFinalJ2SClassNameQualifier(null, typeBinding, className, FINAL_ESCAPECACHE);
					} else {
						// not possible?
						dumpStack("in visit(QualifiedName) outputting raw javaClassName " + nameBinding);
					}
				}
			}
		}

		if (!skipQualifier) {
			if (varBinding != null) {
				if (qualifier instanceof SimpleName) {
					addQualifiedNameForBinding(varBinding, false);
				} else {
					buffer.append('(');
					if (className == null)
						qualifier.accept(this);
					else
						buffer.append(className);
					buffer.append(", ");
					addQualifiedNameForBinding(varBinding, false);
					buffer.append(')');
				}
			} else if (className == null) {
				node.getQualifier().accept(this);
			} else {
				buffer.append(className);
			}
			buffer.append('.');
		}
		node.getName().accept(this);
		return false;
	}

	public boolean visit(SimpleName node) {
		// var x = ...
		// this.pages ....
		if (!getConstantValue(node, true))
			buffer.append(getFinalNameSimpleQualified(node));
		return false;
	}

	/**
	 * Return a qualifier.name or C$.name
	 * 
	 * Only called by
	 * 
	 * @param node
	 * @param mBinding
	 * @param flags    METHOD_ISQUALIFIED | METHOD_NULLEXPRESSION | LAMBDA_METHOD
	 * @return
	 */
	private String getFinalDotQualifiedNameForMethod(SimpleName node, IMethodBinding mBinding, int flags) {
		if (node == null) {
			// not possible?
			dumpStack("node is null in simpleNameInMethodBinding " + mBinding);
		}
		ITypeBinding declaringClass = mBinding.getMethodDeclaration().getDeclaringClass();
		String name = mBinding.getName();
		if (node == null) {
			// lambda::method needs to be qualified here only if it is a functional
			// interface method
			// otherwise it will be qualified in getQualifiedSimpleNameForinvocation
			name = (mBinding.getDeclaringClass().getFunctionalInterfaceMethod() == null ? name : name + "$");
		} else {
			String classDot = "";
//			boolean checkNameViolation = false;
			ASTNode parent;
			if (isStatic(mBinding)) {
				if ((flags & LAMBDA_METHOD) == 0 && declaringClass != null) {
					if ((flags & METHOD_NULLEXPRESSION) != 0) {
						// could be C$. or P$.
						String cname = removeBracketsAndFixNullPackageName(getJavaClassNameQualified(declaringClass));
						if (cname.length() > 0)
							classDot = cname + ".";
					}
				}
			} else if ((parent = node.getParent()) != null && !(parent instanceof FieldAccess)) {
				if ((flags & METHOD_ISQUALIFIED) == 0 && declaringClass != null && class_shortName != null) {
//					checkNameViolation = true;
					classDot = getClassNameAndDot(parent, declaringClass, isPrivate(mBinding));
				}
			}
			name = classDot + name;

			if (classDot.length() > 0)
				name = getFinalJ2SClassName(name, FINAL_PC);

//					+ (checkNameViolation ? NameMapper.getJ2S$JavaScriptCollisionIdentifier(name, true, mBinding)
			// : name);
		}

		return name;
	}

	public boolean visit(SimpleType node) {
		ITypeBinding binding = node.resolveBinding();
		buffer.append(binding == null ? node : getFinalJ2SClassName(getJavaClassNameQualified(binding), FINAL_PC));
		return false;
	}

	public boolean visit(StringLiteral node) {
		buffer.append(node.getEscapedValue().replace("\\u000a", "\\n"));
		return false;
	}

	/**
	 * SuperFieldAccess:
	 *
	 * [ ClassName . ] super . Identifier
	 *
	 */
	public boolean visit(SuperFieldAccess node) {
		buffer.append("this.");
		buffer.append(getFinalFieldName((IVariableBinding) node.getName().resolveBinding()));
		return false;
	}

	/**
	 * this or ClassName.this - just getting the qualifier here, not the whole
	 * thing?
	 * 
	 * 
	 */
	public boolean visit(ThisExpression node) {
		if (node.getQualifier() == null) {
			buffer.append("this");
			return false;
		}
		// xxxx.this.x
		// xxxx.this.foo()
		buffer.append(getThisRefOrSyntheticReference(node, node.resolveTypeBinding(), "this"));
		return false;
	}

	private String getThisRefOrSyntheticReference(ASTNode node, ITypeBinding binding, String ref) {
		ASTNode classNode = (node == null ? null : getAbstractOrAnonymousParentForNode(node));
		if (class_isAnonymousOrLocal || classNode != null && classNode.getParent() != null // CompilationUnit
				&& classNode.getParent().getParent() != null) {
			// not the top level, but "this" refers to this class
			if (binding.getBinaryName().equals(class_typeBinding.getBinaryName()))
				return ref;
			// not the top level -- add the synthetic reference.
			// anonymous and local will not have fully qualified names
			return getSyntheticReference(getJavaClassNameQualified(binding));
		}
		return ref;
	}

	public boolean visit(TypeLiteral node) {
		// Class x = Foo.class
		Type type = node.getType();
		ITypeBinding binding = type.resolveBinding();
		if (type.isPrimitiveType()) {
			// adds Integer.TYPE, Float.TYPE, etc.
			buffer.append(NameMapper.getPrimitiveTYPE(binding.getName()) + ".TYPE");
		} else if (type instanceof ArrayType) {
			buffer.append(clazzArray(binding, ARRAY_CLASS_LITERAL));
		} else {
			// BH we are creating a new Class object around this class
			// if it is an interface, then we explicitly add .$methodList$
			buffer.append("Clazz.getClass(");
			buffer.append(getFinalJ2SClassNameQualifier(null, binding, getJavaClassNameQualified(binding),
					FINAL_ESCAPECACHE));
			if (binding.isInterface())
				addInterfaceMethodListForLiteral(binding);
			buffer.append(")");
		}
		return false;
	}

	/**
	 * Add the list of methods in the Clazz.getClass call so that we can use them
	 * for reflection.
	 * 
	 * @param binding
	 */
	private void addInterfaceMethodListForLiteral(ITypeBinding binding) {
		buffer.append(",[");
		IMethodBinding[] methods = binding.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			if (i > 0)
				buffer.append(",");
			buffer.append("'")
					.append(getFinalMethodNameWith$Params(null, null, methods[i], null, false, METHOD_LITERAL))
					.append("'");
		}
		buffer.append("]");
	}

	@SuppressWarnings("unchecked")
	public boolean visit(VariableDeclarationExpression node) {
		buffer.append("var ");
		visitList(node.fragments(), ", ");
		return false;
	}

	public boolean visit(VariableDeclarationFragment node) {

		SimpleName name = node.getName();
		IBinding binding = name.resolveBinding();
		if (binding == null)
			return false;
		acceptPossiblyFinalVar(name, 0);
		Expression right = node.getInitializer();
		ITypeBinding rightBinding = (right == null ? null : right.resolveTypeBinding());
		if (rightBinding == null)
			return false;
		buffer.append("=");// no space here -- need to check for last char "=" in lambda

		addExpressionAsTargetType(right, name.resolveTypeBinding(), "v", null);
		return false;
	}

	////////// END visit/endVisit ///////////

	/**
	 * pre: ++,--, +, -, ~, !
	 * 
	 * post: ++,--
	 * 
	 * @param node
	 * @param left
	 * @param op
	 * @param isPost
	 * @return
	 */
	@SuppressWarnings({ "null" })
	private boolean addPrePost(Expression node, Expression left, String op, boolean isPost) {

		ASTNode parent = node.getParent();

		ITypeBinding leftTypeBinding = left.resolveTypeBinding();
		IVariableBinding varBinding = getLeftVariableBinding(left, leftTypeBinding);
		String name = null;
		boolean isChar = (leftTypeBinding != null && leftTypeBinding.isPrimitive()
				&& "char".equals(name = leftTypeBinding.getName()));
		boolean isShortOrByte = ("short".equals(name) || "byte".equals(name));
		String term = "";
		if (isStaticBinding(varBinding)) {
			// new Test_Static().y++ where y is static
			if ((isChar || !haveDirectStaticAccess(left))
					&& !(parent instanceof Statement || parent instanceof ParenthesizedExpression)) {

				buffer.append("(");
				term = ")";
			}
			addLeftSidePrefixName(left);
		} else {
			varBinding = null;
		}

		if (isPost) {
			if (isChar || isShortOrByte) {
				addPrePostFix(left, parent, varBinding, op, false, name.charAt(0));
			} else {
				// TODO: have to consider short and byte
				addFieldName(left, varBinding);
				buffer.append(op);
			}
		} else {
			if (isChar || isShortOrByte) {
				if (varBinding == null)
					buffer.append("(");
				addPrePostFix(left, (varBinding == null ? parent : null), varBinding, op, true, name.charAt(0));
				if (varBinding == null)
					buffer.append(")");
			} else {
				// have to consider short and byte
				buffer.append(op);
				addFieldName(left, varBinding);
			}

		}
		buffer.append(term);
		return false;
	}

	/**
	 * 
	 * @param left
	 * @param parent     null for prefix
	 * @param varBinding
	 * @param op
	 * @param b
	 */
	private void addPrePostFix(Expression left, ASTNode parent, IVariableBinding varBinding, String op,
			boolean isPrefix, char type) {

		boolean isChar = (type == 'c');
		if (isChar)
			type = 'p';
		boolean isStatement = (parent instanceof Statement && !(parent instanceof ReturnStatement));
		boolean addAnonymousWrapper = (!isChar || !isPrefix && !isStatement);
		String key = "$" + type + (isChar ? "$" : "$[0]");
		if (addAnonymousWrapper) {
			buffer.append("(" + key + "=");
			trailingBuffer.addType("" + type);
		}
		if (isChar) {
			if (addAnonymousWrapper) {
				addFieldName(left, varBinding);
				buffer.append(",");
			}
			addFieldName(left, varBinding);
			buffer.append(isChar ? "=String.fromCharCode(" : "=");
			addFieldName(left, varBinding);
			buffer.append(CHARCODEAT0).append("++".equals(op) ? "+1" : "-1");
			buffer.append(")");
		} else {
			// byte or short

			// Key in all cases is to return $b$[0], not just $b$[0]++ or ++$b$[0],
			// as that first converts to an integer, and then applies ++ in JavaScript.
			// In Java, ++ first operates on the byte, then that result is converted to
			// an integer.

			boolean wasArray = temp_processingArrayIndex;
			temp_processingArrayIndex = (left instanceof ArrayAccess);
			if (isPrefix)
				buffer.append(op);
			int ptArray = (temp_processingArrayIndex ? buffer.length() : -1);
			addFieldName(left, varBinding);
			int ptArray2 = (temp_processingArrayIndex ? buffer.length() : -1);
			buffer.append(",");
			addFieldName(left, varBinding);
			buffer.append("=");
			if (temp_processingArrayIndex)
				fixAssignArray((ArrayAccess) left, ptArray, ptArray2, wasArray);
			if (isPrefix) {
				// i = ($b$[0]=++b,b=$b$[0]);
				// ($b$[0]=++b,b=$b$[0]);
				buffer.append(key);
			} else {
				// postfix
				// statement: ($b$[0]=b,b=(++$b$[0] ,$b$[0]))
				// not statement: i = ($b$[0]=b,b=(++$b$[0] ,$b$[0]),--$b$[0],$b$[0]);
				buffer.append("(");
				buffer.append(op);
				buffer.append(key);
				if (isStatement) {
					key += ")";
				} else {
					buffer.append(",");
					buffer.append(key);
					buffer.append("),");
					buffer.append(op.equals("++") ? "--" : "++");
					buffer.append(key);
				}
			}
		}
		if (addAnonymousWrapper) {
			buffer.append("," + key + ")");
		}
	}

	private static boolean isBoxTyped(Expression exp) {
		return exp.resolveBoxing() || exp.resolveUnboxing();
	}

	private static boolean isIntegerType(String type) {
		return (type != null && type.length() > 1 && "int long byte short".indexOf(type) >= 0);
	}

	static boolean isPrivate(IBinding b) {
		return b != null && Modifier.isPrivate(b.getModifiers());
	}

	static boolean isStatic(IBinding b) {
		return b != null && Modifier.isStatic(b.getModifiers());
	}

	static boolean isTransient(IBinding b) {
		return b != null && Modifier.isTransient(b.getModifiers());
	}

	static boolean isStatic(BodyDeclaration b) {
		return Modifier.isStatic(b.getModifiers());
	}

	/**
	 * new int[5] becomes Clazz.array(Integer.TYPE, [5])
	 */
	private final static int ARRAY_DIM_ONLY = 0;
	/**
	 * new byte[] {'a', 2, 3, 4, 5} becomes Clazz.array(Byte.TYPE, -1, ["a", 2, 3,
	 * 4, 5]);
	 */
	private final static int ARRAY_INITIALIZED = -1;

	/**
	 * int[][].class becomes Clazz.array(Integer.TYPE, -2)
	 */
	private final static int ARRAY_CLASS_LITERAL = 1;

	/**
	 *
	 * @param type
	 * @param dimFlag
	 * @return JavaScript for array creation
	 */
	private String clazzArray(ITypeBinding type, int dimFlag) {
		ITypeBinding ebinding = type.getElementType();
		if (ebinding == null)
			ebinding = type; // creating for Enum
		String params = (ebinding.isPrimitive() ? NameMapper.getPrimitiveTYPE(ebinding.getName()) + ".TYPE"
				: getFinalJ2SClassNameQualifier(null, ebinding, null, FINAL_ESCAPECACHE))
				+ (dimFlag == ARRAY_DIM_ONLY ? "" : ", " + (Math.abs(dimFlag) * type.getDimensions() * -1));
		return "Clazz.array(" + params + (dimFlag > 0 ? ")" : "");
	}

	////////////////////////////////

	/**
	 * check to change charAt to charCodeAt
	 * 
	 * @param right
	 * @param pt
	 */
	private void addCharCodeAt(Expression right, int pt) {
		String charCodeAt0 = CHARCODEAT0;
		if (right instanceof MethodInvocation) {
			// if possible, just replace "charAt" with "charCodeAt"
			MethodInvocation m = (MethodInvocation) right;
			if (m.resolveMethodBinding().getKey().equals("Ljava/lang/String;.charAt(I)C")) {
				if ((pt = buffer.indexOf(".charAt", pt)) >= 0) {
					charCodeAt0 = "Code" + buffer.substring(pt + 5);
					buffer.setLength(pt + 5);
				}
			}
		}
		buffer.append(charCodeAt0);
	}

	/**
	 * Append an expression, coercing to primitive numeric types of the target
	 * parameter if needed. Used for Method arguments and return values, as well as
	 * variable declaration fragments, where we know the target type and no operator
	 * is involved.
	 * 
	 * 
	 * @param exp
	 * @param targetType ITypeBinding or TYPE or string
	 * @param op         just an identifier of the context: = for assignment, r for
	 *                   return statement, v for variable declaration fragment, p
	 *                   for method parameter, q for first parameter of indexOf or
	 *                   lastIndexOf, which are officially ints
	 */
	private void addExpressionAsTargetType(Expression exp, Object targetType, String op, List<?> extendedOperands) {
		if (targetType == null
				|| exp instanceof CastExpression && ((CastExpression) exp).getExpression() instanceof NullLiteral) {
			addJ2SDoc(exp);
			buffer.append("null");
			return;
		}
		ITypeBinding expTypeBinding = exp.resolveTypeBinding();
		if (expTypeBinding == null)
			return;
		// BH: Question: When does typeBinding == null?
		// A: when there is a compilation error, I think.
		// OK, now we have the same situation as any operand.
		String rightName = expTypeBinding.getName();
		if (rightName.equals("char") && op == "q") {
			appendBoxingNode(exp, false);
			return;
		}
		String paramName = (exp.resolveTypeBinding().isArray() ? ";"
				: targetType instanceof ITypeBinding ? ((ITypeBinding) targetType).getName() : targetType.toString());
		boolean isNumeric = isIntegerType(paramName);
		if ((isNumeric || paramName.equals("char")) && !isBoxTyped(exp)) {
			// using operator "m" to limit int application of $i$

			addPrimitiveTypedExpression(null, null, paramName, op, exp, rightName, extendedOperands, false);
		} else {
			// char f() { return Character }
			// Character f() { return char }
			// int f() { return Character }
			appendBoxingNode(exp, isNumeric);
		}
	}

	private void addExtendedOperands(List<?> extendedOperands, String operator, char pre, char post,
			boolean isToString) {
		if (extendedOperands.size() > 0) {
			buffer.append(' ');
			for (Iterator<?> iter = extendedOperands.iterator(); iter.hasNext();) {
				buffer.append(operator);
				buffer.append(pre);
				ASTNode element = (ASTNode) iter.next();
				addOperand((Expression) element, isToString);
				buffer.append(post);
			}
		}
	}

	private void addFieldName(Expression left, IVariableBinding qualifier) {
		if (qualifier != null) {
			addQualifiedNameForBinding(qualifier, false);
			buffer.append('.');
			left = (left instanceof QualifiedName ? ((QualifiedName) left).getName()
					: left instanceof FieldAccess ? ((FieldAccess) left).getName() : left);
		}
		appendBoxingNode(left, false);
	}

	/**
	 * add a reference to the static field prior to defining it.
	 * 
	 * @param left
	 */
	private void addLeftSidePrefixName(Expression left) {
		if (left instanceof QualifiedName) {
			if ((left = ((QualifiedName) left).getQualifier()) instanceof SimpleName)
				return;
		} else if (left instanceof FieldAccess) {
			if ((left = ((FieldAccess) left).getExpression()) instanceof ThisExpression)
				return;
		} else {
			return;
		}
		left.accept(this);
		buffer.append(", ");
	}

	/**
	 * 
	 * @param parameterTypes
	 * @param methodIsVarArgs
	 * @param arguments
	 * @param flags           METHOD_INDEXOF | 0
	 */
	@SuppressWarnings("null")
	private void addMethodArguments(ITypeBinding[] parameterTypes, boolean methodIsVarArgs, List<?> arguments,
			int flags) {
		String post = ", ";
		int nparam = parameterTypes.length;
		int argCount = arguments.size();
		boolean isIndexOf = ((flags & METHOD_INDEXOF) != 0);
		for (int i = 0; i < nparam; i++) {
			ITypeBinding paramType = parameterTypes[i];
			Expression arg = (i < argCount ? (Expression) arguments.get(i) : null);
			String op = (isIndexOf && i == 0 ? "q" : "p");
			if (i == nparam - 1) {
				// BH: can't just check for an array; it has to be an array with
				// the right number of dimensions
				if (nparam != argCount || methodIsVarArgs && paramType.isArray()
						&& arg.resolveTypeBinding().getDimensions() != paramType.getDimensions()
						&& !(arg instanceof NullLiteral)) {
					buffer.append("[");
					for (int j = i; j < argCount; j++) {
						addExpressionAsTargetType((Expression) arguments.get(j), paramType, op, null);
						if (j != argCount - 1) {
							buffer.append(", ");
						}
					}
					buffer.append("]");
					break;
				}
				post = "";
			}
			addExpressionAsTargetType(arg, paramType, op, null);
			buffer.append(post);
		}
	}

	/**
	 * Do not allow char or Character in a switch or array; instead use int
	 * 
	 * @param exp
	 */
	private void addNonCharacter(Expression exp) {
		String name = exp.resolveTypeBinding().getName();
		switch (name) {
		case "char":
		case "Character":
			addOperand(exp, false);
			break;
		case "Byte":
		case "Short":
		case "Integer":
		case "Long":
			addOperand(exp, false);
			break;
		default:
		case "String":
			exp.accept(this);
			break;
		}
	}

	/**
	 * add the operand, checking to see if it needs some adjustment:
	 * 
	 * (a) String + x where x is {double/float} requires boxing
	 * Double/Float(x).toString()
	 * 
	 * (b) String + x where x is {Double/Float} requires added .toString()
	 * 
	 * (c) Character and char to numeric requires addition of .$c()
	 * 
	 * 
	 * 
	 * @param exp
	 * @param isToString
	 */
	private void addOperand(Expression exp, boolean isToString) {

		boolean needRtParen = false;// (exp instanceof ParenthesizedExpression && getJ2sJavadoc(exp, DOC_CHECK_ONLY)
									// != null);
		ITypeBinding binding = exp.resolveTypeBinding();
		String name = binding.getName();
		if (isToString) {
			String prefix = null, suffix = null;
			switch (name) {
			case "double":
				prefix = "new Double(";
				suffix = ")";
				break;
			case "float":
				prefix = "new Float(";
				suffix = ")";
				break;
			case "Double":
			case "Float":
				prefix = suffix = "";
				break;
			default:
				exp.accept(this);
				break;
			}
			if (prefix != null) {
				buffer.append(prefix);
				exp.accept(this);
				buffer.append(suffix);
				buffer.append(".toString()");
			}
		} else if (!binding.isPrimitive() || !"char".equals(name)) {
			appendBoxingNode(exp, !isToString);
		} else {
			// to numeric only
			// oddly enough, 'c' is considered a simple
			Object constValue = null;
			if (exp instanceof CharacterLiteral) {
				buffer.append(0 + ((CharacterLiteral) exp).charValue());
			} else if ((constValue = getConstant(exp)) != null && constValue instanceof Character) {
				buffer.append(0 + ((Character) constValue).charValue());
			} else if (exp instanceof SimpleName || exp instanceof QualifiedName) {
				int pt = buffer.length();
				appendBoxingNode(exp, false);
				if (pt == buffer.length() - 3 && buffer.charAt(pt) == '\'') {
					char c = buffer.charAt(pt + 1);
					buffer.setLength(pt);
					buffer.append((int) c);
				} else {
					buffer.append(CHARCODEAT0);
				}
			} else if (exp instanceof PrefixExpression || exp instanceof PostfixExpression
					|| exp instanceof ParenthesizedExpression) {
				appendBoxingNode(exp, false);
				buffer.append(CHARCODEAT0);
			} else {
				int pt = buffer.length();
				buffer.append("(");
				appendBoxingNode(exp, false);
				buffer.append(")");
				addCharCodeAt(exp, pt);
			}
		}
		if (needRtParen)
			buffer.append(")");
	}

//	/**
//	 * fix the buffer for number/char issue when generating simple this.foo =
//	 * <constantValue>
//	 * 
//	 * @param code
//	 */
//	private void fixPrimitiveRightSide(Code code) {
//		if (code != PrimitiveType.BOOLEAN) {
//			boolean isCharConst = (buffer.charAt(buffer.length() - 1) == '\'');
//			if (isCharConst != (getPrimitiveDefault(code).charAt(0) == '\'')) {
//				if (!isCharConst) {
//					// char c = 33;
//					buffer.insert(buffer.lastIndexOf(" ") + 1, "String.fromCharCode(");
//					buffer.append(")");
//				} else if (code == PrimitiveType.BYTE) {
//					// byte b = 'c'
//					buffer.insert(buffer.lastIndexOf(" ") + 1, "($b$[0] = ");
//					buffer.append(".$c(), $b$[0])");
//					trailingBuffer.addType("b");
//				} else {
//					// int b = 'c'
//					buffer.append(".$c()");
//				}
//			}
//		}
//	}

	private String getPrimitiveDefault(Code code) {
		return (code == PrimitiveType.BOOLEAN ? "false" : code == PrimitiveType.CHAR ? "'\\0'" : "0");
	}

	/**
	 * A general method to handle implicit casting.
	 * 
	 * @param left
	 * @param assignmentBinding
	 * @param leftName
	 * @param op
	 * @param right
	 * @param rightName
	 * @param extendedOperands
	 * @param isAssignment      (+=, &=, etc)
	 * @param                   return true if is an assignment and a = (a op b) was
	 *                          used
	 */
	private boolean addPrimitiveTypedExpression(Expression left, IVariableBinding assignmentBinding, String leftName,
			String op, Expression right, String rightName, List<?> extendedOperands, boolean isAssignment) {
		// byte|short|int|long /= ...
		// convert to proper number of bits

		// byte a |= right

		// becomes

		// a = ($b$[0] = a | right, $b$[0])

		String classIntArray = null;
		String more = null;

		String prefix = (isAssignment ? "=" : "");
		boolean fromChar = ("char".equals(rightName));
		boolean fromIntType = ("long int short byte".indexOf(rightName) >= 0);
		boolean addParens = (op != "r" || fromChar || right instanceof ParenthesizedExpression);
		boolean isDiv = "/".equals(op);
		boolean toChar = false;
		switch (leftName) {
		case "char":
			if (!fromChar) {
				prefix += "String.fromCharCode(";
				more = ")";
				addParens = false;
			}
			toChar = true;
			break;
		default:
			// double, float
			break;
		case "long":
			if (!fromIntType || isDiv) {
				more = "|0)";
				addParens = true;
			} else {
				left = null;
			}
			break;
		case "int":
			if (!isDiv && (op != null && (!isDiv && fromIntType) || fromChar || rightName.equals("short")
					|| rightName.equals("byte"))) {
				left = null;
			} else {
				more = "|0)";
				addParens = true;
			}
			break;
		case "short":
			if ((rightName.equals("short") || rightName.equals("byte")) && !isDiv) {
				left = null;
				break;
			}
			//$FALL-THROUGH$
		case "byte":
			if (temp_processingArrayIndex) {
				more = "|0)";
				addParens = true;
			} else {
				classIntArray = "$" + leftName.charAt(0) + "$[0]"; // $i$, etc.
				trailingBuffer.addType(leftName);
			}
			break;
		}
		boolean wasArray = temp_processingArrayIndex;

		if (isAssignment && left == null) {
			buffer.append(op);
		}
//		buffer.append("OP_" + op + " " + isAssignment + " left=" + left + "PREFIX_" + prefix + "_PREFIX");
		buffer.append(prefix);
		if (classIntArray != null) {
			if (addParens)
				buffer.append("(");
			buffer.append(classIntArray).append(" = ");
			temp_processingArrayIndex = true;
		} else if (more == "|0)") {
			buffer.append("(");
		}
		if (left != null) {
			// a += b
			addFieldName(left, assignmentBinding);
			buffer.append(op);
			if (isAssignment)
				buffer.append("(");
		}
		if (!appendBoxingNode(right, fromChar) && fromChar && !toChar) {
			buffer.append(CHARCODEAT0);
		}
		if (extendedOperands != null) {
			addExtendedOperands(extendedOperands, op, ' ', ' ', false);
		}
		if (left != null && isAssignment) {
			buffer.append(")");
		}
		if (classIntArray != null) {
			// this is necessary because in JavaScript, (a=3.5) will be 3.5, not
			// a:
			// a = new Int8Array(1)
			// (a[0]=3.4, a[0])
			// 3
			// (a[0]=3.4)
			// 3.4
			buffer.append(", ").append(classIntArray);
			if (addParens)
				buffer.append(")");
			temp_processingArrayIndex = wasArray;
		} else if (more != null) {
			buffer.append(more);
		}
		return (isAssignment && left != null);
	}

	/**
	 * for example: new Test_Static().y++
	 * 
	 * @param varBinding
	 * @param isStatic
	 */
	private void addQualifiedNameForBinding(IVariableBinding varBinding, boolean isStatic) {
		ITypeBinding declaringClass = varBinding.getDeclaringClass();
		String javaClassName = getJavaClassNameQualified(declaringClass);
		isStatic = isStatic(varBinding);
		String finalQualifiedName = getFinalJ2SClassName(javaClassName, FINAL_PC);
		if (isStatic) {// && (isStatic || finalQualifiedName.length() < 2 ||
						// finalQualifiedName.charAt(1) != '$')) {
			finalQualifiedName = getFinalJ2SClassNameQualifier(null, declaringClass, javaClassName, FINAL_ESCAPECACHE);
		}
		buffer.append(finalQualifiedName);
	}

	/**
	 * Determine the qualifier for a method or variable.
	 * 
	 * In the case of private nonstatic methods, this is "p$<n>".
	 * 
	 * For general fields, this will be "this.".
	 * 
	 * For fields in outer classes, we need a synthetic references,
	 * this.b$[className] that points to the outer object, which may be one or more
	 * levels higher than this one.
	 * 
	 * Anonymous inner classes may reference either a superclass method/field or one
	 * in its declaring class stack.
	 * 
	 * @param node                  either a method or field or local variable
	 * @param declaringClass        the class that declares this variable
	 * @param isPrivateAndNotStatic
	 * @return qualifier for method or variable
	 */
	private String getClassNameAndDot(ASTNode node, ITypeBinding declaringClass, boolean isPrivateAndNotStatic) {

		String name = getJavaClassNameQualified(declaringClass);
		String ret = "";
		int superLevel = 0;
		boolean isThis = false;

		// Search parents of this node for an anonymous or abstract class declaration
		while (node != null) {
			boolean isAnonymous = (node instanceof AnonymousClassDeclaration);
			ITypeBinding typeBinding = (isAnonymous ? ((AnonymousClassDeclaration) node).resolveBinding()
					: node instanceof AbstractTypeDeclaration ? ((AbstractTypeDeclaration) node).resolveBinding()
							: null);
			if (typeBinding != null) {
				// is anonymous or abstract class declaration
				superLevel++;
				if (isSuperType(declaringClass, typeBinding)) {
					if (isPrivateAndNotStatic) {
						ret = getPrivateVar(declaringClass, false) + ".";
						isThis = true;
					} else if (superLevel == 1) {
						ret = "this.";
						isThis = true;
					}
					name = getJavaClassNameQualified(typeBinding);
					break;
				}
			}
			node = node.getParent();
		}
		return (isThis ? ret : getSyntheticReference(name) + ".");
	}

//	private String ensureNameIfLocal(String name, ITypeBinding typeBinding, ASTNode parent) {
//		if ((name == null || name.length() == 0) && typeBinding.isLocal()) {
//			name = typeBinding.getBinaryName();
//			int idx0 = name.lastIndexOf(".");
//			if (idx0 == -1) {
//				idx0 = 0;
//			}
//			int idx1 = name.indexOf('$', idx0);
//			if (idx1 != -1) {
//				int idx2 = name.indexOf('$', idx1 + 1);
//				String parentAnon = "";
//				if (idx2 == -1) { // maybe the name is already
//									// "$1$2..." for Java5.0+ in
//									// Eclipse 3.2+
//					parent = parent.getParent();
//					while (parent != null) {
//						if (parent instanceof AbstractTypeDeclaration) {
//							break;
//						} else if (parent instanceof AnonymousClassDeclaration) {
//							AnonymousClassDeclaration atype = (AnonymousClassDeclaration) parent;
//							ITypeBinding aTypeBinding = atype.resolveBinding();
//							String aName = aTypeBinding.getBinaryName();
//							parentAnon = aName.substring(aName.indexOf('$')) + parentAnon;
//						}
//						parent = parent.getParent();
//					}
//					name = name.substring(0, idx1) + parentAnon + name.substring(idx1);
//				}
//			}
//		}
//		return name;
//	}

	/**
	 * Get the synthetic reference for inner classes that reference "this" for outer
	 * classes
	 * 
	 * 
	 * @param className
	 * @return "this" + .qualifier
	 */
	private String getSyntheticReference(String className) {
		return "this" + (className.equals("java.lang.Object") || className.equals("Object") ? ""
				: className.equals(this$0Name) ? ".this$0"
						: ".b$['" + getFinalJ2SClassName(className, FINAL_RAW) + "']");
	}

	/**
	 * box or unbox as necessary
	 * 
	 * @param element
	 * @param toCharCode true to append .c$(), not .valueOf();
	 * @return true if boxing or unboxing
	 */
	private boolean appendBoxingNode(ASTNode element, boolean toCharCode) {
		// Double > x will be unboxed
		// Character == 'c' will be unboxed
		// f$Integer(int) will be boxed
		// buffer.append("/* ?? " + (element instanceof Expression) + " " +
		// element.getClass().getName() + " " + element + " ?? */");
		if (element instanceof Expression) {
			Expression exp = (Expression) element;
			if (exp.resolveBoxing()) {
				// expression is the site of a boxing conversion
				ITypeBinding typeBinding = exp.resolveTypeBinding();
				if (typeBinding.isPrimitive()) {
					String name = typeBinding.getName();
					name = (name.equals("char") ? "Character"
							: name.equals("int") ? "Integer"
									: Character.toUpperCase(name.charAt(0)) + name.substring(1));
					buffer.append("new " + name + "(");
					element.accept(this);
					buffer.append(")");
					return true;
				}
			} else if (exp.resolveUnboxing()) {
				// expression is the site of an unboxing conversion
				ITypeBinding typeBinding = exp.resolveTypeBinding();
				if (!typeBinding.isPrimitive()) {
					String name = getJavaClassNameQualified(typeBinding);
					name = (name.indexOf("Integer") >= 0 ? "int"
							: name.indexOf("Character") >= 0 ? "char" : name.replace("java.lang.", "").toLowerCase());
					buffer.append("(");
					element.accept(this);
					buffer.append(toCharCode && name == "char" ? ")" + CHARCODEAT0 : ")." + name + "Value$()");
					return true;
				}
			}
			if (!(exp instanceof ParenthesizedExpression) && !(exp instanceof PrefixExpression)
					&& !(exp instanceof InfixExpression) && !(exp instanceof PostfixExpression)
					&& getConstantValue(exp, true)) {

				// buffer.append("/* !!! */");

				return false;
			}
		}
		// buffer.append("/* >>> "+ element.getClass().getName() + "*/");
		element.accept(this);
		// buffer.append("/* <<< */");
		return false;
	}

	private boolean checkSimpleBooleanOperator(String op) {
		return (op.equals("^") || op.equals("|") || op.equals("&"));
	}

	/**
	 * Check to see if we have a static variable.
	 * 
	 * @param varBinding
	 * @return
	 */
	private boolean isStaticBinding(IVariableBinding varBinding) {
		return (isStatic(varBinding) && varBinding.getDeclaringClass() != null);
	}

	static String getJavaClassNameQualified(ITypeBinding binding) {
		// about binding.isLocal()
		//
		// Returns whether this type binding represents a local class.
		// A local class is any nested class or enum type not declared as a member of
		// another class or interface. A local class is a subspecies of nested type, and
		// mutually exclusive with member types. For anonymous classes, which are
		// considered a subspecies of local classes, this method returns true.
		// Note: This deviates from JLS3 14.3, which states that anonymous types are not
		// local types since they do not have a name. Also note that interfaces and
		// annotation types cannot be local.
		//
		// about binding.getBinaryName()
		//
		// Note that in some cases, the binary name may be unavailable. This may happen,
		// for example, for a local type declared in unreachable code.
		//
		// about binding.getKey() (not relevant)
		//
		// Note that the key for member value pair bindings is not yet implemented. This
		// method returns null for that kind of bindings.
		//

		if (binding == null)
			return null;
		String binaryName = null, bindingKey;
		if ((binding.isAnonymous() || binding.isLocal()) && (binaryName = binding.getBinaryName()) == null
				&& (bindingKey = binding.getKey()) != null)
			binaryName = bindingKey.substring(1, bindingKey.length() - 1).replace('/', '.');
		return (binaryName == null ? binding.getQualifiedName() : binaryName);
	}

	private static String getJavaClassNameSuperNoBrackets(ITypeBinding typeBinding) {
		ITypeBinding superclass = typeBinding.getSuperclass();
		if (superclass == null)
			return null;
		String qualifiedName = removeBracketsAndFixNullPackageName(getJavaClassNameQualified(superclass));
		return ("java.lang.Object".equals(qualifiedName) || "java.lang.Enum".equals(qualifiedName) ? null
				: qualifiedName);
	}

	private ITypeBinding getJavaClassSuper(ITypeBinding typeBinding) {
		return (typeBinding == null ? null : typeBinding.getSuperclass());
	}

	private int lambdaCount = 0;

	private String getMyJavaClassNameLambda(boolean andIncrement) {
		return package_name + "." + class_shortName.replace('.', '$') + "$lambda"
				+ (andIncrement ? ++lambdaCount : lambdaCount);
	}

	static String stripJavaLang(String name) {
		// shorten java.lang.XXX.YYY but not java.lang.xxx.YYY
		String s = (!name.startsWith("java.lang.") || name.equals("java.lang.Object")
				|| name.length() > 10 && !Character.isUpperCase(name.charAt(10)) ? name : name.substring(10));
		return s;
	}

	/**
	 * From visit(SimpleName) only.
	 * 
	 * @param node
	 * @return
	 */
	private String getFinalNameSimpleQualified(SimpleName node) {
		// xxx.yyy.zzz...
		IBinding binding = node.resolveBinding();
		ASTNode parent = node.getParent();
		if (parent == null) {
			// this does not appear to be reachable
			dumpStack("parent is null in returnFinalNameSimpleQualified");
			return node.toString();
		}
		char leadingChar = getLastCharInBuffer();
		boolean isQualified = (leadingChar == '.');
		// looking for "." or '"' here.
		if (isQualified && parent instanceof QualifiedName) {
			if (binding instanceof IVariableBinding)
				return getFinalFieldName((IVariableBinding) binding);
			// TODO could be a problem here is we have a conversion of a class in .j2s.
			// package or class-qualified:
			// java.lang.Math.xxx "lang" or "Math"
			return node.toString();
		}
		if (parent instanceof ClassInstanceCreation && !(binding instanceof IVariableBinding)) {
			if (binding == null) {
				// not possible?
				dumpStack(" binding null in ClassInstanceCreation");
			}
			return getFinalJ2SClassName(getJavaClassNameQualified(node.resolveTypeBinding()), FINAL_PC);
		}
		if (binding instanceof IVariableBinding)
			return getFinalNameForNode(node, leadingChar, (IVariableBinding) binding);

		if (binding instanceof IMethodBinding) {
// Arises when a lambda expression is used as a method parameter,
// in the one case we have, as a parameter to a constructor in java.util.stream.Collectors:
// In this case, "add" is the name:
//
//		              return new CollectorImpl<>(collectionFactory, 
//			                       Collection<T>::add,
//		                           (r1, r2) -> { r1.addAll(r2); return r1; },
//                                 CH_ID);
//		    }
// ...			
//	        CollectorImpl(
//  			Supplier<A> supplier,
//              BiConsumer<A, T> accumulator,
//              BinaryOperator<A> combiner,
//              Set<Characteristics> characteristics
//          ) {
//              this(supplier, accumulator, combiner, castingIdentity(), characteristics);
//          }			
			return getFinalDotQualifiedNameForMethod(node, (IMethodBinding) binding,
					(isQualified ? METHOD_ISQUALIFIED : METHOD_NOTSPECIAL));
		}

		// a class reference

		// static reference such as explicit "java.lang.Double" in Point2D
		ITypeBinding typeBinding = node.resolveTypeBinding();
		if (typeBinding == null)
			return node.getFullyQualifiedName();
		String name = getFinalJ2SClassName(getJavaClassNameQualified(typeBinding), FINAL_PC);
		return NameMapper.getJavaScriptCollisionIdentifier(name, true);
	}

	/**
	 * Get the final JavaScript name for a local variable, static or nonstatic
	 * field, which may or may not be a method qualifier.
	 * 
	 * Local variables are possibly prefixed with "this.$finals$."
	 * 
	 * @param node
	 * @param lastBufferChar looking for period, single quote, or double quote
	 * @param varBinding
	 * @return a fully qualified variable name
	 */
	private String getFinalNameForNode(SimpleName node, char lastBufferChar, IVariableBinding varBinding) {
		String qualifier = "";
		IVariableBinding variableDeclaration = varBinding.getVariableDeclaration();
		ITypeBinding declaringClass = variableDeclaration.getDeclaringClass();
		String name = varBinding.getName();
		if (isStatic(varBinding)) {
			// a static field
			if (lastBufferChar != '.' && lastBufferChar != '"' && lastBufferChar != '\'') {
				// an unescaped and not explicitly qualified name
				qualifier = getFinalJ2SClassNameQualifier(null, declaringClass, null, FINAL_ESCAPECACHE) + ".";
			}
		} else if (declaringClass != null) {
			// a nonstatic field
			ASTNode parent = node.getParent();
			if (parent == null) {
				// not possible?
				dumpStack("null parent??");
			}
			if (class_shortName == null) {
				// not possible?
				dumpStack("null class_shortName??");
			}
			if (class_shortName != null && lastBufferChar != '.' && parent != null && !(parent instanceof FieldAccess))
				qualifier = getClassNameAndDot(parent, declaringClass, false);
		} else {
			// declaring class is null -- a local variable or method argument
			IMethodBinding meth;
			String key;
			if (class_isAnonymousOrLocal && isFinalOrEffectivelyFinal(varBinding)
					&& (meth = varBinding.getDeclaringMethod()) != null
					&& (key = getNormalizedKey(meth)).indexOf(".lambda$") < 0
					&& (package_methodStackForFinals.size() == 0 || !key.equals(package_methodStackForFinals.peek()))) {
				qualifier = "this.$finals$.";
				if (package_currentBlockForVisit >= 0) {
					for (int i = package_finalVars.size(); --i >= 0;) {
						LocalVariable v = package_finalVars.get(i);
						if (v.isFinalAccess(name, package_currentBlockForVisit)) {
							if (!class_visitedVars.contains(v))
								class_visitedVars.add(v);
							break;
						}
					}
				}
			}
		}
		return qualifier + getFinalFieldOrLocalVariableName(declaringClass, name);
	}

	private static final int FINAL_RAW = 0;
	private static final int FINAL_P = 1;
	private static final int FINAL_C = 2;
	private static final int FINAL_PC = FINAL_P | FINAL_C;

	private static final int FINAL_ESCAPE = 8;
	private static final int FINAL_CACHE = 16;
	private static final int FINAL_ESCAPECACHE = FINAL_ESCAPE | FINAL_CACHE;

	private static final int FINAL_LAMBDA = 32;

	/**
	 * Provide access to C$.$clinit$ when a static method is called or a static
	 * field is accessed.
	 * 
	 * @param methodQualifier        SimpleName qualifier in qualifier.methodName()
	 *                               method invocation
	 * @param declaringJavaClassName the declaring class of the method or variable
	 * @param flags                  or of FINAL_ESCAPE except for static nonprivate
	 *                               field names; FINAL_CACHE generally true, but
	 *                               not for initial class definitions or for some
	 *                               nonstatic references
	 * @return name wrapped if necessary by nested Class.load() calls
	 */
	String getFinalJ2SClassNameQualifier(Name methodQualifier, ITypeBinding declaringJavaClass,
			String declaringJavaClassName, int flags) {
		// BH: The idea here is to load these on demand.
		// It will require synchronous loading,
		// but it will ensure that a class is only
		// loaded when it is really needed.

		if (declaringJavaClassName == null)
			declaringJavaClassName = getJavaClassNameQualified(declaringJavaClass);
		boolean doEscape = ((flags & FINAL_ESCAPE) == FINAL_ESCAPE);
		boolean doCache = ((flags & FINAL_CACHE) == FINAL_CACHE);

		String name = removeBracketsAndFixNullPackageName(declaringJavaClassName);
		doEscape &= !NameMapper.isClassKnown(name);
		if (!doEscape) {
			if (methodQualifier != null) {
				// a method invocation with a Name as qualifier expression
				methodQualifier.accept(this);
				return "";
			}
			return stripJavaLang(name);
		}
		if (doCache && name.equals(class_fullName)) {
			return "C$"; // anonymous class will be like this
		}

		// lambda classes will always be defined at this point. No need to cache them
		if (name.indexOf("$lambda") >= 0)
			return getFinalJ2SClassName(name, FINAL_P);

		return getFinalClazzLoadI$Reference(declaringJavaClass, name, doCache);
	}

	/**
	 * Turns "test.Test0.Exc1.Exc2" into "['test.Test0','.Exc1','.Exc2']"
	 * 
	 * For Clazz.newClass we want an array if the superclass is an inner class so
	 * that the outer class is guaranteed to be loaded first. The same goes for
	 * $I$[] dynamic class loading and interfaces, (but interfaces are handled
	 * differently).
	 * 
	 * This is a temporary will fix and will fail if the program does not adhere to
	 * class/package-capitalization standards.
	 * 
	 * @param packageName   Java package name or "_"
	 * @param javaClassName
	 * @return array listing classes that need to be loaded in order
	 */
	private String getFinalInnerClassList(ITypeBinding javaClass, String javaClassName) {
		if (javaClassName == null)
			javaClassName = getJavaClassNameQualified(javaClass);
		// called by addClassOrInterface and getFinalClazzLoadI
		if (javaClassName.indexOf("$lambda") >= 0)
			return "'" + getFinalJ2SClassName(javaClassName, FINAL_RAW) + "'";
		String name = removeBracketsAndFixNullPackageName(javaClassName);
		String packageName = getPackageName(javaClass);
//		if (name.indexOf(packageName) != 0) {
//			dumpStack("???? packageName " + packageName + " " + name);
//		}
		name = name.substring(packageName.length() + 1);
		String[] parts = name.split("\\.");
		String s = packageName + "." + parts[0];
		int len = parts.length;
		String ret = "'" + stripJavaLang(NameMapper.checkClassReplacement(s)) + "'";
		// add inner classes
		for (int i = 1; i < len; i++)
			ret += ",'." + parts[i] + "'";
		return (ret.indexOf(",") >= 0 ? "[" + ret + "]" : ret);
	}

	/**
	 * Register a qualified static name as an import var I$[n] unless it ends with
	 * "Exception". Create a loading list for inner classes.
	 * 
	 * 'pkg.Foo.Bar', for example, becomes ['pkg.Foo','.Bar'] for Clazz.load().
	 * 
	 * If caching, put into the code $I$(n), where n is the index into the I$[]
	 * array, starting at 1. [0] is reserved for the list of classes we are creating
	 * here.
	 * 
	 * @param javaClassName
	 * @param doCache
	 * @return the string to include in the buffer
	 */
	private String getFinalClazzLoadI$Reference(ITypeBinding javaClass, String javaClassName, boolean doCache) {
		String s = getFinalInnerClassList(javaClass, javaClassName);
		if (doCache) {
			Integer n = package_htIncludeNames.get(s);
			if (n == null && !s.endsWith("Exception'")) {
				// count starts at 1, because i$[0] is the list
				package_htIncludeNames.put(s, n = new Integer(++package_includeCount[0]));
				package_includes.append(package_includeCount[0] == 1 ? ",I$=[[0," : ",").append(s);
			}
			if (n != null)
				return "$I$(" + n + ")";
		}
		return "Clazz.load(" + s + ")";
	}

	/**
	 * Get the final JavaScript class name from a Java class name. Options include
	 * shortening the name with C$. and/or with P$. Brackets are removed. no-package
	 * names are prepended with "_." "java.lang." is removed where appropriate.
	 * 
	 * @param javaClassName
	 * @param flags         FINAL_C to allow C$, FINAL_P to allow P$, FINAL_PC for
	 *                      both of those; FINAL_RAW to just remove brackets, fix
	 *                      null package, and strip java.lang
	 * @return the final JavaScript name
	 */
	private String getFinalJ2SClassName(String javaClassName, int flags) {
		if (javaClassName == null)
			return null;
		String name = javaClassName;
		name = removeBracketsAndFixNullPackageName(name);

		if (((flags & FINAL_C) != 0)) {
			String myJavaClassName = class_fullName;
			if (name.equals(myJavaClassName)) {
				return "C$";
			}
			if (name.startsWith(myJavaClassName + ".")) {
				return "C$." + name.substring(myJavaClassName.length() + 1);
			}
		}
		name = stripJavaLang(NameMapper.checkClassReplacement(name));
		return ((flags & FINAL_P) == 0 ? name : checkPackageP$Name(name));
	}

	/**
	 * strips java.lang, removes brackets, may return P$
	 * 
	 * @param thisPackageName null to not allow P$
	 * @param javaName
	 * @return
	 */
	private String checkPackageP$Name(String javaName) {
		return (javaName.startsWith(package_name + ".") ? "P$." + javaName.substring(package_name.length() + 1)
				: javaName.equals(package_name) ? "P$" : javaName);
	}

	/**
	 * exp is xxxx or xxxx.yyyy or className.this
	 * 
	 * @param exp
	 * @return
	 */
	private boolean haveDirectStaticAccess(Expression exp) {
		return exp instanceof SimpleName
				|| (exp instanceof QualifiedName && ((QualifiedName) exp).getQualifier() instanceof SimpleName)
				|| (exp instanceof FieldAccess && ((FieldAccess) exp).getExpression() instanceof ThisExpression);

	}

	/**
	 * The left operand is primitive boolean. Check to see if the operator is ^, |,
	 * or &, or if the left or right operand is such an expression.
	 * 
	 * If so, we are going to box this all as a Boolean(....).valueOf()
	 * 
	 * @param node
	 * @return
	 */
	private boolean isBitwiseBinaryOperator(InfixExpression node) {
		if (checkSimpleBooleanOperator(node.getOperator().toString())) {
			return true;
		}
		Expression left = node.getLeftOperand();
		if (left instanceof InfixExpression) {
			if (isBitwiseBinaryOperator((InfixExpression) left)) {
				return true;
			}
		}
		Expression right = node.getRightOperand();
		if (right instanceof InfixExpression) {
			if (isBitwiseBinaryOperator((InfixExpression) right)) {
				return true;
			}
		}
		return false;
	}

	private void visitList(List<ASTNode> list, String separator) {
		for (Iterator<ASTNode> iter = list.iterator(); iter.hasNext();) {
			appendBoxingNode(iter.next(), false);
			if (iter.hasNext()) {
				buffer.append(separator);
			}
		}
	}

	private static Map<String, Map<String, List<String[]>>> genericClassMap = new HashMap<String, Map<String, List<String[]>>>();
	private static Map<String, Map<String, String>> genericClassTypes = new HashMap<String, Map<String, String>>();

	/**
	 * Check a class, interface, or Enum binding for generics.
	 * 
	 * This is used in the method declaration to add alias names to methods.
	 * 
	 * @param topBinding -- the class being declared
	 * @param binding
	 * @return true if this class could have generic replacements
	 */
	private static boolean checkGenericClass(ITypeBinding topBinding, ITypeBinding binding) {
		// debugListAllOverrides(binding);
		if (topBinding == binding)
			genericClassMap.put(binding.getKey(), null);
		// check all superclasses from most super to least super
		String classKey = binding.getKey();
		boolean hasGenerics = (binding.isRawType() || binding.getTypeArguments().length > 0);
		if (hasSuperClass(binding)) {
			hasGenerics = checkGenericClass(topBinding, binding.getSuperclass()) || hasGenerics;
		}
		// check all interfaces
		ITypeBinding[] interfaces = binding.getInterfaces();
		for (int i = interfaces.length; --i >= 0;) {
			hasGenerics = checkGenericClass(topBinding, interfaces[i]) || hasGenerics;
		}
		if (hasGenerics) {
			checkMethodsWithGenericParams(topBinding.getKey(), binding);
		} else {
			genericClassMap.put(classKey, null);
		}
		return hasGenerics;
	}

	/**
	 * Tie class type parameters (T, V, etc.) to the bound implemented types for all
	 * methods that implement generics
	 * 
	 * @param topClassKey
	 * @param binding
	 */
	private static void checkMethodsWithGenericParams(String topClassKey, ITypeBinding binding) {
		Map<String, String> classTypes = getGenericClassTypes(binding);
		if (classTypes == null)
			return;
		String classKey = binding.getKey();
		IMethodBinding[] methods = binding.getErasure().getDeclaredMethods();
		for (int i = methods.length; --i >= 0;) {
			IMethodBinding m = methods[i];
			String methodName = m.getName();
			ITypeBinding[] params = m.getParameterTypes();
			if (params.length == 0)
				continue;
			String key = m.getKey();
			if (key.indexOf(";T") >= 0 || key.indexOf("(T") >= 0) {
				String[] list = new String[params.length];
				for (int j = list.length; --j >= 0;) {
					String name = params[j].getName();
					list[j] = name + "|" + classTypes.get(name) + ";";
				}
				addGenericClassMethod(classKey, methodName, list);
				addGenericClassMethod(topClassKey, methodName, list);
			}
		}

	}

	private static ASTNode getAbstractOrAnonymousParentForNode(ASTNode node) {
		ASTNode parent = node.getParent();
		while (parent != null && !(parent instanceof AbstractTypeDeclaration)
				&& !(parent instanceof AnonymousClassDeclaration)) {
			parent = parent.getParent();
		}
		return parent;
	}

	private static ITypeBinding resolveAbstractOrAnonymousBinding(ASTNode node) {
		node = getAbstractOrAnonymousParentForNode(node);
		return (node instanceof AbstractTypeDeclaration ? ((AbstractTypeDeclaration) node).resolveBinding()
				: node instanceof AnonymousClassDeclaration ? ((AnonymousClassDeclaration) node).resolveBinding()
						: null);
	}

	/**
	 * Create a map of the class type arguments for an implemented generic class
	 * 
	 * @param type
	 * @return a map {T:"java.lang.String",K:"java.lang.Object"}
	 */
	private static Map<String, String> getGenericClassTypes(ITypeBinding type) {
		String classKey = type.getKey();
		Map<String, String> classTypes = genericClassTypes.get(classKey);
		if (classTypes != null)
			return classTypes;
		ITypeBinding[] typeArgs = type.getTypeArguments();
		ITypeBinding[] typeParams = type.getTypeParameters();
		boolean isGeneric = (typeParams.length > 0);
		boolean isExtended = (typeArgs.length > 0 || type.isRawType());
		if (!isGeneric && !isExtended) {
			if (hasSuperClass(type))
				genericClassTypes.put(classKey, classTypes = genericClassTypes.get(type.getSuperclass().getKey()));
			return classTypes;
		}
		ITypeBinding[] types = (isGeneric ? typeParams : typeArgs);
		classTypes = new Hashtable<String, String>();
		// We have to parse this by hand, because I cannot seem to get access to
		// the
		// typeParameters of a superclass. Java seems to have erased all that.
		String erasure = type.getErasure().toString();
		// abstract class test.Test_GenericExt_T<T extends Map<T,K>, K>
		erasure = erasure.substring(erasure.indexOf("<") + 1);
		StringBuffer sb = new StringBuffer(erasure.substring(0, erasure.indexOf(">\n")));
		for (int n = 0, i = sb.length(); --i >= 0;) {
			switch (sb.charAt(i)) {
			case '>':
				n++;
				sb.setCharAt(i, ' ');
				break;
			case '<':
				n--;
				sb.setCharAt(i, ' ');
				break;
			case ',':
				if (n != 0)
					sb.setCharAt(i, ' ');
				break;
			default:
				break;
			}
		}

		String[] tokens = sb.toString().split(",");
		for (int i = tokens.length; --i >= 0;) {
			String key = tokens[i].trim();
			key = key.substring(0, (key + " ").indexOf(" "));
			String value = (i < types.length ? getJavaClassNameQualified(types[i]) : "java.lang.Object");
			classTypes.put(key, value);
		}
		return classTypes;
	}

	/**
	 * Retrieve a list of generic types such as { ["T|java.lang.String",
	 * "V|java.lang.Object"], ["M|java.lang.String", "N|java.lang.Object"] } if it
	 * exists
	 * 
	 * @param methodClass
	 * @param methodName
	 * @return list of generic types for methods with this name
	 */
	private static List<String[]> getGenericMethodList(ITypeBinding methodClass, String methodName) {
		Map<String, List<String[]>> methodList = genericClassMap.get(methodClass.getKey());
		return (methodList == null ? null : methodList.get(methodName));
	}

	/**
	 * add a generic class method to the genericClassMap under the class and method
	 * 
	 * @param classKey
	 * @param methodName
	 * @param list
	 */
	private static void addGenericClassMethod(String classKey, String methodName, String[] list) {

		Map<String, List<String[]>> classMap = genericClassMap.get(classKey);
		if (classMap == null)
			genericClassMap.put(classKey, classMap = new Hashtable<String, List<String[]>>());
		List<String[]> methodList = classMap.get(methodName);
		if (methodList == null)
			classMap.put(methodName, methodList = new ArrayList<String[]>());
		methodList.add(list);
	}

	/**
	 * 
	 * This is the method used to get the name or names to write into the method
	 * declaration Clazz.newMeth(...). Bracketed returns tell Clazz to create
	 * multiple aliases for the same method.
	 * 
	 * @param node
	 * @param mBinding
	 * @param isConstructor
	 * @param qualification
	 * @return j2s-qualified name or an array of j2s-qualified names
	 */
	private String getFinalMethodNameOrArrayForDeclaration(IMethodBinding mBinding, boolean isConstructor,
			int qualification) {
		String nodeName = mBinding.getName();
		String methodName = (isConstructor ? "c$" : nodeName);
		String qname = getFinalMethodNameWith$Params(methodName, null, mBinding, null, false, METHOD_NOTSPECIAL);
		ITypeBinding methodClass = mBinding.getDeclaringClass();
		List<String> names = null;
		List<String[]> methodList = getGenericMethodList(methodClass, nodeName);

		if (methodList != null) {
			names = new ArrayList<String>();
			for (int i = methodList.size(); --i >= 0;) {
				String pname = getFinalMethodNameWith$Params(methodName, null, mBinding, methodList.get(i), false,
						METHOD_NOTSPECIAL);
				if (pname != null)
					names.add(pname);
				if (qualification != METHOD_FULLY_QUALIFIED)
					names.add(ensureMethod$Name(methodName, mBinding, getJavaClassNameQualified(methodClass)));
			}
		}
		if ((qualification & METHOD_$_QUALIFIED) != 0 && !methodName.equals(qname)
				&& !classHasNoParameterMethod(methodClass, methodName)) {
			if (names == null)
				names = new ArrayList<String>();
			names.add(methodName + (methodName.indexOf("$") >= 0 ? "" : "$"));
		}
		if ((qualification & METHOD_UNQUALIFIED) != 0) {
			if (names == null)
				names = new ArrayList<String>();
			names.add(methodName);
		}
		if (names == null || names.size() == 0)
			return "'" + qname + "'";
		qname = ",'" + qname + "'";
		for (int i = names.size(); --i >= 0;) {
			String next = ",'" + names.get(i) + "'";
			if (qname.indexOf(next) < 0)
				qname += next;
		}
		return "[" + qname.substring(1) + "]";
	}

	private static boolean classHasNoParameterMethod(ITypeBinding methodClass, String methodName) {
		while (methodClass != null) {
			IMethodBinding[] methods = methodClass.getDeclaredMethods();
			for (int i = methods.length; --i >= 0;) {
				IMethodBinding m = methods[i];
				if (m.getName().equals(methodName) && m.getParameterTypes().length == 0 && !isPrivate(m))
					return true;
			}
			methodClass = methodClass.getSuperclass();
		}
		return false;
	}

	/**
	 * Add postfix "$" is the method is:
	 * 
	 * (a) static or not private, and
	 * 
	 * (b) there is no field name that covers it, and
	 * 
	 * (c) it doesn't already have a $ in it after the 3rd character (P$..., C$...
	 * excepted), and
	 * 
	 * (d) the name is not "c$", which is reserved for function(s) { s.charCodeAt(0)
	 * }, and
	 * 
	 * (e) the method is not an explicitly nonqualified method (namely, toString())
	 * or in an explicitly nonqualified class
	 * 
	 * @param j2sName
	 * @param mBinding
	 * @param className
	 * @return
	 */
	private static String ensureMethod$Name(String j2sName, IMethodBinding mBinding, String className) {
		if (isPrivate(mBinding) && !isStatic(mBinding) || NameMapper.fieldNameCoversMethod(j2sName)
				|| j2sName.indexOf("$", 2) >= 0 || j2sName.equals("c$")
				|| className != null && NameMapper.isMethodNonqualified(className, mBinding.getName()))
			return j2sName;
		return j2sName + "$";
	}

	/**
	 * finish the generic foo || bar fix
	 * 
	 * @param pt                    start of this method invocation in buffer
	 * @param qname                 qualified name, containing " || "
	 * @param isPrivateAndNotStatic switch $O$ to p$; already using .apply(this)
	 */
	private void postFixGeneric$OMethodName(int pt, String qname, boolean isPrivateAndNotStatic, String privateVar) {
		// this is a Java8-compatibility hack. The class is accessing a
		// type-parameterized method which it might not be overriding
		// and might be nongeneric in Java 6.
		// this.adItem$TE(o) becomes (($o$=this).addItem$TE ||
		// $o$.addItem$O).apply($o$,[o]);
		if (isPrivateAndNotStatic) {
			buffer.insert(pt, "(");
			buffer.append(qname.replace("$o$", privateVar)).append(")");
			return;
		}
		buffer.insert(pt, "(($o$=");
		buffer.append(qname).append(").apply($o$,[");
		buffer.insert(buffer.lastIndexOf(".", buffer.lastIndexOf("|")), ")");
		trailingBuffer.addType("o");
	}

	private boolean isJava(String className) {
		return className.length() > 5 && "java.javax".contains(className.substring(0, 5));
	}

	private static String getParamsAsString(int nParams, String[] genericTypes, ITypeBinding[] paramTypes,
			boolean toObject) {
		StringBuffer sbParams = new StringBuffer();
		// if this is a method invocation and has generics, then we alias that
		boolean haveGeneric = false;
		for (int i = 0; i < nParams; i++) {
			String type = j2sGetParamCode(paramTypes[i], true, toObject);
			if (genericTypes != null) {
				String genericType = genericTypes[i];
				if (genericType != null) {
					if (genericType.indexOf("|null") < 0) {
						if (genericType.indexOf("|" + getJavaClassNameQualified(paramTypes[i]) + ";") < 0)
							return null;
						type = "T" + genericType.substring(0, genericType.indexOf("|")); // "O";//
						haveGeneric = true;
						// Originally I was substituting in the generic type
						// T,V,E,etc., but
						// this causes a problem when the user is working with a
						// later version of
						// Java and subclassing what was originally not a
						// generic class (JComboBox)
						// but which is now generic (JComboBox<E>). The new
						// version of Java will be
						// used by the transpiler working on the user's machine,
						// and then we will
						// have the problem that the code will have addItem$TE
						// inserted even though
						// the version of Java in the SwingJS distribution will
						// be only addItem$O.
						// Using Object here because that would be the default
						// for
						// JComboBox<>
						// and so match that earlier non-generic designation
						// (hopefully).
					}
				}
			}
			sbParams.append("$").append(type);
		}
		return (toObject && !haveGeneric ? null : sbParams.toString());
	}

	private static String j2sGetParamCode(ITypeBinding binding, boolean addAAA, boolean asGenericObject) {
		String prefix = (removeBracketsAndFixNullPackageName(binding.getKey()).indexOf(":T") >= 0 ? "T" : null);
		String name = removeBrackets(getJavaClassNameQualified(binding));
		if (!asGenericObject && prefix == null && !binding.isPrimitive())
			name = NameMapper.fixPackageName(name);
		String arrays = null;
		int pt = name.indexOf("[");
		if (pt >= 0) {
			arrays = name.substring(pt + (name.indexOf("[L") >= 0 ? 1 : 0));
			name = name.substring(0, pt);
		}

		// NOTE: If any of these are changed, they must be changed in j2sSwingJS
		// as well.
		// NOTE: These are the same as standard Java Spec, with the exception of
		// Short, which is "H" instead of "S"

		switch (name) {
		case "boolean":
			name = "Z";
			break;
		case "byte":
			name = "B";
			break;
		case "char":
			name = "C";
			break;
		case "double":
			name = "D";
			break;
		case "float":
			name = "F";
			break;
		case "int":
			name = "I";
			break;
		case "long":
			name = "J";
			break;
		case "short":
			name = "H"; // differs from Java Spec so we can use S for String
			break;
		case "java.lang.Object":
		case "Object":
			name = "O";
			break;
		case "java.lang.String":
			name = "S";
			break;
		default:
			if (prefix == null)
				name = NameMapper.checkClassReplacement(name);
			else
				name = (asGenericObject ? "O" : prefix + name); // "O";//
			name = name.replace("java.lang.", "").replace('.', '_');
			break;
		}
		if (arrays != null) {
			if (addAAA)
				arrays = arrays.replaceAll("\\[\\]", "A");
			name += arrays;
		}
		return name;
	}

	/**
	 * Remove <...> in class and method names
	 * 
	 * @param qName
	 * @return
	 */
	static String removeBracketsAndFixNullPackageName(String qName) {
		if (qName == null)
			return null;
		qName = NameMapper.fixPackageName(qName);
		return removeBrackets(qName);
	}

	private static String removeBrackets(String qName) {
		if (qName.indexOf('<') < 0)
			return qName;
		StringBuffer buf = new StringBuffer();
		int ltCount = 0;
		char c;
		for (int i = 0, len = qName.length(); i < len; i++) {
			switch (c = qName.charAt(i)) {
			case '<':
				ltCount++;
				continue;
			case '>':
				ltCount--;
				continue;
			default:
				if (ltCount == 0)
					buf.append(c);
				continue;
			}
		}
		return buf.toString().trim();
	}

	/**
	 * Returns <code>true</code> if the given type is a super type of a candidate.
	 * <code>true</code> is returned if the two type bindings are identical
	 * 
	 * @param possibleSuperType the type to inspect
	 * @param type              the type whose super types are looked at
	 * @return <code>true</code> iff <code>possibleSuperType</code> is a super type
	 *         of <code>type</code> or is equal to it
	 */
	private static boolean isSuperType(ITypeBinding possibleSuperType, ITypeBinding type) {
		if (type.isArray() || type.isPrimitive()) {
			return false;
		}
		String name;
		if (possibleSuperType.isEqualTo(type)
				|| (name = possibleSuperType.getBinaryName()) != null && name.equals(type.getBinaryName()))
			return true;
		ITypeBinding superClass = type.getSuperclass();
		if (superClass != null && isSuperType(possibleSuperType, superClass))

			return true;

		if (possibleSuperType.isInterface()) {
			ITypeBinding[] superInterfaces = type.getInterfaces();
			for (int i = 0; i < superInterfaces.length; i++) {
				if (isSuperType(possibleSuperType, superInterfaces[i])) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean hasSuperClass(ITypeBinding typeBinding) {
		ITypeBinding superclass = typeBinding.getSuperclass();
		return (superclass != null && !"java.lang.Object".equals(superclass.getQualifiedName()));
	}

	private Object getConstant(Expression exp) {
		return (noDocProblem(exp) ? exp.resolveConstantExpressionValue() : null);
	}

	private boolean noDocProblem(Expression exp) {
		return (getJ2sJavadoc(exp, DOC_CHECK_ONLY) == null
				|| !(exp instanceof PrefixExpression || exp instanceof InfixExpression
						|| exp instanceof PostfixExpression || exp instanceof ParenthesizedExpression));
	}

	/**
	 * If given expression is constant value expression, return its value string; or
	 * character or return null.
	 * 
	 * @param node
	 * @return
	 */
	private boolean getConstantValue(Expression node, boolean andWrite) {
		if (node == null)
			return false;
		Object constValue = getConstant(node);
		StringBuffer sb = null;
		if (constValue instanceof Number) {
			sb = new StringBuffer();
			String s = constValue.toString();
			if (s.startsWith("-") && buffer.charAt(buffer.length() - 1) == '-')
				sb.append(' ');
			sb.append(s);
		} else if (constValue instanceof Character || constValue instanceof Boolean) {
			sb = new StringBuffer();
  			if (constValue instanceof Character) {
				sb.append('"');
				addChar(((Character) constValue).charValue(), sb);
				sb.append('"');
			} else {
				sb.append(constValue);
			}
		} else if (constValue instanceof String) {
			sb = new StringBuffer();
			String str = (String) constValue;
			int length = str.length();
			sb.append('"');
			for (int i = 0; i < length; i++)
				addChar(str.charAt(i), sb);
			sb.append('"');
		}
		if (sb == null)
			return false;
		if (andWrite) {
			// this is just in case we have (/** @j2sNative 1?x:*/y);
			boolean needParen = (node instanceof ParenthesizedExpression);
			if (needParen)
				buffer.append("(");
			addJ2SDoc(node);
			buffer.append(sb);
			if (needParen)
				buffer.append(")");
		}
		return true;
	}

	private static void addChar(char c, StringBuffer buffer) {
		switch (c) {
		case '\\':
		case '\'':
		case '\"':
			buffer.append('\\');
			buffer.append(c);
			break;
		case '\r':
			buffer.append("\\r");
			break;
		case '\n':
			buffer.append("\\n");
			break;
		case '\t':
			buffer.append("\\t");
			break;
		case '\f':
			buffer.append("\\f");
			break;
		default:
			if (c < 32 || c > 127) {
				String hexStr = "0000" + Integer.toHexString(c);
				buffer.append("\\u").append(hexStr.substring(hexStr.length() - 4));
			} else {
				buffer.append(c);
			}
			break;
		}
	}

////////////////////////

	private void setMapJavaDoc(PackageDeclaration node) {
		ASTNode root = node.getRoot();
		package_mapBlockJavadoc = new HashMap<Integer, List<Javadoc>>();

		// gat a list of all @j2s blocks

		List<ASTNode> list = new ArrayList<ASTNode>();
		List<?> commentList = ((CompilationUnit) root).getCommentList();
		for (int i = 0, n = commentList.size(); i < n; i++) {
			Comment comment = (Comment) commentList.get(i);
			if (comment instanceof Javadoc) {
				List<?> tags = ((Javadoc) comment).tags();
				if (tags.size() != 0) {
					for (Iterator<?> itr = tags.iterator(); itr.hasNext();) {
						TagElement tagEl = (TagElement) itr.next();
						String tagName = tagEl.getTagName();
						if (tagName == null || !tagName.startsWith("@j2sNative") && !tagName.startsWith("@j2sIgnore")
								&& !tagName.startsWith("@j2sDebug"))
							continue;
						list.add(comment);
						break;
					}
				}
			}
		}
		if (list.isEmpty())
			return;

		// now add all the associated elements

		try {
			root.accept(new NativeDoc.BlockVisitor(list));
		} catch (@SuppressWarnings("unused") IndexOutOfBoundsException e) {
			// normal termination from item after last j2sjavadoc
		}

//??		System.out.println("/**** list ****/");
//??		for (int i = 0, n = list.size(); i < n; i++) {
//??			System. out.println(i + "  " + (list.get(i) == null ? null : list.get(i).getClass().getName() + " " + list.get(i).getStartPosition() + "..." + (list.get(i).getStartPosition() + list.get(i).getLength())));
//??		}

		// and link javadoc to its closest block

		for (int i = 0, n = list.size() - 1; i < n;) {
			Javadoc doc = (Javadoc) list.get(i++);
			ASTNode item = list.get(i);
			int factor = 1;
			if (item instanceof Javadoc) {
				logErr("!!Note: @j2s doc ignored because nothing follows it: " + doc.getStartPosition() + "\r\n" + doc);
			} else {
				if (item == null) {
					factor = -1;
					item = list.get(++i);
				}
				i++;
				Integer pt = Integer.valueOf(item.getStartPosition() * factor);
				List<Javadoc> docs = package_mapBlockJavadoc.get(pt);
				if (docs == null)
					package_mapBlockJavadoc.put(pt, docs = new ArrayList<Javadoc>());
				docs.add(doc);
			}
		}
	}

	/**
	 * check any node other than the package node for @j2sNative or @j2sDebug
	 * or @j2sIgnore
	 */
	public boolean preVisit2(ASTNode node) {
		return (node instanceof ParenthesizedExpression || !addJ2SDoc(node) || !(node instanceof Block));
	}

	private final static int DOC_CHECK_ONLY = 0;
	private final static int DOC_ADD_PRE = 1;
	private final static int DOC_ADD_POST = 2;

	/**
	 * 
	 * @param node
	 * @return true if code was added
	 */
	private boolean addJ2SDoc(ASTNode node) {
		List<Javadoc> j2sJavadoc;
		if (package_mapBlockJavadoc == null || node instanceof MethodDeclaration || node instanceof Initializer
				|| (j2sJavadoc = getJ2sJavadoc(node, DOC_CHECK_ONLY)) == null || node instanceof InfixExpression
						&& ((InfixExpression) node).getLeftOperand() instanceof ParenthesizedExpression)
			return false;
		boolean ret = NativeDoc.addJ2sJavadocs(buffer, j2sJavadoc, node instanceof Block);
		j2sJavadoc.clear();
		return ret;
	}

	private List<Javadoc> getJ2sJavadoc(ASTNode node, int mode) {

		// package_mapBlockJavadoc will be null for a no-package class like VARNA.java,
		// which are not allowed to use @j2sNative

		if (package_mapBlockJavadoc == null)
			return null;
		List<Javadoc> docs;
		if (mode == DOC_ADD_POST) {
			docs = package_mapBlockJavadoc.remove(Integer.valueOf(-1 * node.getStartPosition()));
			if (docs != null)
				NativeDoc.addJ2sJavadocs(buffer, docs, false);
		} else {
			docs = package_mapBlockJavadoc.get(Integer.valueOf(node.getStartPosition()));
		}
		return docs;
	}

	private final static int CHECK_J2S_IGNORE_ONLY = 1;
	private final static int CHECK_ANNOTATIONS_ONLY = 2;
	private final static int CHECK_J2S_IGNORE_AND_ANNOTATIONS = 3;

	/**
	 * Method with "j2s*" tag.
	 * 
	 * @param node
	 * @return false if we have @j2sIngore for this BodyDeclaration
	 */
	private boolean checkAnnotations(BodyDeclaration node, int mode) {
		if (mode != CHECK_ANNOTATIONS_ONLY) {
			Javadoc javadoc = node.getJavadoc();
			if (javadoc != null) {
				List<?> tags = javadoc.tags();
				if (tags.size() != 0) {
					for (Iterator<?> iter = tags.iterator(); iter.hasNext();) {
						TagElement tagEl = (TagElement) iter.next();
						if ("@j2sIgnore".equals(tagEl.getTagName())) {
							return false;
						}
					}
				}
			}
		}
		List<?> modifiers = node.modifiers();
		if (modifiers != null && modifiers.size() > 0) {

			for (Iterator<?> iter = modifiers.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof Annotation) {
					if (!addAnnotation((Annotation) obj, node, mode))
						return false;
				}
			}
		}
		return true;
	}

	private boolean addAnnotation(Annotation annotation, ASTNode node, int mode) {
		String qName = annotation.getTypeName().getFullyQualifiedName();
		int idx = qName.indexOf("J2S");
		if (idx >= 0) {
			if (mode == CHECK_ANNOTATIONS_ONLY)
				return true;
			String annName = qName.substring(idx);
			if (annName.startsWith("J2SIgnore")) {
				return false;
			}
		} else if (qName.equals("Override") || qName.equals("Deprecated") || qName.startsWith("Suppress")
				|| qName.startsWith("ConstructorProperties")) {
			// see java\awt\ScrollPane.js @ConstructorProperties({"scrollbarDisplayPolicy"})
			// ignore
		} else {
			if (class_annotations == null)
				class_annotations = new ArrayList<ClassAnnotation>();
			class_annotations.add(new ClassAnnotation(qName, annotation, node));
			if ("XmlAccessorType".equals(qName)) {
				String s = annotation.toString();
				class_jaxbAccessorType = (s.contains("FIELD") ? JAXB_TYPE_FIELD
						: s.contains("PUBLIC") ? JAXB_TYPE_PUBLIC_MEMBER
								: s.contains("PROPERTY") ? JAXB_TYPE_PROPERTY : JAXB_TYPE_NONE);
			} else if (qName.startsWith("XmlEnum")) {
				class_jaxbAccessorType = JAXB_TYPE_ENUM;
			} else if (class_jaxbAccessorType == JAXB_TYPE_UNKNOWN && qName.startsWith("Xml")) {
				System.out.println(">>>unspecified!");
				class_jaxbAccessorType = JAXB_TYPE_UNSPECIFIED;
			} else if ("XmlElements".equals(qName) && annotation.isSingleMemberAnnotation()) {
				Expression e = ((SingleMemberAnnotation) annotation).getValue();
				if (e instanceof ArrayInitializer) {
					List<Expression> expressions = ((ArrayInitializer) e).expressions();
					for (int i = expressions.size(); --i >= 0;) {
						Expression exp = expressions.get(i);
						if (exp instanceof Annotation) {
							class_annotations.add(new ClassAnnotation(qName, (Annotation) exp, node));
						}
					}
				}

			}
		}
		return true;
	}

	/////////////////////////////

	/**
	 * includes @j2sDebug blocks; from j2s.compiler.mode=debug in .j2s
	 * 
	 */
	static boolean global_j2sFlag_isDebugging = false;

	public static void setDebugging(boolean isDebugging) {
		global_j2sFlag_isDebugging = isDebugging;
	}

	private static List<String> global_lstMethodsDeclared;

	private static Map<String, String> global_htMethodsCalled;

	private static boolean global_logAllCalls;

	public static void setLogging(List<String> lstMethodsDeclared, Map<String, String> htMethodsCalled,
			boolean logAllCalls) {
		global_lstMethodsDeclared = lstMethodsDeclared;
		global_htMethodsCalled = htMethodsCalled;
		global_logAllCalls = logAllCalls;
		if (lstMethodsDeclared != null)
			lstMethodsDeclared.clear();
		if (logAllCalls)
			htMethodsCalled.clear();
	}

	private void logMethodDeclared(String name) {
		if (name.startsWith("[")) {
			String[] names = name.substring(0, name.length() - 1).split(",");
			for (int i = 0; i < names.length; i++)
				logMethodDeclared(names[i]);
			return;
		}
		String myName = fixLogName(class_fullName);
		if (name.startsWith("'"))
			name = name.substring(1, name.length() - 1);
		global_lstMethodsDeclared.add(myName + "." + name);
	}

	private void logMethodCalled(String name) {
		name = fixLogName(name);
		String myName = fixLogName(class_fullName);
		if (global_logAllCalls)
			global_htMethodsCalled.put(name + "," + myName, "-");
		else
			global_htMethodsCalled.put(name, myName);
	}

	private String fixLogName(String name) {
		name = NameMapper.checkClassReplacement(name);
		int pt = name.indexOf("<");
		return (pt > 0 ? name.substring(0, pt) : name);
	}

	/**
	 * tracks file byte pointers for @j2sNative, @j2sIgnore
	 */
	private Map<Integer, List<Javadoc>> package_mapBlockJavadoc;

	private boolean allowClazzNewLambda;

	/**
	 * separates top-level classes found in a source file
	 * 
	 */
	private static final String ELEMENT_KEY = "__@J2S_ELEMENT__";

	/**
	 * Add the top-level class name with the element key.
	 *
	 * @param className
	 */
	private void appendElementKey(String className) {
		buffer.append(ELEMENT_KEY + ("=" + className) + "\r\n");
	}

	/**
	 * Separate the buffer into a list so that all top-level elements can be in
	 * their own file (as is done in Java). Provide a common include list
	 * 
	 * We do not have to worry about inner classes, as they are never referenced
	 * directly.
	 * 
	 * @return List {elementName, js, elementName, js, ....}
	 */
	public List<String> getElementList() {
		if (class_jaxbAccessorType != JAXB_TYPE_UNKNOWN) {
			addDummyClassForPackageOnlyFile();
		}

		String trailer = ";Clazz.setTVer('" + VERSION + "');//Created "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " Java2ScriptVisitor version "
				+ VERSION + " net.sf.j2s.core.jar version " + CorePlugin.VERSION + "\n";
		List<String> elements = new ArrayList<String>();
		String js = buffer.toString();
		String eq = "="; // because we might be operating on this file
		String[] parts = js.split(ELEMENT_KEY + eq);
		String header = parts[0];
		String header_noIncludes = header.replace(",I$=[[]]", "");
		header = header.replace(",I$=[]", privateVarString + (package_includes.length() == 0 ? ""
				: package_includes.append("]],$I$=function(i){return I$[i]||(I$[i]=Clazz.load(I$[0][i]))}")));
		for (int i = 1; i < parts.length; i++) {
			js = parts[i];
			int pt = js.indexOf("\r\n");
			String name = js.substring(0, pt);
			elements.add(name);
			js = js.substring(pt + 2);
			String head = "(function(){"
					+ (js.indexOf("$I$(") < 0 && js.indexOf("p$") < 0 ? header_noIncludes : header);
			elements.add(head + js + "})();\r\n" + trailer);
		}
		resetPrivateVars();
		return elements;
	}

	private void addDummyClassForPackageOnlyFile() {
		appendElementKey("_$");
		buffer.append("var C$=Clazz.newClass(\"_$\");\nC$.$clinit$ = function() {Clazz.load(C$, 1)};\n");
		ClassAnnotation.addClassAnnotations(class_jaxbAccessorType, class_annotations, null, null, null, null,
				trailingBuffer);
		buffer.append(trailingBuffer);
		addDefaultConstructor();
	}

	public boolean visit(AnnotationTypeDeclaration node) {
		return false;
	}

	public boolean visit(AnnotationTypeMemberDeclaration node) {
		return false;
	}

	public boolean visit(BlockComment node) {
		return false;
	}

	public boolean visit(ImportDeclaration node) {
		return false;
	}

	public boolean visit(Javadoc node) {
		return false;
	}

	public boolean visit(LineComment node) {
		return false;
	}

	public boolean visit(MarkerAnnotation node) {
		return false;
	}

	public boolean visit(MemberRef node) {
		return false;
	}

	public boolean visit(MemberValuePair node) {
		return false;
	}

	public boolean visit(MethodRef node) {
		return false;
	}

	public boolean visit(MethodRefParameter node) {
		return false;
	}

	public boolean visit(NormalAnnotation node) {
		return false;
	}

	public boolean visit(ParameterizedType node) {
		node.getType().accept(this);
		return false;
	}

	public boolean visit(SingleMemberAnnotation node) {
		return false;
	}

	public boolean visit(TagElement node) {
		return false;
	}

	public boolean visit(TextElement node) {
		return false;
	}

	public boolean visit(TypeParameter node) {
		return false;
	}

	public boolean visit(WildcardType node) {
		return false;
	}

	/**
	 * Prepend a $ to a local var name if there is a JavaScript keyword collision.
	 * Either a local variable or a method invocation parameter.
	 * 
	 * @param localName
	 * @return localName or $localName
	 */
	private static String getFinalVarName(String localName) {
		return NameMapper.getJavaScriptCollisionIdentifier(localName, true);
	}

	private static String getFinalFieldName(IVariableBinding binding) {
		return getFinalFieldOrLocalVariableName(binding.getDeclaringClass(), binding.getName());
	}

	/**
	 * Prepend a $ to a field name if there is a JavaScript keyword collision, and n
	 * more if there are collisions with higher-level field names.
	 * 
	 * This method no longer checks for field-method collisions.
	 * 
	 * @param classBinding
	 * @param fieldName
	 * @param isPrivate
	 * @return
	 */
	private static String getFinalFieldOrLocalVariableName(ITypeBinding classBinding, String fieldName) {
		String js$ = NameMapper.getJavaScriptCollisionIdentifier(fieldName, false);
		return (isJ2SInheritedFieldName(classBinding, fieldName) ? NameMapper.getJ2S$$InheritedFieldName(classBinding,
				fieldName, NameMapper.newFieldNameBuf(fieldName, new StringBuffer(js$))).toString() : js$ + fieldName);
	}

	/**
	 * Iteratively check whether the given field name is already defined in a stack
	 * of classes or interfaces.
	 * 
	 * The algorithm:
	 * 
	 * 1. Check self (class or interface)
	 * 
	 * 2. Check super class
	 * 
	 * 3. Check interfaces
	 * 
	 * @param binding
	 * @param name
	 * @return
	 */
	static boolean isJ2SInheritedFieldName(ITypeBinding binding, String name) {

		if (binding == null)
			return false;

		if ("serialVersionUID".equals(name)) {
			// ignore
			return false;
		}

		IVariableBinding[] declaredFields;
		ITypeBinding superclass = binding.getSuperclass();
		if (superclass == null) {
			// interface
			declaredFields = binding.getDeclaredFields();
		} else {
			declaredFields = superclass.getDeclaredFields();
		}
		for (int i = 0; i < declaredFields.length; i++) {
			if (name.equals(declaredFields[i].getName())) {
				return true;
			}
		}
		if (superclass != null && isJ2SInheritedFieldName(superclass, name)) {
			return true;
		}
		ITypeBinding[] interfaces = binding.getInterfaces();
		if (interfaces != null) {
			for (int i = 0; i < interfaces.length; i++) {
				if (isJ2SInheritedFieldName(interfaces[i], name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This is the principle method for returning a fully qualified method name. x
	 * Determine the qualified parameter suffix for method names, including
	 * constructors. Now returns name$ for all unparameterized methods not
	 * explicitly excluded.
	 * 
	 * @param javaClassNameForInvocation
	 * @param mBinding
	 * @param genericTypes               only in the case of method declarations,
	 *                                   where we are trying to match generic
	 *                                   methods
	 * @param addCallingOption$O
	 * @param isLiteralOrLambda_C
	 * 
	 * @return a fully j2s-qualified method name
	 */
	private String getFinalMethodNameWith$Params(String j2sName, String javaClassNameForInvocation,
			IMethodBinding mBinding, String[] genericTypes, boolean addCallingOption$O, int specialType) {
		// The problem is that System.out and System.err are PrintStreams, and
		// we
		// do not intend to change those. So in the case that we just wrote
		// "System....", we use that instead and do not qualify the name
		// Note: binding can be null if we have errors in the Java and we are
		// compiling
		//
		// mBinding should never be null, but...
		if (mBinding == null)
			return j2sName;
		String methodName = mBinding.getName();
		if (j2sName == null)
			j2sName = methodName;
		ITypeBinding declaringClass = mBinding.getDeclaringClass();
		String javaClassName = getJavaClassNameQualified(declaringClass);
		if (NameMapper.isMethodNonqualified(javaClassName, methodName))
			return j2sName;

		// BH: Note in the next statement, that Map.put$K$V is translated to actual
		// values
		// if .getMethodDeclaration() is not used.
		// Without that, it uses the bound parameters such as
		// String, Object instead of the declared ones, such as $TK$TV

		// !isMethodInvoaction and isLiteralOrLambda_C means LambdaC
		ITypeBinding[] paramTypes = (specialType == METHOD_LAMBDA_C ? mBinding.getParameterTypes()
				: mBinding.getMethodDeclaration().getParameterTypes());

		int nParams = paramTypes.length;
		if (genericTypes != null && genericTypes.length != nParams)
			return null;

		// xxx() adds $ to become xxx$() iff it is NOT
		// - (private and not static)
		// - toString or hashCode
		// - already qualified with $
		// skipping C$.
		if (nParams == 0)
			return ensureMethod$Name(j2sName, mBinding, null);

		// functional interface methods are qualified only by "$", not their parameters.
		// This is not ideal.

		IMethodBinding fm = (specialType != METHOD_NOTSPECIAL || javaClassName.equals("java.lang.reflect.Proxy") ? null
				: declaringClass.getFunctionalInterfaceMethod());
		if (fm != null && methodName.equals(fm.getName()))
			return ensureMethod$Name(j2sName, mBinding, null);

		String s = getParamsAsString(nParams, genericTypes, paramTypes, false);

		if (addCallingOption$O && s.indexOf("$T") >= 0 && isJava(javaClassName) && !isJava(class_fullName)) {
			// If the method being called is a Java class and the calling class is NOT a
			// Java class,
			// then also add the $O version.
			String generic = getParamsAsString(nParams, genericTypes, paramTypes, true);
			if (generic != null) {
				trailingBuffer.addType("o");
				return j2sName + s + " || $o$." + j2sName.substring(j2sName.lastIndexOf(".") + 1) + generic;
			}
			// this does not work for two reasons:
			// 1) sometimes the qualifier, so for t.foo$TA(o), "t." is outside
			// the scope of these parentheses.
			// 2) When selecting functions like this, one needs to use apply,
			// so: ((a$ = expression).foo$TA || a$.foo$O).apply(a$, [o])
			//
			// thus, this determination must be made very early.

		}

		return j2sName + s;
	}

	/**
	 * LocalVariable is used to track final or effectively final variables and
	 * whether they have been used or not.
	 * 
	 * @author zhou renjian
	 *
	 *         2006-12-6
	 */
	private static class LocalVariable {

		/**
		 * Level of the block
		 */
		private int blockLevel;

		int getBlockLevel() {
			return blockLevel;
		}

		/**
		 * Final variable may be in a very deep anonymous class
		 */
		private String methodScope;

		/**
		 * Variable name that is defined in Java sources
		 */
		private String javaName;

		String getJavaName() {
			return javaName;
		}

		/**
		 * $name, if required
		 * 
		 */
		private String j2sName;

		private String key;

		LocalVariable(int blockLevel, String javaName, String methodScope, String j2sName) {
			this.blockLevel = blockLevel;
			this.methodScope = methodScope;
			this.javaName = javaName;
			this.j2sName = j2sName;
			this.key = blockLevel + "," + methodScope + "," + javaName + "," + j2sName;
		}

		/**
		 * Check for final-variable access, adding this to visitedVariables if need be.
		 * 
		 * @param name
		 * @param block
		 * @param visitedVars
		 * @return
		 */
		boolean isFinalAccess(String name, int block) {
			return (blockLevel <= block && javaName.equals(name));
		}

		/**
		 * create the {width:width, height:height} array for Clazz.new_()
		 * 
		 * @param scope
		 * @param buf
		 */
		void appendToBuffer(String scope, StringBuffer buf) {
			buf.append(j2sName);
			buf.append(": ");
			if (methodScope == null ? scope == null : methodScope.equals(scope)) {
				buf.append(j2sName);
			} else {
				buf.append("this.$finals$." + j2sName);
			}
		}

		public String toString() {
			return "[" + key + "]";
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + blockLevel;
			result = prime * result + ((methodScope == null) ? 0 : methodScope.hashCode());
			result = prime * result + ((javaName == null) ? 0 : javaName.hashCode());
			return result;
		}

		public boolean equals(Object obj) {
			return (this == obj || (obj instanceof LocalVariable) && ((LocalVariable) obj).key.equals(key));
		}

	}

	public static class NameMapper {

		/**
		 * defined already in j2sClazz.js
		 */
		static String[] j2sClazzPackages = { "java.lang", "java.lang.reflect", "java.io", "java.util" };

		static boolean isJ2sClazzPackage(String packageName) {
			for (int i = 0; i < j2sClazzPackages.length; i++)
				if (j2sClazzPackages[i].equals(packageName))
					return true;
			return false;
		}

		static final String primitiveTypeEquivalents = "Boolean,Byte,Character,Short,Integer,Long,Float,Double,Void,";
		static final String noConstructorNames = ",Boolean,Byte,Short,Integer,Long,Float,Double,";

		static String fixPackageName(String name) {
			// trying to avoid "double" or "doubleA" here. Not perfect, but who would
			// ever name a top-level class with a lower-case starting letter?
			// a name can be "" here as for LambdaExpressions
			return (name.length() > 0 && name.indexOf(".") < 0 && !Character.isLowerCase(name.charAt(0))
					? NULL_PACKAGE + "." + name
					: name);
		}

		static boolean isOneOf(String key, String values) {
			return (values.indexOf("," + key + ",") >= 0);
		}

		static String getPrimitiveTYPE(String name) {
			int pt = primitiveTypeEquivalents.indexOf(name.substring(1)) - 1;
			String type = primitiveTypeEquivalents.substring(pt);
			return type.substring(0, type.indexOf(","));
		}

		private final static String[] knownClasses = new String[] { "java.lang.Object", "java.lang.Class",
				"java.lang.String", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Long",
				"java.lang.Integer", "java.lang.Float", "java.lang.Double", "java.io.Serializable",
				"java.lang.Iterable", "java.lang.CharSequence", "java.lang.Cloneable", "java.lang.Comparable",
				"java.lang.Runnable", "java.lang.System", "java.lang.ClassLoader", "java.lang.Math",
				"java.lang.Number" };
		private final static Set<String> knownClassHash = new HashSet<String>();
		static {
			for (int i = knownClasses.length; --i >= 0;)
				knownClassHash.add(knownClasses[i]);
		}

		static boolean isClassKnown(String qualifiedName) {
			return knownClassHash.contains(qualifiedName);
		}

		private static Map<String, String> htClassReplacements;
		private static List<String> lstPackageReplacements;

		public static void setClassReplacements(String keyValues) {
			// j2s.class.replacements=org.apache.log4j.*:jalview.jslogger.;
			htClassReplacements = null;
			if (keyValues == null)
				return;
			htClassReplacements = new Hashtable<String, String>();
			lstPackageReplacements = new ArrayList<String>();
			String[] pairs = keyValues.split(";");
			for (int i = pairs.length; --i >= 0;) {
				pairs[i] = pairs[i].trim();
				if (pairs[i].length() == 0)
					continue;
				String[] kv = pairs[i].split("->");
				htClassReplacements.put(kv[0], kv[1]);
				if (kv[0].endsWith("."))
					lstPackageReplacements.add(kv[0]);
				log("class replacement " + kv[0] + " --> " + kv[1]);
			}
		}

		static String checkClassReplacement(String className) {
			if (htClassReplacements != null) {
				String rep = htClassReplacements.get(className);
				if (rep == null && lstPackageReplacements != null) {
					for (int i = lstPackageReplacements.size(); --i >= 0;) {
						rep = lstPackageReplacements.get(i);
						if (className.startsWith(rep)) {
							rep = htClassReplacements.get(rep) + className.substring(rep.length());
							break;
						}
						if (i == 0)
							rep = null;
					}

				}
				if (rep != null) {
					log(className + " -> " + rep);
					return rep;
				}
			}
			return className;
		}

		/**
		 * classes and packages that do not accept $ in their method names
		 * 
		 */
		private final static String defaultNonQualified
		// Math and Date both are minor extensions
		// of JavaScript, so they are not qualified
				= //"java.lang.Math;" +
						// MAYBE NOT! + "java.util.Date;"
						// swingjs.api.js and javajs.api.js contain
						// interfaces to JavaScript methods and so
						// are not parameterized.
						
						"*.api.js;"
		// netscape.JSObject interface includes 8 methods
		// that do not need to be parameterized.
		// + "netscape.*;"
		;

		private static String[] nonQualifiedPackages;

		/**
		 * .j2s option j2s.compiler.nonqualified.packages/classes
		 * 
		 * @param names semicolon-separated list. For example,
		 *              org.jmol.api.js;jspecview.api.js
		 */
		public static void setNonQualifiedNamePackages(String names) {
			names = defaultNonQualified + (names == null ? "" : names);
			nonQualifiedPackages = names.replace(";;", ";").trim().split(";");
			for (int i = nonQualifiedPackages.length; --i >= 0;) {
				String s = nonQualifiedPackages[i];
				if (s.length() == 0)
					continue;
				if (s.startsWith("*."))
					s = s.substring(1);
				if (s.endsWith("."))
					s = s.substring(0, s.length() - 1);
				nonQualifiedPackages[i] = (s.endsWith("*") ? s.substring(0, s.length() - 1) : s + ".").trim();
			}
		}

		/**
		 * Check to see if this class is in a package for which we exclude parameter
		 * qualification
		 * 
		 * @param className
		 * @return
		 */
		private static boolean isPackageOrClassNonqualified(String className) {
			className += ".";
			for (int i = nonQualifiedPackages.length; --i >= 0;) {
				String s = nonQualifiedPackages[i];
				if (s.length() > 0 && s.startsWith(".") ? className.contains(s) : className.startsWith(s)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * @param methodName not used but could be
		 */
		static boolean isMethodNonqualified(String className, String methodName) {
			if (className.equals("java.lang.Math")) {
				switch (methodName) {
				case "ulp":
				case "nextDown":
				case "nextUp":
				case "nextAfter":
				case "getExponent":
					return false;
				default:
					return true;
				}
			}
			return (isPackageOrClassNonqualified(className));
		}

		/**
		 * Check for special direct Clazz method calls, avoiding loading the entire
		 * class.
		 * 
		 * @param javaClassName
		 * @return
		 */
		static String getJ2SFinalMapClazzMethod(String javaClassName, String methodName) {
			switch (methodName) {
			case "forName":
				return (javaClassName.equals("java.lang.Class") ? "Clazz.forName" : null);
			case "newInstance":
				return (javaClassName.equals("java.lang.reflect.Array") ? "Clazz.array" : null);
			default:
				return null;
			}
		}

		/**
		 * Prepend a $ if the name is a JavaScript keyword such as "var" or "for".
		 * 
		 * For example,
		 * 
		 * int var = 3
		 * 
		 * becomes
		 * 
		 * int $var = 3
		 * 
		 * @param identifier
		 * @param addName
		 * @param binding    when null, this is for a field; when not null, this is for
		 *                   a method
		 * @return
		 */
		static String getJavaScriptCollisionIdentifier(String identifier, boolean addName) {
			String s = getJavaScriptKeywordViolationChar(identifier);
			return (!addName ? s : s.length() == 0 ? identifier : s + identifier);
		}

		/*
		 * IE passes the following: public,private,private,static,package,
		 * implements,prototype,false,throws,label
		 * 
		 * Firefox passes the following: public,prototype,false,label
		 * 
		 * The following does not contain all the reserved keywords:
		 * http://developer.mozilla.org/en/docs/Core_JavaScript_1.5_Reference:
		 * Reserved_Words
		 * 
		 * abstract, boolean, break, byte, case, catch, char, class, const, continue,
		 * debugger, default, delete, do, double, else, enum, export, extends, false,
		 * final, finally, float, for, function, goto, if, implements, import, in,
		 * instanceof, int, interface, long, native, new, null, package, private,
		 * private, public, return, short, static, super, switch, synchronized, this,
		 * throw, throws, transient, true, try, typeof, var, void, volatile, while,
		 * with,
		 * 
		 * 
		 * 
		 */
		private static String[] keywords = new String[] { "class", /* "java", "javax", "sun", */"for", "while", "do",
				"in", "return", "function", "var", "class", "public", "private", "new", "delete", "static", "package",
				"import", "extends", "implements", "instanceof", "typeof", "void", "if", "this", "super", "prototype",
				"else", "break", "true", "false", "try", "catch", "throw", "throws", "continue", "switch", "default",
				"case", "export", "import", "const", /* "label", */"with",
				// BH and a few of our own, based on checking developer console:
				"c$", "apply", "arguments", "bind", "call", "caller", "watch", "unwatch", "valueOf", "isPrototypeOf",
				"isGenerator", "prototype" };

		private static String getJavaScriptKeywordViolationChar(String identifier) {
			if (!Character.isUpperCase(identifier.charAt(0))) {
				String s = identifier.intern();
				for (int i = 0; i < keywords.length; i++) {
					if (keywords[i] == s) {
						return "$";
					}
				}
			}
			return "";
		}

		/**
		 * Prefix field name with as many $ as necessary to make it inheritance-unique
		 * 
		 * @param binding
		 * @param name
		 * @return
		 */
		static StringBuffer getJ2S$$InheritedFieldName(ITypeBinding binding, String name, StringBuffer buf) {
			if (binding != null) {
				ITypeBinding superclass = binding.getSuperclass();
				if (superclass != null) {
					IVariableBinding[] declaredFields = superclass.getDeclaredFields();
					for (int i = 0; i < declaredFields.length; i++) {
						if (name.equals(declaredFields[i].getName())) {
							buf.append("$");
							break;
						}
					}
					return getJ2S$$InheritedFieldName(superclass, name, buf);
				}
			}
			return buf.append(name);
		}

		/**
		 * Initializer for the $$$... buffer that is prepended to all field names that
		 * have conflicts with methods. Currently just toString();
		 * 
		 * @param fieldName
		 * @param buf
		 * @return
		 */
		static StringBuffer newFieldNameBuf(String fieldName, StringBuffer buf) {
			if (buf == null)
				buf = new StringBuffer();
			if (fieldNameCoversMethod(fieldName))
				buf.append("$");
			return buf;
		}

		/**
		 * toString is the only method not qualified. It will be used directly by
		 * JavaScript in s1 + s2. So in this case, we need a field by that name to be
		 * prepended with "$".
		 * 
		 * @param fieldName
		 * @return
		 */
		static boolean fieldNameCoversMethod(String fieldName) {
			return fieldName.equals("toString");
		}

	}

	static class ClassAnnotation {

		protected ASTNode node;
		protected Annotation annotation;
		private String qName;

		protected ClassAnnotation(String qName, Annotation annotation, ASTNode node) {
			this.qName = qName;
			this.annotation = annotation;
			this.node = node;
		}

		@SuppressWarnings("unchecked")
		public static void addClassAnnotations(int accessType, List<ClassAnnotation> class_annotations,
				List<EnumConstantDeclaration> enums, List<FieldDeclaration> fields, List<IMethodBinding> methods,
				List<AbstractTypeDeclaration> innerClasses, TrailingBuffer trailingBuffer) {
			boolean isPackage = (fields == null && enums == null);
			int pt = 0, ptBuf = 0;
			ASTNode lastNode = null;
			List<?> fragments = null;
			String propOrder = null;
			String lastClassName = null;
			for (int i = 0; i < class_annotations.size(); i++) {
				ClassAnnotation a = class_annotations.get(i);
				String str = a.annotation.toString();
				if (a.annotation instanceof NormalAnnotation) {
					// @XmlElement(name="test",type=Integer.class)
					// remove commas, add quotes
					NormalAnnotation na = (NormalAnnotation) a.annotation;
					IMemberValuePairBinding[] pairs = na.resolveAnnotationBinding().getDeclaredMemberValuePairs();
					str = str.substring(0, str.indexOf("(") + 1);
					for (int j = 0; j < pairs.length; j++)
						str += annotationNameValue(pairs[j].getName(), pairs[j].getValue());
					str += ")";
				} else if (a.annotation instanceof SingleMemberAnnotation) {
					// add quotes
					List<ASTNode> expressions = null;
					Expression e = ((SingleMemberAnnotation) a.annotation).getValue();
					if (e instanceof TypeLiteral) {
						expressions = new ArrayList<ASTNode>();
						expressions.add(e);
					} else if (e instanceof ArrayInitializer) {
						expressions = ((ArrayInitializer) e).expressions();
					}
					if (expressions != null) {
						str = str.substring(0, str.indexOf("(") + 1);
						int n = expressions.size();
						String sep = (n > 1 ? "{" : "");
						for (int j = 0; j < n; j++) {
							str += sep + annotationNameValue(null, expressions.get(j));
							sep = ",";
						}
						str += (n > 1 ? "})" : ")");
					}
				}
				if (a.node == lastNode) {
					trailingBuffer.append(",");
				} else {
					lastNode = a.node;
					String varName = null;
					ITypeBinding type = null;
					// time to pick up the fragments
					addTrailingFragments(fragments, trailingBuffer, ptBuf);
					fragments = null;
					if (a.node instanceof EnumDeclaration) {
						type = ((EnumDeclaration) a.node).resolveBinding();
						propOrder = "XX";
					} else if (a.node instanceof TypeDeclaration) {
						type = ((TypeDeclaration) a.node).resolveBinding();
					} else if (a.node instanceof FieldDeclaration) {
						FieldDeclaration field = (FieldDeclaration) a.node;
						fields.remove(field);
						fragments = field.fragments();
						VariableDeclarationFragment identifier = (VariableDeclarationFragment) fragments.get(0);
						IVariableBinding var = identifier.resolveBinding();
						varName = var.getName();
						type = var.getType();
					} else if (a.node instanceof MethodDeclaration) {
						MethodDeclaration method = (MethodDeclaration) a.node;
						IMethodBinding var = method.resolveBinding();
						if (methods.contains(var))
							methods.remove(var);
						var = getJAXBGetMethod(var, methods, false);
						if (var == null)
							continue;
						varName = "M:" + var.getName();
						type = var.getReturnType();
					} else if (a.node instanceof EnumConstantDeclaration) {
						EnumConstantDeclaration con = (EnumConstantDeclaration) a.node;
						enums.remove(con);
						IVariableBinding var = con.resolveVariable();
						varName = var.getName();
						type = var.getType();
					}
					String className = (type == null ? null
							: stripJavaLang(
									// NameMapper.checkClassReplacement(
									// removeBracketsAndFixNullPackageName(
									NameMapper.fixPackageName(getJavaClassNameQualified(type))
							// ))
							));
					if (className != null && className.equals(lastClassName)) {
						className = ".";
					} else {
						lastClassName = className;
					}
					trailingBuffer.append(pt++ == 0 ? "C$.__ANN__ = [[[" : "]],\n  [[");
					trailingBuffer.append((varName == null ? null : "'" + varName + "'"));
					ptBuf = trailingBuffer.buf.length();
					trailingBuffer.append(",'" + className + "'],[");
				}
				trailingBuffer.append("'" + str + "'");
				if (propOrder == null && str.indexOf("propOrder=") >= 0)
					propOrder = str;
			}
			if (pt > 0) {
				addTrailingFragments(fragments, trailingBuffer, ptBuf);
				if (!isPackage)
					addImplicitJAXBFieldsAndMethods(accessType, trailingBuffer, enums, fields, methods, innerClasses,
							propOrder);
				trailingBuffer.append("]]];\n");
			}
		}

		private static String annotationNameValue(String name, Object value) {
			String str = (name == null ? "" : name + "=");
			if (value instanceof TypeLiteral) {
				str += "\"" + ((TypeLiteral) value).getType().resolveBinding().getQualifiedName() + ".class\"";
			} else if (value instanceof IVariableBinding) {
				str += "\"" + ((IVariableBinding) value).getType().getQualifiedName() + "."
						+ ((IVariableBinding) value).getName() + "\"";
			} else if (value instanceof Expression) {
				str += "\"" + value + "\"";
			} else if (value instanceof ITypeBinding) {
				str += "\"" + ((ITypeBinding) value).getQualifiedName() + ".class\"";
			} else if (value instanceof Object[]) {
				// propOrder
				Object[] o = (Object[]) value;
				str += "{";
				for (int i = 0; i < o.length; i++)
					str += annotationNameValue(null, o[i]) + " ";
				str += "}";
			} else {
				str += "\"" + value.toString() + "\"";
			}
			str += " ";
			return str;
		}

		private static IMethodBinding getJAXBGetMethod(IMethodBinding var, List<IMethodBinding> methods,
				boolean returnVar2) {
			String varName = var.getName();
			// check for matching get/is and set
			if (varName.startsWith("create")) {
				return var;
			}
			if (varName.startsWith("set")) {
				return getMethodBinding(methods, "g" + varName.substring(1));
			}
			IMethodBinding var2 = getMethodBinding(methods,
					"set" + varName.substring(varName.startsWith("get") ? 3 : 2));
			return (var2 == null ? null : returnVar2 ? var2 : var);
		}

		/**
		 * Add all implicit fields. Note that we still cannot marshal a class that has
		 * NO JAXB annotations at all. We have to have some.
		 * 
		 * @param accessType
		 * @param trailingBuffer
		 * @param enums
		 * @param fields
		 * @param methods
		 * @param innerClasses
		 * @param propOrder
		 */
		private static void addImplicitJAXBFieldsAndMethods(int accessType, TrailingBuffer trailingBuffer,
				List<EnumConstantDeclaration> enums, List<FieldDeclaration> fields, List<IMethodBinding> methods,
				List<AbstractTypeDeclaration> innerClasses, String propOrder) {
			for (int i = 0; i < innerClasses.size(); i++) {
				ITypeBinding type = innerClasses.get(i).resolveBinding();
				if (isStatic(type)) {
					addAnnotation(null, type, "!XmlInner", trailingBuffer);
				}
			}

			switch (accessType) {
			case JAXB_TYPE_NONE:
				return;
			case JAXB_TYPE_ENUM:
				for (int j = 0; j < enums.size(); j++) {
					EnumConstantDeclaration con = enums.get(j);
					IVariableBinding var = con.resolveVariable();
					String varName = var.getName();
					ITypeBinding type = var.getType();
					addAnnotation(varName, type, "@XmlEnumValue", trailingBuffer);
				}
				return;
			default:
				boolean isUnspecified = (accessType == JAXB_TYPE_UNSPECIFIED);
				boolean publicOnly = (accessType == JAXB_TYPE_PUBLIC_MEMBER);
				if (accessType != JAXB_TYPE_PROPERTY) {
					for (int j = 0; j < fields.size(); j++) {
						FieldDeclaration field = fields.get(j);
						boolean isPublic = Modifier.isPublic(field.getModifiers());
						if (publicOnly && !isPublic)
							continue;
						List fragments = field.fragments();
						for (int i = 0; i < fragments.size(); i++) {
							VariableDeclarationFragment identifier = (VariableDeclarationFragment) fragments.get(i);
							IVariableBinding var = identifier.resolveBinding();
							String varName = var.getName();
							// If propOrder is defined, then we are only allowed to
							// add implicit fields that are in that propOrder
							if (propOrder != null && propOrder.indexOf("\"" + varName + "\"") < 0)
								continue;
							ITypeBinding type = var.getType();
							addAnnotation(varName, type, "@XmlElement", trailingBuffer);
							if (isUnspecified)
								addAnnotation(varName, type, "!XmlPublic(" + isPublic + ")", trailingBuffer);
						}
					}
				}
				if (accessType != JAXB_TYPE_FIELD) {
					for (int i = 0; i < methods.size(); i++) {
						IMethodBinding var = methods.get(i);
						IMethodBinding var2 = getJAXBGetMethod(var, methods, true);
						if (var2 == null)
							continue;
						boolean isPublic = (Modifier.isPublic(var.getModifiers())
								|| Modifier.isPublic(var2.getModifiers()));
						if (publicOnly && !isPublic)
							continue;
						String varName = var.getName();
						if (varName.startsWith("set"))
							varName = (var = var2).getName();
						ITypeBinding type = var.getReturnType();
						addAnnotation("M:" + varName, type, "@XmlElement", trailingBuffer);
						if (isUnspecified)
							addAnnotation("M:" + varName, type, "!XmlPublic(" + isPublic + ")", trailingBuffer);
					}
				}
				break;
			}
		}

		private static void addAnnotation(String varName, ITypeBinding type, String str,
				TrailingBuffer trailingBuffer) {
			String className = (stripJavaLang(NameMapper.fixPackageName(getJavaClassNameQualified(type))));
			trailingBuffer.append("]],\n  [[");
			trailingBuffer.append("'" + varName + "'");
			trailingBuffer.append(",'" + className + "'],['" + str + "'");
		}

		private static IMethodBinding getMethodBinding(List<IMethodBinding> methods, String name) {
			for (int i = methods.size(); --i >= 0;) {
				if (name.equals(methods.get(i).getName())) {
					return methods.remove(i);
				}
			}
			return null;
		}

		private static void addTrailingFragments(List<?> fragments, TrailingBuffer trailingBuffer, int ptBuf) {
			if (fragments == null || fragments.size() == 0)
				return;
			String line = trailingBuffer.buf.substring(ptBuf);
			for (int f = 1; f < fragments.size(); f++) {
				VariableDeclarationFragment identifier = (VariableDeclarationFragment) fragments.get(f);
				IVariableBinding var = identifier.resolveBinding();
				trailingBuffer.append("]],\n  [['" + var.getName() + "'");
				trailingBuffer.append(line);
			}
		}

	}

	static class NativeDoc {

		/**
		 * prepare a list that alternates [javadoc element javadoc element ... ]
		 * associating an element with its javadoc.
		 * 
		 * @author RM
		 *
		 */
		private static class BlockVisitor extends ASTVisitor {

			private int ptrDoc0;
			private List<ASTNode> list;
			private int listPtr;

			BlockVisitor(List<ASTNode> list) {
				this.list = list;
				ptrDoc0 = list.get(listPtr = 0).getStartPosition();
			}

			/**
			 * Just collect blocks after j2s Javadocs. Throws an IndexOfBoundsException when
			 * done with scanning.
			 * 
			 * Note that this no longer requires that the node be a block, because we are
			 * processing these BEFORE the node is visited.
			 * 
			 */

			public void preVisit(ASTNode node) throws IndexOutOfBoundsException {
				checkNode(node, false);
			}

			public void postVisit(ASTNode node) {
				checkNode(node, true);
			}

			private void checkNode(ASTNode node, boolean isPost) {
				int nodept = node.getStartPosition() + (isPost ? node.getLength() : 0);
				boolean checkParens = (!isPost && node instanceof ParenthesizedExpression);
				while (nodept >= ptrDoc0 || checkParens && nodept == ptrDoc0 - 1)
					addNode(node, isPost);
			}

			private void addNode(ASTNode node, boolean isPost) {
				if (isPost)
					list.add(++listPtr, null);
				list.add(++listPtr, node);
				ptrDoc0 = list.get(++listPtr).getStartPosition();
			}

		}

		/**
		 * 
		 * Check for j2sIgnore, j2sDebug, j2sNative
		 * 
		 * @param javadoc
		 * @param isBlock
		 * @return true if code was added
		 */
		static boolean addJ2sJavadocs(StringBuffer buffer, List<Javadoc> list, boolean isBlock) {
			boolean didAdd = false;
			int n = list.size();
			for (int i = 0; i < n; i++) {
				Javadoc javadoc = list.get(i);
				List<?> tags = javadoc.tags();
				if (tags != null && tags.size() > 0
						&& (isBlock && getTag(tags, "@j2sIgnore") != null
								&& addJ2SSourceForTag(buffer, null, i == 0, i == n - 1, true)
								|| isBlock && global_j2sFlag_isDebugging
										&& addJ2SSourceForTag(buffer, getTag(tags, "@j2sDebug"), i == 0, i == n - 1,
												false)
								|| addJ2SSourceForTag(buffer, getTag(tags, "@j2sNative"), isBlock && i == 0,
										isBlock && i == n - 1, false))) {
					didAdd = true;
				}
			}
			return didAdd;
		}

		private static TagElement getTag(List<?> tags, String j2sKey) {
			Iterator<?> iter = tags.iterator();
			while (iter.hasNext()) {
				TagElement tagEl = (TagElement) iter.next();
				if (j2sKey.equals(tagEl.getTagName())) {
					return tagEl;
				}
			}
			return null;
		}

		/**
		 * 
		 * @param buffer
		 * @param tag
		 * @param addPrefix
		 * @param addPostfix
		 * @param isIgnore
		 * @return true to indicate we have written, so this block can be skipped
		 */
		private static boolean addJ2SSourceForTag(StringBuffer buffer, TagElement tag, boolean addPrefix,
				boolean addPostfix, boolean isIgnore) {
			if (isIgnore) {
				buffer.append("\r\n{}\r\n");
				return true;
			}
			if (tag == null)
				return false;
			StringBuffer buf = new StringBuffer();
			List<?> fragments = tag.fragments();
			for (Iterator<?> iterator = fragments.iterator(); iterator.hasNext();) {
				TextElement commentEl = (TextElement) iterator.next();
				String text = commentEl.getText().trim();
				buf.append(text);
				if (text.length() != 0) {
					buf.append(text.endsWith(";") || text.indexOf("//") >= 0 ? "\r\n" : " ");
					// BH note that all line terminators are removed,
					// as this causes problems after source cleaning, which may result
					// in code such as:
					//
					// return
					// x
					//
					// but this still does not fix the problem that we can have
					// x = "
					// "
					// after source cleaning
				}
			}
			String code = buf.toString();
			// /-* comment *-/ becomes /* comment */ and <@> becomes @
			if (code.length() > 0)
				code = Pattern.compile("\\/-\\*(.*)\\*-\\/", Pattern.MULTILINE | Pattern.DOTALL).matcher(code)
						.replaceAll("/*$1*/").replaceAll("<@>", "@").trim();
			// use of inline comment
			// that is, no {...} after it, in the middle of an expression
			// for example: int x = /**@j2sNative 32||*/15;
			// has limitations in that you cannot replace a string by "" or an object by
			// null.
			// end with || to replace Java value; must not be 0, "", or null
			// end with 0 && to replace a number with 0.
			// end with null && to replace anything with null
			// end with 1?xxx: to replace anything with xxx
			// /** @j2sNatve ! */true
			// (/** @j2sNative 1?x: */y)
			// /** @j2sNative true || */()

			boolean isInline = code.endsWith("|") || code.endsWith("&") || code.endsWith(":") || code.endsWith("!");
			buffer.append(isInline ? "" : addPrefix ? "{\r\n" : "\r\n");
			buffer.append(code);
			buffer.append(isInline ? "" : addPostfix ? "\r\n}\r\n" : "\r\n");
			return true;
		}

	}

	/**
	 * for debugging -- to System.err.println
	 * 
	 * @param msg
	 */
	public static void dumpStack(String msg) {
		try {
			throw new NullPointerException("Why am I here? " + msg);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

////////////////// JAVA 8 LAMBDA ADDITIONS /////////////////	

	/**
	 * 
	 * Function<Integer, int[]> iaCreator = int[]::new;
	 * 
	 */
	public boolean visit(CreationReference node) {
//		// lambda_C
		ITypeBinding binding = node.resolveTypeBinding();
		processLocalInstance(node, null, binding, null, null, LAMBDA_CREATION, false);
		return false;
	}

	/**
	 * System.out::println;
	 * 
	 * new Test()::test2;
	 * 
	 */
	public boolean visit(ExpressionMethodReference node) {
		// lambda_M
		// oddly enough, if we just use visit(MethodReference) it goes
		// somewhere else.
		return addLambdaMethodReference(node, node.getExpression());
	}

	/**
	 * 
	 * super::test2;
	 * 
	 */
	public boolean visit(SuperMethodReference node) {
		return addLambdaMethodReference(node, null);
	}

	public boolean visit(TypeMethodReference node) {
		return addLambdaMethodReference(node, null);
	}

	private boolean addLambdaMethodReference(MethodReference node, Expression exp) {
		ITypeBinding binding = node.resolveTypeBinding();
		IMethodBinding mBinding = node.resolveMethodBinding();
		ITypeBinding declaringClass = mBinding.getDeclaringClass();
		char lambdaType = getLambdaType(binding);
		switch (lambdaType) {
		case 'F':
		case 'C':
		case 'P':
		case 'S':
			if (!addLambda$class$Method(node, binding, exp, declaringClass, true)) {
				buffer.append("Clazz.newLambda(");
				appendFinalMethodQualifier(exp, declaringClass, null, FINAL_ESCAPECACHE | FINAL_LAMBDA);
				buffer.append(",");
				buffer.append(getFinalMethodNameOrArrayForDeclaration(mBinding, false, METHOD_FULLY_QUALIFIED));
				buffer.append(",'" + lambdaType + "')");
			}
			break;
		default:
			addLambda$class$Method(node, binding, exp, declaringClass, false);
			break;
		}
		return false;
	}

	/**
	 * Generate code not using Clazz.newLambda. If there are no finals and no
	 * external method calls, then delete this and rewrite it using Clazz.newLambda.
	 * 
	 * We do not allow Clazz.newLambda for assignments such as
	 * 
	 * 
	 * 
	 * @param node
	 * @param binding
	 * @param exp
	 * @param declaringClassJavaName
	 * @param checkFinals
	 * @return true if we are done and cannot use Clazz.newLamba for this call
	 */
	private boolean addLambda$class$Method(MethodReference node, ITypeBinding binding, Expression exp,
			ITypeBinding declaringClass, boolean checkFinals) {
		
		
		allowClazzNewLambda = (ALLOW_NEW_LAMBDA && getLastCharInBuffer() != '=');
		int pt = buffer.length();
		buffer.append("(function($class$){");
		String anonName = processLocalInstance(node, null, binding, null, null, LAMBDA_METHOD, false);
		buffer.append("})(");
		appendFinalMethodQualifier(exp, declaringClass, null, FINAL_ESCAPECACHE | FINAL_LAMBDA);
		buffer.append(")");
		if (checkFinals && allowClazzNewLambda)
			buffer.setLength(pt);
		if (anonName != null && ALLOW_LAMBDA_OBJECT_REUSE)
			addLambdaReuse(pt, anonName);		
		return !allowClazzNewLambda;

	}

	/**
	 * s -> b(s)
	 * 
	 * Only Function, Consumer, Predicate, and Supplier are covered by
	 * Clazz.newLambda.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public boolean visit(LambdaExpression node) {
		// LambdaExpression:
		// Identifier -> Body
		// ( [ Identifier { , Identifier } ] ) -> Body
		// ( [ FormalParameter { , FormalParameter } ] ) -> Body
		// ==>
		// new runnable() { public xxx singleMethod() { Body })

		// lambda_E
		ITypeBinding binding = node.resolveTypeBinding();
		char lambdaType = getLambdaType(binding);
		switch (lambdaType) {
		case 'F':
		case 'C':
		case 'P':
		case 'S':
			// lambda parameters are NOT final and do not cover
			// variables in the method. Lambda functions are at the same
			// block level as their caller with all their same final calls

			if (!addLambda$class$Expr(node, binding, true)) {
				List<ASTNode> params = node.parameters();
				buffer.append("Clazz.newLambda(function(");
				visitList(params, ",");
				buffer.append("){");
				inNewLambdaExpression = true;
				addLambdaBody(node.getBody());
				inNewLambdaExpression = false;
				buffer.append("},0,'" + lambdaType + "'");
				buffer.append(")");
			}
			break;
		default:
			addLambda$class$Expr(node, binding, false);
			break;
		}
		return false;
	}

	private boolean addLambda$class$Expr(LambdaExpression node, ITypeBinding binding, boolean checkFinals) {
		allowClazzNewLambda = (ALLOW_NEW_LAMBDA && getLastCharInBuffer() != '=');
		int pt = buffer.length();
		String anonName = processLocalInstance(node, null, binding, null, null, LAMBDA_EXPRESSION, false);
		if (checkFinals && allowClazzNewLambda)
			buffer.setLength(pt);
		if (anonName != null && ALLOW_LAMBDA_OBJECT_REUSE)
			addLambdaReuse(pt, anonName);		
		return !allowClazzNewLambda;
	}

	/**
	 * allow reuse of Lambda method and expression objects when they involve no finals
	 *  
	 * @param pt
	 * @param anonName
	 */
	private void addLambdaReuse(int pt, String anonName) {
		String tmp = buffer.substring(pt);
		buffer.setLength(pt);
		anonName = getFinalJ2SClassName(anonName, FINAL_P);
		buffer.append("(" + anonName + "$||(" + anonName + "$=(")
			.append(tmp).append(")))");
	}

	private char getLambdaType(ITypeBinding binding) {
		String name = removeBracketsAndFixNullPackageName(getJavaClassNameQualified(binding));
		if (!name.startsWith("java.util.function.") || name.indexOf("To") >= 0)
			return '?';
		// TODO: covers Boolean...Int..., Long..., Double... but only roughly
		char c = (name.endsWith("Function") ? 'F'
				: name.endsWith("Consumer") ? 'C'
						: name.endsWith("Predicate") ? 'P' : name.endsWith("Supplier") ? 'S' : '-');
		return c;
	}

	private void addLambdaBody(ASTNode body) {
		
		
		if (body instanceof Block) {
			body.accept(this);
		} else {
			// there may be no return, but we still want to do this
			buffer.append("{ return ");
			if (body == null)
				return; // handled elsewhere
			buffer.append("(");
			body.accept(this);
			buffer.append(");}");
		}
	}

	private String getLambdaParamList(IMethodBinding mBinding, int arity0) {
		int n = mBinding.getParameterTypes().length;
		if (arity0 < 0)
			arity0 = n;
		// accept(t,u)......add(u)
		// buffer.append("/*"+arity0 + "," + n + "*/");
		return (n == 0 ? "" : " t,u,v,w,x,y,z".substring(Math.max(0, (arity0 - n) * 2) + 1, arity0 * 2));
	}

	/**
	 * This method is a fallback for the full-class implementation that does not use
	 * Clazz.newLambda. It is called by addClassOrInterface only.
	 * 
	 * @param lnode
	 * @param mBinding
	 */
	private void addLambdaMethod(ASTNode lnode, IMethodBinding mBinding) {
		if (lnode instanceof LambdaExpression) {
			buffer.append("/*lambda_E*/");
			LambdaExpression node = (LambdaExpression) lnode;
			mBinding = node.resolveMethodBinding();
			@SuppressWarnings("unchecked")
			List<ASTNode> params = node.parameters();
			processMethodDeclaration(mBinding, params, node.getBody(), false, LAMBDA_EXPRESSION);
			return;
		}
		if (lnode instanceof CreationReference) {
			buffer.append("/*lambda_C*/");
			processMethodDeclaration(mBinding, null, null, false, LAMBDA_CREATION);
			CreationReference node = (CreationReference) lnode;
			Type ctype = node.getType();
			ITypeBinding binding = ctype.resolveBinding();
			if (ctype instanceof ArrayType) {
				// int[]::new;
				addArrayConstructor(binding, null);
			} else {
				// MatchSink::new;
				addConstructor(binding, mBinding, null, mBinding.getParameterTypes().length);
			}
			buffer.append("});\r\n");
			return;
		}
		SimpleName identifier;
		Expression exp = null;
		IMethodBinding mBinding1;
		if (lnode instanceof TypeMethodReference) {
			// Collection<T>::add (see java.stream.Collectors)
			buffer.append("/*lambda_T*/");
			TypeMethodReference node = (TypeMethodReference) lnode;
			identifier = node.getName();
			mBinding1 = node.resolveMethodBinding();
		} else if (lnode instanceof SuperMethodReference) {
			// super::test3
			buffer.append("/*lambda_S*/");
			SuperMethodReference node = (SuperMethodReference) lnode;
			identifier = node.getName();
			mBinding1 = node.resolveMethodBinding();
		} else if (lnode instanceof ExpressionMethodReference) {
			// String.out::println
			buffer.append("/*lambda_M*/");
			ExpressionMethodReference node = (ExpressionMethodReference) lnode;
			identifier = node.getName();
			mBinding1 = node.resolveMethodBinding();
			exp = node.getExpression();
		} else {
			buffer.append("/*lambda_?*/");
			log("??? addLambdaMethod " + lnode.getClass().getName());
			return;
		}
		processMethodDeclaration(mBinding, null, null, false, LAMBDA_METHOD);
		addMethodInvocation(identifier, null, mBinding1, exp, mBinding.getParameterTypes().length);
		buffer.append("});\r\n");
	}

	/**
	 * SwingJS uses var p$<n> within an anonymous function wrapper ;(function()
	 * {.....})(); to isolate local variables within all classes of all types.
	 * Within this framework, C$ is the raw JavaScript object for the class, p$<n>
	 * is is an associative array containing private methods.
	 * 
	 * This hashtable is reset for each top-level class, indicating which private
	 * var to use for a private method -- p$1, p$2, p$3 etc. -- depending upon the
	 * class being referred to.
	 * 
	 */
	private static Map<String, String> classToPrivateVar = new Hashtable<String, String>();
	private static String privateVarString = "";
	private static int privateClassCount = 0;
	private static int privateVarCount = 0;

	/**
	 * p$1, p$2, etc.
	 * 
	 * Also used to compare two classes for equivalence, because I cannot figure out
	 * how to equate a class being invoked and a class being declared
	 * 
	 * @param binding
	 * @return
	 */
	private String getPrivateVar(IBinding binding, boolean isClassCompare) {
		String key = binding.getKey(), key0 = null, key1 = null;
		if (isClassCompare)
			key = "_" + key;
		String p$ = classToPrivateVar.get(key);
		if (p$ == null) {
			key0 = key;
			p$ = classToPrivateVar.get(key = (isClassCompare ? "_" : "") + getNormalizedKey(binding));
		}
		if (p$ == null && !isClassCompare && key.indexOf("[") >= 0) {
			key1 = key;
			p$ = classToPrivateVar.get(key = key.substring(0, key.indexOf("[") + 1) + "]");
		}
		if (p$ == null) {
			classToPrivateVar.put(key, p$ = "p$" + (isClassCompare ? ++privateClassCount : ++privateVarCount));
			classToPrivateVar.put(key0, p$);
			if (!isClassCompare) {
				if (key1 != null)
					classToPrivateVar.put(key1, p$);
				privateVarString += "," + p$ + "={}";
			}
		}
		return p$;
	}

	/**
	 * Test to see if an invocation is the same as a declaration using cached
	 * normalized keys
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean areEqual(IBinding a, IBinding b) {
		return getPrivateVar(a, true).equals(getPrivateVar(b, true));
	}

	private static String getNormalizedKey(IBinding b) {
		return getNormalizedSubKey(b.getKey());
		// e.g.: invocation vs. declaration
		// Ltest/Test_GenericIMV_AB<Ltest/Test_GenericIMV_AB;:TA;Ltest/Test_GenericIMV_AB;:TB;>;
		// becomes test/Test_GenericIMV_AB[TA;TB;];
		// Ljava/util/Hashtable<Ljava/util/Hashtable;:TK;Ljava/util/Hashtable;:TV;>.Enumerator<Ljava/util/Hashtable$Enumerator;:TT;>;
		// becomes java/util/Hashtable[TK;TV;].Enumerator[TT;];
	}

	private static String getNormalizedSubKey(String key) {
		int pt0 = key.lastIndexOf("<"), pt1;
		if (pt0 >= 0)
			return getNormalizedSubKey(key.substring(0, pt0) + "[" + getNormalizedSubKey(
					key.substring(pt0 + 1, (pt1 = key.indexOf(">", pt0))) + "]" + key.substring(pt1 + 1)));
		if (key.indexOf(":") < 0)
			return key;
		int left = -1;
		int pt;
		while ((pt = key.indexOf(":", ++left)) >= 0) {
			// Ltest/Test_GenericIMV_AB;:TA;Ltest/Test_GenericIMV_AB;:TB;
			// ^ ^
			// include ; but not :
			key = key.substring(0, left) + key.substring(pt + 1);
			// TA;Ltest/Test_GenericIMV_AB;:TB;
			// ^
			left = key.indexOf(";", left);
			if (left < 0)
				break;

			// Ljava/util/Hashtable<Ljava/util/Hashtable;:TK;Ljava/util/Hashtable;:TV;>.Enumerator<Ljava/util/Hashtable$Enumerator;:TT;>;
			// Ljava/util/Hashtable<Ljava/util/Hashtable;:TK;Ljava/util/Hashtable;:TV;>.Enumerator[TT;];
			// Ljava/util/Hashtable[TK;TV;].Enumerator[TT;];

		}
		return key;
	}

	private void resetPrivateVars() {
		privateVarCount = privateClassCount = 0;
		privateVarString = "";
		classToPrivateVar.clear();
	}

	///////////////// debugging //////////////////////////

	void debugDumpClass(ITypeBinding binding) {
		ITypeBinding[] lst = binding.getTypeParameters();

		// Check for <T,V> - these are for the generic class defs themselves
		for (int i = 0; i < lst.length; i++)
			log(binding.getKey() + "typeP " + i + lst[i].getName());

		// check for <String,Object> for the implemented classes
		lst = binding.getTypeArguments();
		for (int i = 0; i < lst.length; i++)
			log(binding.getKey() + "typeA " + i + lst[i].getName());

		IMethodBinding[] methods = binding.getDeclaredMethods();
		for (int i = methods.length; --i >= 0;) {
			IMethodBinding m = methods[i];
			log(getFinalMethodNameWith$Params(m.getName(), null, m, null, false, METHOD_NOTSPECIAL));
			ITypeBinding[] params = m.getParameterTypes();
			for (int j = 0; j < params.length; j++)
				log("\t" + params[j].getName());

		}
	}

	static void debugListAllOverrides(ITypeBinding binding) {
		IMethodBinding[] jmethods = binding.getDeclaredMethods();
		for (int j = jmethods.length; --j >= 0;) {
			IMethodBinding m = jmethods[j];
			ITypeBinding b = null;
			while ((b = (b == null ? m.getDeclaringClass() : b.getSuperclass())) != null) {
				IMethodBinding[] methods = b.getDeclaredMethods();
				for (int i = methods.length; --i >= 0;)
					if (m.overrides(methods[i]))
						log("!! " + m.getKey() + " overrides " + methods[i].getKey());
			}
		}
	}

}

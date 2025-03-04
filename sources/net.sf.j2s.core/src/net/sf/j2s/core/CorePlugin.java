package net.sf.j2s.core;

//import net.sf.j2s.core.hotspot.InnerHotspotServer;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 */
public class CorePlugin extends Plugin {

	//The shared instance.
	private static CorePlugin plugin;
	
	/**
	 * Note that Eclipse must be started with the -clean flag if it is to 
	 * register the bundle version properly. So we use VERSION here instead.
	 * 
	 */
	public static String VERSION = "3.2.4.07";
	// TODO/NOTE final static int FOO = (/**@j2sNative 5 || */3) stated but not recognized when used as its new value 
	// BH 2/3/2019 -- 3.2.4.07 fixes "final static Float = (float)" missing definition
	// BH 1/2/2019 -- 3.2.4.06 fixes try(resources) with more than one resource missing semicolon
	// BH 12/13/2018 -- 3.2.4.05 fixes problem with OuterClass.this.foo() not using .apply()
	// BH 11/10/2018 -- 3.2.4.04 additional support for JAXB
	// BH 11/4/2018 -- 3.2.4.02 broad JAXB support
	// BH 10/27/2018 -- 3.2.4.01 support for JAXB FIELD+propOrder and NONE types 
	// BH 9/28/2018 -- 3.2.4.00 adds minimal support for JAXB
	// BH 9/23/2018 -- 3.2.3.00 adds support for java.applet.Applet and java.awt.* controls without use of a2s.*
	// BH 9/16/2018 -- 3.2.2.06 removes "$" in JApplet public method alternative name
	// 3.2.2.04 2018.08.15 fixing Java->JavaScript "getFinal" code for class names.
	// 3.2.2.04 adds support for window-level applets, such as JmolApplet
	// 3.2.2.03 adds Java 8 function and stream
	// 3.2.2.02 adds $-qualified names for all methods
	// BH 8/20/2018 -- fix for return (short)++;
	// BH 8/19/2018 -- refactored to simplify $finals$
	// BH 8/12/2018 -- refactored to simplify naming issues
	// BH 8/6/2018  -- additional Java 8 fixes; enum $valueOf$S to valueOf$S
	// BH 8/1/2018  -- adds interface default methods as C$.$defaults$(C$)
	// BH 7/29/2018 -- java.util.stream.Collectors is returning java.util.Collectionthis.b$['java.util.Collection'].add
	// BH 7/25/2018 -- allows for direct private function calls in inner and anonymous classes using var p$, p$$, p$$$, etc
	// BH 7/22/2018 -- fixes improper use of charCodeAt() to replace charCode().$c() when not java.lang.String.charAt
	// BH 7/20/2018 -- removes qualifications for single-abstract method overrides
	// BH 7/19/2018 -- fixes Enum.Enum
	// BH 7/18/2018 -- addw Java 8 try without catch or finally
	// BH 7/16/2018 -- adds Java 8 :: operator
	// BH 7/15/2018 -- adds Java 8 lambda expressions
	// BH 7/14/2018 -- removes java2scriptbuilder; uses CompilationParticipant instead
	// BH 7/5/2018 -- fixes int | char
	// BH 7/3/2018 -- adds tryWithResource
	// BH 7/3/2018 -- adds effectively final -- FINAL keyword no longer necessary  
	// BH 6/27/2018 -- fix for a[Integer] not becoming a[Integer.valueOf]
	// BH 6/26/2018 -- method logging via j2s.log.methods.called and j2s.log.methods.declared
	// BH 6/24/2018 -- synchronized(a = new Object()) {...} ---> ...; only if an assignment or not a simple function call to Object.getTreeLock()
	// BH 6/23/2018 -- synchronized(a = new Object()) {...} ---> if(!(a = new Object()) {throw new NullPointerException()}else{...}
	// BH 6/21/2018 -- CharSequence.subSequence() should be defined both subSequence$I$I and subSequence
	// BH 6/20/2018 -- fixes for (int var : new int[] {3,4,5}) becoming for var var
	// BH 6/19/2018 -- adds .j2s j2s.class.replacements=org.apache.log4j.->jalview.javascript.log4j.;
	// BH 5/15/2018 -- fix for a[pt++] |= 3  incrementing pt twice and disregarding a[][] (see test/Test_Or.java)
	// BH 3/27/2018 -- fix for anonymous inner classes of inner classes not having this.this$0
	// BH 1/5/2018 --  @j2sKeep removed; refactored into one class
	// BH 12/31/2017 -- competely rewritten for no run-time ambiguities
	// BH 9/10/2017 -- adds full byte, short, and int distinction using class-level local fields $b$, $s$, and $i$, which are IntXArray[1]. (See ASTKeywordVisitor)
	// BH 9/7/2017 -- primitive casting for *=,/=,+=,-=,&=,|=,^=
	// BH 9/7/2017 -- primitive numeric casting -- (byte) was ignored so that (byte)  0xFF remained 0xFF.
	// BH 9/7/2017 -- fixed multiple issues with char and Character
	// BH 9/4/2017 -- java.awt, javax.swing, swingjs code added; additional fixes required
	// BH 8/30/2017 -- all i/o working, including printf and FileOutputStream
	// BH 8/19/2017 -- String must implement CharSequence, so all .length() -> .length$()
	// BH 8/19/2017 -- varargs logic fixed for missing argument
	// BH 8/18/2017 -- array instanceof, reflection, componentType fixes
	// BH 8/16/2017 -- JSE8-UnionType catch (Exception... | Exception...) {...}
	// BH 8/13/2017 -- includes native code calls in System.err
	// BH 7/31/2017 -- extensively reworked for fully qualified method names and no SAEM
	// 3.2.1.01 original SwingJS version through 2017 adds $-signatures for methods
	// 3.1.1 last Zhou Renjian unqualified name version
	
	
	/**
	 * The constructor.
	 */
	public CorePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("net.sf.j2s.core." + context.getBundle().getVersion() + "/" + VERSION + " started");
//		if (!InnerHotspotServer.isServerStarted()) {
//			InnerHotspotServer.getSingletonServer().startServer();
//		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
//		if (InnerHotspotServer.isServerStarted()) {
//			InnerHotspotServer.getSingletonServer().stopServer();
//		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static CorePlugin getDefault() {
		return plugin;
	}

}

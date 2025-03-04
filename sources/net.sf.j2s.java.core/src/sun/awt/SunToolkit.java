/*
 * Some portions of this file have been modified by Robert Hanson hansonr.at.stolaf.edu 2012-2017
 * for use in SwingJS via transpilation into JavaScript using Java2Script.
 *
 * Copyright (c) 1997, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.awt;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
//import java.awt.image.ImageProducer;
import java.awt.peer.PanelPeer;
//import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
//import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
//import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
//import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
//import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR;
//import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB;
//import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//import sun.awt.image.FileImageSource;
//import sun.awt.image.URLImageSource;
import sun.font.FontDesignMetrics;
//import java.util.Collections;
//import java.util.WeakHashMap;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//import java.awt.GraphicsEnvironment;
//import java.lang.reflect.Method;
//import java.util.Iterator;
//import java.awt.Graphics2D;
//import java.awt.GraphicsEnvironment;
//import java.awt.image.BufferedImage;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class SunToolkit extends Toolkit implements
		WindowClosingSupport, WindowClosingListener, ComponentFactory {// ,
																																		// InputMethodSupport
																																		// {

	// 8014736: logging has been removed from SunToolkit

	/* Load debug settings for native code */
	// SwingJS ?? static {
	// if (AccessController.doPrivileged(new
	// GetBooleanAction("sun.awt.nativedebug"))) {
	// DebugSettings.init();
	// }
	// };

	/**
	 * Special mask for the UngrabEvent events, in addition to the public masks
	 * defined in AWTEvent. Should be used as the mask value for
	 * Toolkit.addAWTEventListener.
	 */
	public static final int GRAB_EVENT_MASK = 0x80000000;

	// private static Method wakeupMethod;
	/*
	 * The key to put()/get() the PostEventQueue into/from the AppContext.
	 */
	protected static final String POST_EVENT_QUEUE_KEY = "PostEventQueue";

	public SunToolkit() {
		// SwingJS simplification here
		// /* If awt.threadgroup is set to class name the instance of
		// * this class is created (should be subclass of ThreadGroup)
		// * and EventDispatchThread is created inside of it
		// *
		// * If loaded class overrides uncaughtException instance
		// * handles all uncaught exception on EventDispatchThread
		// */
		// Runnable initEQ = new Runnable() {
		// public void run () {
		EventQueue eventQueue;
		//
		// String eqName = System.getProperty("AWT.EventQueueClass",
		// "java.awt.EventQueue");
		//
		// try {
		// eventQueue = (EventQueue)Class.forName(eqName).newInstance();
		// } catch (Exception e) {
		// System.err.println("Failed loading " + eqName + ": " + e);
		// e.printStackTrace();
		eventQueue = new EventQueue();
		// }
		AppContext appContext = AppContext.getAppContext();
		if (appContext != null) {
			appContext.put(AppContext.EVENT_QUEUE_KEY, eventQueue);
			PostEventQueue postEventQueue = new PostEventQueue(eventQueue);
			appContext.put(POST_EVENT_QUEUE_KEY, postEventQueue);
		}
		// }
		// };

		// if (threadGroup != null) {
		// Thread eqInitThread = new Thread(threadGroup, initEQ, "EventQueue-Init");
		// eqInitThread.start();
		// try {
		// eqInitThread.join();
		// } catch (InterruptedException e) {
		// System.out.println("Suntoolkit error in threadgroup " + e);
		// e.printStackTrace();
		// }
		// } else {
		// initEQ.run();
		// }
	}

	public boolean useBufferPerWindow() {
		return false;
	}

	// public abstract WindowPeer createWindow(Window target)
	// throws HeadlessException;
	//
	// public abstract FramePeer createFrame(Frame target)
	// throws HeadlessException;
	//
	// public abstract DialogPeer createDialog(Dialog target)
	// throws HeadlessException;
	//
	// public abstract ButtonPeer createButton(Button target)
	// throws HeadlessException;
	//
	// public abstract TextFieldPeer createTextField(TextField target)
	// throws HeadlessException;
	//
	// public abstract ChoicePeer createChoice(Choice target)
	// throws HeadlessException;
	//
	// public abstract LabelPeer createLabel(Label target)
	// throws HeadlessException;
	//
	// public abstract ListPeer createList(java.awt.List target)
	// throws HeadlessException;
	//
	// public abstract CheckboxPeer createCheckbox(Checkbox target)
	// throws HeadlessException;
	//
	// public abstract ScrollbarPeer createScrollbar(Scrollbar target)
	// throws HeadlessException;
	//
	// public abstract ScrollPanePeer createScrollPane(ScrollPane target)
	// throws HeadlessException;
	//
	// public abstract TextAreaPeer createTextArea(TextArea target)
	// throws HeadlessException;
	//
	// public abstract FileDialogPeer createFileDialog(FileDialog target)
	// throws HeadlessException;
	//
	// public abstract MenuBarPeer createMenuBar(MenuBar target)
	// throws HeadlessException;
	//
	// public abstract MenuPeer createMenu(Menu target)
	// throws HeadlessException;
	//
	// public abstract PopupMenuPeer createPopupMenu(PopupMenu target)
	// throws HeadlessException;
	//
	// public abstract MenuItemPeer createMenuItem(MenuItem target)
	// throws HeadlessException;
	//
	// public abstract CheckboxMenuItemPeer createCheckboxMenuItem(
	// CheckboxMenuItem target)
	// throws HeadlessException;
	//
	// public abstract DragSourceContextPeer createDragSourceContextPeer(
	// DragGestureEvent dge)
	// throws InvalidDnDOperationException;
	//
	// public abstract TrayIconPeer createTrayIcon(TrayIcon target)
	// throws HeadlessException, AWTException;
	//
	// public abstract SystemTrayPeer createSystemTray(SystemTray target);

	public abstract boolean isTraySupported();

	// public abstract FontPeer getFontPeer(String name, int style);
	//
	// public abstract RobotPeer createRobot(Robot target, GraphicsDevice screen)
	// throws AWTException;

	// public KeyboardFocusManagerPeer
	// createKeyboardFocusManagerPeer(KeyboardFocusManager manager) throws
	// HeadlessException {
	// return null;
	// // SwingJS ?? KeyboardFocusManagerPeerImpl peer = new
	// KeyboardFocusManagerPeerImpl(manager);
	// // return peer;
	// }

	// /**
	// * The AWT lock is typically only used on Unix platforms to synchronize
	// * access to Xlib, OpenGL, etc. However, these methods are implemented
	// * in SunToolkit so that they can be called from shared code (e.g.
	// * from the OGL pipeline) or from the X11 pipeline regardless of whether
	// * XToolkit or MToolkit is currently in use. There are native macros
	// * (such as AWT_LOCK) defined in awt.h, so if the implementation of these
	// * methods is changed, make sure it is compatible with the native macros.
	// *
	// * Note: The following methods (awtLock(), awtUnlock(), etc) should be
	// * used in place of:
	// * synchronized (getAWTLock()) {
	// * ...
	// * }
	// *
	// * By factoring these methods out specially, we are able to change the
	// * implementation of these methods (e.g. use more advanced locking
	// * mechanisms) without impacting calling code.
	// *
	// * Sample usage:
	// * private void doStuffWithXlib() {
	// * assert !SunToolkit.isAWTLockHeldByCurrentThread();
	// * SunToolkit.awtLock();
	// * try {
	// * ...
	// * XlibWrapper.XDoStuff();
	// * } finally {
	// * SunToolkit.awtUnlock();
	// * }
	// * }
	// */
	//
	// private static final ReentrantLock AWT_LOCK = new ReentrantLock();
	// private static final Condition AWT_LOCK_COND = AWT_LOCK.newCondition();
	//
	// public static final void awtLock() {
	// AWT_LOCK.lock();
	// }
	//
	// public static final boolean awtTryLock() {
	// return AWT_LOCK.tryLock();
	// }
	//
	// public static final void awtUnlock() {
	// AWT_LOCK.unlock();
	// }
	//
	// public static final void awtLockWait()
	// throws InterruptedException
	// {
	// AWT_LOCK_COND.await();
	// }
	//
	// public static final void awtLockWait(long timeout)
	// throws InterruptedException
	// {
	// AWT_LOCK_COND.await(timeout, TimeUnit.MILLISECONDS);
	// }
	//
	// public static final void awtLockNotify() {
	// AWT_LOCK_COND.signal();
	// }
	//
	// public static final void awtLockNotifyAll() {
	// AWT_LOCK_COND.signalAll();
	// }
	//
	// public static final boolean isAWTLockHeldByCurrentThread() {
	// return AWT_LOCK.isHeldByCurrentThread();
	// }

	/*
	 * Create a new AppContext, along with its EventQueue, for a new ThreadGroup.
	 * Browser code, for example, would use this method to create an AppContext &
	 * EventQueue for an Applet.
	 */
	public static AppContext createNewAppContext() {
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		return createNewAppContext(threadGroup);
	}

	static final AppContext createNewAppContext(ThreadGroup threadGroup) {
		EventQueue eventQueue;
		
		String eqName = System.getProperty("AWT.EventQueueClass",
				"java.awt.EventQueue");
		try {
			eventQueue = (EventQueue) Class.forName(eqName).newInstance();
		} catch (Exception e) {
			System.err.println("Failed loading " + eqName + ": " + e);
			eventQueue = new EventQueue();
		}
		AppContext appContext = new AppContext(threadGroup);
		appContext.put(AppContext.EVENT_QUEUE_KEY, eventQueue);

		PostEventQueue postEventQueue = new PostEventQueue(eventQueue);
		appContext.put(POST_EVENT_QUEUE_KEY, postEventQueue);

		return appContext;
	}

	static void wakeupEventQueue(EventQueue q, boolean isShutdown) {
		q.wakeup(isShutdown);
		// SwingJS -- not applicable? if (wakeupMethod == null){
		// wakeupMethod = (Method)AccessController.doPrivileged(new
		// PrivilegedAction(){
		// public Object run(){
		// try {
		// Method method = EventQueue.class.getDeclaredMethod("wakeup",new Class []
		// {Boolean.TYPE} );
		// if (method != null) {
		// method.setAccessible(true);
		// }
		// return method;
		// } catch (NoSuchMethodException e) {
		// assert false;
		// } catch (SecurityException e) {
		// assert false;
		// }
		// return null;
		// }//run
		// });
		// }
		// try{
		// if (wakeupMethod != null){
		// wakeupMethod.invoke(q, new Object[]{Boolean.valueOf(isShutdown)});
		// }
		// } catch (InvocationTargetException e){
		// assert false;
		// } catch (IllegalAccessException e) {
		// assert false;
		// }
	}

	/*
	 * Fetch the peer associated with the given target (as specified in the peer
	 * creation method). This can be used to determine things like what the parent
	 * peer is. If the target is null or the target can't be found (either because
	 * the a peer was never created for it or the peer was disposed), a null will
	 * be returned.
	 */
	protected static Object targetToPeer(Object target) {
		if (target != null
		// && !GraphicsEnvironment.isHeadless()
		) {
			return AWTAutoShutdown.getInstance().getPeer(target);
		}
		return null;
	}

	protected static void targetCreatedPeer(Object target, Object peer) {
		if (target != null && peer != null
		// && !GraphicsEnvironment.isHeadless()
		) {
			AWTAutoShutdown.getInstance().registerPeer(target, peer);
		}
	}

	protected static void targetDisposedPeer(Object target, Object peer) {
		if (target != null && peer != null
		// &&
		// !GraphicsEnvironment.isHeadless()
		) {
			AWTAutoShutdown.getInstance().unregisterPeer(target, peer);
		}
	}

	// Maps from non-Component/MenuComponent to AppContext.
	// WeakHashMap<Component,AppContext>
	private static final Map appContextMap = new HashMap();

	// Collections.synchronizedMap(new WeakHashMap());

	/**
	 * Sets the appContext field of target. If target is not a Component or
	 * MenuComponent, this returns false.
	 */
	private static boolean setAppContext(Object target, AppContext context) {
		// SwingJS -- NOT allowing moving components from one applet to another!
		return false;
//		if (target instanceof Component) {
//			AWTAccessor.getComponentAccessor().setAppContext((Component) target,
//					context);
//			// } else if (target instanceof MenuComponent) {
//			// AWTAccessor.getMenuComponentAccessor().
//			// setAppContext((MenuComponent)target, context);
//		} else {
//			return false;
//		}
//		return true;
	}

	/**
	 * Returns the appContext field for target. If target is not a Component or
	 * MenuComponent this returns null.
	 */
	private static AppContext getAppContext(Object target) {
		if (target instanceof Component) {
			// SwingJS untested; TODO
			// in SwingJS can't be MenuComponent - that's an AWT menu construct
			/**
			 * @j2sNative
			 * 
			 *            return target.appContext;
			 */
			{
				return null;
//						AWTAccessor.getComponentAccessor().getAppContext(
//						(Component) target);
			}
			// } else if (target instanceof MenuComponent) {
			// return AWTAccessor.getMenuComponentAccessor().
			// getAppContext((MenuComponent)target);
		} else {
			return null;
		}
	}

	/*
	 * Fetch the AppContext associated with the given target. This can be used to
	 * determine things like which EventQueue to use for posting events to a
	 * Component. If the target is null or the target can't be found, a null with
	 * be returned.
	 */
	public static AppContext targetToAppContext(Object target) {
		if (target == null
		// || GraphicsEnvironment.isHeadless()
		) {
			return null;
		}
		AppContext context = getAppContext(target);
		if (context == null) {
			// target is not a Component/MenuComponent, try the
			// appContextMap.
			context = (AppContext) appContextMap.get(target);
		}
		return context;
	}

	/**
	 * Sets the synchronous status of focus requests on lightweight components in
	 * the specified window to the specified value. If the boolean parameter is
	 * <code>true</code> then the focus requests on lightweight components will be
	 * performed synchronously, if it is <code>false</code>, then asynchronously.
	 * By default, all windows have their lightweight request status set to
	 * asynchronous.
	 * <p>
	 * The application can only set the status of lightweight focus requests to
	 * synchronous for any of its windows if it doesn't perform focus transfers
	 * between different heavyweight containers. In this case the observable focus
	 * behaviour is the same as with asynchronous status.
	 * <p>
	 * If the application performs focus transfer between different heavyweight
	 * containers and sets the lightweight focus request status to synchronous for
	 * any of its windows, then further focus behaviour is unspecified.
	 * <p>
	 * 
	 * @param w
	 *          window for which the lightweight focus request status should be
	 *          set
	 * @param status
	 *          the value of lightweight focus request status
	 */

	public static void setLWRequestStatus(Window changed, boolean status) {
		changed.setLWRequestStatus(status);
		//AWTAccessor.getWindowAccessor().setLWRequestStatus(changed, status);
	}

	public static void checkAndSetPolicy(Container cont, boolean isSwingCont) {
		// FocusTraversalPolicy defaultPolicy = KeyboardFocusManager
		// .getCurrentKeyboardFocusManager().getDefaultFocusTraversalPolicy();
		//
		// String toolkitName = Toolkit.getDefaultToolkit().getClass().getName();
		// // if this is not XAWT then use default policy
		// // because Swing change it
		// if (!"sun.awt.X11.XToolkit".equals(toolkitName)) {
		// cont.setFocusTraversalPolicy(defaultPolicy);
		// return;
		// }
		//
		// String policyName = defaultPolicy.getClass().getName();
		//
		// if (DefaultFocusTraversalPolicy.class != defaultPolicy.getClass()) {
		// // Policy was changed
		// // Check if it is awt policy or swing policy
		// // If it is Swing policy we shouldn't use it in AWT frames
		// // If it is AWT policy we shouldn't use it in Swing frames
		// // Otherwise we should use this policy
		// if (policyName.startsWith("java.awt.")) {
		// // AWT
		// if (isSwingCont) {
		// // Can't use AWT policy in Swing windows - should use Swing's one.
		// defaultPolicy = createLayoutPolicy();
		// } else {
		// // New awt policy.
		// }
		// } else if (policyName.startsWith("javax.swing.")) {
		// if (isSwingCont) {
		// // New Swing's policy
		// } else {
		// defaultPolicy = new DefaultFocusTraversalPolicy();
		// }
		// }
		// } else {
		// // Policy is default, use different default policy for swing
		// if (isSwingCont) {
		// defaultPolicy = createLayoutPolicy();
		// }
		// }
		// cont.setFocusTraversalPolicy(defaultPolicy);
	}

	// private static FocusTraversalPolicy createLayoutPolicy() {
	// FocusTraversalPolicy policy = null;
	// try {
	// Class layoutPolicyClass =
	// Class.forName("javax.swing.LayoutFocusTraversalPolicy");
	// policy = (FocusTraversalPolicy) layoutPolicyClass.newInstance();
	// }
	// catch (ClassNotFoundException e) {
	// assert false;
	// }
	// catch (InstantiationException e) {
	// assert false;
	// }
	// catch (IllegalAccessException e) {
	// assert false;
	// }
	//
	// return policy;
	// }

	/*
	 * Insert a mapping from target to AppContext, for later retrieval via
	 * targetToAppContext() above.
	 */
	public static void insertTargetMapping(Object target, AppContext appContext) {
		// if (!GraphicsEnvironment.isHeadless()) {
		if (!setAppContext(target, appContext)) {
			// Target is not a Component/MenuComponent, use the private Map
			// instead.
			appContextMap.put(target, appContext);
		}
		// }
	}

	/*
	 * Post an AWTEvent to the Java EventQueue, using the PostEventQueue to avoid
	 * possibly calling client code (EventQueueSubclass.postEvent()) on the
	 * toolkit (AWT-Windows/AWT-Motif) thread. This function should not be called
	 * under another lock since it locks the EventQueue. See bugids 4632918,
	 * 4526597.
	 */
	public static void postEvent(AppContext appContext, AWTEvent event) {
		if (event == null) {
			throw new NullPointerException();
		}
		PostEventQueue postEventQueue = (PostEventQueue) appContext
				.get(POST_EVENT_QUEUE_KEY);
		if (postEventQueue != null) {
			postEventQueue.postEvent(event);
		}
	}

	/*
	 * Post AWTEvent of high priority.
	 */
	public static void postPriorityEvent(final AWTEvent e) {
		// SwingJS -- no priorities here -- could be a problem?
		PeerEvent pe = new PeerEvent(Toolkit.getDefaultToolkit(), new Runnable() {
			@Override
			public void run() {
				e.setPosted();
//				AWTAccessor.getAWTEventAccessor().setPosted(e);
				((Component) e.getSource()).dispatchEvent(e);
			}
		}, PeerEvent.ULTIMATE_PRIORITY_EVENT);
		postEvent(targetToAppContext(e.getSource()), pe);
	}

	/*
	 * Flush any pending events which haven't been posted to the AWT EventQueue
	 * yet.
	 */
	public static void flushPendingEvents() {
		AppContext appContext = AppContext.getAppContext();
		PostEventQueue postEventQueue = (PostEventQueue) appContext
				.get(POST_EVENT_QUEUE_KEY);
		if (postEventQueue != null) {
			postEventQueue.flush();
		}
	}

	public static boolean isPostEventQueueEmpty() {
		AppContext appContext = AppContext.getAppContext();
		PostEventQueue postEventQueue = (PostEventQueue) appContext
				.get(POST_EVENT_QUEUE_KEY);
		return (postEventQueue == null || postEventQueue.noEvents());
	}

	/*
	 * Execute a chunk of code on the Java event handler thread for the given
	 * target. Does not wait for the execution to occur before returning to the
	 * caller.
	 */
	public static void executeOnEventHandlerThread(Object target,
			Runnable runnable) {
		executeOnEventHandlerThread(new PeerEvent(target, runnable,
				PeerEvent.PRIORITY_EVENT));
	}

	/*
	 * Fixed 5064013: the InvocationEvent time should be equals the time of the
	 * ActionEvent
	 */
	@SuppressWarnings("serial")
	public static void executeOnEventHandlerThread(Object target,
			Runnable runnable, final long when) {
		executeOnEventHandlerThread(new PeerEvent(target, runnable,
				PeerEvent.PRIORITY_EVENT) {
			@Override
			public long getWhen() {
				return when;
			}
		});
	}

	/*
	 * Execute a chunk of code on the Java event handler thread for the given
	 * target. Does not wait for the execution to occur before returning to the
	 * caller.
	 */
	public static void executeOnEventHandlerThread(PeerEvent peerEvent) {
		postEvent(targetToAppContext(peerEvent.getSource()), peerEvent);
	}

	/*
	 * Execute a chunk of code on the Java event handler thread. The method takes
	 * into account provided AppContext and sets
	 * <code>SunToolkit.getDefaultToolkit()</code> as a target of the event. See
	 * 6451487 for detailes. Does not wait for the execution to occur before
	 * returning to the caller.
	 */
	public static void invokeLaterOnAppContext(AppContext appContext,
			Runnable dispatcher) {
		postEvent(appContext, new PeerEvent(Toolkit.getDefaultToolkit(),
				dispatcher, PeerEvent.PRIORITY_EVENT));
	}

	// /*
	// * Execute a chunk of code on the Java event handler thread for the
	// * given target. Waits for the execution to occur before returning
	// * to the caller.
	// */
	// public static void executeOnEDTAndWait(Object target, Runnable runnable)
	// throws InterruptedException, InvocationTargetException
	// {
	// if (EventQueue.isDispatchThread()) {
	// throw new
	// Error("Cannot call executeOnEDTAndWait from any event dispatcher thread");
	// }
	//
	// // class AWTInvocationLock {}
	// // Object lock = new AWTInvocationLock();
	// //
	// PeerEvent event = new PeerEvent(target, runnable, null, true,
	// PeerEvent.PRIORITY_EVENT);
	//
	// // synchronized (lock) {
	// executeOnEventHandlerThread(event);
	// // lock.wait();
	// // }
	//
	// Throwable eventThrowable = event.getThrowable();
	// if (eventThrowable != null) {
	// throw new InvocationTargetException(eventThrowable);
	// }
	// }
	//
	/*
	 * Returns true if the calling thread is the event dispatch thread contained
	 * within AppContext which associated with the given target. Use this call to
	 * ensure that a given task is being executed (or not being) on the event
	 * dispatch thread for the given target.
	 */
	public static boolean isDispatchThreadForAppContext(Object target) {
		AppContext appContext = targetToAppContext(target);
		EventQueue eq = (EventQueue) appContext.get(AppContext.EVENT_QUEUE_KEY);
		// BH SwingJS does this take into account app context? -- Never called in SwingJS
		return eq.isDispatchThread();
	}

	@Override
	public Dimension getScreenSize() {
		return new Dimension(getScreenWidth(), getScreenHeight());
	}

	protected abstract int getScreenWidth();

	protected abstract int getScreenHeight();

	@Override
	public FontMetrics getFontMetrics(Font font) {
		return FontDesignMetrics.getMetrics(font);
	}

	@Override
	public String[] getFontList() {
		String[] hardwiredFontList = { Font.DIALOG, Font.SANS_SERIF, Font.SERIF,
				Font.MONOSPACED, Font.DIALOG_INPUT

		// -- Obsolete font names from 1.0.2. It was decided that
		// -- getFontList should not return these old names:
		// "Helvetica", "TimesRoman", "Courier", "ZapfDingbats"
		};
		return hardwiredFontList;
	}

	@Override
	public PanelPeer createPanel(Panel target) {
		return (PanelPeer) createComponent(target);
	}
	//
	// public CanvasPeer createCanvas(Canvas target) {
	// return (CanvasPeer)createComponent(target);
	// }

	// /**
	// * Disables erasing of background on the canvas before painting if
	// * this is supported by the current toolkit. It is recommended to
	// * call this method early, before the Canvas becomes displayable,
	// * because some Toolkit implementations do not support changing
	// * this property once the Canvas becomes displayable.
	// */
	// public void disableBackgroundErase(Canvas canvas) {
	// disableBackgroundEraseImpl(canvas);
	// }

	/**
	 * Disables the native erasing of the background on the given component before
	 * painting if this is supported by the current toolkit. This only has an
	 * effect for certain components such as Canvas, Panel and Window. It is
	 * recommended to call this method early, before the Component becomes
	 * displayable, because some Toolkit implementations do not support changing
	 * this property once the Component becomes displayable.
	 */
	public void disableBackgroundErase(Component component) {
		component.setBackgroundEraseDisabled(true);
	}

//	private void disableBackgroundEraseImpl(Component component) {
//		AWTAccessor.getComponentAccessor().setBackgroundEraseDisabled(component,
//				true);
//	}

	/**
	 * Returns the value of "sun.awt.noerasebackground" property. Default value
	 * is {@code false}.
	 */
	public static boolean getSunAwtNoerasebackground() {
		return false;
		// return AccessController.doPrivileged(new
		// GetBooleanAction("sun.awt.noerasebackground"));
	}

	/**
	 * Returns the value of "sun.awt.erasebackgroundonresize" property. Default
	 * value is {@code false}.
	 */
	public static boolean getSunAwtErasebackgroundonresize() {
		return true;
		// return AccessController.doPrivileged(new
		// GetBooleanAction("sun.awt.erasebackgroundonresize"));
	}

	/**
	 * Makes the window OverrideRedirect, on X11 platforms. See ICCCM
	 * specification for more details about OverrideRedirect windows. Implemented
	 * in XToolkit, no-op in WToolkit.
	 */
	public void setOverrideRedirect(Window target) {
	}

	static HashMap imgCache = new HashMap();

	static synchronized Image getImageFromHash(Toolkit tk, URL url) {
//		SecurityManager sm = System.getSecurityManager();
//		if (sm != null) {
//			try {
//				java.security.Permission perm = url.openConnection().getPermission();
//				if (perm != null) {
//					try {
//						sm.checkPermission(perm);
//					} catch (SecurityException se) {
//						// fallback to checkRead/checkConnect for pre 1.2
//						// security managers
//						if ((perm instanceof java.io.FilePermission)
//								&& perm.getActions().indexOf("read") != -1) {
//							sm.checkRead(perm.getName());
//						} else if ((perm instanceof java.net.SocketPermission)
//								&& perm.getActions().indexOf("connect") != -1) {
//							sm.checkConnect(url.getHost(), url.getPort());
//						} else {
//							throw se;
//						}
//					}
//				}
//			} catch (java.io.IOException ioe) {
//				sm.checkConnect(url.getHost(), url.getPort());
//			}
//		}
		Image img = (Image) imgCache.get(url);
		if (img == null) {
			try {
				img = tk.createImage(url);
				imgCache.put(url, img);
			} catch (Exception e) {
			}
		}
		return img;
	}

	static synchronized Image getImageFromHash(Toolkit tk, String filename) {
//		SecurityManager security = System.getSecurityManager();
//		if (security != null) {
//			security.checkRead(filename);
//		}
		Image img = (Image) imgCache.get(filename);
		if (img == null) {
			try {
				tk.createImage(filename); //new FileImageSource(filename);
				imgCache.put(filename, img);
			} catch (Exception e) {
			}
		}
		return img;
	}

	@Override
	public Image getImage(String filename) {
		return getImageFromHash(this, filename);
	}

	@Override
	public Image getImage(URL url) {
		return getImageFromHash(this, url);
	}

	@Override
	public abstract Image createImage(String filename);
//	{
//		SecurityManager security = System.getSecurityManager();
//		if (security != null) {
//			security.checkRead(filename);
//		}
//		return null;
//		// SwingJS ?? return createImage(new FileImageSource(filename));
//	}

	@Override
	public abstract Image createImage(URL url);// {
//		SecurityManager sm = System.getSecurityManager();
//		if (sm != null) {
//			try {
//				java.security.Permission perm = url.openConnection().getPermission();
//				if (perm != null) {
//					try {
//						sm.checkPermission(perm);
//					} catch (SecurityException se) {
//						// fallback to checkRead/checkConnect for pre 1.2
//						// security managers
//						if ((perm instanceof java.io.FilePermission)
//								&& perm.getActions().indexOf("read") != -1) {
//							sm.checkRead(perm.getName());
//						} else if ((perm instanceof java.net.SocketPermission)
//								&& perm.getActions().indexOf("connect") != -1) {
//							sm.checkConnect(url.getHost(), url.getPort());
//						} else {
//							throw se;
//						}
//					}
//				}
//			} catch (java.io.IOException ioe) {
//				sm.checkConnect(url.getHost(), url.getPort());
//			}
//		}
//		return createImage(new URLImageSource(url));
//	}

//	public Image createImage(byte[] data, int offset, int length) {
//		return null;
//		// SwingJS ?? return createImage(new ByteArrayImageSource(data, offset,
//		// length));
//	}

//	public Image createImage(ImageProducer producer) {
//		return null;
//		// SwingJS ?? return new ToolkitImage(producer);
//	}
//
	@Override
	public int checkImage(Image img, int w, int h, ImageObserver o) {
		// SwingJS ??
		// if (!(img instanceof ToolkitImage)) {
		// return ImageObserver.ALLBITS;
		// }
		//
		// ToolkitImage tkimg = (ToolkitImage)img;
		// int repbits;
		// if (w == 0 || h == 0) {
		// repbits = ImageObserver.ALLBITS;
		// } else {
		// repbits = tkimg.getImageRep().check(o);
		// }
		// return tkimg.check(o) | repbits;
		return 0;
	}

	@Override
	public boolean prepareImage(Image img, int w, int h, ImageObserver o) {
		if (w == 0 || h == 0) {
			return true;
		}
		//
		// // Must be a ToolkitImage
		// if (!(img instanceof ToolkitImage)) {
		// return true;
		// }
		//
		// ToolkitImage tkimg = (ToolkitImage)img;
		// if (tkimg.hasError()) {
		// if (o != null) {
		// o.imageUpdate(img, ImageObserver.ERROR|ImageObserver.ABORT,
		// -1, -1, -1, -1);
		// }
		// return false;
		// }
		// ImageRepresentation ir = tkimg.getImageRep();
		// return ir.prepare(o);
		return true;
	}

	// /**
	// * Scans {@code imageList} for best-looking image of specified dimensions.
	// * Image can be scaled and/or padded with transparency.
	// */
	// public static BufferedImage getScaledIconImage(java.util.List<Image>
	// imageList, int width, int height) {
	// if (width == 0 || height == 0) {
	// return null;
	// }
	// Image bestImage = null;
	// int bestWidth = 0;
	// int bestHeight = 0;
	// double bestSimilarity = 3; //Impossibly high value
	// // double bestScaleFactor = 0;
	// for (Iterator<Image> i = imageList.iterator();i.hasNext();) {
	// //Iterate imageList looking for best matching image.
	// //'Similarity' measure is defined as good scale factor and small insets.
	// //best possible similarity is 0 (no scale, no insets).
	// //It's found while the experiments that good-looking result is achieved
	// //with scale factors x1, x3/4, x2/3, xN, x1/N.
	// Image im = i.next();
	// if (im == null) {
	// continue;
	// }
	// // if (im instanceof ToolkitImage) {
	// // ImageRepresentation ir = ((ToolkitImage)im).getImageRep();
	// // ir.reconstruct(ImageObserver.ALLBITS);
	// // }
	// int iw;
	// int ih;
	// try {
	// iw = im.getWidth(null);
	// ih = im.getHeight(null);
	// } catch (Exception e){
	// continue;
	// }
	// if (iw > 0 && ih > 0) {
	// //Calc scale factor
	// double scaleFactor = Math.min((double)width / (double)iw,
	// (double)height / (double)ih);
	// //Calculate scaled image dimensions
	// //adjusting scale factor to nearest "good" value
	// int adjw = 0;
	// int adjh = 0;
	// double scaleMeasure = 1; //0 - best (no) scale, 1 - impossibly bad
	// if (scaleFactor >= 2) {
	// //Need to enlarge image more than twice
	// //Round down scale factor to multiply by integer value
	// scaleFactor = Math.floor(scaleFactor);
	// adjw = iw * (int)scaleFactor;
	// adjh = ih * (int)scaleFactor;
	// scaleMeasure = 1.0 - 0.5 / scaleFactor;
	// } else if (scaleFactor >= 1) {
	// //Don't scale
	// scaleFactor = 1.0;
	// adjw = iw;
	// adjh = ih;
	// scaleMeasure = 0;
	// } else if (scaleFactor >= 0.75) {
	// //Multiply by 3/4
	// scaleFactor = 0.75;
	// adjw = iw * 3 / 4;
	// adjh = ih * 3 / 4;
	// scaleMeasure = 0.3;
	// } else if (scaleFactor >= 0.6666) {
	// //Multiply by 2/3
	// scaleFactor = 0.6666;
	// adjw = iw * 2 / 3;
	// adjh = ih * 2 / 3;
	// scaleMeasure = 0.33;
	// } else {
	// //Multiply size by 1/scaleDivider
	// //where scaleDivider is minimum possible integer
	// //larger than 1/scaleFactor
	// double scaleDivider = Math.ceil(1.0 / scaleFactor);
	// scaleFactor = 1.0 / scaleDivider;
	// adjw = (int)Math.round((double)iw / scaleDivider);
	// adjh = (int)Math.round((double)ih / scaleDivider);
	// scaleMeasure = 1.0 - 1.0 / scaleDivider;
	// }
	// double similarity = ((double)width - (double)adjw) / (double)width +
	// ((double)height - (double)adjh) / (double)height + //Large padding is bad
	// scaleMeasure; //Large rescale is bad
	// if (similarity < bestSimilarity) {
	// bestSimilarity = similarity;
	// bestScaleFactor = scaleFactor;
	// bestImage = im;
	// bestWidth = adjw;
	// bestHeight = adjh;
	// }
	// if (similarity == 0) break;
	// }
	// }
	// if (bestImage == null) {
	// //No images were found, possibly all are broken
	// return null;
	// }
	// BufferedImage bimage =
	// new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	// Graphics2D g = bimage.createGraphics();
	// g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	// try {
	// int x = (width - bestWidth) / 2;
	// int y = (height - bestHeight) / 2;
	// g.drawImage(bestImage, x, y, bestWidth, bestHeight, null);
	// } finally {
	// g.dispose();
	// }
	// return bimage;
	// }
	//
	// public static DataBufferInt getScaledIconData(java.util.List<Image>
	// imageList, int width, int height) {
	// BufferedImage bimage = getScaledIconImage(imageList, width, height);
	// if (bimage == null) {
	// return null;
	// }
	// Raster raster = bimage.getRaster();
	// DataBuffer buffer = raster.getDataBuffer();
	// return (DataBufferInt)buffer;
	// }

	@Override
	protected EventQueue getSystemEventQueueImpl() {
		return getSystemEventQueueImplPP(AppContext.getAppContext());
	}

	// Package private implementation
	static EventQueue getSystemEventQueueImplPPS() {
		return getSystemEventQueueImplPP(AppContext.getAppContext());
	}

	public static EventQueue getSystemEventQueueImplPP(AppContext appContext) {
		EventQueue theEventQueue = (EventQueue) appContext
				.get(AppContext.EVENT_QUEUE_KEY);
		return theEventQueue;
	}

	/**
	 * Give native peers the ability to query the native container given a native
	 * component (eg the direct parent may be lightweight).
	 */
	public static Container getNativeContainer(Component c) {
		return Toolkit.getNativeContainer(c);
	}

	// SwingJS ?? /**
	// * Returns a new input method window, with behavior as specified in
	// * {@link java.awt.im.spi.InputMethodContext#createInputMethodWindow}.
	// * If the inputContext is not null, the window should return it from its
	// * getInputContext() method. The window needs to implement
	// * sun.awt.im.InputMethodWindow.
	// * <p>
	// * SunToolkit subclasses can override this method to return better input
	// * method windows.
	// */
	// public Window createInputMethodWindow(String title, InputContext context) {
	// return new sun.awt.im.SimpleInputMethodWindow(title, context);
	// }

    /**
     * Returns key modifiers used by Swing to set up a focus accelerator key stroke.
     */
    public int getFocusAcceleratorKeyMask() {
        return InputEvent.ALT_MASK;
    }

	/**
	 * Returns whether enableInputMethods should be set to true for peered
	 * TextComponent instances on this platform. False by default.
	 */
	public boolean enableInputMethodsForTextComponent() {
		return false;
	}

	private static Locale startupLocale = null;

	/**
	 * Returns the locale in which the runtime was started.
	 */
	public static Locale getStartupLocale() {
		if (startupLocale == null) {
			startupLocale = Locale.US;
			// SwingJS TODO -- set startup locale using navigator.language, e.g. "en-US"
			/**
			 * @j2sNative
			 */
			// String language, region, country, variant;
			// language = (String) AccessController.doPrivileged(
			// new GetPropertyAction("user.language", "en"));
			// // for compatibility, check for old user.region property
			// region = (String) AccessController.doPrivileged(
			// new GetPropertyAction("user.region"));
			// if (region != null) {
			// // region can be of form country, country_variant, or _variant
			// int i = region.indexOf('_');
			// if (i >= 0) {
			// country = region.substring(0, i);
			// variant = region.substring(i + 1);
			// } else {
			// country = region;
			// variant = "";
			// }
			// } else {
			// country = (String) AccessController.doPrivileged(
			// new GetPropertyAction("user.country", ""));
			// variant = (String) AccessController.doPrivileged(
			// new GetPropertyAction("user.variant", ""));
			// }
			// startupLocale = new Locale(language, country, variant);
		}
		return startupLocale;
	}

	/**
	 * Returns the default keyboard locale of the underlying operating system
	 */
	public Locale getDefaultKeyboardLocale() {
		return getStartupLocale();
	}

	private static String dataTransfererClassName = null;

	protected static void setDataTransfererClassName(String className) {
		dataTransfererClassName = className;
	}

	public static String getDataTransfererClassName() {
		if (dataTransfererClassName == null) {
			Toolkit.getDefaultToolkit(); // transferer set during toolkit init
		}
		return dataTransfererClassName;
	}

	// Support for window closing event notifications
	private transient WindowClosingListener windowClosingListener = null;

	/**
	 * @see sun.awt.WindowClosingSupport#getWindowClosingListener
	 */
	@Override
	public WindowClosingListener getWindowClosingListener() {
		return windowClosingListener;
	}

	/**
	 * @see sun.awt.WindowClosingSupport#setWindowClosingListener
	 */
	@Override
	public void setWindowClosingListener(WindowClosingListener wcl) {
		windowClosingListener = wcl;
	}

	/**
	 * @see sun.awt.WindowClosingListener#windowClosingNotify
	 */
	@Override
	public RuntimeException windowClosingNotify(WindowEvent event) {
		if (windowClosingListener != null) {
			return windowClosingListener.windowClosingNotify(event);
		} else {
			return null;
		}
	}

	/**
	 * @see sun.awt.WindowClosingListener#windowClosingDelivered
	 */
	@Override
	public RuntimeException windowClosingDelivered(WindowEvent event) {
		if (windowClosingListener != null) {
			return windowClosingListener.windowClosingDelivered(event);
		} else {
			return null;
		}
	}

	// SwingJS ?? private static DefaultMouseInfoPeer mPeer = null;

	// protected synchronized MouseInfoPeer getMouseInfoPeer() {
	// return null;
	// // if (mPeer == null) {
	// // mPeer = new DefaultMouseInfoPeer();
	// // }
	// // return mPeer;
	// }

	// /**
	// * Returns whether default toolkit needs the support of the xembed
	// * from embedding host(if any).
	// * @return <code>true</code>, if XEmbed is needed, <code>false</code>
	// otherwise
	// */
	// public static boolean needsXEmbed() {
	// String noxembed = (String) AccessController.
	// doPrivileged(new GetPropertyAction("sun.awt.noxembed", "false"));
	// if ("true".equals(noxembed)) {
	// return false;
	// }
	//
	// Toolkit tk = Toolkit.getDefaultToolkit();
	// if (tk instanceof SunToolkit) {
	// // SunToolkit descendants should override this method to specify
	// // concrete behavior
	// return ((SunToolkit)tk).needsXEmbedImpl();
	// } else {
	// // Non-SunToolkit doubtly might support XEmbed
	// return false;
	// }
	// }
	//
	// /**
	// * Returns whether this toolkit needs the support of the xembed
	// * from embedding host(if any).
	// * @return <code>true</code>, if XEmbed is needed, <code>false</code>
	// otherwise
	// */
	// protected boolean needsXEmbedImpl() {
	// return false;
	// }
	//
	// private static Dialog.ModalExclusionType DEFAULT_MODAL_EXCLUSION_TYPE;
	//
	// static {
	// DEFAULT_MODAL_EXCLUSION_TYPE =
	// (Dialog.ModalExclusionType)AccessController.doPrivileged(new
	// PrivilegedAction() {
	// public Object run() {
	// Dialog.ModalExclusionType defaultType =
	// Dialog.ModalExclusionType.NO_EXCLUDE;
	// try {
	// java.lang.reflect.Field f =
	// Dialog.class.getDeclaredField("DEFAULT_MODAL_EXCLUSION_TYPE");
	// f.setAccessible(true);
	// defaultType = (Dialog.ModalExclusionType)f.get(null);
	// } catch (Exception e) {
	// }
	// return defaultType;
	// }
	// });
	// }
	//
	// /**
	// * Returns whether the XEmbed server feature is requested by
	// * developer. If true, Toolkit should return an
	// * XEmbed-server-enabled CanvasPeer instead of the ordinary CanvasPeer.
	// */
	// protected final boolean isXEmbedServerRequested() {
	// return AccessController.doPrivileged(new
	// GetBooleanAction("sun.awt.xembedserver"));
	// }

	// /**
	// * Returns whether the modal exclusion API is supported by the current
	// toolkit.
	// * When it isn't supported, calling <code>setModalExcluded</code> has no
	// * effect, and <code>isModalExcluded</code> returns false for all windows.
	// *
	// * @return true if modal exclusion is supported by the toolkit, false
	// otherwise
	// *
	// * @see sun.awt.SunToolkit#setModalExcluded(java.awt.Window)
	// * @see sun.awt.SunToolkit#isModalExcluded(java.awt.Window)
	// *
	// * @since 1.5
	// */
	// public static boolean isModalExcludedSupported()
	// {
	// Toolkit tk = Toolkit.getDefaultToolkit();
	// return tk.isModalExclusionTypeSupported(DEFAULT_MODAL_EXCLUSION_TYPE);
	// }
	// /*
	// * Default implementation for isModalExcludedSupportedImpl(), returns false.
	// *
	// * @see sun.awt.windows.WToolkit#isModalExcludeSupportedImpl
	// * @see sun.awt.X11.XToolkit#isModalExcludeSupportedImpl
	// *
	// * @since 1.5
	// */
	// protected boolean isModalExcludedSupportedImpl()
	// {
	// return false;
	// }

	// /*
	// * Sets this window to be excluded from being modally blocked. When the
	// * toolkit supports modal exclusion and this method is called, input
	// * events, focus transfer and z-order will continue to work for the
	// * window, it's owned windows and child components, even in the
	// * presence of a modal dialog.
	// * For details on which <code>Window</code>s are normally blocked
	// * by modal dialog, see {@link java.awt.Dialog}.
	// * Invoking this method when the modal exclusion API is not supported by
	// * the current toolkit has no effect.
	// * @param window Window to be marked as not modally blocked
	// * @see java.awt.Dialog
	// * @see java.awt.Dialog#setModal(boolean)
	// * @see sun.awt.SunToolkit#isModalExcludedSupported
	// * @see sun.awt.SunToolkit#isModalExcluded(java.awt.Window)
	// */
	// public static void setModalExcluded(Window window)
	// {
	// window.setModalExclusionType(DEFAULT_MODAL_EXCLUSION_TYPE);
	// }
	//
	/*
	 * Returns whether the specified window is blocked by modal dialogs. If the
	 * modal exclusion API isn't supported by the current toolkit, it returns
	 * false for all windows.
	 * 
	 * @param window Window to test for modal exclusion
	 * 
	 * @return true if the window is modal excluded, false otherwise. If the modal
	 * exclusion isn't supported by the current Toolkit, false is returned
	 * 
	 * @see sun.awt.SunToolkit#isModalExcludedSupported
	 * 
	 * @see sun.awt.SunToolkit#setModalExcluded(java.awt.Window)
	 * 
	 * @since 1.5
	 */
	public static boolean isModalExcluded(Window window) {
		return true;// SwingJS was
								// window.getModalExclusionType().compareTo(DEFAULT_MODAL_EXCLUSION_TYPE)
								// >= 0;
	}

	//
	// /**
	// * Overridden in XToolkit and WToolkit
	// */
	// public boolean isModalityTypeSupported(Dialog.ModalityType modalityType) {
	// return (modalityType == Dialog.ModalityType.MODELESS) ||
	// (modalityType == Dialog.ModalityType.APPLICATION_MODAL);
	// }
	//
	// /**
	// * Overridden in XToolkit and WToolkit
	// */
	// public boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType
	// exclusionType) {
	// return (exclusionType == Dialog.ModalExclusionType.NO_EXCLUDE);
	// }

	// ///////////////////////////////////////////////////////////////////////////
	// //
	// // The following is used by the Java Plug-in to coordinate dialog modality
	// // between containing applications (browsers, ActiveX containers etc) and
	// // the AWT.
	// //
	// ///////////////////////////////////////////////////////////////////////////
	//
	// private ModalityListenerList modalityListeners = new
	// ModalityListenerList();
	//
	// public void addModalityListener(ModalityListener listener) {
	// modalityListeners.add(listener);
	// }
	//
	// public void removeModalityListener(ModalityListener listener) {
	// modalityListeners.remove(listener);
	// }
	//
	// public void notifyModalityPushed(Dialog dialog) {
	// notifyModalityChange(ModalityEvent.MODALITY_PUSHED, dialog);
	// }
	//
	// public void notifyModalityPopped(Dialog dialog) {
	// notifyModalityChange(ModalityEvent.MODALITY_POPPED, dialog);
	// }
	//
	// final void notifyModalityChange(int id, Dialog source) {
	// ModalityEvent ev = new ModalityEvent(source, modalityListeners, id);
	// ev.dispatch();
	// }
	//
	// static class ModalityListenerList implements ModalityListener {
	//
	// Vector<ModalityListener> listeners = new Vector<ModalityListener>();
	//
	// void add(ModalityListener listener) {
	// listeners.addElement(listener);
	// }
	//
	// void remove(ModalityListener listener) {
	// listeners.removeElement(listener);
	// }
	//
	// public void modalityPushed(ModalityEvent ev) {
	// Iterator<ModalityListener> it = listeners.iterator();
	// while (it.hasNext()) {
	// it.next().modalityPushed(ev);
	// }
	// }
	//
	// public void modalityPopped(ModalityEvent ev) {
	// Iterator<ModalityListener> it = listeners.iterator();
	// while (it.hasNext()) {
	// it.next().modalityPopped(ev);
	// }
	// }
	// } // end of class ModalityListenerList
	//
	// /////////////////////////////////////////////////////////////////////////
	// End Plug-in code
	// /////////////////////////////////////////////////////////////////////////

//	public static boolean isLightweightOrUnknown(Component comp) {
//		// SwingJS - not tested; probably never called
//		// if (false || !true)...
//		if (comp.isLightweight() || !(getDefaultToolkit() instanceof SunToolkit)) {
//			return true;
//		}
//		// SwingJS no idea what this is for
//		return !(
//		// comp instanceof Button
//		// || comp instanceof Canvas
//		// || comp instanceof Checkbox
//		// || comp instanceof Choice
//		// || comp instanceof Label
//		// || comp instanceof List
//		// ||
//		comp instanceof Panel
//		// || comp instanceof Scrollbar
//		// || comp instanceof ScrollPane
//		// || comp instanceof TextArea
//		// || comp instanceof TextField
//		|| comp.isWindowOrJSApplet());
//	}

	// public static class OperationTimedOut extends RuntimeException {
	// public OperationTimedOut(String msg) {
	// super(msg);
	// }
	// public OperationTimedOut() {
	// }
	// }
	// public static class InfiniteLoop extends RuntimeException {
	// }
	//
	// public static class IllegalThreadException extends RuntimeException {
	// public IllegalThreadException(String msg) {
	// super(msg);
	// }
	// public IllegalThreadException() {
	// }
	// }

	// public static final int DEFAULT_WAIT_TIME = 10000;
	// private static final int MAX_ITERS = 20;
	// private static final int MIN_ITERS = 0;
	// private static final int MINIMAL_EDELAY = 0;

	// /**
	// * Parameterless version of realsync which uses default timout (see
	// DEFAUL_WAIT_TIME).
	// */
	// public void realSync() throws OperationTimedOut, InfiniteLoop {
	// realSync(DEFAULT_WAIT_TIME);
	// }

	// /**
	// * Forces toolkit to synchronize with the native windowing
	// * sub-system, flushing all pending work and waiting for all the
	// * events to be processed. This method guarantees that after
	// * return no additional Java events will be generated, unless
	// * cause by user. Obviously, the method cannot be used on the
	// * event dispatch thread (EDT). In case it nevertheless gets
	// * invoked on this thread, the method throws the
	// * IllegalThreadException runtime exception.
	// *
	// * <p> This method allows to write tests without explicit timeouts
	// * or wait for some event. Example:
	// * <code>
	// * Frame f = ...;
	// * f.setVisible(true);
	// * ((SunToolkit)Toolkit.getDefaultToolkit()).realSync();
	// * </code>
	// *
	// * <p> After realSync, <code>f</code> will be completely visible
	// * on the screen, its getLocationOnScreen will be returning the
	// * right result and it will be the focus owner.
	// *
	// * <p> Another example:
	// * <code>
	// * b.requestFocus();
	// * ((SunToolkit)Toolkit.getDefaultToolkit()).realSync();
	// * </code>
	// *
	// * <p> After realSync, <code>b</code> will be focus owner.
	// *
	// * <p> Notice that realSync isn't guaranteed to work if recurring
	// * actions occur, such as if during processing of some event
	// * another request which may generate some events occurs. By
	// * default, sync tries to perform as much as {@value MAX_ITERS}
	// * cycles of event processing, allowing for roughly {@value
	// * MAX_ITERS} additional requests.
	// *
	// * <p> For example, requestFocus() generates native request, which
	// * generates one or two Java focus events, which then generate a
	// * serie of paint events, a serie of Java focus events, which then
	// * generate a serie of paint events which then are processed -
	// * three cycles, minimum.
	// *
	// * @param timeout the maximum time to wait in milliseconds, negative means
	// "forever".
	// */
	// public void realSync(final long timeout) throws OperationTimedOut,
	// InfiniteLoop
	// {
	// if (EventQueue.isDispatchThread()) {
	// throw new
	// IllegalThreadException("The SunToolkit.realSync() method cannot be used on the event dispatch thread (EDT).");
	// }
	// int bigLoop = 0;
	// do {
	// // Let's do sync first
	// sync();
	//
	// // During the wait process, when we were processing incoming
	// // events, we could have made some new request, which can
	// // generate new events. Example: MapNotify/XSetInputFocus.
	// // Therefore, we dispatch them as long as there is something
	// // to dispatch.
	// int iters = 0;
	// while (iters < MIN_ITERS) {
	// syncNativeQueue(timeout);
	// iters++;
	// }
	// while (syncNativeQueue(timeout) && iters < MAX_ITERS) {
	// iters++;
	// }
	// if (iters >= MAX_ITERS) {
	// throw new InfiniteLoop();
	// }
	//
	// // native requests were dispatched by X/Window Manager or Windows
	// // Moreover, we processed them all on Toolkit thread
	// // Now wait while EDT processes them.
	// //
	// // During processing of some events (focus, for example),
	// // some other events could have been generated. So, after
	// // waitForIdle, we may end up with full EventQueue
	// iters = 0;
	// while (iters < MIN_ITERS) {
	// waitForIdle(timeout);
	// iters++;
	// }
	// while (waitForIdle(timeout) && iters < MAX_ITERS) {
	// iters++;
	// }
	// if (iters >= MAX_ITERS) {
	// throw new InfiniteLoop();
	// }
	//
	// bigLoop++;
	// // Again, for Java events, it was simple to check for new Java
	// // events by checking event queue, but what if Java events
	// // resulted in native requests? Therefor, check native events again.
	// } while ((syncNativeQueue(timeout) || waitForIdle(timeout)) && bigLoop <
	// MAX_ITERS);
	// }

//	/**
//	 * Platform toolkits need to implement this method to perform the sync of the
//	 * native queue. The method should wait until native requests are processed,
//	 * all native events are processed and corresponding Java events are
//	 * generated. Should return <code>true</code> if some events were processed,
//	 * <code>false</code> otherwise.
//	 */
//	protected abstract boolean syncNativeQueue(final long timeout);

	// private boolean eventDispatched = false;
	// private boolean queueEmpty = false;
	// private final Object waitLock = "Wait Lock";

	// private boolean isEQEmpty() {
	// EventQueue queue = getSystemEventQueueImpl();
	// return AWTAccessor.getEventQueueAccessor().noEvents(queue);
	// }
	//
	// SwingJS CANNOT DO THIS waitForIdle
	//
	// /**
	// * Waits for the Java event queue to empty. Ensures that all
	// * events are processed (including paint events), and that if
	// * recursive events were generated, they are also processed.
	// * Should return <code>true</code> if more processing is
	// * necessary, <code>false</code> otherwise.
	// */
	// protected final boolean waitForIdle(final long timeout) {
	// flushPendingEvents();
	// boolean queueWasEmpty = isEQEmpty();
	// queueEmpty = false;
	// eventDispatched = false;
	// synchronized(waitLock) {
	// postEvent(AppContext.getAppContext(),
	// new PeerEvent(getSystemEventQueueImpl(), null,
	// PeerEvent.LOW_PRIORITY_EVENT) {
	// public void dispatch() {
	// // Here we block EDT. It could have some
	// // events, it should have dispatched them by
	// // now. So native requests could have been
	// // generated. First, dispatch them. Then,
	// // flush Java events again.
	// int iters = 0;
	// while (iters < MIN_ITERS) {
	// syncNativeQueue(timeout);
	// iters++;
	// }
	// while (syncNativeQueue(timeout) && iters < MAX_ITERS) {
	// iters++;
	// }
	// flushPendingEvents();
	//
	// synchronized(waitLock) {
	// queueEmpty = isEQEmpty();
	// eventDispatched = true;
	// // SwingJS CANNOT DO THIS waitLock.notifyAll();
	// }
	// }
	// });
	// try {
	// while (!eventDispatched) {
	// waitLock.wait();
	// }
	// } catch (InterruptedException ie) {
	// return false;
	// }
	// }
	//
	// try {
	// Thread.sleep(MINIMAL_EDELAY);
	// } catch (InterruptedException ie) {
	// throw new RuntimeException("Interrupted");
	// }
	//
	// flushPendingEvents();
	//
	// // Lock to force write-cache flush for queueEmpty.
	// synchronized (waitLock) {
	// return !(queueEmpty && isEQEmpty() && queueWasEmpty);
	// }
	// }

	/**
	 * Grabs the mouse input for the given window. The window must be visible. The
	 * window or its children do not receive any additional mouse events besides
	 * those targeted to them. All other events will be dispatched as before - to
	 * the respective targets. This Window will receive UngrabEvent when automatic
	 * ungrab is about to happen. The event can be listened to by installing
	 * AWTEventListener with WINDOW_EVENT_MASK. See UngrabEvent class for the list
	 * of conditions when ungrab is about to happen.
	 * 
	 * @see UngrabEvent
	 */
	public abstract void grab(Window w);

	/**
	 * Forces ungrab. No event will be sent.
	 */
	public abstract void ungrab(Window w);

	/**
	 * Locates the splash screen library in a platform dependent way and closes
	 * the splash screen. Should be invoked on first top-level frame display.
	 * 
	 * @see java.awt.SplashScreen
	 * @since 1.6
	 */
	// public static native void closeSplashScreen();

	/*
	 * The following methods and variables are to support retrieving desktop text
	 * anti-aliasing settings
	 */

	// /* Need an instance method because setDesktopProperty(..) is protected. */
	// private void fireDesktopFontPropertyChanges() {
	// setDesktopProperty(SunToolkit.DESKTOPFONTHINTS,
	// SunToolkit.getDesktopFontHints());
	// }

	// private static boolean checkedSystemAAFontSettings;
	// private static boolean useSystemAAFontSettings;
	// private static boolean lastExtraCondition = true;
	// private static RenderingHints desktopFontHints;

	// /* Since Swing is the reason for this "extra condition" logic its
	// * worth documenting it in some detail.
	// * First, a goal is for Swing and applications to both retrieve and
	// * use the same desktop property value so that there is complete
	// * consistency between the settings used by JDK's Swing implementation
	// * and 3rd party custom Swing components, custom L&Fs and any general
	// * text rendering that wants to be consistent with these.
	// * But by default on Solaris & Linux Swing will not use AA text over
	// * remote X11 display (unless Xrender can be used which is TBD and may not
	// * always be available anyway) as that is a noticeable performance hit.
	// * So there needs to be a way to express that extra condition so that
	// * it is seen by all clients of the desktop property API.
	// * If this were the only condition it could be handled here as it would
	// * be the same for any L&F and could reasonably be considered to be
	// * a static behaviour of those systems.
	// * But GTK currently has an additional test based on locale which is
	// * not applied by Metal. So mixing GTK in a few locales with Metal
	// * would mean the last one wins.
	// * This could be stored per-app context which would work
	// * for different applets, but wouldn't help for a single application
	// * using GTK and some other L&F concurrently.
	// * But it is expected this will be addressed within GTK and the font
	// * system so is a temporary and somewhat unlikely harmless corner case.
	// */
	// public static void setAAFontSettingsCondition(boolean extraCondition) {
	// if (extraCondition != lastExtraCondition) {
	// lastExtraCondition = extraCondition;
	// if (checkedSystemAAFontSettings) {
	// /* Someone already asked for this info, under a different
	// * condition.
	// * We'll force re-evaluation instead of replicating the
	// * logic, then notify any listeners of any change.
	// */
	// checkedSystemAAFontSettings = false;
	// Toolkit tk = Toolkit.getDefaultToolkit();
	// if (tk instanceof SunToolkit) {
	// ((SunToolkit)tk).fireDesktopFontPropertyChanges();
	// }
	// }
	// }
	// }

	// /* "false", "off", ""default" aren't explicitly tested, they
	// * just fall through to produce a null return which all are equated to
	// * "false".
	// */
	// private static RenderingHints getDesktopAAHintsByName(String hintname) {
	// Object aaHint = null;
	// hintname = hintname.toLowerCase(Locale.ENGLISH);
	// if (hintname.equals("on")) {
	// aaHint = VALUE_TEXT_ANTIALIAS_ON;
	// } else if (hintname.equals("gasp")) {
	// aaHint = VALUE_TEXT_ANTIALIAS_GASP;
	// } else if (hintname.equals("lcd") || hintname.equals("lcd_hrgb")) {
	// aaHint = VALUE_TEXT_ANTIALIAS_LCD_HRGB;
	// } else if (hintname.equals("lcd_hbgr")) {
	// aaHint = VALUE_TEXT_ANTIALIAS_LCD_HBGR;
	// } else if (hintname.equals("lcd_vrgb")) {
	// aaHint = VALUE_TEXT_ANTIALIAS_LCD_VRGB;
	// } else if (hintname.equals("lcd_vbgr")) {
	// aaHint = VALUE_TEXT_ANTIALIAS_LCD_VBGR;
	// }
	// if (aaHint != null) {
	// RenderingHints map = new RenderingHints(null);
	// map.put(KEY_TEXT_ANTIALIASING, aaHint);
	// return map;
	// } else {
	// return null;
	// }
	// }
	//
	// /* This method determines whether to use the system font settings,
	// * or ignore them if a L&F has specified they should be ignored, or
	// * to override both of these with a system property specified value.
	// * If the toolkit isn't a SunToolkit, (eg may be headless) then that
	// * system property isn't applied as desktop properties are considered
	// * to be inapplicable in that case. In that headless case although
	// * this method will return "true" the toolkit will return a null map.
	// */
	// private static boolean useSystemAAFontSettings() {
	// if (!checkedSystemAAFontSettings) {
	// useSystemAAFontSettings = true; /* initially set this true */
	// String systemAAFonts = null;
	// Toolkit tk = Toolkit.getDefaultToolkit();
	// if (tk instanceof SunToolkit) {
	// systemAAFonts =
	// (String)AccessController.doPrivileged(
	// new GetPropertyAction("awt.useSystemAAFontSettings"));
	// }
	// if (systemAAFonts != null) {
	// useSystemAAFontSettings =
	// Boolean.valueOf(systemAAFonts).booleanValue();
	// /* If it is anything other than "true", then it may be
	// * a hint name , or it may be "off, "default", etc.
	// */
	// if (!useSystemAAFontSettings) {
	// desktopFontHints = getDesktopAAHintsByName(systemAAFonts);
	// }
	// }
	// /* If its still true, apply the extra condition */
	// if (useSystemAAFontSettings) {
	// useSystemAAFontSettings = lastExtraCondition;
	// }
	// checkedSystemAAFontSettings = true;
	// }
	// return useSystemAAFontSettings;
	// }

	/* A variable defined for the convenience of JDK code */
	public static final String DESKTOPFONTHINTS = "awt.font.desktophints";

	/* Overridden by subclasses to return platform/desktop specific values */
	protected RenderingHints getDesktopAAHints() {
		return null;
	}

	// /* Subclass desktop property loading methods call this which
	// * in turn calls the appropriate subclass implementation of
	// * getDesktopAAHints() when system settings are being used.
	// * Its public rather than protected because subclasses may delegate
	// * to a helper class.
	// */
	// public static RenderingHints getDesktopFontHints() {
	// if (useSystemAAFontSettings()) {
	// Toolkit tk = Toolkit.getDefaultToolkit();
	// if (tk instanceof SunToolkit) {
	// Object map = ((SunToolkit)tk).getDesktopAAHints();
	// return (RenderingHints)map;
	// } else { /* Headless Toolkit */
	// return null;
	// }
	// } else if (desktopFontHints != null) {
	// /* cloning not necessary as the return value is cloned later, but
	// * its harmless.
	// */
	// return (RenderingHints)(desktopFontHints.clone());
	// } else {
	// return null;
	// }
	// }

	// public abstract boolean isDesktopSupported();

	// /*
	// * consumeNextKeyTyped() method is not currently used,
	// * however Swing could use it in the future.
	// */
	// public static synchronized void consumeNextKeyTyped(KeyEvent keyEvent) {
	// try {
	// AWTAccessor.getDefaultKeyboardFocusManagerAccessor().consumeNextKeyTyped(
	// (DefaultKeyboardFocusManager)KeyboardFocusManager.
	// getCurrentKeyboardFocusManager(),
	// keyEvent);
	// } catch (ClassCastException cce) {
	// cce.printStackTrace();
	// }
	// }

	// protected static void dumpPeers(final Logger aLog) {
	// // SwingJS ?? AWTAutoShutdown.getInstance().dumpPeers(aLog);
	// }
	//
	/**
	 * Returns the <code>Window</code> ancestor of the component <code>comp</code>
	 * .
	 * 
	 * @return Window ancestor of the component or component by itself if it is
	 *         Window; null, if component is not a part of window hierarchy
	 */
	public static Window getContainingWindow(Component comp) {
		while (comp != null && !comp.isWindowOrJSApplet()) {
			comp = comp.getParent();
		}
		return (Window) comp;
	}

	// private static Boolean sunAwtDisableMixing = null;
	//
	// /**
	// * Returns the value of "sun.awt.disableMixing" property. Default
	// * value is {@code false}.
	// */
	// public synchronized static boolean getSunAwtDisableMixing() {
	// if (sunAwtDisableMixing == null) {
	// sunAwtDisableMixing = Boolean.valueOf(
	// AccessController.doPrivileged(
	// new GetBooleanAction("sun.awt.disableMixing")));
	// }
	// return sunAwtDisableMixing.booleanValue();
	// }
	//
	/**
	 * Returns true if the native GTK libraries are available. The default
	 * implementation returns false, but UNIXToolkit overrides this method to
	 * provide a more specific answer.
	 */
	public boolean isNativeGTKAvailable() {
		return false;
	}

	// Constant alpha
	public boolean isWindowOpacitySupported() {
		return false;
	}

	// Shaping
	public boolean isWindowShapingSupported() {
		return false;
	}

	// Per-pixel alpha
	public boolean isWindowTranslucencySupported() {
		return false;
	}

	public boolean isTranslucencyCapable(GraphicsConfiguration gc) {
		return false;
	}

	// /**
	// * Returns whether or not a containing top level window for the passed
	// * component is
	// * {@link com.sun.awt.AWTUtilities.Translucency#PERPIXEL_TRANSLUCENT
	// PERPIXEL_TRANSLUCENT}.
	// *
	// * @param c a Component which toplevel's to check
	// * @return {@code true} if the passed component is not null and has a
	// * containing toplevel window which is opaque (so per-pixel translucency
	// * is not enabled), {@code false} otherwise
	// * @see com.sun.awt.AWTUtilities.Translucency#PERPIXEL_TRANSLUCENT
	// * @see com.sun.awt.AWTUtilities#isWindowOpaque(Window)
	// */
	// public static boolean isContainingTopLevelOpaque(Component c) {
	// Window w = getContainingWindow(c);
	// // return w != null && (w).isOpaque();
	// return w != null && com.sun.awt.AWTUtilities.isWindowOpaque(w);
	// }
	//
	// /**
	// * Returns whether or not a containing top level window for the passed
	// * component is
	// * {@link com.sun.awt.AWTUtilities.Translucency#TRANSLUCENT TRANSLUCENT}.
	// *
	// * @param c a Component which toplevel's to check
	// * @return {@code true} if the passed component is not null and has a
	// * containing toplevel window which has opacity less than
	// * 1.0f (which means that it is translucent), {@code false} otherwise
	// * @see com.sun.awt.AWTUtilities.Translucency#TRANSLUCENT
	// * @see com.sun.awt.AWTUtilities#getWindowOpacity(Window)
	// */
	// public static boolean isContainingTopLevelTranslucent(Component c) {
	// Window w = getContainingWindow(c);
	// // return w != null && (w).getOpacity() < 1.0f;
	// return w != null && com.sun.awt.AWTUtilities.getWindowOpacity((Window)w)
	// < 1.0f;
	// }

	/**
	 * Returns whether the native system requires using the peer.updateWindow()
	 * method to update the contents of a non-opaque window, or if usual painting
	 * procedures are sufficient. The default return value covers the X11 systems.
	 * On MS Windows this method is overriden in WToolkit to return true.
	 */
	public boolean needUpdateWindow() {
		return false;
	}

} // class SunToolkit

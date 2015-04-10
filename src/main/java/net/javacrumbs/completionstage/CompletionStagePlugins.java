package net.javacrumbs.completionstage;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Registry for plugin implementations that allows global override and handles the retrieval of correct
 * implementation based on order of precedence:
 * <ol>
 * <li>plugin registered globally via {@code register} methods in this class</li>
 * <li>plugin registered and retrieved using {@link java.lang.System#getProperty(String)} (see get methods for
 * property names)</li>
 * <li>default implementation</li>
 * </ol>
 */
public class CompletionStagePlugins {
	private final static CompletionStagePlugins INSTANCE = new CompletionStagePlugins();

	static final Consumer<Throwable> DEFAULT_ERROR_HANDLER = ex -> {
	};

	private final AtomicReference<Consumer<? super Throwable>> errorHandler = new AtomicReference<>();

	/* package accessible for unit tests */void reset() {
		INSTANCE.errorHandler.set(null);
	}

	public static CompletionStagePlugins getInstance() {
		return INSTANCE;
	}

	private static Object getPluginImplementationViaProperty(String pluginName, Class<?> pluginClass) {
		/*
         * Check system properties for plugin class.
         * <p>
         * This will only happen during system startup thus it's okay to use the synchronized
         * System.getProperties as it will never get called in normal operations.
         */
		String implementingClass = System.getProperty("net.javacrumbs.completionstage.plugin." + pluginName + ".implementation");
		if (implementingClass != null) {
			try {
				Class<?> cls = Class.forName(implementingClass);
				// narrow the scope (cast) to the type we're expecting
				cls.asSubclass(pluginClass).newInstance();
				return cls.newInstance();
			} catch (ClassCastException e) {
				throw new RuntimeException(pluginName + " implementation is not an instance of " + pluginName + ": " + implementingClass);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(pluginName + " implementation class not found: " + implementingClass, e);
			} catch (InstantiationException e) {
				throw new RuntimeException(pluginName + " implementation not able to be instantiated: " + implementingClass, e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(pluginName + " implementation not able to be accessed: " + implementingClass, e);
			}
		} else {
			return null;
		}
	}

	/**
	 * Retrieves an instance of error handler to use based on order of precedence as defined in
	 * {@link CompletionStagePlugins} class header.
	 * <p/>
	 * Override the default by calling {@link #registerErrorHandler(Consumer)} or by setting the
	 * property {@code net.javacrumbs.completionstage.plugin.ErrorHandler.implementation} with the full classname to load.
	 *
	 * @return error handler implementation to use
	 */
	public Consumer<? super Throwable> getErrorHandler() {
		if (errorHandler.get() == null) {
			// check for an implementation from System.getProperty first
			@SuppressWarnings("unchecked")
			Consumer<? super Throwable> impl = (Consumer<? super Throwable>) getPluginImplementationViaProperty("ErrorHandler", Consumer.class);
			if (impl == null) {
				// nothing set via properties so initialize with default
				errorHandler.compareAndSet(null, DEFAULT_ERROR_HANDLER);
				// we don't return from here but call get() again in case of thread-race so the winner will always get returned
			} else {
				// we received an implementation from the system property so use it
				errorHandler.compareAndSet(null, impl);
			}
		}
		return errorHandler.get();
	}

	/**
	 * Registers an error handler implementation as a global override of any injected or default
	 * implementations.
	 *
	 * @param errorHandler error handler implementation
	 *
	 * @throws IllegalStateException if called more than once or after the default was initialized (if usage occurs before trying
	 *                               to register)
	 */
	public void registerErrorHandler(Consumer<? super Throwable> errorHandler) {
		if (!this.errorHandler.compareAndSet(null, errorHandler)) {
			throw new IllegalStateException("Another strategy was already registered: " + this.errorHandler.get());
		}
	}
}

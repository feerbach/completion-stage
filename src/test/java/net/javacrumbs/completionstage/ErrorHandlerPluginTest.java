package net.javacrumbs.completionstage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ErrorHandlerPluginTest {

	@Mock
	private Consumer<Throwable> errorHandler;

	private static final RuntimeException EXCEPTION = new RuntimeException("test");

	@Before
	public void before() {
		CompletionStagePlugins plugins = CompletionStagePlugins.getInstance();
		plugins.reset();
		plugins.registerErrorHandler(errorHandler);
	}

	@Test
	public void thenApplyAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		completionStage.thenApplyAsync((a) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void thenAcceptAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		completionStage.thenAcceptAsync((a) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void thenRunAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		completionStage.thenRunAsync(() -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void thenCombineAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		SimpleCompletionStage<Object> anotherCompletionStage = new SimpleCompletionStage<>(Runnable::run);

		completionStage.thenCombineAsync(anotherCompletionStage, (a, b) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);
		anotherCompletionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void thenAcceptBothAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		SimpleCompletionStage<Object> anotherCompletionStage = new SimpleCompletionStage<>(Runnable::run);

		completionStage.thenAcceptBothAsync(anotherCompletionStage, (a, b) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);
		anotherCompletionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void runAfterBothAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		SimpleCompletionStage<Object> anotherCompletionStage = new SimpleCompletionStage<>(Runnable::run);

		completionStage.runAfterBothAsync(anotherCompletionStage, () -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);
		anotherCompletionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void applyToEitherAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		SimpleCompletionStage<Object> anotherCompletionStage = new SimpleCompletionStage<>(Runnable::run);

		completionStage.applyToEitherAsync(anotherCompletionStage, (a) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void acceptEitherAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		SimpleCompletionStage<Object> anotherCompletionStage = new SimpleCompletionStage<>(Runnable::run);

		completionStage.acceptEitherAsync(anotherCompletionStage, (a) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void runAfterEitherAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		SimpleCompletionStage<Object> anotherCompletionStage = new SimpleCompletionStage<>(Runnable::run);

		completionStage.runAfterEitherAsync(anotherCompletionStage, () -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void thenComposeAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		completionStage.thenComposeAsync((a) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void exceptionallyTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		completionStage.exceptionally((a) -> {
			throw EXCEPTION;
		});

		completionStage.completeExceptionally(new RuntimeException("Some error"));

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void whenCompleteAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		completionStage.whenCompleteAsync((a, b) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}

	@Test
	public void handleAsyncTest() {
		SimpleCompletionStage<Object> completionStage = new SimpleCompletionStage<>(Runnable::run);
		completionStage.handleAsync((a, b) -> {
			throw EXCEPTION;
		});

		completionStage.complete(null);

		verify(errorHandler).accept(same(EXCEPTION));
	}
}

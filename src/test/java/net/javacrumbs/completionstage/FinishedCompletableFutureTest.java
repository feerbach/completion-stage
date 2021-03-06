/**
 * Copyright 2009-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.completionstage;

import org.junit.Ignore;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Tests CompletableFuture. Just to be sure I am reading the spec correctly. Same tests are executed on
 * CompletionStage and CompletableFuture.
 */
public class FinishedCompletableFutureTest extends AbstractCompletionStageTest {

    @Override
    protected CompletionStage<String> createCompletionStage(String value) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.complete(value);
        return completableFuture;
    }

    @Override
    protected CompletionStage<String> createCompletionStage(Throwable e) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(e);
        return completableFuture;
    }

    @Override
    protected void finish(CompletionStage<String> completionStage) {

    }

    @Override
    @Ignore
    public void thenComposePassesFailureFromTheOtherCompletionStage() {
        // http://stackoverflow.com/questions/27747976/why-java-8-completablefuture-thencompose-generates-different-exception-depending
    }
}
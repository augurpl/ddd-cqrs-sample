/*
 * Copyright 2011-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.com.bottega.ddd.sagas;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.com.bottega.ddd.domain.DomainEventPublisher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/sagasIntegrationTestContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class SagaIntegrationTest {

    @Inject
    private DomainEventPublisher publisher;

    @Inject
    private SagaSpy spy;

    /**
     * testing {@link SimpleSaga}
     */
    @Test
    public void shouldRunSimpleTwoStepSaga() throws Exception {
        // when
        publisher.publish(new SampleDomainEvent(1L));
        publisher.publish(new AnotherDomainEvent(1L, "data"));
        // then
        assertEquals(1, spy.getSampleEventHandledCount());
        assertEquals(1, spy.getAnotherEventHandledCount());
        assertEquals(1, spy.getSagaCompletedCount());
    }

    @Test
    public void shouldNotCompleteSameSagaTwice() throws Exception {
        // when
        publisher.publish(new SampleDomainEvent(1L));
        publisher.publish(new AnotherDomainEvent(1L, "data"));
        publisher.publish(new SampleDomainEvent(1L));
        // then
        assertEquals(2, spy.getSampleEventHandledCount());
        assertEquals(1, spy.getAnotherEventHandledCount());
        assertEquals(1, spy.getSagaCompletedCount());
    }
}

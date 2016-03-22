/*
 * Copyright 2012-2016 the original author or authors.
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

package org.springframework.boot.test.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.logging.ConditionEvalutionReportMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * {@link TestExecutionListener} to print the {@link ConditionEvaluationReport} when the
 * context cannot be prepared.
 *
 * @author Phillip Webb
 */
class AutoConfigureReportTestExecutionListener extends AbstractTestExecutionListener {

	private DependencyInjectionTestExecutionListener delegate = new DependencyInjectionTestExecutionListener();

	@Override
	public int getOrder() {
		return this.delegate.getOrder() - 1;
	}

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		try {
			this.delegate.prepareTestInstance(testContext);
		}
		catch (Exception ex) {
			ApplicationContext context = testContext.getApplicationContext();
			if (context instanceof ConfigurableApplicationContext) {
				ConditionEvaluationReport report = ConditionEvaluationReport
						.get(((ConfigurableApplicationContext) context).getBeanFactory());
				System.err.println(new ConditionEvalutionReportMessage(report));
			}
			throw ex;
		}
	}

}
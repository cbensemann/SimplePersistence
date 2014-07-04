/*
 * Copyright 2014 Nomad Consulting Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nz.co.nomadconsulting.simplepersistence;

import nz.co.nomadconsulting.simpleessentials.Expressions;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.transaction.Status;
import javax.transaction.UserTransaction;


@SuppressWarnings("serial")
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 200)
@Transaction
public class TransactionInterceptor implements Serializable {

    @Inject
    private UserTransaction userTransaction;

    @Inject
    private EntityManager entityManager;

    @Inject
    private transient Expressions expressions;

    private Map<String, Filter> filterDefinitions;


    @AroundInvoke
    public Object aroundInvoke(final InvocationContext ic) throws Exception {
        final boolean act = userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION;
        if (act) {
            userTransaction.begin();
        }
        entityManager.joinTransaction();
        try {
            final Transaction annotation = ic.getMethod().getAnnotation(Transaction.class);
            if (!annotation.filter().trim().isEmpty()) {
                enableFilter(filterDefinitions.get(annotation.filter()), entityManager);
            }
            final Object result = ic.proceed();
            if (act) {
                userTransaction.commit();
            }
            return result;
        }
        catch (final Exception e) {
            // TODO should read exceptions from @Transaction annotation
            if (act) {
                userTransaction.rollback();
            }
            throw e;
        }
    }


    @Inject
    public void setFilterDefinitions(final HibernateFilterExtension extension) {
        filterDefinitions = extension.getFilterDefinitions();
    }


    private void enableFilter(final Filter f, final EntityManager entityManager) {
        try {
            if (!(Boolean) expressions.evaluateValueExpression(f.getEnabledExpression())) {
                return;
            }
            final org.hibernate.Filter filter = getSession(entityManager).enableFilter(f.getName());
            for (final Map.Entry<String, String> me : f.getParameters().entrySet()) {
                final Object filterValue = expressions.evaluateValueExpression(me.getValue());
                if (filterValue instanceof Collection) {
                    filter.setParameterList(me.getKey(), (Collection<?>) filterValue);
                }
                else {
                    filter.setParameter(me.getKey(), filterValue);
                }
            }
            filter.validate();
        }
        catch (final NotHibernateException nhe) {
            throw new RuntimeException("bob");
        }
    }


    private org.hibernate.Session getSession(final EntityManager entityManager) {
        final Object delegate = entityManager.getDelegate();
        if (delegate instanceof org.hibernate.Session) {
            return (org.hibernate.Session) delegate;
        }
        else {
            throw new NotHibernateException();
        }
    }

    static class NotHibernateException extends IllegalArgumentException {
    }
}

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

import org.hibernate.cfg.ImprovedNamingStrategy;


/**
 * By Natural I mean natural to me :) default sql style naming with underscores and _id etc
 */
@SuppressWarnings("serial")
public class NaturalNamingStrategy extends ImprovedNamingStrategy {

    /**
     * @see org.hibernate.cfg.ImprovedNamingStrategy#foreignKeyColumnName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String foreignKeyColumnName(final String propertyName, final String propertyEntityName,
            final String propertyTableName, final String referencedColumnName) {
        final String name = super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName,
                referencedColumnName);
        return name.endsWith("_id") ? name : name + "_id";
    }
}

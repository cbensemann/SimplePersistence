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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class Filter implements Serializable {

    private String name;

    private Map<String, String> parameters = new HashMap<>();

    private String enabledExpression;


    public Filter(final String key) {
        name = key;
    }


    public String getName() {
        return name;
    }


    public void setName(final String name) {
        this.name = name;
    }


    public Map<String, String> getParameters() {
        return parameters;
    }


    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }


    public String getEnabledExpression() {
        return enabledExpression;
    }


    public void setEnabledExpression(final String enabledExpression) {
        this.enabledExpression = enabledExpression;
    }


    public void addParameter(final String key, final String value) {
        parameters.put(key, value);
    }


    @Override
    public String toString() {
        return "Filter [name=" + name + ", parameters=" + parameters + ", enabledExpression=" + enabledExpression + "]";
    }
}

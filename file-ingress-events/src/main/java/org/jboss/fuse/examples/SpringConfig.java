/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.fuse.examples;

import io.fabric8.mq.camel.AMQComponent;
import io.fabric8.mq.core.MQConnectionFactory;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@Configuration
@ComponentScan(basePackages = "org.jboss.fuse.examples.route")
public class SpringConfig extends CamelConfiguration {

    @Bean
    public AMQComponent amq() {
        AMQComponent rc = new AMQComponent();
        MQConnectionFactory factory = new MQConnectionFactory("admin", "admin");
        factory.setServiceName("broker");
        rc.setConnectionFactory(factory);
        return rc;
    }

    @Bean
    public PropertiesComponent properties() {
        PropertiesComponent rc = new PropertiesComponent();
        return rc;
    }

}

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.extensions.Job;
import io.fabric8.kubernetes.api.model.extensions.JobBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
public class KubernetesJobManfiestCreator {

    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Autowired
    private KubernetesClient kubernetesClient;
    private Map<String, String> jobLabels;

    public String generateKubernetesJobManifest(@Body String fileLocation) {
        String kubeJson = null;
        try {
            Job kubeJob = createJob(fileLocation);
            kubeJson = MAPPER.writeValueAsString(kubeJob);
//            kubernetesClient.extensions().jobs().create(kubeJob);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        return kubeJson;
    }

    private Job createJob(String fileLocation) {
        JobBuilder builder = new JobBuilder();
        builder.withNewMetadata().withName("job1").endMetadata()
                .withNewSpec()
                    .withNewSelector()
                        .withMatchLabels(getJobLabels())
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                        .withName("job#1")
                        .withLabels(getJobLabels())
                    .endMetadata()
                    .withNewSpec()
                        .addNewContainer()
                            .withName("jobcontainer")
                            .withImage("image/foo")
                            .withCommand("fooc0mmand")
                                .withEnv(getJobEnvironmentVariables(fileLocation))
                        .endContainer()
                        .withRestartPolicy("Never")
                    .endSpec()
                    .endTemplate()
                .endSpec();

        return builder.build();
    }

    public Map<String,String> getJobLabels() {
        HashMap<String, String> map = new HashMap<>();
        map.put("job", "job11");
        return map;
    }

    public List<EnvVar> getJobEnvironmentVariables(String fileLocation) {
        LinkedList<EnvVar> rc = new LinkedList<>();
        rc.add(new EnvVarBuilder().withName("FILE_NAME").withValue(fileLocation).build());
        return rc;

    }
}

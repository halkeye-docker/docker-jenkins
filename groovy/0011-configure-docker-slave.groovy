import jenkins.model.*;
import io.jenkins.docker.client.DockerAPI;
import io.jenkins.docker.connector.DockerComputerAttachConnector;
import org.jenkinsci.plugins.docker.commons.credentials.DockerServerEndpoint;
import com.nirima.jenkins.plugins.docker.DockerCloud;
import com.nirima.jenkins.plugins.docker.DockerTemplate;
import com.nirima.jenkins.plugins.docker.DockerTemplateBase;


DockerTemplateBase dockerTemplateBase = new DockerTemplateBase("halkeye/dind-jenkins-slave");
dockerTemplateBase.setPrivileged(true);
dockerTemplateBase.setTty(true);
// dockerTemplateBase.setVolumesString("/var/lib/docker:/var/lib/docker\n/var/run/docker.sock:/var/run/docker.sock")

DockerTemplate dockerTemplate = new DockerTemplate(
    dockerTemplateBase,
    new DockerComputerAttachConnector(),
    "docker-slave", /* labelString */
    "/home/jenkins", /* remoteFs */
    "2147483647" /* instanceCapStr */
)
dockerTemplate.setRemoveVolumes(true);

println('Configuring docker cloud')
def clouds = [new DockerCloud(
    "docker",
    new DockerAPI(
        new DockerServerEndpoint(
            System.getenv("DOCKER_HOST"),
            "docker-localhost"
        )
    ),
    [dockerTemplate]
)] as ArrayList

Jenkins.getInstance().clouds.replaceBy(clouds)

FROM jenkins/jenkins:2.265-alpine
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN wget -o /dev/null -O /tmp/jenkins-plugin-manager.jar https://github.com/jenkinsci/plugin-installation-manager-tool/releases/download/2.1.1/jenkins-plugin-manager-2.1.1.jar && \
    chmod 777 /tmp/jenkins-plugin-manager.jar && \
    alias jenkins-plugin-cli="java -jar /tmp/jenkins-plugin-manager.jar" && \
    jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt --verbose
RUN echo $JENKINS_VERSION > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
RUN echo $JENKINS_VERSION > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion
ENV CASC_JENKINS_CONFIG /config/jenkins.yaml
COPY jenkins.yaml /config/jenkins.yaml

FROM jenkins/jenkins:2.443-alpine
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
RUN echo $JENKINS_VERSION > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
RUN echo $JENKINS_VERSION > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion
ENV CASC_JENKINS_CONFIG /config/jenkins.yaml
COPY jenkins.yaml /config/jenkins.yaml

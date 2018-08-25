ARG JENKINS_VERSION
FROM jenkins/jenkins:$JENKINS_VERSION-alpine
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
RUN echo $JENKINS_VERSION > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
RUN echo $JENKINS_VERSION > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion
COPY groovy/*.groovy /usr/share/jenkins/ref/init.groovy.d/

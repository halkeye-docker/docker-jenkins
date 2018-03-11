FROM jenkins/jenkins:2.110
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
RUN echo 2.110 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state
RUN echo 2.110 > /usr/share/jenkins/ref/jenkins.install.InstallUtil.lastExecVersion
COPY halkeye-plugin.hpi /usr/share/jenkins/ref/plugins/halkeye-plugin.hpi
COPY groovy/*.groovy /usr/share/jenkins/ref/init.groovy.d/

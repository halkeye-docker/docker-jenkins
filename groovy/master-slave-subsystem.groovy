import jenkins.security.s2m.AdminWhitelistRule
import jenkins.model.Jenkins
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class)
.setMasterKillSwitch(false)

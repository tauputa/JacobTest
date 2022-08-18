import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests

class PullRequestProject() : Project() {
    init {
        name = "Pull Requests"
        id(name.toId())

        buildType(PackerBuild{
            name = "teamcity-base"
            id("PR $name".toId())

            packerConfig = "."
            packerVersion = "1.7.8"

            buildVars = mapOf(
                "ami_variant" to "pr",
                "build_branch" to "%teamcity.build.branch%",
                "ami_version" to "%teamcity.build.branch%",
                "teamcity_server" to "teamcity-base"
            )

            vcs {
                root(DslContext.settingsRoot)
                branchFilter = """
                    -:<default>
                    -:refs/heads/main
                    +:pull/*
                """.trimIndent()
                cleanCheckout = true
            }

            triggers {
                vcs {
                    branchFilter = "+:refs/heads/main"
                    triggerRules = """
                        -:<default>
                        -:refs/heads/main
                        +:pull/*
                    """.trimIndent()
                }
            }
        })
    }
}
